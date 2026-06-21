package com.peasenet.gui.mod.render

import com.peasenet.config.esp.OreEspConfig
import com.peasenet.gavui.math.PointF
import com.peasenet.gui.GuiElement
import com.peasenet.main.Settings
import com.peasenet.mods.esp.ModOreEsp
import com.peasenet.settings.clickSetting
import com.peasenet.settings.colorSetting
import com.peasenet.settings.slideSetting
import com.peasenet.settings.toggleSetting
import net.minecraft.client.gui.components.FocusableTextWidget
import net.minecraft.network.chat.Component
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.components.WidgetTooltipHolder


/**
 *
 * @author GT3CH1
 * @version 12-06-2025
 * @since 12-06-2025
 */
class GuiOreEsp : GuiElement(Component.translatable("gavinsmod.mod.esp.ore"),2 ) {
    private lateinit var seedBox: EditBox
    private lateinit var seedText: StringWidget

    private fun getSettings(): OreEspConfig {
        return Settings.getConfig("oreesp")
    }

    override fun init() {
        val screenWidth = Minecraft.getInstance().window.guiScaledWidth
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.coal.color"
            color = getSettings().coalColor
            callback = {
                getSettings().coalColor = it.color

            }
        })

        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.coal.enabled"
            state = getSettings().coalEnabled
            callback = {
                getSettings().coalEnabled = it.state

            }
        })
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.iron.color"
            color = getSettings().ironColor
            callback = {
                getSettings().ironColor = it.color

            }
        })

        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.iron.enabled"
            state = getSettings().ironEnabled
            callback = {
                getSettings().ironEnabled = it.state

            }
        })
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.gold.color"
            color = getSettings().goldColor
            callback = {
                getSettings().goldColor = it.color

            }
        })
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.gold.enabled"
            state = getSettings().goldEnabled
            callback = {
                getSettings().goldEnabled = it.state

            }
        })
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.redstone.color"
            color = getSettings().redstoneColor
            callback = {
                getSettings().redstoneColor = it.color

            }
        })
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.redstone.enabled"
            state = getSettings().redstoneEnabled
            callback = {
                getSettings().redstoneEnabled = it.state

            }
        })
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.diamond.color"
            color = getSettings().diamondColor
            callback = {
                getSettings().diamondColor = it.color

            }
        })
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.diamond.enabled"
            state = getSettings().diamondEnabled
            callback = {
                getSettings().diamondEnabled = it.state

            }
        })
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.lapis.color"
            color = getSettings().lapisColor
            callback = {
                getSettings().lapisColor = it.color

            }
        })

        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.lapis.enabled"
            state = getSettings().lapisEnabled
            callback = {
                getSettings().lapisEnabled = it.state

            }
        })
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.copper.color"
            color = getSettings().copperColor
            callback = {
                getSettings().copperColor = it.color

            }
        })
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.copper.enabled"
            state = getSettings().copperEnabled
            callback = {
                getSettings().copperEnabled = it.state

            }
        })
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.emerald.color"
            color = getSettings().emeraldColor
            callback = {
                getSettings().emeraldColor = it.color

            }
        })

        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.emerald.enabled"
            state = getSettings().emeraldEnabled
            callback = {
                getSettings().emeraldEnabled = it.state

            }
        })
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.quartz.color"
            color = getSettings().quartzColor
            callback = {
                getSettings().quartzColor = it.color

            }
        })
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.quartz.enabled"
            state = getSettings().quartzEnabled
            callback = {
                getSettings().quartzEnabled = it.state

            }
        })
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.debris.color"
            color = getSettings().debrisColor
            callback = {
                getSettings().debrisColor = it.color

            }
        })
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.debris.enabled"
            state = getSettings().debrisEnabled
            callback = {
                getSettings().debrisEnabled = it.state

            }
        })
        addSetting( slideSetting {
            title = "gavinsmod.settings.alpha"
            value = getSettings().alpha
            callback = {
                getSettings().alpha = it.value
            }
        })
        addSetting(toggleSetting {
            title = "gavinsmod.mod.esp.blockesp.structure"
            state = getSettings().structureEsp
            callback = {
                getSettings().structureEsp = it.state

            }
        })

        resizeElements()
        super.init()
        val apply = clickSetting {
            title = "gavinsmod.settings.apply"
            topLeft = titleBox!!.position.add(titleBox!!.width + 2f, 0f)
            callback = {
                reload()
            }
            width = 32f
        }
        addSetting(apply)
        seedText = StringWidget(
            apply.topLeft.x.toInt() + 36, apply.topLeft.y.toInt() + 1,
            25,
            12,
            Component.literal("Seed"),
            font
        )
        seedBox = EditBox(font, seedText.x + 30, apply.topLeft.y.toInt(), 150, 12, Component.empty())
        seedBox.value = getSettings().seed
        addWidget(seedBox)
        addRenderableWidget(seedText)
    }

    override fun extractRenderState(drawContext: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        super.extractRenderState(drawContext, mouseX, mouseY, delta)
        seedBox.extractRenderState(drawContext, mouseX, mouseY, delta)
        seedText.extractRenderState(drawContext, mouseX, mouseY, delta)
    }

    private fun reload() {
        Settings.getConfig<OreEspConfig>("oreesp").seed = seedBox.value
        ModOreEsp.reload()
    }
}