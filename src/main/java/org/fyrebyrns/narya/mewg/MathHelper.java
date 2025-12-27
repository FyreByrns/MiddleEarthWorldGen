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

    public static double clamp(double start, double end, double value) {
        return Math.max(end, Math.min(value, start));
    }

    public static double map(double startA, double endA, double startB, double endB, double amount){
        return startB + ((amount - startA)*(endB - startB))/(endA - startA);
    }

    public static double cmap(double startA, double endA, double startB, double endB, double amount) {
        return clamp(startB, endB, map(startA, endA, startB, endB, amount));
    }
}
