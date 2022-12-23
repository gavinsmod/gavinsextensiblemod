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

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.main.Mods;
import com.peasenet.main.Settings;
import com.peasenet.mixinterface.IMinecraftClient;
import com.peasenet.settings.Setting;
import com.peasenet.util.KeyBindUtils;
import com.peasenet.util.event.EventManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 7/5/2022
 * The base class for mods. Inheriting this class will allow for creating different mods that have a keybinding,
 * and a gui button based off of the given category.
 */
public abstract class Mod implements IMod {

    public static EventManager em = GavinsMod.eventManager;

    /**
     * The string shown in the chat window when the player toggles the mod.
     */

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
     * Whether this mod is currently deactivating.
     */
    protected boolean deactivating = true;
    /**
     * The settings of the mod.
     */
    protected ArrayList<Setting> modSettings = new ArrayList<>();
    /**
     * Whether the mod is currently reloading.
     */
    private boolean reloading = false;
    /**
     * Whether the mod is enabled.
     */
    private boolean isEnabled = false;

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
        Mods.addMod(this);
    }

    /**
     * Creates a new mod with an empty keybinding.
     *
     * @param type     The type of the mod.
     * @param category The category of this mod.
     */
    public Mod(Type type, Type.Category category) {
        this(type, category, (type.getKeyBinding() != GLFW.GLFW_KEY_UNKNOWN) ?
                KeyBindUtils.registerKeyBindForType(type) : KeyBindUtils.registerEmptyKeyBind(type));
    }

    /**
     * Creates a new mod with the given type and keybinding.
     *
     * @param type       - The type of the mod.
     * @param keyBinding - The keybinding for this mod.
     */
    public Mod(Type type, KeyBinding keyBinding) {
        this(type, type.getModCategory(), keyBinding);
    }

    /**
     * Creates a new mod with an empty keybinding.
     *
     * @param type - The type of the mod.
     */
    public Mod(Type type) {
        this(type, type.getModCategory());
    }

    /**
     * Gets the minecraft client.
     *
     * @return The minecraft client.
     */
    protected static IMinecraftClient getClient() {
        return GavinsModClient.getMinecraftClient();
    }

    /**
     * Sends a message to the player.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        if (Settings.getBool("misc.messages") && !reloading)
            GavinsModClient.getPlayer().sendMessage(Text.literal(message), false);
    }

    public void onEnable() {
        sendMessage(GAVINS_MOD_STRING + type.getName() + " §a§lenabled§r!");
    }

    public void onDisable() {
        sendMessage(GAVINS_MOD_STRING + type.getName() + " §c§ldisabled§r!");
    }

    public void onTick() {
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

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
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

    public ClientPlayerEntity getPlayer() {
        return getClient().getPlayer();
    }

    public void onAttack(Entity target) {
    }

    public boolean isDeactivating() {
        return false;
    }

    public void addSetting(Setting setting) {
        modSettings.add(setting);
    }

    public ArrayList<Setting> getSettings() {
        return modSettings;
    }

    public boolean hasSettings() {
        return !modSettings.isEmpty();
    }

    public void reload() {
        var prevState = isActive();
        if (!prevState)
            return;
        reloading = true;
        deactivate();
        activate();
        reloading = false;
    }

    public void reloadSettings() {
        modSettings = new ArrayList<>(getSettings());
    }

    public ClientWorld getWorld() {
        return getClient().getWorld();
    }

    public void onWorldRender(ClientWorld world, MatrixStack stack, BufferBuilder buffer, float delta) {

    }
}
