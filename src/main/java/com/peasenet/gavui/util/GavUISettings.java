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

package com.peasenet.gavui.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;
import com.peasenet.gavui.GavUI;
import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author gt3ch1
 * @version 01/07/2023
 * A class that contains all the settings for the mod.
 */
public class GavUISettings {

    /**
     * The list of all settings and their values.
     */
    private static final HashMap<String, Object> settings = new HashMap<>();

    /**
     * The collection of default settings.
     */
    private static final HashMap<String, Object> default_settings = new HashMap<>();

    /**
     * Initializes and loads the configuration file. If the file does not exist, it will be created.
     * If the load fails, the default settings will be used.
     */
    private GavUISettings() {

    }

    /**
     * Initializes the settings.
     */
    public static void initialize() {
        default_settings.put("gui.color.background", (Colors.BLACK));
        default_settings.put("gui.color.foreground", (Colors.WHITE));
        default_settings.put("gui.color.category", (Colors.INDIGO));
        default_settings.put("gui.color.enabled", (Colors.CYAN));
        default_settings.put("gui.sound", false);
        default_settings.put("gui.alpha", 0.5f);
        default_settings.put("gui.color.frozen", (Colors.RED));
        default_settings.put("gui.color.border", (Colors.WHITE));
        load();
    }

    /**
     * Saves the current settings to mods/gavui/settings.json
     */
    public static void save() {
        // open the mods folder
        var cfgFile = getFilePath();
        // ensure the settings file exists
        ensureCfgCreated(cfgFile);
        var json = new GsonBuilder().setPrettyPrinting().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create();
        var map = new HashMap<>(settings);
        try {
            var writer = Files.newBufferedWriter(Paths.get(cfgFile));
            json.toJson(map, writer);
            writer.close();
        } catch (Exception ignored) {

        }
        load();
    }

    /**
     * Ensures that the configuration file is created.
     *
     * @param cfgFile - The path to the configuration file.
     */
    private static void ensureCfgCreated(String cfgFile) {
        Path cfg = Path.of(cfgFile);
        if (!Files.exists(cfg)) {
            try {
                Files.createFile(cfg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Gets the file path to the settings file.
     */
    @NotNull
    private static String getFilePath() {
        var runDir = MinecraftClient.getInstance().runDirectory.getAbsolutePath();
        var modsDir = runDir + "/mods";
        // ensure the gavinsmod folder exists
        var gavinsmodDir = modsDir + "/gavui";
        var cfgFile = gavinsmodDir + "/settings.json";
        var gavinsModFile = new File(gavinsmodDir);
        if (!gavinsModFile.exists()) {
            if (!gavinsModFile.mkdir())
                throw new RuntimeException("Could not create gavui folder.");
        }
        return cfgFile;
    }

    /**
     * Loads the settings from the settings file.
     */
    public static void load() {
        // open the mods folder
        var cfgFile = getFilePath();
        // ensure the settings file exists
        ensureCfgCreated(cfgFile);
        Gson gson = new GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create();
        try {
            var map = gson.fromJson(new FileReader(cfgFile), HashMap.class);

            AtomicBoolean wasNull = new AtomicBoolean(false);
            default_settings.forEach((k, _v) -> {
                var val = map.get(k);
                if (val == null) {
                    wasNull.set(true);
                    val = default_settings.get(k);
                }
                settings.put(k, val);
            });
            if (wasNull.get()) {
                save();
            }
        } catch (Exception e) {
            // rename settings file to settings.bak
            var bakFile = cfgFile + ".bak";
            int bakCount = 1;
            // check if the backup file exists
            if (!Files.exists(Paths.get(bakFile))) {
                loadDefault();
                save();
                return;
            }
            while (Files.exists(Paths.get(bakFile))) {
                bakFile = cfgFile + ".bak" + bakCount;
            }
            try {
                Files.move(Paths.get(cfgFile), Paths.get(bakFile));
            } catch (IOException e1) {
                GavUI.LOGGER.error("Error renaming settings file.");
                GavUI.LOGGER.error(e1.getMessage());
            }
            loadDefault();
            save();
        }
    }

    /**
     * Gets the boolean value of the given setting.
     *
     * @param key - The key of the setting.
     * @return The boolean value of the setting.
     */
    public static boolean getBool(String key) {
        if (!settings.containsKey(key)) return false;
        if (settings.get(key) == null) {
            settings.put(key, false);
            return false;
        }
        return (boolean) settings.get(key);
    }

    /**
     * Gets the color for the given key.
     *
     * @param key - The key of the color.
     * @return The color.
     */
    public static Color getColor(String key) {
        if (!settings.containsKey(key)) return Colors.WHITE;
        Gson gson = new Gson();
        Type colorListType = new TypeToken<Color>() {
        }.getType();
        Color c = gson.fromJson(settings.get(key).toString(), colorListType);
        return c;
    }

    /**
     * Loads the default configuration.
     */
    public static void loadDefault() {
        settings.putAll(default_settings);
        save();
    }

    /**
     * Adds a new setting to the settings list.
     *
     * @param key   - The key of the setting.
     * @param value - The value of the setting.
     */
    public static void add(String key, Serializable value) {
        settings.put(key, value);
        save();
    }

    /**
     * Gets the float value of the given setting.
     *
     * @param s - The key of the setting.
     * @return The float value of the setting.
     */
    public static float getFloat(String s) {
        if (!settings.containsKey(s)) return 0;
        if (settings.get(s) == null) {
            settings.put(s, 0);
            return 0;
        }
        return Float.parseFloat(settings.get(s).toString());
    }
}
