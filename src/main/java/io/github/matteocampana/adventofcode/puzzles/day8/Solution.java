package io.github.matteocampana.adventofcode.puzzles.day8;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Solution {

    private static class Antenna {
        int row;
        int col;
        Character identifier;

        public Antenna(int row, int col, Character identifier) {
            this.row = row;
            this.col = col;
            this.identifier = identifier;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Antenna antenna = (Antenna) o;
            return row == antenna.row && col == antenna.col && Objects.equals(identifier, antenna.identifier);
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col, identifier);
        }
    }

    private static class Antinode {
        Integer row;
        Integer col;

        public Antinode(Integer row, Integer col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Antinode antinode = (Antinode) o;
            return Objects.equals(row, antinode.row) && Objects.equals(col, antinode.col);
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day8/input.txt");
        List<String> input = new ArrayList<>();

        try (InputStream inputStream = Solution.class.getClassLoader()
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

        char[][] map = input.stream().map(String::toCharArray).toArray(char[][]::new);

        Part1(map);
    }

    private static void Part1(char[][] map) {

        Map<Character, List<Antenna>> antennas = new HashMap<>();

        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] != '.') {
                    if (!antennas.containsKey(map[row][col])) {
                        antennas.put(map[row][col], new ArrayList<>());
                    }
                    antennas.get(map[row][col]).add(new Antenna(row, col, map[row][col]));
                }
            }
        }

        Set<Antinode> antinodes = new HashSet<>();
        Set<Antinode> antinodes2 = new HashSet<>();

        // calculate antinodes for all pairs of antennas with the same identifier

        for (Map.Entry<Character, List<Antenna>> entry : antennas.entrySet()) {
            List<Antenna> antennaList = entry.getValue();
            for (int i = 0; i < antennaList.size(); i++) {
                for (int j = i + 1; j < antennaList.size(); j++) {
                    antinodes.addAll(calculateAntinodes(antennaList.get(i), antennaList.get(j)));
                    antinodes2.addAll(calculateAntinodes2(antennaList.get(j), antennaList.get(i), map));
                }
            }
        }

        // remove antinodes that are outside the map bounds
        antinodes.removeIf(antinode -> !antinodeInMapBounds(map, antinode));
        antinodes2.removeIf(antinode -> !antinodeInMapBounds(map, antinode));

        char[][] map1 = new char[map.length][map[0].length];
        char[][] map2 = new char[map.length][map[0].length];

        for (int i = 0; i < map.length; i++) {
            map1[i] = Arrays.copyOf(map[i], map[i].length);
            map2[i] = Arrays.copyOf(map[i], map[i].length);
        }

        // print the final map with the antinodes marked as '#'
        for (Antinode antinode : antinodes) {
            if (map1[antinode.row][antinode.col] == '.') {
                map1[antinode.row][antinode.col] = '#';
            }
        }

        for (Antinode antinode : antinodes2) {
            if (map2[antinode.row][antinode.col] == '.') {
                map2[antinode.row][antinode.col] = '#';
            }
        }

        System.out.println("#".repeat(50));
        System.out.println("Part 1:");
        System.out.println("-".repeat(50));
        for (char[] row : map1) {
            System.out.println(row);
        }

        // print the number of antinodes
        System.out.println("Total number of valid anitnodes: " + antinodes.size());

        System.out.println("#".repeat(50));
        System.out.println("Part 2:");
        System.out.println("-".repeat(50));

        for (char[] row : map2) {
            System.out.println(row);
        }

        // some of the new antinodes will occur at the position of each antenna (unless
        // that antenna is the only one of its frequency).

        for (Antenna antenna : antennas.values().stream().filter(list -> list.size() > 1).flatMap(List::stream)
                .toList()) {
            antinodes2.removeIf(antinode -> antinode.row == antenna.row && antinode.col == antenna.col);
        }

        int antinodesAntennas = (int) antennas.values().stream().filter(list -> list.size() > 1).flatMap(List::stream)
                .count();

        System.out.println("Total number of valid anitnodes: " + antinodes2.size());
        System.out.println("Total number of antennas that are valid antinodes: " + antinodesAntennas);

        System.out.println("Total number of valid anitnodes + Total number of antennas: "
                + (antinodes2.size() + antinodesAntennas));

    }

    private static Set<Antinode> calculateAntinodes(Antenna antenna1, Antenna antenna2) {
        Set<Antinode> antinodes = new HashSet<>();

        int y = Math.abs(antenna1.row - antenna2.row);
        int x = Math.abs(antenna1.col - antenna2.col);
        if (antenna1.row < antenna2.row) {
            if (antenna1.col < antenna2.col) {
                antinodes.add(new Antinode(antenna1.row - y, antenna1.col - x));
                antinodes.add(new Antinode(antenna2.row + y, antenna2.col + x));
            } else {
                antinodes.add(new Antinode(antenna1.row - y, antenna1.col + x));
                antinodes.add(new Antinode(antenna2.row + y, antenna2.col - x));
            }
        } else {
            if (antenna1.col < antenna2.col) {
                antinodes.add(new Antinode(antenna1.row + y, antenna1.col - x));
                antinodes.add(new Antinode(antenna2.row - y, antenna2.col + x));
            } else {
                antinodes.add(new Antinode(antenna1.row + y, antenna1.col + x));
                antinodes.add(new Antinode(antenna2.row - y, antenna2.col - x));
            }
        }

        return antinodes;
    }

    private static Set<Antinode> calculateAntinodes2(Antenna antenna1, Antenna antenna2, char[][] map) {
        Set<Antinode> antinodes = new HashSet<>();

        int y = Math.abs(antenna1.row - antenna2.row);
        int x = Math.abs(antenna1.col - antenna2.col);
        if (antenna1.row < antenna2.row) {
            if (antenna1.col < antenna2.col) {
                Antinode a1 = new Antinode(antenna1.row - y, antenna1.col - x);
                while (antinodeInMapBounds(map, a1)) {
                    antinodes.add(a1);
                    a1 = new Antinode(a1.row - y, a1.col - x);
                }

                Antinode a2 = new Antinode(antenna2.row + y, antenna2.col + x);
                while (antinodeInMapBounds(map, a2)) {
                    antinodes.add(a2);
                    a2 = new Antinode(a2.row + y, a2.col + x);
                }
            } else {
                Antinode a1 = new Antinode(antenna1.row - y, antenna1.col + x);
                while (antinodeInMapBounds(map, a1)) {
                    antinodes.add(a1);
                    a1 = new Antinode(a1.row - y, a1.col + x);
                }

                Antinode a2 = new Antinode(antenna2.row + y, antenna2.col - x);
                while (antinodeInMapBounds(map, a2)) {
                    antinodes.add(a2);
                    a2 = new Antinode(a2.row + y, a2.col - x);
                }
            }
        } else {
            if (antenna1.col < antenna2.col) {
                Antinode a1 = new Antinode(antenna1.row + y, antenna1.col - x);
                while (antinodeInMapBounds(map, a1)) {
                    antinodes.add(a1);
                    a1 = new Antinode(a1.row + y, a1.col - x);
                }

                Antinode a2 = new Antinode(antenna2.row - y, antenna2.col + x);
                while (antinodeInMapBounds(map, a2)) {
                    antinodes.add(a2);
                    a2 = new Antinode(a2.row - y, a2.col + x);
                }
            } else {
                Antinode a1 = new Antinode(antenna1.row + y, antenna1.col + x);
                while (antinodeInMapBounds(map, a1)) {
                    antinodes.add(a1);
                    a1 = new Antinode(a1.row + y, a1.col + x);
                }

                Antinode a2 = new Antinode(antenna2.row - y, antenna2.col - x);
                while (antinodeInMapBounds(map, a2)) {
                    antinodes.add(a2);
                    a2 = new Antinode(a2.row - y, a2.col - x);
                }
            }
        }

        return antinodes;
    }

    private static boolean antinodeInMapBounds(char[][] map, Antinode antinode) {
        return antinode.row >= 0 && antinode.row < map.length && antinode.col >= 0 && antinode.col < map[0].length;
    }
}
