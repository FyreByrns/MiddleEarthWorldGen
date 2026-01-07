package org.fyrebyrns.narya.mewg;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

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
        Registry.register(
                BuiltInRegistries.MATERIAL_RULE,
                Identifier.fromNamespaceAndPath(MOD_ID, "mesurface"),
                MESurface.CODEC
        );

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("metp")
                    .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                    .then(argument("location", BlockPosArgument.blockPos())
                            .executes(ctx -> {
                                BlockPos pos = BlockPosArgument.getBlockPos(ctx, "location");
                                ctx.getSource().getPlayerOrException().teleportTo(
                                        pos.getX() * LOTRMap.BLOCKS_PER_MAP_CELL,
                                        pos.getY(),
                                        pos.getZ() * LOTRMap.BLOCKS_PER_MAP_CELL);
                                return 1;
                            })
                    )
            );
        });

        DefaultLOTRBiomes.init();
    }
}