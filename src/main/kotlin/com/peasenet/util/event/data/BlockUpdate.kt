﻿package com.peasenet.util.event.data

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class BlockUpdate (var blockPos: BlockPos, var oldState: BlockState, var newState: BlockState){
}