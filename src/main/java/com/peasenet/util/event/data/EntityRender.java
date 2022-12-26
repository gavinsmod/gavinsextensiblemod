package com.peasenet.util.event.data;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;

public class EntityRender {

    public Entity entity;
    public MatrixStack stack;
    public BufferBuilder buffer;
    public Vec3d center;
    public Vec3d playerPos;
    public float delta;
    public VertexConsumerProvider vertexConsumers;
    public int light = 0;

    public EntityRender(Entity entity, MatrixStack stack, BufferBuilder buffer, Vec3d center, Vec3d playerPos, float delta) {
        this.entity = entity;
        this.stack = stack;
        this.buffer = buffer;
        this.center = center;
        this.playerPos = playerPos;
        this.delta = delta;
        this.vertexConsumers = null;
    }

    public EntityRender(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        this.entity = entity;
        this.stack = matrices;
        this.buffer = null;
        this.center = null;
        this.playerPos = null;
        this.delta = tickDelta;
        this.vertexConsumers = vertexConsumers;
        this.light = light;
    }

    public EntityType<?> getEntityType() {
        return entity.getType();
    }
}
