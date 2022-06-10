package com.peasenet.mods;

import com.peasenet.main.GavinsModClient;
import com.peasenet.util.KeyBindUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

/**
 * @author gt3ch1
 * The base class for mods. Inheriting this class will allow for creating different mods that have a keybinding,
 * and a gui button based off of the given category.
 */
public abstract class Mod implements IMod {

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
     * The keybind for this mod.
     */
    protected final KeyBinding keyBinding;

    public static final String GAVINS_MOD_STRING = "§d§l[ §b§lGavinsMod §d§l] §9";

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
    protected static MinecraftClient getClient() {
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
}
