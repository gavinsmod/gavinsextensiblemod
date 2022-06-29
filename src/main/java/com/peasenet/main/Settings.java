/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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

package com.peasenet.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.peasenet.util.color.Color;
import com.peasenet.util.color.Colors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author gt3ch1
 * @version 6/27/2022
 * A class that contains all the settings for the mod.
 * Note: Retrieving a setting that does not exist will cause the game to crash.
 */
public class Settings {

    /**
     * The list of all settings and their values.
     */
    private static final HashMap<String, Object> settings = new HashMap<>();

    /**
     * Saves the current settings to mods/gavinsmod/settings.json
     */
    public static void save() {
        // open the mods folder
        var cfgFile = getFilePath();
        // ensure the settings file exists
        ensureCfgCreated(cfgFile);
        var json = new GsonBuilder().setPrettyPrinting().create();
        var map = new HashMap<>(settings);
        try {
            var writer = Files.newBufferedWriter(Paths.get(cfgFile));
            json.toJson(map, writer);
            writer.close();
        } catch (Exception e) {
            GavinsMod.LOGGER.error("Error writing settings to file.");
            GavinsMod.LOGGER.error(e.getMessage());
        }
    }

    /**
     * Ensures that the configuration file is created.
     *
     * @param cfgFile - The path to the configuration file.
     */
    private static void ensureCfgCreated(String cfgFile) {
        if (!Files.exists(Paths.get(cfgFile))) {
            try {
                Files.createFile(Paths.get(cfgFile));
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
        var runDir = GavinsModClient.getMinecraftClient().getRunDirectory().getAbsolutePath();
        var modsDir = runDir + "/mods";
        // ensure the gavinsmod folder exists
        var gavinsmodDir = modsDir + "/gavinsmod";
        var cfgFile = gavinsmodDir + "/settings.json";
        var gavinsModFile = new File(gavinsmodDir);
        if (!gavinsModFile.exists()) {
            GavinsMod.LOGGER.info("Creating gavinsmod folder.");
            gavinsModFile.mkdir();
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
        Gson gson = new Gson();
        try {
            var map = gson.fromJson(new FileReader(cfgFile), HashMap.class);
            settings.put("gammaFade", map.get("gammaFade"));
            settings.put("fpsColors", map.get("fpsColors"));
            settings.put("chatMessage", map.get("chatMessage"));
            settings.put("chestEspColor", map.get("chestEspColor"));
            settings.put("chestTracerColor", map.get("chestTracerColor"));
            settings.put("hostileMobEspColor", map.get("hostileMobEspColor"));
            settings.put("hostileMobTracerColor", map.get("hostileMobTracerColor"));
            settings.put("peacefulMobEspColor", map.get("peacefulMobEspColor"));
            settings.put("peacefulMobTracerColor", map.get("peacefulMobTracerColor"));
            settings.put("playerEspColor", map.get("playerEspColor"));
            settings.put("playerTracerColor", map.get("playerTracerColor"));
            settings.put("itemEspColor", map.get("itemEspColor"));
            settings.put("itemTracerColor", map.get("itemTracerColor"));
            settings.put("slowFpsColor", map.get("slowFpsColor"));
            settings.put("okFpsColor", map.get("okFpsColor"));
            settings.put("fastFpsColor", map.get("fastFpsColor"));
            settings.put("backgroundColor", map.get("backgroundColor"));
            settings.put("foregroundColor", map.get("foregroundColor"));
            settings.put("enabledColor", map.get("enabledColor"));
            settings.put("categoryColor", map.get("categoryColor"));
            settings.put("autoFullBright", map.get("autoFullBright"));
            settings.put("guiSounds", map.get("guiSounds"));

        } catch (Exception e) {
            GavinsMod.LOGGER.error("Error reading settings from file. Saving defaults.");
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
        if (!settings.containsKey(key))
            return false;
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
        if (!settings.containsKey(key))
            return Colors.WHITE;
        if (settings.get(key) == null) {
            settings.put(key, Colors.WHITE);
            return Colors.WHITE;
        }
        var item = settings.get(key);
        if (item instanceof Color) {
            return (Color) item;
        } else {
            try {
                return Colors.COLORS[((Double) settings.get(key)).intValue()];
            } catch (Exception e) {
                return Colors.COLORS[(Integer) settings.get(key)];
            }
        }

    }

    /**
     * Loads the default configuration.
     */
    public static void loadDefault() {
        settings.put("gammaFade", false);
        settings.put("fpsColors", false);
        settings.put("chatMessage", true);
        settings.put("chestEspColor", 6);
        settings.put("chestTracerColor", 6);
        settings.put("hostileMobEspColor", 0);
        settings.put("hostileMobTracerColor", 0);
        settings.put("peacefulMobEspColor", 2);
        settings.put("peacefulMobTracerColor", 2);
        settings.put("playerEspColor", 13);
        settings.put("playerTracerColor", 13);
        settings.put("itemEspColor", 7);
        settings.put("itemTracerColor", 7);
        settings.put("slowFpsColor", 0);
        settings.put("okFpsColor", 5);
        settings.put("fastFpsColor", 2);
        settings.put("backgroundColor", 14);
        settings.put("foregroundColor", 8);
        settings.put("enabledColor", 15);
        settings.put("categoryColor", 17);
        settings.put("autoFullBright", false);
        settings.put("guiSounds", false);
    }

    /**
     * Adds a new setting to the settings list.
     *
     * @param key   - The key of the setting.
     * @param value - The value of the setting.
     */
    public static void add(String key, Object value) {
        settings.put(key, value);
    }
}
