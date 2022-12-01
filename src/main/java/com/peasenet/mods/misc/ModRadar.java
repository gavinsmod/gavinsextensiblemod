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

package com.peasenet.mods.misc;

import com.peasenet.gavui.color.Colors;
import com.peasenet.gavui.math.BoxD;
import com.peasenet.gavui.math.PointD;
import com.peasenet.main.Settings;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.mods.render.waypoints.Waypoint;
import com.peasenet.settings.ClickSetting;
import com.peasenet.settings.ColorSetting;
import com.peasenet.settings.SubSetting;
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

/**
 * A mod that allows for a radar-like view of the world.
 *
 * @version 11/30/2022
 */
public class ModRadar extends Mod {

    /**
     * The key for accessing the radar scale in the settings.
     */
    private static final String RADAR_SCALE_KEY = "radar.scale";

    /**
     * The key for accessing the radar point size in the settings.
     */
    private static final String RADAR_POINT_SIZE_KEY = "radar.pointSize";
    /**
     * Y coordinate of the radar.
     */
    private static final int y = 12;
    /**
     * The size of the radar in pixels.
     */
    public static int radarSize = 0;
    /**
     * The current scale of the radar.
     */
    public static int scale = 4;
    /**
     * The current point size of the radar.
     */
    public static int pointSize = 3;
    /**
     * The setting relating to the radar scale.
     */
    private static ClickSetting scaleSetting;
    /**
     * The setting relating to the radar point size.
     */
    private static ClickSetting pointSizeSetting;
    /**
     * X coordinate of the radar.
     */
    private static int x = 0;

    /**
     * Creates a radar overlay in the top-right corner of the screen.
     */
    public ModRadar() {
        super(Type.RADAR);
        var playerEntityColor = new ColorSetting("radar.player.color", "gavinsmod.settings.radar.player.color");
        var hostileMobEntityColor = new ColorSetting("radar.mob.hostile.color", "gavinsmod.settings.radar.mob.hostile.color");
        var peacefulMobEntityColor = new ColorSetting("radar.mob.peaceful.color", "gavinsmod.settings.radar.mob.peaceful.color");
        var entityItemColor = new ColorSetting("radar.item.color", "gavinsmod.settings.radar.item.color");
        var waypointColor = new ColorSetting("radar.waypoint.color", "gavinsmod.settings.radar.waypoint.color");

        scaleSetting = new ClickSetting(RADAR_SCALE_KEY, "gavinsmod.settings.radar.scale");
        pointSizeSetting = new ClickSetting(RADAR_POINT_SIZE_KEY, "gavinsmod.settings.radar.pointsize");
        var peacefulMobsSetting = new ToggleSetting("radar.mob.peaceful", "gavinsmod.settings.radar.mob.peaceful");
        var hostileMobsSetting = new ToggleSetting("radar.mob.hostile", "gavinsmod.settings.radar.mob.hostile");
        var itemsSetting = new ToggleSetting("radar.item", "gavinsmod.settings.radar.item");
        var waypointsSetting = new ToggleSetting("radar.waypoints", "gavinsmod.settings.radar.waypoints");
        var playerSetting = new ToggleSetting("radar.player", "gavinsmod.settings.radar.player");
        var useWaypointColorSetting = new ToggleSetting("radar.waypoint.usecolor", "gavinsmod.settings.radar.waypoint.usecolor");

        var color = new SubSetting(100, 10, "gavinsmod.settings.radar.color");
        var drawSettings = new SubSetting(100, 10, "gavinsmod.settings.radar.drawn");

        color.add(playerEntityColor);
        color.add(hostileMobEntityColor);
        color.add(peacefulMobEntityColor);
        color.add(entityItemColor);
        color.add(waypointColor);
        color.getGui().getChildren().forEach(c -> c.setWidth(115));

        scaleSetting.setCallback(this::increaseScale);
        pointSizeSetting.setCallback(this::togglePointSize);
        drawSettings.add(pointSizeSetting);
        drawSettings.add(scaleSetting);
        drawSettings.add(peacefulMobsSetting);
        drawSettings.add(hostileMobsSetting);
        drawSettings.add(itemsSetting);
        drawSettings.add(waypointsSetting);
        drawSettings.add(playerSetting);
        drawSettings.add(useWaypointColorSetting);
        drawSettings.getGui().getChildren().forEach(c -> c.setWidth(115));
        addSetting(color);
        addSetting(drawSettings);

        scale = Settings.getInt(RADAR_SCALE_KEY);
        pointSize = Settings.getInt(RADAR_POINT_SIZE_KEY);
        // clamp scale to 1-8
        scale = Math.max(1, Math.min(8, scale));
        // clamp pointsize to either 1 3 or 5
        pointSize = pointSize == 1 ? 1 : pointSize == 5 ? 5 : 3;

        updateScale();
        updateScaleText(pointSizeSetting, pointSize);

    }

