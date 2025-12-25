package org.fyrebyrns.narya.mewg;

public class MathHelper {
    public static double lerp(double start, double end, double amount) {
        return (start * (1.0 - amount)) + (end * amount);
    }
}
