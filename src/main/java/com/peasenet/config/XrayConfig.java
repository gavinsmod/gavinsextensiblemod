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
package com.peasenet.config;

import com.peasenet.main.GavinsMod;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

/**
 * The configuration for xray.
 *
 * @author gt3ch1
 * @version 01/04/2022
 */
public class XrayConfig extends Config<XrayConfig> {

    /**
     * The instance of the configuration.
     */
    private static XrayConfig instance;

    /**
     * The list of blocks to xray.
     */
    private final HashSet<String> blocks;

    /**
     * Whether to cull blocks.
     */
    private boolean blockCulling = false;

    public XrayConfig() {
        setKey("xray");
        blocks = new HashSet<>();
        loadDefaultBlocks();
        setInstance(this);
    }

    /**
     * Loads the default block list into the configuration, and saves it.
     */
    public static void loadDefaultBlocks() {
        GavinsMod.xrayConfig.setList(getDefaultBlockList());
    }

    /**
     * Gets the default block list.
     *
     * @return The default block list.
     */
    private static List<Block> getDefaultBlockList() {
        return Registries.BLOCK.stream().filter(b -> b instanceof ExperienceDroppingBlock).toList();
    }

    /**
     * Gets the name of the block, used to identify blocks to xray.
     *
     * @param b - The block to get the name of.
     * @return The name of the block.
     */
    @NotNull
    private static String getId(Block b) {
        var path = b.getLootTableId().getPath();
        return path.equals("empty") ? b.getTranslationKey().replace("block.minecraft.", "") : path.replace("blocks/", "");
    }

    /**
     * Whether to cull blocks.
     *
     * @return Whether to cull blocks.
     */
    public boolean shouldCullBlocks() {
        return blockCulling;
    }

    /**
     * Sets whether to cull blocks.
     *
     * @param blockCulling - Whether to cull blocks.
     */
    public void setBlockCulling(boolean blockCulling) {
        getInstance().blockCulling = blockCulling;
        saveConfig();
    }

    /**
     * Adds a block to the list.
     *
     * @param b - The block to add.
     */
    public void addBlock(Block b) {
        String id = getId(b);
        getInstance().blocks.add(id);
        saveConfig();
    }

    /**
     * Sets the xray block list.
     *
     * @param list - the list to set to.
     */
    public void setList(List<Block> list) {
        getInstance().blocks.clear();
        list.forEach(this::addBlock);
        saveConfig();
    }

    /**
     * Removes a block from the list.
     *
     * @param b - The block to remove.
     */
    public void removeBlock(Block b) {
        String id = getId(b);
        getInstance().blocks.remove(id);
        saveConfig();
    }

    /**
     * Whether the block is in the list.
     *
     * @param b - The block to check.
     * @return Whether the block is in the list.
     */
    public boolean isInList(Block b) {
        String id = getId(b);
        return getInstance().blocks.contains(id);
    }

    @Override
    public XrayConfig getInstance() {
        return instance;
    }

    @Override
    public void setInstance(XrayConfig data) {
        instance = data;
    }

}
