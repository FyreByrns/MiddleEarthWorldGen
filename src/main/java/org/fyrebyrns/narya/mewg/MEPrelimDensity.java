package org.fyrebyrns.narya.mewg;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public class MEPrelimDensity implements DensityFunction {
    public static final MapCodec<MEPrelimDensity> CODEC = MapCodec.unit(MEPrelimDensity::new);
    public static final MEPrelimDensity function = new MEPrelimDensity();

    @Override
    public double compute(FunctionContext functionContext) {
        int x = functionContext.blockX();
        int y = functionContext.blockY();
        int z = functionContext.blockZ();

        return y < LOTRMap.getMapHeight(x, z) ? 1 - 0.090625 : 0;
    }

    @Override
    public double minValue() {
        return 1;
    }
    @Override
    public double maxValue() {
        return 1;
    }

    @Override
    public void fillArray(double[] ds, ContextProvider contextProvider) {
        contextProvider.fillAllDirectly(ds, this);
    }
    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(function);
    }

    public static final KeyDispatchDataCodec<MEPrelimDensity> KDC = KeyDispatchDataCodec.of(CODEC);
    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return KDC;
    }
}
