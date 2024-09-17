/*
 * MIT License
 *
 * Copyright (c) 2022-2024, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.peasenet.util.event

import com.peasenet.util.event.data.EntityNameRender
import com.peasenet.util.listeners.EntityRenderNameListener
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.text.Text

/**
 * The event for the world render event.
 *
 * @author GT3CH1
 * @version 03-02-2023
 */
class EntityRenderNameEvent(
    entity: Entity,
    matrices: MatrixStack,
    vertexConsumers: VertexConsumerProvider?,
    tickDelta: Float,
    light: Int
) : Event<EntityRenderNameListener>() {
    private var entityRender: EntityNameRender = EntityNameRender(
        entity,
        matrices,
        vertexConsumers!!,
        tickDelta,
        light,
    )

    override fun fire(listeners: ArrayList<EntityRenderNameListener>) {
        for (listener in listeners) {
            listener.onEntityNameRender(entityRender)
        }
    }

    override val event: Class<EntityRenderNameListener>
        get() = EntityRenderNameListener::class.java
}