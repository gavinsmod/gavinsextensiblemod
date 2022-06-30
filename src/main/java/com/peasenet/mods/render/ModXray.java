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
import com.peasenet.main.Mods;
import com.peasenet.main.Settings;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.settings.SubSetting;
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.util.registry.Registry;

import java.util.Comparator;
import java.util.HashSet;

/**
 * @author gt3ch1
 * @version 6/14/2022
 * A mod for xray like feature, allowing the player to see through certain blocks.
 */
public class ModXray extends Mod {

    /**
     * A list of blocks that SHOULD be visible (coal, iron, gold, diamond, lapis, redstone, etc.)
     */
    public static HashSet<Block> blocks;
    private boolean deactivating = true;

    public ModXray() {
        super(Type.XRAY);
        var dropdownWidth = 80;
        SubSetting xraySubSetting = new SubSetting(100, 10, getTranslationKey());
        SubSetting blockSubSetting = new SubSetting(dropdownWidth, 10, "gavinsmod.settings.xray.blocks");
        setupBlocks();
        for (Block block : blocks.stream().sorted(Comparator.comparing(Block::getTranslationKey)).toArray(Block[]::new)) {
            var blockId = block.getLootTableId().toString();
            var blockSettingKey = "xray.block." + blockId;
            ToggleSetting toggleSetting = new ToggleSetting(blockSettingKey, block.getTranslationKey());
            toggleSetting.setCallback(() -> {
                if (!toggleSetting.getValue()) {
                    ModXray.removeFromBlocks(block);
                } else {
                    ModXray.addToBlocks(block);
                }
                if (Mods.getMod("xray").isActive()) reloadRenderer();
            });
            toggleSetting.setWidth(155);
            toggleSetting.setValue(Settings.getBool(blockSettingKey));
            blockSubSetting.add(toggleSetting);
        }
        ToggleSetting culling = new ToggleSetting("xray.disable_culling", "gavinsmod.settings.xray.culling");
        culling.setWidth(dropdownWidth);
        culling.setCallback(this::reloadRenderer);
        ToggleSetting chests = new ToggleSetting("xray.chests", "gavinsmod.settings.xray.chests");
        chests.setWidth(dropdownWidth);
        culling.setCallback(this::reload);
        chests.setCallback(() -> {
            if (chests.getValue()) addChests();
            else removeChests();
            reloadRenderer();
        });
        xraySubSetting.add(culling);
        blockSubSetting.setChildrenWidth(155);
        xraySubSetting.add(blockSubSetting);
        xraySubSetting.add(chests);
        addSetting(xraySubSetting);
    }


    /**
     * Checks if a block is visible
     *
     * @param block Block to check
     * @return True if visible, false if not
     */
    public static boolean shouldDrawFace(BlockState block) {
        if (GavinsMod.isEnabled(Type.XRAY)) return blocks.contains(block.getBlock());
        return true;
    }

    /**
     * Removes the given block from the blocks that should be visible.
     *
     * @param block - Block to add
     */
    public static void removeFromBlocks(Block block) {
        blocks.remove(block);
    }

    /**
     * Adds the given block to the blocks that should be visible.
     *
     * @param block - Block to add
     */
    public static void addToBlocks(Block block) {
        blocks.add(block);
    }

    private void addChests() {
        for (Block block : Registry.BLOCK) {
            if (block instanceof ChestBlock) {
                addToBlocks(block);
            }
        }
    }

    private void removeChests() {
        for (Block block : Registry.BLOCK) {
            if (block instanceof ChestBlock) {
                removeFromBlocks(block);
            }
        }
    }

    /**
     * Initializes the blocks that should be visible.
     */
    private void setupBlocks() {
        blocks = new HashSet<>();
        for (Block block : Registry.BLOCK.stream().filter(b -> b instanceof OreBlock || b instanceof ChestBlock).toList()) {
            if (block instanceof OreBlock) {
                blocks.add(block);
            }
        }
    }

    @Override
    public void activate() {
        if (!GavinsMod.isEnabled(Type.FULL_BRIGHT)) RenderUtils.setLastGamma();
        if (Settings.getBool("xray.disable_culling")) getClient().setChunkCulling(false);
        super.activate();
        reloadRenderer();
    }

    /**
     * Reloads the renderer if and only if the setting "xray.forcereload" is true.
     */
    private void reloadRenderer() {
        if (Mods.getMod("xray").isActive()) getClient().getWorldRenderer().reload();
    }

    @Override
    public void onTick() {
        if (isActive() && !RenderUtils.isHighGamma()) RenderUtils.setHighGamma();
        else if (!GavinsMod.isEnabled(Type.FULL_BRIGHT) && !RenderUtils.isLastGamma() && deactivating) {
            RenderUtils.setLowGamma();
            deactivating = !RenderUtils.isLastGamma();
        }
    }

    @Override
    public boolean isDeactivating() {
        return deactivating;
    }

    @Override
    public void deactivate() {
        // check if full bright is disabled, if it is, reset gamma back to LAST_GAMMA
        if (!GavinsMod.isEnabled(Type.FULL_BRIGHT)) RenderUtils.setLowGamma();
        getClient().setChunkCulling(true);
        reloadRenderer();
        deactivating = true;
        RenderUtils.setGamma(4);
        super.deactivate();
    }
}
