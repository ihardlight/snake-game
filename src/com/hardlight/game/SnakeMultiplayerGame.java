package com.hardlight.game;

import com.hardlight.auxiliary.Coordinate;
import com.hardlight.auxiliary.Direction;
import com.hardlight.bots.interfaces.MultiplayerBot;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SnakeMultiplayerGame extends SnakeGame {
    protected HashSet<Coordinate> apples;
    protected SnakeMultiplayerRunner[] runners;


    public SnakeMultiplayerGame(SnakeBoard board,
                                Coordinate mazeSize,
                                MultiplayerBot[] bots,
                                Pair<Coordinate, Direction>[] initSnakePairs,
                                int initSize,
                                int applesNum) {
        super(board, mazeSize, bots, initSnakePairs, initSize);
        this.apples = initApples(applesNum);
        this.runners = initSnakeRunners(bots);
    }

    // TODO: add points to snake if it eat apple
    // TODO: check collisions
    // TODO: filtering dead snakes?
    public boolean runOneStep() {
        Stream<SnakeMultiplayerRunner> snakeRunnerStream = Arrays.stream(runners);

        snakeRunnerStream.parallel()
                .filter(SnakeMultiplayerRunner::isAlive)
                .forEach(runner -> runner.setApples(apples));

        Stream<Boolean> overtimesResults = getOvertimeResults(snakeRunnerStream);
        Direction[] chosenDirections = getChosenDirections(snakeRunnerStream);

        boolean hasOvertime = overtimesResults.anyMatch(overtime -> overtime);

        if (hasOvertime) {
            Boolean[] overtimeArray = overtimesResults.toArray(Boolean[]::new);
            IntStream.range(0, runners.length)
                    .filter(i -> overtimeArray[i])
                    .forEach(i -> runners[i].killRunner());
        }

        Stream<Boolean> growResult = getGrowResult(chosenDirections);

        boolean wasGrow = growResult.anyMatch(grow -> grow);

        if (wasGrow) {
            int applesSize = apples.size();
            apples = apples.stream()
                    .filter(apple -> IntStream.range(0, snakes.length)
                            .mapToObj(i -> snakes[i]
                                    .getHead()
                                    .moveInDirection(chosenDirections[i])
                                    .equals(apple)
                            ).noneMatch(x -> x))
                    .collect(Collectors.toCollection(HashSet::new));
            while (apples.size() < applesSize)
                apples.add(getRandomCoordinate());
        }

        Boolean[] growArray = growResult.toArray(Boolean[]::new);

//        return IntStream
//                .range(0, snakes.length)
//                .mapToObj(ind -> {
//                    Snake[] opponents = snakeRunnerStream
//                            .filter(SnakeMultiplayerRunner::isAlive)
//                            .map(SnakeMultiplayerRunner::getOpponents);
//                    boolean notDeadDueMove = snakes[firstInd].moveTo(chosenDirections[firstInd], growArray[firstInd]);
//                    boolean headCollides = snakes[firstInd].headCollidesWith(snakes[secondInd]);
//                    return notDeadDueMove && !headCollides;
//                })
//                .reduce(true, (accum, current) -> accum && current);
        return false;
    }

    private HashSet<Coordinate> initApples(int size) {
        HashSet<Coordinate> apples = new HashSet<>();
        while (apples.size() < size) {
            apples.add(getRandomCoordinate());
        }
        return apples;
    }

    protected SnakeMultiplayerRunner[] initSnakeRunners(MultiplayerBot[] bots) {
        SnakeMultiplayerRunner[] runners = new SnakeMultiplayerRunner[bots.length];
        Arrays.setAll(runners, i -> {
            Snake[] opponentSnakes = Arrays.stream(snakes)
                    .filter(snake -> snake != snakes[i])
                    .toArray(Snake[]::new);
            return new SnakeMultiplayerRunner(bots[i], snakes[i], opponentSnakes, mazeSize, apples);
        });
        return runners;
    }
}
