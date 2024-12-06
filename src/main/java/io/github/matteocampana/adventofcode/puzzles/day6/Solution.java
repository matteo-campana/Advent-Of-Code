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

        Guard guard1 = new Guard(labMap);

        System.out.println("#".repeat(50));
        System.out.println("Part 1");
        System.out.println("-".repeat(50));
        guard1.updateMapPart1();

        printLabMap(guard1.labMap);

        System.out.println(guard1.countVisitedCells());

        System.out.println("#".repeat(50));
        System.out.println("Part 2");
        System.out.println("-".repeat(50));

        Guard guard2 = new Guard(labMap);

        guard2.updateMapPart2();

        printLabMap(guard2.labMap);
    }

    private static class Guard {
        int posY, posX;
        boolean outOfMap;
        char[][] labMap;
        char direction;

        public Guard(char[][] labMap) {
            this.labMap = new char[labMap.length][labMap[0].length];
            for (int i = 0; i < labMap.length; i++) {
                System.arraycopy(labMap[i], 0, this.labMap[i], 0, labMap[i].length);
            }
            findGuard();
            outOfMap = false;
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
        }

        private boolean isOutOfMap(int x, int y) {
            return y >= labMap.length || x >= labMap[0].length || y < 0
                    || x < 0;
        }

        private void rotate() {
            direction = switch (direction) {
                case '>' -> 'v';
                case 'v' -> '<';
                case '<' -> '^';
                case '^' -> '>';
                default -> direction;
            };
        }

        public void updateMapPart1() {
            while (!outOfMap) {
                int[] nextPosition = nextPos();
                if (isOutOfMap(nextPosition[1], nextPosition[0])) {
                    outOfMap = true;
                    labMap[posY][posX] = 'X';
                } else {
                    labMap[posY][posX] = 'X';
                    posY = nextPosition[0];
                    posX = nextPosition[1];
                }
            }
        }

        public void updateMapPart2() {
            while (!outOfMap) {

                char marker;
                char prevDirection = direction;

                int[] nextPosition = nextPos();

                if (prevDirection == direction) {
                    if (prevDirection == '^' || prevDirection == 'v') {
                        marker = '|';
                    } else {
                        marker = '-';
                    }
                } else {
                    marker = '+';
                }

                if (marker == '|' && labMap[posY][posX] == '-' || marker == '-' &&
                        labMap[posY][posX] == '|') {
                    marker = '+';
                }

                if (isOutOfMap(nextPosition[1], nextPosition[0])) {
                    outOfMap = true;
                    labMap[posY][posX] = marker;
                } else {
                    labMap[posY][posX] = marker;
                    posY = nextPosition[0];
                    posX = nextPosition[1];

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

            int[] nextPosition = new int[2];
            switch (direction) {
                case 'v' -> {
                    if (posY + 1 >= labMap.length) {
                        outOfMap = true;
                        return new int[] { posY + 1, posX };
                    }
                    if (labMap[posY + 1][posX] == '#') {
                        rotate();
                        return nextPos();
                    } else {
                        nextPosition[0] = posY + 1;
                        nextPosition[1] = posX;
                    }
                }
                case '^' -> {
                    if (posY - 1 < 0) {
                        outOfMap = true;
                        return new int[] { posY - 1, posX };
                    }
                    if (labMap[posY - 1][posX] == '#') {
                        rotate();
                        return nextPos();
                    } else {
                        nextPosition[0] = posY - 1;
                        nextPosition[1] = posX;
                    }
                }
                case '>' -> {
                    if (posX + 1 >= labMap[0].length) {
                        outOfMap = true;
                        return new int[] { posY, posX + 1 };
                    }
                    if (labMap[posY][posX + 1] == '#') {
                        rotate();
                        return nextPos();
                    } else {
                        nextPosition[0] = posY;
                        nextPosition[1] = posX + 1;
                    }
                }
                case '<' -> {
                    if (posX - 1 < 0) {
                        outOfMap = true;
                        return new int[] { posY, posX - 1 };
                    }
                    if (labMap[posY][posX - 1] == '#') {
                        rotate();
                        return nextPos();
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
