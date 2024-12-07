package io.github.matteocampana.adventofcode.puzzles.day7;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Solution {

    public static void main(String[] args) {

        Path inputFilePath = Paths.get("input/day7/input.txt");
        long totalCalibrationResultPart1 = 0;
        long totalCalibrationResultPart2 = 0;

        try (InputStream inputStream = Solution.class.getClassLoader()
                .getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {

                    String[] parts = line.split(": ");
                    long target = Long.parseLong(parts[0]);
                    int[] numbers = Arrays.stream(parts[1].split(" ")).mapToInt(Integer::parseInt).toArray();

                    if (canAchieveTargetPart1(numbers, target)) {
                        totalCalibrationResultPart1 += target;
                    }

                    if (canAchieveTargetPart2(numbers, target)) {
                        totalCalibrationResultPart2 += target;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("[Part 1]Total Calibration Result: " + totalCalibrationResultPart1);
        System.out.println("[Part 2]Total Calibration Result: " + totalCalibrationResultPart2);
    }

    // Function to check if the target value can be achieved with any combination of
    // + and *
    private static boolean canAchieveTargetPart2(int[] numbers, long target) {
        return evaluateExpressionsPart2(numbers, 0, numbers[0], target);
    }

    private static boolean canAchieveTargetPart1(int[] numbers, long target) {
        return evaluateExpressionsPart1(numbers, 0, numbers[0], target);
    }

    // Recursive function to evaluate all possible combinations of + and *
    private static boolean evaluateExpressionsPart1(int[] numbers, int index, long currentValue, long target) {
        // Base case: if we've used all numbers
        if (index == numbers.length - 1) {
            return currentValue == target;
        }

        // Try addition
        if (evaluateExpressionsPart2(numbers, index + 1, currentValue + numbers[index + 1], target)) {
            return true;
        }

        // Try multiplication
        if (evaluateExpressionsPart2(numbers, index + 1, currentValue * numbers[index + 1], target)) {
            return true;
        }

        return false;
    }

    // Recursive function to evaluate all possible combinations of + and *
    private static boolean evaluateExpressionsPart2(int[] numbers, int index, long currentValue, long target) {
        // Base case: if we've used all numbers
        if (index == numbers.length - 1) {
            return currentValue == target;
        }

        // Try addition
        if (evaluateExpressionsPart2(numbers, index + 1, currentValue + numbers[index + 1], target)) {
            return true;
        }

        // Try multiplication
        if (evaluateExpressionsPart2(numbers, index + 1, currentValue * numbers[index + 1], target)) {
            return true;
        }

        // Try concatenation || example a || b = ab
        if (evaluateExpressionsPart2(numbers, index + 1,
                Long.parseLong(Long.toString(currentValue) + Long.toString(numbers[index + 1])), target)) {
            return true;
        }

        return false;
    }

}
