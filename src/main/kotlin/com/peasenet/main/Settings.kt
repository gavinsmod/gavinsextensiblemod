package com.peasenet.main

import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.google.gson.stream.JsonReader
import com.peasenet.annotations.GsonExclusionStrategy
import com.peasenet.config.Config
import com.peasenet.config.XrayConfig
import com.peasenet.config.XrayConfigGsonAdapter
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * The core settings class for gemclient-core/gavinsmod. This class is responsible for reading, storing, adding, and saving various
 * configurations for any GEM.
 *
 * @author GT3CH1
 * @version 09-16-2024
 * @since 7-18-2023
 */
object Settings {
    /**
     * The list of all settings and their values.
     */
    val settings: HashMap<String?, Config<*>> = HashMap()

    /**
     * A set of default settings.
     */
    private val defaultSettings: HashMap<String?, Config<*>> = HashMap()

    private val gson = GsonBuilder().setExclusionStrategies(GsonExclusionStrategy())
        .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).registerTypeHierarchyAdapter(XrayConfig::class.java, XrayConfigGsonAdapter())
        .create()

    fun init() {
        defaultSettings.putAll(settings)
        // check if the config file exists
        val path = filePath
        val file = File(path)
        if (!file.exists()) {
            save()
        }
        try {
            val reader = JsonReader(InputStreamReader(FileInputStream(file)))
            val data = gson.fromJson<HashMap<String, Config<*>>>(
                reader, HashMap::class.java
            )
            if (data == null) loadDefault()
            reader.close()
        } catch (ignored: Exception) {
        }
        for ((key, value) in settings) {
            settings[key] = value.readFromSettings()
        }
    }

    /**
     * Fetches a configuration from the settings file.
     * <pre>
     * `var cfg = Settings.fetchConfig(TracerConfig.class, "tracer");
     * ...
    ` *
    </pre> *
     *
     * @param clazz - The class of the configuration.
     * @param key   - The key of the configuration.
     * @return - The configuration.
     */
    private fun fetchConfig(clazz: Config<*>, key: String?): Config<*> {
        // open the settings file
         val cfgFile = filePath

        val map: Any?
        try {
            map = gson.fromJson(FileReader(cfgFile), Map::class.java)[key]
        } catch (e: FileNotFoundException) {
            ensureCfgCreated(cfgFile)
            settings[key] = defaultSettings[key]!!
            GavinsMod.LOGGER.error("Error reading settings file ($key), file not found.")
//            save()
            return defaultSettings[key]!!
        }
        try {
            val jsonObject = gson.toJsonTree(map).asJsonObject
            // parse the json object to the configuration class
            return gson.fromJson(jsonObject, clazz::class.java)
        } catch (e: IllegalStateException) {
            GavinsMod.LOGGER.error("Error parsing settings file ($key): ", e)
            settings[key] = defaultSettings[key]!!
//            save()
            return defaultSettings[key]!!
        }
    }

    /**
     * Saves the current settings to <pre>mods/gavinsmod/settings.json</pre>
     */
    fun save() {
        val cfgFile = filePath
        ensureCfgCreated(cfgFile)
        val json = GsonBuilder().setPrettyPrinting().setObjectToNumberStrategy(
            ToNumberPolicy.LONG_OR_DOUBLE
        ).registerTypeAdapter(XrayConfig::class.java, XrayConfigGsonAdapter())
            .setExclusionStrategies(GsonExclusionStrategy()).create()
        try {
            val writer = Files.newBufferedWriter(Paths.get(cfgFile))
            json.toJson(settings, writer)
            writer.close()
            GavinsMod.LOGGER.info("Settings saved.")
        } catch (e: Exception) {
            GavinsMod.LOGGER.error("Error writing settings to file.")
            GavinsMod.LOGGER.error(e.message)
        }
    }

    /**
     * Adds a config to the settings.
     * <pre>
     * @code {
     * Settings.addConfig(new TracerConfig());
     * }
    </pre> *
     *
     * @param config - The config to add.
     */
    fun addConfig(config: Config<*>) {
        defaultSettings[config.key] = config
        settings[config.key] = fetchConfig(config, config.key)
    }

    /**
     * Fetches and mutates a configuration from settings.
     * <pre>
     * `var cfg = Settings.getConfig<TracerConfig>("tracer");
     * cfg.beehiveTracerColor = ...
    ` *
    </pre> *
     *
     * @param key - The key of the configuration.
     * @param <T> - The type of the configuration.
     * @return The configuration with the given key.
    </T> */
    @Suppress("UNCHECKED_CAST")
    fun <T : Config<*>?> getConfig(key: String?): T {
        return settings[key] as T
    }

    /**
     * Ensures that the configuration file is created.
     *
     * @param cfgFile - The path to the configuration file.
     */
    private fun ensureCfgCreated(cfgFile: String) {
        val cfg = Path.of(cfgFile)
        if (!Files.exists(cfg)) {
            try {
                Files.createFile(cfg)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    private val filePath: String
        /**
         * Gets the file path to the settings file.
         */
        get() {
            val modDir = "${GavinsModClient.minecraftClient.runDirectory.absolutePath}/mods/gavinsmod"
            val cfgFile = "$modDir/settings.json"
            val settingsDir = File(modDir)
            if (!settingsDir.exists()) {
                GavinsMod.LOGGER.info("Creating gavinsmod folder.")
                settingsDir.mkdir()
            }
            // convert cfgFile path to correct path separator
            return Paths.get(cfgFile).toString()
        }

    /**
     * Loads the default configuration.
     */
    private fun loadDefault() {
        GavinsMod.LOGGER.warn("Loading default settings.")
        val cfgFile = filePath
        // rename settings file to settings.bak
        var bakFile = "$cfgFile.bak"
        var bakCount = 1
        // if bak file exists, rename it to settings.bak.1, settings.bak.2, etc.
        val bFile = File(bakFile)
        var renamed = false
        while (bFile.exists()) {
            bakFile = "$cfgFile.bak.$bakCount"
            bakCount++
            renamed = true
        }
        // move the settings file to the bak file
        // check if the settings file exists
        val cfg = File(cfgFile)
        if (cfg.exists() && renamed) {
            val res = cfg.renameTo(bFile)
            if (!res) throw RuntimeException(String.format("Could not rename %s to %s", cfgFile, bakFile))
        }
        save()
    }
}