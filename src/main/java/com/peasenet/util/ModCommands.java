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

import com.peasenet.gui.GuiSettings;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.Mods;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.util.listeners.OnChatSendListener;
import net.minecraft.client.resource.language.I18n;

import java.util.Arrays;

/**
 * @author gt3ch1
 * @version 6/28/2022
 * A class that handles chat commands for all mods.
 */
public class ModCommands implements OnChatSendListener {

    private static String lastCommand = "";

    public ModCommands() {
        GavinsMod.eventManager.subscribe(OnChatSendListener.class, this);
    }

    /**
     * Checks if the command is a mod command. A mod command is a string of text that starts with the prefix "."
     * followed by the name of the mod (an example would be ".fly" to toggle the fly mod). If it is, the given mod
     * is toggled on or off.
     *
     * @param message The message to check.
     * @return True if the message is a mod command, false otherwise.
     */
    public static boolean handleCommand(String message) {
        // check if the message is a command
        if (!message.startsWith("."))
            return false;
        if (message.length() == 1)
            return false;
        message = message.substring(1);
        for (Mod mod : Mods.getMods()) {
            if (message.equals(mod.getChatCommand())) {
                mod.toggle();
                return true;
            }
        }
        if (message.equals("help")) {
            // get all mod types
            PlayerUtils.sendMessage("§bEach command is preceded by a period (§l.§r§b)", true);
            var mods = Type.values();
            // sort by category then name
            Arrays.sort(mods, (o1, o2) -> {
                if (o1.getCategory().equals(o2.getCategory())) {
                    return o1.getName().compareTo(o2.getName());
                }
                return o1.getCategory().compareTo(o2.getCategory());
            });
            var previousCategory = "";
            for (var t : mods) {
                if (!previousCategory.equals(t.getCategory())) {
                    PlayerUtils.sendMessage("§l" + I18n.translate(t.getModCategory().translationKey), false);
                    previousCategory = t.getCategory();
                }
                PlayerUtils.sendMessage("§a" + I18n.translate(t.getTranslationKey()) + " §9-§c " + t.getChatCommand(), false);
            }
            return true;
        }
        if (message.startsWith("up")) {
            // split the message from "up" and a number.
            String[] split = message.split(" ");
            if (split.length == 2) {
                try {
                    int amount = Integer.parseInt(split[1]);
                    PlayerUtils.moveUp(amount);
                } catch (NumberFormatException e) {
                    GavinsMod.LOGGER.error("Invalid number: " + split[1]);
                }
            }
            return true;
        }
        if (message.equals("resetgui")) {
            GavinsMod.gui.reset();
            GavinsMod.guiSettings.reset();
            return true;
        }
        if (message.equals("reloadgui")) {
            GavinsMod.guiSettings = new GuiSettings();
            return true;
        }
        if (!lastCommand.equals(message)) {
            PlayerUtils.sendMessage("§cUnknown command: §l" + message, true);
            PlayerUtils.sendMessage("§cSend your message again if you meant to send it.", false);
            lastCommand = message;
            return true;
        }
        lastCommand = "";
        return false;
    }

    @Override
    public void onChatSend(ChatMessage s) {
        if (handleCommand(s.getMessage()))
            s.cancel();
    }
}
