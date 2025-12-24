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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.peasenet.main.Mods;
import com.peasenet.util.ChatCommand;
import com.peasenet.util.event.EntityRenderNameEvent;
import com.peasenet.util.event.EventManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    @Inject(at = @At("HEAD"), method = "shouldShowName", cancellable = true)
    private <E extends Entity, S extends EntityRenderState>
    void shouldShowName(E entity, double d, CallbackInfoReturnable<Boolean> cir) {
        var hpTagsEnabled = Mods.isActive(ChatCommand.HealthTag) && entity instanceof LivingEntity;
        if(hpTagsEnabled)
            cir.setReturnValue(true);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;shouldShowName(Lnet/minecraft/world/entity/Entity;D)Z"), method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V")
    private <E extends Entity, S extends EntityRenderState> boolean wrapRender(EntityRenderer<E, S> instance, E entity, double d, Operation<Boolean> original) {
        if(Mods.isActive(ChatCommand.HealthTag) && entity instanceof LivingEntity)
            return true;
        return original.call(instance, entity, d);
    }

    @Inject(at = @At("TAIL"), method = "extractRenderState")
    private void healthTags(T entity, S entityRenderState, float f, CallbackInfo ci) {

        if (!(entity instanceof LivingEntity le) || entityRenderState.nameTag == null) {
            return;
        }
        var event = new EntityRenderNameEvent(le);
        EventManager.getEventManager().call(event);
        if (event.isCancelled()) {
            entityRenderState.nameTag = entityRenderState.nameTag.copy().append(" - ").append(event.getEventData());
        }
    }
}
