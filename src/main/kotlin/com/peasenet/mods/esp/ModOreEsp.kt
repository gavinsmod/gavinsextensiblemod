package com.peasenet.mods.esp

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.datafixers.util.Either
import com.peasenet.config.esp.OreEspConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gui.mod.render.GuiOreEsp
import com.peasenet.main.Mods
import com.peasenet.main.Settings
import com.peasenet.util.*
import com.peasenet.util.RenderUtils.getVertexConsumerProvider
import com.peasenet.util.block.GavBlock
import com.peasenet.util.block.Ore
import com.peasenet.util.chunk.GavChunk
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.executor.GemExecutor
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceKey
import net.minecraft.util.Mth
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.status.ChunkStatus
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.WorldgenRandom
import org.lwjgl.opengl.GL11
import java.util.*
import java.util.stream.Collectors
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin


/**
 * This module has been modified from NoraTweaks, based off of Meteor Rejects
 * Source: https://github.com/noramibu/Nora-Tweaks, https://github.com/AntiCope/meteor-rejects/
 * @author GT3CH1
 * @version 12-06-2025
 * @since 12-06-2025
 */
class ModOreEsp : BlockEsp<OreEspConfig>("gavinsmod.mod.esp.ore", "oreesp") {

    private lateinit var oreConfig: Map<ResourceKey<Biome>, List<Ore>>

    init {
        clickSetting {
            title = translationKey
            callback = {
                client.setScreen(GuiOreEsp())
            }
        }
    }

    override fun onEnable() {
        if (getSettings().seed.toLongOrNull() == null) {
            PlayerUtils.sendMessage("An invalid seed was provided!", true)
            return
        }
        oreConfig = Ore.registry(Dimension.fromValue(client.getWorld().dimensionType().skybox.toString()))
        super.onEnable()
        GemExecutor.execute {
            RenderUtils.getVisibleChunks().forEach(this::searchChunk)
        }
    }

    override fun getSettings(): OreEspConfig {
        return Settings.getConfig("oreesp")
    }

    override fun searchChunk(chunk: ChunkAccess) {
        GemExecutor.execute {
            val chunkPos = chunk.pos
            val chunkKey = chunkPos.toLong()
            val gavChunk = GavChunk(chunkPos)
            if (chunks.containsKey(chunkKey))
                return@execute
            val biomes = HashSet<ResourceKey<Biome>>()
            ChunkPos.rangeClosed(chunkPos, 1).forEach { cPos ->
                val chunkX = world.getChunk(cPos.x, cPos.z, ChunkStatus.BIOMES, false) ?: return@forEach
                for (section in chunkX.sections) {
                    section.biomes.getAll { biomes.add(it.unwrapKey().get()) }
                }
            }
            val oreSet: Set<Ore> =
                biomes.stream().flatMap { getOres(it).stream() }.collect(Collectors.toSet())

            val chunkX = chunkPos.x shl 4
            val chunkZ = chunkPos.z shl 4
            val random = WorldgenRandom(WorldgenRandom.Algorithm.XOROSHIRO.newInstance(0))
            val populationSeed = random.setDecorationSeed(getSettings().seed.toLong(), chunkX, chunkZ)
            for (ore in oreSet) {
                random.setFeatureSeed(populationSeed, ore.index, ore.step)
                val repeat = ore.count.sample(random)
                for (cleai in 0 until repeat) {
                    if (ore.rarity != 1F && random.nextFloat() >= 1 / ore.rarity)
                        continue
                    val x = random.nextInt(16) + chunkX
                    val z = random.nextInt(16) + chunkZ
                    val y = ore.heightProvider!!.sample(random, ore.heightContext)
                    val origin = BlockPos(x, y, z)

                    val biome = chunk.getNoiseBiome(x, y, z).unwrap()
                    if (!getOres(biome).contains(ore) || !ore.enabled)
                        continue

                    if (ore.isScattered) {
                        generateHidden(world, random, origin, ore.size).forEach { blockPos ->
                            gavChunk.addBlock(
                                GavBlock(blockPos, { true }, ore.color)
                            )
                        }
                    } else {
                        generateNormal(world, random, origin, ore.size, ore.discardOnAirChance)
                            .forEach { gavChunk.addBlock(GavBlock(it, { true }, ore.color)) }
                    }
                }
            }
            addBlocksFromChunk(gavChunk)
        }
    }

