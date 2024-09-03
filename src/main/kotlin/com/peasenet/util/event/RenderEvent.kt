package com.peasenet.util.event

import com.peasenet.util.listeners.RenderListener
import net.minecraft.client.util.math.MatrixStack

class RenderEvent : Event<RenderListener>() {
    lateinit var matrixStack: MatrixStack
    var partialTicks: Float = 0.0f

    companion object {

        private var INSTANCE = RenderEvent()
        fun get(matrixStack: MatrixStack, partialTicks: Float): RenderEvent {
            INSTANCE.matrixStack = matrixStack
            INSTANCE.partialTicks = partialTicks
            return INSTANCE
        }
    }

    override fun fire(listeners: ArrayList<RenderListener>) {
        for (listener in listeners) {
            listener.onRender(matrixStack, partialTicks)
        }
    }

    override val event: Class<RenderListener>
        get() = RenderListener::class.java
}