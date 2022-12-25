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
import com.google.gson.JsonSyntaxException;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.peasenet.config.EspConfig;
import com.peasenet.config.TracerConfig;
import com.peasenet.config.XrayConfig;
import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;
import com.peasenet.gavui.util.GavUISettings;
import com.peasenet.mods.render.radar.Radar;
import com.peasenet.mods.render.waypoints.Waypoint;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
        default_settings.put("esp.mob.hostile.color", (Colors.RED));
        default_settings.put("esp.mob.peaceful.color", (Colors.GREEN));
        default_settings.put("esp.player.color", (Colors.YELLOW));
        default_settings.put("esp.chest.color", (Colors.PURPLE));
        default_settings.put("esp.item.color", (Colors.CYAN));

        default_settings.put("tracer.mob.hostile.color", (Colors.RED));
        default_settings.put("tracer.mob.peaceful.color", (Colors.GREEN));
        default_settings.put("tracer.player.color", (Colors.YELLOW));
        default_settings.put("tracer.chest.color", (Colors.PURPLE));
        default_settings.put("tracer.item.color", (Colors.CYAN));

        default_settings.put("misc.fps.color.enabled", false);
        default_settings.put("misc.fps.color.slow", (Colors.RED));
        default_settings.put("misc.fps.color.ok", (Colors.YELLOW));
        default_settings.put("misc.fps.color.fast", (Colors.GREEN));

        default_settings.put("misc.messages", true);

        default_settings.put("render.fullbright.gammafade", true);
        default_settings.put("render.fullbright.autofullbright", false);

        default_settings.put("xray.disable_culling", true);
        default_settings.put("xray.blocks", new ArrayList<String>());
        default_settings.put("waypoint.locations", new ArrayList<Waypoint>());
        default_settings.put("radar", new Radar());
        default_settings.put("esp", new EspConfig());
        default_settings.put("tracer", new TracerConfig());
        default_settings.put("xray", new XrayConfig());
        load();
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
        Gson gson = new GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create();
        try {
            var map = gson.fromJson(new FileReader(cfgFile), HashMap.class);
            AtomicBoolean defaultSettingLoaded = new AtomicBoolean(false);
            default_settings.forEach((k, _v) -> {
                if (map.containsKey(k))
                    settings.put(k, map.get(k));
                else {
                    settings.put(k, _v);
                    defaultSettingLoaded.set(true);
                }
            });
            if (defaultSettingLoaded.get())
                save();
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
            if (!default_settings.containsKey(key))
                return false;
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
     * Gets the integer value of the given setting.
     *
     * @param key - The key of the setting.
     * @return The integer value of the setting.
     */
    public static int getInt(String key) {
        if (!settings.containsKey(key)) {
            // check if the default settings contain the key
            if (!default_settings.containsKey(key))
                return 0;
            // add the key to the settings
            settings.put(key, default_settings.get(key));
        }
        if (settings.get(key) == null) {
            settings.put(key, 0);
            return 0;
        }
        // convert long to int
        if (settings.get(key) instanceof Long)
            return ((Long) settings.get(key)).intValue();
        return (int) settings.get(key);
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
        Color c = Colors.WHITE;
        try {
            c = gson.fromJson(settings.get(key).toString(), colorListType);
        } catch (JsonSyntaxException e) {
            loadDefault();
        } catch (NullPointerException e1) {
            GavinsMod.LOGGER.error("Color not found for key: " + key);
            // check if default settings contains the key
            if (default_settings.containsKey(key)) {
                GavinsMod.LOGGER.warn("Saving default color for key: " + key);
                // set the color to the default color
                c = (Color) default_settings.get(key);
                // set the color in the settings
                settings.put(key, c);
                save();
            } else {
                return Colors.WHITE;
            }
        }
        return c;
    }


    public static Color getColorFromEntity(Entity e, String baseKey) {
        var key = baseKey;
        if (e instanceof PlayerEntity) {
            key += ".player.color";
        } else if (e instanceof MobEntity) {
            if ((e.getType().getSpawnGroup().isPeaceful())) {
                key += ".mob.peaceful.color";
            } else {
                key += ".mob.hostile.color";
            }
        } else if (e instanceof ItemEntity) {
            key += ".item.color";
        }
        return getColor(key);
    }

    public static Color getUiColor(String key) {
        return GavUISettings.getColor(key);
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
        new File(cfgFile).renameTo(new File(bakFile));
        loadDefaultXrayBlocks();
        settings.putAll(default_settings);
        save();
    }

    /**
     * Loads the default xray blocks into the settings.
     */
    private static void loadDefaultXrayBlocks() {
        var list = new LinkedHashSet<>();
        Registries.BLOCK.stream().filter(b -> b instanceof ExperienceDroppingBlock).forEach((b -> list.add(b.toString())));
        default_settings.put("xray.blocks", list);
    }

    /**
     * Adds the given block to the list of blocks used for xray.
     *
     * @param b - The block to add.
     */
    public static void addXrayBlock(Block b) {
        var currList = getXrayBlocks();
        currList.add(b.toString());
        settings.put("xray.blocks", currList);
        save();
    }

    /**
     * Gets the list of blocks currently used for xray.
     *
     * @return The list of blocks.
     */
    public static LinkedHashSet<String> getXrayBlocks() {
        LinkedHashSet<String> list = new LinkedHashSet<>();
        try {
            list = (LinkedHashSet<String>) settings.get("xray.blocks");
        } catch (ClassCastException e) {
            var arrList = (ArrayList<String>) settings.get("xray.blocks");
            list.addAll(arrList);
        }
        if (list == null)
            return new LinkedHashSet<>();
        return list;
    }

    /**
     * Removes the given block from the list of blocks used for xray.
     *
     * @param b - The block to remove.
     */
    public static void removeXrayBlock(Block b) {
        var currList = getXrayBlocks();
        currList.remove(b.toString());
        settings.put("xray.blocks", currList);
        save();
    }

    /**
     * Gets whether the given block is in the list of blocks used for xray.
     *
     * @param b - The block to check.
     * @return Whether the block is in the list.
     */
    public static boolean isXrayBlock(Block b) {
        var currList = getXrayBlocks();

        var isInList = currList.contains(b.toString());
        return isInList;
    }

    /**
     * Adds the given waypoint to the list of waypoints.
     *
     * @param w - The waypoint to add.
     */
    public static void addWaypoint(Waypoint w) {
        var currList = getWaypoints();
        if (currList == null) currList = new ArrayList<>();
        currList.removeIf(wp -> wp.equals(w));
        w.setName(w.getName().replace(' ', '_'));
        currList.add(w);
        settings.put("waypoint.locations", currList);
        save();
    }

    /**
     * Removes the given waypoint from the list of waypoints.
     *
     * @param w - The waypoint to remove.
     */
    public static void deleteWaypoint(Waypoint w) {
        var currList = getWaypoints();
        if (currList == null) currList = new ArrayList<>();
        // remove from currList where the waypoint is the same as w
        currList.removeIf(wp -> wp.equals(w));
        settings.put("waypoint.locations", currList);
        save();
    }

    /**
     * Gets the list of waypoints.
     *
     * @return The list of waypoints.
     */
    public static ArrayList<Waypoint> getWaypoints() {

        Gson gson = new Gson();
        Type waypointType = new TypeToken<ArrayList<Waypoint>>() {
        }.getType();
        ArrayList<Waypoint> waypoints = gson.fromJson(settings.get("waypoint.locations").toString(), waypointType);
        if (waypoints == null)
            return new ArrayList<>();
        return waypoints;
    }


    public static void saveRadar() {
        settings.put("radar", Radar.getInstance());
        save();
    }

    public static Radar getRadar() {
        Gson gson = new Gson();
        Type radarType = new TypeToken<Radar>() {
        }.getType();
        Radar radar = gson.fromJson(settings.get("radar").toString(), radarType);
        if (radar == null)
            return new Radar();
        Radar.setInstance(radar);
        return radar;
    }

    public static EspConfig getEspConfig() {
        Gson gson = new Gson();
        Type espConfigType = new TypeToken<EspConfig>() {
        }.getType();
        EspConfig espConfig = gson.fromJson(settings.get("esp").toString(), espConfigType);
        if (espConfig == null)
            return new EspConfig();
        return espConfig;
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

    public static TracerConfig getTracerConfig() {
        Gson gson = new Gson();
        Type tracerConfigType = new TypeToken<TracerConfig>() {
        }.getType();
        TracerConfig tracerConfig = gson.fromJson(settings.get("tracer").toString(), tracerConfigType);
        if (tracerConfig == null)
            return new TracerConfig();
        return tracerConfig;
    }

    public static XrayConfig getXrayConfig() {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new StringReader(settings.get("xray").toString()));
        reader.setLenient(true);
        return gson.fromJson(reader, XrayConfig.class);
    }

}
