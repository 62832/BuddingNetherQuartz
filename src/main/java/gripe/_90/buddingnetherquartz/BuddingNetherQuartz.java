package gripe._90.buddingnetherquartz;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(BuddingNetherQuartz.MODID)
public class BuddingNetherQuartz {
    public static final String MODID = "buddingnetherquartz";

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    private static final BlockBehaviour.Properties BUDDING_PROPS = BlockBehaviour.Properties.of()
            .strength(3, 5)
            .mapColor(MapColor.QUARTZ)
            .requiresCorrectToolForDrops()
            .pushReaction(PushReaction.DESTROY)
            .randomTicks();
    private static final BlockBehaviour.Properties CLUSTER_PROPS = BlockBehaviour.Properties.of()
            .strength(1.5F)
            .mapColor(MapColor.QUARTZ)
            .sound(SoundType.AMETHYST_CLUSTER)
            .pushReaction(PushReaction.DESTROY)
            .forceSolidOn()
            .noOcclusion();

    public static final DeferredBlock<Block> FLAWLESS_BUDDING_QUARTZ = budding("flawless_budding_quartz");
    public static final DeferredBlock<Block> FLAWED_BUDDING_QUARTZ = budding("flawed_budding_quartz");
    public static final DeferredBlock<Block> CHIPPED_BUDDING_QUARTZ = budding("chipped_budding_quartz");
    public static final DeferredBlock<Block> DAMAGED_BUDDING_QUARTZ = budding("damaged_budding_quartz");

    public static final DeferredBlock<Block> SMALL_QUARTZ_BUD = cluster("small_quartz_bud", 3, 4);
    public static final DeferredBlock<Block> MEDIUM_QUARTZ_BUD = cluster("medium_quartz_bud", 4, 3);
    public static final DeferredBlock<Block> LARGE_QUARTZ_BUD = cluster("large_quartz_bud", 5, 3);
    public static final DeferredBlock<Block> QUARTZ_CLUSTER = cluster("quartz_cluster", 7, 3);

    public static final DeferredItem<Item> QUARTZ_DUST = ITEMS.registerItem("quartz_dust", Item::new);
    public static final TagKey<Item> QUARTZ_DUST_TAG = ItemTags.create(ResourceLocation.parse("c:dusts/nether_quartz"));

    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, MODID);
    public static final DeferredHolder<Feature<?>, Feature<GeodeConfiguration>> QUARTZ_GEODE =
            FEATURES.register("geode", () -> new GeodeFeature(GeodeConfiguration.CODEC));

    public BuddingNetherQuartz(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        FEATURES.register(eventBus);

        eventBus.addListener(BuildCreativeModeTabContentsEvent.class, event -> {
            if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
                event.insertAfter(
                        Items.AMETHYST_CLUSTER.getDefaultInstance(),
                        FLAWLESS_BUDDING_QUARTZ.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(
                        FLAWLESS_BUDDING_QUARTZ.toStack(),
                        FLAWED_BUDDING_QUARTZ.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(
                        FLAWED_BUDDING_QUARTZ.toStack(),
                        CHIPPED_BUDDING_QUARTZ.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(
                        CHIPPED_BUDDING_QUARTZ.toStack(),
                        DAMAGED_BUDDING_QUARTZ.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

                event.insertAfter(
                        DAMAGED_BUDDING_QUARTZ.toStack(),
                        SMALL_QUARTZ_BUD.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(
                        SMALL_QUARTZ_BUD.toStack(),
                        MEDIUM_QUARTZ_BUD.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(
                        MEDIUM_QUARTZ_BUD.toStack(),
                        LARGE_QUARTZ_BUD.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(
                        LARGE_QUARTZ_BUD.toStack(),
                        QUARTZ_CLUSTER.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
                event.insertAfter(
                        Items.QUARTZ.getDefaultInstance(),
                        QUARTZ_DUST.toStack(),
                        CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        });
    }

    private static DeferredBlock<Block> budding(String id) {
        return BLOCKS.register(id, () -> {
            var block = new BuddingNetherQuartzBlock(BUDDING_PROPS);
            ITEMS.registerSimpleBlockItem(id, () -> block);
            return block;
        });
    }

    private static DeferredBlock<Block> cluster(String id, float height, float aabbOffset) {
        return BLOCKS.register(id, () -> {
            var block = new NetherQuartzClusterBlock(height, aabbOffset, CLUSTER_PROPS);
            ITEMS.registerSimpleBlockItem(id, () -> block);
            return block;
        });
    }
}
