package com.hardlight.game;

import com.hardlight.auxiliary.Coordinate;
import com.hardlight.auxiliary.Direction;
import com.hardlight.bots.interfaces.Bot;

public class SnakeRunner implements Runner {
    private final Bot bot;
    private final Snake snake;
    private final Snake opponent;
    private final Coordinate mazeSize;
    private Coordinate apple;
    private Direction chosenDirection;

    public SnakeRunner(Bot bot, Snake snake, Snake opponent, Coordinate mazeSize, Coordinate apple) {
        this.bot = bot;
        this.snake = snake;
        this.opponent = opponent;
        this.mazeSize = mazeSize;
        this.apple = apple;
    }

    @Override
    public void run() {
        chosenDirection = bot.chooseDirection(snake, opponent, mazeSize, apple);
    }

    public Coordinate getApple() {
        return apple;
    }

    public void setApple(Coordinate apple) {
        this.apple = apple;
    }

    public String getBotName() {
        return bot.getClass().getSimpleName();
    }

    public Direction getChosenDirection() {
        return chosenDirection;
    }
}
