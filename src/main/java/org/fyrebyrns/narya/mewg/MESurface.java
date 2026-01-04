package org.fyrebyrns.narya.mewg;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;

import java.util.function.Function;

public class MESurface implements Function<SurfaceRules.Context, SurfaceRules.Condition> {
    public static final MapCodec<MESurface> CODEC = MapCodec.unit(MESurface::new);

    @Override
    public SurfaceRules.Condition apply(SurfaceRules.Context context) {
        return null;
    }
}
