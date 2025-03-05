package eth22.backroomswanderer.block.custom.level_0;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.random.Random;

public class Level0LightBlock extends Block {

    public static final BooleanProperty LIT = Properties.LIT;

    public Level0LightBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getDefaultState().with(LIT, true)); // Starts in "on" state
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean currentlyLit = state.get(LIT);
        world.setBlockState(pos, state.with(LIT, !currentlyLit), Block.NOTIFY_ALL);

        int nextBlinkTime = 20 + random.nextInt(40);
        world.scheduleBlockTick(pos, this, nextBlinkTime);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, net.minecraft.entity.LivingEntity placer, net.minecraft.item.ItemStack itemStack) {
        if (!world.isClient) {
            world.scheduleBlockTick(pos, this, 100);
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    public static int getLuminance(BlockState state) {
        return state.get(LIT) ? 15 : 0;
    }
}
