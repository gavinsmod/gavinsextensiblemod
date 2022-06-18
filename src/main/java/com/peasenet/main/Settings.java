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

/**
 * @author gt3ch1
 * @version 6/18/2022
 * A class that contains all the settings for the mod.
 */
public class Settings {

    public static boolean FullbrightFade = false;
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


}
