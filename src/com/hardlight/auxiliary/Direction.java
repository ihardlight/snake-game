package com.hardlight.auxiliary;

public enum Direction {
    UP(0, 1),
    DOWN(0, -1),
    RIGHT(1, 0),
    LEFT(-1, 0);

    public final int dx, dy;
    public final Coordinate v;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
        this.v = new Coordinate(dx, dy);
    }
}
