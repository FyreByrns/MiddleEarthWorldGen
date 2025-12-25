package org.fyrebyrns.narya.mewg;

public record MapPosition(int x, int z) {
    public MapPosition(int x, int z) {
        this.x = Math.max(0, Math.min(x, LOTRMap.MAP_WIDTH-1));
        this.z = Math.max(0, Math.min(z, LOTRMap.MAP_HEIGHT-1));
    }

    public MapPosition modify(int modX, int modZ) {
        return new MapPosition(this.x() + modX, this.z() + modZ);
    }

    public MapPosition north() { return modify(0, -1); }
    public MapPosition south() { return modify(0, 1); }
    public MapPosition east() { return modify(1, 0); }
    public MapPosition west() { return modify(-1, 0); }
}

