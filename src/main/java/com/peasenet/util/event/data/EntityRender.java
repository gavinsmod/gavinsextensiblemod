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
