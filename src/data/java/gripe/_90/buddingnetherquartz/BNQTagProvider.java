package gripe._90.buddingnetherquartz;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class BNQTagProvider {
    public static class Blocks extends IntrinsicHolderTagsProvider<Block> {
        public Blocks(
                PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existing) {
            super(
                    output,
                    Registries.BLOCK,
                    registries,
                    block -> BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow(),
                    BuddingNetherQuartz.MODID,
                    existing);
        }

        @Override
        protected void addTags(@NotNull HolderLookup.Provider provider) {
            tag(Tags.Blocks.BUDDING_BLOCKS)
                    .add(BuddingNetherQuartz.FLAWLESS_BUDDING_QUARTZ.value())
                    .add(BuddingNetherQuartz.FLAWED_BUDDING_QUARTZ.value())
                    .add(BuddingNetherQuartz.CHIPPED_BUDDING_QUARTZ.value())
                    .add(BuddingNetherQuartz.DAMAGED_BUDDING_QUARTZ.value());

            tag(Tags.Blocks.BUDS)
                    .add(BuddingNetherQuartz.SMALL_QUARTZ_BUD.value())
                    .add(BuddingNetherQuartz.MEDIUM_QUARTZ_BUD.value())
                    .add(BuddingNetherQuartz.LARGE_QUARTZ_BUD.value());
            tag(Tags.Blocks.CLUSTERS).add(BuddingNetherQuartz.QUARTZ_CLUSTER.value());
        }

        @NotNull
        @Override
        public String getName() {
            return "Tags (Block)";
        }
    }

    public static class Items extends ItemTagsProvider {
        public Items(
                PackOutput output,
                CompletableFuture<HolderLookup.Provider> registries,
                CompletableFuture<TagsProvider.TagLookup<net.minecraft.world.level.block.Block>> blockTags,
                ExistingFileHelper existing) {
            super(output, registries, blockTags, BuddingNetherQuartz.MODID, existing);
        }

        @Override
        protected void addTags(@NotNull HolderLookup.Provider provider) {
            copy(Tags.Blocks.BUDDING_BLOCKS, Tags.Blocks.BUDS, Tags.Blocks.CLUSTERS);

            tag(BuddingNetherQuartz.QUARTZ_DUST_TAG).add(BuddingNetherQuartz.QUARTZ_DUST.asItem());
            tag(Tags.Items.DUSTS).addTag(BuddingNetherQuartz.QUARTZ_DUST_TAG);

            tag(TagKey.create(Registries.ITEM, ResourceLocation.parse("ae2:all_quartz_dust")))
                    .addTag(BuddingNetherQuartz.QUARTZ_DUST_TAG);
        }

        @SafeVarargs
        private void copy(TagKey<Block>... blockTags) {
            for (var tag : blockTags) {
                copy(tag, TagKey.create(Registries.ITEM, tag.location()));
            }
        }

        @NotNull
        @Override
        public String getName() {
            return "Tags (Item)";
        }
    }
}
