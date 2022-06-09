package com.peasenet.util;

import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.mods.Type;
import com.peasenet.util.math.Rotation;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

/**
 * @author gt3ch1
 * @version 5/23/2022
 * A helper class with utilities relating to the player.
 */
public class PlayerUtils {
    static int lastAttackTime = 0;

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
     * Gets the new player position after the change in time.
     *
     * @param deltaTime The change in time.
     * @param camera    The camera to use.
     * @return The new player position.
     */
    public static Vec3f getNewPlayerPosition(float deltaTime, Camera camera) {
        var look = camera.getHorizontalPlane();
        var player = GavinsModClient.getPlayer();
        var px = (float) (player.prevX + (getPlayerPos().getX() - player.prevX) * deltaTime) + look.getX();
        var py = (float) (player.prevY + (getPlayerPos().getY() - player.prevY) * deltaTime) + look.getY()
                + player.getStandingEyeHeight();
        var pz = (float) (player.prevZ + (getPlayerPos().getZ() - player.prevZ) * deltaTime) + look.getZ();
        return new Vec3f(px, py, pz);
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
        if (onGround() && !player.noClip && lastAttackTime % 20 == 0) {
            GavinsModClient.getMinecraftClient().interactionManager.attackEntity(player, entity);
            player.tryAttack(entity);
            player.swingHand(Hand.MAIN_HAND);
            lastAttackTime = 0;
        }
        lastAttackTime++;
    }

    /**
     * Checks whether flight is enabled.
     */
    public static void updateFlight() {
        var player = GavinsModClient.getPlayer();
        if (player == null || player.getAbilities() == null)
            return;
        var abilities = player.getAbilities();
        abilities.allowFlying = GavinsMod.isEnabled(Type.FLY) || abilities.creativeMode || GavinsMod.isEnabled(Type.NO_CLIP);
        if (GavinsMod.isEnabled(Type.FLY) && GavinsMod.isEnabled(Type.NO_CLIP))
            abilities.flying = true;
        if (!abilities.creativeMode && !GavinsMod.isEnabled(Type.FLY) && !GavinsMod.isEnabled(Type.NO_CLIP))
            abilities.flying = false;
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
     * Gets whether the player can be damaged by the current fall speed.
     *
     * @return True if the player can be damaged, false otherwise.
     */
    public static boolean fallSpeedCanDamage() {
        var player = GavinsModClient.getPlayer();
        return player.getVelocity().y < -0.5;
    }

    /**
     * Handles No Fall. This will prevent the player from falling when enabled.
     */
    public static void handleNoFall() {
        //TODO: This is apparently a bit weird in preventing the player from crouching + moving while flying.
        var player = GavinsModClient.getPlayer();
        if (player != null) {
            if (player.fallDistance <= (isFalling() ? 1 : 2))
                return;
            if (player.isSneaking() && !fallSpeedCanDamage() && player.isFallFlying())
                return;
            if (GavinsMod.isEnabled(Type.NO_FALL)) {
                player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
            }
        }
    }

    /**
     * Sets the position of the player to the given position.
     *
     * @param pos The position to set the player to.
     */
    public static void setPosition(Vec3d pos) {
        var player = GavinsModClient.getPlayer();
        player.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Moves the player up by the given amount.
     *
     * @param amount The amount to move the player up.
     */
    public static void moveUp(int amount) {
        var player = GavinsModClient.getPlayer();
        setPosition(new Vec3d(player.getPos().getX(), player.getPos().getY() + amount, player.getPos().getZ()));
    }
}
