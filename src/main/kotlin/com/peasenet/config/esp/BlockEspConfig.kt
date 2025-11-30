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
import net.minecraft.world.level.block.Blocks

/**
 * A configuration for block esp.
 * Default settings are:
 *  Blocks => Sugar Cane
 *  Block Color => Dark Spring Green
 *  Alpha => 0.5
 * @author GT3CH1
 * @version 09-01-2024
 */
class BlockEspConfig : BlockListConfig<BlockEspConfig>({ it.defaultBlockState() == Blocks.SUGAR_CANE.defaultBlockState() }),
    IBlockEspTracerConfig {
    init {
        key = "blockesp"
    }

    override var structureEsp: Boolean = false
        set(value) {
            field = value
            saveConfig()
        }

    override var blockTracer: Boolean = false
        set(value) {
            field = value
            saveConfig()
        }
}