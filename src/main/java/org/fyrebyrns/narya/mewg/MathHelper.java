package org.fyrebyrns.narya.mewg;

public class MathHelper {
    public static double lerp(double start, double end, double amount) {
        return (start * (1.0 - amount)) + (end * amount);
    }

    public static double sqrt(double num) {
        return Math.sqrt(num);
    }
    public static double square(double num) {
        return num * num;
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return sqrt(square(x2 - x1) + square(y2 - y1));
    }
}
