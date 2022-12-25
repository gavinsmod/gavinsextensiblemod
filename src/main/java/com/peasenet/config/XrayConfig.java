package com.peasenet.config;

import com.peasenet.main.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.registry.Registries;

import java.util.HashSet;

public class XrayConfig extends Config<XrayConfig> {
    private static XrayConfig instance;

    public boolean isBlockCulling() {
        return blockCulling;
    }

    public void setBlockCulling(boolean blockCulling) {
        this.blockCulling = blockCulling;
    }

    private HashSet<String> blocks;
    private boolean blockCulling = false;

    public XrayConfig() {
        blocks = new HashSet<>();
        Registries.BLOCK.stream().filter(b -> b instanceof ExperienceDroppingBlock).forEach(this::addBlock);
        setInstance(this);
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
    public void setInstance(XrayConfig data) {
        instance = data;
    }

    @Override
    public void loadDefaultConfig() {
        blocks = new HashSet<>();
        Registries.BLOCK.stream().filter(b -> b instanceof ExperienceDroppingBlock).forEach((b -> blocks.add(b.getLootTableId().getPath())));
        blockCulling = false;
        setInstance(this);
        Settings.settings.put("xray", instance);
        Settings.save();
    }

    @Override
    public XrayConfig getInstance() {
        return this;
    }

    @Override
    public void readFromSettings() {
        setInstance(Settings.getXrayConfig());
    }
}
