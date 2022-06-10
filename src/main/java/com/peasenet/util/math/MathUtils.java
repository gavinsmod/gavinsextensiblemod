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
