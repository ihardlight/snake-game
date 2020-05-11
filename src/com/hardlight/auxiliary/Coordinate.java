package com.hardlight.auxiliary;

import java.util.Objects;

public class Coordinate implements Comparable<Coordinate> {
    public final int x, y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinate add(Coordinate a, Coordinate b) {
        return new Coordinate(a.x + b.x, a.y + b.y);
    }

    public Coordinate moveInDirection(Direction d) {
        return add(this, d.v);
    }

    public Direction getDirection(Coordinate other) {
        final Coordinate vector = new Coordinate(other.x - this.x, other.y - this.y);
        for (Direction direction : Direction.values())
            if (direction.dx == vector.x && direction.dy == vector.y)
                return direction;
        return null;
    }

    public boolean inBounds(Coordinate mazeSize) {
        return x >= 0 && y >= 0 && x < mazeSize.x && y < mazeSize.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate coordinate = (Coordinate) o;
        return x == coordinate.x &&
                y == coordinate.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Coordinate o) {
        int dx = Integer.compare(x, o.x);
        int dy = Integer.compare(y, o.y);
        return dx == 0 ? dy : dx;
    }
}
