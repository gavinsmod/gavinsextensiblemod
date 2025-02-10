package com.peasenet.settings

import com.peasenet.gavui.GavUI
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.math.PointF
import com.peasenet.gavui.util.Direction

/**
 *
 * @author GT3CH1
 * @version 02-06-2025
 * @since 02-06-2025
 */
open class CallbackSetting<T : Setting>(
    topLeft: PointF = PointF(0F, 0F),
    width: Float = 0F,
    height: Float = 10F,
    title: String = "",
    state: Boolean = false,
    hoverable: Boolean = false,
    transparency: Float = -1f,
    symbol: Char = '\u0000',
    cycleIndex: Int = 0,
    cycleSize: Int = 0,
    maxChildren: Int = 4,
    defaultMaxChildren: Int = 4,
    direction: Direction = Direction.DOWN,
    children: MutableList<Setting> = mutableListOf(),
    color: Color = GavUI.backgroundColor(),
    value: Float = 0.5f,
    open var callback: ((T) -> Unit)? = null,
) : Setting(
    topLeft,
    width,
    height,
    title,
    state,
    hoverable,
    transparency,
    symbol,
    cycleIndex,
    cycleSize,
    maxChildren,
    defaultMaxChildren,
    direction,
    children,
    color,
    value
)
