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

import com.peasenet.util.color.Color;
import com.peasenet.util.color.Colors;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author gt3ch1
 * @version 6/18/2022
 * A class that contains all the settings for the mod.
 */
public class Settings {

    public static boolean GammaFade = false;
    public static boolean FpsColors = true;
    public static boolean ChatMessage = true;

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

    public static void save() {
        // open the mods folder
        File cfgFile = getFile();
        // ensure the settings file exists
        if (!cfgFile.exists()) {
            try {
                cfgFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        JSONObject json = new JSONObject();
        json.put("gammaFade", GammaFade);
        json.put("fpsColors", FpsColors);
        json.put("chatMessage", ChatMessage);
        json.put("chestEspColor", Colors.getColorIndex(ChestEspColor));
        json.put("chestTracerColor", Colors.getColorIndex(ChestTracerColor));
        json.put("hostileMobEspColor", Colors.getColorIndex(HostileMobEspColor));
        json.put("hostileMobTracerColor", Colors.getColorIndex(HostileMobTracerColor));
        json.put("peacefulMobEspColor", Colors.getColorIndex(PeacefulMobEspColor));
        json.put("peacefulMobTracerColor", Colors.getColorIndex(PeacefulMobTracerColor));
        json.put("playerEspColor", Colors.getColorIndex(PlayerEspColor));
        json.put("playerTracerColor", Colors.getColorIndex(PlayerTracerColor));
        json.put("itemEspColor", Colors.getColorIndex(ItemEspColor));
        json.put("itemTracerColor", Colors.getColorIndex(ItemTracerColor));
        json.put("slowFpsColor", Colors.getColorIndex(SlowFpsColor));
        json.put("okFpsColor", Colors.getColorIndex(OkFpsColor));
        json.put("fastFpsColor", Colors.getColorIndex(FastFpsColor));
        json.put("backgroundColor", Colors.getColorIndex(BackgroundColor));
        json.put("foregroundColor", Colors.getColorIndex(ForegroundColor));
        json.put("enabledColor", Colors.getColorIndex(EnabledColor));
        json.put("categoryColor", Colors.getColorIndex(CategoryColor));
        try {
            PrintWriter writer = new PrintWriter(cfgFile, "UTF-8");
            writer.println(json.toJSONString());
            writer.close();
        } catch (Exception e) {
            GavinsMod.LOGGER.error("Error writing settings to file.");
            GavinsMod.LOGGER.error(e.getMessage());
        }
    }

    @NotNull
    private static File getFile() {
        var runDir = GavinsModClient.getMinecraftClient().getRunDirectory().getAbsolutePath();
        var modsDir = runDir + "/mods";
        // ensure the gavinsmod folder exists
        var gavinsmodDir = modsDir + "/gavinsmod";
        var cfgFile = new File(gavinsmodDir + "/settings.json");
        var gavinsModFile = new File(gavinsmodDir);
        if (!gavinsModFile.exists()) {
            GavinsMod.LOGGER.info("Creating gavinsmod folder.");
            gavinsModFile.mkdir();
        }
        return cfgFile;
    }

    public static void load() {
        // open the mods folder
        File cfgFile = getFile();
        // ensure the settings file exists
        if (!cfgFile.exists()) {
            try {
                cfgFile.createNewFile();
                GavinsMod.LOGGER.info("Created settings file.");
                save();
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        JSONObject json;
        try {
            json = (JSONObject) org.json.simple.JSONValue.parse(new FileReader(cfgFile));
            GammaFade = (boolean) json.get("gammaFade");
            FpsColors = (boolean) json.get("fpsColors");
            ChatMessage = (boolean) json.get("chatMessage");
            ChestEspColor = Colors.COLORS[((Long) json.get("chestEspColor")).intValue()];
            ChestTracerColor = Colors.COLORS[((Long) json.get("chestTracerColor")).intValue()];
            HostileMobEspColor = Colors.COLORS[((Long) json.get("hostileMobEspColor")).intValue()];
            HostileMobTracerColor = Colors.COLORS[((Long) json.get("hostileMobTracerColor")).intValue()];
            PeacefulMobEspColor = Colors.COLORS[((Long) json.get("peacefulMobEspColor")).intValue()];
            PeacefulMobTracerColor = Colors.COLORS[((Long) json.get("peacefulMobTracerColor")).intValue()];
            PlayerEspColor = Colors.COLORS[((Long) json.get("playerEspColor")).intValue()];
            PlayerTracerColor = Colors.COLORS[((Long) json.get("playerTracerColor")).intValue()];
            ItemEspColor = Colors.COLORS[((Long) json.get("itemEspColor")).intValue()];
            ItemTracerColor = Colors.COLORS[((Long) json.get("itemTracerColor")).intValue()];
            SlowFpsColor = Colors.COLORS[((Long) json.get("slowFpsColor")).intValue()];
            OkFpsColor = Colors.COLORS[((Long) json.get("okFpsColor")).intValue()];
            FastFpsColor = Colors.COLORS[((Long) json.get("fastFpsColor")).intValue()];
            BackgroundColor = Colors.COLORS[((Long) json.get("backgroundColor")).intValue()];
            ForegroundColor = Colors.COLORS[((Long) json.get("foregroundColor")).intValue()];
            EnabledColor = Colors.COLORS[((Long) json.get("enabledColor")).intValue()];
            CategoryColor = Colors.COLORS[((Long) json.get("categoryColor")).intValue()];
        } catch (Exception e) {
            GavinsMod.LOGGER.error("Error reading settings from file. Saving defaults.");
            save();
        }
    }
}
