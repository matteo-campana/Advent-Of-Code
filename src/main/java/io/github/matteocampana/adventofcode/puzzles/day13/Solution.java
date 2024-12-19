package io.github.matteocampana.adventofcode.puzzles.day13;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Solution {

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day13/test.txt");
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

        List<ClawMachine> machines = new ArrayList<>();

        for (long i = 0; i < input.size(); i += 4) {
            String[] buttonA = input.get((int) i).split(", ");
            long ax = Integer.parseInt(buttonA[0].split("\\+")[1].trim());
            long ay = Integer.parseInt(buttonA[1].split("\\+")[1].trim());

            // Parse Button B
            String[] buttonB = input.get((int) i + 1).split(", ");
            long bx = Integer.parseInt(buttonB[0].split("\\+")[1].trim());
            long by = Integer.parseInt(buttonB[1].split("\\+")[1].trim());

            // Parse Prize
            String[] prize = input.get((int) i + 2).split(", ");
            long px = Integer.parseInt(prize[0].split("=")[1].trim());
            long py = Integer.parseInt(prize[1].split("=")[1].trim());

            // Add the machine to the list
            machines.add(new ClawMachine(ax, ay, bx, by, px, py));
        }
        System.out.println("[PART 1] min token to win: " + minTokensToWin(machines));

        System.out.println("[PART 1] min token to win test: " + minTokensToWinTest(machines));

        // List<ClawMachine> machines2 = new ArrayList<>(machines);
        // for (ClawMachine machine : machines2) {
        // machine.px += 10000000000000L;
        // machine.py += 10000000000000L;
        // }

    }

    static class ClawMachine {
        long ax, ay, bx, by; // Button A and B movements
        long px, py; // Prize location

        ClawMachine(long ax, long ay, long bx, long by, long px, long py) {
            this.ax = ax;
            this.ay = ay;
            this.bx = bx;
            this.by = by;
            this.px = px;
            this.py = py;
        }

        @Override
        public String toString() {
            return "ClawMachine{" +
                    "ax=" + ax +
                    ", ay=" + ay +
                    ", bx=" + bx +
                    ", by=" + by +
                    ", px=" + px +
                    ", py=" + py +
                    '}';
        }
    }

    private static long minTokensToWin(List<ClawMachine> machines) {
        long totalTokens = 0;

        for (ClawMachine machine : machines) {
            long minTokens = Integer.MAX_VALUE;
            boolean canWin = false;

            // Iterate over all possible combinations of A and B presses (0 to 100)
            for (long a = 0; a <= 100; a++) {
                for (long b = 0; b <= 100; b++) {
                    long x = a * machine.ax + b * machine.bx;
                    long y = a * machine.ay + b * machine.by;

                    // Check if the claw aligns with the prize
                    if (x == machine.px && y == machine.py) {
                        canWin = true;
                        long tokens = a * 3 + b; // Cost of tokens (3 for A, 1 for B)
                        minTokens = Math.min(minTokens, tokens);
                    }
                }
            }

            // Add the cost of winning this machine, if possible
            if (canWin) {
                totalTokens += minTokens;
            }
        }

        return totalTokens;
    }

    private static long minTokensToWinTest(List<ClawMachine> machines) {
        long totalTokens = 0L;

        for (ClawMachine machine : machines) {
            // Solve the system of equations for a and b
            long determinant = machine.ax * machine.by - machine.ay * machine.bx;

            if (determinant != 0) {
                long a = (machine.px * machine.by - machine.py * machine.bx) / determinant;
                long b = (machine.ax * machine.py - machine.ay * machine.px) / determinant;

                // Check if a and b are non-negative integers
                if (a >= 0 && b >= 0 && a == Math.floor(a) && b == Math.floor(b) && a <= 100 && b <= 100) {
                    totalTokens += (int) a * 3 + (int) b; // Cost of tokens (3 for A, 1 for B)
                }
            }
        }
        return totalTokens;
    }

}
