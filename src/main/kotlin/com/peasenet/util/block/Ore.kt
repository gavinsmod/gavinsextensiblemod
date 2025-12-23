package com.peasenet.util.block

import com.peasenet.config.esp.OreEspConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.main.Settings
import com.peasenet.util.Dimension
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.data.registries.VanillaRegistries
import net.minecraft.data.worldgen.features.OreFeatures
import net.minecraft.resources.ResourceKey
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.FeatureSorter
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.WorldGenerationContext
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.OreFeature
import net.minecraft.world.level.levelgen.feature.ScatteredOreFeature
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider
import net.minecraft.world.level.levelgen.placement.CountPlacement
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.RarityFilter
import net.minecraft.world.level.levelgen.presets.WorldPresets

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
    var count: IntProvider = ConstantInt.of(1)
    var heightContext: WorldGenerationContext
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
        heightContext: WorldGenerationContext,
        enabled: Boolean,
        color: Color,
    ) {
        this.placedFeature = placedFeature
        this.step = genStep
        this.index = index
        this.enabled = enabled
        this.color = color
        this.heightContext = heightContext
        for (modifier in placedFeature.placement) {
            if (modifier is CountPlacement)
                this.count = modifier.count
            else if (modifier is HeightRangePlacement)
                this.heightProvider = modifier.height
            else if (modifier is RarityFilter)
                this.rarity = modifier.chance.toFloat()
        }

        val featureConfig = placedFeature.feature.value().config
        if (featureConfig is OreFeature) {
            val oreConfig = featureConfig as OreConfiguration
            this.discardOnAirChacne = oreConfig.discardChanceOnAirExposure
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

        fun registry(dimension: Dimension): MutableMap<ResourceKey<Biome>, out List<Ore>> {
            val registry = VanillaRegistries.createLookup()
            val keys = registry.listRegistryKeys()
            // flatten map to list
//            val keyList = keys.flatMap { it.second }
            val features = registry.getOrThrow(Registries.PLACED_FEATURE)
            val reg = registry.getOrThrow(Registries.WORLD_PRESET)
                .value().getOrThrow(WorldPresets.NORMAL).value().createWorldDimensions().dimensions;
            val dim = when (dimension) {
                Dimension.OVERWORLD -> reg[LevelStem.OVERWORLD]
                Dimension.NETHER -> reg[LevelStem.NETHER]
                Dimension.END -> reg[LevelStem.END]
            }
            if (dim == null) return mutableMapOf()
            val biomes = dim.generator.biomeSource.possibleBiomes();
            val heightContext: WorldGenerationContext?
            if (Minecraft.getInstance().level != null) {
                val bottom: Int = Minecraft.getInstance().level!!.minY
                val logical: Int = Minecraft.getInstance().level!!.dimensionType().logicalHeight
                heightContext = WorldGenerationContext(dim.generator(), LevelHeightAccessor.create(bottom, logical))
            } else {
                // Fallback safe defaults matching typical world limits; avoids NPE before a world is loaded
                heightContext = WorldGenerationContext(dim.generator(), LevelHeightAccessor.create(-64, 384))
            }
            var biomeList = biomes.stream().toList()

            val indexer = FeatureSorter.buildFeaturesPerStep(
                biomeList, { it.value().generationSettings.features() }, true
            )
            val featureToOre: MutableMap<PlacedFeature, Ore> = HashMap()

            registerOre(
                featureToOre,
                indexer,
                features,
                OreFeatures.ORE_COAL,
                6,
                heightContext,
                getSettings().coalEnabled,
                getSettings().coalColor
            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_COAL_BURIED,
//                6,
//                heightContext,
//                getSettings().coalEnabled,
//                getSettings().coalColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_IRON,
//                6,
//                heightContext,
//                getSettings().ironEnabled,
//                getSettings().ironColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_IRON_SMALL,
//                6,
//                heightContext,
//                getSettings().ironEnabled,
//                getSettings().ironColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_GOLD,
//                6,
//                heightContext,
//                getSettings().goldEnabled,
//                getSettings().goldColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_GOLD_BURIED,
//                6,
//                heightContext,
//                getSettings().goldEnabled,
//                getSettings().goldColor
//            )
//
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_NETHER_GOLD,
//                7,
//                heightContext,
//                getSettings().goldEnabled,
//                getSettings().goldColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_REDSTONE,
//                6,
//                heightContext,
//                getSettings().redstoneEnabled,
//                getSettings().redstoneColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_DIAMOND_SMALL,
//                6,
//                heightContext,
//                getSettings().diamondEnabled,
//                getSettings().diamondColor
//            )
//
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_DIAMOND_BURIED,
//                6,
//                heightContext,
//                getSettings().diamondEnabled,
//                getSettings().diamondColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_DIAMOND_LARGE,
//                6,
//                heightContext,
//                getSettings().diamondEnabled,
//                getSettings().diamondColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_DIAMOND_MEDIUM,
//                6,
//                heightContext,
//                getSettings().diamondEnabled,
//                getSettings().diamondColor
//            )
//
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_LAPIS,
//                6,
//                heightContext,
//                getSettings().lapisEnabled,
//                getSettings().lapisColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_COPPPER_SMALL,
//                6,
//                heightContext,
//                getSettings().copperEnabled,
//                getSettings().copperColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_COPPER_LARGE,
//                6,
//                heightContext,
//                getSettings().copperEnabled,
//                getSettings().copperColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_EMERALD,
//                6,
//                heightContext,
//                getSettings().emeraldEnabled,
//                getSettings().emeraldColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_QUARTZ,
//                7,
//                heightContext,
//                getSettings().quartzEnabled,
//                getSettings().quartzColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_ANCIENT_DEBRIS_SMALL,
//                7,
//                heightContext,
//                getSettings().debrisEnabled,
//                getSettings().debrisColor
//            )
//            registerOre(
//                featureToOre,
//                indexer,
//                features,
//                OreFeatures.ORE_ANCIENT_DEBRIS_LARGE,
//                7,
//                heightContext,
//                getSettings().debrisEnabled,
//                getSettings().debrisColor
//            )

            var biomeOreMap = mutableMapOf<ResourceKey<Biome>, MutableList<Ore>>()
            biomeList.forEach {
                biomeOreMap[it.unwrapKey().get()] = mutableListOf()
                it.value().generationSettings.features().stream().flatMap { s -> s.stream() }
                    .map { b -> b.value() }
                    .filter(featureToOre::containsKey)
                    .forEach { f ->
                        biomeOreMap[it.unwrapKey().get()]?.add(featureToOre[f]!!)
                    }
            }
            return biomeOreMap
        }

        fun registerOre(
            map: MutableMap<PlacedFeature, Ore>,
            indexer: List<FeatureSorter.StepFeatureData>,
            registry: Holder.Reference<Registry<PlacedFeature>>,
            key: ResourceKey<ConfiguredFeature<*, *>>,
            step: Int,
            heightContext: WorldGenerationContext,
            enabled: Boolean,
            color: Color,
        ) {
            val resourceKey = ResourceKey.create(
                Registries.PLACED_FEATURE,
                key.registry()
            )
            val orePlacement = registry.value().getValueOrThrow(resourceKey)
            val index = indexer[step].indexMapping.applyAsInt(orePlacement)
            val ore = Ore(orePlacement, step, index, heightContext, enabled, color)
            map[orePlacement] = ore
        }

    }


}