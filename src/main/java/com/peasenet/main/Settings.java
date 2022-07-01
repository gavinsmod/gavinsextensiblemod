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
import com.google.gson.ToNumberPolicy;
import com.google.gson.internal.LinkedTreeMap;
import com.peasenet.mods.render.waypoints.Waypoint;
import com.peasenet.util.color.Color;
import com.peasenet.util.color.Colors;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

    private static final HashMap<String, Object> default_settings = new HashMap<>();

    private Settings() {
        default_settings.put("esp.mob.hostile.color", Colors.getColorIndex(Colors.RED));
        default_settings.put("esp.mob.peaceful.color", Colors.getColorIndex(Colors.GREEN));
        default_settings.put("esp.player.color", Colors.getColorIndex(Colors.YELLOW));
        default_settings.put("esp.chest.color", Colors.getColorIndex(Colors.PURPLE));
        default_settings.put("esp.item.color", Colors.getColorIndex(Colors.CYAN));

        default_settings.put("tracer.mob.hostile.color", Colors.getColorIndex(Colors.RED));
        default_settings.put("tracer.mob.peaceful.color", Colors.getColorIndex(Colors.GREEN));
        default_settings.put("tracer.player.color", Colors.getColorIndex(Colors.YELLOW));
        default_settings.put("tracer.chest.color", Colors.getColorIndex(Colors.PURPLE));
        default_settings.put("tracer.item.color", Colors.getColorIndex(Colors.CYAN));

        default_settings.put("gui.color.background", Colors.getColorIndex(Colors.INDIGO));
        default_settings.put("gui.color.foreground", Colors.getColorIndex(Colors.WHITE));
        default_settings.put("gui.color.category", Colors.getColorIndex(Colors.DARK_SPRING_GREEN));
        default_settings.put("gui.color.enabled", Colors.getColorIndex(Colors.MEDIUM_SEA_GREEN));
        default_settings.put("gui.sound", false);

        default_settings.put("misc.fps.color.enabled", false);
        default_settings.put("misc.fps.color.slow", Colors.getColorIndex(Colors.RED));
        default_settings.put("misc.fps.color.ok", Colors.getColorIndex(Colors.YELLOW));
        default_settings.put("misc.fps.color.fast", Colors.getColorIndex(Colors.GREEN));

        default_settings.put("misc.messages", true);

        default_settings.put("render.fullbright.gammafade", true);
        default_settings.put("render.fullbright.autofullbright", false);

        default_settings.put("xray.disable_culling", true);
        default_settings.put("xray.blocks", new ArrayList<String>());
        default_settings.put("waypoint.locations", new ArrayList<Waypoint>());
        default_settings.put("waypoint.locations.enabled", new HashSet<Integer>());
        load();
    }

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
        Gson gson = new GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create();
        try {
            var map = gson.fromJson(new FileReader(cfgFile), HashMap.class);
            default_settings.forEach((k, _v) -> settings.put(k, map.get(k)));
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
        loadDefaultXrayBlocks();
        settings.putAll(default_settings);
        save();
    }


    //TODO: Doc comments
    private static void loadDefaultXrayBlocks() {
        var list = new ArrayList<String>();
        Registry.BLOCK.stream().filter(b -> b instanceof OreBlock).toList().forEach(b -> list.add(b.getLootTableId().toString()));
        default_settings.put("xray.blocks", list);
    }

    public static void addXrayBlock(Block b) {
        var currList = (ArrayList<String>) settings.get("xray.blocks");
        currList.add(b.getLootTableId().toString());
        settings.put("xray.blocks", currList);
        save();
    }

    public static void removeXrayBlock(Block b) {
        var currList = (ArrayList<String>) settings.get("xray.blocks");
        currList.remove(b.getLootTableId().toString());
        settings.put("xray.blocks", currList);
        save();
    }

    public static boolean isXrayBlock(Block b) {
        var currList = (ArrayList<Waypoint>) settings.get("xray.blocks");
        if (currList == null) return false;
        var isInList = currList.contains(b.getLootTableId().toString());
        return isInList;
    }

    public static void addWaypoint(Waypoint w) {
        var currList = (ArrayList<Waypoint>) settings.get("waypoint.locations");
        if (currList == null) currList = new ArrayList<>();
        currList.add(w);
        settings.put("waypoint.locations", currList);
        save();
    }

    public static Waypoint getWaypoint(Waypoint w) {
        return getWaypoints().stream().filter(w2 -> w2.hashCode() == w.hashCode()).findFirst().orElse(null);
    }

    public static void removeWaypoint(Waypoint w) {
        var currList = (ArrayList<Waypoint>) settings.get("waypoint.locations");
        if (currList == null) return;
        currList.remove(w);
        settings.put("waypoint.locations", currList);
        save();
    }

    public static ArrayList<Waypoint> getWaypoints() {
        // note : need to figurte out way to convert from LinkedTreeMap to ArrayList<Waypoint> nicer.
        try {
            var list = (ArrayList<LinkedTreeMap>) settings.get("waypoint.locations");
            var wpList = new ArrayList<Waypoint>();
            list.forEach(l -> {
                        var x = ((Long) l.get("x")).intValue();
                        var y = ((Long) l.get("y")).intValue();
                        var z = ((Long) l.get("z")).intValue();
                        var w = new Waypoint(x, y, z);
                        w.setEnabled((Boolean) l.get("enabled"));
                        wpList.add(w);
                    }
            );
            return wpList;
        } catch (ClassCastException e) {
            return (ArrayList<Waypoint>) settings.get("waypoint.locations");
        }
    }

    public static void setWaypointState(Waypoint w, boolean state) {
        w.setEnabled(state);
        var currList = getWaypoints();
        // find the waypoint in the list and set it's state
        for (int i = 0; i < currList.size(); i++) {
            if (currList.get(i).hashCode() == w.hashCode()) {
                currList.get(i).setEnabled(state);
                break;
            }
        }
        settings.put("waypoint.locations", currList);
        save();
    }

    public static void setBool(String key, boolean value) {
        settings.put(key, value);
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
