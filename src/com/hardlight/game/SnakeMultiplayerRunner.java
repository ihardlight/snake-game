package com.hardlight.game;

import com.hardlight.auxiliary.Coordinate;
import com.hardlight.auxiliary.Direction;
import com.hardlight.bots.interfaces.MultiplayerBot;

import java.util.HashSet;

public class SnakeMultiplayerRunner implements Runner {
    private final MultiplayerBot bot;
    private final Snake snake;
    private final Snake[] opponents;
    private final Coordinate mazeSize;
    private HashSet<Coordinate> apples;
    private Direction chosenDirection;
    private boolean isLive;

    public SnakeMultiplayerRunner(MultiplayerBot bot, Snake snake, Snake[] opponents, Coordinate mazeSize, HashSet<Coordinate> apples) {
        this.bot = bot;
        this.snake = snake;
        this.opponents = opponents;
        this.mazeSize = mazeSize;
        this.apples = apples;
        this.isLive = true;
    }

    @Override
    public void run() {
        chosenDirection = bot.chooseDirection(snake, opponents, mazeSize, apples);
    }

    public HashSet<Coordinate> getApples() {
        return apples;
    }

    public void setApples(HashSet<Coordinate> apple) {
        this.apples = apple;
    }

    public String getBotName() {
        return bot.getClass().getSimpleName();
    }

    public Snake[] getOpponents() {
        return opponents;
    }

    public Direction getChosenDirection() {
        return chosenDirection;
    }

    public boolean isAlive() {
        return isLive;
    }

    public void killRunner() {
        isLive = false;
    }
}
