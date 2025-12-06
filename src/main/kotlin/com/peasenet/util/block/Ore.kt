package com.peasenet.util.block

import com.peasenet.main.GavinsModClient
import com.peasenet.util.Dimension
import net.minecraft.client.MinecraftClient
import net.minecraft.registry.BuiltinRegistries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.util.math.intprovider.ConstantIntProvider
import net.minecraft.util.math.intprovider.IntProvider
import net.minecraft.world.HeightLimitView
import net.minecraft.world.biome.Biome
import net.minecraft.world.dimension.DimensionOptions
import net.minecraft.world.gen.HeightContext
import net.minecraft.world.gen.WorldPresets
import net.minecraft.world.gen.feature.OreFeatureConfig
import net.minecraft.world.gen.feature.OrePlacedFeatures
import net.minecraft.world.gen.feature.PlacedFeature
import net.minecraft.world.gen.feature.ScatteredOreFeature
import net.minecraft.world.gen.feature.util.PlacedFeatureIndexer
import net.minecraft.world.gen.heightprovider.HeightProvider
import net.minecraft.world.gen.placementmodifier.CountPlacementModifier
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier

/**
 *
 * @author GT3CH1
 * @version 12-06-2025
 * @since 12-06-2025
 */
class Ore {
    private var placedFeature: PlacedFeature
    var step: Int = 0
    var index: Int = 0
    var count: IntProvider = ConstantIntProvider.create(1)
    var heightContext: HeightContext
    var heightProvider: HeightProvider? = null
    var rarity: Float = 1f
    var discardOnAirChacne = 0.0f
    var size: Int = 0
    var isScattered: Boolean = false

    private constructor(
        placedFeature: PlacedFeature,
        genStep: Int,
        index: Int,
        heightContext: HeightContext,
    ) {
        this.placedFeature = placedFeature
        this.step = genStep
        this.index = index

        val bottom = GavinsModClient.minecraftClient.getWorld().bottomY
        val height = GavinsModClient.minecraftClient.getWorld().dimension.logicalHeight
        this.heightContext = heightContext
        for (modifier in placedFeature.placementModifiers) {
            if (modifier is CountPlacementModifier)
                this.count = modifier.count
            else if (modifier is HeightRangePlacementModifier)
                this.heightProvider = (modifier as HeightRangePlacementModifier).height
            else if (modifier is RarityFilterPlacementModifier)
                this.rarity = (modifier as RarityFilterPlacementModifier).chance.toFloat()
        }

        val featureConfig = placedFeature.feature.value().config
        if (featureConfig is OreFeatureConfig) {
            val oreConfig = featureConfig as OreFeatureConfig
            this.discardOnAirChacne = oreConfig.discardOnAirChance
            this.size = oreConfig.size
        } else {
            throw IllegalArgumentException("PlacedFeature is not an OreFeatureConfig")
        }
        if (featureConfig is ScatteredOreFeature) {
            isScattered = true
        }
    }

    fun test() {

    }

    companion object {
        fun registry(dimension: Dimension): MutableMap<RegistryKey<Biome>, out List<Ore>> {
            val registry = BuiltinRegistries.createWrapperLookup()
            val features = registry.getOrThrow(RegistryKeys.PLACED_FEATURE)
            val reg = registry.getOrThrow(RegistryKeys.WORLD_PRESET).getOrThrow(WorldPresets.DEFAULT)
                .value().createDimensionsRegistryHolder().dimensions;
            if (reg == null) return mutableMapOf()
            val dim = when (dimension) {
                Dimension.OVERWORLD -> reg[DimensionOptions.OVERWORLD]
                Dimension.NETHER -> reg[DimensionOptions.NETHER]
                Dimension.END -> reg[DimensionOptions.END]
            }
            if (dim == null) return mutableMapOf()
            var biomes = dim.chunkGenerator.biomeSource.biomes;
            val heightContext: HeightContext?
            if (MinecraftClient.getInstance().world != null) {
                val bottom: Int = MinecraftClient.getInstance().world!!.getBottomY()
                val logical: Int = MinecraftClient.getInstance().world!!.getDimension().logicalHeight()
                heightContext =
                    HeightContext(dim.chunkGenerator(), HeightLimitView.create(bottom, logical))
            } else {
                // Fallback safe defaults matching typical world limits; avoids NPE before a world is loaded
                heightContext = HeightContext(dim.chunkGenerator(), HeightLimitView.create(-64, 384))
            }
            var biomeList = biomes.stream().toList()

            val indexer = PlacedFeatureIndexer.collectIndexedFeatures(
                biomeList, { it.value().generationSettings.features }, true
            )
            val featureToOre: MutableMap<PlacedFeature, Ore> = HashMap()

            registerOre(featureToOre, indexer, features, OrePlacedFeatures.ORE_COAL_LOWER, 6, heightContext)
            registerOre(featureToOre, indexer, features, OrePlacedFeatures.ORE_COAL_UPPER, 6, heightContext)
//            registerOre(featureToOre, indexer, features, OrePlacedFeatures.ORE_IRON_MIDDLE, 6, heightContext)
//            registerOre(featureToOre, indexer, features, OrePlacedFeatures.ORE_IRON_SMALL, 6, heightContext)
//            registerOre(featureToOre, indexer, features, OrePlacedFeatures.ORE_IRON_UPPER, 6, heightContext)



            var biomeOreMap  = mutableMapOf<RegistryKey<Biome>, MutableList<Ore>>()
            biomeList.forEach {
                biomeOreMap[it.key.get()] = mutableListOf()
                it.value().generationSettings.features.stream().flatMap { s -> s.stream() }
                    .map { b -> b.value() }
                    .filter(featureToOre::containsKey)
                    .forEach { f ->
//                       biomeOreMap[it.key.get].put biomeOreMap[it.key.get()]?.plus(featureToOre[f])
                        biomeOreMap[it.key.get()]?.add(featureToOre[f]!!)
                    }
            }
            return biomeOreMap
        }

        fun registerOre(
            map: MutableMap<PlacedFeature, Ore>,
            indexer: List<PlacedFeatureIndexer.IndexedFeatures>,
            registry: RegistryWrapper.Impl<PlacedFeature>,
            key: RegistryKey<PlacedFeature>,
            step: Int,
            heightContext: HeightContext,
        ) {
            val orePlacement = registry.getOrThrow(key).value()
            val index = indexer[step].indexMapping.applyAsInt(orePlacement)
            val ore = Ore(orePlacement, step, index, heightContext)
            map[orePlacement] = ore
        }

    }


}