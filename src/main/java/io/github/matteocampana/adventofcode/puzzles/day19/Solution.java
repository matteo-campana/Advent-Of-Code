package io.github.matteocampana.adventofcode.puzzles.day19;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day19/input.txt");
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

        List<String> targetPatterns = new ArrayList<>(Arrays.stream(input.get(0).split(", ")).toList());
        input.removeFirst(); // remove the target pattern line
        input.removeFirst(); // remove the blank line

        List<String> designs = new ArrayList<>(input);

        // Count possible designs
        int resultPart1 = countPossibleDesigns(targetPatterns, designs);
        System.out.println("[PART 1] Number of possible designs: " + resultPart1);

        // Calculate total number of ways
        long resultPart2 = totalWaysForAllDesigns(targetPatterns, designs);

        // Output the result
        System.out.println("[PART 2] Total number of ways: " + resultPart2);

    }

    private static void Part1(List<String> targetPatterns, List<String> designs) {
        // sort the target patterns
        targetPatterns.sort(Comparator.naturalOrder());

        int longestInputPattern = designs.stream().mapToInt(String::length).max().orElse(0);

        // build a radix-trie with the target patterns
        RadixTrie trie = new RadixTrie(targetPatterns, longestInputPattern);

        // count the number of valid patterns
        int validPatterns = 0;
        for (String pattern : designs) {
            if (trie.isValid(pattern))
                validPatterns++;
        }

        System.out.println("[PART 1] The number of valid patterns is: " + validPatterns);

    }

    // Class representing the Radix Trie
    private static class RadixTrie {
        private final RadixTrieNode root;
        private final int maxDepth;
        private final List<String> vocabulary;

        // Class representing a node in the Radix Trie
        private static class RadixTrieNode implements Comparable<RadixTrieNode> {
            String key; // The substring stored at this node
            String fullWord;
            List<RadixTrieNode> children; // Map of child nodes
            @SuppressWarnings("unused")
            RadixTrieNode parent; // Parent node
            int depth; // Depth of the node in the trie
            int characterCount; // Number of characters from root to this node

            public RadixTrieNode(String key, RadixTrieNode parent) {
                this.key = key;
                this.children = new ArrayList<>();
                this.parent = parent;
                this.characterCount = parent != null ? parent.characterCount + key.length() : 0;
                this.depth = parent != null ? parent.depth + 1 : 0;
                this.fullWord = parent != null ? parent.fullWord + key : key; // Ensure key is correctly concatenated
            }

            @Override
            public int compareTo(@NotNull RadixTrieNode o) {
                return this.characterCount - o.characterCount;
            }

            @Override
            public String toString() {
                return "depth=" + depth +
                        ", fullWord='" + fullWord + '\'' +
                        ", key='" + key + '\'' +
                        ", characterCount=" + characterCount +
                        '}';
            }
        }

        public RadixTrie(List<String> vocabulary, int maxDepth) {
            this.root = new RadixTrieNode("", null);
            this.vocabulary = vocabulary;
            this.maxDepth = maxDepth;
            buildRadixTrie();
        }

        public void buildRadixTrie() {
            Queue<RadixTrieNode> openSet = new PriorityQueue<>();
            openSet.add(this.root);
            RadixTrieNode current;
            while (!openSet.isEmpty()) {
                current = openSet.poll();
                for (String word : vocabulary) {
                    if (current.characterCount + word.length() <= this.maxDepth) {
                        RadixTrieNode newNode = new RadixTrieNode(word, current);
                        current.children.add(newNode);
                        openSet.add(newNode);
                    }
                }
            }
        }

        private static boolean partialMatch(RadixTrieNode node, String word) {
            return word.substring(0, node.characterCount).equals(node.fullWord);
        }

        private static boolean match(RadixTrieNode node, String word) {
            return word.equals(node.fullWord);
        }

        public boolean isValid(String word) {
            Stack<RadixTrieNode> openSet = new Stack<>();
            openSet.add(this.root);
            RadixTrieNode current;
            while (!openSet.isEmpty()) {
                current = openSet.pop();
                if (word.length() < current.characterCount)
                    continue;
                if (match(current, word))
                    return true;
                if (partialMatch(current, word)) {
                    openSet.addAll(current.children);
                }
            }
            return false;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Max Depth: ").append(this.maxDepth).append("\n");
            printTrieNode(sb, this.root, 0);
            return sb.toString();
        }

        private void printTrieNode(StringBuilder sb, RadixTrieNode node, int level) {
            sb.append("--".repeat(Math.max(0, level)));
            sb.append(node).append("\n");
            for (RadixTrieNode child : node.children) {
                printTrieNode(sb, child, level + 1);
            }
        }
    }

    // --- Part 1 ---

    public static boolean canFormDesign(String design, List<String> patterns, Map<String, Boolean> memo) {
        if (design.isEmpty()) {
            return true; // Base case: an empty design is always achievable
        }
        if (memo.containsKey(design)) {
            return memo.get(design); // Return cached result
        }

        // Try to match the design with each pattern
        for (String pattern : patterns) {
            if (design.startsWith(pattern)) { // Check if pattern is a prefix
                if (canFormDesign(design.substring(pattern.length()), patterns, memo)) {
                    memo.put(design, true);
                    return true;
                }
            }
        }

        memo.put(design, false); // If no pattern works
        return false;
    }

    public static int countPossibleDesigns(List<String> patterns, List<String> designs) {
        Map<String, Boolean> memo = new HashMap<>();
        int count = 0;

        for (String design : designs) {
            if (canFormDesign(design, patterns, memo)) {
                count++;
            }
        }

        return count;
    }

    // --- Part 2 ---

    // Recursive helper function to count all possible ways to form a design
    public static long countWays(String design, List<String> patterns, Map<String, Long> memo) {
        if (design.isEmpty()) {
            return 1; // Base case: one way to form an empty design
        }
        if (memo.containsKey(design)) {
            return memo.get(design); // Return cached result
        }

        long totalWays = 0;

        // Try every pattern to see if it can start the design
        for (String pattern : patterns) {
            if (design.startsWith(pattern)) { // Check if the design starts with this pattern
                String remaining = design.substring(pattern.length());
                totalWays += countWays(remaining, patterns, memo); // Count ways for the remaining part
            }
        }

        memo.put(design, totalWays); // Cache the result for this design
        return totalWays;
    }

    // Function to calculate total ways for all designs
    public static long totalWaysForAllDesigns(List<String> patterns, List<String> designs) {
        long totalWays = 0;

        for (String design : designs) {
            Map<String, Long> memo = new HashMap<>(); // Memoization for each design
            long waysForThisDesign = countWays(design, patterns, memo);
            totalWays += Math.max(waysForThisDesign, 0);

            // Debugging output
            System.out.println("Design: " + design + ", Ways: " + waysForThisDesign);
        }

        return totalWays;
    }

}
