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

package com.peasenet.util.math;

import com.peasenet.main.GavinsModClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * @author gt3ch1
 * @version 5/23/2022
 */
public class MathUtils {
    /**
     * Gets the rotation to set to see the given entity at its midpoint.
     *
     * @param entity The entity to look at.
     * @return The rotation to set to.
     */
    public static Rotation getRotationToEntity(Entity entity) {
        var player = GavinsModClient.getPlayer();
        var playerPos = new Vec3d(player.getX(), player.getY() + player.getEyeHeight(player.getPose()), player.getZ());
        var diffX = entity.getX() - playerPos.x;
        var diffY = (entity.getBoundingBox().getCenter().y - playerPos.y);
        var diffZ = entity.getZ() - playerPos.z;
        var diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        var yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        var pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));
        return new Rotation(pitch, yaw);
    }
}
