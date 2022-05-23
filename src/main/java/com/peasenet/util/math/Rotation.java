package com.peasenet.util.math;

/**
 * @author gt3ch1
 * @version 5/23/2022
 */
public record Rotation(float pitch, float yaw) {

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }
}