    private fun getOres(biomeKey: Either<ResourceKey<Biome>, Biome>): List<Ore> {
        if (oreConfig.containsKey(biomeKey.left().get()))
            return oreConfig[biomeKey.left().get()]!!
        return oreConfig.values.stream().findAny().get()
    }


    private fun getOres(biomeKey: ResourceKey<Biome>): List<Ore> {
        if (oreConfig.containsKey(biomeKey))
            return oreConfig[biomeKey]!!
        return oreConfig.values.stream().findAny().get()
    }

    override fun getColor(): Color {
        return Colors.RED_ORANGE
    }

    override fun onRender(matrixStack: PoseStack, partialTicks: Float) {
        synchronized(chunks) {
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            val vcp = getVertexConsumerProvider()
            val layer = GemRenderLayers.LINES
            val buffer = vcp.getBuffer(layer)
            chunks.values.filter { chunkInRenderDistance(it) }.forEach {
                it.render(
                    matrixStack,
                    Colors.RED_ORANGE,
                    partialTicks,
                    getSettings().alpha,
                    structureEsp = false,
                    blockTracer = false,
                    buffer
                )
            }
            vcp.endBatch()
            GL11.glEnable(GL11.GL_DEPTH_TEST)
        }
    }

    override fun onBlockUpdate(bue: BlockUpdate) {
        GemExecutor.execute {
            synchronized(chunks) {
                val chunk = client.getWorld().getChunk(bue.blockPos)
                updateChunk(false, GavBlock(bue.blockPos), chunk.pos)
            }
        }
    }

    private fun generateNormal(
        world: ClientLevel,
        random: WorldgenRandom,
        blockPos: BlockPos,
        veinSize: Int,
        discardOnAir: Float,
    ): ArrayList<BlockPos> {
        val f = random.nextFloat() * Mth.PI
        val g = veinSize.toFloat() / 8.0f
        val i = Mth.ceil((veinSize.toFloat() / 16.0f * 2.0f + 1.0f) / 2.0f)
        val d = blockPos.x.toDouble() + sin(f.toDouble()) * g.toDouble()
        val e = blockPos.x.toDouble() - sin(f.toDouble()) * g.toDouble()
        val h = blockPos.z.toDouble() + cos(f.toDouble()) * g.toDouble()
        val j = blockPos.z.toDouble() - cos(f.toDouble()) * g.toDouble()
        val l = (blockPos.y + random.nextInt(3) - 2).toDouble()
        val m = (blockPos.y + random.nextInt(3) - 2).toDouble()
        val n = blockPos.x - Mth.ceil(g) - i
        val o = blockPos.y - 2 - i
        val p = blockPos.z - Mth.ceil(g) - i
        val q = 2 * (Mth.ceil(g) + i)
        val r = 2 * (2 + i)

        for (s in n..n + q) {
            for (t in p..p + q) {
                if (o <= world.getHeight(Heightmap.Types.MOTION_BLOCKING, s, t)) {
                    return this.generateVeinPart(world, random, veinSize, d, e, h, j, l, m, n, o, p, q, r, discardOnAir)
                }
            }
        }

        return ArrayList<BlockPos>()
    }

