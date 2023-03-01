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

package com.peasenet.mods.combat;

import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.util.listeners.PlayerAttackListener;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/**
 * @author gt3ch1
 * @version 6/24/2022
 * A combat mod to make the player jump automatically when attacking an entity.
 */
public class ModAutoCrit extends Mod implements PlayerAttackListener {

    public ModAutoCrit() {
        super(Type.AUTO_CRIT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        em.subscribe(PlayerAttackListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        em.unsubscribe(PlayerAttackListener.class, this);
    }

    @Override
    public void onAttackEntity() {
        var x = getPlayer().getX();
        var y = getPlayer().getY();
        var z = getPlayer().getZ();

        sendPos(x, y + 0.0625D, z, true);
        sendPos(x, y, z, false);
        sendPos(x, y + 1.1E-5D, z, false);
        sendPos(x, y, z, false);
//        getPlayer().jump();
    }

    private void sendPos(double x, double y, double z, boolean onGround) {
        getPlayer().networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround));
    }
}
