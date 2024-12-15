package io.github.matteocampana.adventofcode.puzzles.day12;

import kotlin.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day12/test.txt");
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

        char[][] garden = input.stream().map(String::toCharArray).toArray(char[][]::new);

        for (char[] c : garden) {
            System.out.println(Arrays.toString(c).replaceAll("[\\[\\], ]", ""));
        }

        System.out.println("\n\n" + "#".repeat(100));
        System.out.println("Part 1: ");
        System.out.println("Part 1: total fence cost: " + Part1(garden));

        System.out.println("\n\n" + "#".repeat(100));
        System.out.println("Part 2: ");
        System.out.println("Part 2: total fence cost: " + Part2(garden));
    }

    private static int Part1(char[][] garden) {
        boolean[][] visited = new boolean[garden.length][garden[0].length];

        int totalCost = 0;
        int area, perimeter, fenceCost = 0;

        for (int row = 0; row < garden.length; row++) {
            for (int col = 0; col < garden[0].length; col++) {
                List<Pair<Integer, Integer>> region = exploreRegion(garden, visited, row, col);

                if (!region.isEmpty()) {
                    area = calculateRegionArea(region);
                    perimeter = calculateRegionPerimeter(region);
                    fenceCost = area * perimeter;
                    totalCost += fenceCost;

                    // System.out.println("A region of " + garden[row][col] + " plants with price "
                    // + area + " * "
                    // + perimeter + " = " + fenceCost + ".");
                }
            }
        }

        return totalCost;
    }

    private static int Part2(char[][] garden) {
        boolean[][] visited = new boolean[garden.length][garden[0].length];

        int totalCost = 0;
        int area, side, fenceCost = 0;

        for (int row = 0; row < garden.length; row++) {
            for (int col = 0; col < garden[0].length; col++) {
                List<Pair<Integer, Integer>> region = exploreRegion(garden, visited, row, col);

                if (!region.isEmpty()) {
                    area = calculateRegionArea(region);
                    List<Pair<Integer, Integer>> fence = buildFence(region);
                    side = printFence(fence, region, garden[row][col]);
                    // side = calculateSides(fence);
                    fenceCost = area * side;
                    totalCost += fenceCost;

                    System.out.println("A region of " + garden[row][col] + " plants with price " + area + " * " + side
                            + " = " + fenceCost + ".\n\n");
                }
            }
        }

        return totalCost;
    }

    private static List<Pair<Integer, Integer>> exploreRegion(char[][] garden, boolean[][] visited, int row, int col) {

        if (visited[row][col])
            return new ArrayList<>();

        char regionValue = garden[row][col];

        List<Pair<Integer, Integer>> region = new ArrayList<>();

        Stack<Pair<Integer, Integer>> bfsStack = new Stack<>();

        bfsStack.add(new Pair<>(row, col));

        while (!bfsStack.isEmpty()) {
            Pair<Integer, Integer> nextNode = bfsStack.pop();
            int currentRow = nextNode.component1();
            int currentCol = nextNode.component2();

            if (garden[currentRow][currentCol] != regionValue || visited[currentRow][currentCol]) {
                continue;
            }

            if (region.contains(new Pair<>(currentRow, currentCol)))
                continue;

            // expand the region and set the cell as visited
            region.add(new Pair<>(currentRow, currentCol));
            visited[currentRow][currentCol] = true;

            // add the adjacent cells to the stack

            // up
            if (currentRow - 1 >= 0 && !visited[currentRow - 1][currentCol]) {
                bfsStack.add(new Pair<>(currentRow - 1, currentCol));
            }

            // down
            if (currentRow + 1 < garden.length && !visited[currentRow + 1][currentCol]) {
                bfsStack.add(new Pair<>(currentRow + 1, currentCol));
            }

            // left
            if (currentCol - 1 >= 0 && !visited[currentRow][currentCol - 1]) {
                bfsStack.add(new Pair<>(currentRow, currentCol - 1));
            }

            // right
            if (currentCol + 1 < garden[0].length && !visited[currentRow][currentCol + 1]) {
                bfsStack.add(new Pair<>(currentRow, currentCol + 1));
            }
        }
        return region;
    }

    private static int calculateRegionArea(List<Pair<Integer, Integer>> region) {
        return region.size();
    }

    private static int calculateRegionPerimeter(List<Pair<Integer, Integer>> region) {
        int perimeter = 0;

        for (Pair<Integer, Integer> cell : region) {
            int row = cell.component1();
            int col = cell.component2();

            if (!region.contains(new Pair<Integer, Integer>(row - 1, col))) {
                perimeter++;
            }

            if (!region.contains(new Pair<Integer, Integer>(row + 1, col))) {
                perimeter++;
            }

            if (!region.contains(new Pair<Integer, Integer>(row, col - 1))) {
                perimeter++;
            }

            if (!region.contains(new Pair<Integer, Integer>(row, col + 1))) {
                perimeter++;
            }
        }
        return perimeter;
    }

    private static List<Pair<Integer, Integer>> buildFence(List<Pair<Integer, Integer>> region) {
        List<Pair<Integer, Integer>> fence = new ArrayList<>();
        for (Pair<Integer, Integer> cell : region) {
            int row = cell.component1();
            int col = cell.component2();

            if (!region.contains(new Pair<Integer, Integer>(row - 1, col))) {
                fence.add(new Pair<>(row - 1, col));
            }

            if (!region.contains(new Pair<Integer, Integer>(row + 1, col))) {
                fence.add(new Pair<>(row + 1, col));
            }

            if (!region.contains(new Pair<Integer, Integer>(row, col - 1))) {
                fence.add(new Pair<>(row, col - 1));
            }

            if (!region.contains(new Pair<Integer, Integer>(row, col + 1))) {
                fence.add(new Pair<>(row, col + 1));
            }
        }
        return fence;
    }

    private static boolean checkProximity(int[][] arr, int row, int col, char value) {
        // top-right angle
        if (row - 1 > 0 && row - 1 < arr.length - 1 && col + 1 < arr[row].length - 1) {
            if (arr[row - 1][col + 1] == value) {
                return true;
            }
        }

        // top left angle
        if (row - 1 > 0 && row - 1 < arr.length - 1 && col - 1 > 0
                && col - 1 < arr[row].length - 1) {
            if (arr[row - 1][col - 1] == value) {

                return true;
            }
        }

        // bottom right angle
        if (row + 1 < arr.length - 1 && col + 1 < arr[row].length - 1) {
            if (arr[row + 1][col + 1] == value) {

                return true;
            }
        }

        // bottom left angle
        if (row + 1 < arr.length - 1 && col - 1 > 0 && col - 1 < arr[row].length - 1) {
            if (arr[row + 1][col - 1] == value) {

                return true;
            }
        }

        return false;

    }

    private static int printFence(List<Pair<Integer, Integer>> fence, List<Pair<Integer, Integer>> region, char value) {
        if (fence.isEmpty()) {
            return 0;
        }

        // get the max row and column

        int maxRow = fence.stream().map(Pair::component1).max(Integer::compareTo).get();
        int maxCol = fence.stream().map(Pair::component2).max(Integer::compareTo).get();
        int minRow = fence.stream().map(Pair::component1).min(Integer::compareTo).get();
        int minCol = fence.stream().map(Pair::component2).min(Integer::compareTo).get();

        // standardize the fence matrix
        int rows = maxRow - minRow + 1 + 2;
        int cols = maxCol - minCol + 1 + 2;

        char[][] fenceMatrix = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            Arrays.fill(fenceMatrix[i], '.');
        }

        // add the region to the fence matrix
        for (Pair<Integer, Integer> cell : region) {
            int row = cell.component1() - minRow + 1;
            int col = cell.component2() - minCol + 1;
            fenceMatrix[row][col] = value;
        }

        for (Pair<Integer, Integer> cell : fence) {
            int row = cell.component1() - minRow + 1;
            int col = cell.component2() - minCol + 1;
            fenceMatrix[row][col] = '#';
        }

        int sidesCount = 0;

        // add angles to edges as "+"
        for (int row = 0; row < fenceMatrix.length; row++) {
            for (int col = 0; col < fenceMatrix[row].length; col++) {

                // V1
                if (fenceMatrix[row][col] == '#') {

                    // top-right angle
                    if (row - 1 > 0 && row - 1 < fenceMatrix.length - 1 && col + 1 < fenceMatrix[row].length - 1) {
                        if (fenceMatrix[row - 1][col + 1] == '#' && fenceMatrix[row - 1][col] == '.') {
                            fenceMatrix[row - 1][col] = '+';
                            sidesCount++;
                        }
                    }

                    // top left angle
                    if (row - 1 > 0 && row - 1 < fenceMatrix.length - 1 && col - 1 > 0
                            && col - 1 < fenceMatrix[row].length - 1) {
                        if (fenceMatrix[row - 1][col - 1] == '#' && fenceMatrix[row - 1][col] == '.') {
                            fenceMatrix[row - 1][col] = '+';
                            sidesCount++;
                        }
                    }

                    // bottom right angle
                    if (row + 1 < fenceMatrix.length - 1 && col + 1 < fenceMatrix[row].length - 1) {
                        if (fenceMatrix[row + 1][col + 1] == '#' && fenceMatrix[row + 1][col] == '.') {
                            fenceMatrix[row + 1][col] = '+';
                            sidesCount++;
                        }
                    }

                    // bottom left angle
                    if (row + 1 < fenceMatrix.length - 1 && col - 1 > 0 && col - 1 < fenceMatrix[row].length - 1) {
                        if (fenceMatrix[row + 1][col - 1] == '#' && fenceMatrix[row + 1][col] == '.') {
                            fenceMatrix[row + 1][col] = '+';
                            sidesCount++;
                        }
                    }

                }
            }
        }

        // print the fence matrix
        System.out.println("Fence for region of " + value + " plants:");
        for (char[] row : fenceMatrix) {
            System.out.println(Arrays.toString(row).replaceAll("[\\[\\], ]", ""));
        }

        return sidesCount;
    }

    private static int calculateSides(List<Pair<Integer, Integer>> fence) {
        int sides = 0;
        List<Pair<Integer, Integer>> visited = new ArrayList<>();

        fence.sort(
                Comparator.<Pair<Integer, Integer>, Integer>comparing(Pair::getFirst).thenComparing(Pair::getSecond));

        for (Pair<Integer, Integer> cell : fence) {

            int row = cell.component1();
            int col = cell.component2();

            if (visited.contains(cell)) {
                continue;
            }

            int colHorizontalRight = col;
            int colHorizontalLeft = col;

            int rowVerticalUp = row;
            int rowVerticalDown = row;

            int horizontalSides = 0;
            int verticalSides = 0;

            visited.add(new Pair<>(row, col));

            // left
            while (fence.contains(new Pair<>(row, ++colHorizontalRight))) {
                if (!visited.contains(new Pair<>(row, colHorizontalRight))) {
                    visited.add(new Pair<>(row, colHorizontalRight));
                    horizontalSides++;
                }
            }

            // right
            while (fence.contains(new Pair<>(row, --colHorizontalLeft))) {
                if (!visited.contains(new Pair<>(row, colHorizontalLeft))) {
                    visited.add(new Pair<>(row, colHorizontalLeft));
                    horizontalSides++;
                }
            }

            // up
            while (fence.contains(new Pair<>(--rowVerticalUp, col))) {
                if (!visited.contains(new Pair<>(rowVerticalUp, col))) {
                    visited.add(new Pair<>(rowVerticalUp, col));
                    verticalSides++;
                }
            }

            // down
            while (fence.contains(new Pair<>(++rowVerticalDown, col))) {
                if (!visited.contains(new Pair<>(rowVerticalDown, col))) {
                    visited.add(new Pair<>(rowVerticalDown, col));
                    verticalSides++;
                }
            }

            if (horizontalSides > 0) {
                sides++;
            }

            if (verticalSides > 0) {
                sides++;
            }

            if (horizontalSides == 0 && verticalSides == 0) {
                sides++;
            }
        }

        return sides;
    }

}
