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

import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.color.Colors;
import com.peasenet.gavui.math.BoxD;
import com.peasenet.gavui.math.PointD;
import com.peasenet.main.Settings;
import com.peasenet.mods.Mod;
import com.peasenet.mods.Type;
import com.peasenet.mods.render.radar.Radar;
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
        Settings.getRadar();
        var playerEntityColor = new ColorSetting("gavinsmod.settings.radar.player.color");
        playerEntityColor.setCallback(() -> Radar.getInstance().setPlayerColor(playerEntityColor.getColor()));
        playerEntityColor.setColor(Radar.getInstance().getPlayerColor());

        var hostileMobEntityColor = new ColorSetting("gavinsmod.settings.radar.mob.hostile.color");
        hostileMobEntityColor.setCallback(() -> Radar.getInstance().setHostileMobColor(hostileMobEntityColor.getColor()));
        hostileMobEntityColor.setColor(Radar.getInstance().getHostileMobColor());

        var peacefulMobEntityColor = new ColorSetting("gavinsmod.settings.radar.mob.peaceful.color");
        peacefulMobEntityColor.setCallback(() -> Radar.getInstance().setPeacefulMobColor(peacefulMobEntityColor.getColor()));
        peacefulMobEntityColor.setColor(Radar.getInstance().getPeacefulMobColor());

        var entityItemColor = new ColorSetting("gavinsmod.settings.radar.item.color");
        entityItemColor.setCallback(() -> Radar.getInstance().setItemColor(entityItemColor.getColor()));
        entityItemColor.setColor(Radar.getInstance().getItemColor());

        var waypointColor = new ColorSetting("gavinsmod.settings.radar.waypoint.color");
        waypointColor.setCallback(() -> Radar.getInstance().setWaypointColor(waypointColor.getColor()));
        waypointColor.setColor(Radar.getInstance().getWaypointColor());

        scaleSetting = new ClickSetting("gavinsmod.settings.radar.scale");
        pointSizeSetting = new ClickSetting("gavinsmod.settings.radar.pointsize");

        var peacefulMobsSetting = new ToggleSetting("gavinsmod.settings.radar.mob.peaceful");
        peacefulMobsSetting.setCallback(() -> Radar.getInstance().setShowPeacefulMob(peacefulMobsSetting.getValue()));
        peacefulMobsSetting.setValue(Radar.getInstance().isShowPeacefulMob());

        var hostileMobsSetting = new ToggleSetting("gavinsmod.settings.radar.mob.hostile");
        hostileMobsSetting.setCallback(() -> Radar.getInstance().setShowHostileMob(hostileMobsSetting.getValue()));
        hostileMobsSetting.setValue(Radar.getInstance().isShowHostileMob());

        var itemsSetting = new ToggleSetting("gavinsmod.settings.radar.item");
        itemsSetting.setCallback(() -> Radar.getInstance().setShowItem(itemsSetting.getValue()));
        itemsSetting.setValue(Radar.getInstance().isShowItem());

        var waypointsSetting = new ToggleSetting("gavinsmod.settings.radar.waypoints");
        waypointsSetting.setCallback(() -> Radar.getInstance().setShowWaypoint(waypointsSetting.getValue()));
        waypointsSetting.setValue(Radar.getInstance().isShowWaypoint());

        var playerSetting = new ToggleSetting("gavinsmod.settings.radar.player");
        playerSetting.setCallback(() -> Radar.getInstance().setShowPlayer(playerSetting.getValue()));
        playerSetting.setValue(Radar.getInstance().isShowPlayer());

        var useWaypointColorSetting = new ToggleSetting("gavinsmod.settings.radar.waypoint.usecolor");
        useWaypointColorSetting.setCallback(() -> Radar.getInstance().setUseWaypointColor(useWaypointColorSetting.getValue()));
        useWaypointColorSetting.setValue(Radar.getInstance().isUseWaypointColor());

        var color = new SubSetting(90, 10, "gavinsmod.settings.radar.color");
        var drawSettings = new SubSetting(90, 10, "gavinsmod.settings.radar.drawn");

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

        color.getGui().getChildren().forEach(c -> c.setWidth(color.getGui().getWidth() + 20));
        drawSettings.getGui().getChildren().forEach(c -> c.setWidth(drawSettings.getGui().getWidth() + 20));

        addSetting(color);
        addSetting(drawSettings);
        updateScaleText(pointSizeSetting, Radar.getInstance().getPointSize());
        updateScaleText(scaleSetting, Radar.getInstance().getScale());
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
        if (Radar.getInstance().getPointSize() == 1) return 0;
        return (Radar.getInstance().getPointSize() - 1) / 2;
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
        Radar.getInstance().increaseScaleCallback();
        updateScaleText(scaleSetting, Radar.getInstance().getScale());
    }

    @Override
    public void onRenderInGameHud(MatrixStack stack, float delta) {
        if (!isActive()) return;
        Radar.setX(getClient().getWindow().getScaledWidth() - Radar.getInstance().getSize() - 10);
        RenderUtils.drawBox(Colors.DARK_GRAY, new BoxD(new PointD(Radar.getInstance().getX(), Radar.getInstance().getY()), Radar.getInstance().getSize(), Radar.getInstance().getSize()), stack, 0.5f);
        drawEntitiesOnRadar(stack);
        if (Radar.getInstance().isShowWaypoint()) drawWaypointsOnRadar(stack);
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
            if (!Radar.getInstance().isUseWaypointColor())
                color = Radar.getInstance().getWaypointColor();
            var location = getScaledPos(w.getPos(), getPointRelativeToYaw(w.getPos(), yaw));
            RenderUtils.drawBox(color, new BoxD(location, Radar.getInstance().getPointSize(), Radar.getInstance().getPointSize()), stack, 1f);
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
            RenderUtils.drawBox(color, new BoxD(point, Radar.getInstance().getPointSize(), Radar.getInstance().getPointSize()), stack, 1f);
        }
    }

    private Color getColorFromEntity(Entity entity) {
        if (entity instanceof PlayerEntity) return Radar.getInstance().getPlayerColor();
        if (entity instanceof ItemEntity) return Radar.getInstance().getItemColor();
        if (entity.getType().getSpawnGroup().isPeaceful()) return Radar.getInstance().getPeacefulMobColor();
        if (!entity.getType().getSpawnGroup().isPeaceful()) return Radar.getInstance().getHostileMobColor();
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
        if (w.distanceTo(getPlayer().getPos()) >= (Radar.getInstance().getSize() >> 1) - Radar.getInstance().getPointSize())
            location = clampPoint(location);
        location = location.add(new PointD(Radar.getInstance().getX() + Radar.getInstance().getSize() / 2.0, Radar.getInstance().getSize() / 2.0 + Radar.getInstance().getY()));
        if (Radar.getInstance().getPointSize() != 1)
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
        var offset = Radar.getInstance().getSize() / 2;

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
        Radar.getInstance().updatePointSizeCallback();
        updateScaleText(pointSizeSetting, Radar.getInstance().getPointSize());
    }

    /**
     * Whether the given entity can be rendered on the radar.
     *
     * @param entity - The entity to check.
     * @return Whether the given entity can be rendered on the radar.
     */
    private boolean canRenderEntity(Entity entity) {
        if (entity instanceof PlayerEntity) return Radar.getInstance().isShowPlayer();
        if (entity instanceof MobEntity) {
            if (!entity.getType().getSpawnGroup().isPeaceful()) return Radar.getInstance().isShowHostileMob();
            return Radar.getInstance().isShowPeacefulMob();
        }
        if (entity instanceof ItemEntity) return Radar.getInstance().isShowItem();
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
