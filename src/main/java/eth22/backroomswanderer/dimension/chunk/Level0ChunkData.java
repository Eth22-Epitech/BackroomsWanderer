package eth22.backroomswanderer.dimension.chunk;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class Level0ChunkData {
    private static final String HAS_LIGHTS_KEY = "HasLights";
    private static final BlockPos DATA_POS = new BlockPos(0, 0, 0);

    private boolean hasLights;

    public Level0ChunkData(boolean hasLights) {
        this.hasLights = hasLights;
    }

    public boolean hasLights() {
        return hasLights;
    }

    public void setHasLights(boolean hasLights) {
        this.hasLights = hasLights;
    }

    public static Level0ChunkData fromChunk(Chunk chunk) {
        NbtCompound nbt = chunk.getBlockEntityNbt(DATA_POS);
        if (nbt != null && nbt.contains(HAS_LIGHTS_KEY)) {
            return new Level0ChunkData(nbt.getBoolean(HAS_LIGHTS_KEY));
        }
        return new Level0ChunkData(true);
    }

    public void toChunk(Chunk chunk) {
        NbtCompound nbt = chunk.getBlockEntityNbt(DATA_POS);
        if (nbt == null) {
            nbt = new NbtCompound();
        }
        nbt.putBoolean(HAS_LIGHTS_KEY, hasLights);
        chunk.addPendingBlockEntityNbt(nbt);
    }
}