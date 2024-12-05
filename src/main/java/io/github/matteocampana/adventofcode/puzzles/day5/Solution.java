package io.github.matteocampana.adventofcode.puzzles.day5;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Solution {
    private static List<Rule> parsedRules = new ArrayList<>();

    public static void main(String[] args) {

        Path inputFilePath = Paths.get("input/day5/input.txt");
        List<String> rules = new ArrayList<>();
        List<List<Integer>> content = new ArrayList<>();
        boolean switchFromRulesToContent = false;

        try (InputStream inputStream = io.github.matteocampana.adventofcode.puzzles.day4.Solution.class.getClassLoader()
                .getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isEmpty()) {
                        switchFromRulesToContent = true;
                        continue;
                    }
                    if (!switchFromRulesToContent) {
                        rules.add(line);
                    } else {
                        content.add(Arrays.stream(line.split(","))
                                .map(Integer::parseInt)
                                .toList());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String rule : rules) {
            parsedRules.add(parseRule(rule));
        }

        int sumCorrectMiddlePages = 0;
        int sumReorderedMiddlePages = 0;

        // Process each update
        for (List<Integer> update : content) {
            if (isCorrectOrder(update, rules)) {
                // If the update is in correct order, add its middle page to the sum
                sumCorrectMiddlePages += getMiddlePage(update);
            } else {
                // If not, reorder the update and add its middle page to the sum
                List<Integer> reorderedUpdate = reorderUpdate(update, rules);
                sumReorderedMiddlePages += getMiddlePage(reorderedUpdate);
            }
        }

        // Print the results
        System.out.println("Sum of middle pages of correctly ordered updates: " + sumCorrectMiddlePages);
        System.out.println("Sum of middle pages of reordered updates: " + sumReorderedMiddlePages);
    }

    // Check if the update is in the correct order based on the rules
    private static boolean isCorrectOrder(List<Integer> update, List<String> rules) {
        for (Rule rule : parsedRules) {
            if (update.contains(rule.x) && update.contains(rule.y)) {
                if (update.indexOf(rule.x) > update.indexOf(rule.y)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Reorder the update based on the rules
    private static List<Integer> reorderUpdate(List<Integer> update, List<String> rules) {
        List<Integer> orderedUpdate = new ArrayList<>(update);
        orderedUpdate.sort((a, b) -> {
            for (Rule rule : parsedRules) {
                if ((a == rule.x && b == rule.y) || (a == rule.y && b == rule.x)) {
                    return a == rule.x ? -1 : 1;
                }
            }
            return 0;
        });
        return orderedUpdate;
    }

    private static Rule parseRule(String rule) {
        String[] parts = rule.split("\\|");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        return new Rule(x, y);
    }

    private static class Rule {
        int x, y;

        Rule(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Get the middle page of the update
    private static int getMiddlePage(List<Integer> update) {
        return update.get(update.size() / 2);
    }
}
