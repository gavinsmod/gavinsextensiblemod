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

package com.peasenet.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.main.Mods;
import com.peasenet.main.Settings;
import com.peasenet.mixinterface.ISimpleOption;
import com.peasenet.mods.Type;
import com.peasenet.mods.render.waypoints.Waypoint;
import com.peasenet.util.color.Color;
import com.peasenet.util.math.BoxD;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.GL11;

/**
 * @author gt3ch1
 * @version 7/5/2022
 * A utility class for rendering tracers and esp's.
 */
public class RenderUtils {
    /**
     * How many chunks away to render things.
     */
    private static int CHUNK_RADIUS = GavinsModClient.getMinecraftClient().getOptions().getViewDistance().getValue();


    /**
     * The last player configured gamma.
     */
    private static double LAST_GAMMA;

    private RenderUtils() {
    }

    /**
     * Draws a single line in the given color.
     *
     * @param stack     The matrix stack to use.
     * @param buffer    The buffer to write to.
     * @param playerPos The position of the player.
     * @param boxPos    The center of the location we want to draw a line to.
     * @param color     The color to draw the line in.
     */
    public static void renderSingleLine(MatrixStack stack, VertexConsumer buffer, Vec3f playerPos,
                                        Vec3f boxPos, Color color) {
        Vec3f normal = new Vec3f(boxPos.getX() - playerPos.getX(), boxPos.getY() - playerPos.getY(), boxPos.getZ() - playerPos.getZ());
        normal.normalize();
        Matrix4f matrix4f = stack.peek().getPositionMatrix();
        Matrix3f matrix3f = stack.peek().getNormalMatrix();
        buffer.vertex(matrix4f, playerPos.getX(), playerPos.getY(), playerPos.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), 0.5f)
                .normal(matrix3f, normal.getX(), normal.getY(), normal.getZ()).next();
        buffer.vertex(matrix4f, boxPos.getX(), boxPos.getY(), boxPos.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), 0.5f)
                .normal(matrix3f, normal.getX(), normal.getY(), normal.getZ()).next();
    }

    /**
     * Processes events for rendering player, chest, item, and mob tracers or esp's in the world.
     *
     * @param context The render context.
     */
    public static void afterEntities(WorldRenderContext context) {
        CHUNK_RADIUS = GavinsModClient.getMinecraftClient().getOptions().getViewDistance().getValue() / 2;
        // this helps with lag
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientWorld level = minecraft.world;
        ClientPlayerEntity player = minecraft.player;
        MatrixStack stack = context.matrixStack();
        float delta = context.tickDelta();
        Camera mainCamera = minecraft.gameRenderer.getCamera();
        Vec3d camera = mainCamera.getPos();

        setupRenderSystem();

        stack.push();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        RenderSystem.applyModelViewMatrix();
        stack.translate(-camera.x, -camera.y, -camera.z);
        assert player != null;
        Vec3f playerPos = PlayerUtils.getNewPlayerPosition(delta, mainCamera);
        assert level != null;
        int chunk_x = player.getChunkPos().x;
        int chunk_z = player.getChunkPos().z;

        drawChestMods(level, stack, buffer, playerPos, chunk_x, chunk_z);
        drawEntityMods(level, player, stack, delta, buffer, playerPos);
        drawWaypoint(stack, buffer, playerPos);
        tessellator.draw();
        stack.pop();

        resetRenderSystem();
    }

    /**
     * Draws the waypoint.
     *
     * @param stack     - The matrix stack to use.
     * @param buffer    - The buffer to write to.
     * @param playerPos - The position of the player.
     */
    private static void drawWaypoint(MatrixStack stack, BufferBuilder buffer, Vec3f playerPos) {
        if (!Mods.getMod("waypoints").isActive())
            return;
        Settings.getWaypoints().stream().filter(Waypoint::isEnabled).forEach(w -> {
            Box aabb = new Box(new BlockPos(w.getX(), w.getY(), w.getZ()));
            Vec3f boxPos = new Vec3f(aabb.getCenter());
            if (w.isTracerEnabled())
                renderSingleLine(stack, buffer, playerPos, boxPos, w.getColor());
            if (w.isEspEnabled())
                drawBox(stack, buffer, aabb, w.getColor());
        });
    }

    /**
     * Resets the render system to the default state.
     */
    private static void resetRenderSystem() {
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    /**
     * Sets up the render system for the tracers and esps to work.
     */
    private static void setupRenderSystem() {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
    }

    /**
     * Draws Chest ESPs and tracers.
     *
     * @param level     The world.
     * @param stack     The matrix stack.
     * @param buffer    The buffer to write to.
     * @param playerPos The player's position.
     * @param chunk_x   The player's chunk x.
     * @param chunk_z   The player's chunk z.
     */
    private static void drawChestMods(ClientWorld level, MatrixStack stack, BufferBuilder buffer, Vec3f playerPos, int chunk_x, int chunk_z) {
        if (GavinsMod.isEnabled(Type.CHEST_ESP) || GavinsMod.isEnabled(Type.CHEST_TRACER)) {
            // For each chunk in the CHUNK_RADIUS centered around chunk_x and chunk_z, draw a chest ESP or tracer.
            for (int x = -CHUNK_RADIUS; x <= CHUNK_RADIUS; x++) {
                for (int z = -CHUNK_RADIUS; z <= CHUNK_RADIUS; z++) {
                    int chunk_x_ = chunk_x + x;
                    int chunk_z_ = chunk_z + z;
                    if (level.getChunk(chunk_x_, chunk_z_) != null) {
                        level.getChunk(chunk_x_, chunk_z_).getBlockEntities().forEach((blockPos, blockEntity) -> {
                                    if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof EnderChestBlockEntity ||
                                            blockEntity instanceof ShulkerBoxBlockEntity) {
                                        Box aabb = new Box(blockPos);
                                        Vec3f boxPos = new Vec3f(aabb.getCenter());
                                        if (GavinsMod.isEnabled(Type.CHEST_ESP))
                                            drawBox(stack, buffer, aabb, Settings.getColor("esp.chest.color"));
                                        if (GavinsMod.isEnabled(Type.CHEST_TRACER)) {
                                            renderSingleLine(stack, buffer, playerPos, boxPos, Settings.getColor("tracer.chest.color"));
                                        }
                                    }
                                }
                        );
                    }
                }
            }
        }
    }

    /**
     * Draws a box on the world.
     *
     * @param stack  The matrix stack.
     * @param buffer The buffer to write to.
     * @param aabb   The box to draw.
     * @param c      The color to draw the box in.
     */
    private static void drawBox(MatrixStack stack, BufferBuilder buffer, Box aabb, Color c) {
        WorldRenderer.drawBox(stack, buffer, aabb, c.getRed(), c.getGreen(), c.getBlue(), 1f);
    }

    /**
     * Draws the Entity based ESP's and tracers.
     *
     * @param level     The world.
     * @param player    The player.
     * @param stack     The matrix stack.
     * @param delta     The change in time.
     * @param buffer    The buffer to write to.
     * @param playerPos The player's position.
     */
    private static void drawEntityMods(ClientWorld level, ClientPlayerEntity player, MatrixStack stack,
                                       float delta, BufferBuilder buffer, Vec3f playerPos) {
        level.getEntities().forEach(e -> {
            if ((e.squaredDistanceTo(player) > 64 * CHUNK_RADIUS * 16) || player == e)
                return;

            EntityType<?> type = e.getType();

            Box aabb = getEntityBox(delta, e, type);
            Vec3f boxPos = new Vec3f(aabb.getCenter());
            if (type == EntityType.ITEM) {
                if (GavinsMod.isEnabled(Type.ENTITY_ITEM_ESP))
                    drawBox(stack, buffer, aabb, Settings.getColor("esp.item.color"));
                if (GavinsMod.isEnabled(Type.ENTITY_ITEM_TRACER))
                    renderSingleLine(stack, buffer, playerPos, boxPos, Settings.getColor("tracer.item.color"));
                return;
            }

            if (type == EntityType.PLAYER) {
                if (GavinsMod.isEnabled(Type.ENTITY_PLAYER_ESP))
                    drawBox(stack, buffer, aabb, Settings.getColor("esp.player.color"));
                if (GavinsMod.isEnabled(Type.ENTITY_PLAYER_TRACER))
                    renderSingleLine(stack, buffer, playerPos, boxPos, Settings.getColor("tracer.player.color"));
                return;
            }

            var espColor = type.getSpawnGroup().isPeaceful() ? Settings.getColor("esp.mob.peaceful.color") : Settings.getColor("esp.mob.hostile.color");
            var tracerColor = type.getSpawnGroup().isPeaceful() ? Settings.getColor("tracer.mob.peaceful.color") : Settings.getColor("tracer.mob.hostile.color");
            if (GavinsMod.isEnabled(Type.MOB_ESP))
                drawBox(stack, buffer, aabb, espColor);
            if (GavinsMod.isEnabled(Type.MOB_TRACER))
                renderSingleLine(stack, buffer, playerPos, boxPos, tracerColor);
        });
    }

    /**
     * Gets the bounding box of an entity.
     *
     * @param delta The delta time.
     * @param e     The entity.
     * @param type  The entity type.
     * @return The bounding box of the entity.
     */
    private static Box getEntityBox(float delta, Entity e, EntityType<?> type) {
        double x = e.prevX + (e.getX() - e.prevX) * delta;
        double y = e.prevY + (e.getY() - e.prevY) * delta;
        double z = e.prevZ + (e.getZ() - e.prevZ) * delta;
        return type.createSimpleBoundingBox(x, y, z);
    }

    /**
     * Sets the gamma of the game to the full bright value of 10000.0 while storing the last gamma value.
     */
    public static void setHighGamma() {
        if (Settings.getBool("render.fullbright.gammafade")) {
            fadeGammaUp();
        } else {
            setGamma(64.0);
        }
    }

    /**
     * Resets the gamma to the players last configured value.
     */
    public static void setLowGamma() {
        if (Settings.getBool("render.fullbright.gammafade")) {
            fadeGammaDown();
        } else {
            setGamma(LAST_GAMMA);
        }
    }

    /**
     * Gets the current game gamma.
     *
     * @return The current game gamma.
     */
    public static double getGamma() {
        return GavinsModClient.getMinecraftClient().getOptions().getGamma().getValue();
    }

    /**
     * Sets the gamma to the given value.
     *
     * @param gamma The value to set the gamma to.
     */
    public static void setGamma(double gamma) {
        if (gamma < 0.0)
            gamma = 0.0;
        if (gamma > 64.0)
            return;
        var newGamma = GavinsModClient.getMinecraftClient().getOptions().getGamma();
        if (newGamma.getValue() != gamma) {
            @SuppressWarnings("unchecked")
            var newGamma2 = (ISimpleOption<Double>) (Object) newGamma;
            newGamma2.forceSetValue(gamma);
        }
    }

    /**
     * Whether the gamma is set to its full bright value.
     *
     * @return Whether the gamma is set to its full bright value.
     */
    public static boolean isHighGamma() {
        return getGamma() == 64;
    }

    /**
     * Whether the gamma is currently at its last user configured value.
     *
     * @return Whether the gamma is currently at its last user configured value.
     */
    public static boolean isLastGamma() {
        return getGamma() <= LAST_GAMMA;
    }

    /**
     * Sets the gamma to the last user configured value.
     */
    public static void setLastGamma() {
        if (getGamma() > 100)
            return;
        LAST_GAMMA = getGamma();
    }

    /**
     * Gets the last user configured value of the gamma.
     *
     * @return The last user configured value of the gamma.
     */
    public static double getLastGamma() {
        return LAST_GAMMA;
    }

    /**
     * Fades the gamma up to the full bright value.
     */
    public static void fadeGammaUp() {
        setGamma(getGamma() + 0.2f);
    }

    /**
     * Fades the gamma down to the last user configured value.
     */
    public static void fadeGammaDown() {
        setGamma(getGamma() - 0.2f);
        if (getGamma() < getLastGamma())
            setGamma(getLastGamma());
    }

    /**
     * Draws a box within the area of the given BoxD.
     *
     * @param acColor     The color of the box.
     * @param box         The box to draw.
     * @param matrixStack The matrix stack.
     */
    public static void drawBox(float[] acColor, BoxD box, MatrixStack matrixStack) {
        drawBox(acColor, (int) box.getTopLeft().x(), (int) box.getTopLeft().y(), (int) box.getBottomRight().x(), (int) box.getBottomRight().y(), matrixStack);
    }

    /**
     * Draws a box on screen.
     *
     * @param acColor     The color of the box as a 4 point float array.
     * @param xt1         The x coordinate of the top left corner of the box.
     * @param yt1         The y coordinate of the top left corner of the box.
     * @param xt2         The x coordinate of the bottom right corner of the box.
     * @param yt2         The y coordinate of the bottom right corner of the box.
     * @param matrixStack The matrix stack used to draw boxes on screen.
     */
    public static void drawBox(float[] acColor, int xt1, int yt1, int xt2, int yt2, MatrixStack matrixStack) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        var matrix = matrixStack.peek().getPositionMatrix();
        var bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], 0.5f);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        drawBox(xt1, yt1, xt2, yt2, matrix, bufferBuilder);
        Tessellator.getInstance().draw();
    }

    /**
     * Draws a box from the given points
     *
     * @param xt1           - The x coordinate of the top left corner of the box.
     * @param yt1           - The y coordinate of the top left corner of the box.
     * @param xt2           - The x coordinate of the bottom right corner of the box.
     * @param yt2           - The y coordinate of the bottom right corner of the box.
     * @param matrix        - The matrix stack used to draw boxes on screen.
     * @param bufferBuilder - The buffer builder used to draw boxes on screen.
     */
    private static void drawBox(int xt1, int yt1, int xt2, int yt2, Matrix4f matrix, BufferBuilder bufferBuilder) {
        bufferBuilder.vertex(matrix, xt1, yt1, 0).next();
        bufferBuilder.vertex(matrix, xt1, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt2, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt1, 0).next();
    }

    /**
     * Draws a line on screen.
     *
     * @param accColor    - The color of the line as a 4 point float array.
     * @param xt1         - The x coordinate of the first point of the line.
     * @param yt1         - The y coordinate of the first point of the line.
     * @param xt2         - The x coordinate of the second point of the line.
     * @param yt2         - The y coordinate of the second point of the line.
     * @param matrixStack - The matrix stack used to draw lines on screen.
     */
    public static void drawSingleLine(float[] accColor, int xt1, int yt1, int xt2, int yt2, MatrixStack matrixStack) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        var matrix = matrixStack.peek().getPositionMatrix();
        var bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShaderColor(accColor[0], accColor[1], accColor[2], 1f);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, xt1, yt1, 0).next();
        bufferBuilder.vertex(matrix, xt2, yt2, 0).next();
        Tessellator.getInstance().draw();
    }

    /**
     * Draws an outline on screen.
     *
     * @param acColor     The color of the box.
     * @param box         The box to draw.
     * @param matrixStack The matrix stack.
     */
    public static void drawOutline(float[] acColor, BoxD box, MatrixStack matrixStack) {
        drawOutline(acColor, (int) box.getTopLeft().x(), (int) box.getTopLeft().y(), (int) box.getBottomRight().x(), (int) box.getBottomRight().y(), matrixStack);
    }


    /**
     * Draws a box outline on screen.
     *
     * @param acColor     The color of the box as a 4 point float array.
     * @param xt1         The x coordinate of the top left corner of the box.
     * @param yt1         The y coordinate of the top left corner of the box.
     * @param xt2         The x coordinate of the bottom right corner of the box.
     * @param yt2         The y coordinate of the bottom right corner of the box.
     * @param matrixStack The matrix stack used to draw boxes on screen.
     */
    public static void drawOutline(float[] acColor, int xt1, int yt1, int xt2, int yt2, MatrixStack matrixStack) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.enableBlend();
        var matrix = matrixStack.peek().getPositionMatrix();
        var bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], 1.0F);
        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION);
        drawBox(xt1, yt1, xt2, yt2, matrix, bufferBuilder);
        bufferBuilder.vertex(matrix, xt1, yt1, 0).next();
        Tessellator.getInstance().draw();
    }

}