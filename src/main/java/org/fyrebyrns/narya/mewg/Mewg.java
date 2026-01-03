package org.fyrebyrns.narya.mewg;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mewg implements ModInitializer {
    public static final String MOD_ID = "narya";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Registry.register(
                BuiltInRegistries.CHUNK_GENERATOR,
                Identifier.fromNamespaceAndPath(MOD_ID, "worldgen"),
                MEChunkGen.CODEC
        );
        Registry.register(
                BuiltInRegistries.BIOME_SOURCE,
                Identifier.fromNamespaceAndPath(MOD_ID, "biomegen"),
                MEBiomeGen.CODEC
        );
        Registry.register(
                BuiltInRegistries.DENSITY_FUNCTION_TYPE,
                Identifier.fromNamespaceAndPath(MOD_ID, "medensity"),
                MEDensity.CODEC
        );
        Registry.register(
                BuiltInRegistries.DENSITY_FUNCTION_TYPE,
                Identifier.fromNamespaceAndPath(MOD_ID, "meprelimdensity"),
                MEPrelimDensity.CODEC
        );

//        Registry.register(
//                BuiltInRegistries.CONDITION
//        );


        DefaultLOTRBiomes.init();
    }
}