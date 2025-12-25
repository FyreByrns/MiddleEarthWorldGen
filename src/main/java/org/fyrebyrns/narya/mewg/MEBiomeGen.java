package org.fyrebyrns.narya.mewg;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.*;

import java.util.ArrayList;
import java.util.stream.Stream;

public class MEBiomeGen extends BiomeSource {
    public MEBiomeGen(HolderGetter<Biome> holderGetter){
        getter = holderGetter;
    }

    public static final MapCodec<MEBiomeGen> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(RegistryOps.retrieveGetter(Registries.BIOME)).apply(instance, MEBiomeGen::new));

    private final HolderGetter<Biome> getter;

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        ArrayList<Holder<Biome>> possibleBiomes = new ArrayList<>();

        for(int i : DefaultLOTRBiomes.BiomesByID.keySet()) {
            possibleBiomes.add(getter.get(DefaultLOTRBiomes.BiomesByID.get(i)).orElseThrow());
        }

        return possibleBiomes.stream();
    }

    @Override
    public Holder<Biome> getNoiseBiome(int i, int j, int k, Climate.Sampler sampler) {
        int x = QuartPos.toBlock(i);
        int y = QuartPos.toBlock(j);
        int z = QuartPos.toBlock(k);

        ResourceKey<Biome> biome = LOTRMap.getBiome(LOTRMap.getMapPos(x, z));
        return getter.get(biome).orElseThrow();
    }
}
