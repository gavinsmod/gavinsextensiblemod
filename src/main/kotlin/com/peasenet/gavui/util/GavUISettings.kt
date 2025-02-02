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
package com.peasenet.gavui.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.google.gson.reflect.TypeToken
import com.peasenet.gavui.GavUI
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import net.minecraft.client.MinecraftClient
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.Serializable
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * A class that contains all the settings for the mod.
 * @author GT3CH1
 * @version 02-02-2025
 * @since 01/07/2023
 */
object GavUISettings {
    /**
     * The list of all settings and their values.
     */
    private val settings = HashMap<String, Any?>()

    /**
     * The collection of default settings.
     */
    private val default_settings = HashMap<String, Any?>()

    /**
     * Initializes the settings.
     */
    fun initialize() {
        default_settings["gui.color.background"] = (Colors.BLACK)
        default_settings["gui.color.foreground"] = (Colors.WHITE)
        default_settings["gui.color.category"] = (Colors.INDIGO)
        default_settings["gui.color.enabled"] = (Colors.CYAN)
        default_settings["gui.sound"] = false
        default_settings["gui.alpha"] = 0.5f
        default_settings["gui.color.frozen"] = (Colors.RED)
        default_settings["gui.color.border"] = (Colors.WHITE)
        load()
    }

    /**
     * Saves the current settings to mods/gavui/settings.json
     */
    fun save() {
        // open the mods folder
        val cfgFile = filePath
        // ensure the settings file exists
        ensureCfgCreated(cfgFile)
        val json = GsonBuilder().setPrettyPrinting().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create()
        val map = HashMap(settings)
        try {
            val writer = Files.newBufferedWriter(Paths.get(cfgFile))
            json.toJson(map, writer)
            writer.close()
        } catch (ignored: Exception) {
        }
        load()
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
            val runDir =
                MinecraftClient.getInstance().runDirectory.absolutePath
            val modsDir = "$runDir/mods"
            // ensure the gavinsmod folder exists
            val gavinsmodDir = "$modsDir/gavui"
            val cfgFile = "$gavinsmodDir/settings.json"
            val gavinsModFile = File(gavinsmodDir)
            if (!gavinsModFile.exists()) {
                if (!gavinsModFile.mkdir()) throw RuntimeException("Could not create gavui folder.")
            }
            return cfgFile
        }

    /**
     * Loads the settings from the settings file.
     */
    fun load() {
        // open the mods folder
        val cfgFile = filePath
        // ensure the settings file exists
        ensureCfgCreated(cfgFile)
        val gson = GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create()
        try {
            val map = gson.fromJson(FileReader(cfgFile), HashMap::class.java)
            // read each default setting key and add the new value to the settings
            val jsonObject = gson.toJsonTree(default_settings)
            default_settings.keys.forEach { key ->
                if (map.containsKey(key)) {
                    settings[key] = map[key]
                } else {
                    settings[key] = gson.fromJson(jsonObject, default_settings[key]!!::class.java)
                }
            }

        } catch (e: Exception) {
            // rename settings file to settings.bak
            var bakFile = "$cfgFile.bak"
            val bakCount = 1
            // check if the backup file exists
            if (!Files.exists(Paths.get(bakFile))) {
                loadDefault()
                save()
                return
            }
            while (Files.exists(Paths.get(bakFile))) {
                bakFile = "$cfgFile.bak$bakCount"
            }
            try {
                Files.move(Paths.get(cfgFile), Paths.get(bakFile))
            } catch (e1: IOException) {
                GavUI.LOGGER.error("Error renaming settings file.")
                GavUI.LOGGER.error(e1.message)
            }
            loadDefault()
            save()
        }
    }

    /**
     * Gets the boolean value of the given setting.
     *
     * @param key - The key of the setting.
     * @return The boolean value of the setting.
     */
    fun getBool(key: String): Boolean {
        if (!settings.containsKey(key)) return false
        if (settings[key] == null) {
            settings[key] = false
            return false
        }
        return settings[key] as Boolean
    }

    /**
     * Gets the color for the given key.
     *
     * @param key - The key of the color.
     * @return The color.
     */
    fun getColor(key: String): Color {
        if (!settings.containsKey(key)) return Colors.WHITE
        val gson = Gson()
        val colorListType = object : TypeToken<Color?>() {
        }.type
        val c = gson.fromJson<Color>(settings[key].toString(), colorListType)
        return c ?: Colors.WHITE
    }

    /**
     * Loads the default configuration.
     */
    fun loadDefault() {
        settings.putAll(default_settings)
        save()
    }

    /**
     * Adds a new setting to the settings list.
     *
     * @param key   - The key of the setting.
     * @param value - The value of the setting.
     */
    fun add(key: String, value: Serializable?) {
        settings[key] = value
        save()
    }

    /**
     * Gets the float value of the given setting.
     *
     * @param s - The key of the setting.
     * @return The float value of the setting.
     */
    fun getFloat(s: String): Float {
        if (!settings.containsKey(s)) return 0f
        if (settings[s] == null) {
            settings[s] = 0
            return 0f
        }
        return settings[s].toString().toFloat()
    }
}
