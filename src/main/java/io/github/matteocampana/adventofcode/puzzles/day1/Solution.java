package io.github.matteocampana.adventofcode.puzzles.day1;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Solution {

    private static void Part1(List<Integer> input1, List<Integer> input2) {
        input1.sort(Integer::compareTo);
        input2.sort(Integer::compareTo);

        // zip the two lists

        assert input1.size() == input2.size();
        int totalDistance = 0;

        for (int i = 0; i < input1.size(); i++) {
            totalDistance += Math.abs(input1.get(i) - input2.get(i));
        }
        System.out.println("Total distance: " + totalDistance);
    }

    private static void Part2(List<Integer> input1, List<Integer> input2) {
        HashMap<Integer, Integer> dict = new HashMap<>();

        for (Integer i : input2) {
            dict.put(i, dict.getOrDefault(i, 0) + 1);
        }

        int similarityScore = 0;

        for (Integer i : input1) {
            if (dict.containsKey(i)) {
                similarityScore += i * dict.get(i);
            }
        }

        System.out.println("Similarity score: " + similarityScore);
    }

    public static void main(String[] args) {
        // Read input from file

        List<Integer> input1 = new ArrayList<>();
        List<Integer> input2 = new ArrayList<>();

        int value1;
        int value2;

        Path inputFilePath = Paths.get("/input/day1/input.txt");

        try (InputStream inputStream = Solution.class.getClassLoader().getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty())
                        continue; // Skip empty lines
                    String[] lineArray = line.trim().split("\\s+");
                    value1 = Integer.parseInt(lineArray[0].trim());
                    value2 = Integer.parseInt(lineArray[1].trim());
                    input1.add(value1);
                    input2.add(value2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Part1(input1, input2);

        Part2(input1, input2);

    }
}