    /**
     * Updates the title for the given setting.
     *
     * @param setting - The setting to change the title for.
     * @param value   - The integer value to append to the title.
     */
    private static void updateScaleText(ClickSetting setting, int value) {
        setting.setTitle(Text.translatable(setting.getTranslationKey()).append(Text.literal(" (%s)".formatted(value))));
    }

    /**
     * Calculates the offset to draw the points on the radar.
     *
     * @return The offset to draw the points on the radar.
     */
    public static int getPointOffset() {
        if (pointSize == 1) return 0;
        return (pointSize - 1) / 2;
    }

    /**
     * Calculates the position and distance of the given coordinates from the player.
     *
     * @param yaw - The player yaw.
     * @param x   - The x coordinate.
     * @param z   - The z coordinate.
     * @return The position and distance of the given coordinates from the player.
     */
    @NotNull
    private static PointD calculateDistance(float yaw, double x, double z) {
        var arctan = Math.atan2(z, x);
        var angle = Math.toDegrees(arctan) - yaw;
        var distance = Math.sqrt(x * x + z * z);
        x = Math.cos(Math.toRadians(angle)) * distance * -1;
        z = Math.sin(Math.toRadians(angle)) * distance * -1;
        return new PointD(x, z);
    }

    /**
     * Callback method for the scale setting.
     */
    private void increaseScale() {
        scale++;
        if (scale > 8) scale = 1;
        Settings.add(RADAR_SCALE_KEY, scale);
        Settings.save();
        updateScale();
    }

    /**
     * Callback method for updating the scale of the radar.
     */
    private void updateScale() {
        radarSize = 16 * scale + 1;
        Settings.add(RADAR_SCALE_KEY, scale);
        Settings.save();
        updateScaleText(scaleSetting, scale);
    }

    @Override
    public void onRenderInGameHud(MatrixStack stack, float delta) {
        if (!isActive()) return;
        x = getClient().getWindow().getScaledWidth() - radarSize - 10;
        RenderUtils.drawBox(Colors.DARK_GRAY, new BoxD(new PointD(x, y), radarSize, radarSize), stack, 0.5f);
        drawEntitiesOnRadar(stack);
        drawWaypointsOnRadar(stack);

    }

    /**
     * Draws enabled waypoints on the radar.
     *
     * @param stack - The matrix stack.
     */
    private void drawWaypointsOnRadar(MatrixStack stack) {
        var yaw = getPlayer().getYaw();
        var waypoints = Settings.getWaypoints().stream().filter(Waypoint::isEnabled).toList();
        for (Waypoint w : waypoints) {
            var color = w.getColor();
            if (!Settings.getBool("radar.waypoint.usecolor")) color = Settings.getColor("radar.waypoint.color");
            var location = getScaledPos(w.getPos(), getPointRelativeToYaw(w, yaw));
            RenderUtils.drawBox(color, new BoxD(location, pointSize, pointSize), stack, 1f);
        }
    }

