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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.GL11;

/**
 * @author gt3ch1
 * Allows rendering of boxes and tracers to the screen.
 */
public class RenderUtils {

    private RenderUtils() {
    }

    /**
     * Draws a single line.
     *
     * @param stack  The matrix stack to use.
     * @param buffer The buffer to use.
     * @param coord1 The first set of coordinates.
     * @param coord2 The second set of coordinates.
     * @param color  The color to draw the line in.
     */
    public static void renderSingleLine(MatrixStack stack, VertexConsumer buffer, Vec3f coord1, Vec3f coord2, Color color) {
        Vec3f normal = new Vec3f(coord2.getX() - coord1.getX(), coord2.getY() - coord1.getY(), coord2.getZ() - coord1.getZ());
        normal.normalize();
        Matrix4f matrix4f = stack.peek().getPositionMatrix();
        Matrix3f matrix3f = stack.peek().getNormalMatrix();
        buffer.vertex(matrix4f, coord1.getX(), coord1.getY(), coord1.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .normal(matrix3f, normal.getX(), normal.getY(), normal.getZ()).next();
        buffer.vertex(matrix4f, coord2.getX(), coord2.getY(), coord2.getZ()).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .normal(matrix3f, normal.getX(), normal.getY(), normal.getZ()).next();
    }

    public static void onRender(WorldRenderContext context) {
        // this helps with lag
        int CHUNK_RADIUS = GavinsModClient.getMinecraftClient().options.viewDistance / 2;
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientWorld level = minecraft.world;
        ClientPlayerEntity player = minecraft.player;
        MatrixStack stack = context.matrixStack();
        float delta = context.tickDelta();
        Camera mainCamera = minecraft.gameRenderer.getCamera();
        Vec3d camera = mainCamera.getPos();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();

        stack.push();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        RenderSystem.applyModelViewMatrix();
        stack.translate(-camera.x, -camera.y, -camera.z);
        Vec3f look = mainCamera.getHorizontalPlane();
        assert player != null;
        float px = (float) (player.prevX + (player.getX() - player.prevX) * delta) + look.getX();
        float py = (float) (player.prevY + (player.getY() - player.prevY) * delta) + look.getY()
                + player.getStandingEyeHeight();
        float pz = (float) (player.prevZ + (player.getZ() - player.prevZ) * delta) + look.getZ();
        Vec3f playerPos = new Vec3f(px, py, pz);
        assert level != null;
        int chunk_x = player.getChunkPos().x;
        int chunk_z = player.getChunkPos().z;

        // prevent an expensive computation!
        if (GavinsMod.ChestEspEnabled() || GavinsMod.ChestTracerEnabled()) {
            for (int x = chunk_x - CHUNK_RADIUS; x <= chunk_x + CHUNK_RADIUS; x++)
                for (int z = chunk_z - CHUNK_RADIUS; z <= chunk_z + CHUNK_RADIUS; z++)
                    drawEspToChests(level, stack, buffer, playerPos, x, z);
        }


        drawEntityEsp(level, player, stack, delta, buffer);
        tessellator.draw();
        stack.pop();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    /**
     * Draws the ESP for a chest.
     *
     * @param level     The world to draw the ESP in.
     * @param stack     The matrix stack to use.
     * @param buffer    The buffer to use.
     * @param playerPos The position of the player.
     * @param chunk_x   The x coordinate of the chunk.
     * @param chunk_z   The z coordinate of the chunk.
     */
    private static void drawEspToChests(ClientWorld level, MatrixStack stack, BufferBuilder buffer, Vec3f playerPos, int chunk_x, int chunk_z) {

        level.getChunk(chunk_x, chunk_z).getBlockEntities().forEach((blockPos, blockEntity) -> {
            if (!(blockEntity instanceof ChestBlockEntity))
                return;

            Box aabb = new Box(blockPos);
            if (GavinsMod.ChestEspEnabled())
                drawBox(stack, buffer, aabb, Colors.PURPLE);

            if (GavinsMod.ChestTracerEnabled()) {
                Vec3d center = aabb.getCenter();
                Vec3f coord1 = new Vec3f((float) center.x, (float) center.y, (float) center.z);
                RenderUtils.renderSingleLine(stack, buffer, playerPos, coord1, Colors.PURPLE);
            }
        });

    }

    /**
     * Draws a box.
     *
     * @param stack  The matrix stack to use.
     * @param buffer The buffer to use.
     * @param box    The box to draw.
     * @param color  The color to draw the box in.
     */
    private static void drawBox(MatrixStack stack, BufferBuilder buffer, Box box, Color color) {
        WorldRenderer.drawBox(stack, buffer, box, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * Draws the ESP for entities.
     *
     * @param level  The world to draw in.
     * @param player The player to draw from.
     * @param stack  The matrix stack to use.
     * @param delta  The delta to use.
     * @param buffer The buffer to use.
     */
    private static void drawEntityEsp(ClientWorld level, ClientPlayerEntity player, MatrixStack stack, float delta, BufferBuilder buffer) {
        level.getEntities().forEach(e -> {
            if ((e.squaredDistanceTo(player) > 1000) || player == e)
                return;

            EntityType<?> type = e.getType();


            double x = e.prevX + (e.getX() - e.prevX) * delta;
            double y = e.prevY + (e.getY() - e.prevY) * delta;
            double z = e.prevZ + (e.getZ() - e.prevZ) * delta;
            Box aabb = type.createSimpleBoundingBox(x, y, z);
            Vec3f coord1 = new Vec3f((float) x, (float) y, (float) z);
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
                    drawTracer(stack, buffer, coord1, aabb, c);
                return;
            }

            if (type.getSpawnGroup().isPeaceful())
                c = Colors.GREEN;
            if (!type.getSpawnGroup().isPeaceful())
                c = Colors.RED;
            if (type.getSpawnGroup().isRare())
                c = Colors.GOLD;
            if (type == EntityType.PLAYER)
                c = Colors.YELLOW;

            if (GavinsMod.EntityEspEnabled())
                drawBox(stack, buffer, aabb, c);
            if (GavinsMod.EntityTracerEnabled())
                drawTracer(stack, buffer, coord1, aabb, c);
        });
    }

    /**
     * Draws a tracer from the player to the entity.
     *
     * @param stack     The matrix stack.
     * @param buffer    The buffer.
     * @param playerPos The player's position.
     * @param aabb      The entity's bounding box.
     * @param c         The color.
     */
    private static void drawTracer(MatrixStack stack, BufferBuilder buffer, Vec3f playerPos, Box aabb, Color c) {
        Vec3d center = aabb.getCenter();
        Vec3f coord1 = new Vec3f((float) center.x, (float) center.y, (float) center.z);
        RenderUtils.renderSingleLine(stack, buffer, playerPos, coord1, c);
    }
}