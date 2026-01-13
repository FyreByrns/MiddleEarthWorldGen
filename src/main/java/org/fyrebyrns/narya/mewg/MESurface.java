package org.fyrebyrns.narya.mewg;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.material.FlowingFluid;
import org.jspecify.annotations.Nullable;

import static org.fyrebyrns.narya.mewg.LOTRMap.*;

public class MESurface implements SurfaceRules.RuleSource {
    public static final MapCodec<? extends SurfaceRules.RuleSource> CODEC = MapCodec.unit(MESurface::new);

    @Override
    public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
        return KeyDispatchDataCodec.of(CODEC);
    }

    @Override
    public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
        int x = context.blockX;
        int y = context.blockY;
        int z = context.blockZ;

        return MERule.buildChain(context)
                .thenTry(new MEGridLinesRule(context))
                .thenTry(new MEWaterRule(context))
                .thenTry(new MEGrassRule(context))
                .top();

//        return new MEGrassRule(context);
    }
}

abstract class MERule implements SurfaceRules.SurfaceRule {
    protected SurfaceRules.Context context;
    protected MERule previous, next;

    public MERule(SurfaceRules.Context context) {
        this.context = context;
    }

    /** Seek to the top of the rule chain */
    public MERule top() {
        MERule top = this;

        while(top.previous != null) {
            top = top.previous;
        }
        return top;
    }

    public MERule thenTry(MERule next) {
        next.previous = this;
        this.next = next;
        return next;
    }

    protected @Nullable BlockState chain(int x, int y, int z) {
        if(next != null) {
            return next.tryApply(x, y, z);
        }
        return null;
    }

    public static MERule buildChain(SurfaceRules.Context context) {
        return new MEBaseRule(context);
    }
}

class MEBaseRule extends MERule {
    public MEBaseRule(SurfaceRules.Context context) { super(context); }

    @Override
    public @Nullable BlockState tryApply(int x, int y, int z) {
        return chain(x, y, z);
    }
}

class MEGridLinesRule extends MERule {

    public MEGridLinesRule(SurfaceRules.Context context) { super(context); }

    @Override
    public @Nullable BlockState tryApply(int x, int y, int z) {
        if(x % BLOCKS_PER_MAP_CELL == 0 || z % BLOCKS_PER_MAP_CELL == 0)
            return Blocks.EMERALD_BLOCK.defaultBlockState();
        if(x % BLOCKS_PER_MAP_CELL == BLOCKS_PER_MAP_CELL -1 || z % BLOCKS_PER_MAP_CELL == BLOCKS_PER_MAP_CELL -1)
            return Blocks.LAPIS_BLOCK.defaultBlockState();

        return chain(x, y, z);
    }
}

class MEWaterRule extends MERule {
    public MEWaterRule(SurfaceRules.Context context) { super(context); }

    @Override
    public @Nullable BlockState tryApply(int x, int y, int z) {
//        int con = coordinateOffsetNoise(x, z);
//        int xx = x + con;
//        int zz = z + con;

        if(context.chunk.getBlockState(new BlockPos(x, y, z)) == Blocks.BLUE_STAINED_GLASS.defaultBlockState()) {
            return Blocks.WATER.defaultBlockState()
                    .setValue(FlowingFluid.LEVEL, 0);
        }

        return chain(x, y, z);
    }
}

class MEGrassRule extends MERule {
    public MEGrassRule(SurfaceRules.Context context) { super(context); }

    @Override
    public @Nullable BlockState tryApply(int x, int y, int z) {
        if(!context.steep.test()) {
            if (LOTRMap.getMapHeight(x, z) - 3 <= y) {
                return Blocks.GRASS_BLOCK.defaultBlockState();
            }
        }

        return chain(x, y, z);
    }
}