    private fun generateVeinPart(
        world: ClientLevel,
        random: WorldgenRandom,
        veinSize: Int,
        startX: Double,
        endX: Double,
        startZ: Double,
        endZ: Double,
        startY: Double,
        endY: Double,
        minX: Int,
        minY: Int,
        minZ: Int,
        sizeX: Int,
        sizeY: Int,
        discardOnAir: Float,
    ): ArrayList<BlockPos> {
        val bitSet = BitSet(sizeX * sizeY * sizeX)
        val mutable = BlockPos.MutableBlockPos()
        val buffer = DoubleArray(veinSize * 4)

        val positions = ArrayList<BlockPos>()

        for (i in 0..<veinSize) {
            val progress = i.toFloat() / veinSize.toFloat()
            val x: Double = Mth.lerp(progress.toDouble(), startX, endX)
            val y: Double = Mth.lerp(progress.toDouble(), startY, endY)
            val z: Double = Mth.lerp(progress.toDouble(), startZ, endZ)
            val scale: Double = random.nextDouble() * veinSize / 16.0
            val radius: Double = (Mth.sin(Math.PI.toFloat() * progress.toDouble()) + 1.0f) * scale + 1.0
            buffer[i * 4] = x
            buffer[i * 4 + 1] = y
            buffer[i * 4 + 2] = z
            buffer[i * 4 + 3] = radius / 2.0
        }

        for (i in 0..<veinSize - 1) {
            if (buffer[i * 4 + 3] <= 0.0) continue
            for (j in i + 1..<veinSize) {
                if (buffer[j * 4 + 3] <= 0.0) continue
                val dx: Double = buffer[i * 4] - buffer[j * 4]
                val dy: Double = buffer[i * 4 + 1] - buffer[j * 4 + 1]
                val dz: Double = buffer[i * 4 + 2] - buffer[j * 4 + 2]
                val dr: Double = buffer[i * 4 + 3] - buffer[j * 4 + 3]
                if (dr * dr > dx * dx + dy * dy + dz * dz) {
                    if (dr > 0.0) buffer[j * 4 + 3] = -1.0
                    else buffer[i * 4 + 3] = -1.0
                }
            }
        }

        for (i in 0..<veinSize) {
            val radius: Double = buffer[i * 4 + 3]
            if (radius < 0.0) continue
            val centerX: Double = buffer[i * 4]
            val centerY: Double = buffer[i * 4 + 1]
            val centerZ: Double = buffer[i * 4 + 2]
            val minBlockX: Int = Mth.floor(centerX - radius).coerceAtLeast(minX)
            val minBlockY: Int = Mth.floor(centerY - radius).coerceAtLeast(minY)
            val minBlockZ: Int = Mth.floor(centerZ - radius).coerceAtLeast(minZ)
            val maxBlockX = Mth.floor(centerX + radius).coerceAtLeast(minBlockX)
            val maxBlockY = Mth.floor(centerY + radius).coerceAtLeast(minBlockY)
            val maxBlockZ = Mth.floor(centerZ + radius).coerceAtLeast(minBlockZ)

            for (x in minBlockX..maxBlockX) {
                val normX = (x.toDouble() + 0.5 - centerX) / radius
                if (normX * normX >= 1.0) continue
                for (y in minBlockY..maxBlockY) {
                    val normY = (y.toDouble() + 0.5 - centerY) / radius
                    if (normX * normX + normY * normY >= 1.0) continue
                    for (z in minBlockZ..maxBlockZ) {
                        val normZ = (z.toDouble() + 0.5 - centerZ) / radius
                        if (normX * normX + normY * normY + normZ * normZ >= 1.0) continue
                        val index: Int = x - minX + (y - minY) * sizeX + (z - minZ) * sizeX * sizeY
                        if (bitSet.get(index)) continue
                        bitSet.set(index)
                        mutable.set(x, y, z)
                        if (y < -64 || y >= 320 || !world.getBlockState(mutable).canOcclude()) continue
                        if (shouldPlace(world, mutable, discardOnAir, random)) {
                            positions.add(BlockPos(x, y, z))
                        }
                    }
                }
            }
        }

        return positions
    }

    private fun shouldPlace(
        world: ClientLevel,
        orePos: BlockPos,
        discardOnAir: Float,
        random: WorldgenRandom,
    ): Boolean {
        if (discardOnAir == 0f || (discardOnAir != 1f && random.nextFloat() >= discardOnAir)) {
            return true
        }

        for (direction in Direction.entries) {
            if (!world.getBlockState(orePos.relative(direction, 1)).isSolidRender && discardOnAir != 1f) {
                return false
            }
        }
        return true
    }

    private fun generateHidden(
        world: ClientLevel,
        random: WorldgenRandom,
        blockPos: BlockPos,
        size: Int,
    ): ArrayList<BlockPos> {
        var size = size
        val poses = ArrayList<BlockPos>()

        val i = random.nextInt(size + 1)

        for (j in 0..<i) {
            size = min(j, 7)
            val x = this.randomCoord(random, size) + blockPos.x
            val y = this.randomCoord(random, size) + blockPos.y
            val z = this.randomCoord(random, size) + blockPos.z
            if (world.getBlockState(BlockPos(x, y, z)).canOcclude()) {
                if (shouldPlace(world, BlockPos(x, y, z), 1f, random)) {
                    poses.add(BlockPos(x, y, z))
                }
            }
        }

        return poses
    }

    private fun randomCoord(random: WorldgenRandom, size: Int): Int {
        return ((random.nextFloat() - random.nextFloat()) * size.toFloat()).roundToInt()
    }

    override fun chunkInRenderDistance(chunk: GavChunk): Boolean {
        return chunk.inRenderDistance(RenderUtils.getRenderDistance() / 2)
    }

    companion object {
        fun reload() {
            Mods.getMod<ModOreEsp>(ChatCommand.OreEsp).reload()
        }
    }
}