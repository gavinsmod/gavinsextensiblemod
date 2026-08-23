package com.peasenet.gui.mod.tracer

import com.peasenet.gui.GuiElement
import com.peasenet.main.Settings
import com.peasenet.config.tracer.ProjectileTracerConfig
import com.peasenet.settings.Setting
import com.peasenet.settings.colorSetting
import com.peasenet.settings.cycleSetting
import com.peasenet.settings.slideSetting
import com.peasenet.settings.toggleSetting
import com.peasenet.util.ChatCommand
import net.minecraft.network.chat.Component

/**
 *
 * A GUI for the [ProjectileTracerConfig] settings, allowing the user to customize the appearance and behavior of projectile tracers in the game.
 *
 * @author GT3CH1
 * @version 07-12-2026
 * @since 07-03-2026 
 */
class GuiProjectileTracer : GuiElement(Component.translatable("gavinsmod.settings.mobtracer"), 3) {

    override fun init() {

        val settings = arrayOf<Setting>(
            toggleSetting {
                title = "gavinsmod.settings.projectiletracer.showtrajectory"
                state = getSettings().showTrajectory
                callback = {
                    getSettings().showTrajectory = it.state
                }
            },
            slideSetting {
                title = "gavinsmod.settings.projectiletracer.trajectoryalpha"
                value = getSettings().trajectoryAlpha
                callback = {
                    getSettings().trajectoryAlpha = it.value
                }
            },


            toggleSetting {
                title = "gavinsmod.settings.projectiletracer.showimpact"
                state = getSettings().showImpact
                callback = {
                    getSettings().showImpact = it.state
                }
            },
            colorSetting {
                title = "gavinsmod.settings.projectiletracer.impactcolor"
                color = getSettings().impactColor
                callback = {
                    getSettings().impactColor = it.color
                }
            },

            toggleSetting {
                title = "gavinsmod.settings.projectiletracer.showhitentity"
                state = getSettings().showHitEntity
                callback = {
                    getSettings().showHitEntity = it.state
                }
            },
            colorSetting {
                title = "gavinsmod.settings.projectiletracer.hitentitycolor"
                color = getSettings().hitEntityColor
                callback = {
                    getSettings().hitEntityColor = it.color
                }
            },

            slideSetting {
                title = "gavinsmod.settings.projectiletracer.impactalpha"
                value = getSettings().impactAlpha
                callback = {
                    getSettings().impactAlpha = it.value
                }
            },
            slideSetting {
                title = "gavinsmod.settings.projectiletracer.hitentityalpha"
                value = getSettings().hitEntityAlpha
                callback = {
                    getSettings().hitEntityAlpha = it.value
                }
            },

            toggleSetting {
                title = "gavinsmod.settings.projectiletracer.showdistance"
                state = getSettings().showImpactDistance
                callback = {
                    getSettings().showImpactDistance = it.state
                }
            },

            colorSetting {
                title = "gavinsmod.settings.projectiletracer.distancetextcolor"
                color = getSettings().impactDistanceColor
                callback = {
                    getSettings().impactDistanceColor = it.color
                }
            },
            colorSetting {
                title = "gavinsmod.settings.projectiletracer.distancetextoutlinecolor"
                color = getSettings().impactDistanceOutline
                callback = {
                    getSettings().impactDistanceOutline = it.color
                }
            },
            slideSetting {
                title = "gavinsmod.settings.projectiletracer.distancetextalpha"
                value = getSettings().impactDistanceAlpha
                callback = {
                    getSettings().impactDistanceAlpha = it.value
                }
            },

            toggleSetting {
                title = "gavinsmod.settings.projectiletracer.showentitydistance"
                state = getSettings().showEntityDistance
                callback = {
                    getSettings().showEntityDistance = it.state
                }
            },

            toggleSetting {
                title = "gavinsmod.settings.projectiletracer.showhitentityoutline"
                state = getSettings().showHitEntityOutline
                callback = {
                    getSettings().showHitEntityOutline = it.state
                }
            },
            colorSetting {
                title = "gavinsmod.settings.projectiletracer.hitentityoutlinecolor"
                color = getSettings().hitEntityOutlineColor
                callback = {
                    getSettings().hitEntityOutlineColor = it.color
                }
            },


            slideSetting {
                title = "gavinsmod.settings.projectiletracer.hitentityoutlinealpha"
                value = getSettings().hitEntityOutlineAlpha
                callback = {
                    getSettings().hitEntityOutlineAlpha = it.value
                }
            },
            colorSetting {
                title = "gavinsmod.settings.projectiletracer.bowtrajectorycolor"
                color = getSettings().bowTrajectoryColor
                callback = {
                    getSettings().bowTrajectoryColor = it.color
                }
            },


            colorSetting {
                title = "gavinsmod.settings.projectiletracer.crossbowtrajectorycolor"
                color = getSettings().crossbowTrajectoryColor
                callback = {
                    getSettings().crossbowTrajectoryColor = it.color
                }
            },
            colorSetting {
                title = "gavinsmod.settings.projectiletracer.snowballtrajectorycolor"
                color = getSettings().snowballTrajectoryColor
                callback = {
                    getSettings().snowballTrajectoryColor = it.color
                }
            },


            colorSetting {
                title = "gavinsmod.settings.projectiletracer.enderpearltrajectorycolor"
                color = getSettings().enderpearlTrajectoryColor
                callback = {
                    getSettings().enderpearlTrajectoryColor = it.color
                }
            },
            colorSetting {
                title = "gavinsmod.settings.projectiletracer.tridenttrajectorycolor"
                color = getSettings().tridentTrajectoryColor
                callback = {
                    getSettings().tridentTrajectoryColor = it.color
                }
            },

            cycleSetting {
                title =
                    "gavinsmod.settings.projectiletracer.tracerstyle." + getSettings().trajectoryStyle.name.lowercase()
                cycleSize = ProjectileTracerConfig.TrajectoryStyle.entries.size
                cycleIndex = getSettings().trajectoryStyle.ordinal
                callback = {
                    val newIndex = (it.cycleIndex + 1) % ProjectileTracerConfig.TrajectoryStyle.entries.size
                    getSettings().trajectoryStyle = ProjectileTracerConfig.TrajectoryStyle.entries[newIndex]
                    // add to the cycle setting to update the title
                    it.cycleIndex = newIndex
                    it.gui.title =
                        Component.translatable("gavinsmod.settings.projectiletracer.tracerstyle." + getSettings().trajectoryStyle.name.lowercase())
                }
            },

            cycleSetting {
                title =
                    "gavinsmod.settings.projectiletracer.tracerwidth." + getSettings().trajectoryWidth.name.lowercase()
                cycleSize = ProjectileTracerConfig.TrajectoryWidth.entries.size
                cycleIndex = getSettings().trajectoryWidth.ordinal
                callback = {
                    val newIndex = (it.cycleIndex + 1) % ProjectileTracerConfig.TrajectoryWidth.entries.size
                    getSettings().trajectoryWidth = ProjectileTracerConfig.TrajectoryWidth.entries[newIndex]
                    // add to the cycle setting to update the title
                    it.cycleIndex = newIndex
                    it.gui.title =
                        Component.translatable("gavinsmod.settings.projectiletracer.tracerwidth." + getSettings().trajectoryWidth.name.lowercase())
                }
            }
        )

        addSettings(settings)
        super.init()
    }


    private fun getSettings(): ProjectileTracerConfig {
        return Settings.getConfig(ChatCommand.ProjectileTracer)
    }


}