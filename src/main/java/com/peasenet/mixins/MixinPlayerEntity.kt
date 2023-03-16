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
package com.peasenet.mixins

import com.peasenet.main.GavinsMod.Companion.isEnabled
import com.peasenet.mods.Type
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import org.objectweb.asm.Opcodes
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.Redirect
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(PlayerEntity::class)
class MixinPlayerEntity {
    @Inject(method = ["getBlockBreakingSpeed"], at = [At("RETURN")], cancellable = true)
    fun checkFastMine(state: BlockState?, ci: CallbackInfoReturnable<Float?>) {
        if (isEnabled(Type.FAST_MINE)) ci.returnValue = 500.0f
    }

    @Redirect(
        method = ["tick()V"],
        at = At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerEntity;noClip:Z",
            opcode = Opcodes.PUTFIELD
        )
    )
    fun doNoClip(p: PlayerEntity, noClip: Boolean) {
        p.noClip = isEnabled(Type.NO_CLIP)
    }
}