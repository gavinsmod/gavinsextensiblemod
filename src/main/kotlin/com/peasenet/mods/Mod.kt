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

import com.peasenet.config.*
import com.peasenet.gavui.Gui
import com.peasenet.main.GavinsMod
import com.peasenet.main.GavinsModClient
import com.peasenet.main.Mods
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
 * @author gt3ch1
 * @version 04-11-2023
 * The base class for mods. Inheriting this class will allow for creating different mods that have a keybinding,
 * and a gui button based off of the given category.
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
    override var isActive: Boolean = false


    override val isDeactivating: Boolean
        get() = deactivating

    /**
     * The settings of the mod.
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

    fun setEnabled(value: Boolean) {
        isEnabled = value
        isActive = value
    }

    init {
        Mods.addMod(this)
    }


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
        KeyBindUtils.registerKeyBindForType(translationKey, modCategory, keyBinding)
    )

    /**
     * Sends a message to the player.
     *
     * @param message The message to send.
     */
    private fun sendMessage(message: String?) {
        if (miscConfig.isMessages && !reloading) GavinsModClient.player!!.sendMessage(Text.literal(message), false)
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

    fun addSetting(setting: Setting) {
        modSettings.add(setting.gui!!)
    }

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

    override val world: ClientWorld
        get() = client.getWorld()

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
         * Tracer configuration.
         */
        @JvmStatic
        protected var tracerConfig: TracerConfig = GavinsMod.tracerConfig

        /**
         * ESP configuration.
         */
        @JvmStatic
        var espConfig: EspConfig = GavinsMod.espConfig

        /**
         * Fps color configuration.
         */
        @JvmStatic
        protected var fpsColorConfig: FpsColorConfig = GavinsMod.fpsColorConfig

        /**
         * The fullbright configuration.
         */
        @JvmStatic
        protected var fullbrightConfig: FullbrightConfig = GavinsMod.fullbrightConfig

        /**
         * The radar configuration.
         */
        @JvmStatic
        protected var radarConfig: RadarConfig = GavinsMod.radarConfig

        /**
         * The waypoint configuration.
         */
        @JvmStatic
        protected var waypointConfig: WaypointConfig = GavinsMod.waypointConfig

        /**
         * The xray configuration.
         */
        @JvmStatic
        protected var xrayConfig: XrayConfig = GavinsMod.xrayConfig

        /**
         * The miscellaneous configuration.
         */
        @JvmStatic
        protected var miscConfig: MiscConfig = GavinsMod.miscConfig

    }
}