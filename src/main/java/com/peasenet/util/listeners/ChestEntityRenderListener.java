/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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
package com.peasenet.util.listeners;

import com.peasenet.util.ChestEntityRender;
import com.peasenet.util.event.Event;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

/**
 * A listener for the world render event.
 *
 * @author GT3CH1
 * @version 12/23/2022
 */
public interface ChestEntityRenderListener extends Listener {

    /**
     * Called when the world is rendered.
     */
    void onEntityRender(ChestEntityRender er);

    /**
     * The event for the world render event.
     *
     * @author GT3CH1
     * @version 12/22/2022
     */
    class ChestEntityRenderEvent extends Event<ChestEntityRenderListener> {
        ChestEntityRender entityRender;

        /**
         * Creates a new world render event.
         *
         * @param stack     - The matrix stack.
         * @param buffer    - The buffer builder.
         * @param center    - The box.
         * @param playerPos - The delta.
         */
        public ChestEntityRenderEvent(BlockEntity entity, MatrixStack stack, BufferBuilder buffer, Vec3d center, Vec3d playerPos, float delta) {
            this.entityRender = new ChestEntityRender(entity, stack, buffer, center, playerPos, delta);
        }

        @Override
        public void fire(ArrayList<ChestEntityRenderListener> listeners) {
            for (ChestEntityRenderListener listener : listeners) {
                listener.onEntityRender(entityRender);
            }
        }

        @Override
        public Class<ChestEntityRenderListener> getEvent() {
            return ChestEntityRenderListener.class;
        }
    }

}
