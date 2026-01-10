package org.fyrebyrns.narya.mewg;

public class MathHelper {
    public static final double SQRT2 = sqrt(2.0);

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
        if(start > end) {
            double temp = start;
            start = end;
            end = temp;
        }

        if(value < start) return start;
        if(value > end) return end;
        return value;
    }

    public static double map(double startA, double endA, double startB, double endB, double amount){
        return ((amount - startA) / (endA - startA)) * (endB - startB) + startB;
    }

    public static double cmap(double startA, double endA, double startB, double endB, double amount) {
        return clamp(startB, endB, map(startA, endA, startB, endB, amount));
    }

    public static double dot(double aX, double aY, double bX, double bY) {
        return aX * bX + aY * bY;
    }
}
