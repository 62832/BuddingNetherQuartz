package gripe._90.buddingnetherquartz;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BNQFeatureProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, BNQFeatureProvider::bootstrapConfigured)
            .add(Registries.PLACED_FEATURE, BNQFeatureProvider::bootstrapPlaced)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BNQFeatureProvider::bootstrapModifier);

    private static final ResourceLocation GEODE_ID = BuddingNetherQuartz.QUARTZ_GEODE.getId();
    private static final ResourceKey<ConfiguredFeature<?, ?>> GEODE_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, GEODE_ID);
    private static final ResourceKey<PlacedFeature> GEODE_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, GEODE_ID);
    private static final ResourceKey<BiomeModifier> MODIFIER = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, GEODE_ID);

    public BNQFeatureProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(BuddingNetherQuartz.MODID));
    }

    private static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(
                context,
                GEODE_CONFIGURED,
                BuddingNetherQuartz.QUARTZ_GEODE.get(),
                new GeodeConfiguration(
                        new GeodeBlockSettings(
                                BlockStateProvider.simple(Blocks.AIR),
                                BlockStateProvider.simple(Blocks.SMOOTH_QUARTZ),
                                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                                        .add(state(BuddingNetherQuartz.DAMAGED_BUDDING_QUARTZ), 10)
                                        .add(state(BuddingNetherQuartz.CHIPPED_BUDDING_QUARTZ), 6)
                                        .add(state(BuddingNetherQuartz.FLAWED_BUDDING_QUARTZ), 3)
                                        .add(state(BuddingNetherQuartz.FLAWLESS_BUDDING_QUARTZ), 1)
                                        .build()),
                                BlockStateProvider.simple(Blocks.BLACKSTONE),
                                BlockStateProvider.simple(Blocks.SMOOTH_BASALT),
                                List.of(
                                        state(BuddingNetherQuartz.SMALL_QUARTZ_BUD),
                                        state(BuddingNetherQuartz.MEDIUM_QUARTZ_BUD),
                                        state(BuddingNetherQuartz.LARGE_QUARTZ_BUD),
                                        state(BuddingNetherQuartz.QUARTZ_CLUSTER)),
                                BlockTags.FEATURES_CANNOT_REPLACE,
                                BlockTags.GEODE_INVALID_BLOCKS),
                        new GeodeLayerSettings(1.7, 2.2, 3.2, 4.2),
                        new GeodeCrackSettings(0.95, 2.0, 2),
                        0.35,
                        0.083,
                        true,
                        UniformInt.of(4, 6),
                        UniformInt.of(3, 4),
                        UniformInt.of(1, 2),
                        -16,
                        16,
                        0.05,
                        1));
    }

    private static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        PlacementUtils.register(
                context,
                GEODE_PLACED,
                context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(GEODE_CONFIGURED),
                RarityFilter.onAverageOnceEvery(24),
                InSquarePlacement.spread(),
                PlacementUtils.FULL_RANGE,
                BiomeFilter.biome());
    }

    private static void bootstrapModifier(BootstrapContext<BiomeModifier> context) {
        context.register(MODIFIER, new BiomeModifiers.AddFeaturesBiomeModifier(
                context.lookup(Registries.BIOME).getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(context.lookup(Registries.PLACED_FEATURE).getOrThrow(GEODE_PLACED)),
                GenerationStep.Decoration.LOCAL_MODIFICATIONS));
    }

    private static BlockState state(Supplier<Block> block) {
        return block.get().defaultBlockState();
    }
}
