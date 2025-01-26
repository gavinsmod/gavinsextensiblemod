﻿/*
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

import com.peasenet.annotations.Exclude
import com.peasenet.config.Config
import com.peasenet.config.commons.IBlockEspTracerConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.mods.esp.SearchType

/**
 *
 * @author GT3CH1
 * @version 01-18-2025
 * @since 01-18-2025
 */
class CaveEspConfig() : Config<CaveEspConfig>(), IBlockEspTracerConfig {
    init {
        key = "caveesp"
    }

    override var alpha = 0.5f
        set(value) {
            field = value
            saveConfig()
        }

    @Exclude
    override var structureEsp: Boolean = true

    @Exclude
    override var blockTracer: Boolean = false

    override var blockColor: Color = Colors.MEDIUM_SEA_GREEN
        set(value) {
            field = value
            saveConfig()
        }

    /**
     * What [SearchType] to use.
     */
    var searchMode: SearchType = SearchType.Caves
        set(value) {
            field = value
            saveConfig()
        }
}