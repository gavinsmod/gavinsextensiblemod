package com.peasenet.config

import com.peasenet.gavui.color.Colors
import net.minecraft.block.Blocks

class BlockEspConfig : BlockListConfig<BlockEspConfig>({ it.defaultState == Blocks.COBBLESTONE.defaultState }) {
    init {
        key = "blockesp"
    }

    /**
     * Whether to cull blocks.
     */
    var culling = false
        set(value) {
            field = value
            saveConfig()
        }
    
    var blockColor = Colors.DARK_SPRING_GREEN
        set(value) {
            field = value
            saveConfig()
        }
    
    var alpha = 0.5f
        set(value) {
            field = value
            saveConfig()
        }
}