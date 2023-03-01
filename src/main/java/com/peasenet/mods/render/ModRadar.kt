/*
 * Copyright (c) 2022-2023. Gavin Pease and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
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
import com.peasenet.gavui.math.BoxF;
import com.peasenet.gavui.math.PointF;
import com.peasenet.gavui.util.GuiUtil;
import com.peasenet.main.GavinsMod;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.mods.render.waypoints.Waypoint;
import com.peasenet.settings.*;
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
 * @author gt3ch1
 * @version 01/07/2023
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
        playerEntityColor.setCallback(() -> radarConfig.setPlayerColor(playerEntityColor.getColor()));
        playerEntityColor.setColor(radarConfig.getPlayerColor());

        var hostileMobEntityColor = new ColorSetting("gavinsmod.settings.radar.mob.hostile.color");
        hostileMobEntityColor.setCallback(() -> radarConfig.setHostileMobColor(hostileMobEntityColor.getColor()));
        hostileMobEntityColor.setColor(radarConfig.getHostileMobColor());

        var peacefulMobEntityColor = new ColorSetting("gavinsmod.settings.radar.mob.peaceful.color");
        peacefulMobEntityColor.setCallback(() -> radarConfig.setPeacefulMobColor(peacefulMobEntityColor.getColor()));
        peacefulMobEntityColor.setColor(radarConfig.getPeacefulMobColor());

        var entityItemColor = new ColorSetting("gavinsmod.settings.radar.item.color");
        entityItemColor.setCallback(() -> radarConfig.setItemColor(entityItemColor.getColor()));
        entityItemColor.setColor(radarConfig.getItemColor());

        var waypointColor = new ColorSetting("gavinsmod.settings.radar.waypoint.color");
        waypointColor.setCallback(() -> radarConfig.setWaypointColor(waypointColor.getColor()));
        waypointColor.setColor(radarConfig.getWaypointColor());

        scaleSetting = new ClickSetting("gavinsmod.settings.radar.scale");
        pointSizeSetting = new ClickSetting("gavinsmod.settings.radar.pointsize");

        var peacefulMobsSetting = new ToggleSetting("gavinsmod.settings.radar.mob.peaceful");
        peacefulMobsSetting.setCallback(() -> radarConfig.setShowPeacefulMob(peacefulMobsSetting.getValue()));
        peacefulMobsSetting.setValue(radarConfig.isShowPeacefulMob());

        var hostileMobsSetting = new ToggleSetting("gavinsmod.settings.radar.mob.hostile");
        hostileMobsSetting.setCallback(() -> radarConfig.setShowHostileMob(hostileMobsSetting.getValue()));
        hostileMobsSetting.setValue(radarConfig.isShowHostileMob());

        var itemsSetting = new ToggleSetting("gavinsmod.settings.radar.item");
        itemsSetting.setCallback(() -> radarConfig.setShowItem(itemsSetting.getValue()));
        itemsSetting.setValue(radarConfig.isShowItem());

        var waypointsSetting = new ToggleSetting("gavinsmod.settings.radar.waypoints");
        waypointsSetting.setCallback(() -> radarConfig.setShowWaypoint(waypointsSetting.getValue()));
        waypointsSetting.setValue(radarConfig.isShowWaypoint());

        var playerSetting = new ToggleSetting("gavinsmod.settings.radar.player");
        playerSetting.setCallback(() -> radarConfig.setShowPlayer(playerSetting.getValue()));
        playerSetting.setValue(radarConfig.isShowPlayer());

        var useWaypointColorSetting = new ToggleSetting("gavinsmod.settings.radar.waypoint.usecolor");
        useWaypointColorSetting.setCallback(() -> radarConfig.setUseWaypointColor(useWaypointColorSetting.getValue()));
        useWaypointColorSetting.setValue(radarConfig.isUseWaypointColor());

        var backgroundAlphaSetting = new SlideSetting("gavinsmod.settings.radar.background.alpha");
        backgroundAlphaSetting.setCallback(() -> radarConfig.setBackgroundAlpha(backgroundAlphaSetting.getValue()));
        backgroundAlphaSetting.setValue(radarConfig.getBackgroundAlpha());

        var pointAlphaSetting = new SlideSetting("gavinsmod.settings.radar.point.alpha");
        pointAlphaSetting.setCallback(() -> radarConfig.setPointAlpha(pointAlphaSetting.getValue()));
        pointAlphaSetting.setValue(radarConfig.getPointAlpha());

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
        drawSettings.add(backgroundAlphaSetting);
        drawSettings.add(pointAlphaSetting);

        drawSettings.add(color);

        addSetting(drawSettings);

        updateScaleText(pointSizeSetting, radarConfig.getPointSize());
        updateScaleText(scaleSetting, radarConfig.getScale());
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
    public static float getPointOffset() {
        if (radarConfig.getPointSize() == 1) return 0;
        return (radarConfig.getPointSize() - 1) / 2;
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
    private static PointF calculateDistance(float yaw, double x, double z) {
        var arctan = Math.atan2(z, x);
        var angle = Math.toDegrees(arctan) - yaw;
        var distance = Math.sqrt(x * x + z * z);
        x = Math.cos(Math.toRadians(angle)) * distance * -1;
        z = Math.sin(Math.toRadians(angle)) * distance * -1;
        return new PointF(x, z);
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
        radarConfig.setScale(radarConfig.getScale() + 1);
        updateScaleText(scaleSetting, radarConfig.getScale());
    }

    @Override
    public void onRenderInGameHud(MatrixStack stack, float delta) {
        if (!isActive()) return;
        RadarConfig.x = (getClient().getWindow().getScaledWidth() - RadarConfig.size - 10);
        GuiUtil.drawBox(Colors.DARK_GRAY, new BoxF(new PointF(RadarConfig.x, RadarConfig.y), radarConfig.getSize(), radarConfig.getSize()), stack, radarConfig.getBackgroundAlpha());
        drawEntitiesOnRadar(stack);
        if (radarConfig.isShowWaypoint()) drawWaypointsOnRadar(stack);
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
            if (!radarConfig.isUseWaypointColor()) color = radarConfig.getWaypointColor();
            var location = getScaledPos(w.getPos(), getPointRelativeToYaw(w.getPos(), yaw));
            GuiUtil.drawBox(color, new BoxF(location, radarConfig.getPointSize(), radarConfig.getPointSize()), stack, radarConfig.getPointAlpha());
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
            GuiUtil.drawBox(color, new BoxF(point, radarConfig.getPointSize(), radarConfig.getPointSize()), stack, radarConfig.getPointAlpha());
        }
    }

    private Color getColorFromEntity(Entity entity) {
        if (entity instanceof PlayerEntity) return radarConfig.getPlayerColor();
        if (entity instanceof ItemEntity) return radarConfig.getItemColor();
        if (entity.getType().getSpawnGroup().isPeaceful()) return radarConfig.getPeacefulMobColor();
        if (!entity.getType().getSpawnGroup().isPeaceful()) return radarConfig.getHostileMobColor();
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
    private PointF getScaledPos(Vec3d w, PointF location) {
        if (w.distanceTo(getPlayer().getPos()) >= (radarConfig.getSize() / 2f) - radarConfig.getPointSize())
            location = clampPoint(location);
        location = location.add(new PointF(RadarConfig.x + radarConfig.getSize() / 2f, radarConfig.getSize() / 2f + RadarConfig.y));
        if (radarConfig.getPointSize() != 1)
            location = location.subtract(new PointF(getPointOffset(), getPointOffset()));
        return location;
    }

    /**
     * Clamps the given point to the edges of the radar.
     *
     * @param point - The point to clamp.
     * @return The clamped point.
     */
    private PointF clampPoint(PointF point) {
        float offset = radarConfig.getSize() / 2f;
        if (point.x() >= offset - getPointOffset()) point = new PointF(offset - getPointOffset() - 1f, point.y());
        else if (point.x() <= -(offset) + getPointOffset()) point = new PointF(-offset + getPointOffset(), point.y());
        if (point.y() >= (offset) - getPointOffset()) point = new PointF(point.x(), offset - getPointOffset() - 1f);
        else if (point.y() <= -(offset) + getPointOffset()) point = new PointF(point.x(), -offset + getPointOffset());
        return point;
    }

    /**
     * Callback for the point size setting.
     */
    private void togglePointSize() {
        radarConfig.updatePointSizeCallback();
        updateScaleText(pointSizeSetting, radarConfig.getPointSize());
    }

    /**
     * Whether the given entity can be rendered on the radar.
     *
     * @param entity - The entity to check.
     * @return Whether the given entity can be rendered on the radar.
     */
    private boolean canRenderEntity(Entity entity) {
        if (entity instanceof PlayerEntity) return radarConfig.isShowPlayer();
        if (entity instanceof MobEntity) {
            if (!entity.getType().getSpawnGroup().isPeaceful()) return radarConfig.isShowHostileMob();
            return radarConfig.isShowPeacefulMob();
        }
        if (entity instanceof ItemEntity) return radarConfig.isShowItem();
        return false;
    }

    /**
     * Gets the point relative to the waypoint and the player's yaw.
     *
     * @param loc - The waypoint to get the position of.
     * @param yaw - The yaw of the player.
     * @return A new PointF with the x and z values.
     */
    private PointF getPointRelativeToYaw(Vec3d loc, float yaw) {
        double x = loc.getX() - getPlayer().getX();
        double z = loc.getZ() - getPlayer().getZ();
        return calculateDistance(yaw, x, z);
    }
}
