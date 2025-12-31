package com.peasenet.config.render

import com.peasenet.config.Config
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.util.ChatCommand

/**
 *
 * @author GT3CH1
 * @version 12-24-2025
 * @since 12-24-2025
 */
class HealthTagConfig : Config<HealthTagConfig>() {
    init {
        key = ChatCommand.HealthTag.command
    }

    var backgroundColor: Color = Colors.DARK_RED
        set(value) {
            field = value
            saveConfig()
        }
    var alpha: Float = 0.5F
        set(value) {
            field = value
            saveConfig()
        }
    var friendlyMobs = true
        set(value) {
            field = value
            saveConfig()
        }
    var hostileMobs = true
        set(value) {
            field = value
            saveConfig()
        }
    var highHealthColor = Colors.GREEN
        set(value) {
            field = value
            saveConfig()
        }
    var midHealthColor = Colors.YELLOW
        set(value) {
            field = value
            saveConfig()
        }
    var midLowHealthColor = Colors.RED_ORANGE
        set(value) {
            field = value
            saveConfig()
        }
    var lowHealthColor = Colors.RED
        set(value) {
            field = value
            saveConfig()
        }

}