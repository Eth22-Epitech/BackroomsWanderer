package eth22.backroomswanderer.dimension;

import com.mojang.serialization.MapCodec;
import eth22.backroomswanderer.dimension.chunk.Level0ChunkGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class ModChunkGenerators {

    public static final RegistryKey<MapCodec<? extends ChunkGenerator>> LEVEL0_CHUNK_GENERATOR = RegistryKey.of(RegistryKeys.CHUNK_GENERATOR, Identifier.of("backroomswanderer", "level_0"));

    public static void registerChunkGenerators() {
        Registry.register(Registries.CHUNK_GENERATOR, LEVEL0_CHUNK_GENERATOR.getValue(), Level0ChunkGenerator.MAP_CODEC);
    }
}
