package org.fyrebyrns.narya.mewg;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.SurfaceRules.Context;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.*;

import static org.fyrebyrns.narya.mewg.LOTRMap.SEA_LEVEL;

public class MEChunkGen extends NoiseBasedChunkGenerator {
    public static final MapCodec<MEChunkGen> CODEC =
            RecordCodecBuilder
                    .mapCodec((instance) ->
                            instance.group(BiomeSource.CODEC.fieldOf("biome_source")
                                    .forGetter((mecg) -> mecg.biomeSource)
                                    , NoiseGeneratorSettings.CODEC.fieldOf("settings")
                                            .forGetter((mecg) -> mecg.settings))
                                    .apply(instance, instance.stable(MEChunkGen::new))
                    );

    public MEChunkGen(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> holder) {
        super(biomeSource, holder);
    }

    BlockState defaultBlock = Blocks.STONE.defaultBlockState();

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunk) {
        if (!SharedConstants.debugVoidTerrain(chunk.getPos()) && !SharedConstants.DEBUG_DISABLE_SURFACE) {
            WorldGenerationContext worldGenerationContext = new WorldGenerationContext(this, region);
            this.buildSurface(
                      chunk
                    , worldGenerationContext
                    , randomState
                    , structureManager
                    , region.getBiomeManager()
                    , region.registryAccess().lookupOrThrow(Registries.BIOME)
                    , Blender.of(region)
            );
        }
    }

    public void buildSurface(
              ChunkAccess chunkAccess
            , WorldGenerationContext worldGenerationContext
            , RandomState randomState
            , StructureManager structureManager
            , BiomeManager biomeManager
            , Registry<Biome> registry
            , Blender blender
    ) {
        NoiseChunk noiseChunk = chunkAccess.getOrCreateNoiseChunk(chunkAccessx -> this.createNoiseChunk(chunkAccessx, structureManager, blender, randomState));
        NoiseGeneratorSettings noiseGeneratorSettings = this.settings.value();
        meBuildSurface(
                randomState
                , biomeManager
                , registry
                , noiseGeneratorSettings.useLegacyRandomSource()
                , worldGenerationContext
                , chunkAccess
                , noiseChunk
                , noiseGeneratorSettings.surfaceRule()
        );
    }

    /** Copied from SurfaceBuilder */
    public void meBuildSurface(RandomState randomState, BiomeManager biomeManager, Registry<Biome> registry, boolean bl, WorldGenerationContext worldGenerationContext, final ChunkAccess chunkAccess, NoiseChunk noiseChunk, SurfaceRules.RuleSource ruleSource) {
        final BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        final ChunkPos chunkPos = chunkAccess.getPos();
        int minX = chunkPos.getMinBlockX();
        int minZ = chunkPos.getMinBlockZ();

        BlockColumn blockColumn = new BlockColumn() {
            public BlockState getBlock(int i) {
                return chunkAccess.getBlockState(blockPos.setY(i));
            }

            public void setBlock(int i, BlockState blockState) {
                LevelHeightAccessor levelHeightAccessor = chunkAccess.getHeightAccessorForGeneration();
                if (levelHeightAccessor.isInsideBuildHeight(i)) {
                    chunkAccess.setBlockState(blockPos.setY(i), blockState);
                    if (!blockState.getFluidState().isEmpty()) {
                        chunkAccess.markPosForPostprocessing(blockPos);
                    }
                }

            }

            public String toString() {
                return "ChunkBlockColumn " + String.valueOf(chunkPos);
            }
        };

        Objects.requireNonNull(biomeManager);
        Context context = new Context(
                new SurfaceSystem(
                          randomState
                        , defaultBlock
                        , SEA_LEVEL
                        , new XoroshiroRandomSource.XoroshiroPositionalRandomFactory(0, 0))
                , randomState
                , chunkAccess
                , noiseChunk
                , biomeManager::getBiome
                , registry
                , worldGenerationContext
        );
        SurfaceRules.SurfaceRule surfaceRule = ruleSource.apply(context);

        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                int localX = minX + x;
                int localZ = minZ + z;
                blockPos.setX(localX).setZ(localZ);

                int aboveSurfaceY = chunkAccess.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) + 1;
                context.updateXZ(localX, localZ);
                int stoneDepthAbove = 0;
                int waterHeight = Integer.MIN_VALUE;
                int aboveStoneDepth = Integer.MAX_VALUE;
                int minY = chunkAccess.getMinY();

                for(int checkingY = aboveSurfaceY; checkingY >= minY; --checkingY) {
                    BlockState currentBlockState = blockColumn.getBlock(checkingY);

                    if (currentBlockState.isAir()) {
                        stoneDepthAbove = 0;
                        waterHeight = Integer.MIN_VALUE;
                    } else if (!currentBlockState.getFluidState().isEmpty()) {
                        if (waterHeight == Integer.MIN_VALUE) {
                            waterHeight = checkingY + 1;
                        }
                    } else {
                        int stoneDepthBelow;
                        BlockState newBlockState;

                        if (aboveStoneDepth >= checkingY) {
                            aboveStoneDepth = DimensionType.WAY_BELOW_MIN_Y;

                            for(stoneDepthBelow = checkingY - 1; stoneDepthBelow >= minY - 1; --stoneDepthBelow) {
                                newBlockState = blockColumn.getBlock(stoneDepthBelow);
                                if (!isStone(newBlockState)) {
                                    aboveStoneDepth = stoneDepthBelow + 1;
                                    break;
                                }
                            }
                        }

                        ++stoneDepthAbove;
                        stoneDepthBelow = checkingY - aboveStoneDepth + 1;
                        context.updateY(stoneDepthAbove, stoneDepthBelow, waterHeight, localX, checkingY, localZ);

                        // actually apply the surface rule
                        if (currentBlockState == defaultBlock) {
                            newBlockState = surfaceRule.tryApply(localX, checkingY, localZ);
                            if (newBlockState != null) {
                                blockColumn.setBlock(checkingY, newBlockState);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isStone(BlockState blockState) {
        return !blockState.isAir() && blockState.getFluidState().isEmpty();
    }
}