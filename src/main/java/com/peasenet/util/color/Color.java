package com.peasenet.util.color;

public class Color {

    private final int red;

    private final int green;

    private final int blue;

    private final int alpha;

    /**
     * Creates a new RGBA color. Must be between 0 and 255.
     * 0 being completely "off" and 255 being completely "on".
     *
     * @param red Red value.
     * @param green Green value.
     * @param blue Blue value.
     * @param alpha (optional) - defaults to 255
     */
    public Color(int red, int green, int blue, int alpha) {
        this.red = red % 256;
        this.green = green % 256;
        this.blue = blue % 256;
        this.alpha = alpha % 256;
    }

    /**
     * Creates a new RGBA color. Must be between 0 and 255.
     * @param red Red value.
     * @param green Green value.
     * @param blue Blue value.
     */
    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    /**
     * Gets the red value of this color. Will be within 0 and 1.
     * @return red value
     */
    public float getRed() {
        return red/255f;
    }

    /**
     * Gets the green value of this color. Will be within 0 and 1.
     * @return green value
     */
    public float getGreen() {
        return green/255f;
    }

    /**
     * Gets the blue value of this color. Will be within 0 and 1.
     * @return blue value
     */
    public float getBlue() {
        return blue/255f;
    }

    /**
     * Gets the alpha value of this color. Will be within 0 and 1.
     * @return alpha value
     */
    public float getAlpha() {
        return alpha/255f;
    }

    /**
     * Gets the float array value of this color. values range from 0 to 1.
     * @return float array of color values
     */
    public float[] getAsFloatArray() {
        return new float[]{getRed(), getGreen(), getBlue(), getAlpha()};
    }


}
