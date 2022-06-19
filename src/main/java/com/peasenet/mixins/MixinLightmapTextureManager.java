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

package com.peasenet.mixins;

import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author gt3ch1
 * Credit: <a href="https://github.com/ate47/Xray/">https://github.com/ate47/Xray/</a>
 * @version 5/16/2022
 */
@Mixin(value = LightmapTextureManager.class)
public class MixinLightmapTextureManager {
//    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;getGamma*:Lnet/minecraft/client/option/SimpleOption;", opcode = Opcodes.GETFIELD), method = "update(F)V")
//    private double getFieldValue(SimpleOption<Double> gamma) {
//        if (GavinsMod.isEnabled(Type.XRAY) || GavinsMod.isEnabled(Type.FULL_BRIGHT))
//            return 10000;
//        else {
//            return gamma.getValue();
//        }
//    }
}