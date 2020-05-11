package com.hardlight.bots.interfaces;

import com.hardlight.auxiliary.Coordinate;
import com.hardlight.auxiliary.Direction;
import com.hardlight.game.Snake;

import java.util.HashSet;

public interface MultiplayerBot extends Bot {
    Direction chooseDirection(final Snake snake, final Snake[] opponents, final Coordinate mazeSize, final HashSet<Coordinate> apples);
}
