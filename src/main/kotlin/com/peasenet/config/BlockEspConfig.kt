package com.peasenet.config

import net.minecraft.block.Blocks

class BlockEspConfig : BlockListConfig<BlockEspConfig>({ it.defaultState == Blocks.COBBLESTONE.defaultState }) {
    init {
        key = "blockesp"
    }
}