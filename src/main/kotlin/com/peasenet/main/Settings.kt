/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.peasenet.main

import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.google.gson.stream.JsonReader
import com.peasenet.annotations.GsonExclusionStrategy
import com.peasenet.config.Config
import com.peasenet.config.render.XrayConfig
import com.peasenet.config.render.XrayConfigGsonAdapter
import com.peasenet.util.ChatCommand
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
        .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        .registerTypeHierarchyAdapter(XrayConfig::class.java, XrayConfigGsonAdapter())
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
        val defaultSetting = defaultSettings[key]!!
        val map: Any?
        try {
            map = gson.fromJson(FileReader(cfgFile), Map::class.java)[key]
        } catch (e: FileNotFoundException) {
            ensureCfgCreated(cfgFile)
            settings[key] = defaultSetting
            GavinsMod.LOGGER.error("Error reading settings file ($key), file not found.")
            return defaultSetting
        }
        try {
            val jsonObject = gson.toJsonTree(map).asJsonObject
            return gson.fromJson(jsonObject, clazz::class.java)
        } catch (e: IllegalStateException) {
            GavinsMod.LOGGER.error("Error parsing settings file ($key): ", e)
            settings[key] = defaultSetting
            return defaultSetting
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


    @Suppress("UNCHECKED_CAST")
    fun <T : Config<*>?> getConfig(key: ChatCommand): T {
        return getConfig(key.command)
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
        var bakFile = "$cfgFile.bak"
        var bakCount = 1
        val bFile = File(bakFile)
        var renamed = false
        while (bFile.exists()) {
            bakFile = "$cfgFile.bak.$bakCount"
            bakCount++
            renamed = true
        }
        val cfg = File(cfgFile)
        if (cfg.exists() && renamed) {
            val res = cfg.renameTo(bFile)
            if (!res) throw RuntimeException(String.format("Could not rename %s to %s", cfgFile, bakFile))
        }
        save()
    }
}