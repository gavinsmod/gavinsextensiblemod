/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.peasenet.mods.movement

import net.minecraft.block.Blocks

/**
 * @author gt3ch1
 * @version 03-02-2023
 * AntiTrample prevents the player from trampling over farm blocks.
 */
class ModAntiTrample : MovementMod(
    "Anti Trample",
    "gavinsmod.mod.movement.antitrample",
    "antitrample"
) {
    override fun onTick() {
        // check if the player is on farmland by looking at the block below the player.
        val player = client.getPlayer()
        val playerLoc = player.blockPos
        val playerLocDown = player.blockPos.down()
        val playerBlock = world.getBlockState(playerLoc).block
        val playerBlockDown = world.getBlockState(playerLocDown).block
        val isOnFarmland = playerBlock === FARMLAND || playerBlockDown === FARMLAND
        if (isOnFarmland) {
            client.options.sneakKey.isPressed = true
            wasOnFarmland = true
            return
        }
        if (wasOnFarmland) {
            client.options.sneakKey.isPressed = false
            wasOnFarmland = false
        }
    }

    companion object {
        private val FARMLAND = Blocks.FARMLAND
        private var wasOnFarmland = false
    }
}