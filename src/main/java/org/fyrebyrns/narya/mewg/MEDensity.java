package org.fyrebyrns.narya.mewg;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

import static org.fyrebyrns.narya.mewg.LOTRMap.*;

public class MEDensity implements DensityFunction {
    public static final MapCodec<MEDensity> CODEC = MapCodec.unit(MEDensity::new);

    @Override
    public double compute(FunctionContext functionContext) {
        int x = functionContext.blockX();
        int y = functionContext.blockY();
        int z = functionContext.blockZ();

        return y < LOTRMap.getMapHeight(x, z) ? 1 : 0;
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
        return visitor.apply(new MEDensity());
    }

    public static final KeyDispatchDataCodec<MEDensity> KDC = KeyDispatchDataCodec.of(CODEC);
    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return KDC;
    }
}
