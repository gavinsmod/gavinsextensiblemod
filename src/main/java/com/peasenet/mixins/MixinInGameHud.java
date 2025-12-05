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

import com.peasenet.util.event.EventManager;
import com.peasenet.util.event.InGameHudRenderEvent;
import com.peasenet.util.event.RenderOverlayEvent;
import com.peasenet.util.event.data.RenderOverlay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.DeltaTracker;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author GT3CH1
 * @version 12/25/2022
 * A mixin that allows modding of the in game hud (ie, overlays, extra text, etc.)
 */
@Mixin(Gui.class)
public class MixinInGameHud {
    @Inject(at = @At("HEAD"), method = "render")
    private void mixin(GuiGraphics context, DeltaTracker tickDelta, CallbackInfo ci) {
        var event = new InGameHudRenderEvent(context, tickDelta.getGameTimeDeltaTicks());
        EventManager.getEventManager().call(event);
    }


    @Inject(at = @At("HEAD"), method = "renderTextureOverlay", cancellable = true)
    private void antiPumpkin(GuiGraphics context, Identifier texture, float opacity, CallbackInfo ci) {
        var overlay = new RenderOverlay(texture);
        RenderOverlayEvent event = new RenderOverlayEvent(overlay);
        EventManager.getEventManager().call(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "renderVignette", cancellable = true)
    private void antiVignette(CallbackInfo ci) {
        var overlay = new RenderOverlay(Identifier.parse("textures/misc/vignette.png"));
        RenderOverlayEvent event = new RenderOverlayEvent(overlay);
        EventManager.getEventManager().call(event);
        if (event.isCancelled())
            ci.cancel();
    }
}