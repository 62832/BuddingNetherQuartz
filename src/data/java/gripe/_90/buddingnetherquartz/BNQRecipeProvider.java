package gripe._90.buddingnetherquartz;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipeBuilder;
import appeng.recipes.transform.TransformCircumstance;
import appeng.recipes.transform.TransformRecipeBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import org.jetbrains.annotations.NotNull;

public class BNQRecipeProvider extends RecipeProvider {
    public BNQRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        var conditional = output.withConditions(new ModLoadedCondition("ae2"));

        var water = TransformCircumstance.fluid(FluidTags.WATER);
        TransformRecipeBuilder.transform(
                conditional,
                makeId("ae2/damaged_budding_quartz"),
                AEBlocks.DAMAGED_BUDDING_QUARTZ,
                1,
                water,
                AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED,
                Blocks.QUARTZ_BLOCK);
        TransformRecipeBuilder.transform(
                conditional,
                makeId("ae2/damaged_budding_quartz_smooth"),
                AEBlocks.DAMAGED_BUDDING_QUARTZ,
                1,
                water,
                AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED,
                Blocks.SMOOTH_QUARTZ);
        TransformRecipeBuilder.transform(
                conditional,
                makeId("ae2/chipped_budding_quartz"),
                AEBlocks.CHIPPED_BUDDING_QUARTZ,
                1,
                water,
                AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED,
                AEBlocks.DAMAGED_BUDDING_QUARTZ);
        TransformRecipeBuilder.transform(
                conditional,
                makeId("ae2/flawed_budding_quartz"),
                AEBlocks.FLAWED_BUDDING_QUARTZ,
                1,
                water,
                AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED,
                AEBlocks.CHIPPED_BUDDING_QUARTZ);

        TransformRecipeBuilder.transform(
                conditional,
                makeId("ae2/nether_quartz"),
                Items.QUARTZ,
                1,
                water,
                Ingredient.of(BuddingNetherQuartz.QUARTZ_DUST_TAG),
                Ingredient.of(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED));

        InscriberRecipeBuilder.inscribe(Tags.Items.GEMS_QUARTZ, BuddingNetherQuartz.QUARTZ_DUST, 1)
                .setMode(InscriberProcessType.INSCRIBE)
                .save(conditional, makeId("ae2/quartz_dust"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(BuddingNetherQuartz.QUARTZ_DUST_TAG),
                        RecipeCategory.MISC,
                        AEItems.SILICON,
                        .35F,
                        200)
                .unlockedBy("has_certus_quartz_dust", has(BuddingNetherQuartz.QUARTZ_DUST_TAG))
                .save(conditional, makeId("ae2/silicon_smelting"));
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(BuddingNetherQuartz.QUARTZ_DUST_TAG),
                        RecipeCategory.MISC,
                        AEItems.SILICON,
                        .35F,
                        100)
                .unlockedBy("has_certus_quartz_dust", has(BuddingNetherQuartz.QUARTZ_DUST_TAG))
                .save(conditional, makeId("ae2/silicon_blasting"));
    }

    private ResourceLocation makeId(String id) {
        return ResourceLocation.fromNamespaceAndPath(BuddingNetherQuartz.MODID, id);
    }
}
