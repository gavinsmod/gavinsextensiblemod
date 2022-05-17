package com.peasenet.main;

import com.peasenet.mods.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


/**
 * @author gt3ch1
 * @version 5/14/2022
 */
public class GavinsMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("gavinsmod");
    public static ModXray XRay;
    public static ModFly Fly;
    public static ModFastMine FastMine;

    public static ModFullBright FullBright;

    public static ArrayList<Mod> mods = new ArrayList<>();
    @Override
    public void onInitialize() {
        LOGGER.info("GavinsMod initialized");
        KeyBinding xrayKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(ModType.XRAY.getTranslationKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, ModType.XRAY.getCategory()));
        XRay = new ModXray(ModType.XRAY, xrayKeybinding);
        mods.add(XRay);

        KeyBinding flyKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(ModType.FLY.getTranslationKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F, ModType.FLY.getCategory()));
        Fly = new ModFly(ModType.FLY, flyKeybinding);
        mods.add(Fly);

        KeyBinding fastmineKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(ModType.FAST_MINE.getTranslationKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, ModType.FAST_MINE.getCategory()));
        FastMine = new ModFastMine(ModType.FAST_MINE, fastmineKeybinding);
        mods.add(FastMine);

        KeyBinding fullbrightKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(ModType.FULL_BRIGHT.getTranslationKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, ModType.FULL_BRIGHT.getCategory()));
        FullBright = new ModFullBright(ModType.FULL_BRIGHT, fullbrightKeybinding);
        mods.add(FullBright);
    }
}

