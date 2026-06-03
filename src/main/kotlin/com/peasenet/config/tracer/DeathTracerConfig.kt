package com.peasenet.config.tracer

import com.peasenet.config.Config
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors

/**
 *
 * @author GT3CH1
 * @version 06-03-2026
 * @since 06-03-2026 
 */
class DeathTracerConfig : Config<DeathTracerConfig>() {
    var color: Color = Colors.RED_ORANGE
        set(value) {
            field = value
            saveConfig()
        }
}