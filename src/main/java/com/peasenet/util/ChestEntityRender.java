package com.peasenet.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

public class ChestEntityRender {

    public BlockEntity entity;
    public MatrixStack stack;
    public BufferBuilder buffer;
    public Vec3d center;
    public Vec3d playerPos;
    public float delta;

    public ChestEntityRender(BlockEntity entity, MatrixStack stack, BufferBuilder buffer, Vec3d center, Vec3d playerPos, float delta) {
        this.entity = entity;
        this.stack = stack;
        this.buffer = buffer;
        this.center = center;
        this.playerPos = playerPos;
        this.delta = delta;
    }

    public BlockEntityType<?> getEntityType() {
        return entity.getType();
    }
}
