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

package com.peasenet.main;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.ToNumberPolicy;
import com.google.gson.stream.JsonReader;
import com.peasenet.config.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author gt3ch1
 * @version 7/5/2022
 * A class that contains all the settings for the mod.
 */
public class Settings {

    /**
     * The list of all settings and their values.
     */
    public static final HashMap<String, Config<?>> settings = new HashMap<>();
    public static final HashMap<String, Config<?>> defaultSettings = new HashMap<>();

    @SuppressWarnings("rawtypes")
    public static Config getConfig(Class<? extends Config> clazz, String key) {
        // open the settings file
        var cfgFile = getFilePath();
        var json = new GsonBuilder().create();
        Object map;
        try {
            map = json.fromJson(new FileReader(cfgFile), HashMap.class).get(key);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            JsonObject jsonObject = json.toJsonTree(map).getAsJsonObject();
            return json.fromJson(jsonObject, clazz);
        } catch (IllegalStateException e) {
            settings.put(key, defaultSettings.get(key));
            save();
            return defaultSettings.get(key);
        }
    }

    /**
     * Initializes the settings.
     */
    public static void initialize() {
        settings.put("misc", new MiscConfig());
        settings.put("radar", new RadarConfig());
        settings.put("esp", new EspConfig());
        settings.put("tracer", new TracerConfig());
        settings.put("xray", new XrayConfig());
        settings.put("fullbright", new FullbrightConfig());
        settings.put("fpsColors", new FpsColorConfig());
        settings.put("waypoints", new WaypointConfig());
        defaultSettings.putAll(settings);
        // check if the config file exists
        var path = getFilePath();
        var file = new File(path);
        if (!file.exists()) {
            save();
        }
        var json = new GsonBuilder().setPrettyPrinting().create();
        try {
            var reader = new JsonReader(new InputStreamReader(new FileInputStream(file)));
            HashMap<String, Config<?>> data = json.fromJson(reader, HashMap.class);
            if (data == null)
                loadDefault();
            reader.close();
        } catch (Exception ignored) {
        }
        for (var entry : settings.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            settings.put(key, value.readFromSettings());
        }
    }

    /**
     * Saves the current settings to mods/gavinsmod/settings.json
     */
    public static void save() {
        // open the mods folder
        var cfgFile = getFilePath();
        // ensure the settings file exists
        ensureCfgCreated(cfgFile);
        var json = new GsonBuilder().setPrettyPrinting().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create();
        try {
            var writer = Files.newBufferedWriter(Paths.get(cfgFile));
            json.toJson(settings, writer);
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
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @NotNull
    private static String getFilePath() {
        var runDir = GavinsModClient.Companion.getMinecraftClient().getRunDirectory().getAbsolutePath();
        var modsDir = runDir + "/mods";
        // ensure the gavinsmod folder exists
        var gavinsmodDir = modsDir + "/gavinsmod";
        var cfgFile = gavinsmodDir + "/settings.json";
        var gavinsModFile = new File(gavinsmodDir);
        if (!gavinsModFile.exists()) {
            GavinsMod.LOGGER.info("Creating gavinsmod folder.");
            gavinsModFile.mkdir();
        }
        // convert cfgFile path to correct path separator
        return Paths.get(cfgFile).toString();
    }

    /**
     * Loads the default configuration.
     */
    public static void loadDefault() {

        GavinsMod.LOGGER.warn("Loading default settings.");
        var cfgFile = getFilePath();
        // rename settings file to settings.bak
        var bakFile = cfgFile + ".bak";
        int bakCount = 1;
        // if bak file exists, rename it to settings.bak.1, settings.bak.2, etc.
        var bFile = new File(bakFile);
        var renamed = false;
        while (bFile.exists()) {
            bakFile = cfgFile + ".bak." + bakCount;
            bakCount++;
            renamed = true;
        }
        // move the settings file to the bak file
        // check if the settings file exists
        var cfg = new File(cfgFile);
        if (cfg.exists() && renamed) {
            var res = cfg.renameTo(bFile);
            if (!res) throw new RuntimeException(String.format("Could not rename %s to %s", cfgFile, bakFile));
        }
        save();
    }
}
