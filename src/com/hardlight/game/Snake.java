package com.hardlight.game;

import com.hardlight.auxiliary.Coordinate;
import com.hardlight.auxiliary.Direction;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;

public class Snake implements Cloneable {
    public final HashSet<Coordinate> elements;
    public final Deque<Coordinate> body;

    public final Coordinate mazeSize;

    private Snake(Coordinate mazeSize, HashSet<Coordinate> elements, Deque<Coordinate> body) {
        this.mazeSize = mazeSize;
        this.elements = elements;
        this.body = body;
    }

    public Snake(Coordinate initialHead, Coordinate mazeSize) {
        this(mazeSize, new HashSet<>(), new LinkedList<>());

        body.addFirst(initialHead);
        elements.add(initialHead);
    }

    public Snake(Coordinate head, Direction tailDirection, int size, Coordinate mazeSize) {
        this(head, mazeSize);

        Coordinate p = head.moveInDirection(tailDirection);
        for (int i = 0; i < size - 1; i++) {
            body.addLast(p);
            elements.add(p);
            p = p.moveInDirection(tailDirection);
        }
    }

    public Coordinate getHead() {
        return body.getFirst();
    }

    public boolean moveTo(Direction d, boolean grow) {
        Coordinate newHead = getHead().moveInDirection(d);

        if (!grow) {
            elements.remove(body.removeLast());
        }

        boolean leftMaze = !newHead.inBounds(mazeSize);
        boolean faceItself = elements.contains(newHead);
        if (leftMaze || faceItself)
            return false;

        body.addFirst(newHead);
        elements.add(newHead);

        return true;
    }

    public boolean headCollidesWith(Snake other) {
        return other.elements.contains(getHead());
    }
}