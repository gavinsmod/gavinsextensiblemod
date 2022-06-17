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

package com.peasenet.mods;

import com.peasenet.main.GavinsModClient;
import com.peasenet.mixinterface.IMinecraftClient;
import com.peasenet.util.KeyBindUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

/**
 * @author gt3ch1
 * @version 6/14/2022
 * The base class for mods. Inheriting this class will allow for creating different mods that have a keybinding,
 * and a gui button based off of the given category.
 */
public abstract class Mod implements IMod {

    public static final String GAVINS_MOD_STRING = "§d§l[ §b§lGavinsMod §d§l] §9";
    /**
     * The keybind for this mod.
     */
    protected final KeyBinding keyBinding;
    /**
     * The type of the mod.
     */
    private final Type type;
    /**
     * The category of this mod.
     */
    private final Type.Category category;
    /**
     * Whether the mod is enabled.
     */
    protected boolean isEnabled = false;

    /**
     * Creates a new mod.
     *
     * @param type       The type of the mod.
     * @param category   The category of this mod.
     * @param keyBinding The keybind for this mod.
     */

    public Mod(Type type, Type.Category category, KeyBinding keyBinding) {
        this.type = type;
        this.category = category;
        this.keyBinding = keyBinding;
    }

    /**
     * Creats a new mod with an empty keybinding.
     *
     * @param type     The type of the mod.
     * @param category The category of this mod.
     */
    public Mod(Type type, Type.Category category) {
        this.type = type;
        this.category = category;
        this.keyBinding = KeyBindUtils.registerEmptyKeyBind(type);
    }

    /**
     * Creates a new mod with the given type and keybinding.
     *
     * @param type       - The type of the mod.
     * @param keyBinding - The keybind for this mod.
     */
    public Mod(Type type, KeyBinding keyBinding) {
        this.type = type;
        this.category = type.getModCategory();
        this.keyBinding = keyBinding;
    }

    /**
     * Creates a new mod with an empty keybinding.
     *
     * @param type - The type of the mod.
     */
    public Mod(Type type) {
        this.type = type;
        this.category = type.getModCategory();
        if (type.getKeyBinding() == GLFW.GLFW_KEY_UNKNOWN) {
            this.keyBinding = KeyBindUtils.registerEmptyKeyBind(type);
        } else {
            this.keyBinding = KeyBindUtils.registerKeyBindForType(type);
        }
    }

    /**
     * Sends a message to the player.
     *
     * @param message The message to send.
     */
    public static void sendMessage(String message) {
        GavinsModClient.getPlayer().sendMessage(Text.literal(message), false);
    }

    /**
     * Gets the minecraft client.
     *
     * @return The minecraft client.
     */
    protected static IMinecraftClient getClient() {
        return GavinsModClient.getMinecraftClient();
    }

    public void onEnable() {
        sendMessage(GAVINS_MOD_STRING + type.getName() + " §a§lenabled§r!");
    }

    public void onDisable() {
        sendMessage(GAVINS_MOD_STRING + type.getName() + " §c§ldisabled§r!");
    }

    public void onTick() {
        checkKeybinding();
    }

    public void checkKeybinding() {
        if (keyBinding.wasPressed()) {
            if (isEnabled) {
                deactivate();
            } else {
                activate();
            }
        }
    }

    public boolean isActive() {
        return isEnabled;
    }

    public void activate() {
        isEnabled = true;
        onEnable();
    }

    public void deactivate() {
        isEnabled = false;
        onDisable();
    }

    public void toggle() {
        if (isActive()) {
            deactivate();
        } else {
            activate();
        }
    }

    public Type.Category getCategory() {
        return category;
    }

    public String getTranslationKey() {
        return type.getTranslationKey();
    }

    public String getName() {
        return type.getName();
    }

    public String getChatCommand() {
        return type.getChatCommand();
    }

    public Type getType() {
        return type;
    }

    @Override
    public ClientPlayerEntity getPlayer() {
        return getClient().getPlayer();
    }
}
