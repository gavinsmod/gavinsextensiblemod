package com.peasenet.util.event.data

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.util.event.CancellableEvent
import net.minecraft.client.gui.Font
import net.minecraft.world.phys.Vec3

/**
 *
 * @author GT3CH1
 * @version 07-12-2026
 * @since 07-12-2026 
 */
data class LevelRenderEndEventData(
    var targetVec: Vec3 = Vec3.ZERO,
    var textToDraw: String = "",
    var textColor: Color = Colors.WHITE,
    var backgroundColor: Color = Colors.BLACK.withAlpha(0.5f),
    var outlineColor: Color = Colors.BLACK,
    var scale: Float = 1/8f,
    var dropShadow: Boolean = false,
    var displayMode: Font.DisplayMode = Font.DisplayMode.NORMAL,
    var lightCoords: Int = 0xffffff,
) : Cancellable() {
}
