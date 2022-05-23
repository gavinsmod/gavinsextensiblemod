package com.peasenet.util;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.util.math.Rotation;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

/**
 * @author gt3ch1
 * @version 5/23/2022
 * A helper class with utilities relating to the player.
 */
public class PlayerUtils {
    /**
     * Sets the rotation of the player.
     *
     * @param rotation The rotation to set the view to.
     */
    public static void setRotation(Rotation rotation) {
        var player = GavinsModClient.getPlayer();
        player.setPitch(rotation.getPitch());
        player.setYaw(rotation.getYaw());
    }

    /**
     * Gets the current position of the player.
     *
     * @return The current position of the player.
     */
    public static Vec3d getPlayerPos() {
        var player = GavinsModClient.getPlayer();
        return player.getPos();
    }

    /**
     * Makes the player do a jump.
     */
    public static void doJump() {
        var player = GavinsModClient.getPlayer();
        if (onGround())
            player.jump();
    }

    /**
     * Checks if the player is on the ground.
     *
     * @return True if the player is on the ground, false otherwise.
     */
    public static boolean onGround() {
        var player = GavinsModClient.getPlayer();
        return player.isOnGround();
    }

    /**
     * Attacks the given entity.
     *
     * @param entity The entity to attack.
     */
    public static void attackEntity(Entity entity) {
        var player = GavinsModClient.getPlayer();
        assert GavinsModClient.getMinecraftClient().interactionManager != null;
        GavinsModClient.getMinecraftClient().interactionManager.attackEntity(player, entity);
        player.swingHand(Hand.MAIN_HAND);
    }

    /**
     * Checks whether flight is enabled.
     */
    public static void updateFlight() {
        var player = GavinsModClient.getPlayer();
        if (player == null || player.getAbilities() == null)
            return;
        var abilities = player.getAbilities();
        abilities.allowFlying = GavinsMod.FlyEnabled() || abilities.creativeMode || GavinsMod.NoClipEnabled();
        if (GavinsMod.NoClipEnabled())
            abilities.flying = true;
    }

    /**
     * Gets the distance between the player and the given entity.
     *
     * @param entity The entity to get the distance to.
     * @return The distance between the player and the given entity.
     */
    public static double distanceToEntity(Entity entity) {
        var player = GavinsModClient.getPlayer();
        return player.squaredDistanceTo(entity);
    }

    /**
     * Performs an attack jump (crit)
     *
     * @param entity The entity to attack.
     */
    public static void doAttackJump(Entity entity) {
        var player = GavinsModClient.getPlayer();
        if (onGround())
            player.jump();

        attackEntity(entity);
    }

    /**
     * Gets whether the player is in creative mode.
     *
     * @return True if the player is in creative mode, false otherwise.
     */
    public static boolean isCreative() {
        var player = GavinsModClient.getPlayer();
        return player.getAbilities().creativeMode;
    }

    /**
     * Gets whether the player is falling.
     *
     * @return True if the player is falling, false otherwise.
     */
    public static boolean isFalling() {
        var player = GavinsModClient.getPlayer();
        return player.isFallFlying();
    }

    /**
     * Handles No Fall. This will prevent the player from falling when enabled.
     */
    public static void handleNoFall() {
        var player = GavinsModClient.getPlayer();
        if (player != null)
            player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(GavinsMod.NoFallEnabled()));
    }

}