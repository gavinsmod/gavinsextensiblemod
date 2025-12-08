package com.peasenet.util.block

import com.peasenet.config.esp.OreEspConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.main.Settings
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
    var enabled: Boolean = false
    var color: Color = Colors.WHITE

    private constructor(
        placedFeature: PlacedFeature,
        genStep: Int,
        index: Int,
        heightContext: HeightContext,
        enabled: Boolean,
        color: Color,
    ) {
        this.placedFeature = placedFeature
        this.step = genStep
        this.index = index
        this.enabled = enabled
        this.color = color
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


    companion object {

        private fun getSettings(): OreEspConfig {
            return Settings.getConfig("oreesp")
        }

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

            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_COAL_LOWER,
                6,
                heightContext,
                getSettings().coalEnabled,
                getSettings().coalColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_COAL_UPPER,
                6,
                heightContext,
                getSettings().coalEnabled,
                getSettings().coalColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_IRON_MIDDLE,
                6,
                heightContext,
                getSettings().ironEnabled,
                getSettings().ironColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_IRON_SMALL,
                6,
                heightContext,
                getSettings().ironEnabled,
                getSettings().ironColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_IRON_UPPER,
                6,
                heightContext,
                getSettings().goldEnabled,
                getSettings().ironColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_GOLD,
                6,
                heightContext,
                getSettings().goldEnabled,
                getSettings().goldColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_GOLD_LOWER,
                6,
                heightContext,
                getSettings().goldEnabled,
                getSettings().goldColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_GOLD_EXTRA,
                6,
                heightContext,
                getSettings().goldEnabled,
                getSettings().goldColor
            )

            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_GOLD_NETHER,
                7,
                heightContext,
                getSettings().goldEnabled,
                getSettings().goldColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_GOLD_DELTAS,
                7,
                heightContext,
                getSettings().goldEnabled,
                getSettings().goldColor
            )

            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_REDSTONE,
                6,
                heightContext,
                getSettings().redstoneEnabled,
                getSettings().redstoneColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_REDSTONE_LOWER,
                6,
                heightContext,
                getSettings().redstoneEnabled,
                getSettings().redstoneColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_DIAMOND,
                6,
                heightContext,
                getSettings().diamondEnabled,
                getSettings().diamondColor
            )

            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_DIAMOND_BURIED,
                6,
                heightContext,
                getSettings().diamondEnabled,
                getSettings().diamondColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_DIAMOND_LARGE,
                6,
                heightContext,
                getSettings().diamondEnabled,
                getSettings().diamondColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_DIAMOND_MEDIUM,
                6,
                heightContext,
                getSettings().diamondEnabled,
                getSettings().diamondColor
            )

            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_LAPIS,
                6,
                heightContext,
                getSettings().lapisEnabled,
                getSettings().lapisColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_LAPIS,
                6,
                heightContext,
                getSettings().lapisEnabled,
                getSettings().lapisColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_COPPER,
                6,
                heightContext,
                getSettings().copperEnabled,
                getSettings().copperColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_COPPER_LARGE,
                6,
                heightContext,
                getSettings().copperEnabled,
                getSettings().copperColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_EMERALD,
                6,
                heightContext,
                getSettings().emeraldEnabled,
                getSettings().emeraldColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_QUARTZ_NETHER,
                7,
                heightContext,
                getSettings().quartzEnabled,
                getSettings().quartzColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_QUARTZ_DELTAS,
                7,
                heightContext,
                getSettings().quartzEnabled,
                getSettings().quartzColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_DEBRIS_SMALL,
                7,
                heightContext,
                getSettings().debrisEnabled,
                getSettings().debrisColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacedFeatures.ORE_ANCIENT_DEBRIS_LARGE,
                7,
                heightContext,
                getSettings().debrisEnabled,
                getSettings().debrisColor
            )

            var biomeOreMap = mutableMapOf<RegistryKey<Biome>, MutableList<Ore>>()
            biomeList.forEach {
                biomeOreMap[it.key.get()] = mutableListOf()
                it.value().generationSettings.features.stream().flatMap { s -> s.stream() }
                    .map { b -> b.value() }
                    .filter(featureToOre::containsKey)
                    .forEach { f ->
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
            enabled: Boolean,
            color: Color,
        ) {
            val orePlacement = registry.getOrThrow(key).value()
            val index = indexer[step].indexMapping.applyAsInt(orePlacement)
            val ore = Ore(orePlacement, step, index, heightContext, enabled, color)
            map[orePlacement] = ore
        }

    }


}