package com.peasenet.mods;

import com.peasenet.main.GavinsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.option.AoMode;
import net.minecraft.client.option.KeyBinding;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 5/16/2022
 */
public class ModXray extends Mod {
    private static AoMode prevAoMode;
    /**
     * A list of blocks that SHOULD be visible (coal, iron, gold, diamond, lapis, redstone, etc.)
     */
    public static ArrayList<Block> blocks = new ArrayList<>() {
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
            add(Blocks.CHEST);
            add(Blocks.END_PORTAL_FRAME);
            add(Blocks.END_PORTAL);
            add(Blocks.ENDER_CHEST);
            add(Blocks.SPAWNER);
        }
    };

    public ModXray(ModType type, KeyBinding keyBinding) {
        super(type, keyBinding);
    }

    @Override
    public void activate() {
        prevAoMode = getOptions().ao;
        isEnabled = true;
        getOptions().ao = AoMode.OFF;
        getClient().chunkCullingEnabled = false;
        getClient().worldRenderer.reload();
        onEnable();
    }

    @Override
    public void deactivate() {
        isEnabled = false;
        getOptions().ao = prevAoMode;
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
        if (GavinsMod.XRay.isActive())
            return blocks.contains(block.getBlock());
        return true;
    }
}
