package org.fyrebyrns.narya.mewg;

public record MapPosition(int x, int z) {
    public MapPosition modify(int modX, int modZ) {
        return new MapPosition(this.x() + modX, this.z() + modZ);
    }

    public MapPosition north() { return modify(0, -1); }
    public MapPosition south() { return modify(0, 1); }
    public MapPosition east() { return modify(1, 0); }
    public MapPosition west() { return modify(-1, 0); }
}

