package eth22.backroomswanderer.dimension.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eth22.backroomswanderer.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Level0ChunkGenerator extends ChunkGenerator {

    public static final MapCodec<Level0ChunkGenerator> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(Level0ChunkGenerator::getBiomeSource),
                    Codec.INT.fieldOf("sea_level").forGetter(Level0ChunkGenerator::getSeaLevel),
                    Codec.INT.fieldOf("world_height").forGetter(Level0ChunkGenerator::getWorldHeight)
            ).apply(instance, Level0ChunkGenerator::new)
    );

    // Fields for customization
    private final BiomeSource biomeSource;
    private final int seaLevel;
    private final int worldHeight;

    // Constructor that will be called when creating a new instance from the Codec
    public Level0ChunkGenerator(BiomeSource biomeSource, int seaLevel, int worldHeight) {
        super(biomeSource);
        this.biomeSource = biomeSource;
        this.seaLevel = seaLevel;
        this.worldHeight = worldHeight;
    }

    // Getter methods for the Codec
    public BiomeSource getBiomeSource() {
        return biomeSource;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return MAP_CODEC;
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, net.minecraft.world.biome.source.BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep) {
        // No carving
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkPos.getStartX() + x;
                int worldZ = chunkPos.getStartZ() + z;

                // Set the floor block
                chunk.setBlockState(new BlockPos(worldX, 0, worldZ), ModBlocks.LEVEL_0_CARPET_BLOCK.getDefaultState(), false);

                // Set the ceiling block
                chunk.setBlockState(new BlockPos(worldX, 5, worldZ), ModBlocks.LEVEL_0_TILE.getDefaultState(), false);

                // Set the lights block
                if ((x == 4 || x == 11) && (z == 4 || z == 11)) {
                    chunk.setBlockState(new BlockPos(worldX, 5, worldZ), ModBlocks.LEVEL_0_LIGHT.getDefaultState(), false);
                }

                // Set the wall blocks on the north and west edges, with a centered 4x3 hole
                if (x == 0 && (z < 6 || z > 9)) {
                    for (int y = 1; y <= 4; y++) {
                        chunk.setBlockState(new BlockPos(worldX, y, worldZ), ModBlocks.LEVEL_0_WALLPAPER.getDefaultState(), false);
                    }
                } else if (x == 0) {
                        chunk.setBlockState(new BlockPos(worldX, 4, worldZ), ModBlocks.LEVEL_0_WALLPAPER.getDefaultState(), false);
                }

                if (z == 0 && (x < 6 || x > 9)) {
                    for (int y = 1; y <= 4; y++) {
                        chunk.setBlockState(new BlockPos(worldX, y, worldZ), ModBlocks.LEVEL_0_WALLPAPER.getDefaultState(), false);
                    }
                } else if (z == 0) {
                    chunk.setBlockState(new BlockPos(worldX, 4, worldZ), ModBlocks.LEVEL_0_WALLPAPER.getDefaultState(), false);
                }
            }
        }
    }

    @Override
    public void populateEntities(ChunkRegion region) {
        // No entities
    }

    @Override
    public int getWorldHeight() {
        return worldHeight;
    }

    @Override
    public int getSeaLevel() {
        return seaLevel;
    }


    @Override
    public int getMinimumY() {
        return 0;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return 0;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        return new VerticalBlockSample(0, new net.minecraft.block.BlockState[]{Blocks.WHITE_WOOL.getDefaultState()});
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {
        text.add("Level 0 Chunk Generator - Flat Wool World");
    }
}
