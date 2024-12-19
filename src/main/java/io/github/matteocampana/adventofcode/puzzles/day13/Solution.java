package io.github.matteocampana.adventofcode.puzzles.day13;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Solution {

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day13/input.txt");
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
            BigDecimal ax = new BigDecimal(buttonA[0].split("\\+")[1].trim());
            BigDecimal ay = new BigDecimal(buttonA[1].split("\\+")[1].trim());

            // Parse Button B
            String[] buttonB = input.get((int) i + 1).split(", ");
            BigDecimal bx = new BigDecimal(buttonB[0].split("\\+")[1].trim());
            BigDecimal by = new BigDecimal(buttonB[1].split("\\+")[1].trim());

            // Parse Prize
            String[] prize = input.get((int) i + 2).split(", ");
            BigDecimal px = new BigDecimal(prize[0].split("=")[1].trim());
            BigDecimal py = new BigDecimal(prize[1].split("=")[1].trim());

            // Add the machine to the list
            machines.add(new ClawMachine(ax, ay, bx, by, px, py));
        }
        System.out.println("[PART 1] min token to win: " + minTokensToWin(machines));

        System.out.println("[PART 1] min token to win test: " + minTokensToWin2(machines));

        List<ClawMachine> machines2 = new ArrayList<>(machines);

        for (ClawMachine machine : machines2) {
            machine.px = machine.px.add(new BigDecimal(10000000000000L));
            machine.py = machine.py.add(new BigDecimal(10000000000000L));
        }

        System.out.println("[PART 2] min token to win: " + minTokensToWinPart2(machines2));

    }

    static class ClawMachine {
        BigDecimal ax, ay, bx, by; // Button A and B movements
        BigDecimal px, py; // Prize location

        ClawMachine(BigDecimal ax, BigDecimal ay, BigDecimal bx, BigDecimal by, BigDecimal px, BigDecimal py) {
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

    private static BigDecimal minTokensToWin(List<ClawMachine> machines) {
        BigDecimal totalTokens = BigDecimal.ZERO;

        for (ClawMachine machine : machines) {
            BigDecimal minTokens = BigDecimal.valueOf(Long.MAX_VALUE);
            boolean canWin = false;

            // Iterate over all possible combinations of A and B presses (0 to 100)
            for (int a = 0; a <= 100; a++) {
                for (int b = 0; b <= 100; b++) {
                    BigDecimal x = machine.ax.multiply(BigDecimal.valueOf(a))
                            .add(machine.bx.multiply(BigDecimal.valueOf(b)));
                    BigDecimal y = machine.ay.multiply(BigDecimal.valueOf(a))
                            .add(machine.by.multiply(BigDecimal.valueOf(b)));

                    // Check if the claw aligns with the prize
                    if (Objects.equals(x, machine.px) && Objects.equals(y, machine.py)) {
                        canWin = true;
                        BigDecimal tokens = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(3))
                                .add(BigDecimal.valueOf(b)); // Cost of tokens (3 for A, 1 for B)
                        minTokens = minTokens.min(tokens);
                    }
                }
            }

            // Add the cost of winning this machine, if possible
            if (canWin) {
                totalTokens = totalTokens.add(BigDecimal.valueOf(minTokens.longValue()));
            }
        }

        return totalTokens;
    }

    private static BigDecimal minTokensToWin2(List<ClawMachine> machines) {
        BigDecimal totalTokens = BigDecimal.ZERO;

        for (ClawMachine machine : machines) {
            // Solve the system of equations for a and b
            BigDecimal determinant = machine.ax.multiply(machine.by).subtract(machine.ay.multiply(machine.bx));

            if (determinant.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal a = machine.px.multiply(machine.by).subtract(machine.py.multiply(machine.bx))
                        .divide(determinant, java.math.RoundingMode.HALF_UP);
                BigDecimal b = machine.ax.multiply(machine.py).subtract(machine.ay.multiply(machine.px))
                        .divide(determinant, java.math.RoundingMode.HALF_UP);

                BigDecimal x = machine.ax.multiply(a)
                        .add(machine.bx.multiply(b));
                BigDecimal y = machine.ay.multiply(a)
                        .add(machine.by.multiply(b));

                if (Objects.equals(x, machine.px) && Objects.equals(y, machine.py)
                        && a.compareTo(BigDecimal.valueOf(100)) <= 0 && b.compareTo(BigDecimal.valueOf(100)) <= 0) {
                    totalTokens = totalTokens.add(a.multiply(BigDecimal.valueOf(3)).add(b)); // Cost of tokens (3 for A,
                                                                                             // 1 for B)
                }
            }
        }
        return totalTokens;
    }

    private static BigDecimal minTokensToWinPart2(List<ClawMachine> machines) {
        BigDecimal totalTokens = BigDecimal.ZERO;

        for (ClawMachine machine : machines) {
            // Solve the system of equations for a and b
            BigDecimal determinant = machine.ax.multiply(machine.by).subtract(machine.ay.multiply(machine.bx));

            if (determinant.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal a = machine.px.multiply(machine.by).subtract(machine.py.multiply(machine.bx))
                        .divide(determinant, java.math.RoundingMode.HALF_UP);
                BigDecimal b = machine.ax.multiply(machine.py).subtract(machine.ay.multiply(machine.px))
                        .divide(determinant, java.math.RoundingMode.HALF_UP);

                BigDecimal x = machine.ax.multiply(a)
                        .add(machine.bx.multiply(b));
                BigDecimal y = machine.ay.multiply(a)
                        .add(machine.by.multiply(b));

                if (Objects.equals(x, machine.px) && Objects.equals(y, machine.py)) {
                    totalTokens = totalTokens.add(a.multiply(BigDecimal.valueOf(3)).add(b)); // Cost of tokens (3 for A, 1 for B)
                }
            }
        }
        return totalTokens;
    }

}
