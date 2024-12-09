package io.github.matteocampana.adventofcode.puzzles.day9;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Path inputFilePath = Paths.get("input/day9/input.txt");
        StringBuilder diskMap = new StringBuilder();

        try (InputStream inputStream = Solution.class.getClassLoader()
                .getResourceAsStream(inputFilePath.toString())) {
            if (inputStream == null)
                throw new FileNotFoundException("File not found at: " + inputFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    diskMap.append(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (diskMap.length() % 2 != 0) {
            diskMap.append("0");
        }

        String line = diskMap.toString();
        FileSystem fileSystem = defragment(line, false);
        System.out.println("Part 1: " + fileSystem.checkSum());

        fileSystem = defragment(line, true);
        System.out.println("Part 2: " + fileSystem.checkSum());
    }

    private static FileSystem defragment(String input, boolean blockFileMove) {
        FileSystem fileSystem = parse(input);

        for (int i = fileSystem.files.size() - 1; i >= 0; i--) {
            Item file = fileSystem.getFile(i);

            int blockMoveIterations = file.size();
            int blockSizeToSearch = blockFileMove ? file.size() : 1;
            Item blankItem = null;
            while (blockMoveIterations-- > 0) {
                blankItem = blankItem == null ? fileSystem.getBlankItem(blockSizeToSearch) : blankItem;

                if (blankItem == null || blankItem.start > file.start) {
                    break;
                }
                file.removeBlockFromEnd();
                int blankBlock = blankItem.removeBlockFromStart();
                file.addBlock(blankBlock);
                if (blankItem.size() == 0) {
                    blankItem = null;
                }
            }
            if (blankItem != null) {
                fileSystem.blankItemHeapPerSize.get(blankItem.size()).add(blankItem);
            }
        }
        return fileSystem;
    }

    private static FileSystem parse(String line) {
        List<Item> files = new ArrayList<>();
        LinkedHashMap<Integer, Item> blankSpaces = new LinkedHashMap<>();
        int index = 0;
        for (int i = 0; i < line.length(); i++) {
            int val = Character.getNumericValue(line.charAt(i));
            int id = i / 2;
            if (i % 2 == 0) {
                files.add(new Item(id, index, index + val - 1));
                index += val;
            } else if (val > 0) {
                blankSpaces.put(id, new Item(id, index, index + val - 1));
                index += val;
            }
        }
        List<PriorityQueue<Item>> blankItemHeapPerSize = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            blankItemHeapPerSize.add(new PriorityQueue<>(Comparator.comparingInt(i2 -> i2.start)));
        blankSpaces.values().forEach(bs -> blankItemHeapPerSize.get(bs.size()).add(bs));
        return new FileSystem(files, blankItemHeapPerSize);
    }

    static class Item {
        private final int id;
        private int start;
        private final ArrayDeque<Integer> blocks;

        Item(int id, int s, int e) {
            this.id = id;
            this.start = s;
            this.blocks = new ArrayDeque<>();
            for (int i = s; i <= e; i++) {
                blocks.add(i);
            }
        }

        long checkSum() {
            long checkSum = 0;
            for (int blockId : blocks) {
                checkSum += ((long) blockId * id);
            }
            return checkSum;
        }

        int size() {
            return blocks.size();
        }

        void removeBlockFromEnd() {
            blocks.removeLast();
        }

        int removeBlockFromStart() {
            start++;
            return blocks.removeFirst();
        }

        void addBlock(int id) {
            blocks.addFirst(id);
        }
    }

    record FileSystem(List<Item> files, List<PriorityQueue<Item>> blankItemHeapPerSize) {
        Item getFile(int index) {
            return files.get(index);
        }

        long checkSum() {
            return files.stream().map(Item::checkSum).mapToLong(l -> l).sum();
        }

        Item getBlankItem(int size) {
            int start = Integer.MAX_VALUE, sz = -1;

            for (int i = blankItemHeapPerSize.size() - 1; i >= 0; i--) {
                var heap = blankItemHeapPerSize.get(i);
                if (i >= size && !heap.isEmpty() && heap.peek().start < start) {
                    start = heap.peek().start;
                    sz = i;
                }
            }
            return sz != -1 ? blankItemHeapPerSize.get(sz).poll() : null;
        }
    }
}
