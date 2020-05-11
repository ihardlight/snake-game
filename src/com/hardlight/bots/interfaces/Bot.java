package com.hardlight.bots.interfaces;

import com.hardlight.auxiliary.Coordinate;
import com.hardlight.auxiliary.Direction;
import com.hardlight.game.Snake;

public interface Bot {
    Direction chooseDirection(final Snake snake, final Snake opponent, final Coordinate mazeSize, final Coordinate apple);
}
