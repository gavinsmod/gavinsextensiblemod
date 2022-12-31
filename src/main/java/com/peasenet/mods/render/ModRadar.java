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

package com.peasenet.mods.render;

import com.peasenet.config.RadarConfig;
import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;
import com.peasenet.gavui.math.BoxD;
import com.peasenet.gavui.math.PointD;
import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.mods.render.waypoints.Waypoint;
import com.peasenet.settings.ClickSetting;
import com.peasenet.settings.ColorSetting;
import com.peasenet.settings.SubSetting;
import com.peasenet.settings.ToggleSetting;
import com.peasenet.util.RenderUtils;
import com.peasenet.util.listeners.InGameHudRenderListener;
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
public class ModRadar extends Mod implements InGameHudRenderListener {

    /**
     * The setting relating to the radar scale.
     */
    private static ClickSetting scaleSetting;
    /**
     * The setting relating to the radar point size.
     */
    private static ClickSetting pointSizeSetting;

    /**
     * Creates a radar overlay in the top-right corner of the screen.
     */
    public ModRadar() {
        super(Type.RADAR);
        var playerEntityColor = new ColorSetting("gavinsmod.settings.radar.player.color");
        playerEntityColor.setCallback(() -> radarConfig.getInstance().setPlayerColor(playerEntityColor.getColor()));
        playerEntityColor.setColor(radarConfig.getInstance().getPlayerColor());

        var hostileMobEntityColor = new ColorSetting("gavinsmod.settings.radar.mob.hostile.color");
        hostileMobEntityColor.setCallback(() -> radarConfig.getInstance().setHostileMobColor(hostileMobEntityColor.getColor()));
        hostileMobEntityColor.setColor(radarConfig.getInstance().getHostileMobColor());

        var peacefulMobEntityColor = new ColorSetting("gavinsmod.settings.radar.mob.peaceful.color");
        peacefulMobEntityColor.setCallback(() -> radarConfig.getInstance().setPeacefulMobColor(peacefulMobEntityColor.getColor()));
        peacefulMobEntityColor.setColor(radarConfig.getInstance().getPeacefulMobColor());

        var entityItemColor = new ColorSetting("gavinsmod.settings.radar.item.color");
        entityItemColor.setCallback(() -> radarConfig.getInstance().setItemColor(entityItemColor.getColor()));
        entityItemColor.setColor(radarConfig.getInstance().getItemColor());

        var waypointColor = new ColorSetting("gavinsmod.settings.radar.waypoint.color");
        waypointColor.setCallback(() -> radarConfig.getInstance().setWaypointColor(waypointColor.getColor()));
        waypointColor.setColor(radarConfig.getInstance().getWaypointColor());

        scaleSetting = new ClickSetting("gavinsmod.settings.radar.scale");
        pointSizeSetting = new ClickSetting("gavinsmod.settings.radar.pointsize");

        var peacefulMobsSetting = new ToggleSetting("gavinsmod.settings.radar.mob.peaceful");
        peacefulMobsSetting.setCallback(() -> radarConfig.getInstance().setShowPeacefulMob(peacefulMobsSetting.getValue()));
        peacefulMobsSetting.setValue(radarConfig.getInstance().isShowPeacefulMob());

        var hostileMobsSetting = new ToggleSetting("gavinsmod.settings.radar.mob.hostile");
        hostileMobsSetting.setCallback(() -> radarConfig.getInstance().setShowHostileMob(hostileMobsSetting.getValue()));
        hostileMobsSetting.setValue(radarConfig.getInstance().isShowHostileMob());

        var itemsSetting = new ToggleSetting("gavinsmod.settings.radar.item");
        itemsSetting.setCallback(() -> radarConfig.getInstance().setShowItem(itemsSetting.getValue()));
        itemsSetting.setValue(radarConfig.getInstance().isShowItem());

        var waypointsSetting = new ToggleSetting("gavinsmod.settings.radar.waypoints");
        waypointsSetting.setCallback(() -> radarConfig.getInstance().setShowWaypoint(waypointsSetting.getValue()));
        waypointsSetting.setValue(radarConfig.getInstance().isShowWaypoint());

        var playerSetting = new ToggleSetting("gavinsmod.settings.radar.player");
        playerSetting.setCallback(() -> radarConfig.getInstance().setShowPlayer(playerSetting.getValue()));
        playerSetting.setValue(radarConfig.getInstance().isShowPlayer());

        var useWaypointColorSetting = new ToggleSetting("gavinsmod.settings.radar.waypoint.usecolor");
        useWaypointColorSetting.setCallback(() -> radarConfig.getInstance().setUseWaypointColor(useWaypointColorSetting.getValue()));
        useWaypointColorSetting.setValue(radarConfig.getInstance().isUseWaypointColor());

        var color = new SubSetting(110, 10, "gavinsmod.settings.radar.color");
        var drawSettings = new SubSetting(110, 10, "gavinsmod.settings.radar.drawn");

        color.add(playerEntityColor);
        color.add(hostileMobEntityColor);
        color.add(peacefulMobEntityColor);
        color.add(entityItemColor);
        color.add(waypointColor);


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

        addSetting(color);
        addSetting(drawSettings);

        updateScaleText(pointSizeSetting, radarConfig.getInstance().getPointSize());
        updateScaleText(scaleSetting, radarConfig.getInstance().getScale());
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
        if (radarConfig.getInstance().getPointSize() == 1) return 0;
        return (radarConfig.getInstance().getPointSize() - 1) / 2;
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

    @Override
    public void onEnable() {
        super.onEnable();
        em.subscribe(InGameHudRenderListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        em.unsubscribe(InGameHudRenderListener.class, this);
    }

    /**
     * Callback method for the scale setting.
     */
    private void increaseScale() {
        radarConfig.getInstance().increaseScaleCallback();
        updateScaleText(scaleSetting, radarConfig.getInstance().getScale());
    }

    @Override
    public void onRenderInGameHud(MatrixStack stack, float delta) {
        if (!isActive()) return;
        RadarConfig.setX(getClient().getWindow().getScaledWidth() - radarConfig.getInstance().getSize() - 10);
        RenderUtils.drawBox(Colors.DARK_GRAY, new BoxD(new PointD(radarConfig.getInstance().getX(), radarConfig.getInstance().getY()), radarConfig.getInstance().getSize(), radarConfig.getInstance().getSize()), stack, 0.5f);
        drawEntitiesOnRadar(stack);
        if (radarConfig.getInstance().isShowWaypoint()) drawWaypointsOnRadar(stack);
    }

    /**
     * Draws enabled waypoints on the radar.
     *
     * @param stack - The matrix stack.
     */
    private void drawWaypointsOnRadar(MatrixStack stack) {
        var yaw = getPlayer().getYaw();
        var waypoints = GavinsMod.waypointConfig.getLocations().stream().filter(Waypoint::isEnabled).toList();
        for (Waypoint w : waypoints) {
            var color = w.getColor();
            if (!radarConfig.getInstance().isUseWaypointColor())
                color = radarConfig.getInstance().getWaypointColor();
            var location = getScaledPos(w.getPos(), getPointRelativeToYaw(w.getPos(), yaw));
            RenderUtils.drawBox(color, new BoxD(location, radarConfig.getInstance().getPointSize(), radarConfig.getInstance().getPointSize()), stack, 1f);
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
            var color = getColorFromEntity(entity);
            var point = getScaledPos(entity.getPos(), getPointRelativeToYaw(entity.getPos(), yaw));
            RenderUtils.drawBox(color, new BoxD(point, radarConfig.getInstance().getPointSize(), radarConfig.getInstance().getPointSize()), stack, 1f);
        }
    }

    private Color getColorFromEntity(Entity entity) {
        if (entity instanceof PlayerEntity) return radarConfig.getInstance().getPlayerColor();
        if (entity instanceof ItemEntity) return radarConfig.getInstance().getItemColor();
        if (entity.getType().getSpawnGroup().isPeaceful())
            return radarConfig.getInstance().getPeacefulMobColor();
        if (!entity.getType().getSpawnGroup().isPeaceful())
            return radarConfig.getInstance().getHostileMobColor();
        return Colors.WHITE;
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
        if (w.distanceTo(getPlayer().getPos()) >= (radarConfig.getInstance().getSize() >> 1) - radarConfig.getInstance().getPointSize())
            location = clampPoint(location);
        location = location.add(new PointD(radarConfig.getInstance().getX() + radarConfig.getInstance().getSize() / 2.0, radarConfig.getInstance().getSize() / 2.0 + radarConfig.getInstance().getY()));
        if (radarConfig.getInstance().getPointSize() != 1)
            location = location.subtract(new PointD(getPointOffset(), getPointOffset()));
        return location;
    }

    /**
     * Clamps the given point to the edges of the radar.
     *
     * @param point - The point to clamp.
     * @return The clamped point.
     */
    private PointD clampPoint(PointD point) {
        var offset = radarConfig.getInstance().getSize() / 2;

        if (point.x() >= (offset) - getPointOffset())
            point = new PointD(offset - getPointOffset(), point.y());
        else if (point.x() <= -(offset) + getPointOffset())
            point = new PointD(-offset + getPointOffset(), point.y());
        if (point.y() >= (offset) - getPointOffset())
            point = new PointD(point.x(), offset - getPointOffset());
        else if (point.y() <= -(offset) + getPointOffset())
            point = new PointD(point.x(), -offset + getPointOffset());
        return point;
    }

    /**
     * Callback for the point size setting.
     */
    private void togglePointSize() {
        radarConfig.getInstance().updatePointSizeCallback();
        updateScaleText(pointSizeSetting, radarConfig.getInstance().getPointSize());
    }

    /**
     * Whether the given entity can be rendered on the radar.
     *
     * @param entity - The entity to check.
     * @return Whether the given entity can be rendered on the radar.
     */
    private boolean canRenderEntity(Entity entity) {
        if (entity instanceof PlayerEntity) return radarConfig.getInstance().isShowPlayer();
        if (entity instanceof MobEntity) {
            if (!entity.getType().getSpawnGroup().isPeaceful())
                return radarConfig.getInstance().isShowHostileMob();
            return radarConfig.getInstance().isShowPeacefulMob();
        }
        if (entity instanceof ItemEntity) return radarConfig.getInstance().isShowItem();
        return false;
    }

    /**
     * Gets the point relative to the waypoint and the player's yaw.
     *
     * @param loc - The waypoint to get the position of.
     * @param yaw - The yaw of the player.
     * @return A new PointD with the x and z values.
     */
    private PointD getPointRelativeToYaw(Vec3d loc, float yaw) {
        double x = loc.getX() - getPlayer().getX();
        double z = loc.getZ() - getPlayer().getZ();
        return calculateDistance(yaw, x, z);
    }
}
