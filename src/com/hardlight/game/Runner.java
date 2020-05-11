package com.hardlight.game;

import com.hardlight.auxiliary.Direction;

public interface Runner extends Runnable {
    Direction getChosenDirection();
}
