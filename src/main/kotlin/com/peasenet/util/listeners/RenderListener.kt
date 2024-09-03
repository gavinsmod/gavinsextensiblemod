package com.peasenet.util.listeners

import net.minecraft.client.util.math.MatrixStack

interface RenderListener : Listener {
    fun onRender(matrixStack: MatrixStack, partialTicks: Float)
}