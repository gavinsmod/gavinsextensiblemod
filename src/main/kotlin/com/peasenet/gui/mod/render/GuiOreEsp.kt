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
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.components.WidgetTooltipHolder


/**
 *
 * @author GT3CH1
 * @version 12-06-2025
 * @since 12-06-2025
 */
class GuiOreEsp : GuiElement(Component.translatable("gavinsmod.mod.esp.ore")) {
    private var m_width = 200
    private val m_height = 11 * 13f
    private lateinit var seedBox: EditBox
    private lateinit var seedText: StringWidget

    private fun getSettings(): OreEspConfig {
        return Settings.getConfig("oreesp")
    }

    override fun init() {

        val screenWidth = Minecraft.getInstance().window.guiScaledWidth
        val screenHeight = Minecraft.getInstance().window.guiScaledHeight

        val offsetX = (screenWidth - m_width) / 2f
        val offsetY = (screenHeight - m_height) / 2f
        var guiPosition = PointF(offsetX, offsetY)
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.coal.color"
            color = getSettings().coalColor
            topLeft = guiPosition
            callback = {
                getSettings().coalColor = it.color

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.iron.color"
            color = getSettings().ironColor
            topLeft = guiPosition
            callback = {
                getSettings().ironColor = it.color

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.gold.color"
            color = getSettings().goldColor
            topLeft = guiPosition
            callback = {
                getSettings().goldColor = it.color

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.redstone.color"
            color = getSettings().redstoneColor
            topLeft = guiPosition
            callback = {
                getSettings().redstoneColor = it.color

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.diamond.color"
            color = getSettings().diamondColor
            topLeft = guiPosition
            callback = {
                getSettings().diamondColor = it.color

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.lapis.color"
            color = getSettings().lapisColor
            topLeft = guiPosition
            callback = {
                getSettings().lapisColor = it.color

            }
        })

        guiPosition = guiPosition.add(0f, 12f)
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.copper.color"
            color = getSettings().copperColor
            topLeft = guiPosition
            callback = {
                getSettings().copperColor = it.color

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.emerald.color"
            color = getSettings().emeraldColor
            topLeft = guiPosition
            callback = {
                getSettings().emeraldColor = it.color

            }
        })

        guiPosition = guiPosition.add(0f, 12f)
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.quartz.color"
            color = getSettings().quartzColor
            topLeft = guiPosition
            callback = {
                getSettings().quartzColor = it.color

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(colorSetting {
            title = "gavinsmod.mod.blocks.debris.color"
            color = getSettings().debrisColor
            topLeft = guiPosition
            callback = {
                getSettings().debrisColor = it.color

            }
        })
        guiPosition = PointF(offsetX + 40f, offsetY)
        // add all settings for enable/disable of ores
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.coal.enabled"
            state = getSettings().coalEnabled
            topLeft = guiPosition
            callback = {
                getSettings().coalEnabled = it.state

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.iron.enabled"
            state = getSettings().ironEnabled
            topLeft = guiPosition
            callback = {
                getSettings().ironEnabled = it.state

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.gold.enabled"
            state = getSettings().goldEnabled
            topLeft = guiPosition
            callback = {
                getSettings().goldEnabled = it.state

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.redstone.enabled"
            state = getSettings().redstoneEnabled
            topLeft = guiPosition
            callback = {
                getSettings().redstoneEnabled = it.state

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.diamond.enabled"
            state = getSettings().diamondEnabled
            topLeft = guiPosition
            callback = {
                getSettings().diamondEnabled = it.state

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.lapis.enabled"
            state = getSettings().lapisEnabled
            topLeft = guiPosition
            callback = {
                getSettings().lapisEnabled = it.state

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.copper.enabled"
            state = getSettings().copperEnabled
            topLeft = guiPosition
            callback = {
                getSettings().copperEnabled = it.state

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.emerald.enabled"
            state = getSettings().emeraldEnabled
            topLeft = guiPosition
            callback = {
                getSettings().emeraldEnabled = it.state

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.quartz.enabled"
            state = getSettings().quartzEnabled
            topLeft = guiPosition
            callback = {
                getSettings().quartzEnabled = it.state

            }
        })
        guiPosition = guiPosition.add(0f, 12f)
        addSetting(toggleSetting {
            title = "gavinsmod.mod.blocks.debris.enabled"
            state = getSettings().debrisEnabled
            topLeft = guiPosition
            callback = {
                getSettings().debrisEnabled = it.state

            }
        })
        // find longest title width
        var longestWidth = 0f
        for (setting in guis) {
            val width = Minecraft.getInstance().font.width(setting.title)
            if (width > longestWidth) {
                longestWidth = width.toFloat()
            }
        }
        m_width = (longestWidth * 2f + 24f).toInt()
        // recalculate positions
        guiPosition = PointF((screenWidth - m_width) / 2f - 6f, (screenHeight - m_height) / 2f)
        // set first 10 guis to left side
        for (i in guis.indices) {
            val setting = guis[i]
            setting.width = longestWidth + 12f
            if (i < guis.size / 2) {
                setting.position = guiPosition
                guiPosition = guiPosition.add(0f, 12f)
            } else {
                setting.width = longestWidth + 12f
                if (i == guis.size / 2) {
                    guiPosition = PointF(
                        (screenWidth - m_width) / 2f + (longestWidth) + 18f,
                        (screenHeight - m_height) / 2f
                    )
                }
                setting.position = guiPosition
                guiPosition = guiPosition.add(0f, 12f)
            }
        }
        // add alpha slider at the bottom
        val slider = slideSetting {
            title = "gavinsmod.settings.alpha"
            topLeft = guiPosition
            value = getSettings().alpha
            callback = {
                getSettings().alpha = it.value

            }
        }
        guiPosition = guiPosition.add(0f, 12f)
        val structureEsp = toggleSetting {
            title = "gavinsmod.mod.esp.blockesp.structure"
            state = getSettings().structureEsp
            topLeft = guiPosition
            callback = {
                getSettings().structureEsp = it.state

            }
        }

        val slideWidth = Minecraft.getInstance().font.width(slider.title)
        // set slider in center of X axis
        val centerX = (screenWidth - slideWidth) / 2f

        slider.topLeft = PointF(
            centerX,
            guiPosition.y
        )
        slider.gui?.position = slider.topLeft
        addSetting(slider)

        structureEsp.topLeft = slider.topLeft.add(0f, 12f)
        structureEsp.gui.position = structureEsp.topLeft
        addSetting(structureEsp)
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

    override fun render(drawContext: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(drawContext, mouseX, mouseY, delta)
        seedBox.render(drawContext, mouseX, mouseY, delta)
        seedText.render(drawContext, mouseX, mouseY, delta)
    }

    private fun reload() {
        Settings.getConfig<OreEspConfig>("oreesp").seed = seedBox.value
        ModOreEsp.reload()
    }
}