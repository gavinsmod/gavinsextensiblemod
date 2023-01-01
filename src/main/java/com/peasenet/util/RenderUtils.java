/*
 * Copyright (c) 2022-2022. Gavin Pease and contributors.
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

package com.peasenet.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peasenet.gavui.color.Color;
import com.peasenet.gavui.math.BoxD;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.mixinterface.ISimpleOption;
import com.peasenet.util.event.BlockEntityRenderEvent;
import com.peasenet.util.event.EntityRenderEvent;
import com.peasenet.util.event.WorldRenderEvent;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

/**
 * @author gt3ch1
 * @version 13/31/2022
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
    public static void renderSingleLine(MatrixStack stack, VertexConsumer buffer, Vec3d playerPos, Vec3d boxPos, Color color) {
        renderSingleLine(stack, buffer, playerPos, boxPos, color, 1);
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
    public static void renderSingleLine(MatrixStack stack, VertexConsumer buffer, Vec3d playerPos, Vec3d boxPos, Color color, float alpha) {
        Vec3d normal = new Vec3d(boxPos.getX() - playerPos.getX(), boxPos.getY() - playerPos.getY(), boxPos.getZ() - playerPos.getZ());
        normal.normalize();
        Matrix4f matrix4f = stack.peek().getPositionMatrix();
        Matrix3f matrix3f = stack.peek().getNormalMatrix();
        buffer.vertex(matrix4f, (float) playerPos.getX(), (float) playerPos.getY(), (float) playerPos.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), alpha).normal(matrix3f, (float) normal.getX(), (float) normal.getY(), (float) normal.getZ()).next();
        buffer.vertex(matrix4f, (float) boxPos.getX(), (float) boxPos.getY(), (float) boxPos.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), alpha).normal(matrix3f, (float) normal.getX(), (float) normal.getY(), (float) normal.getZ()).next();
    }

    /**
     * Processes events for rendering player, chest, item, and mob tracers or esp's in the world.
     *
     * @param context The render context.
     */
    public static void afterEntities(WorldRenderContext context) {
//        RenderUtils.context = context;
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
        Vec3d playerPos = PlayerUtils.getNewPlayerPosition(delta, mainCamera);
        assert level != null;
        int chunk_x = player.getChunkPos().x;
        int chunk_z = player.getChunkPos().z;

//        drawChestMods(level, stack, buffer, playerPos, chunk_x, chunk_z, delta);
        drawEntityMods(level, player, stack, delta, buffer, playerPos);
        WorldRenderEvent event = new WorldRenderEvent(level, stack, buffer, delta);
        GavinsMod.eventManager.call(event);
        tessellator.draw();
        stack.pop();
        resetRenderSystem();
    }

    public static boolean beforeBlockOutline(WorldRenderContext context, HitResult h) {
        CHUNK_RADIUS = GavinsModClient.getMinecraftClient().getOptions().getViewDistance().getValue();
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
        Vec3d playerPos = PlayerUtils.getNewPlayerPosition(delta, mainCamera);
        assert level != null;
        int chunk_x = player.getChunkPos().x;
        int chunk_z = player.getChunkPos().z;

        drawBlockMods(level, stack, buffer, playerPos, chunk_x, chunk_z, delta);

        tessellator.draw();
        stack.pop();
        resetRenderSystem();
        return true;
    }

    /**
     * Resets the render system to the default state.
     */
    private static void resetRenderSystem() {
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    /**
     * Sets up the render system for the tracers and esps to work.
     */
    private static void setupRenderSystem() {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
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
    private static void drawBlockMods(ClientWorld level, MatrixStack stack, BufferBuilder buffer, Vec3d playerPos, int chunk_x, int chunk_z, float delta) {
        for (int x = -CHUNK_RADIUS; x <= CHUNK_RADIUS; x++) {
            for (int z = -CHUNK_RADIUS; z <= CHUNK_RADIUS; z++) {
                int chunk_x_ = chunk_x + x;
                int chunk_z_ = chunk_z + z;
                if (level.getChunk(chunk_x_, chunk_z_) != null) {
                    var blockEntities = level.getChunk(chunk_x_, chunk_z_).getBlockEntities();
                    for (var entry : blockEntities.entrySet()) {
                        var blockPos = entry.getKey();
                        var blockEntity = entry.getValue();
                        Box aabb = new Box(blockPos);
                        Vec3d boxPos = aabb.getCenter();
                        BlockEntityRenderEvent event = new BlockEntityRenderEvent(blockEntity, stack, buffer, boxPos, playerPos, delta);
                        GavinsMod.eventManager.call(event);
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
    public static void drawBox(MatrixStack stack, BufferBuilder buffer, Box aabb, Color c) {
        drawBox(stack, buffer, aabb, c, 1);
    }

    /**
     * Draws a box on the world.
     *
     * @param stack  The matrix stack.
     * @param buffer The buffer to write to.
     * @param aabb   The box to draw.
     * @param c      The color to draw the box in.
     * @param alpha  The alpha of the box.
     */
    public static void drawBox(MatrixStack stack, BufferBuilder buffer, Box aabb, Color c, float alpha) {
        WorldRenderer.drawBox(stack, buffer, aabb, c.getRed(), c.getGreen(), c.getBlue(), alpha);
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
    private static void drawEntityMods(ClientWorld level, ClientPlayerEntity player, MatrixStack stack, float delta, BufferBuilder buffer, Vec3d playerPos) {
        level.getEntities().forEach(e -> {
            if ((e.squaredDistanceTo(player) > 64 * CHUNK_RADIUS * 16) || player == e) return;
            Box aabb = getEntityBox(delta, e);
            Vec3d boxPos = aabb.getCenter();
            EntityRenderEvent event = new EntityRenderEvent(e, stack, buffer, boxPos, playerPos, delta);
            GavinsMod.eventManager.call(event);
        });
    }

    /**
     * Gets the bounding box of an entity.
     *
     * @param delta The delta time.
     * @param e     The entity.
     * @return The bounding box of the entity.
     */
    public static Box getEntityBox(float delta, Entity e) {
        double x = e.prevX + (e.getX() - e.prevX) * delta;
        double y = e.prevY + (e.getY() - e.prevY) * delta;
        double z = e.prevZ + (e.getZ() - e.prevZ) * delta;
        return e.getType().createSimpleBoundingBox(x, y, z);
    }

    /**
     * Sets the gamma of the game to the full bright value of 10000.0 while storing the last gamma value.
     */
    public static void setHighGamma() {
        if (GavinsMod.fullbrightConfig.isGammaFade()) {
            fadeGammaUp();
        } else {
            setGamma(GavinsMod.fullbrightConfig.getMaxGamma());
        }
    }

    /**
     * Resets the gamma to the players last configured value.
     */
    public static void setLowGamma() {
        if (GavinsMod.fullbrightConfig.isGammaFade()) {
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
        var maxGamma = GavinsMod.fullbrightConfig.getMaxGamma();
        if (gamma < 0.0) gamma = 0.0;
        if (gamma > maxGamma) gamma = maxGamma;
        var newGamma = GavinsModClient.getMinecraftClient().getOptions().getGamma();
        if (newGamma.getValue() != gamma) {
            @SuppressWarnings("unchecked") var newGamma2 = (ISimpleOption<Double>) (Object) newGamma;
            newGamma2.forceSetValue(gamma);
        }
    }

    /**
     * Whether the gamma is set to its full bright value.
     *
     * @return Whether the gamma is set to its full bright value.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isHighGamma() {
        return getGamma() == 16;
    }

    /**
     * Whether the gamma is currently at its last user configured value.
     *
     * @return Whether the gamma is currently at its last user configured value.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isLastGamma() {
        return getGamma() <= LAST_GAMMA;
    }

    /**
     * Sets the gamma to the last user configured value.
     */
    public static void setLastGamma() {
        if (getGamma() > 1) return;
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
        if (getGamma() < getLastGamma()) setGamma(getLastGamma());
    }

    /**
     * Draws a box within the area of the given BoxD.
     *
     * @param acColor     The color of the box.
     * @param box         The box to draw.
     * @param matrixStack The matrix stack.
     */
    public static void drawBox(float[] acColor, BoxD box, MatrixStack matrixStack, float alpha) {
        drawBox(acColor, (int) box.getTopLeft().x(), (int) box.getTopLeft().y(), (int) box.getBottomRight().x(), (int) box.getBottomRight().y(), matrixStack, alpha);
    }

    public static void drawBox(Color c, BoxD box, MatrixStack matrixStack, float alpha) {
        drawBox(c.getAsFloatArray(), box, matrixStack, alpha);
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
     * @param alpha       The alpha value of the box.
     */
    public static void drawBox(float[] acColor, int xt1, int yt1, int xt2, int yt2, MatrixStack matrixStack, float alpha) {
        // set alpha to be between 0 and 1
        alpha = Math.max(0, Math.min(1, alpha));
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.enableBlend();
        var matrix = matrixStack.peek().getPositionMatrix();
        var bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShaderColor(acColor[0], acColor[1], acColor[2], alpha);
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
        RenderSystem.setShader(GameRenderer::getPositionProgram);
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
        RenderSystem.setShader(GameRenderer::getPositionProgram);
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