package eth22.backroomswanderer.block.custom.level_0;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.random.Random;

public class BlinkingLightBlock extends Block {

    public enum BlinkType implements StringIdentifiable {
        IDLE("idle"),
        SHORT_BLINK_ON("short_blink_on"),
        SHORT_BLINK_OFF("short_blink_off"),
        END_OF_SEQUENCE("end_of_sequence");

        private final String name;

        BlinkType(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

    public static final EnumProperty<BlinkType> BLINK_TYPE = EnumProperty.of("blink_type", BlinkType.class);
    public static final BooleanProperty LIT = Properties.LIT;
    public int BLINK_COUNT = 0;

    public BlinkingLightBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getDefaultState().with(LIT, true).with(BLINK_TYPE, BlinkType.IDLE));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return false;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, BLINK_TYPE);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlinkType previousBlinkType = state.get(BLINK_TYPE);
        BLINK_COUNT++;

        if (previousBlinkType == BlinkType.IDLE) { // Decide next blink type
            BLINK_COUNT = 1;
            if (random.nextFloat() < 0.8f) { // 80% chance: short blink
                state = state.with(BLINK_TYPE, BlinkType.SHORT_BLINK_OFF).with(LIT, false);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);

                int nextTick = 2 + random.nextInt(7); // 2 to 8 ticks
                world.scheduleBlockTick(pos, this, nextTick);
            } else { // 20% chance: long blink
                state = state.with(BLINK_TYPE, BlinkType.END_OF_SEQUENCE).with(LIT, false);
                world.setBlockState(pos, state, Block.NOTIFY_ALL);

                int nextTick = 60 + random.nextInt(101); // 60 to 160 ticks
                world.scheduleBlockTick(pos, this, nextTick);
            }
        } else if (previousBlinkType == BlinkType.SHORT_BLINK_OFF) { // If last blink was short blink off, continue sequence
            int nextTick = 2 + random.nextInt(7); // 2 to 8 ticks

            if (BLINK_COUNT < 3) { // If blinks are less than 3, always continue the sequence
                state = state.with(BLINK_TYPE, BlinkType.SHORT_BLINK_ON).with(LIT, true);
            } else if (BLINK_COUNT < 8) { // If blinks are between 3 and 7, roll a 70% chance to continue the sequence
                boolean continueBlink = random.nextFloat() < 0.7f;

                if (continueBlink) {
                    state = state.with(BLINK_TYPE, BlinkType.SHORT_BLINK_ON).with(LIT, true);
                } else {
                    state = state.with(BLINK_TYPE, BlinkType.END_OF_SEQUENCE).with(LIT, false);
                }

                world.setBlockState(pos, state, Block.NOTIFY_ALL);
                world.scheduleBlockTick(pos, this, nextTick);
            } else { // If blinks are 8 or more, always end the sequence
                state = state.with(BLINK_TYPE, BlinkType.END_OF_SEQUENCE).with(LIT, false);
            }

            world.setBlockState(pos, state, Block.NOTIFY_ALL);
            world.scheduleBlockTick(pos, this, nextTick);

        } else if (previousBlinkType == BlinkType.SHORT_BLINK_ON) { // After short blink on, immediately go back off
            state = state.with(BLINK_TYPE, BlinkType.SHORT_BLINK_OFF).with(LIT, false);
            world.setBlockState(pos, state, Block.NOTIFY_ALL);

            int nextTick = 2 + random.nextInt(11); // 2 to 12 ticks
            world.scheduleBlockTick(pos, this, nextTick);
        } else if (previousBlinkType == BlinkType.END_OF_SEQUENCE) { // If last blink was end of sequence, turn back on and wait 5 to 10s
            state = state.with(BLINK_TYPE, BlinkType.IDLE).with(LIT, true);
            world.setBlockState(pos, state, Block.NOTIFY_ALL);

            int idleWaitTime = 200 + random.nextInt(401); // 10s to 30s
            world.scheduleBlockTick(pos, this, idleWaitTime);
        } else {
            world.scheduleBlockTick(pos, this, 100); // Safety Fallback
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, net.minecraft.entity.LivingEntity placer, net.minecraft.item.ItemStack itemStack) {
        if (!world.isClient) {
            world.scheduleBlockTick(pos, this, 100);
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }
}
