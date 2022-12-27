/*
 * Copyright (c) 2022-2022. Gavin Pease and contributors.
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

public class XrayConfig extends Config<XrayConfig> {
    private static XrayConfig instance;
    private final HashSet<String> blocks;
    private boolean blockCulling = false;

    public XrayConfig() {
        setKey("xray");
        blocks = new HashSet<>();
        Registries.BLOCK.stream().filter(b -> b instanceof ExperienceDroppingBlock).forEach(b -> blocks.add(
                b.getLootTableId().getPath().replace("blocks/", "")
        ));
        setInstance(this);
    }

    public boolean isBlockCulling() {
        return blockCulling;
    }

    public void setBlockCulling(boolean blockCulling) {
        this.blockCulling = blockCulling;
        setInstance(this);
        saveConfig();
    }

    public void addBlock(Block b) {
        blocks.add(b.getLootTableId().getPath().replace("blocks/", ""));
        setInstance(this);
        saveConfig();
    }

    public void removeBlock(Block b) {
        blocks.remove(b.getLootTableId().getPath().replace("blocks/", ""));
        setInstance(this);
        saveConfig();
    }

    public boolean isInList(Block b) {
        return blocks.contains(b.getLootTableId().getPath().replace("blocks/", ""));
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
