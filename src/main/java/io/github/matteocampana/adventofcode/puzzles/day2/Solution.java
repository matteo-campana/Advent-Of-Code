package io.github.matteocampana.adventofcode.puzzles.day2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Solution {

    private static boolean checkReportPart1(String report) {
        String[] lineArray = report.trim().split("\\s+");
        for (int i = 0; i < lineArray.length - 1; i++) {
            int current = Integer.parseInt(lineArray[i]);
            int next = Integer.parseInt(lineArray[i + 1]);
            if (Math.abs(current - next) > 3 || current == next) {
                System.out.println("Not safe: " + current + " " + next);
                return false;
            }
            if (i > 0) {
                int previous = Integer.parseInt(lineArray[i - 1]);
                if ((previous > current && current < next) || (previous < current && current > next)) {
                    System.out.println("Not safe: " + previous + " " + current + " " + next);
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkReportPart2(String report) {
        String[] lineArray = report.trim().split("\\s+");
        for (int i = 0; i < lineArray.length - 1; i++) {
            int current = Integer.parseInt(lineArray[i]);
            int next = Integer.parseInt(lineArray[i + 1]);
            if (Math.abs(current - next) > 3 || current == next) {
                System.out.println("Not safe: " + current + " " + next);
                // Try to sanitize the report by removing the current or next element
                for (int j = i; j <= i + 1; j++) {
                    ArrayList<String> sanitizedReport = new ArrayList<>(Arrays.asList(lineArray));
                    sanitizedReport.remove(j);
                    if (checkReportPart1(String.join(" ", sanitizedReport))) {
                        System.out.println("Sanitized report: " + String.join(" ", sanitizedReport));
                        return true;
                    }
                }
                return false;

            }
            if (i > 0) {
                int previous = Integer.parseInt(lineArray[i - 1]);
                if ((previous > current && current < next) || (previous < current && current > next)) {
                    System.out.println("Not safe: " + previous + " " + current + " " + next);
                    for (int j = i - 1; j <= i + 1; j++) {
                        ArrayList<String> sanitizedReport = new ArrayList<>(Arrays.asList(lineArray));
                        sanitizedReport.remove(j);
                        if (checkReportPart1(String.join(" ", sanitizedReport))) {
                            System.out.println("Sanitized report: " + String.join(" ", sanitizedReport));
                            return true;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("/input/day2/input.txt");
        int countSafeReportsPart1 = 0;
        int countSafeReportsPart2 = 0;

        try (InputStream inputStream = Solution.class.getClassLoader().getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty() && checkReportPart1(line)) {
                        countSafeReportsPart1++;
                    }
                    if (!line.trim().isEmpty() && checkReportPart2(line)) {
                        countSafeReportsPart2++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (countSafeReportsPart2 < countSafeReportsPart1)
            throw new RuntimeException("Part 2 should have more or equal safe reports than part 1");
        System.out.println("Count safe reports part 1: " + countSafeReportsPart1);
        System.out.println("Count safe reports part 2: " + countSafeReportsPart2);
    }
}
