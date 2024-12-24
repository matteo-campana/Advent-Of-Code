package io.github.matteocampana.adventofcode.puzzles.day24;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Path inputFilePath = Path.of("input/day24/input.txt");

        Map<String, Boolean> registry = new HashMap<>();
        List<List<String>> instructions = new ArrayList<>();

        boolean separator = false;

        try (InputStream inputStream = Solution.class.getClassLoader().getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null) {
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.isBlank()) {
                        separator = true;
                        continue;
                    }

                    if (!separator) {
                        String[] parts = line.split(":");
                        if (parts.length != 2) {
                            throw new IllegalArgumentException("Invalid registry format: " + line);
                        }
                        String registryName = parts[0].trim();
                        boolean value = "1".equals(parts[1].trim());
                        registry.put(registryName, value);
                    } else {
                        String[] parts = line.split(" ");
                        if (parts.length != 5 || !"->".equals(parts[3])) {
                            throw new IllegalArgumentException("Invalid instruction format: " + line);
                        }
                        String firstOperand = parts[0].trim();
                        String operation = parts[1].trim();
                        String secondOperand = parts[2].trim();
                        String destination = parts[4].trim();
                        instructions.add(List.of(destination, operation, firstOperand, secondOperand));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Graph graph = new Graph(registry, instructions);
        graph.computeNodeValues();

        String result = graph.part1();
        System.out.println("[PART 1] Result: " + result);
        System.out.println("[PART 1] Value: " + Long.parseLong(result, 2));
    }

    private static class Node {
        String name;
        List<List<String>> edges;

        public Node(String name) {
            this.name = name;
            this.edges = new ArrayList<>();
        }

        public void wire(String operand, String firstOperand, String secondOperand) {
            edges.add(List.of(operand, firstOperand, secondOperand));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return name.equals(node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    private static class Graph {
        List<Node> nodes;
        Map<String, Boolean> registry;

        public Graph(Map<String, Boolean> registry, List<List<String>> instructions) {
            this.nodes = new ArrayList<>();
            this.registry = new HashMap<>(registry);

            for (Map.Entry<String, Boolean> entry : registry.entrySet()) {
                nodes.add(new Node(entry.getKey()));
            }

            for (List<String> instruction : instructions) {
                String destination = instruction.get(0);
                Node node = nodes.stream()
                        .filter(n -> n.name.equals(destination))
                        .findFirst()
                        .orElseGet(() -> {
                            Node newNode = new Node(destination);
                            nodes.add(newNode);
                            return newNode;
                        });
                node.wire(instruction.get(1), instruction.get(2), instruction.get(3));
            }
        }

        public void computeNodeValues() {
            while (!nodes.stream().allMatch(n -> n.edges.isEmpty())) {
                List<Node> readyNodes = nodes.stream().filter(node -> node.edges.stream()
                                .allMatch(edge -> registry.containsKey(edge.get(1)) && registry.containsKey(edge.get(2))))
                        .toList();

                if (readyNodes.isEmpty()) {
                    throw new IllegalStateException("Unresolvable dependencies found in the graph.");
                }

                for (Node node : readyNodes) {
                    for (List<String> edge : node.edges) {
                        registry.put(node.name, switch (edge.get(0)) {
                            case "AND" -> registry.get(edge.get(1)) && registry.get(edge.get(2));
                            case "OR" -> registry.get(edge.get(1)) || registry.get(edge.get(2));
                            case "XOR" -> registry.get(edge.get(1)) ^ registry.get(edge.get(2));
                            default -> throw new IllegalStateException("Unexpected operation: " + edge.get(0));
                        });
                    }
                    node.edges.clear();
                }
            }
        }

        public String part1() {
            List<String> keys = registry.keySet().stream()
                    .filter(key -> key.startsWith("z"))
                    .sorted()
                    .toList()
                    .reversed();

            System.out.println("Keys: "+keys.toString());

            StringBuilder binaryString = new StringBuilder();
            for (String key : keys) {
                binaryString.append(registry.get(key) ? "1" : "0");
            }
            return binaryString.toString();
        }
    }
}
