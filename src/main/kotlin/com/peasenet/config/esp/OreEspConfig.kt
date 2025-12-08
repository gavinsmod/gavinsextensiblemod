/*
 * MIT License
 *
 * Copyright (c) 2022-2025, Gavin C. Pease
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.peasenet.config.esp

import com.peasenet.config.commons.BlockListConfig
import com.peasenet.config.commons.IBlockEspTracerConfig
import com.peasenet.gavui.color.Color
import net.minecraft.block.Blocks

/**
 * A configuration for block esp.
 * Default settings are:
 *  Blocks => Sugar Cane
 *  Block Color => Dark Spring Green
 *  Alpha => 0.5
 * @author GT3CH1
 * @version 09-01-2024
 */
class OreEspConfig : BlockListConfig<OreEspConfig>({ it.defaultState == Blocks.COAL_ORE.defaultState }),
    IBlockEspTracerConfig {
    init {
        key = "oreesp"
    }

    var coalColor = Color(47, 44, 54)
        set(value) {
            field = value
            saveConfig()
        }
    var ironColor = Color(236, 173, 119)
        set(value) {
            field = value
            saveConfig()
        }
    var goldColor = Color(247, 229, 30)
        set(value) {
            field = value
            saveConfig()
        }
    var redstoneColor = Color(245, 7, 23)
        set(value) {
            field = value
            saveConfig()
        }
    var diamondColor = Color(33, 244, 255)
        set(value) {
            field = value
            saveConfig()
        }
    var lapisColor = Color(8, 26, 189)
        set(value) {
            field = value
            saveConfig()
        }
    var copperColor = Color(239, 151, 0)
        set(value) {
            field = value
            saveConfig()
        }
    var emeraldColor = Color(27, 209, 45)
        set(value) {
            field = value
            saveConfig()
        }
    var quartzColor = Color(205, 205, 205)
        set(value) {
            field = value
            saveConfig()
        }
    var debrisColor = Color(209, 27, 245)
        set(value) {
            field = value
            saveConfig()
        }

    var coalEnabled: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }
    var ironEnabled: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }
    var goldEnabled: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }
    var redstoneEnabled: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }
    var diamondEnabled: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }
    var lapisEnabled: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }
    var copperEnabled: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }
    var emeraldEnabled: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }
    var quartzEnabled: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }
    var debrisEnabled: Boolean = true
        set(value) {
            field = value
            saveConfig()
        }

    var seed: String = ""
        set(value) {
            field = value
            saveConfig()
        }

    override var blockTracer: Boolean = false
    override var structureEsp: Boolean = false
        set(value) {
            field = value
            saveConfig()
        }
}