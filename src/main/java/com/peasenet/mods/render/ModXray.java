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

package com.peasenet.mods.render;

import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import java.util.ArrayList;

/**
 * @author gt3ch1
 * @version 6/14/2022
 * A mod for xray like feature, allowing the player to see through certain blocks.
 */
public class ModXray extends Mod {

    private boolean deactivating = true;

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
            add(Blocks.NETHERITE_BLOCK);
            add(Blocks.NETHER_GOLD_ORE);
        }
    };

    public ModXray() {
        super(Type.XRAY);
    }

    /**
     * Checks if a block is visible
     *
     * @param block Block to check
     * @return True if visible, false if not
     */
    public static boolean shouldDrawFace(BlockState block) {
        if (GavinsMod.isEnabled(Type.XRAY))
            return blocks.contains(block.getBlock());
        return true;
    }

    @Override
    public void activate() {
        if (!GavinsMod.isEnabled(Type.FULL_BRIGHT))
            RenderUtils.setLastGamma();
        getClient().setChunkCulling(false);
        getClient().getWorldRenderer().reload();
        super.activate();
    }

    @Override
    public void onTick() {
        if (isActive() && !RenderUtils.isHighGamma())
            RenderUtils.setHighGamma();
        else if (!GavinsMod.isEnabled(Type.FULL_BRIGHT) && !RenderUtils.isLastGamma() && deactivating) {
            RenderUtils.setLowGamma();
            deactivating = !RenderUtils.isLastGamma();
        }
        super.onTick();
    }

    @Override
    public void deactivate() {
        // check if full bright is disabled, if it is, reset gamma back to LAST_GAMMA
        if (!GavinsMod.isEnabled(Type.FULL_BRIGHT))
            RenderUtils.setLowGamma();
        getClient().setChunkCulling(true);
        getClient().getWorldRenderer().reload();
        deactivating = true;
        RenderUtils.setGamma(4);
        super.deactivate();
    }
}
