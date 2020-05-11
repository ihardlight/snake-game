package com.hardlight.game;

import com.hardlight.auxiliary.Coordinate;
import com.hardlight.auxiliary.Direction;
import com.hardlight.bots.interfaces.Bot;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SnakeGame {
    public static final int TIMEOUT = 1000;
    public static final Random random = new Random();
    public final SnakeBoard board;
    public final Coordinate mazeSize;
    protected final Snake[] snakes;
    protected final SnakeRunner[] runners;
    protected Coordinate apple;

    public SnakeGame(SnakeBoard board,
                     Coordinate mazeSize,
                     Bot[] bots,
                     Pair<Coordinate, Direction>[] initSnakePairs,
                     int initSize) {
        this.mazeSize = mazeSize;
        this.board = board;
        this.snakes = initSnakes(initSnakePairs, initSize);
        this.apple = getRandomCoordinate();
        this.runners = initSnakeRunners(bots);
    }

    public boolean runOneStep() {
        Stream<SnakeRunner> runnerStream = Arrays.stream(runners);

        runnerStream.parallel().forEach(runner -> runner.setApple(apple));

        Stream<Boolean> overtimesResults = getOvertimeResults(runnerStream);
        Direction[] chosenDirections = getChosenDirections(runnerStream);

        boolean hasOvertime = overtimesResults.anyMatch(overtime -> overtime);

        if (hasOvertime) {
            Boolean[] overtimeArray = overtimesResults.toArray(Boolean[]::new);
            for (int firstInd = 0; firstInd < overtimeArray.length; firstInd++) {
                int secondInd = Math.abs(overtimeArray.length - firstInd - 1);
                if (!overtimeArray[firstInd]) {
                    board.addWinnerPoint(runners[firstInd].getBotName(), runners[secondInd].getBotName());
                }
            }
            return false;
        }

        Stream<Boolean> growResult = getGrowResult(chosenDirections);

        boolean wasGrow = growResult.anyMatch(grow -> grow);

        if (wasGrow)
            apple = getRandomCoordinate();

        Boolean[] growArray = growResult.toArray(Boolean[]::new);

        return IntStream
                .range(0, snakes.length)
                .mapToObj(firstInd -> {
                    int secondInd = Math.abs(snakes.length - firstInd - 1);
                    boolean notDeadDueMove = snakes[firstInd].moveTo(chosenDirections[firstInd], growArray[firstInd]);
                    boolean headCollides = snakes[firstInd].headCollidesWith(snakes[secondInd]);
                    return notDeadDueMove && !headCollides;
                })
                .reduce(true, (accum, current) -> accum && current);
    }

    protected <T extends Runnable> Stream<Boolean> getOvertimeResults(Stream<T> stream) {
        return stream
                .map(Thread::new)
                .peek(Thread::start)
                .peek(thread -> {
                    try {
                        thread.join(TIMEOUT);
                    } catch (InterruptedException ignore) {
                    }
                })
                .sequential()
                .map(thread -> {
                    boolean isOvertime = thread.isAlive();
                    if (isOvertime)
                        thread.interrupt();
                    return isOvertime;
                });
    }

    protected <T extends Runner> Direction[] getChosenDirections(Stream<T> stream) {
        return stream
                .map(T::getChosenDirection)
                .toArray(Direction[]::new);
    }

    protected Stream<Boolean> getGrowResult(Direction[] chosenDirections) {
        return IntStream
                .range(0, snakes.length)
                .mapToObj(i -> snakes[i]
                        .getHead()
                        .moveInDirection(chosenDirections[i])
                        .equals(apple));
    }

    protected Snake[] initSnakes(Pair<Coordinate, Direction>[] initSnakePairs, int initSize) {
        Snake[] snakes = new Snake[initSnakePairs.length];
        Arrays.setAll(snakes, i -> new Snake(initSnakePairs[i].getKey(),
                initSnakePairs[i].getValue(), initSize, mazeSize));
        return snakes;
    }

    protected SnakeRunner[] initSnakeRunners(Bot[] bots) {
        return new SnakeRunner[]{
                new SnakeRunner(bots[0], snakes[0], snakes[1], mazeSize, apple),
                new SnakeRunner(bots[1], snakes[1], snakes[0], mazeSize, apple),
        };
    }

    protected Coordinate getRandomCoordinate() {
        Coordinate coordinate;
        boolean isFree;
        do {
            Coordinate finalCoordinate = new Coordinate(random.nextInt(mazeSize.x), random.nextInt(mazeSize.y));
            coordinate = finalCoordinate;
            isFree = Arrays.stream(this.snakes).noneMatch(snake -> snake.elements.contains(finalCoordinate));
        } while (isFree);

        return coordinate;
    }
}
