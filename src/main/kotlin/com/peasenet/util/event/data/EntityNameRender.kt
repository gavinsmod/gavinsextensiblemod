/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
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

package com.peasenet.util.event.data

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity

/**
 * A data class that contains the data needed for [ModHealthTag][com.peasenet.mods.render.ModHealthTag].
 *
 * @param entity The entity to render the health tag for.
 * @param matrixStack The matrix stack to render the health tag with.
 * @param vertexConsumerProvider The vertex consumer provider to render the health tag with.
 * @param light The light level to render the health tag with.
 * @see com.peasenet.mods.render.ModHealthTag
 * @see com.peasenet.util.event.EntityRenderNameEvent
 * @see com.peasenet.util.listeners.EntityRenderNameListener
 *
 * @sample com.peasenet.mods.render.ModHealthTag
 *
 * @author GT3CH1
 * @version 01-12-2025
 * @since 09-16-2024
 */
data class EntityNameRender(
    val entity: Entity,
    val matrixStack: MatrixStack,
    var vertexConsumerProvider: VertexConsumerProvider?,
    var light: Int,
)