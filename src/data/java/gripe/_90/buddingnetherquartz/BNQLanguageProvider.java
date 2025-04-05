package gripe._90.buddingnetherquartz;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class BNQLanguageProvider extends LanguageProvider {
    public BNQLanguageProvider(PackOutput output) {
        super(output, BuddingNetherQuartz.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(BuddingNetherQuartz.FLAWLESS_BUDDING_QUARTZ, "Flawless Budding Nether Quartz");
        addBlock(BuddingNetherQuartz.FLAWED_BUDDING_QUARTZ, "Flawed Budding Nether Quartz");
        addBlock(BuddingNetherQuartz.CHIPPED_BUDDING_QUARTZ, "Chipped Budding Nether Quartz");
        addBlock(BuddingNetherQuartz.DAMAGED_BUDDING_QUARTZ, "Damaged Budding Nether Quartz");

        addBlock(BuddingNetherQuartz.SMALL_QUARTZ_BUD, "Small Nether Quartz Bud");
        addBlock(BuddingNetherQuartz.MEDIUM_QUARTZ_BUD, "Medium Nether Quartz Bud");
        addBlock(BuddingNetherQuartz.LARGE_QUARTZ_BUD, "Large Nether Quartz Bud");
        addBlock(BuddingNetherQuartz.QUARTZ_CLUSTER, "Nether Quartz Cluster");

        addItem(BuddingNetherQuartz.QUARTZ_DUST, "Nether Quartz Dust");
    }
}
