package com.peasenet.mods;

import com.peasenet.main.GavinsMod;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 5/16/2022
 * A mod for an xray like feature, allowing the player to see through certain blocks.
 */
public class ModXray extends Mod {
    /**
     * A list of blocks that SHOULD be visible (coal, iron, gold, diamond, lapis, redstone, etc.)
     */
    public static final ArrayList<Block> blocks = new ArrayList<>() {
        {
            add(Blocks.COAL_ORE);
            add(Blocks.IRON_ORE);
            add(Blocks.GOLD_ORE);
            add(Blocks.DIAMOND_ORE);
            add(Blocks.LAPIS_ORE);
            add(Blocks.REDSTONE_ORE);
            add(Blocks.EMERALD_ORE);
            add(Blocks.DEEPSLATE_COAL_ORE);
            add(Blocks.DEEPSLATE_IRON_ORE);
            add(Blocks.DEEPSLATE_GOLD_ORE);
            add(Blocks.DEEPSLATE_DIAMOND_ORE);
            add(Blocks.DEEPSLATE_LAPIS_ORE);
            add(Blocks.DEEPSLATE_REDSTONE_ORE);
            add(Blocks.DEEPSLATE_EMERALD_ORE);
            add(Blocks.END_PORTAL_FRAME);
            add(Blocks.END_PORTAL);
            add(Blocks.ENDER_CHEST);
            add(Blocks.SPAWNER);
        }
    };

    public ModXray() {
        super(Mods.XRAY, Mods.Category.RENDER, KeyBindingHelper.registerKeyBinding(
                new KeyBinding(Mods.XRAY.getTranslationKey(),
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_X,
                        Mods.XRAY.getCategory())));
    }

    @Override
    public void activate() {
        isEnabled = true;

        getClient().chunkCullingEnabled = false;
        getClient().worldRenderer.reload();
        onEnable();
    }

    @Override
    public void deactivate() {
        isEnabled = false;
        getClient().chunkCullingEnabled = true;
        getClient().worldRenderer.reload();
        onDisable();
    }

    /**
     * Checks if a block is visible
     *
     * @param block Block to check
     * @return True if visible, false if not
     */
    public static boolean shouldDrawFace(BlockState block) {
        if (GavinsMod.XRayEnabled())
            return blocks.contains(block.getBlock());
        return true;
    }
}
