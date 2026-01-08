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

                for (int _y = 0; _y < height; _y++) {
                    if(_y == 0) {
                        chunkAccess.setBlockState(pos.set(x, getMinY(), z), bedrock);
                        continue;
                    }

                    int y = chunkAccess.getMinY() + _y;
                    chunkAccess.setBlockState(pos.set(x, y, z), block);
                }
            }
        }

        return CompletableFuture.completedFuture(chunkAccess);
    }

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
