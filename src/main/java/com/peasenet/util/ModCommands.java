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

package com.peasenet.util;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.Mods;
import com.peasenet.mods.Mod;

/**
 * @author gt3ch1
 * @version 6/28/2022
 * A class that handles chat commands for all mods.
 */
public class ModCommands {
    /**
     * Checks if the command is a mod command. A mod command is a string of text that starts with the prefix "."
     * followed by the name of the mod (an example would be ".fly" to toggle the fly mod). If it is, the given mod
     * is toggled on or off.
     *
     * @param message The message to check.
     * @return True if the message is a mod command, false otherwise.
     */
    public static boolean handleCommand(String message) {
        // remove the . from the message
        message = message.substring(1);
        for (Mod mod : Mods.getMods()) {
            if (message.equals(mod.getChatCommand())) {
                mod.toggle();
                return true;
            }
        }
        if (message.startsWith("up")) {
            // split the message from "up" and a number.
            String[] split = message.split(" ");
            if (split.length == 2) {
                try {
                    int amount = Integer.parseInt(split[1]);
                    PlayerUtils.moveUp(amount);
                    return true;
                } catch (NumberFormatException e) {
                    GavinsMod.LOGGER.error("Invalid number: " + split[1]);
                }
            }
        }
        if (message.startsWith("resetgui")) {
            GavinsMod.gui.reset();
            GavinsMod.guiSettings.reset();
            return true;
        }
        return false;
    }
}