    /**
     * Draws all applicable entities on the radar.
     *
     * @param stack - The matrix stack.
     */
    private void drawEntitiesOnRadar(MatrixStack stack) {
        var yaw = getPlayer().getYaw();
        var entities = getWorld().getEntities();
        for (Entity entity : entities) {
            if (!canRenderEntity(entity)) continue;
            // get entity x and z relative to player
            var color = Settings.getColorFromEntity(entity, "radar");
            var point = getScaledPos(entity.getPos(), getPointRelativeToYaw(entity, yaw));
            RenderUtils.drawBox(color, new BoxD(point, pointSize, pointSize), stack, 1f);
        }
    }

    /**
     * Gets the scaled position relative to the radar.
     *
     * @param w        - The vector to scale.
     * @param location - The location to scale.
     * @return A scaled position relative to the radar, clamped to the radar.
     */
    @NotNull
    private PointD getScaledPos(Vec3d w, PointD location) {
        if (w.distanceTo(getPlayer().getPos()) >= (radarSize / 2) - pointSize)
            location = clampPoint(location);
        location = location.add(new PointD(x + radarSize / 2.0, radarSize / 2.0 + y));
        if (pointSize != 1) location = location.subtract(new PointD(getPointOffset(), getPointOffset()));
        return location;
    }

    /**
     * Clamps the given point to the edges of the radar.
     *
     * @param point - The point to clamp.
     * @return The clamped point.
     */
    private PointD clampPoint(PointD point) {
        if (point.x() >= (radarSize / 2) - getPointOffset())
            point = new PointD(radarSize / 2 - getPointOffset(), point.y());
        else if (point.x() <= -(radarSize / 2) + getPointOffset())
            point = new PointD(-radarSize / 2 + getPointOffset(), point.y());

        if (point.y() >= (radarSize / 2) - getPointOffset())
            point = new PointD(point.x(), radarSize / 2 - getPointOffset());
        else if (point.y() <= -(radarSize / 2) + getPointOffset())
            point = new PointD(point.x(), -radarSize / 2 + getPointOffset());
        return point;
    }

    /**
     * Callback for the point size setting.
     */
    private void togglePointSize() {
        if (pointSize + 2 > 5)
            pointSize = 1;
        else
            pointSize += 2;

        updateScaleText(pointSizeSetting, pointSize);
    }

    /**
     * Whether the given entity can be rendered on the radar.
     *
     * @param entity - The entity to check.
     * @return Whether the given entity can be rendered on the radar.
     */
    private boolean canRenderEntity(Entity entity) {
        if (entity instanceof PlayerEntity)
            return Settings.getBool("radar.player");
        if (entity instanceof MobEntity)
            if (!entity.getType().getSpawnGroup().isPeaceful())
                return Settings.getBool("radar.mob.hostile");
            else
                return Settings.getBool("radar.mob.peaceful");
        if (entity instanceof ItemEntity)
            return Settings.getBool("radar.item");
        return false;
    }

    /**
     * Creates a new PointD based off of the current yaw, player position, and entity position.
     *
     * @param entity - The entity to get the position of.
     * @param yaw    - The yaw of the player.
     * @return A new PointD with the x and z values.
     */
    private PointD getPointRelativeToYaw(Entity entity, float yaw) {
        double x = entity.getX() - getPlayer().getX();
        double z = entity.getZ() - getPlayer().getZ();
        // rotate x and z
        return calculateDistance(yaw, x, z);
    }


    /**
     * Gets the point relative to the waypoint and the player's yaw.
     *
     * @param waypoint - The waypoint to get the position of.
     * @param yaw      - The yaw of the player.
     * @return A new PointD with the x and z values.
     */
    private PointD getPointRelativeToYaw(Waypoint waypoint, float yaw) {
        double x = waypoint.getX() - getPlayer().getX();
        double z = waypoint.getZ() - getPlayer().getZ();
        return calculateDistance(yaw, x, z);
    }
}
