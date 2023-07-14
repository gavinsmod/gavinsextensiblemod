/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
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
package com.peasenet.util

import com.peasenet.gui.GuiSettings
import com.peasenet.main.GavinsMod
import com.peasenet.main.Mods
import com.peasenet.main.Mods.Companion.mods
import com.peasenet.mods.Mod
import com.peasenet.mods.ModCategory
import com.peasenet.util.PlayerUtils.sendMessage
import com.peasenet.util.event.EventManager
import com.peasenet.util.event.data.ChatMessage
import com.peasenet.util.listeners.OnChatSendListener
import net.minecraft.client.resource.language.I18n

/**
 * @author gt3ch1
 * @version 04-11-2023
 * A class that handles chat commands for all mods.
 */
class ModCommands : OnChatSendListener {
    init {
        EventManager.eventManager.subscribe(OnChatSendListener::class.java, this)
    }

    override fun onChatSend(s: ChatMessage) {
        if (handleCommand(s.message)) s.cancel()
    }

    companion object {
        private var lastCommand = ""

        /**
         * Checks if the command is a mod command. A mod command is a string of text that starts with the prefix "."
         * followed by the name of the mod (an example would be ".fly" to toggle the fly mod). If it is, the given mod
         * is toggled on or off.
         *
         * @param message The message to check.
         * @return True if the message is a mod command, false otherwise.
         */
        fun handleCommand(message: String): Boolean {
            // check if the message is a command
            var s = message
            if (!s.startsWith(".")) return false
            if (s.length == 1) return false
            s = s.substring(1)
            for (mod in mods) {
                if (mod.modCategory == ModCategory.GUI) continue
                if (s == mod.chatCommand) {
                    mod.toggle()
                    return true
                }
            }
            if (s == "help") {
                // get all mod types
                sendMessage("§bEach command is preceded by a period (§l.§r§b)", true)
                val mods = Mods.mods.toMutableList()
                mods.sortWith(compareBy<Mod> { it.modCategory }.thenBy { it.name })
                var previousCategory = ModCategory.NONE
                for (t in mods) {
                    if (t.modCategory == ModCategory.GUI) continue
                    if (previousCategory != t.modCategory) {
                        sendMessage("§l" + I18n.translate(t.modCategory.translationKey), false)
                        previousCategory = t.modCategory
                    }
                    sendMessage("§a" + I18n.translate(t.translationKey) + " §9-§c " + t.chatCommand, false)
                }
                return true
            }
            if (s == "resetgui") {
                GavinsMod.gui.reset()
                GavinsMod.guiSettings.reset()
                return true
            }
            if (s == "reloadgui") {
                GavinsMod.guiSettings = GuiSettings()
                return true
            }
            if (lastCommand != s) {
                sendMessage("§cUnknown command: §l$s", true)
                sendMessage("§cSend your message again if you meant to send it.", false)
                lastCommand = s
                return true
            }
            lastCommand = ""
            return false
        }
    }
}