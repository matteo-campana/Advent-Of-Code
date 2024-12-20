package io.github.matteocampana.adventofcode.utils;

import java.util.*;

public class GridPaths {
    private static final int[] DX = { 0, 1, 0, -1 }; // Directions for row movement
    private static final int[] DY = { 1, 0, -1, 0 }; // Directions for column movement
    private static final int[] COST = { 1, 1, 1, 1 }; // Costs for moving in each direction
    private static final int TURN_COST = 1; // Cost for turning left or right

    public static void main(String[] args) {
        char[][] grid = {
                { 'S', '.', '.', '#', '.', '.' },
                { '.', '#', '.', '#', '.', '.' },
                { '.', '#', '.', '.', '.', 'E' },
                { '.', '.', '#', '#', '.', '.' },
        };

        System.out.println("Original Grid:");
        printGrid(grid, null);

        List<List<State>> paths = new ArrayList<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        State start = findPosition(grid, 'S');
        State end = findPosition(grid, 'E');

        if (start == null || end == null) {
            System.out.println("Start or End point not found!");
            return;
        }

        // DFS to find all paths
        List<State> currentPath = new ArrayList<>();
        dfs(grid, start, end, visited, currentPath, paths, 0, -1);

        // Output DFS results
        System.out.println("\nPaths and their costs:");
        for (List<State> path : paths) {
            System.out.println("Path: " + pathToString(path) + ", Cost: " + path.get(path.size() - 1).cost);
            System.out.println("Grid with Path:");
            printGrid(grid, path);
        }

        // BFS to find the minimum-cost path
        List<State> minPath = bfs(grid, start, end);
        System.out.println(
                "\nMinimum Path (BFS): " + pathToString(minPath) + ", Cost: " + minPath.get(minPath.size() - 1).cost);
        System.out.println("Grid with Minimum Path:");
        printGrid(grid, minPath);
    }

    private static class State {
        int x, y;
        long cost;
        int direction;

        public State(int x, int y) {
            this(x, y, 0, -1);
        }

        public State(int x, int y, long cost, int direction) {
            this.x = x;
            this.y = y;
            this.cost = cost;
            this.direction = direction;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    private static void dfs(char[][] grid, State start, State end,
            boolean[][] visited, List<State> currentPath,
            List<List<State>> paths, long currentCost, int prevDirection) {
        if (start.x == end.x && start.y == end.y) {
            // Found a path
            currentPath.add(new State(start.x, start.y, currentCost, prevDirection));
            paths.add(new ArrayList<>(currentPath));
            currentPath.remove(currentPath.size() - 1);
            return;
        }

        visited[start.x][start.y] = true;
        currentPath.add(new State(start.x, start.y, currentCost, prevDirection));

        for (int i = 0; i < 4; i++) {
            int newX = start.x + DX[i];
            int newY = start.y + DY[i];
            long newCost = currentCost + COST[i];

            if (prevDirection != -1 && prevDirection != i) {
                newCost += TURN_COST;
            }

            if (isValid(grid, newX, newY, visited)) {
                dfs(grid, new State(newX, newY, newCost, i), end, visited, currentPath, paths, newCost, i);
            }
        }

        currentPath.remove(currentPath.size() - 1);
        visited[start.x][start.y] = false;
    }

    private static List<State> bfs(char[][] grid, State start, State end) {
        Queue<List<State>> queue = new PriorityQueue<>(
                Comparator.comparingLong(path -> path.get(path.size() - 1).cost));
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        // Initialize BFS with the starting point
        List<State> startPath = new ArrayList<>();
        startPath.add(start);
        queue.add(startPath);
        visited[start.x][start.y] = true;

        while (!queue.isEmpty()) {
            List<State> currentPath = queue.poll();
            State currentPoint = currentPath.get(currentPath.size() - 1);
            long currentCost = currentPoint.cost;
            int prevDirection = currentPoint.direction;

            // Check if we reached the end point
            if (currentPoint.x == end.x && currentPoint.y == end.y) {
                return currentPath; // Return the shortest path
            }

            // Explore neighbors
            for (int i = 0; i < 4; i++) {
                int newX = currentPoint.x + DX[i];
                int newY = currentPoint.y + DY[i];
                long newCost = currentCost + COST[i];

                if (prevDirection != -1 && prevDirection != i) {
                    newCost += TURN_COST;
                }

                State newState = new State(newX, newY, newCost, i);
                if (isValid(grid, newX, newY, visited)) {
                    visited[newX][newY] = true;
                    List<State> newPath = new ArrayList<>(currentPath);
                    newPath.add(newState);
                    queue.add(newPath);
                }
            }
        }

        return new ArrayList<>(); // Return empty path if no path exists
    }

    private static boolean isValid(char[][] grid, int x, int y, boolean[][] visited) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length
                && grid[x][y] != '#' && !visited[x][y];
    }

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
                if (tempGrid[x][y] == '.') { // Don't overwrite start (S) or end (E)
                    tempGrid[x][y] = '*';
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
