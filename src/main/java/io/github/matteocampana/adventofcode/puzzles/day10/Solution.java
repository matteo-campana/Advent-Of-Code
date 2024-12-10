package io.github.matteocampana.adventofcode.puzzles.day10;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Solution {
    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day10/input.txt");
        List<String> input = new ArrayList<>();

        try (
                InputStream inputStream = Solution.class
                        .getClassLoader()
                        .getResourceAsStream(inputFilePath.toString())) {
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

        int countValidTrailsheads = 0;
        int countPossibleTrailheadsPaths = 0;

        for (int row = 0; row < input.size(); row++) {
            for (int col = 0; col < input.get(row).length(); col++) { // Fix the loop condition
                countValidTrailsheads += countValidTrailheads(input, row, col,false);
                countPossibleTrailheadsPaths += countValidTrailheads(input, row, col,true);
            }
        }

        System.out.println("\n\n[PART 1] Valid Trailheads: " + countValidTrailsheads);
        System.out.println("[PART 2] Possible Valid Trailheads Paths: " + countValidTrailsheads);

    }

    private static int countValidTrailheads(List<String> topoMap, int row, int col, boolean countPossiblePaths) {
        if (topoMap.get(row).charAt(col) != '0')
            return 0;

        int validPathsCount = 0;
        Queue<List<Integer>> trailPath = new ArrayDeque<>();
        trailPath.add(Arrays.asList(row, col));

        Set<String> visited = new HashSet<>();
        while (!trailPath.isEmpty()) {
            List<Integer> currentPosition = trailPath.poll();
            int currentPositionRow = currentPosition.get(0);
            int currentPositionCol = currentPosition.get(1);
            if (topoMap.get(currentPositionRow).charAt(currentPositionCol) == '.') // Fix the condition
                continue;
            int currentHeight = Character.getNumericValue(topoMap.get(currentPositionRow).charAt(currentPositionCol));

            if (currentHeight == 9) {
                String position = currentPositionRow + "," + currentPositionCol;
                if (!countPossiblePaths && visited.contains(position)) {
                    continue;
                }
                visited.add(position);
                validPathsCount++;
                continue;
            }

            if (currentPositionRow + 1 < topoMap.size()
                    && currentHeight + 1 == Character
                            .getNumericValue(topoMap.get(currentPositionRow + 1).charAt(currentPositionCol))) {

                trailPath.add(Arrays.asList(currentPositionRow + 1, currentPositionCol));
            }

            if (currentPositionRow - 1 >= 0
                    && currentHeight + 1 == Character
                            .getNumericValue(topoMap.get(currentPositionRow - 1).charAt(currentPositionCol))) {

                trailPath.add(Arrays.asList(currentPositionRow - 1, currentPositionCol));
            }

            if (currentPositionCol + 1 < topoMap.get(0).length()
                    && currentHeight + 1 == Character
                            .getNumericValue(topoMap.get(currentPositionRow).charAt(currentPositionCol + 1))) {

                trailPath.add(Arrays.asList(currentPositionRow, currentPositionCol + 1));
            }

            if (currentPositionCol - 1 >= 0
                    && currentHeight + 1 == Character
                            .getNumericValue(topoMap.get(currentPositionRow).charAt(currentPositionCol - 1))) {

                trailPath.add(Arrays.asList(currentPositionRow, currentPositionCol - 1));
            }
        }

        // System.out.println("\n\n");
        // System.out.println("row: " + row + ", col: " + col);
        // System.out.println("Valid trailheads: " + validPathsCount);

        return validPathsCount;
    }
}