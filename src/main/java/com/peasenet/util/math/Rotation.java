package com.peasenet.util.math;

/**
 * @author gt3ch1
 * @version 5/23/2022
 * A class that represents a rotation (pitch and yaw).
 */
public record Rotation(float pitch, float yaw) {

    /**
     * Gets the pitch of this rotation.
     *
     * @return The pitch of this rotation.
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * Gets the yaw of this rotation.
     *
     * @return The yaw of this rotation.
     */
    public float getYaw() {
        return this.yaw;
    }
}
