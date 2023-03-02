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
package com.peasenet.util.event

import com.peasenet.util.event.data.EntityRender
import com.peasenet.util.listeners.EntityRenderListener
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d

/**
 * The event for the world render event.
 *
 * @author GT3CH1
 * @version 03-02-2023
 */
class EntityRenderEvent(
    entity: Entity,
    stack: MatrixStack,
    buffer: BufferBuilder?,
    center: Vec3d?,
    playerPos: Vec3d?,
    delta: Float
) : Event<EntityRenderListener>() {
    private var entityRender: EntityRender

    init {
        entityRender = EntityRender(entity, stack, buffer, center, playerPos, delta)
    }

    override fun fire(listeners: ArrayList<EntityRenderListener>) {
        for (listener in listeners) {
            listener.onEntityRender(entityRender)
        }
    }

    override val event: Class<EntityRenderListener>
        get() = EntityRenderListener::class.java
}