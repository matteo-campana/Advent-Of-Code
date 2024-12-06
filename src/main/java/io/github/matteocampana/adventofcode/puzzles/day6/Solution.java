package io.github.matteocampana.adventofcode.puzzles.day6;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {

    private static void printLabMap(char[][] labMap) {
        for (char[] line : labMap) {
            System.out.println(Arrays.toString(line));
        }
    }

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day6/test.txt");

        List<char[]> labMapList = new ArrayList<>();

        try (InputStream inputStream = Solution.class.getClassLoader().getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    labMapList.add(line.trim().toCharArray());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        char[][] labMap = labMapList.toArray(new char[0][]);
        printLabMap(labMap);

        Guard guard = new Guard(labMap);

        System.out.println("#".repeat(50));

        printLabMap(labMap);

        System.out.println(guard.countVisitedCells());
    }

    private static class Guard {
        int posY, posX;
        boolean outOfMap;
        char[][] labMap;
        char direction;

        public Guard(char[][] labMap) {
            this.labMap = labMap;
            findGuard();
            outOfMap = false;
            updateMap();
        }

        private void findGuard() {
            for (int i = 0; i < labMap.length; i++) {
                for (int j = 0; j < labMap[i].length; j++) {
                    if ("<>v^".indexOf(labMap[i][j]) != -1) {
                        posY = i;
                        posX = j;
                        direction = labMap[i][j];

                        return;
                    }
                }
            }
            System.out.println("Guard not found");
        }

        private void rotate() {
            direction = switch (direction) {
                case '>' -> 'v';
                case 'v' -> '<';
                case '<' -> '^';
                case '^' -> '>';
                default -> direction;
            };
            labMap[posY][posX] = direction;
        }

        private void updateMap() {
            while (!outOfMap) {
                int[] nextPosition = nextPos();
                if (nextPosition[0] >= labMap.length || nextPosition[1] >= labMap[0].length || nextPosition[0] < 0
                        || nextPosition[1] < 0) {
                    outOfMap = true;
                    labMap[posY][posX] = 'X';
                } else {
                    labMap[posY][posX] = 'X';
                    posY = nextPosition[0];
                    posX = nextPosition[1];
                    labMap[posY][posX] = direction;
                }
            }
        }

        private int countVisitedCells() {
            int count = 0;
            for (char[] line : labMap) {
                for (char cell : line) {
                    if (cell == 'X') {
                        count++;
                    }
                }
            }
            return count;
        }

        private int[] nextPos() {
            if (outOfMap) {
                return new int[] { -1, -1 };
            }

            int[] nextPosition = new int[2];
            switch (direction) {
                case 'v' -> {
                    if (posY + 1 >= labMap.length) {
                        outOfMap = true;
                        return new int[] { -1, -1 };
                    }
                    if (labMap[posY + 1][posX] == '#') {
                        rotate();
                        return new int[]{posY,posX};
                    } else {
                        nextPosition[0] = posY + 1;
                        nextPosition[1] = posX;
                    }
                }
                case '^' -> {
                    if (posY - 1 < 0) {
                        outOfMap = true;
                        return new int[] { -1, -1 };
                    }
                    if (labMap[posY - 1][posX] == '#') {
                        rotate();
                        return new int[]{posY,posX};
                    } else {
                        nextPosition[0] = posY - 1;
                        nextPosition[1] = posX;
                    }
                }
                case '>' -> {
                    if (posX + 1 >= labMap[0].length) {
                        outOfMap = true;
                        return new int[] { -1, -1 };
                    }
                    if (labMap[posY][posX + 1] == '#') {
                        rotate();
                        return new int[]{posY,posX};
                    } else {
                        nextPosition[0] = posY;
                        nextPosition[1] = posX + 1;
                    }
                }
                case '<' -> {
                    if (posX - 1 < 0) {
                        outOfMap = true;
                        return new int[] { -1, -1 };
                    }
                    if (labMap[posY][posX - 1] == '#') {
                        rotate();
                        return new int[]{posY,posX};
                    } else {
                        nextPosition[0] = posY;
                        nextPosition[1] = posX - 1;
                    }
                }
            }
            return nextPosition;
        }
    }
}
