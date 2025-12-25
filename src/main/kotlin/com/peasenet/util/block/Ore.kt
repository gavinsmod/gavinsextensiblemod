package com.peasenet.util.block

import com.peasenet.config.esp.OreEspConfig
import com.peasenet.gavui.color.Color
import com.peasenet.gavui.color.Colors
import com.peasenet.main.Settings
import com.peasenet.util.Dimension
import net.minecraft.client.Minecraft
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.registries.VanillaRegistries
import net.minecraft.data.worldgen.placement.OrePlacements
import net.minecraft.resources.ResourceKey
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.FeatureSorter
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.WorldGenerationContext
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
     var placedFeature: PlacedFeature
    var step: Int = 0
    var index: Int = 0
    var count: IntProvider = ConstantInt.of(1)
    var heightContext: WorldGenerationContext
    var heightProvider: HeightProvider? = null
    var rarity: Float = 1f
    var discardOnAirChance = 0.0f
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
            when (modifier) {
                is CountPlacement -> this.count = modifier.count
                is HeightRangePlacement -> this.heightProvider = modifier.height
                is RarityFilter -> this.rarity = modifier.chance.toFloat()
            }
        }

        val featureConfig = placedFeature.feature.value().config
        if (featureConfig is ScatteredOreFeature) {
            isScattered = true
        }
        if (featureConfig is OreConfiguration) {
            this.discardOnAirChance = featureConfig.discardChanceOnAirExposure
            this.size = featureConfig.size

        } else {
            throw IllegalArgumentException("PlacedFeature is not an OreFeatureConfig")
        }
    }


    companion object {

        private fun getSettings(): OreEspConfig {
            return Settings.getConfig("oreesp")
        }

        fun registry(dimension: Dimension): MutableMap<ResourceKey<Biome>, out List<Ore>> {
            val registry = VanillaRegistries.createLookup()
            val features = registry.lookupOrThrow(Registries.PLACED_FEATURE)
            val reg = registry.lookupOrThrow(Registries.WORLD_PRESET)
                .getOrThrow(WorldPresets.NORMAL).value().createWorldDimensions().dimensions;
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
                heightContext = WorldGenerationContext(dim.generator(), LevelHeightAccessor.create(-64, 384))
            }
            val biomeList = biomes.stream().toList()

            val indexer = FeatureSorter.buildFeaturesPerStep(
                biomeList, { it.value().generationSettings.features() }, true
            )
            val featureToOre: MutableMap<PlacedFeature, Ore> = HashMap()

            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_COAL_UPPER,
                6,
                heightContext,
                getSettings().coalEnabled,
                getSettings().coalColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_COAL_LOWER,
                6,
                heightContext,
                getSettings().coalEnabled,
                getSettings().coalColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_COAL_UPPER,
                6,
                heightContext,
                getSettings().coalEnabled,
                getSettings().coalColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_IRON_MIDDLE,
                6,
                heightContext,
                getSettings().ironEnabled,
                getSettings().ironColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_IRON_SMALL,
                6,
                heightContext,
                getSettings().ironEnabled,
                getSettings().ironColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_IRON_UPPER,
                6,
                heightContext,
                getSettings().goldEnabled,
                getSettings().ironColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_GOLD,
                6,
                heightContext,
                getSettings().goldEnabled,
                getSettings().goldColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_GOLD_LOWER,
                6,
                heightContext,
                getSettings().goldEnabled,
                getSettings().goldColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_GOLD_EXTRA,
                6,
                heightContext,
                getSettings().goldEnabled,
                getSettings().goldColor
            )

            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_GOLD_NETHER,
                7,
                heightContext,
                getSettings().goldEnabled,
                getSettings().goldColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_GOLD_DELTAS,
                7,
                heightContext,
                getSettings().goldEnabled,
                getSettings().goldColor
            )

            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_REDSTONE,
                6,
                heightContext,
                getSettings().redstoneEnabled,
                getSettings().redstoneColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_REDSTONE_LOWER,
                6,
                heightContext,
                getSettings().redstoneEnabled,
                getSettings().redstoneColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_DIAMOND,
                6,
                heightContext,
                getSettings().diamondEnabled,
                getSettings().diamondColor
            )

            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_DIAMOND_BURIED,
                6,
                heightContext,
                getSettings().diamondEnabled,
                getSettings().diamondColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_DIAMOND_LARGE,
                6,
                heightContext,
                getSettings().diamondEnabled,
                getSettings().diamondColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_DIAMOND_MEDIUM,
                6,
                heightContext,
                getSettings().diamondEnabled,
                getSettings().diamondColor
            )

            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_LAPIS,
                6,
                heightContext,
                getSettings().lapisEnabled,
                getSettings().lapisColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_LAPIS,
                6,
                heightContext,
                getSettings().lapisEnabled,
                getSettings().lapisColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_COPPER,
                6,
                heightContext,
                getSettings().copperEnabled,
                getSettings().copperColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_COPPER_LARGE,
                6,
                heightContext,
                getSettings().copperEnabled,
                getSettings().copperColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_EMERALD,
                6,
                heightContext,
                getSettings().emeraldEnabled,
                getSettings().emeraldColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_QUARTZ_NETHER,
                7,
                heightContext,
                getSettings().quartzEnabled,
                getSettings().quartzColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_QUARTZ_DELTAS,
                7,
                heightContext,
                getSettings().quartzEnabled,
                getSettings().quartzColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_ANCIENT_DEBRIS_SMALL,
                7,
                heightContext,
                getSettings().debrisEnabled,
                getSettings().debrisColor
            )
            registerOre(
                featureToOre,
                indexer,
                features,
                OrePlacements.ORE_ANCIENT_DEBRIS_LARGE,
                7,
                heightContext,
                getSettings().debrisEnabled,
                getSettings().debrisColor
            )

            val biomeOreMap = mutableMapOf<ResourceKey<Biome>, MutableList<Ore>>()
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
            registry: HolderLookup.RegistryLookup<PlacedFeature>,
            key: ResourceKey<PlacedFeature>,
            step: Int,
            heightContext: WorldGenerationContext,
            enabled: Boolean,
            color: Color,
        ) {
            val orePlacement = registry.get(key).get().value()
            val index = indexer[step].indexMapping.applyAsInt(orePlacement)
            val ore = Ore(orePlacement, step, index, heightContext, enabled, color)
            map[orePlacement] = ore
        }

    }


}