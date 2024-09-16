package com.peasenet.config

import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors

/**
 *
 * @author GT3CH1
 * @version 09-16-2024
 * @since 09-16-2024
 */
class FreeCamConfig : Config<FreeCamConfig>() {
    init {
        key = "freecam"
    }

    var tracerEnabled: Boolean = false
        set(value) {
            field = value
            saveConfig()
        }

    var espEnabled: Boolean = false
        set(value) {
            field = value
            saveConfig()
        }

    var freeCamSpeed: Float = 1f
        set(value) {
            field = value
            saveConfig()
        }

    var alpha = 0.5f
        set(value) {
            field = value
            saveConfig()
        }

    var color: Color = Colors.GOLD
        set(value) {
            field = value
            saveConfig()
        }

}