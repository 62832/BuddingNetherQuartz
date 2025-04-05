package gripe._90.buddingnetherquartz;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

public class BNQLootProvider extends LootTableProvider {
    public BNQLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)), registries);
    }

    private static class BlockLoot extends BlockLootSubProvider {
        protected BlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
        }

        @Override
        public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> writer) {
            generate();
            map.forEach(writer);
        }

        @Override
        public void generate() {
            add(
                    BuddingNetherQuartz.FLAWLESS_BUDDING_QUARTZ.get(),
                    createSingleItemTable(BuddingNetherQuartz.FLAWLESS_BUDDING_QUARTZ));

            budding(BuddingNetherQuartz.FLAWED_BUDDING_QUARTZ, BuddingNetherQuartz.CHIPPED_BUDDING_QUARTZ);
            budding(BuddingNetherQuartz.CHIPPED_BUDDING_QUARTZ, BuddingNetherQuartz.DAMAGED_BUDDING_QUARTZ);
            budding(BuddingNetherQuartz.DAMAGED_BUDDING_QUARTZ, Blocks.SMOOTH_QUARTZ);

            bud(BuddingNetherQuartz.SMALL_QUARTZ_BUD);
            bud(BuddingNetherQuartz.MEDIUM_QUARTZ_BUD);
            bud(BuddingNetherQuartz.LARGE_QUARTZ_BUD);

            add(
                    BuddingNetherQuartz.QUARTZ_CLUSTER.get(),
                    createSilkTouchDispatchTable(
                            BuddingNetherQuartz.QUARTZ_CLUSTER.get(),
                            LootItem.lootTableItem(Items.QUARTZ)
                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4)))
                                    .apply(ApplyBonusCount.addUniformBonusCount(registries
                                            .lookupOrThrow(Registries.ENCHANTMENT)
                                            .getOrThrow(Enchantments.FORTUNE)))
                                    .apply(ApplyExplosionDecay.explosionDecay())));
        }

        private void budding(Supplier<Block> initial, ItemLike degraded) {
            add(initial.get(), createSingleItemTableWithSilkTouch(initial.get(), degraded));
        }

        private void bud(Supplier<Block> block) {
            add(block.get(), createSingleItemTableWithSilkTouch(block.get(), BuddingNetherQuartz.QUARTZ_DUST));
        }
    }
}
