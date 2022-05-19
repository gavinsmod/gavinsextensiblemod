package com.peasenet.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.peasenet.main.GavinsMod;
import com.peasenet.main.GavinsModClient;
import com.peasenet.util.color.Color;
import com.peasenet.util.color.Colors;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.entity.BlockEntity;
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

public class RenderUtils {

    private RenderUtils() {
    }

    public static void renderSingleLine(MatrixStack stack, VertexConsumer buffer, float x1, float y1, float z1,
                                        float x2, float y2,
                                        float z2, float r, float g, float b, float a) {
        Vec3f normal = new Vec3f(x2 - x1, y2 - y1, z2 - z1);
        normal.normalize();
        renderSingleLine(stack, buffer, x1, y1, z1, x2, y2, z2, r, g, b, a, normal.getX(), normal.getY(),
                normal.getZ());
    }

    public static void renderSingleLine(MatrixStack stack, VertexConsumer buffer, float x1, float y1, float z1,
                                        float x2, float y2,
                                        float z2, float r, float g, float b, float a, float normalX, float normalY, float normalZ) {
        Matrix4f matrix4f = stack.peek().getPositionMatrix();
        Matrix3f matrix3f = stack.peek().getNormalMatrix();
        buffer.vertex(matrix4f, x1, y1, z1).color(r, g, b, a)
                .normal(matrix3f, normalX, normalY, normalZ).next();
        buffer.vertex(matrix4f, x2, y2, z2).color(r, g, b, a)
                .normal(matrix3f, normalX, normalY, normalZ).next();
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
        float px = (float) (player.prevX + (player.getX() - player.prevX) * delta) + look.getX();
        float py = (float) (player.prevY + (player.getY() - player.prevY) * delta) + look.getY()
                + player.getStandingEyeHeight();
        float pz = (float) (player.prevZ + (player.getZ() - player.prevZ) * delta) + look.getZ();

        assert level != null;
        int chunk_x = player.getChunkPos().x;
        int chunk_z = player.getChunkPos().z;
        // For all chunks in a radius of CHUNK_RADIUS from the player, draw the esp.
        if (GavinsMod.ChestFinderEnabled()) {
            for (int x = chunk_x - CHUNK_RADIUS; x <= chunk_x + CHUNK_RADIUS; x++) {
                for (int z = chunk_z - CHUNK_RADIUS; z <= chunk_z + CHUNK_RADIUS; z++) {
                    drawEspToChests(level, stack, delta, buffer, px, py, pz, x, z);
                }
            }
        }
        if (GavinsMod.MobTracerEnabled()) {
            drawEntityEsp(level, player, stack, delta, buffer, px, py, pz);
        }
        tessellator.draw();
        stack.pop();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    private static void drawEspToChests(ClientWorld level, MatrixStack stack, float delta, BufferBuilder buffer, float px, float py, float pz, int chunk_x, int chunk_z) {
        level.getChunk(chunk_x, chunk_z).getBlockEntities().forEach((blockPos, blockEntity) -> {
            if (!(blockEntity instanceof ChestBlockEntity))
                return;

            BlockEntity type = level.getBlockEntity(blockPos);

            Box aabb = new Box(blockPos);

            drawBox(stack, buffer, aabb, Colors.PURPLE);


            //TODO: Make tracer for chests a separate mod.
//            Vec3d center = aabb.getCenter();
//            RenderUtils.renderSingleLine(stack, buffer, px, py, pz, (float) center.x,
//                    (float) center.y, (float) center.z, r, g, b, a);

        });

    }

    private static void drawBox(MatrixStack stack, BufferBuilder buffer, Box aabb, Color c) {
        WorldRenderer.drawBox(stack, buffer, aabb, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    private static void drawEntityEsp(ClientWorld level, ClientPlayerEntity player, MatrixStack stack, float delta, BufferBuilder buffer, float px, float py, float pz) {
        level.getEntities().forEach(e -> {
            if ((e.squaredDistanceTo(player) > 1000) || player == e)
                return;

            EntityType<?> type = e.getType();


            double x = e.prevX + (e.getX() - e.prevX) * delta;
            double y = e.prevY + (e.getY() - e.prevY) * delta;
            double z = e.prevZ + (e.getZ() - e.prevZ) * delta;

            Color c = Colors.PURPLE;

            //TODO: Make this a separate mod.
            if (type == EntityType.ITEM) {
                c = Colors.DARK_CYAN;
                Item i = ((ItemEntity) e).getStack().getItem();
                //TODO: This will be a list of rare items.
                if (i.asItem() == Items.CREEPER_SPAWN_EGG) {
                    c = Colors.WHITE;
                }
//                if(item == Items.CREEPER_SPAWN_EGG) {
//                    r = 1;
//                    b = 0.5f;
//                    a = 0.25f;
//                }

            } else if (type.getSpawnGroup().isPeaceful()) {
                // green
                c = Colors.GREEN;
            } else if (!type.getSpawnGroup().isPeaceful()) {
                // red
                c = Colors.RED;
            } else if (type.getSpawnGroup().isRare()) {
                c = Colors.GOLD;
            } else if (type == EntityType.PLAYER) {
                c = Colors.YELLOW;
            }

            Box aabb = type.createSimpleBoundingBox(x, y, z);

            drawBox(stack, buffer, aabb, c);
//          TODO: Make tracer for mobs a separate mod.
            drawTracer(stack, buffer, px, py, pz, aabb, c);
        });
    }

    private static void drawTracer(MatrixStack stack, BufferBuilder buffer, float px, float py, float pz, Box aabb, Color c) {
        Vec3d center = aabb.getCenter();
        RenderUtils.renderSingleLine(stack, buffer, px, py, pz, (float) center.x,
                (float) center.y, (float) center.z, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }
}