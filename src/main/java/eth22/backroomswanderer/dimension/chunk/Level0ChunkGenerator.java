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
import java.util.Random;
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

    private void generateFloor(Chunk chunk, ChunkPos chunkPos, int yOffset) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkPos.getStartX() + x;
                int worldZ = chunkPos.getStartZ() + z;
                chunk.setBlockState(new BlockPos(worldX, yOffset, worldZ), ModBlocks.LEVEL_0_CARPET_BLOCK.getDefaultState(), false);
            }
        }
    }

    private void generateCeiling(Chunk chunk, ChunkPos chunkPos, boolean generateLights, int yOffset) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkPos.getStartX() + x;
                int worldZ = chunkPos.getStartZ() + z;
                chunk.setBlockState(new BlockPos(worldX, 5 + yOffset, worldZ), ModBlocks.LEVEL_0_TILE.getDefaultState(), false);

                // Set the lights block
                if (generateLights && ((x == 5 || x == 11) && (z == 5 || z == 11))) {
                    chunk.setBlockState(new BlockPos(worldX, 5 + yOffset, worldZ), ModBlocks.LEVEL_0_LIGHT.getDefaultState(), false);
                }
                if (generateLights && ((x == 0 || x == 8) && (z == 0 || z == 8))) {
                    chunk.setBlockState(new BlockPos(worldX, 5 + yOffset, worldZ), ModBlocks.LEVEL_0_LIGHT.getDefaultState(), false);
                }
            }
        }
    }

    private void generateWallsWithHoles(Chunk chunk, ChunkPos chunkPos, boolean generateWalls, int yOffset) {
        Random random = new Random(chunkPos.toLong());

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkPos.getStartX() + x;
                int worldZ = chunkPos.getStartZ() + z;

                // Generate the north wall if generateWalls is true
                if (generateWalls && x == 0) {
                    for (int y = 1; y <= 4; y++) {
                        if (y == 1) {
                            chunk.setBlockState(new BlockPos(worldX, y + yOffset, worldZ), ModBlocks.LEVEL_0_WALLPAPER_BOTTOM.getDefaultState(), false);
                        } else {
                            chunk.setBlockState(new BlockPos(worldX, y + yOffset, worldZ), ModBlocks.LEVEL_0_WALLPAPER.getDefaultState(), false);
                        }
                    }
                }

                // Generate the west wall if generateWalls is true
                if (generateWalls && z == 0) {
                    for (int y = 1; y <= 4; y++) {
                        if (y == 1) {
                            chunk.setBlockState(new BlockPos(worldX, y + yOffset, worldZ), ModBlocks.LEVEL_0_WALLPAPER_BOTTOM.getDefaultState(), false);
                        } else {
                            chunk.setBlockState(new BlockPos(worldX, y + yOffset, worldZ), ModBlocks.LEVEL_0_WALLPAPER.getDefaultState(), false);
                        }
                    }
                }
            }
        }

        if (generateWalls) {
            // Randomize the north hole
            int northHoleLength = random.nextInt(5) + 2;
            int northHoleStart = random.nextInt(16 - northHoleLength);
            for (int z = northHoleStart; z < northHoleStart + northHoleLength; z++) {
                for (int y = 1; y <= 4; y++) {
                    chunk.setBlockState(new BlockPos(chunkPos.getStartX(), y + yOffset, chunkPos.getStartZ() + z), Blocks.AIR.getDefaultState(), false);
                }
            }

            // Randomize the west hole
            int westHoleLength = random.nextInt(5) + 2;
            int westHoleStart = random.nextInt(16 - westHoleLength);
            for (int x = westHoleStart; x < westHoleStart + westHoleLength; x++) {
                for (int y = 1; y <= 4; y++) {
                    chunk.setBlockState(new BlockPos(chunkPos.getStartX() + x, y + yOffset, chunkPos.getStartZ()), Blocks.AIR.getDefaultState(), false);
                }
            }
        }
    }

    private void generateRandomWall(Chunk chunk, ChunkPos chunkPos, boolean generateWalls, int yOffset) {
        if (generateWalls) {
            return;
        }

        Random random = new Random(chunkPos.toLong());
        boolean isXAxis = random.nextBoolean();
        int startX = random.nextInt(12) + 2; // Ensure start is not on x == 0 or 1
        int startZ = random.nextInt(12) + 2; // Ensure start is not on z == 0 or 1
        int length = random.nextInt(16 - (isXAxis ? startX : startZ)) + 1;

        // 60% chance to extend the wall until the edge of the chunk
        if (random.nextInt(100) < 60) {
            length = 16 - (isXAxis ? startX : startZ);
        }

        // Generate the random wall
        for (int i = 0; i < length; i++) {
            int worldX = chunkPos.getStartX() + (isXAxis ? startX + i : startX);
            int worldZ = chunkPos.getStartZ() + (isXAxis ? startZ : startZ + i);

            for (int y = 1; y <= 4; y++) {
                if (y == 1) {
                    chunk.setBlockState(new BlockPos(worldX, y + yOffset, worldZ), ModBlocks.LEVEL_0_WALLPAPER_BOTTOM.getDefaultState(), false);
                } else {
                    chunk.setBlockState(new BlockPos(worldX, y + yOffset, worldZ), ModBlocks.LEVEL_0_WALLPAPER.getDefaultState(), false);
                }
            }
        }
    }

    private void extendWall(Chunk chunk, ChunkPos chunkPos, ChunkRegion region, boolean generateWalls, int yOffset) {
        if (generateWalls) {
            return;
        }

        Random random = new Random(chunkPos.toLong());

        // Check for walls in all adjacent chunks and extend if necessary
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] direction : directions) {
            int dx = direction[0];
            int dz = direction[1];

            ChunkPos neighborPos = new ChunkPos(chunkPos.x + dx, chunkPos.z + dz);
            Chunk neighborChunk = region.getChunk(neighborPos.x, neighborPos.z);
            BlockPos wallPos = neighborChunk != null ? hasWallAtEdge(neighborChunk, dx, dz, yOffset) : null;
            if (wallPos != null) {
                int length = random.nextInt(100) < 50 ? 16 : random.nextInt(16) + 1;
                boolean hasHole = length == 16 && random.nextInt(100) < 40;
                int holeStart = 0;
                int holeWidth = 0;

                if (hasHole) {
                    holeWidth = random.nextInt(6) + 1;
                    holeStart = random.nextInt(16 - holeWidth);
                }

                for (int i = 0; i < length; i++) {
                    int worldX = chunkPos.getStartX() + (dx == 1 ? 15 - i : (dx == -1 ? i : wallPos.getX() - chunkPos.getStartX()));
                    int worldZ = chunkPos.getStartZ() + (dz == 1 ? 15 - i : (dz == -1 ? i : wallPos.getZ() - chunkPos.getStartZ()));

                    for (int y = 1; y <= 4; y++) {
                        if (hasHole && i >= holeStart && i < holeStart + holeWidth) {
                            chunk.setBlockState(new BlockPos(worldX, y + yOffset, worldZ), Blocks.AIR.getDefaultState(), false);
                        } else {
                            if (y == 1) {
                                chunk.setBlockState(new BlockPos(worldX, y + yOffset, worldZ), ModBlocks.LEVEL_0_WALLPAPER_BOTTOM.getDefaultState(), false);
                            } else {
                                chunk.setBlockState(new BlockPos(worldX, y + yOffset, worldZ), ModBlocks.LEVEL_0_WALLPAPER.getDefaultState(), false);
                            }
                        }
                    }
                }
            }
        }
    }

    private BlockPos hasWallAtEdge(Chunk chunk, int dx, int dz, int yOffset) {
        int edgeX = dx == -1 ? 15 : (dx == 1 ? 0 : -1);
        int edgeZ = dz == -1 ? 15 : (dz == 1 ? 0 : -1);

        for (int y = 1; y <= 4; y++) {
            for (int i = 0; i < 16; i++) {
                int x = dx == 0 ? i : edgeX;
                int z = dz == 0 ? i : edgeZ;
                if (x == -1 || z == -1) continue;

                BlockPos pos = new BlockPos(chunk.getPos().getStartX() + x, y + yOffset, chunk.getPos().getStartZ() + z);
                if (chunk.getBlockState(pos).isOf(ModBlocks.LEVEL_0_WALLPAPER) || chunk.getBlockState(pos).isOf(ModBlocks.LEVEL_0_WALLPAPER_BOTTOM)) {
                    return pos;
                }
            }
        }
        return null;
    }

    private void generateBiomeLiminalHalls(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
        ChunkPos chunkPos = chunk.getPos();
        Random random = new Random(chunkPos.toLong());

        Level0ChunkData chunkData = Level0ChunkData.fromChunk(chunk);

        // Check neighboring chunks
        boolean neighborWithoutLights = false;
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] direction : directions) {
            int dx = direction[0];
            int dz = direction[1];

            ChunkPos neighborPos = new ChunkPos(chunkPos.x + dx, chunkPos.z + dz);
            Chunk neighborChunk = region.getChunk(neighborPos.x, neighborPos.z);
            if (neighborChunk != null) {
                Level0ChunkData neighborData = Level0ChunkData.fromChunk(neighborChunk);
                if (!neighborData.hasLights()) {
                    neighborWithoutLights = true;
                    break;
                }
            }
        }

        // === Random Elements ===
        boolean generateLights = random.nextInt(100) < (neighborWithoutLights ? 35 : 98);
        chunkData.setHasLights(generateLights);
        chunkData.toChunk(chunk);

        boolean generateWalls = random.nextInt(100) < 50;

        // Generate the floor
        generateFloor(chunk, chunkPos, 0);

        // Generate the ceiling
        generateCeiling(chunk, chunkPos, generateLights, 0);

        // Generate the walls
        generateWallsWithHoles(chunk, chunkPos, generateWalls, 0);
        generateRandomWall(chunk, chunkPos, generateWalls, 0);
        extendWall(chunk, chunkPos, region, generateWalls, 0);
        extendWall(chunk, chunkPos, region, generateWalls, 0);
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
        // Liminal Halls Biome
        generateBiomeLiminalHalls(region, structures, noiseConfig, chunk);
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
