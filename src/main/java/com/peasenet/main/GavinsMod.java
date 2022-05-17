package com.peasenet.main;

import com.peasenet.mods.Mod;
import com.peasenet.mods.ModType;
import com.peasenet.mods.XrayMod;
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
    public static XrayMod XRay;
    public static ArrayList<Mod> mods = new ArrayList<>();
    @Override
    public void onInitialize() {
        LOGGER.info("GavinsMod initialized");
        KeyBinding xrayKeybinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(ModType.XRAY.getTranslationKey(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, ModType.XRAY.getCategory()));
        XRay = new XrayMod(ModType.XRAY, xrayKeybinding);
        mods.add(XRay);
    }
}

