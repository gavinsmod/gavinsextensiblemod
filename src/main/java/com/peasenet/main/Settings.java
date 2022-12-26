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

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.peasenet.annotations.Exclude;
import com.peasenet.config.*;
import com.peasenet.mods.render.waypoints.Waypoint;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author gt3ch1
 * @version 7/5/2022
 * A class that contains all the settings for the mod.
 */
public class Settings {

    /**
     * The list of all settings and their values.
     */
    public static final HashMap<String, Object> settings = new HashMap<>();

    /**
     * The collection of default settings.
     */
    private static final HashMap<String, Object> default_settings = new HashMap<>();

    /**
     * Initializes and loads the configuration file. If the file does not exist, it will be created.
     * If the load fails, the default settings will be used.
     */
    private Settings() {
        default_settings.put("misc.messages", true);
        default_settings.put("waypoint.locations", new ArrayList<Waypoint>());
        default_settings.put("radar", new RadarConfig());
        default_settings.put("esp", new EspConfig());
        default_settings.put("tracer", new TracerConfig());
        default_settings.put("xray", new XrayConfig());
        default_settings.put("fullbright", new FullbrightConfig());
        default_settings.put("fpsColors", new FpsColorConfig());
        default_settings.put("waypoints", new WaypointConfig());
        load();
    }

    @SuppressWarnings("rawtypes")
    public static Config getConfig(Class<? extends Config> clazz, String key) {
        var gson = new GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .setExclusionStrategies(new AnnotationExclusionStrategy())
                .create();
        var cfg = settings.get(key).toString();
        JsonReader reader = new JsonReader(new StringReader(cfg));
        reader.setLenient(true);
        return gson.fromJson(reader, clazz);
    }

    /**
     * Initializes the settings.
     */
    public static void initialize() {
        new Settings();
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
        var map = new HashMap<>(settings);
        try {
            var writer = Files.newBufferedWriter(Paths.get(cfgFile));
            json.toJson(map, writer);
            writer.close();
        } catch (Exception e) {
            GavinsMod.LOGGER.error("Error writing settings to file.");
            GavinsMod.LOGGER.error(e.getMessage());
        }
//        load();
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
        // https://stackoverflow.com/questions/4802887/gson-how-to-exclude-specific-fields-from-serialization-without-annotations/27986860#27986860

        Gson gson = new GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .setExclusionStrategies(new AnnotationExclusionStrategy())
                .create();
        settings.clear();
        try {
            var map = gson.fromJson(new FileReader(cfgFile), HashMap.class);
            AtomicBoolean defaultSettingLoaded = new AtomicBoolean(false);
            default_settings.forEach((k, _v) -> {
                if (map.containsKey(k))
                    settings.put(k, map.get(k));
                else {
                    settings.put(k, _v);
                    if (_v instanceof Config c) {
                        settings.put(k, c.getInstance());
                    }
                    defaultSettingLoaded.set(true);
                }
            });
            if (defaultSettingLoaded.get()) {
                save();
                load();
            }
        } catch (Exception e) {
            GavinsMod.LOGGER.error("Error reading settings from file. Saving defaults.");
            loadDefault();
        }
    }

    /**
     * Gets the boolean value of the given setting.
     *
     * @param key - The key of the setting.
     * @return The boolean value of the setting.
     */
    public static boolean getBool(String key) {
        if (!settings.containsKey(key)) {
            // check if the default settings contain the key
            if (!default_settings.containsKey(key)) return false;
            // add the key to the settings
            settings.put(key, default_settings.get(key));
        }
        if (settings.get(key) == null) {
            settings.put(key, false);
            return false;
        }
        return (boolean) settings.get(key);
    }

    /**
     * Loads the default configuration.
     */
    public static void loadDefault() {
        var cfgFile = getFilePath();
        // rename settings file to settings.bak
        var bakFile = cfgFile + ".bak";
        int bakCount = 1;
        // if bak file exists, rename it to settings.bak.1, settings.bak.2, etc.
        while (new File(bakFile).exists()) {
            bakFile = cfgFile + ".bak." + bakCount;
            bakCount++;
        }
        // move the settings file to the bak file
        var res = new File(cfgFile).renameTo(new File(bakFile));
        if (!res) throw new RuntimeException(String.format("Could not rename %s to %s", cfgFile, bakFile));
        settings.putAll(default_settings);
        save();
    }

    /**
     * Sets the given key to the given value.
     *
     * @param key   - The key to set.
     * @param value - The value to set.
     */
    public static void setBool(String key, boolean value) {
        settings.put(key, value);
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

    public static class AnnotationExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(Exclude.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

}
