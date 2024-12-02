package io.github.matteocampana.adventofcode.puzzles.day2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Solution {

    private static boolean checkReport(String report) {
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

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("/input/day2/input.txt");
        int countSafeReports = 0;

        try (InputStream inputStream = Solution.class.getClassLoader().getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.trim().isEmpty() && checkReport(line)) {
                        countSafeReports++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Count safe reports: " + countSafeReports);
    }
}
