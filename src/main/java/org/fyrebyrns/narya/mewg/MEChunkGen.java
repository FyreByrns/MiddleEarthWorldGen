package org.fyrebyrns.narya.mewg;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.*;

import java.util.*;

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

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunk) {
        super.buildSurface(region, structureManager, randomState, chunk);
    }
}