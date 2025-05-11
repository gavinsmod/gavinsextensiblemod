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

package com.peasenet.mixins;

import com.peasenet.gavui.color.Color;
import com.peasenet.mixinterface.IDrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author GT3CH1
 * @version 02-05-2025
 * @since 02-05-2025
 */

@Mixin(DrawContext.class)
public class MixinDrawContext implements IDrawContext {

    @Shadow
    @Final
    private VertexConsumerProvider.Immediate vertexConsumers;

    @Shadow
    @Final
    private MatrixStack matrices;

    @Override
    public @NotNull VertexConsumerProvider.Immediate gavins_mod$getVertexConsumerProvider() {
        return this.vertexConsumers;
    }

    @Override
    public void gavins_mod$drawText(TextRenderer textRenderer, Text text, float x, float y, Color color, boolean shadow) {
        textRenderer.draw(
                text,
                x,
                y,
                color.getAsInt(),
                shadow,
                matrices.peek().getPositionMatrix(),
                gavins_mod$getVertexConsumerProvider(),
                TextRenderer.TextLayerType.NORMAL,
                0,
                15728880
        );
    }

    @Override
    public void gavins_mod$drawText(TextRenderer textRenderer, String text, float x, float y, Color color, boolean shadow) {
        gavins_mod$drawText(
                textRenderer,
                Text.of(text),
                x,
                y,
                color,
                shadow
        );
    }
}
