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
package com.peasenet.mods.render

import com.peasenet.mixinterface.ISimpleOption
import net.minecraft.client.option.ParticlesMode

/**
 * @author gt3ch1
 * @version 03-02-2023
 */
@Suppress("UNCHECKED_CAST")
class ModBarrierDetect : RenderMod(
    "Barrier Detect",
    "gavinsmod.mod.render.barrierdetect",
    "barrierdetect"
) {
    override fun onEnable() {
        super.onEnable()
        particlesMode = client.options.particles.value
        when (particlesMode) {
            ParticlesMode.ALL, ParticlesMode.DECREASED -> {}
            ParticlesMode.MINIMAL -> {
                val newMode = client.options.particles
                (newMode as Any as ISimpleOption<ParticlesMode>).forceSetValue(ParticlesMode.ALL)
            }

            null -> TODO()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onDisable() {
        super.onDisable()
        when (particlesMode) {
            ParticlesMode.ALL, ParticlesMode.DECREASED -> {}
            ParticlesMode.MINIMAL -> {
                val newMode = client.options.particles!!
                (newMode as ISimpleOption<ParticlesMode>).forceSetValue(ParticlesMode.MINIMAL)
            }

            null -> TODO()
        }
    }

    companion object {
        private var particlesMode: ParticlesMode? = null
    }
}