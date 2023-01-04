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

import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.registry.Registries;

import java.util.HashSet;

/**
 * The configuration for xray.
 *
 * @author gt3ch1
 * @version 12/31/2022
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
        Registries.BLOCK.stream().filter(b -> b instanceof ExperienceDroppingBlock).forEach(b -> blocks.add(
                b.getLootTableId().getPath().replace("blocks/", "")
        ));
        setInstance(this);
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
        this.blockCulling = blockCulling;
        setInstance(this);
        saveConfig();
    }

    /**
     * Adds a block to the list.
     *
     * @param b - The block to add.
     */
    public void addBlock(Block b) {
        blocks.add(b.getLootTableId().getPath().replace("blocks/", ""));
        setInstance(this);
        saveConfig();
    }

    /**
     * Removes a block from the list.
     *
     * @param b - The block to remove.
     */
    public void removeBlock(Block b) {
        blocks.remove(b.getLootTableId().getPath().replace("blocks/", ""));
        setInstance(this);
        saveConfig();
    }

    /**
     * Whether the block is in the list.
     *
     * @param b - The block to check.
     * @return Whether the block is in the list.
     */
    public boolean isInList(Block b) {
        var path = b.getLootTableId().getPath().replace("blocks/", "");
        return blocks.contains(path);
    }

    @Override
    public XrayConfig getInstance() {
        return this;
    }

    @Override
    public void setInstance(XrayConfig data) {
        instance = data;
    }

}
