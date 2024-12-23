package io.github.matteocampana.adventofcode.puzzles.day23;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {
    public static void main(String[] args) {
        Graph graph = new Graph();
        readInputFile(graph);
        System.out.println("[PART 1] Triplet count:" + part1(graph));
        System.out.println("[PART 2] password:" + part2(graph));
    }

    private static void readInputFile(Graph graph) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Solution.class.getClassLoader().getResourceAsStream("input/day23/input.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("-");
                graph.addEdge(parts[0], parts[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long part1(Graph graph) {

        // find trie of nodes such that each n1 -> n2 -> n3 -> n1 and count the number
        // of triplets that contain the letter 't'

        List<List<Graph.Node>> edges = graph.getEdges();
        Set<Set<String>> tripletsSet = new HashSet<>();

        // find triplets
        for (List<Graph.Node> edge : edges) {
            Graph.Node start = edge.getFirst();

            for (Graph.Node end : start.edges) {
                for (Graph.Node end2 : end.edges) {
                    for (Graph.Node end3 : end2.edges) {
                        if (end3 == start) {
                            String triplet = "(" + start.name + "," + end.name + "," + end2.name + ")";
                            if (start.name.startsWith("t") || end.name.startsWith("t") || end2.name.startsWith("t")) {
                                tripletsSet.add(new HashSet<>(List.of(start.name, end.name, end2.name)));
                            }
                        }
                    }
                }
            }
        }

        List<String> triplets = tripletsSet.stream().map(Object::toString).sorted(Comparator.naturalOrder()).toList();

        // for (String triplet : triplets) {
        // System.out.println(triplet);
        // }

        return triplets.size();
    }

    private static String part2(Graph graph) {
        Set<Graph.Node> r = new HashSet<>();
        Set<Graph.Node> p = new HashSet<>(graph.getVertices());
        Set<Graph.Node> x = new HashSet<>();
        Set<Graph.Node> maxClique = new HashSet<>();

        bronKerbosch2(r, p, x, maxClique, graph);

        return maxClique.stream()
                .map(n -> n.name)
                .sorted()
                .reduce("", (a, b) -> a + "," + b).substring(1);
    }

    private static void bronKerbosch2(Set<Graph.Node> r, Set<Graph.Node> p, Set<Graph.Node> x,
            Set<Graph.Node> maxClique, Graph graph) {

        /*
         * algorithm BronKerbosch2(R, P, X) is
         * if P and X are both empty then
         * report R as a maximal clique
         * choose a pivot vertex u in P ⋃ X
         * for each vertex v in P \ N(u) do
         * BronKerbosch2(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
         * P := P \ {v}
         * X := X ⋃ {v}
         */

        if (p.isEmpty() && x.isEmpty()) {
            if (r.size() > maxClique.size()) {
                maxClique.clear();
                maxClique.addAll(r);
            }
            return;
        }

        // Choose pivot vertex u from P ∪ X
        Graph.Node pivot = Stream.concat(p.stream(), x.stream())
                .max(Comparator.comparingInt(v -> getNeighbors(v, p).size()))
                .orElse(null);

        // P \ N(u)
        Set<Graph.Node> candidates = new HashSet<>(p);
        if (pivot != null) {
            candidates.removeAll(getNeighbors(pivot, p));
        }

        for (Graph.Node v : candidates) {
            Set<Graph.Node> neighbors = getNeighbors(v, graph.getVertices());

            // Recursive call with:
            // R ∪ {v}
            Set<Graph.Node> rNew = new HashSet<>(r);
            rNew.add(v);

            // P ∩ N(v)
            Set<Graph.Node> pNew = new HashSet<>(p);
            pNew.retainAll(neighbors);

            // X ∩ N(v)
            Set<Graph.Node> xNew = new HashSet<>(x);
            xNew.retainAll(neighbors);

            bronKerbosch2(rNew, pNew, xNew, maxClique, graph);

            p.remove(v);
            x.add(v);
        }
    }

    private static Set<Graph.Node> getNeighbors(Graph.Node node, Collection<Graph.Node> validNodes) {
        return node.edges.stream()
                .filter(validNodes::contains)
                .collect(Collectors.toSet());
    }

    private static class Graph {
        private static class Node implements Comparable<Node> {
            String name;
            List<Node> edges = new ArrayList<>();

            Node(String name) {
                this.name = name;
            }

            @Override
            public int compareTo(@NotNull Node o) {
                return this.name.compareTo(o.name);
            }

            @Override
            public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass())
                    return false;
                Node node = (Node) o;
                return Objects.equals(name, node.name);
            }

            @Override
            public int hashCode() {
                return Objects.hashCode(name);
            }

            public boolean findVertex(Node other) {
                if (this.edges.contains(other))
                    return true;
                boolean found = false;
                for (Node adj : edges) {
                    found = adj.findVertex(other);
                    if (found)
                        break;
                }
                return found;
            }
        }

        List<Node> nodes = new ArrayList<>();
        boolean directed = false;

        public void addEdge(String v1, String v2) {
            Node node1 = nodes.stream().filter(n -> n.name.equals(v1)).findFirst().orElseGet(() -> {
                Node n = new Node(v1);
                nodes.add(n);
                return n;
            });
            Node node2 = nodes.stream().filter(n -> n.name.equals(v2)).findFirst().orElseGet(() -> {
                Node n = new Node(v2);
                nodes.add(n);
                return n;
            });
            node1.edges.add(node2);
            if (!directed)
                node2.edges.add(node1);
        }

        public List<Node> getVertices() {
            return nodes;
        }

        public List<List<Node>> getEdges() {
            List<List<Node>> edges = new ArrayList<>();
            for (Node node : nodes) {
                for (Node adjacent : node.edges) {
                    List<Node> edge = List.of(node, adjacent);
                    if (!edges.contains(edge)) {
                        edges.add(edge);
                        if (!directed) {
                            edges.add(List.of(adjacent, node));
                        }
                    }
                }
            }
            return edges;
        }
    }
}
