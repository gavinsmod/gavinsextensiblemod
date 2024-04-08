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
package com.peasenet.mods

import com.peasenet.config.EspConfig
import com.peasenet.config.MiscConfig
import com.peasenet.config.TracerConfig
import com.peasenet.gavui.Gui
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Mods
import com.peasenet.main.Settings
import com.peasenet.mixinterface.IMinecraftClient
import com.peasenet.settings.Setting
import com.peasenet.util.KeyBindUtils
import com.peasenet.util.event.EventManager
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.world.ClientWorld
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW

/**
 * The base class for all GEM modules. Extending this class will allow you to have a module that appears
 * in the specified category's dropdown menu, a keybind, and a chat command to toggle the module.
 * For an example of extending this class, please visit the [mod-template](https://github.com/gavinsmod/mod-template) repository.
 * An example of extending this class is shown below:
 * ~~~kotlin
 * class MyMod() : Mod("My Mod", "translation.key.mymod", "mymod", ModCategory.MISC)
 * ~~~
 * This example will create a mod that uses the name "My Mod" for sorting, a translation key of "translation.key.mymod" for the name of the mod in the GUI and in chat,
 * a chat command of "mymod" to toggle the mod, and a category of [ModCategory.MISC], as well as an unset keybinding.
 * @param name - The name of the mod, used for sorting purposes only.
 * @param translationKey - The translation key, used for rendering the mod's name in the GUI and in chat.
 * @param chatCommand - The chat command, used for toggling the mod. This does not need to be prefixed by anything.
 * @param modCategory - The category of the mod. See [ModCategory] for more information.
 * @param keyBinding - The keybinding used for this mod, by default it should be [GLFW.GLFW_KEY_UNKNOWN], which will not bind a key to this mod.
 * @author gt3ch1
 * @version 07-18-2023
 *
 */
