package gripe._90.buddingnetherquartz;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BNQModelProvider extends BlockStateProvider {
    public BNQModelProvider(PackOutput output, ExistingFileHelper existing) {
        super(output, BuddingNetherQuartz.MODID, existing);
    }

    @Override
    protected void registerStatesAndModels() {
        budding(BuddingNetherQuartz.FLAWLESS_BUDDING_QUARTZ.get());
        budding(BuddingNetherQuartz.FLAWED_BUDDING_QUARTZ.get());
        budding(BuddingNetherQuartz.CHIPPED_BUDDING_QUARTZ.get());
        budding(BuddingNetherQuartz.DAMAGED_BUDDING_QUARTZ.get());

        cluster(BuddingNetherQuartz.SMALL_QUARTZ_BUD.get());
        cluster(BuddingNetherQuartz.MEDIUM_QUARTZ_BUD.get());
        cluster(BuddingNetherQuartz.LARGE_QUARTZ_BUD.get());
        cluster(BuddingNetherQuartz.QUARTZ_CLUSTER.get());

        itemModels().basicItem(BuddingNetherQuartz.QUARTZ_DUST.get());
    }

    private void budding(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }

    private void cluster(Block block) {
        var name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        var texture = ResourceLocation.fromNamespaceAndPath(BuddingNetherQuartz.MODID, "block/" + name);
        directionalBlock(block, models().cross(name, texture).renderType("cutout"));
        itemModels().withExistingParent(name, mcLoc("item/generated")).texture("layer0", texture);
    }
}
