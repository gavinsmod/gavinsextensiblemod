package com.peasenet.mixinterface;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public interface IMinecraftClient {
    int getAttackCooldown();

    void setAttackCooldown(int cooldown);

    int getItemUseCooldown();

    void setItemUseCooldown(int cooldown);

    int getFps();

    GameOptions getOptions();

    ClientPlayerEntity getPlayer();

    ClientPlayerInteractionManager getPlayerInteractionManager();

    TextRenderer getTextRenderer();

    ClientWorld getWorld();

    void setScreen(Screen s);

    WorldRenderer getWorldRenderer();

    void setChunkCulling(boolean b);

    Window getWindow();

    Biome getBiomeAtPos(BlockPos pos);


}
