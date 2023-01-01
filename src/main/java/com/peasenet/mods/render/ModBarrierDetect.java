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

package com.peasenet.mods.render;

import com.peasenet.mixinterface.ISimpleOption;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import net.minecraft.client.option.ParticlesMode;

/**
 * @author gt3ch1
 * @version 01/01/2023
 */
public class ModBarrierDetect extends Mod {

    private static ParticlesMode particlesMode;

    public ModBarrierDetect() {
        super(Type.BARRIER_DETECT);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onEnable() {
        super.onEnable();
        particlesMode = getClient().getOptions().getParticles().getValue();
        switch (particlesMode) {
            case ALL, DECREASED -> {
            }
            case MINIMAL -> {
                var newMode = getClient().getOptions().getParticles();
                ((ISimpleOption<ParticlesMode>) (Object) newMode).forceSetValue(ParticlesMode.ALL);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onDisable() {
        super.onDisable();
        switch (particlesMode) {
            case ALL, DECREASED -> {
            }
            case MINIMAL -> {
                var newMode = getClient().getOptions().getParticles();
                ((ISimpleOption<ParticlesMode>) (Object) newMode).forceSetValue(ParticlesMode.MINIMAL);
            }
        }
    }
}
