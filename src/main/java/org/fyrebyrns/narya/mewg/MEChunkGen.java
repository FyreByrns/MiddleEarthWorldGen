package org.fyrebyrns.narya.mewg;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.fyrebyrns.narya.mewg.LOTRMap.*;

public class MEChunkGen extends ChunkGenerator {
    //<editor-fold desc="boilerplate">
    public MEChunkGen(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> noiseGeneratorSettingsHolder) {
        super(biomeSource);
        settings = noiseGeneratorSettingsHolder;
    }

    @Override
    public int getSeaLevel() {
        return 64;
    }

    @Override
    public int getGenDepth() {
        return 100;
    }

    @Override
    public int getMinY() {
        return -64;
    }

    @Override
    public int getBaseHeight(int i, int j, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        return 100;
    }

    @Override
    public void addDebugScreenInfo(List<String> list, RandomState randomState, BlockPos blockPos) {
    }
    //</editor-fold>
    //<editor-fold desc="codec">
    public static final MapCodec<MEChunkGen> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            BiomeSource.CODEC.fieldOf("biome_source").forGetter(n -> n.biomeSource),
                            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(n -> n.settings)
                    )
                    .apply(instance, instance.stable(MEChunkGen::new))
    );
    private final Holder<NoiseGeneratorSettings> settings;

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }
    //</editor-fold>

    //<editor-fold desc="more complicated worldgen">
    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion, long l, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess) {
    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {
    }
    //</editor-fold>


    float lerp(float a, float b, float f) {
        return (float) (a * (1.0 - f) + (b * f));
    }

    // how many square meters each pixel of the map represents
    private static int mapCellSmoothing = 4;
    private static int smoothingSkip = 1;

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(
            Blender blender,
            RandomState randomState,
            StructureManager structureManager,
            ChunkAccess chunkAccess) {

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        BlockState fillerBlock = Blocks.COARSE_DIRT.defaultBlockState();
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();

        int cX = chunkAccess.getPos().x;
        int cZ = chunkAccess.getPos().z;

        final int chunkSize = 16;

        for (int x = 0; x < chunkSize; x++) {
            for (int z = 0; z < chunkSize; z++) {
                int height = getSeaLevel() + 2;
                BlockState block = fillerBlock;

                int worldX = cX * chunkSize + x;
                int worldZ = cZ * chunkSize + z;

                MapPosition mP = LOTRMap.getMapPos(worldX, worldZ);

                // get average of surrounding height
                int total = 0;
                int count = 0;
                for(int xx = -BLOCKS_PER_MAP_CELL/2 * mapCellSmoothing; xx < BLOCKS_PER_MAP_CELL/2* mapCellSmoothing; xx+=smoothingSkip) {
                    for(int zz = -BLOCKS_PER_MAP_CELL/2* mapCellSmoothing; zz < BLOCKS_PER_MAP_CELL/2* mapCellSmoothing; zz+=smoothingSkip) {
                        int wx = worldX + xx;
                        int wz = worldZ + zz;

                        int mx = wx / BLOCKS_PER_MAP_CELL;
                        int mz = wz / BLOCKS_PER_MAP_CELL;

                        int h = LOTRMap.getMapG(mx, mz);
                        if(LOTRMap.isWater(mx, mz)) {
                            h = getSeaLevel();
                        }
                        total += h;
                        count++;
                    }
                }
                height = total/count;

                boolean hasWater = LOTRMap.isWater(mP.x(), mP.y());
                if(hasWater) block = Blocks.WATER.defaultBlockState();
//                float percentageX = (float)(worldX % mapScale) / (float) mapScale;
//                float percentageZ = (float)(worldZ % mapScale) / (float) mapScale;
//                if(percentageX == 0 || percentageZ == 0) {
//                    block = Blocks.EMERALD_BLOCK.defaultBlockState();
//                }

                for (int _y = 0; _y < height; _y++) {
                    if(_y == 0) {
                        chunkAccess.setBlockState(pos.set(x, getMinY(), z), bedrock);
                        continue;
                    }

                    int y = chunkAccess.getMinY() + _y;
                    chunkAccess.setBlockState(pos.set(x, y, z), block);
                }

                if(hasWater && height < getSeaLevel()) {
                    for(int _y = height; _y < getSeaLevel(); _y++) {
                        int y = chunkAccess.getMinY() + _y;
                        chunkAccess.setBlockState(pos.set(x, y, z), Blocks.WATER.defaultBlockState());
                    }
                }
            }
        }

        return CompletableFuture.completedFuture(chunkAccess);
    }

    int test = 0;

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        BlockState fillerBlock = Blocks.STONE.defaultBlockState();

        int ySpan = 100 + Math.abs(getMinY());

        BlockState[] states = new BlockState[ySpan];

        for (int y = 0; y < ySpan; y++) {
            states[y] = fillerBlock;
        }

        return new NoiseColumn(getMinY(), states);
    }
}
