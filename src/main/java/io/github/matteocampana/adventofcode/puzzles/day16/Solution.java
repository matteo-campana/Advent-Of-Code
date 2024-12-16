package io.github.matteocampana.adventofcode.puzzles.day16;

import kotlin.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day16/input.txt");
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

        char[][] maze = input.stream().map(String::toCharArray).toArray(char[][]::new);

        List<List<int[]>> bestPaths = new ArrayList<>();
        int minimumScore = Maze.findMinimumScore(maze, bestPaths);

        System.out.println("[PART 1] Minimum score: " + minimumScore);

        System.out.println("\n\n" + "-".repeat(100) + "\n\n");

        int bestPathTileCount = Maze.countTilesInBestPaths(maze, bestPaths);
        System.out.println("[PART 2] Number of tiles part of at least one best path: " + bestPathTileCount);

        // Print the maze with best path tiles marked
        System.out.println("Maze with best path tiles marked:");
        for (char[] row : maze) {
            System.out.println(new String(row));
        }

        // Print the best paths

        // System.out.println("Best paths:");
        // for (List<int[]> path : bestPaths) {
        // for (int[] step : path) {
        // System.out.print(Arrays.toString(step) + " -> ");
        // }
        // System.out.println("END");
        // }

    }

    private static class Maze {
        private final char[][] maze;

        public Maze(char[][] maze) {
            this.maze = maze.clone();
        }

        @Override
        public String toString() {
            StringBuilder mazeStringBuilder = new StringBuilder();
            for (char[] line : maze) {
                mazeStringBuilder.append(Arrays.toString(line).replaceAll("[\\[\\], ]", "")).append("\n");
            }
            return mazeStringBuilder.toString();
        }

        public static int findLowestScore(char[][] maze, int startX, int startY, int endX, int endY) {
            int rows = maze.length;
            int cols = maze[0].length;

            // Priority Queue for BFS with minimum score
            PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.score));
            // Visited set to store (x, y, direction)
            boolean[][][] visited = new boolean[rows][cols][4];

            // Initialize starting state facing East (0)
            pq.add(new State(startX, startY, 0, 0));

            while (!pq.isEmpty()) {
                State current = pq.poll();

                // If we reach the end tile, return the score
                if (current.x == endX && current.y == endY) {
                    return current.score;
                }

                // Skip if already visited
                if (visited[current.x][current.y][current.direction]) {
                    continue;
                }
                visited[current.x][current.y][current.direction] = true;

                // Move forward
                int nx = current.x + DX[current.direction];
                int ny = current.y + DY[current.direction];

                if (isValidMove(maze, nx, ny)) {
                    pq.add(new State(nx, ny, current.direction, current.score + 1));
                }

                // Rotate clockwise
                int clockwiseDirection = (current.direction + 1) % 4;
                if (!visited[current.x][current.y][clockwiseDirection]) {
                    pq.add(new State(current.x, current.y, clockwiseDirection, current.score + 1000));
                }

                // Rotate counterclockwise
                int counterClockwiseDirection = (current.direction + 3) % 4;
                if (!visited[current.x][current.y][counterClockwiseDirection]) {
                    pq.add(new State(current.x, current.y, counterClockwiseDirection, current.score + 1000));
                }
            }

            // If no path found (should not happen in valid input)
            return -1;
        }

        static class State {
            int x, y, direction, cost, score;
            List<int[]> path;

            public State(int x, int y, int direction, int score) {
                this.x = x;
                this.y = y;
                this.direction = direction;
                this.score = score;
            }

            State(int x, int y, int direction, int cost, List<int[]> path) {
                this.x = x;
                this.y = y;
                this.direction = direction;
                this.cost = cost;
                this.path = new ArrayList<>(path);
                this.path.add(new int[] { x, y });
            }
        }

        // Directions: 0 = East, 1 = South, 2 = West, 3 = North
        static final int[] DX = { 0, 1, 0, -1 };
        static final int[] DY = { 1, 0, -1, 0 };

        public static int findMinimumScore(char[][] maze, List<List<int[]>> bestPaths) {
            int rows = maze.length;
            int cols = maze[0].length;

            int startX = 0, startY = 0, endX = 0, endY = 0;

            // Locate the start (S) and end (E) positions
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (maze[i][j] == 'S') {
                        startX = i;
                        startY = j;
                    } else if (maze[i][j] == 'E') {
                        endX = i;
                        endY = j;
                    }
                }
            }

            // Priority queue for BFS with states sorted by cost (min-heap)
            PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.cost));

            // Visited array to track minimum cost for each position and direction
            int[][][] visited = new int[rows][cols][4];
            for (int[][] row : visited) {
                for (int[] col : row) {
                    Arrays.fill(col, Integer.MAX_VALUE);
                }
            }

            // Start BFS from the start position, facing East (direction 0)
            pq.add(new State(startX, startY, 0, 0, new ArrayList<>()));
            visited[startX][startY][0] = 0;

            int minCost = Integer.MAX_VALUE;

            while (!pq.isEmpty()) {
                State current = pq.poll();

                // If we reach the end, check if the cost is minimal
                if (current.x == endX && current.y == endY) {
                    if (current.cost < minCost) {
                        minCost = current.cost;
                        bestPaths.clear();
                    }
                    if (current.cost == minCost) {
                        bestPaths.add(current.path);
                    }
                    continue;
                }

                // Try moving forward
                int nx = current.x + DX[current.direction];
                int ny = current.y + DY[current.direction];
                if (isValidMove(maze, nx, ny)) {
                    int newCost = current.cost + 1; // Moving forward costs 1
                    if (newCost <= visited[nx][ny][current.direction]) {
                        visited[nx][ny][current.direction] = newCost;
                        pq.add(new State(nx, ny, current.direction, newCost, current.path));
                    }
                }

                // Try rotating (clockwise and counterclockwise)
                for (int turn = -1; turn <= 1; turn += 2) {
                    int newDirection = (current.direction + turn + 4) % 4;
                    int newCost = current.cost + 1000; // Turning costs 1000
                    if (newCost <= visited[current.x][current.y][newDirection]) {
                        visited[current.x][current.y][newDirection] = newCost;
                        pq.add(new State(current.x, current.y, newDirection, newCost, current.path));
                    }
                }
            }

            return minCost == Integer.MAX_VALUE ? -1 : minCost;
        }

        private static boolean isValidMove(char[][] maze, int x, int y) {
            return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length && maze[x][y] != '#';
        }

        public static int countTilesInBestPaths(char[][] maze, List<List<int[]>> bestPaths) {
            Set<String> tiles = new HashSet<>();

            for (List<int[]> path : bestPaths) {
                for (int[] step : path) {
                    tiles.add(step[0] + "," + step[1]);
                }
            }

            // Mark all tiles that are part of best paths
            for (String tile : tiles) {
                String[] parts = tile.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                if (maze[x][y] == '.') {
                    maze[x][y] = 'O';
                }
            }

            return tiles.size();
        }

    }
}
