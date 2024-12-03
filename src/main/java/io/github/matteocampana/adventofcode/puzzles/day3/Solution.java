package io.github.matteocampana.adventofcode.puzzles.day3;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

    private static List<String> extractValidMultiplicationsPart1(String line) {

        String regex = "mul\\(\\d{1,3},\\d{1,3}\\)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(line);
        List<String> validMultiplications = new ArrayList<>();

        while (matcher.find()) {
            // System.out.println("Found: " + matcher.group());
            validMultiplications.add(matcher.group());
        }

        return validMultiplications;
    }

    private static List<String> extractValidMultiplicationsPart2(String line) {

        boolean bDisable = false;
        List<String> validMultiplications = new ArrayList<>();
        int start = 0;

        char[] arr = line.toCharArray();
        for (int i = 0; i < arr.length; i++) {

            // if the current character is a 'd' and the next 3 characters are 'do()'
            if (arr[i] == 'd' &&  i+3 < arr.length && "do()".equals(line.substring(i, i + "do()".length()))) {
                if (bDisable) {
                    bDisable = false;
                    start = i + "do()".length();
                }
            }

            // if the current character is a 'd' and the next 6 characters are 'don't()'
            if (arr[i] == 'd' && i+6 < arr.length && "don't()".equals(line.substring(i, i + "don't()".length()))) {
                if (!bDisable) {
                    // extract all valid multiplications from the block
                    String block = line.substring(start, i);
                    validMultiplications.addAll(extractValidMultiplicationsPart1(block));
                    bDisable = true;
                    start = i + "don't()".length();
                }
            }
        }

        // If the last block is not disabled, process it
        if (!bDisable) {
            String block = line.substring(start);
            validMultiplications.addAll(extractValidMultiplicationsPart1(block));
        }

        return validMultiplications;
    }

    private static int calculateMultiplication(String multiplication) {
        String[] numbers = multiplication.substring(4, multiplication.length() - 1).split(",");
        return Integer.parseInt(numbers[0]) * Integer.parseInt(numbers[1]);
    }

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("/input/day3/input.txt");
        int multiplicationSumPart1 = 0;
        int multiplicationSumPart2 = 0;
        List<String> validMultiplications;

        StringBuilder content = new StringBuilder();

        try (InputStream inputStream = Solution.class.getClassLoader().getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    validMultiplications = extractValidMultiplicationsPart1(line);
                    multiplicationSumPart1 += validMultiplications.stream().map(Solution::calculateMultiplication)
                            .reduce(0, Integer::sum);
                    validMultiplications = extractValidMultiplicationsPart2(line);
                    multiplicationSumPart2 += validMultiplications.stream().map(Solution::calculateMultiplication)
                            .reduce(0, Integer::sum);
                    content.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        validMultiplications = extractValidMultiplicationsPart1(content.toString());
        validMultiplications = extractValidMultiplicationsPart2(content.toString());

        if (multiplicationSumPart2 > multiplicationSumPart1) {
            throw new RuntimeException("Multiplication sum for part 2 is greater than part 1");
        }
        System.out.println("Solution line by line:");
        System.out.println("[PART 1] The sum of all valid multiplications is: " + multiplicationSumPart1);
        System.out.println("[PART 2] The sum of all valid multiplications is: " + multiplicationSumPart2);


        validMultiplications = extractValidMultiplicationsPart1(content.toString());
        multiplicationSumPart1 = validMultiplications.stream().map(Solution::calculateMultiplication)
                .reduce(0, Integer::sum);
        validMultiplications = extractValidMultiplicationsPart2(content.toString());
        multiplicationSumPart2 = validMultiplications.stream().map(Solution::calculateMultiplication)
                .reduce(0, Integer::sum);

        
        System.out.println("#".repeat(150));

        if (multiplicationSumPart2 > multiplicationSumPart1) {
            throw new RuntimeException("Multiplication sum for part 2 is greater than part 1");
        }

        System.out.println("Solution from entire content:");
        System.out.println("[PART 1] The sum of all valid multiplications is: " + multiplicationSumPart1);
        System.out.println("[PART 2] The sum of all valid multiplications is: " + multiplicationSumPart2);

    }
}
