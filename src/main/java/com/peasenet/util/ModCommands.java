package com.peasenet.util;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;

/**
 * @author gt3ch1
 * @version 5/24/2022
 */
public class ModCommands {
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
                    PlayerUtils.doJump();
                    return true;
                } catch (NumberFormatException e) {
                    // do nothing
                }
            }
        }
        return false;
    }
}
