package io.github.matteocampana.adventofcode.puzzles.day4;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Solution {

    public static void main(String[] args) {
        // solutionPart1();
        solutionPart2();
    }

    private static void solutionPart2() {
        Path inputFilePath = Paths.get("input/day4/input.txt");

        char[][] char_arr = new char[3][];
        int lineIndex = 0;
        boolean[][] xmas_placeholders = new boolean[3][];
        StringBuilder output_string = new StringBuilder();

        int xmas_counter = 0;

        try (InputStream inputStream = Solution.class.getClassLoader()
                .getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    char_arr[0] = char_arr[1];
                    char_arr[1] = char_arr[2];
                    char_arr[2] = line.toCharArray();

                    xmas_placeholders[0] = xmas_placeholders[1];
                    xmas_placeholders[1] = xmas_placeholders[2];
                    xmas_placeholders[2] = new boolean[char_arr[2].length];

                    lineIndex++;
                    if (lineIndex < 3) {
                        continue;
                    }

                    char[][] patterns = new char[4][];

                    // Define patterns
                    // S.M
                    // .A.
                    // S.M

                    patterns[0] = "S.M.A.S.M".toCharArray();

                    // S.S
                    // .A.
                    // M.M

                    patterns[1] = "S.S.A.M.M".toCharArray();

                    // M.S
                    // .A.
                    // M.S

                    patterns[2] = "M.S.A.M.S".toCharArray();

                    // M.M
                    // .A.
                    // S.S

                    patterns[3] = "M.M.A.S.S".toCharArray();

                    for (int i = 0; i <= char_arr[0].length - 3; i++) {
                        for (char[] pattern : patterns) {
                            boolean match = true;
                            for (int j = 0; j < pattern.length; j++) {
                                if (pattern[j] != '.' && pattern[j] != char_arr[j / 3][i + j % 3]) {
                                    match = false;
                                    break;
                                }
                            }
                            if (match) {
                                xmas_counter++;
                                for (int j = 0; j < pattern.length; j++) {
                                    if (pattern[j] != '.') {
                                        xmas_placeholders[j / 3][i + j % 3] = true;
                                    }
                                }
                            }
                        }
                    }

                    // update output string
                    for (int i = 0; i < xmas_placeholders[0].length; i++) {
                        if (xmas_placeholders[0][i]) {
                            output_string.append(char_arr[0][i]);
                        } else {
                            output_string.append('.');
                        }
                    }
                    output_string.append("\n");
                }

                // update the last 2 lines

                for (int i = 1; i < xmas_placeholders.length; i++) {
                    for (int j = 0; j < xmas_placeholders[i].length; j++) {
                        if (xmas_placeholders[i][j]) {
                            output_string.append(char_arr[i][j]);
                        } else {
                            output_string.append('.');
                        }
                    }
                    output_string.append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("XMAS counter: " + xmas_counter);
        System.out.println("\n\n" + "#".repeat(100) + "\n\n");
        System.out.println(output_string.toString());
    }

    private static void solutionPart1() {
        Path inputFilePath = Paths.get("input/day4/input.txt");

        char[][] char_arr = new char[4][];
        int lineIndex = 0;
        boolean[][] xmas_placeholders = new boolean[4][];
        StringBuilder output_string = new StringBuilder();

        int xmas_counter = 0;

        try (InputStream inputStream = Solution.class.getClassLoader()
                .getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {

                    char_arr[0] = char_arr[1];
                    char_arr[1] = char_arr[2];
                    char_arr[2] = char_arr[3];
                    char_arr[3] = line.toCharArray();

                    xmas_placeholders[0] = xmas_placeholders[1];
                    xmas_placeholders[1] = xmas_placeholders[2];
                    xmas_placeholders[2] = xmas_placeholders[3];
                    xmas_placeholders[3] = new boolean[char_arr[3].length];

                    lineIndex++;
                    if (lineIndex < 4) {
                        continue;
                    }

                    for (int i = 0; i < char_arr[0].length; i++) {
                        // horizontal
                        if (i <= char_arr[0].length - "XMAS".length()
                                && char_arr[0][i] == 'X'
                                && char_arr[0][i + 1] == 'M'
                                && char_arr[0][i + 2] == 'A'
                                && char_arr[0][i + 3] == 'S') {
                            xmas_counter++;
                            xmas_placeholders[0][i] = true;
                            xmas_placeholders[0][i + 1] = true;
                            xmas_placeholders[0][i + 2] = true;
                            xmas_placeholders[0][i + 3] = true;
                        }
                        // reverse horizontal
                        if (i >= "XMAS".length() - 1
                                && char_arr[0][i] == 'X'
                                && char_arr[0][i - 1] == 'M'
                                && char_arr[0][i - 2] == 'A'
                                && char_arr[0][i - 3] == 'S') {
                            xmas_counter++;
                            xmas_placeholders[0][i] = true;
                            xmas_placeholders[0][i - 1] = true;
                            xmas_placeholders[0][i - 2] = true;
                            xmas_placeholders[0][i - 3] = true;
                        }
                        // vertical
                        if (char_arr[0][i] == 'X'
                                && char_arr[1][i] == 'M'
                                && char_arr[2][i] == 'A'
                                && char_arr[3][i] == 'S') {
                            xmas_counter++;
                            xmas_placeholders[0][i] = true;
                            xmas_placeholders[1][i] = true;
                            xmas_placeholders[2][i] = true;
                            xmas_placeholders[3][i] = true;
                        }
                        // reverse vertical
                        if (char_arr[3][i] == 'X'
                                && char_arr[2][i] == 'M'
                                && char_arr[1][i] == 'A'
                                && char_arr[0][i] == 'S') {
                            xmas_counter++;
                            xmas_placeholders[3][i] = true;
                            xmas_placeholders[2][i] = true;
                            xmas_placeholders[1][i] = true;
                            xmas_placeholders[0][i] = true;
                        }
                        // diagonal
                        if (i <= char_arr[0].length - "XMAS".length()
                                && char_arr[0][i] == 'X'
                                && char_arr[1][i + 1] == 'M'
                                && char_arr[2][i + 2] == 'A'
                                && char_arr[3][i + 3] == 'S') {
                            xmas_counter++;
                            xmas_placeholders[0][i] = true;
                            xmas_placeholders[1][i + 1] = true;
                            xmas_placeholders[2][i + 2] = true;
                            xmas_placeholders[3][i + 3] = true;
                        }
                        // inverse diagonal
                        if (i >= "XMAS".length() - 1
                                && char_arr[3][i] == 'X'
                                && char_arr[2][i - 1] == 'M'
                                && char_arr[1][i - 2] == 'A'
                                && char_arr[0][i - 3] == 'S') {
                            xmas_counter++;
                            xmas_placeholders[3][i] = true;
                            xmas_placeholders[2][i - 1] = true;
                            xmas_placeholders[1][i - 2] = true;
                            xmas_placeholders[0][i - 3] = true;
                        }
                        // reverse diagonal
                        if (i >= "XMAS".length() - 1
                                && char_arr[0][i] == 'X'
                                && char_arr[1][i - 1] == 'M'
                                && char_arr[2][i - 2] == 'A'
                                && char_arr[3][i - 3] == 'S') {
                            xmas_counter++;
                            xmas_placeholders[0][i] = true;
                            xmas_placeholders[1][i - 1] = true;
                            xmas_placeholders[2][i - 2] = true;
                            xmas_placeholders[3][i - 3] = true;
                        }
                        // inverse reverse diagonal
                        if (i <= char_arr[0].length - "XMAS".length()
                                && char_arr[3][i] == 'X'
                                && char_arr[2][i + 1] == 'M'
                                && char_arr[1][i + 2] == 'A'
                                && char_arr[0][i + 3] == 'S') {
                            xmas_counter++;
                            xmas_placeholders[3][i] = true;
                            xmas_placeholders[2][i + 1] = true;
                            xmas_placeholders[1][i + 2] = true;
                            xmas_placeholders[0][i + 3] = true;
                        }

                    }
                    for (int i = 0; i < xmas_placeholders[0].length; i++) {
                        if (xmas_placeholders[0][i]) {
                            output_string.append(char_arr[0][i]);
                        } else {
                            output_string.append('.');
                        }
                    }
                    output_string.append("\n");
                }

                // check the last 3 lines horizontally
                for (int i = 1; i < char_arr.length; i++) {
                    for (int j = 0; j < char_arr[i].length; j++) {
                        // horizontal
                        if (j <= char_arr[i].length - "XMAS".length()
                                && char_arr[i][j] == 'X'
                                && char_arr[i][j + 1] == 'M'
                                && char_arr[i][j + 2] == 'A'
                                && char_arr[i][j + 3] == 'S') {
                            xmas_counter++;
                            xmas_placeholders[i][j] = true;
                            xmas_placeholders[i][j + 1] = true;
                            xmas_placeholders[i][j + 2] = true;
                            xmas_placeholders[i][j + 3] = true;
                        }

                        // reverse horizontal
                        if (j >= "XMAS".length() - 1
                                && char_arr[i][j] == 'X'
                                && char_arr[i][j - 1] == 'M'
                                && char_arr[i][j - 2] == 'A'
                                && char_arr[i][j - 3] == 'S') {
                            xmas_counter++;
                            xmas_placeholders[i][j] = true;
                            xmas_placeholders[i][j - 1] = true;
                            xmas_placeholders[i][j - 2] = true;
                            xmas_placeholders[i][j - 3] = true;
                        }
                    }
                }

                // finish the last lines

                for (int k = 1; k < xmas_placeholders.length; k++) {
                    for (int j = 0; j < xmas_placeholders[k].length; j++) {
                        if (xmas_placeholders[k][j]) {
                            output_string.append(char_arr[k][j]);
                        } else {
                            output_string.append('.');
                        }
                    }
                    output_string.append("\n");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("XMAS counter: " + xmas_counter);
        System.out.println("\n\n" + "#".repeat(100) + "\n\n");
        System.out.println(output_string.toString());
    }

}
