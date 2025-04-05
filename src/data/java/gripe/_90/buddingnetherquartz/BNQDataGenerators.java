package gripe._90.buddingnetherquartz;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = BuddingNetherQuartz.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BNQDataGenerators {
    @SubscribeEvent
    public static void generate(GatherDataEvent event) {
        var generator = event.getGenerator();

        var output = generator.getPackOutput();
        generator.addProvider(event.includeClient(), new BNQLanguageProvider(output));

        var existing = event.getExistingFileHelper();
        generator.addProvider(event.includeClient(), new BNQModelProvider(output, existing));

        var registries = event.getLookupProvider();
        generator.addProvider(event.includeServer(), new BNQLootProvider(output, registries));
        generator.addProvider(event.includeServer(), new BNQFeatureProvider(output, registries));

        var blockTags = new BNQTagProvider.Blocks(output, registries, existing);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(
                event.includeServer(),
                new BNQTagProvider.Items(output, registries, blockTags.contentsGetter(), existing));

        if (ModList.get().isLoaded("ae2")) {
            generator.addProvider(event.includeServer(), new BNQRecipeProvider(output, registries));
        }
    }
}
