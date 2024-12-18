package io.github.matteocampana.adventofcode.puzzles.day18;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day18/input.txt");
        List<int[]> positions = new ArrayList<>();
        int x, y;
        try (InputStream inputStream = Solution.class.getClassLoader().getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    x = Integer.parseInt(line.split(",")[0]);
                    y = Integer.parseInt(line.split(",")[1]);
                    positions.add(new int[]{x, y});
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Part1(positions, 70, 1024);

        Part2(positions, 70, 1024);

    }

    private static void Part1(List<int[]> positions, int gridSize, int blocks) {
        gridSize++;
        boolean[][] grid = new boolean[gridSize][gridSize];

        for (int i = 0; i < blocks; i++) {
            grid[positions.get(i)[1]][positions.get(i)[0]] = true; // Mark corrupted positions
        }

        int steps = findShortestPath(grid, gridSize);
        System.out.println("[PART 1] Minimum number of steps to reach the exit: " + steps);
    }

    private static void Part2(List<int[]> positions, int gridSize, int blocks) {
        gridSize++;
        boolean[][] grid = new boolean[gridSize][gridSize];

        int i = 0;
        while (findShortestPath(grid, gridSize) > 0) {
            i++;
            grid[positions.get(i)[1]][positions.get(i)[0]] = true;
        }

        System.out
                .println("[PART 2] First block that causes the shortest path to be longer valid: [x, y] = "
                        + Arrays.toString(positions.get(i)).replaceAll("[\\[\\] ]", ""));
    }

    private static int findShortestPath(boolean[][] grid, int gridSize) {
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        boolean[][] visited = new boolean[gridSize][gridSize];
        PriorityQueue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a[2]));
        queue.add(new int[]{0, 0, 0}); // {x, y, steps}
        visited[0][0] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1], steps = current[2];

            if (x == gridSize - 1 && y == gridSize - 1) {
                return steps;
            }

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i], ny = y + dy[i];
                if (nx >= 0 && ny >= 0 && nx < gridSize && ny < gridSize && !grid[nx][ny] && !visited[nx][ny]) {
                    queue.add(new int[]{nx, ny, steps + 1});
                    visited[nx][ny] = true;
                }
            }
        }

        return -1; // No path found
    }
}
