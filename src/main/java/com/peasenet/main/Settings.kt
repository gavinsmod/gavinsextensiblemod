/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.peasenet.main

import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.google.gson.stream.JsonReader
import com.peasenet.config.*
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author gt3ch1
 * @version 03-16-2023
 * A class that contains all the settings for the mod.
 */
object Settings {
    /**
     * The list of all settings and their values.
     */
    val settings = HashMap<String, Config<*>>()
    val defaultSettings = HashMap<String, Config<*>>()
    fun getConfig(clazz: Class<out Config<*>?>?, key: String): Config<*> {
        // open the settings file
        val cfgFile = filePath
        val json = GsonBuilder().create()
        val map: Any?
        map = try {
            json.fromJson(FileReader(cfgFile), HashMap::class.java)[key]
        } catch (e: FileNotFoundException) {
            throw RuntimeException(e)
        }
        return try {
            val jsonObject = json.toJsonTree(map).asJsonObject
            json.fromJson(jsonObject, clazz)!!
        } catch (e: IllegalStateException) {
            settings[key] = defaultSettings[key]!!
            save()
            defaultSettings[key]!!
        }
    }

    /**
     * Initializes the settings.
     */
    fun initialize() {
        settings["misc"] = MiscConfig()
        settings["radar"] = RadarConfig()
        settings["esp"] = EspConfig()
        settings["tracer"] = TracerConfig()
        settings["xray"] = XrayConfig()
        settings["fullbright"] = FullbrightConfig()
        settings["fpsColors"] = FpsColorConfig()
        settings["waypoints"] = WaypointConfig()
        defaultSettings.putAll(settings)
        // check if the config file exists
        val path = filePath
        val file = File(path)
        if (!file.exists()) {
            save()
        }
        val json = GsonBuilder().setPrettyPrinting().create()
        try {
            val reader = JsonReader(InputStreamReader(FileInputStream(file)))
            val data = json.fromJson<HashMap<String, Config<*>>>(reader, HashMap::class.java)
            if (data == null) loadDefault()
            reader.close()
        } catch (ignored: Exception) {
        }
        for ((key, value) in settings) {
            settings[key] = value.readFromSettings()
        }
    }

    /**
     * Saves the current settings to mods/gavinsmod/settings.json
     */
    fun save() {
        // open the mods folder
        val cfgFile = filePath
        // ensure the settings file exists
        ensureCfgCreated(cfgFile)
        val json = GsonBuilder().setPrettyPrinting().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create()
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
            val runDir = GavinsModClient.minecraftClient.runDirectory.absolutePath
            val modsDir = "$runDir/mods"
            // ensure the gavinsmod folder exists
            val gavinsmodDir = "$modsDir/gavinsmod"
            val cfgFile = "$gavinsmodDir/settings.json"
            val gavinsModFile = File(gavinsmodDir)
            if (!gavinsModFile.exists()) {
                GavinsMod.LOGGER.info("Creating gavinsmod folder.")
                gavinsModFile.mkdir()
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