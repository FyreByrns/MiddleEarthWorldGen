package org.fyrebyrns.narya.mewg.generation;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.fyrebyrns.narya.mewg.DefaultLOTRBiomes;
import org.fyrebyrns.narya.mewg.LOTRMap;

import java.util.HashMap;

import static org.fyrebyrns.narya.mewg.DefaultLOTRBiomes.*;
import static org.fyrebyrns.narya.mewg.LOTRMap.*;

public class Elevation {
    public static HashMap<ResourceKey<Biome>, Integer> RegisteredHeights = new HashMap<>();

    static {
        register(SEA, -SEA_LEVEL);
        register(RIVER, -SEA_LEVEL / 3);
        register(LAKE, -SEA_LEVEL / 2);

        register(LINDON, SEA_LEVEL + 3);

        for(ResourceKey<Biome> biome : allWithPredicate("hills")) {
            register(biome, BASE_HILLS_LEVEL);
        }
        for(ResourceKey<Biome> biome : allWithPredicate("foothills")) {
            register(biome, BASE_FOOTHILLS_LEVEL);
        }
        for(ResourceKey<Biome> biome : allWithPredicate("mountain")) {
            register(biome, BASE_MOUNTAINS_LEVEL);
        }
    }

    public static int getElevation(ResourceKey<Biome> biome) {
        if (RegisteredHeights.containsKey(biome)) {
            return RegisteredHeights.get(biome);
        }

        return SEA_LEVEL;
    }

    private static void register(ResourceKey<Biome> biome, int height) {
        RegisteredHeights.put(biome, height);
    }
}
