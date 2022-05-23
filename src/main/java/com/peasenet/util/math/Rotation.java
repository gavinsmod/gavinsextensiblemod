package com.peasenet.util.math;

/**
 * @author gt3ch1
 * @version 5/23/2022
 */
public class Rotation {
    private final float pitch;
    private final float yaw;

    public Rotation(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }
}
