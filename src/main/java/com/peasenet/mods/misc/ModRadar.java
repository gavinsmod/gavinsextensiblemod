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
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

/**
 * A mod that allows for a radar-like view of the world.
 *
 * @version 11/29/2022
 */
public class ModRadar extends Mod {

    /**
     * The size of the radar in pixels.
     */
    public static int radarSize = 96 + 1;
    public static int scale = 1;

    private static ClickSetting scaleSetting;

    public ModRadar() {
        super(Type.RADAR);
        var playerEntityColor = new ColorSetting("radar.player.color", "gavinsmod.settings.radar.player.color");
        var hostileMobEntityColor = new ColorSetting("radar.mob.hostile.color", "gavinsmod.settings.radar.mob.hostile.color");
        var peacefulMobEntityColor = new ColorSetting("radar.mob.peaceful.color", "gavinsmod.settings.radar.mob.peaceful.color");
        var entityItemColor = new ColorSetting("radar.item.color", "gavinsmod.settings.radar.item.color");
        var waypointColor = new ColorSetting("radar.waypoint.color", "gavinsmod.settings.radar.waypoint.color");

        scaleSetting = new ClickSetting("radar.scale", "gavinsmod.settings.radar.scale");
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

    }

    private static void updateScaleText() {
        scaleSetting.setTitle(Text.of(I18n.translate(scaleSetting.getTranslationKey()) + " (%s)".formatted(scale)));
    }

    private void increaseScale() {
        scale++;
        if (scale > 8) {
            scale = 1;
        }
        updateRadarSize();
        updateScaleText();
    }

    private void updateRadarSize() {
        radarSize = 16 * scale + 1;
    }

    @Override
    public void onRenderInGameHud(MatrixStack stack, float delta) {

        if (!isActive()) return;
        // draw 64x64 radar
        // get screen width and height
        int width = getClient().getWindow().getScaledWidth() - 10 - radarSize;
        int height = 10;
        RenderUtils.drawBox(Colors.DARK_GRAY, new BoxD(new PointD(width, height), radarSize, radarSize), stack, 0.5f);
        // render all cows
        var entities = getWorld().getEntities();
        // get player yaw
        float yaw = getPlayer().getYaw();
        for (Entity entity : entities) {
            if (entity.distanceTo(getPlayer()) > radarSize / 2.0 || !canRenderEntity(entity)) continue;
            // get entity x and z relative to player
            var point = getPointRelativeToYaw(entity, yaw);
            var color = Settings.getColorFromEntity(entity, "radar");
            RenderUtils.drawBox(color, new BoxD(point.add(new PointD(width + radarSize / 2.0, radarSize / 2.0 + 10)), 3, 3), stack, 1f);
        }
        var waypoints = Settings.getWaypoints().stream().filter(Waypoint::isEnabled).toList();
        for (Waypoint w : waypoints) {
            var color = w.getColor();
            if (!Settings.getBool("radar.waypoint.usecolor")) {
                color = Settings.getColor("radar.waypoint.color");
            }
            if (getDistanceToPoint(new PointD(w.getX(), w.getZ())) > radarSize / 2.0) continue;
            var location = getPointRelativeToYaw(w, yaw);
            location = location.add(new PointD(width + radarSize / 2.0, radarSize / 2.0 + 10));
            RenderUtils.drawBox(color, new BoxD(location, 3, 3), stack, 1f);
        }

    }

    private double getDistanceToPoint(PointD point) {
        var playerX = getPlayer().getX();
        var playerZ = getPlayer().getZ();
        var x = point.x() - playerX;
        var z = point.y() - playerZ;
        return Math.sqrt(x * x + z * z);
    }

    private boolean canRenderEntity(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return Settings.getBool("radar.player");
        } else if (entity instanceof MobEntity) {
            if (!entity.getType().getSpawnGroup().isPeaceful()) {
                return Settings.getBool("radar.mob.hostile");
            } else {
                return Settings.getBool("radar.mob.peaceful");
            }
        } else if (entity instanceof ItemEntity) {
            return Settings.getBool("radar.item");
        }
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
        var arctan = Math.atan2(z, x);
        var angle = Math.toDegrees(arctan) - yaw;
        var distance = Math.sqrt(x * x + z * z);
        // convert to x and z
        x = Math.cos(Math.toRadians(angle)) * distance * -1;
        z = Math.sin(Math.toRadians(angle)) * distance * -1;
        return new PointD(x, z);
    }

    private PointD getPointRelativeToYaw(Waypoint waypoint, float yaw) {
        double x = waypoint.getX() - getPlayer().getX();
        double z = waypoint.getZ() - getPlayer().getZ();
        // rotate x and z
        var arctan = Math.atan2(z, x);
        var angle = Math.toDegrees(arctan) - yaw;
        var distance = Math.sqrt(x * x + z * z);
        // convert to x and z
        x = Math.cos(Math.toRadians(angle)) * distance * -1;
        z = Math.sin(Math.toRadians(angle)) * distance * -1;
        return new PointD(x, z);
    }
}
