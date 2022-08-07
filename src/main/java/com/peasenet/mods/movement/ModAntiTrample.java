/*
 * Copyright (c) 2022. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 *  of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
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

package com.peasenet.mods.movement;

import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

/**
 * @author gt3ch1
 * @version 8/6/2022
 * AntiTrample prevents the player from trampling over farm blocks.
 */
public class ModAntiTrample extends Mod {

    private static final Block FARMLAND = Blocks.FARMLAND;
    private static final boolean wasOnFarmland = false;

    public ModAntiTrample() {
        super(Type.ANTI_TRAMPLE);
    }

    @Override
    public void onTick() {
//        // check if the player is on farmland by looking at the block below the player.
//        var playerLoc = getPlayer().getBlockPos();
//        var playerLocDown = getPlayer().getBlockPos().down();
//        var playerBlock = getWorld().getBlockState(playerLoc).getBlock();
//        var playerBlockDown  = getWorld().getBlockState(playerLocDown).getBlock();
//        var isOnFarmland = playerBlock == FARMLAND || playerBlockDown == FARMLAND;
//        if(isOnFarmland) {
//            getClient().getOptions().sneakKey.setPressed(true);
//        }
//        if(!wasOnFarmland) {
//            getClient().getOptions().sneakKey.setPressed(false);
//            return;
//        }
//        wasOnFarmland = isOnFarmland;
    }

}
