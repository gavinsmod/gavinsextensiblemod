package com.peasenet.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.util.color.Color;
import com.peasenet.util.color.Colors;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.GL11;

/**
 * @author gt3ch1
 * @version 5/23/2022
 * A utility class for rendering tracers and esp's.
 */
public class RenderUtils {
    private static int CHUNK_RADIUS = GavinsModClient.getMinecraftClient().options.viewDistance / 2;

    private RenderUtils() {
    }

    /**
     * Draws a single line in the given color.
     * @param stack The matrix stack to use.
     * @param buffer The buffer to write to.
     * @param playerPos The position of the player.
     * @param boxPos The center of the location we want to draw a line to.
     * @param color The color to draw the line in.
     */
    public static void renderSingleLine(MatrixStack stack, VertexConsumer buffer, Vec3f playerPos,
                                        Vec3f boxPos, Color color) {
        Vec3f normal = new Vec3f(boxPos.getX() - playerPos.getX(), boxPos.getY() - playerPos.getY(), boxPos.getZ() - playerPos.getZ());
        normal.normalize();
        Matrix4f matrix4f = stack.peek().getPositionMatrix();
        Matrix3f matrix3f = stack.peek().getNormalMatrix();
        buffer.vertex(matrix4f, playerPos.getX(), playerPos.getY(), playerPos.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .normal(matrix3f, normal.getX(), normal.getY(), normal.getZ()).next();
        buffer.vertex(matrix4f, boxPos.getX(), boxPos.getY(), boxPos.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .normal(matrix3f, normal.getX(), normal.getY(), normal.getZ()).next();
    }


    /**
     * Processes events for rendering player, chest, item, and mob tracers or esp's in the world.
     * @param context The render context.
     */
    public static void afterEntities(WorldRenderContext context) {
        CHUNK_RADIUS = GavinsModClient.getMinecraftClient().options.viewDistance / 2;
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
        tessellator.draw();
        stack.pop();

        resetRenderSystem();
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
        if (GavinsMod.ChestEspEnabled() || GavinsMod.ChestTracerEnabled()) {
            for (int x = chunk_x - CHUNK_RADIUS; x <= chunk_x + CHUNK_RADIUS; x++) {
                for (int z = chunk_z - CHUNK_RADIUS; z <= chunk_z + CHUNK_RADIUS; z++) {
                    level.getChunk(chunk_x, chunk_z).getBlockEntities().forEach((blockPos, blockEntity) -> {
                        if (!(blockEntity instanceof ChestBlockEntity))
                            return;

                        Box aabb = new Box(blockPos);
                        Vec3f boxPos = new Vec3f(aabb.getCenter());
                        if (GavinsMod.ChestEspEnabled())
                            drawBox(stack, buffer, aabb, Colors.PURPLE);

                        if (GavinsMod.ChestTracerEnabled())
                            renderSingleLine(stack, buffer, playerPos, boxPos, Colors.PURPLE);

                    });
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
        WorldRenderer.drawBox(stack, buffer, aabb, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
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
    private static void drawEntityMods(ClientWorld level, ClientPlayerEntity player, MatrixStack stack, float delta, BufferBuilder buffer, Vec3f playerPos) {
        level.getEntities().forEach(e -> {
            if ((e.squaredDistanceTo(player) > 64 * CHUNK_RADIUS * 16) || player == e)
                return;

            EntityType<?> type = e.getType();

            Box aabb = getEntityBox(delta, e, type);
            Vec3f boxPos = new Vec3f(aabb.getCenter());
            Color c = Colors.PURPLE;

            if (type == EntityType.ITEM) {
                c = Colors.DARK_CYAN;
                Item i = ((ItemEntity) e).getStack().getItem();
                //TODO: This will be a list of rare items.
                if (i.asItem() == Items.CREEPER_SPAWN_EGG) {
                    c = Colors.WHITE;
                }
                if (GavinsMod.EntityItemEspEnabled())
                    drawBox(stack, buffer, aabb, c);
                if (GavinsMod.EntityItemTracerEnabled())
                    renderSingleLine(stack, buffer, playerPos, boxPos, c);
                return;
            }

            if (type == EntityType.PLAYER) {
                if (GavinsMod.EntityPlayerEspEnabled())
                    drawBox(stack, buffer, aabb, c);
                if (GavinsMod.EntityPlayerTracerEnabled())
                    renderSingleLine(stack, buffer, playerPos, boxPos, c);
                return;
            }

            if (type.getSpawnGroup().isPeaceful())
                c = Colors.GREEN;
            if (!type.getSpawnGroup().isPeaceful())
                c = Colors.RED;

            if (GavinsMod.EntityEspEnabled())
                drawBox(stack, buffer, aabb, c);
            if (GavinsMod.EntityTracerEnabled())
                renderSingleLine(stack, buffer, playerPos, boxPos, c);
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
}