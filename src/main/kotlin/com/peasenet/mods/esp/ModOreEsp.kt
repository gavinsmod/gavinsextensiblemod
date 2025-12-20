package com.peasenet.mods.esp

import com.peasenet.config.esp.OreEspConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.gui.mod.render.GuiOreEsp
import com.peasenet.main.Mods
import com.peasenet.main.Settings
import com.peasenet.util.ChatCommand
import com.peasenet.util.Dimension
import com.peasenet.util.GemRenderLayers
import com.peasenet.util.PlayerUtils
import com.peasenet.util.RenderUtils
import com.peasenet.util.RenderUtils.getVertexConsumerProvider
import com.peasenet.util.block.GavBlock
import com.peasenet.util.block.Ore
import com.peasenet.util.chunk.GavChunk
import com.peasenet.util.event.data.BlockUpdate
import com.peasenet.util.executor.GemExecutor
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.registry.RegistryKey
import net.minecraft.util.math.*
import net.minecraft.util.math.random.ChunkRandom
import net.minecraft.world.Heightmap
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkStatus
import org.lwjgl.opengl.GL11
import java.util.*
import java.util.stream.Collectors
import kotlin.math.cos
import kotlin.math.max
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
    private lateinit var oreConfig: Map<RegistryKey<Biome>, List<Ore>>

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
        oreConfig = Ore.registry(Dimension.fromValue(client.getWorld().dimension.effects.path))
        super.onEnable()
        GemExecutor.execute {
            RenderUtils.getVisibleChunks().forEach(this::searchChunk)
        }
    }

    override fun getSettings(): OreEspConfig {
        return Settings.getConfig("oreesp")
    }

    override fun searchChunk(chunk: Chunk) {
        GemExecutor.execute {
            val chunkPos = chunk.pos
            val chunkKey = chunkPos.toLong()
            val gavChunk = GavChunk(chunkPos)
            if (chunks.containsKey(chunkKey))
                return@execute
            val biomes = HashSet<RegistryKey<Biome>>()
            ChunkPos.stream(chunkPos, 1).forEach { cPos ->
                val chunkX = world.getChunk(cPos.x, cPos.z, ChunkStatus.BIOMES, false) ?: return@forEach
                for (section in chunkX.sectionArray) {
                    section.biomeContainer.forEachValue { biomes.add(it.key.get()) }
                }
            }
            val oreSet: Set<Ore> =
                biomes.stream().flatMap { getOres(it).stream() }.collect(Collectors.toSet())

            val chunkX = chunkPos.x shl 4
            val chunkZ = chunkPos.z shl 4
            val random = ChunkRandom(ChunkRandom.RandomProvider.XOROSHIRO.create(0))
            val populationSeed = random.setPopulationSeed(getSettings().seed.toLong(), chunkX, chunkZ)
            val h = mutableMapOf<Ore, MutableSet<GavBlock>>()
            for (ore in oreSet) {
                val blockPos: HashSet<GavBlock> = HashSet()
                random.setDecoratorSeed(populationSeed, ore.index, ore.step)
                val repeat = ore.count.get(random)
                for (i in 0 until repeat) {
                    if (ore.rarity != 1F && random.nextFloat() >= 1 / ore.rarity)
                        continue
                    val x = random.nextInt(16) + chunkX
                    val z = random.nextInt(16) + chunkZ
                    val y = ore.heightProvider!!.get(random, ore.heightContext)
                    val origin = BlockPos(x, y, z)

                    val biome = chunk.getBiomeForNoiseGen(x, y, z).key.get()
                    if (!getOres(biome).contains(ore) || !ore.enabled)
                        continue

                    if (ore.isScattered) {
                        val hidden = generateHidden(world, random, origin, ore.size)
                        hidden.forEach {
                            blockPos.add(
                                GavBlock(it, ore.color)
                            )
                        }
                    } else {
                        val normals = generateNormal(world, random, origin, ore.size, ore.discardOnAirChacne)

                        normals.forEach { blockPos.add(GavBlock(it, ore.color)) }
                    }
                }
                if (!blockPos.isEmpty()) {
                    h[ore] = blockPos
                }
            }
            h.values.flatten().forEach {
                gavChunk.addBlock(it)
            }
            addBlocksFromChunk(gavChunk)
        }
    }

    private fun getOres(biomeKey: RegistryKey<Biome>): List<Ore> {
        if (oreConfig.containsKey(biomeKey))
            return oreConfig[biomeKey]!!
        return oreConfig.values.stream().findAny().get()
    }

    override fun getColor(): Color {
        return Colors.RED_ORANGE
    }

    override fun onRender(matrixStack: MatrixStack, partialTicks: Float) {
        synchronized(chunks) {
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            val vcp = getVertexConsumerProvider()
            val layer = GemRenderLayers.LINES
            val buffer = vcp.getBuffer(layer)
            val blocks = chunks.values

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
            vcp.draw(layer)
            GL11.glEnable(GL11.GL_DEPTH_TEST)
        }
    }

    override fun onBlockUpdate(bue: BlockUpdate) {
        GemExecutor.execute {
            synchronized(chunks) {
                val chunk = client.getWorld().getChunk(bue.blockPos) ?: return@execute
                updateChunk(false, GavBlock(bue.blockPos), chunk.pos)
            }
        }
    }

    private fun generateNormal(
        world: ClientWorld,
        random: ChunkRandom,
        blockPos: BlockPos,
        veinSize: Int,
        discardOnAir: Float,
    ): ArrayList<BlockPos> {
        val f = random.nextFloat() * 3.1415927f
        val g = veinSize.toFloat() / 8.0f
        val i = MathHelper.ceil((veinSize.toFloat() / 16.0f * 2.0f + 1.0f) / 2.0f)
        val d = blockPos.x.toDouble() + sin(f.toDouble()) * g.toDouble()
        val e = blockPos.x.toDouble() - sin(f.toDouble()) * g.toDouble()
        val h = blockPos.z.toDouble() + cos(f.toDouble()) * g.toDouble()
        val j = blockPos.z.toDouble() - cos(f.toDouble()) * g.toDouble()
        val l = (blockPos.y + random.nextInt(3) - 2).toDouble()
        val m = (blockPos.y + random.nextInt(3) - 2).toDouble()
        val n = blockPos.x - MathHelper.ceil(g) - i
        val o = blockPos.y - 2 - i
        val p = blockPos.z - MathHelper.ceil(g) - i
        val q = 2 * (MathHelper.ceil(g) + i)
        val r = 2 * (2 + i)

        for (s in n..n + q) {
            for (t in p..p + q) {
                if (o <= world.getTopY(Heightmap.Type.MOTION_BLOCKING, s, t)) {
                    return this.generateVeinPart(world, random, veinSize, d, e, h, j, l, m, n, o, p, q, r, discardOnAir)
                }
            }
        }

        return ArrayList<BlockPos>()
    }

    private fun generateVeinPart(
        world: ClientWorld,
        random: ChunkRandom,
        veinSize: Int,
        startX: Double,
        endX: Double,
        startZ: Double,
        endZ: Double,
        startY: Double,
        endY: Double,
        x: Int,
        y: Int,
        z: Int,
        size: Int,
        i: Int,
        discardOnAir: Float,
    ): ArrayList<BlockPos> {
        val bitSet = BitSet(size * i * size)
        val mutable = BlockPos.Mutable()
        val ds = DoubleArray(veinSize * 4)

        val poses = ArrayList<BlockPos>()
        var p: Double
        var q: Double
        var r: Double
        var s: Double
        var n: Int = 0
        while (n < veinSize) {
            val f = n.toFloat() / veinSize.toFloat()
            p = MathHelper.lerp(f.toDouble(), startX, endX)
            q = MathHelper.lerp(f.toDouble(), startY, endY)
            r = MathHelper.lerp(f.toDouble(), startZ, endZ)
            s = random.nextDouble() * veinSize.toDouble() / 16.0
            val m = ((MathHelper.sin(3.1415927f * f) + 1.0f).toDouble() * s + 1.0) / 2.0
            ds[n * 4] = p
            ds[n * 4 + 1] = q
            ds[n * 4 + 2] = r
            ds[n * 4 + 3] = m
            ++n
        }

        n = 0
        while (n < veinSize - 1) {
            if (!(ds[n * 4 + 3] <= 0.0)) {
                for (o in n + 1..<veinSize) {
                    if (!(ds[o * 4 + 3] <= 0.0)) {
                        p = ds[n * 4] - ds[o * 4]
                        q = ds[n * 4 + 1] - ds[o * 4 + 1]
                        r = ds[n * 4 + 2] - ds[o * 4 + 2]
                        s = ds[n * 4 + 3] - ds[o * 4 + 3]
                        if (s * s > p * p + q * q + r * r) {
                            if (s > 0.0) {
                                ds[o * 4 + 3] = -1.0
                            } else {
                                ds[n * 4 + 3] = -1.0
                            }
                        }
                    }
                }
            }
            ++n
        }

        n = 0
        while (n < veinSize) {
            val u = ds[n * 4 + 3]
            if (!(u < 0.0)) {
                val v = ds[n * 4]
                val w = ds[n * 4 + 1]
                val aa = ds[n * 4 + 2]
                val ab = max(MathHelper.floor(v - u), x)
                val ac = max(MathHelper.floor(w - u), y)
                val ad = max(MathHelper.floor(aa - u), z)
                val ae = max(MathHelper.floor(v + u), ab)
                val af = max(MathHelper.floor(w + u), ac)
                val ag = max(MathHelper.floor(aa + u), ad)

                for (ah in ab..ae) {
                    val ai = (ah.toDouble() + 0.5 - v) / u
                    if (ai * ai < 1.0) {
                        for (aj in ac..af) {
                            val ak = (aj.toDouble() + 0.5 - w) / u
                            if (ai * ai + ak * ak < 1.0) {
                                for (al in ad..ag) {
                                    val am = (al.toDouble() + 0.5 - aa) / u
                                    if (ai * ai + ak * ak + am * am < 1.0) {
                                        val an = ah - x + (aj - y) * size + (al - z) * size * i
                                        if (!bitSet.get(an)) {
                                            bitSet.set(an)
                                            mutable.set(ah, aj, al)
                                            if (aj >= -64 && aj < 320 && (world.getBlockState(
                                                    mutable
                                                ).isOpaque())
                                            ) {
                                                if (shouldPlace(world, mutable, discardOnAir, random)) {
                                                    poses.add(BlockPos(ah, aj, al))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ++n
        }

        return poses
    }

    private fun shouldPlace(world: ClientWorld, orePos: BlockPos, discardOnAir: Float, random: ChunkRandom): Boolean {
        if (discardOnAir == 0f || (discardOnAir != 1f && random.nextFloat() >= discardOnAir)) {
            return true
        }

        for (direction in Direction.entries) {
            if (!world.getBlockState(orePos.add(direction.vector)).isOpaque && discardOnAir != 1f) {
                return false
            }
        }
        return true
    }

    private fun generateHidden(
        world: ClientWorld,
        random: ChunkRandom,
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
            if (world.getBlockState(BlockPos(x, y, z)).isOpaque) {
                if (shouldPlace(world, BlockPos(x, y, z), 1f, random)) {
                    poses.add(BlockPos(x, y, z))
                }
            }
        }

        return poses
    }

    private fun randomCoord(random: ChunkRandom, size: Int): Int {
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