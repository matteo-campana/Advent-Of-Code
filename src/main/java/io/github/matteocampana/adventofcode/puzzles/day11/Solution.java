package io.github.matteocampana.adventofcode.puzzles.day11;

import kotlin.Pair;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    // Map that uses a tuple of stone and blink as key and the number of generated
    // stones as value
    private static final Map<Pair<String, Integer>, BigInteger> stonesMap = new HashMap<>();

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day11/input.txt");
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

        System.out.println("Test 1: " + Part1(Arrays.asList("125", "17"), 25).size());

        int numberOfStonesTest = Part2(Arrays.asList("125", "17"), 25);

        System.out.println("Test 2: " + numberOfStonesTest);

        System.out
                .println("Part 1: " + Part1(Arrays.asList(input.getFirst().split("\\s+")), 25).size() + ", 25 blinks");

        // int numberOfStones = Part2(Arrays.asList(input.getFirst().split("\\s+")),
        // 25);

        int numberOfStones = Part2(Arrays.asList(input.getFirst().split("\\s+")), 25);

        System.out.println("Part 2: " + numberOfStones + ", 25 blinks");

        numberOfStones = Part2(Arrays.asList(input.getFirst().split("\\s+")), 75);

        System.out.println("Part 2: " + numberOfStones + ", 75 blinks");
    }

    private static String transformStone(String stone) {
        // If the stone is engraved with the number 0, it is replaced by a stone
        // engraved
        // with the number 1.
        if (stone.equals("0"))
            return "1";

        // If the stone is engraved with a number that has an even number of digits,
        // it is replaced by two stones. The left half of the digits are engraved on the
        // new left stone, and the right half of the digits are engraved on the new
        // right stone. (The new numbers don't keep extra leading zeroes: 1000 would
        // become stones 10 and 0.)

        try {
            if (stone.length() % 2 == 0) {
                int half = stone.length() / 2;
                // return Long.parseLong(stone.substring(0, half)) + " " +
                // Long.parseLong(stone.substring(half));
                return stone.substring(0, half).replaceFirst("^0+(?!$)", "") + " "
                        + stone.substring(half).replaceFirst("^0+(?!$)", "");
            }

            // If none of the other rules apply, the stone is replaced by a new stone; the
            // old stone's number multiplied by 2024 is engraved on the new stone.
            return String.valueOf(new BigInteger(stone).multiply(BigInteger.valueOf(2024)));
        } catch (NumberFormatException e) {
            // Handle the case where the stone string is not a valid number
            throw new IllegalArgumentException("Invalid stone value: " + stone, e);
        }
    }

    private static List<String> Part1(List<String> stones, int blink) {
        if (blink == 0)
            return stones;

        List<String> newStones = new ArrayList<>();

        for (String stone : stones) {
            newStones.addAll(Arrays.asList(transformStone(stone).split("\\s+")));
        }

        // System.out.println("\nBlink: " + blink);
        // System.out.println("Stones: " + newStones);
        // System.out.println("Number of Stones: " + newStones.size());

        return Part1(newStones, --blink);
    }

    private static int Part2(List<String> stones, int blink) {
        if (blink == 0)
            return stones.size();

        BigInteger result = BigInteger.ZERO;
        BigInteger tmpResult;
        for (String stone : stones) {
            if (stonesMap.containsKey(new Pair<>(stone, blink))) {
                result = result.add(stonesMap.get(new Pair<>(stone, blink)));
            } else {
                tmpResult = BigInteger.valueOf(Part2(Arrays.asList(transformStone(stone).split("\\s+")), blink - 1));
                stonesMap.put(new Pair<>(stone, blink), tmpResult);
                result = result.add(tmpResult);
            }
        }

        return result.intValue();
    }

}
