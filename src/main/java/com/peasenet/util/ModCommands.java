package com.peasenet.util;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;

/**
 * @author gt3ch1
 * @version 5/24/2022
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
        for (Mod mod : GavinsMod.mods) {
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
        return false;
    }
}
