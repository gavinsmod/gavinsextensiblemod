/*
 * Copyright (c) 2022-$YEAR. Gavin Pease and contributors.
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

package com.peasenet.mods.tracer

import com.peasenet.config.TracerConfig
import com.peasenet.main.Settings
import com.peasenet.mods.Mod
import com.peasenet.mods.ModCategory
import com.peasenet.util.event.data.CameraBob
import com.peasenet.util.listeners.BlockEntityRenderListener
import com.peasenet.util.listeners.CameraBobListener
import com.peasenet.util.listeners.EntityRenderListener
import org.lwjgl.glfw.GLFW

/**
 * The base class for all tracer mods. Extending this class will automatically add the mod to the tracer category,
 * as well as a chat command, GUI element, and an optional keybind.
 * For example,
 * ~~~
 * class ModExampleTracer() : TracerMod("Example Tracer", "example_tracer", "exampletracer")
 * ~~~
 * This class extends the [Mod] class, so it has all the same methods and properties. 
 * Please note that by extending this class, you will have to implement all 
 * the methods from the [EntityRenderListener], [CameraBobListener], and [BlockEntityRenderListener] interfaces.
 * You may also need to include gavinsmod-events in your dependencies.
 * @param name The name of the mod.
 * @param translationKey The translation key for the mod's name.
 * @param chatCommand The chat command for the mod.
 * @param keyBinding The keybind for the mod. Defaults to [GLFW.GLFW_KEY_UNKNOWN].
 * @see Mod
 * @see EntityRenderListener
 * @see CameraBobListener
 * @see BlockEntityRenderListener
 * @author GT3CH1
 * @version 07-18-2023
 */
abstract class TracerMod(
    name: String,
    translationKey: String,
    chatCommand: String,
    keyBinding: Int = GLFW.GLFW_KEY_UNKNOWN
) : Mod(
    name,
    translationKey,
    chatCommand,
    ModCategory.TRACERS,
    keyBinding
), EntityRenderListener, CameraBobListener, BlockEntityRenderListener {
    override fun onEnable() {
        super.onEnable()
        em.subscribe(BlockEntityRenderListener::class.java, this)
        em.subscribe(EntityRenderListener::class.java, this)
        em.subscribe(CameraBobListener::class.java, this)
    }

    override fun onDisable() {
        super.onDisable()
        em.unsubscribe(BlockEntityRenderListener::class.java, this)
        em.unsubscribe(EntityRenderListener::class.java, this)
        em.unsubscribe(CameraBobListener::class.java, this)
    }

    override fun onCameraViewBob(c: CameraBob) {
        if (Settings.getConfig<TracerConfig>("tracer").viewBobCancel) c.cancel()
    }
}