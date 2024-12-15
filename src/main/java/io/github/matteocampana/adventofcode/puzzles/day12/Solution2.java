package io.github.matteocampana.adventofcode.puzzles.day12;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Solution2 {

    private static List<Region> findRegions(char[][] data) {
        List<Region> regions = new ArrayList<>();
        Queue<Position> outRegionPositions = new LinkedList<>();
        outRegionPositions.add(new Position(0, 0));
        Set<String> visited = new HashSet<>();

        while (!outRegionPositions.isEmpty()) {
            Position start = outRegionPositions.poll();
            if (visited.contains(start.toString()))
                continue;

            int area = 0;
            int perimeter = 0;
            Map<String, Position> spaces = new HashMap<>();
            Queue<Position> inRegionPositions = new LinkedList<>();
            inRegionPositions.add(start);
            char regionLetter = data[start.y][start.x];

            while (!inRegionPositions.isEmpty()) {
                Position current = inRegionPositions.poll();
                if (visited.contains(current.toString()))
                    continue;

                List<Position> adjacent = new ArrayList<>(Arrays.asList(
                        new Position(current.y - 1, current.x),
                        new Position(current.y + 1, current.x),
                        new Position(current.y, current.x - 1),
                        new Position(current.y, current.x + 1)));
                adjacent.removeIf(val -> !inGrid(val, data));
                int inRegionAdjacent = 0;
                for (Position val : adjacent) {
                    if (data[val.y][val.x] == regionLetter) {
                        inRegionAdjacent++;
                        inRegionPositions.add(val);
                    } else {
                        outRegionPositions.add(val);
                    }
                }
                int edges = 4 - inRegionAdjacent;
                perimeter += edges;
                area++;
                spaces.put(current.toString(), new Position(current.y, current.x, edges));
                visited.add(current.toString());
            }

            int corners = 0;
            for (Position space : spaces.values()) {
                boolean hasUp = spaces.containsKey(new Position(space.y - 1, space.x).toString());
                boolean hasDown = spaces.containsKey(new Position(space.y + 1, space.x).toString());
                boolean hasLeft = spaces.containsKey(new Position(space.y, space.x - 1).toString());
                boolean hasRight = spaces.containsKey(new Position(space.y, space.x + 1).toString());
                boolean hasUpperRight = spaces.containsKey(new Position(space.y - 1, space.x + 1).toString());
                boolean hasLowerRight = spaces.containsKey(new Position(space.y + 1, space.x + 1).toString());
                boolean hasLowerLeft = spaces.containsKey(new Position(space.y + 1, space.x - 1).toString());
                boolean hasUpperLeft = spaces.containsKey(new Position(space.y - 1, space.x - 1).toString());
                boolean inBoundsUpperRight = inGrid(new Position(space.y - 1, space.x + 1), data);
                boolean inBoundsLowerRight = inGrid(new Position(space.y + 1, space.x + 1), data);
                boolean inBoundsLowerLeft = inGrid(new Position(space.y + 1, space.x - 1), data);
                boolean inBoundsUpperLeft = inGrid(new Position(space.y - 1, space.x - 1), data);

                if (space.edges == 4) {
                    corners += 4;
                } else if (space.edges == 3) {
                    corners += 2;
                } else if (space.edges == 2) {
                    if (hasUp && hasRight) {
                        corners += hasUpperRight ? 1 : 2;
                    } else if (hasRight && hasDown) {
                        corners += hasLowerRight ? 1 : 2;
                    } else if (hasDown && hasLeft) {
                        corners += hasLowerLeft ? 1 : 2;
                    } else if (hasLeft && hasUp) {
                        corners += hasUpperLeft ? 1 : 2;
                    }
                } else if (space.edges == 1) {
                    if (hasLeft && hasDown && hasRight) {
                        if (!hasLowerRight && inBoundsLowerRight)
                            corners += 1;
                        if (!hasLowerLeft && inBoundsLowerLeft)
                            corners += 1;
                    } else if (hasLeft && hasDown && hasUp) {
                        if (!hasUpperLeft && inBoundsUpperLeft)
                            corners += 1;
                        if (!hasLowerLeft && inBoundsLowerLeft)
                            corners += 1;
                    } else if (hasLeft && hasUp && hasRight) {
                        if (!hasUpperRight && inBoundsUpperRight)
                            corners += 1;
                        if (!hasUpperLeft && inBoundsUpperLeft)
                            corners += 1;
                    } else if (hasUp && hasDown && hasRight) {
                        if (!hasLowerRight && inBoundsLowerRight)
                            corners += 1;
                        if (!hasUpperRight && inBoundsUpperRight)
                            corners += 1;
                    }
                } else if (space.edges == 0) {
                    if (!hasLowerRight && inBoundsLowerRight)
                        corners += 1;
                    if (!hasLowerLeft && inBoundsLowerLeft)
                        corners += 1;
                    if (!hasUpperRight && inBoundsUpperRight)
                        corners += 1;
                    if (!hasUpperLeft && inBoundsUpperLeft)
                        corners += 1;
                }
            }
            regions.add(new Region(area, perimeter, corners));
        }
        return regions;
    }

    private static boolean inGrid(Position val, char[][] data) {
        return val.y >= 0 && val.y < data.length && val.x >= 0 && val.x < data[val.y].length;
    }

    static class Position {
        int y, x, edges;

        Position(int y, int x) {
            this.y = y;
            this.x = x;
        }

        Position(int y, int x, int edges) {
            this.y = y;
            this.x = x;
            this.edges = edges;
        }

        @Override
        public String toString() {
            return y + "," + x;
        }
    }

    static class Region {
        int area, perimeter, corners;

        Region(int area, int perimeter, int corners) {
            this.area = area;
            this.perimeter = perimeter;
            this.corners = corners;
        }
    }

    public static void main(String[] args) {

        Path inputFilePath = Paths.get("input/day12/input.txt");
        List<String> fileContents = new ArrayList<>();

        try (InputStream inputStream = Solution.class.getClassLoader().getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    fileContents.add(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        char[][] data = fileContents.stream().map(String::toCharArray).toArray(char[][]::new);
        List<Region> regions = findRegions(data);
        int result1 = regions.stream().mapToInt(region -> region.area * region.perimeter).sum();
        int result2 = regions.stream().mapToInt(region -> region.area * region.corners).sum();

        System.out.println("Part 1: " + result1);
        System.out.println("Part 2: " + result2);
    }
}
