package org.fyrebyrns.narya.mewg;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

import static org.fyrebyrns.narya.mewg.LOTRMap.*;

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

abstract class MERule implements SurfaceRules.SurfaceRule {
    protected SurfaceRules.Context context;
    public MERule(SurfaceRules.Context context) {
        this.context = context;
    }
}

class MEGrassRule extends MERule {
    public MEGrassRule(SurfaceRules.Context context) { super(context); }

    @Override
    public @Nullable BlockState tryApply(int x, int y, int z) {
        if(x % BLOCKS_PER_MAP_CELL == 0 || z % BLOCKS_PER_MAP_CELL == 0)
            return Blocks.EMERALD_BLOCK.defaultBlockState();
        if(x % BLOCKS_PER_MAP_CELL == BLOCKS_PER_MAP_CELL -1 || z % BLOCKS_PER_MAP_CELL == BLOCKS_PER_MAP_CELL -1)
            return Blocks.LAPIS_BLOCK.defaultBlockState();

        if(!context.steep.test())
        if(LOTRMap.getMapHeight(x, z) - 3 <= y) {
            return Blocks.GRASS_BLOCK.defaultBlockState();
        }

        return null;
    }
}