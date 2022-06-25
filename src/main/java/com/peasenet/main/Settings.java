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
 * @version 6/18/2022
 * A class that contains all the settings for the mod.
 */
public class Settings {

    public static boolean GammaFade = false;
    public static boolean FpsColors = true;
    public static boolean ChatMessage = true;
    public static boolean AutoFullBright = true;
    public static boolean GuiSounds = true;

    /*
     * Colors
     */
    public static Color ChestEspColor = Colors.PURPLE;
    public static Color ChestTracerColor = Colors.PURPLE;
    public static Color HostileMobEspColor = Colors.RED;
    public static Color HostileMobTracerColor = Colors.RED;
    public static Color PeacefulMobEspColor = Colors.GREEN;
    public static Color PeacefulMobTracerColor = Colors.GREEN;
    public static Color PlayerEspColor = Colors.GOLD;
    public static Color PlayerTracerColor = Colors.GOLD;
    public static Color ItemEspColor = Colors.CYAN;
    public static Color ItemTracerColor = Colors.CYAN;
    public static Color SlowFpsColor = Colors.RED;
    public static Color OkFpsColor = Colors.YELLOW;
    public static Color FastFpsColor = Colors.GREEN;
    public static Color BackgroundColor = Colors.INDIGO;
    public static Color ForegroundColor = Colors.WHITE;
    public static Color EnabledColor = Colors.SHADOW_BLUE;
    public static Color CategoryColor = Colors.MEDIUM_SEA_GREEN;

    /**
     * Saves the current settings to mods/gavinsmod/settings.json
     */
    public static void save() {
        // open the mods folder
        var cfgFile = getFilePath();
        // ensure the settings file exists
        ensureCfgCreated(cfgFile);
        var map = new HashMap<String, Object>();
        var json = new GsonBuilder().setPrettyPrinting().create();
        map.put("gammaFade", GammaFade);
        map.put("fpsColors", FpsColors);
        map.put("chatMessage", ChatMessage);
        map.put("chestEspColor", Colors.getColorIndex(ChestEspColor));
        map.put("chestTracerColor", Colors.getColorIndex(ChestTracerColor));
        map.put("hostileMobEspColor", Colors.getColorIndex(HostileMobEspColor));
        map.put("hostileMobTracerColor", Colors.getColorIndex(HostileMobTracerColor));
        map.put("peacefulMobEspColor", Colors.getColorIndex(PeacefulMobEspColor));
        map.put("peacefulMobTracerColor", Colors.getColorIndex(PeacefulMobTracerColor));
        map.put("playerEspColor", Colors.getColorIndex(PlayerEspColor));
        map.put("playerTracerColor", Colors.getColorIndex(PlayerTracerColor));
        map.put("itemEspColor", Colors.getColorIndex(ItemEspColor));
        map.put("itemTracerColor", Colors.getColorIndex(ItemTracerColor));
        map.put("slowFpsColor", Colors.getColorIndex(SlowFpsColor));
        map.put("okFpsColor", Colors.getColorIndex(OkFpsColor));
        map.put("fastFpsColor", Colors.getColorIndex(FastFpsColor));
        map.put("backgroundColor", Colors.getColorIndex(BackgroundColor));
        map.put("foregroundColor", Colors.getColorIndex(ForegroundColor));
        map.put("enabledColor", Colors.getColorIndex(EnabledColor));
        map.put("categoryColor", Colors.getColorIndex(CategoryColor));
        map.put("autoFullBright", AutoFullBright);
        map.put("guiSounds", GuiSounds);
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
            GammaFade = (boolean) map.get("gammaFade");
            FpsColors = (boolean) map.get("fpsColors");
            ChatMessage = (boolean) map.get("chatMessage");
            ChestEspColor = Colors.COLORS[((Double) map.get("chestEspColor")).intValue()];
            ChestTracerColor = Colors.COLORS[((Double) map.get("chestTracerColor")).intValue()];
            HostileMobEspColor = Colors.COLORS[((Double) map.get("hostileMobEspColor")).intValue()];
            HostileMobTracerColor = Colors.COLORS[((Double) map.get("hostileMobTracerColor")).intValue()];
            PeacefulMobEspColor = Colors.COLORS[((Double) map.get("peacefulMobEspColor")).intValue()];
            PeacefulMobTracerColor = Colors.COLORS[((Double) map.get("peacefulMobTracerColor")).intValue()];
            PlayerEspColor = Colors.COLORS[((Double) map.get("playerEspColor")).intValue()];
            PlayerTracerColor = Colors.COLORS[((Double) map.get("playerTracerColor")).intValue()];
            ItemEspColor = Colors.COLORS[((Double) map.get("itemEspColor")).intValue()];
            ItemTracerColor = Colors.COLORS[((Double) map.get("itemTracerColor")).intValue()];
            SlowFpsColor = Colors.COLORS[((Double) map.get("slowFpsColor")).intValue()];
            OkFpsColor = Colors.COLORS[((Double) map.get("okFpsColor")).intValue()];
            FastFpsColor = Colors.COLORS[((Double) map.get("fastFpsColor")).intValue()];
            BackgroundColor = Colors.COLORS[((Double) map.get("backgroundColor")).intValue()];
            ForegroundColor = Colors.COLORS[((Double) map.get("foregroundColor")).intValue()];
            EnabledColor = Colors.COLORS[((Double) map.get("enabledColor")).intValue()];
            CategoryColor = Colors.COLORS[((Double) map.get("categoryColor")).intValue()];
            AutoFullBright = (boolean) map.get("autoFullBright");
            GuiSounds = (boolean) map.get("guiSounds");
        } catch (Exception e) {
            GavinsMod.LOGGER.error("Error reading settings from file. Saving defaults.");
            save();
        }
    }
}
