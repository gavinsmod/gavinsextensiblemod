package com.peasenet.config.tracer

import com.peasenet.config.Config
import com.peasenet.gavui.color.Colors
import com.peasenet.util.ChatCommand

class ProjectileTracerConfig : Config<ProjectileTracerConfig>() {
    enum class TrajectoryStyle {
        LINE, DOTTED, DASHED
    }

    enum class TrajectoryWidth {
        SMALL, MEDIUM, LARGE
    }

    init {
        key = ChatCommand.ProjectileTracer.command
    }

    var alpha: Float = 1.0f
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
        }

    var showImpact: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }

    var impactColor = Colors.BLUE
        set(value) {
            field = value
            saveConfig()
        }

    var impactAlpha = 0.25f
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
            saveConfig()
        }
    var showImpactDistance = true
        set(value) {
            field = value
            saveConfig()
        }

    var showEntityDistance = true
        set(value) {
            field = value
            saveConfig()
        }

    var showHitEntity: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }

    var trajectoryAlpha = 1.0f
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
            saveConfig()
        }

    var hitEntityColor = Colors.GREEN
        set(value) {
            field = value
            saveConfig()
        }

    var hitEntityAlpha = 0.25f
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
            saveConfig()
        }

    var showHitEntityOutline = true
        set(value) {
            field = value
            saveConfig()
        }

    var hitEntityOutlineColor = Colors.WHITE
        set(value) {
            field = value
            saveConfig()
        }

    var hitEntityOutlineAlpha = 0.5f
        set(value) {
            field = value.coerceIn(0.0f, 1.0f)
            saveConfig()
        }

    var showTrajectory = true
        set(value) {
            field = value
            saveConfig()
        }

    var bowTrajectoryColor = Colors.RED
        set(value) {
            field = value
            saveConfig()
        }

    var crossbowTrajectoryColor = Colors.PURPLE
        set(value) {
            field = value
            saveConfig()
        }

    var fishingRodTrajectoryColor = Colors.MEDIUM_SEA_GREEN
        set(value) {
            field = value
            saveConfig()
        }

    var snowballTrajectoryColor = Colors.WHITE
        set(value) {
            field = value
            saveConfig()
        }

    var enderpearlTrajectoryColor = Colors.DARK_SPRING_GREEN
        set(value) {
            field = value
            saveConfig()
        }

    var tridentTrajectoryColor = Colors.MEDIUM_SEA_GREEN
        set(value) {
            field = value
            saveConfig()
        }

    var trajectoryStyle = TrajectoryStyle.LINE
        set(value) {
            field = value
            saveConfig()
        }


    var trajectoryWidth = TrajectoryWidth.MEDIUM
        set(value) {
            field = value
            saveConfig()
        }

}