abstract class Mod(
    override val name: String,
    override val translationKey: String,
    override val chatCommand: String,
    final override var modCategory: ModCategory,
    private var keyBinding: KeyBinding
) : IMod {

    /**
     * Whether this mod is currently deactivating.
     */
    protected var deactivating = false

    /**
     * Whether this mod is currently active.
     */
    override var isActive: Boolean = false


    /**
     * Whether this mod is currently deactivating.
     */
    override val isDeactivating: Boolean
        get() = deactivating

    /**
     * The settings for this mod to be displayed in the settings GUI.
     */
    protected var modSettings = ArrayList<Gui>()

    /**
     * Whether the mod is currently reloading.
     */
    private var reloading = false

    /**
     * Whether the mod is enabled.
     */
    private var isEnabled = false

    /**
     * Sets whether this mod should be enabled or not.
     * @param value - The new state of the mod.
     */
    fun setEnabled(value: Boolean) {
        isEnabled = value
        isActive = value
    }

    init {
        Mods.addMod(this)
    }


    /**
     * Creates a mod with the given parameters.
     *
     * @param name - The name of the mod, used for sorting purposes only.
     * @param translationKey - The translation key, used for rendering the mod's name in the GUI and in chat.
     * @param chatCommand - The chat command, used for toggling the mod. This does not need to be prefixed by anything.
     * @param modCategory - The category of the mod. See [ModCategory] for more information.
     * @param keyBinding - The keybinding used for this mod. This is optional, with the default value of [GLFW.GLFW_KEY_UNKNOWN], which will not bind a key to this mod,
     * but will allow the player to bind a key from the Minecraft controls menu.
     */
    constructor(
        name: String,
        translationKey: String,
        chatCommand: String,
        modCategory: ModCategory,
        keyBinding: Int = GLFW.GLFW_KEY_UNKNOWN
    ) : this(
        name,
        translationKey,
        chatCommand,
        modCategory,
        KeyBindUtils.registerModKeybind(translationKey, modCategory, keyBinding)
    )

    /**
     * Sends a message to the player.
     *
     * @param message The message to send.
     */
    private fun sendMessage(message: String?) {
        if (Settings.getConfig<MiscConfig>("misc").isMessages && !reloading) GavinsModClient.player!!.sendMessage(
            Text.literal(
                message
            ), false
        )
    }

    override fun onEnable() {
        sendMessage(
            GAVINS_MOD_STRING + I18n.translate(
                translationKey
            ) + " §a§lenabled§r!"
        )
        isActive = true
        isEnabled = true
    }

    override fun onDisable() {
        sendMessage(
            GAVINS_MOD_STRING + I18n.translate(
                translationKey
            ) + " §c§ldisabled§r!"
        )
        isEnabled = false
        isActive = false
    }

    override fun onTick() {}
    override fun checkKeybinding() {
        if (keyBinding.wasPressed()) {
            if (isEnabled) {
                deactivate()
            } else {
                activate()
            }
        }
    }

    override fun activate() {
        isEnabled = true
        onEnable()
    }

    override fun deactivate() {
        isEnabled = false
        onDisable()
    }

    override fun toggle() {
        if (isActive) {
            deactivate()
        } else {
            activate()
        }
    }

    /**
     * Adds a setting to the mod, such as a slider or toggle.
     * @param setting - The setting to add.
     * @see Setting
     */
    fun addSetting(setting: Setting) {
        modSettings.add(setting.gui!!)
    }

    /**
     * Adds a setting to the mod, such as a label or any other GavUI element.
     * @param setting - The setting to add.
     */
    fun addSetting(setting: Gui) {
        modSettings.add(setting)
    }


    override fun hasSettings(): Boolean {
        return modSettings.isNotEmpty()
    }

    override val settings
        get() = modSettings

    override fun reload() {
        val prevState = isActive
        if (!prevState) return
        reloading = true
        deactivate()
        activate()
        reloading = false
    }

    override fun reloadSettings() {
        modSettings = ArrayList(settings)
    }

    /**
     * Gets the world that the player is currently in.
     */
    override val world: ClientWorld
        get() = client.getWorld()

    /**
     * Gets the minecraft client instance.
     */
    val client: IMinecraftClient
        get() = GavinsModClient.minecraftClient


    companion object {
        /**
         * The string shown in the chat window when the player toggles the mod.
         */
        const val GAVINS_MOD_STRING = "§b§l[ §4§lGavinsMod §b§l] §7"

        /**
         * The event manager.
         */
        @JvmStatic
        protected var em: EventManager = EventManager.eventManager

        /**
         * The tracer configuration.
         * @deprecated. See how to use the new config system in the [Settings] class.
         */
        @Deprecated(
            "You should be using the updated settings API as defined in com.peasenet.main.Settings",
            ReplaceWith(
                "Settings.getConfig<TracerConfig>(\"tracer\")",
                "com.peasenet.main.Settings", "com.peasenet.config.TracerConfig"
            )
        )
        protected val tracerConfig: TracerConfig
            get() {
                return Settings.getConfig<TracerConfig>("tracer")
            }

        /**
         * ESP configuration.
         */
        @Deprecated(
            "You should be using the updated settings API as defined in com.peasenet.main.Settings",
            ReplaceWith(
                "Settings.getConfig<EspConfig>(\"esp\")",
                "com.peasenet.main.Settings", "com.peasenet.config.EspConfig"
            )
        )
        val espConfig: EspConfig
            get() {
                return Settings.getConfig<EspConfig>("esp")
            }

        /**
         * The miscellaneous configuration.
         */

        @Deprecated(
            "You should be using the updated settings API as defined in com.peasenet.main.Settings",
            ReplaceWith(
                "Settings.getConfig<MiscConfig>(\"misc\")",
                "com.peasenet.main.Settings", "com.peasenet.config.MiscConfig"
            )
        )
        protected val miscConfig: MiscConfig
            get() {
                return Settings.getConfig<MiscConfig>("misc")
            }

    }
}
