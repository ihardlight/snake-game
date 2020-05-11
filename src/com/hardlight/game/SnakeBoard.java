package com.hardlight.game;

import com.hardlight.bots.interfaces.Bot;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SnakeBoard {
    private static SnakeBoard snakeBoardInstance = null;
    private final String[] botNames;
    private final int[][] gameTable;

    private SnakeBoard(Bot[] bots) {
        int playersNum = bots.length;
        this.botNames = initBotNames(bots);
        this.gameTable = initGameTable(playersNum);
    }

    public static SnakeBoard getSnakeBoard() {
        return snakeBoardInstance;
    }

    public static SnakeBoard getSnakeBoard(Bot[] bots) {
        if (snakeBoardInstance == null) {
            snakeBoardInstance = new SnakeBoard(bots);
        }
        return getSnakeBoard();
    }

    private String[] initBotNames(Bot[] bots) {
        String[] botNames = new String[bots.length];
        Arrays.setAll(botNames, i -> bots[i].getClass().getSimpleName());
        return botNames;
    }

    private int[][] initGameTable(int size) {
        int[][] gameTable = new int[size][size];
        IntStream.range(0, gameTable.length).forEach(i -> Arrays.fill(gameTable[i], 0));
        return gameTable;
    }

    public void reset() {
        snakeBoardInstance = null;
    }

    public void addWinnerPoint(String winnerName, String looserName) {
        int winnerIndex = getIndex(winnerName, botNames);
        int looserIndex = getIndex(looserName, botNames);

        gameTable[winnerIndex][looserIndex]++;
    }

    public Pair<Integer, Integer> getBattleResult(String name1, String name2) {
        int nameIndex1 = getIndex(name1, botNames);
        int nameIndex2 = getIndex(name2, botNames);

        int result1 = gameTable[nameIndex1][nameIndex2];
        int result2 = gameTable[nameIndex2][nameIndex1];
        return new Pair<>(result1, result2);
    }

    public int getTotalWins(String botName) {
        int botNameIndex = getIndex(botName, botNames);

        return Arrays.stream(gameTable[botNameIndex]).sum();
    }

    private <T> int getIndex(T element, T[] array) {
        if (array == null)
            return -1;

        return IntStream.range(0, array.length).filter(i -> array[i] == element).findFirst().orElse(-1);
    }

}
