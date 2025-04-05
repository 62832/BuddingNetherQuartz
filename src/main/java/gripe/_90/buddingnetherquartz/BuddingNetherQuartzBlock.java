package gripe._90.buddingnetherquartz;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;

public class BuddingNetherQuartzBlock extends BuddingAmethystBlock {
    public static final int DECAY_CHANCE = 12;

    public BuddingNetherQuartzBlock(Properties props) {
        super(props.randomTicks());
    }

    @ParametersAreNonnullByDefault
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(GROWTH_CHANCE) != 0) {
            return;
        }

        var direction = Util.getRandom(Direction.values(), random);
        var targetPos = pos.relative(direction);
        var targetState = level.getBlockState(targetPos);

        Block newCluster = null;

        if (canClusterGrowAtState(targetState)) {
            newCluster = BuddingNetherQuartz.SMALL_QUARTZ_BUD.get();
        } else if (targetState.is(BuddingNetherQuartz.SMALL_QUARTZ_BUD.get())
                && targetState.getValue(BlockStateProperties.FACING) == direction) {
            newCluster = BuddingNetherQuartz.MEDIUM_QUARTZ_BUD.get();
        } else if (targetState.is(BuddingNetherQuartz.MEDIUM_QUARTZ_BUD.get())
                && targetState.getValue(BlockStateProperties.FACING) == direction) {
            newCluster = BuddingNetherQuartz.LARGE_QUARTZ_BUD.get();
        } else if (targetState.is(BuddingNetherQuartz.LARGE_QUARTZ_BUD.get())
                && targetState.getValue(BlockStateProperties.FACING) == direction) {
            newCluster = BuddingNetherQuartz.QUARTZ_CLUSTER.get();
        }

        if (newCluster == null) {
            return;
        }

        var newClusterState = newCluster
                .defaultBlockState()
                .setValue(BlockStateProperties.FACING, direction)
                .setValue(
                        BlockStateProperties.WATERLOGGED,
                        targetState.getFluidState().getType() == Fluids.WATER);
        level.setBlockAndUpdate(targetPos, newClusterState);

        if (this == BuddingNetherQuartz.FLAWLESS_BUDDING_QUARTZ.get() || random.nextInt(DECAY_CHANCE) != 0) {
            return;
        }

        Block newBlock;

        if (this == BuddingNetherQuartz.FLAWED_BUDDING_QUARTZ.get()) {
            newBlock = BuddingNetherQuartz.CHIPPED_BUDDING_QUARTZ.get();
        } else if (this == BuddingNetherQuartz.CHIPPED_BUDDING_QUARTZ.get()) {
            newBlock = BuddingNetherQuartz.DAMAGED_BUDDING_QUARTZ.get();
        } else if (this == BuddingNetherQuartz.DAMAGED_BUDDING_QUARTZ.get()) {
            newBlock = Blocks.SMOOTH_QUARTZ;
        } else {
            throw new IllegalStateException("Unexpected block: " + this);
        }

        level.setBlockAndUpdate(pos, newBlock.defaultBlockState());
    }
}
