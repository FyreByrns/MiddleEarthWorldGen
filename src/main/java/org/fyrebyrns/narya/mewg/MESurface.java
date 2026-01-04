package org.fyrebyrns.narya.mewg;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public class MESurface implements SurfaceRules.RuleSource {
    public static final MapCodec<? extends SurfaceRules.RuleSource> CODEC = MapCodec.unit(MESurface::new);

    @Override
    public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
        return KeyDispatchDataCodec.of(CODEC);
    }

    @Override
    public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
        return new MEGrassRule(context);
    }
}

class MEGrassRule implements SurfaceRules.SurfaceRule {
    protected SurfaceRules.Context context;

    public MEGrassRule(SurfaceRules.Context context) {
        this.context = context;
    }

    @Override
    public @Nullable BlockState tryApply(int x, int y, int z) {
        if(LOTRMap.getMapHeight(x, z) - 3 <= y) {
            return Blocks.GRASS_BLOCK.defaultBlockState();
        }

        return null;
    }
}