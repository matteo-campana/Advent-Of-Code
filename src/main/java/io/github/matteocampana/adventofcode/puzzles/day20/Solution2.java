package io.github.matteocampana.adventofcode.puzzles.day20;

import io.github.matteocampana.adventofcode.utils.GridPaths;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public class Solution2 {

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

        System.out.println("Original Grid:");
        printGrid(racetrackMap, null);

        State start = findPosition(racetrackMap, 'S');
        State end = findPosition(racetrackMap, 'E');

        if (start == null || end == null) {
            System.out.println("Start or End point not found!");
            return;
        }

        // find minimum-cost path
        List<State> minPath = bfs(racetrackMap, start, end);
        assert minPath != null;
        System.out.println("\nMinimum Path (BFS) Cost: " + minPath.get(minPath.size() - 1).cost + " , Path"
                + pathToString(minPath));

        System.out.println("Grid with Minimum Path:");
        printGrid(racetrackMap, minPath);

        long cheatFreeMinCost = minPath.get(minPath.size() - 1).cost;

        // find all paths

        List<List<State>> paths = new ArrayList<>();
        boolean[][] visited = new boolean[racetrackMap.length][racetrackMap[0].length];

        List<State> currentPath = new ArrayList<>();
        dfs(racetrackMap, start, end, visited, currentPath, paths, 0, -1, 0, 1, 1);

        // Output DFS results
        System.out.println("\nPaths and their costs:");
        for (List<State> path : paths) {
            System.out.println("Cost: " + path.getLast().cost + ", Path: " +
                    pathToString(path));
            System.out.println("Grid with Path:");
            printGrid(racetrackMap, path);
        }

        // group all paths by cost and count them

        System.out.println("\nGrouped Paths by Cost:");
        paths.stream()
                .collect(Collectors.groupingBy((List<State> path) -> path.get(path.size() - 1).cost,
                        Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println(
                        "\t- There are " + entry.getValue() + " cheat that saves " + entry.getKey() + " picoseconds."));

    }

    private static class State {
        int y, x;
        long cost;
        int direction;
        int elapsedTime;
        boolean isCheat;

        public State(int y, int x) {
            this.y = y;
            this.x = x;
            this.direction = -1;
        }

        public State(int y, int x, long cost, int direction, int elapsedTime) {
            this.y = y;
            this.x = x;
            this.cost = cost;
            this.direction = direction;
            this.elapsedTime = elapsedTime;
            this.isCheat = false;
        }

        public State(int y, int x, long cost, int direction, int elapsedTime, boolean isCheat) {
            this.y = y;
            this.x = x;
            this.cost = cost;
            this.direction = direction;
            this.elapsedTime = elapsedTime;
            this.isCheat = isCheat;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            State state = (State) o;
            return y == state.y && x == state.x && direction == state.direction;
        }

        @Override
        public String toString() {
            return "(x=" + x + ", y=" + y + ")";
        }
    }

    private static final int[] DX = { 0, 1, 0, -1 }; // Up, Right, Down, Left
    private static final int[] DY = { -1, 0, 1, 0 }; // Up, Right, Down, Left

    private static List<State> bfs(char[][] racetrackMap, State start, State end) {

        Queue<List<State>> openSet = new PriorityQueue<>(
                Comparator.comparing((List<State> path) -> path.get(path.size() - 1).cost));

        boolean[][] visited = new boolean[racetrackMap.length][racetrackMap[0].length];

        List<State> startPath = new ArrayList<>();
        startPath.add(start);
        openSet.add(startPath);
        visited[start.y][start.x] = true;

        while (!openSet.isEmpty()) {
            List<State> currentPath = openSet.poll();
            State currentState = currentPath.get(currentPath.size() - 1);
            long currentCost = currentState.cost;
            int currentElapsedTime = currentState.elapsedTime;

            if (currentState.x == end.x && currentState.y == end.y) {
                return currentPath;
            }

            for (int i = 0; i < 4; i++) {
                int nextX = currentState.x + DX[i];
                int nextY = currentState.y + DY[i];
                long nextCost = currentCost + 1;

                State newState = new State(nextY, nextX, nextCost, i, currentElapsedTime + 1);
                if (nextY >= 0 && nextY < racetrackMap.length && nextX >= 0 && nextX < racetrackMap[nextY].length
                        && !visited[nextY][nextX] && racetrackMap[nextY][nextX] != '#') {
                    visited[nextY][nextX] = true;
                    List<State> nextPath = new ArrayList<>(currentPath);
                    nextPath.add(newState);
                    openSet.add(nextPath);
                }
            }
        }
        return null;
    }

    private static void dfs(char[][] racetrackMap, State start, State end, boolean[][] visited, List<State> currentPath,
            List<List<State>> paths,
            long currentCost, int prevDirection, int prevElapsedTime, int nAllowedCheat, int cheatElapsedTime) {

        if (start.x == end.x && start.y == end.y) {
            currentPath.add(new State(start.y, start.x, currentCost, prevDirection, prevElapsedTime));
            paths.add(new ArrayList<>(currentPath));
            currentPath.remove(currentPath.size() - 1);
            return;
        }

        currentPath.add(new State(start.y, start.x, currentCost, prevDirection, prevElapsedTime));
        visited[start.y][start.x] = true;

        for (int i = 0; i < 4; i++) {
            int nextX = start.x + DX[i];
            int nextY = start.y + DY[i];
            long nextCost = currentCost + 1;
            int nextElapsedTime = prevElapsedTime + 1;

            if (nextY >= 0 && nextY < racetrackMap.length && nextX >= 0 && nextX < racetrackMap[nextY].length
                    && !visited[nextY][nextX]) {
                if (racetrackMap[nextY][nextX] != '#') {
                    dfs(racetrackMap, new State(nextY, nextX, nextCost, i, nextElapsedTime), end, visited,
                            currentPath, paths, nextCost, i, nextElapsedTime, nAllowedCheat, cheatElapsedTime);

                } else {
                    // check the number of active cheats
                    int activeCheats = (int) currentPath
                            .subList(Math.max(currentPath.size() - cheatElapsedTime, 0),
                                    currentPath.size())
                            .stream()
                            .filter(s -> s.isCheat).count();
                    if (activeCheats < nAllowedCheat) {
                        dfs(racetrackMap, new State(nextY, nextX, nextCost, i, nextElapsedTime,
                                true), end, visited,
                                currentPath, paths, nextCost, i, nextElapsedTime, nAllowedCheat,
                                cheatElapsedTime);
                    }
                }
            }
        }

        // Backtrack - unmark visited and remove from path
        visited[start.y][start.x] = false;
        currentPath.remove(currentPath.size() - 1);

    }

    // ------------------------ Utility Methods ------------------------

    private static State findPosition(char[][] grid, char target) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == target) {
                    return new State(i, j);
                }
            }
        }
        return null;
    }

    private static String pathToString(List<State> path) {
        StringBuilder sb = new StringBuilder();
        for (State point : path) {
            sb.append("-> ").append(point).append(" ");
        }
        return sb.toString().trim();
    }

    private static void printGrid(char[][] grid, List<State> path) {
        char[][] tempGrid = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, tempGrid[i], 0, grid[0].length);
        }

        if (path != null) {
            for (State point : path) {
                int x = point.x;
                int y = point.y;
                int direction = point.direction;
                boolean isCheat = point.isCheat;
                if (tempGrid[y][x] == '.') { // Don't overwrite start (S) or end (E)
                    tempGrid[y][x] = switch (direction) {
                        case 0 -> '^'; // Up
                        case 1 -> '>'; // Right
                        case 2 -> 'v'; // Down
                        case 3 -> '<'; // Left
                        default -> '*'; //
                    };
                    if (isCheat) {
                        tempGrid[y][x] = 'O';
                    }
                }
            }
        }

        for (char[] row : tempGrid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

}
