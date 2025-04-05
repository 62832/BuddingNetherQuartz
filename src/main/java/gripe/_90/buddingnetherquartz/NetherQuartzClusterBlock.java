package gripe._90.buddingnetherquartz;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class NetherQuartzClusterBlock extends AmethystClusterBlock {
    public NetherQuartzClusterBlock(float height, float aabbOffset, Properties properties) {
        super(height, aabbOffset, properties);
    }

    @ParametersAreNonnullByDefault
    @Nonnull
    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        // Prevent dropping anything if there is no player to avoid dust and crystals floating around when the bud or
        // cluster's supporting block is destroyed.
        return params.getOptionalParameter(LootContextParams.THIS_ENTITY) != null
                ? super.getDrops(state, params)
                : List.of();
    }
}
