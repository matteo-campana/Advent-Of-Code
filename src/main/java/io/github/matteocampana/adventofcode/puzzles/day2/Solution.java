package io.github.matteocampana.adventofcode.puzzles.day2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Solution {

    private static boolean checkReport(String report) {

        boolean bSafe = true;

        String[] lineArray = report.trim().split("\\s+");
        for (int i = 0; i < lineArray.length - 1; i++) {
            if (Math.abs(Integer.parseInt(lineArray[i]) - Integer.parseInt(lineArray[i + 1])) > 3 ||
                    Integer.parseInt(lineArray[i]) == Integer.parseInt(lineArray[i + 1])) {
                bSafe = false;
                System.out.println("Not safe: " + lineArray[i] + " " + lineArray[i + 1]);
                break;
            }

            if (i > 0) {
                if ((Integer.parseInt(lineArray[i - 1]) > Integer.parseInt(lineArray[i]) &&
                        Integer.parseInt(lineArray[i]) < Integer.parseInt(lineArray[i + 1])) ||
                        (Integer.parseInt(lineArray[i - 1]) < Integer.parseInt(lineArray[i]) &&
                                Integer.parseInt(lineArray[i]) > Integer.parseInt(lineArray[i + 1]))) {
                    bSafe = false;
                    System.out.println("Not safe: " + lineArray[i - 1] + " " + lineArray[i] + " " + lineArray[i + 1]);
                    break;
                }
            }
        }
        return bSafe;
    }

    public static void main(String[] args) {

        Path inputFilePath = Paths.get("/input/day2/input.txt");

        int countSafeReports = 0;

        try (InputStream inputStream = io.github.matteocampana.adventofcode.puzzles.day1.Solution.class.getClassLoader()
                .getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;

                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty())
                        continue; // Skip empty lines

                    if (checkReport(line)) {
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
