package io.github.matteocampana.adventofcode.puzzles.day20;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day20/input.txt");
        List<String> input = new ArrayList<>();
        try (InputStream inputStream = Solution.class.getClassLoader().getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    input.add(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        char[][] racetrackMap = input.stream().map(String::toCharArray).toArray(char[][]::new);

        for (char[] c : racetrackMap) {
            System.out.println(Arrays.toString(c).replaceAll("[\\[\\], ]", ""));
        }

        long noCheatCost = minPathCost(racetrackMap);
        System.out.println("[PART 1] Minimum path cost: " + noCheatCost);

        System.out.println("[PART 2] Number of cheats: " + countSavedPicoseconds(racetrackMap, noCheatCost));
    }

    private static class Cheat implements Comparable<Cheat> {
        int y, x;
        long savedPicosenconds;

        public Cheat(int y, int x, long savedPicosenconds) {
            this.y = y;
            this.x = x;
            this.savedPicosenconds = savedPicosenconds;
        }

        @Override
        public int compareTo(@NotNull Cheat o) {
            if (this.savedPicosenconds == o.savedPicosenconds)
                return 0;
            return this.savedPicosenconds > o.savedPicosenconds ? 1 : -1;
        }

        @Override
        public String toString() {
            return "Cheat{" + "y=" + y + ", x=" + x + ", savedPicosenconds=" + savedPicosenconds + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Cheat cheat = (Cheat) o;
            return y == cheat.y && x == cheat.x;
        }
    }

    private static class State implements Comparable<State> {
        int y, x;
        long cost;
        State parent;

        public State(int y, int x, long cost, State parent) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.parent = parent;
        }

        @Override
        public int compareTo(@NotNull State o) {
            if (this.cost == o.cost)
                return 0;
            return this.cost > o.cost ? 1 : -1;
        }
    }

    private static final int[] DX = new int[] { -1, 0, 0, 1 }; // UP, DOWN, RIGHT, LEFT
    private static final int[] DY = new int[] { 0, 1, -1, 0 }; // UP, DOWN, RIGHT, LEFT

    private static long countSavedPicoseconds(char[][] racetrackMap, long noCheatCost) {
        List<Cheat> possibleCheats = new ArrayList<>();

        for (int row = 0; row < racetrackMap.length; row++) {
            for (int col = 0; col < racetrackMap[row].length; col++) {
                boolean isValidCheat = false;
                if (racetrackMap[row][col] == '#') {
                    for (int i = 0; i < 4; i++) {
                        int newX = col + DX[i];
                        int newY = row + DY[i];
                        if (isInBound(racetrackMap, newY, newX) && racetrackMap[newY][newX] != '#') {
                            isValidCheat = true;
                        }
                    }
                }
                if (isValidCheat) {
                    racetrackMap[row][col] = '.';
                    long savedPicoseconds = noCheatCost - minPathCost(racetrackMap);
                    racetrackMap[row][col] = '#';
                    if (savedPicoseconds > 0)
                        possibleCheats.add(new Cheat(row, col, savedPicoseconds));
                }
            }
        }

        if (possibleCheats.isEmpty())
            return 0L;

        // There is one cheat that saves 40 picoseconds.
        // There is one cheat that saves 64 picoseconds.

        // group by the same saved picoseconds and print the number of cheats and the
        // picoseconds saved
        possibleCheats.stream()
                .collect(Collectors.groupingBy(cheat -> cheat.savedPicosenconds, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println(
                        "\t- There is " + entry.getValue() + " cheat that saves " + entry.getKey() + " picoseconds."));

        possibleCheats.sort(Collections.reverseOrder());
        List<Cheat> validCheats = possibleCheats.stream().filter(cheat -> cheat.savedPicosenconds >= 100).toList();
        return validCheats.size();
    }

    private static int[] find(char[][] racetrackMap, char identifier) {
        for (int row = 0; row < racetrackMap.length; row++) {
            for (int col = 0; col < racetrackMap[row].length; col++) {
                if (racetrackMap[row][col] == identifier)
                    return new int[] { row, col };
            }
        }
        return new int[] { -1, -1 };
    }

    private static long minPathCost(char[][] racetrackMap) {
        if (racetrackMap == null)
            return 0L;

        Map<String, Long> gCost = new HashMap<>();

        int[] startPosition = find(racetrackMap, 'S');
        int[] endPosition = find(racetrackMap, 'E');

        Queue<State> openSet = new PriorityQueue<>();
        openSet.add(new State(startPosition[0], startPosition[1], 1, null));

        while (!openSet.isEmpty()) {
            State currentState = openSet.poll();

            if (currentState.y == endPosition[0] && currentState.x == endPosition[1])
                return gCost.get(currentState.y + "," + currentState.x);

            for (int i = 0; i < 4; i++) {
                int newX = currentState.x + DX[i];
                int newY = currentState.y + DY[i];

                if (isInBound(racetrackMap, newY, newX) && racetrackMap[newY][newX] != '#') {
                    long cost = currentState.cost + 1;
                    if (cost < gCost.getOrDefault(newY + "," + newX, Long.MAX_VALUE)) {
                        gCost.put(newY + "," + newX, cost);
                        State newState = new State(newY, newX, cost, currentState);
                        openSet.add(newState);
                    }
                }
            }
        }
        return -1L;
    }

    private static boolean isInBound(char[][] racetrackMap, int row, int col) {
        return row > 0 && racetrackMap.length > row && col > 0 && racetrackMap[row].length > col;
    }
}
