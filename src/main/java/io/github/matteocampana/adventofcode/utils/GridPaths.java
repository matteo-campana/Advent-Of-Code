package io.github.matteocampana.adventofcode.utils;

import java.util.*;

public class GridPaths {
    private static final int[] DX = {0, 1, 0, -1}; // Directions for row movement
    private static final int[] DY = {1, 0, -1, 0}; // Directions for column movement

    public static void main(String[] args) {
        char[][] grid = {
                {'S', '.', '.', '#', '.', '.'},
                {'.', '#', '.', '#', '.', '.'},
                {'.', '#', '.', '.', '.', 'E'},
                {'.', '.', '#', '#', '.', '.'},
        };

        System.out.println("Original Grid:");
        printGrid(grid, null);

        List<List<int[]>> paths = new ArrayList<>();
        List<Integer> costs = new ArrayList<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        int[] start = findPosition(grid, 'S');
        int[] end = findPosition(grid, 'E');

        if (start == null || end == null) {
            System.out.println("Start or End point not found!");
            return;
        }

        // DFS to find all paths
        List<int[]> currentPath = new ArrayList<>();
        dfs(grid, start[0], start[1], end[0], end[1], visited, currentPath, paths, costs);

        // Output DFS results
        System.out.println("\nPaths and their costs:");
        for (int i = 0; i < paths.size(); i++) {
            System.out.println("Path " + (i + 1) + ": " + pathToString(paths.get(i)) + ", Cost: " + costs.get(i));
            System.out.println("Grid with Path " + (i + 1) + ":");
            printGrid(grid, paths.get(i));
        }

        // BFS to find the minimum-cost path
        List<int[]> minPath = bfs(grid, start, end);
        System.out.println("\nMinimum Path (BFS): " + pathToString(minPath) + ", Cost: " + (minPath.size() - 1));
        System.out.println("Grid with Minimum Path:");
        printGrid(grid, minPath);
    }

    private static void dfs(char[][] grid, int x, int y, int endX, int endY,
                            boolean[][] visited, List<int[]> currentPath,
                            List<List<int[]>> paths, List<Integer> costs) {
        if (x == endX && y == endY) {
            // Found a path
            currentPath.add(new int[]{x, y});
            paths.add(new ArrayList<>(currentPath));
            costs.add(currentPath.size() - 1);
            currentPath.removeLast();
            return;
        }

        visited[x][y] = true;
        currentPath.add(new int[]{x, y});

        for (int i = 0; i < 4; i++) {
            int newX = x + DX[i];
            int newY = y + DY[i];

            if (isValid(grid, newX, newY, visited)) {
                dfs(grid, newX, newY, endX, endY, visited, currentPath, paths, costs);
            }
        }

        currentPath.removeLast();
        visited[x][y] = false;
    }

    private static List<int[]> bfs(char[][] grid, int[] start, int[] end) {
        Queue<List<int[]>> queue = new LinkedList<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        // Initialize BFS with the starting point
        List<int[]> startPath = new ArrayList<>();
        startPath.add(start);
        queue.add(startPath);
        visited[start[0]][start[1]] = true;

        while (!queue.isEmpty()) {
            List<int[]> currentPath = queue.poll();
            int[] currentPoint = currentPath.getLast();

            // Check if we reached the end point
            if (currentPoint[0] == end[0] && currentPoint[1] == end[1]) {
                return currentPath; // Return the shortest path
            }

            // Explore neighbors
            for (int i = 0; i < 4; i++) {
                int newX = currentPoint[0] + DX[i];
                int newY = currentPoint[1] + DY[i];

                if (isValid(grid, newX, newY, visited)) {
                    visited[newX][newY] = true;
                    List<int[]> newPath = new ArrayList<>(currentPath);
                    newPath.add(new int[]{newX, newY});
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

    private static int[] findPosition(char[][] grid, char target) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private static String pathToString(List<int[]> path) {
        StringBuilder sb = new StringBuilder();
        for (int[] point : path) {
            sb.append("-> (").append(point[0]).append(",").append(point[1]).append(") ");
        }
        return sb.toString().trim();
    }

    private static void printGrid(char[][] grid, List<int[]> path) {
        char[][] tempGrid = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, tempGrid[i], 0, grid[0].length);
        }

        if (path != null) {
            for (int[] point : path) {
                int x = point[0];
                int y = point[1];
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
