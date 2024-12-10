# Day 9 Puzzle

## Description

The defragment method is responsible for rearranging the blocks in the file system to optimize space usage. It iterates over the files in reverse order and attempts to move blocks from the end of each file to the start of blank spaces. The method uses a helper method getBlankItem to find suitable blank spaces for the blocks. If a blank space is found, blocks are moved, and the blank space is updated accordingly.

The parse method converts the input string into a FileSystem object. It creates lists of Item objects representing files and blank spaces. Each character in the input string is interpreted as a block size, and Item objects are created accordingly. Blank spaces are stored in a LinkedHashMap and organized into priority queues based on their size.

The Item class represents a file or blank space in the file system. It has an ID, a start position, and a deque of block IDs. The class provides methods to calculate a checksum, get the size of the item, remove blocks from the start or end, and add blocks to the start.

The FileSystem record holds the list of files and a list of priority queues for blank spaces. It provides methods to retrieve a file by index, calculate the total checksum, and find a suitable blank space for a given block size. The getBlankItem method searches through the priority queues to find the smallest blank space that can accommodate the specified block size.

## Input

The input file ([`input.txt`](src\main\resources\input\day9\input.txt)) contains a single line of numbers representing the disk blocks.

## Part 1 Solution

Defragment the disk by moving blocks to the first available blank space. The checksum of the disk is calculated after defragmentation.

### Algorithm

1. Parse the input string into blocks and blank spaces.
2. Move each block to the first available blank space.
3. Calculate the checksum of the disk.

|Time complexity|Space complexity|
|---|---|
|O(n^2)|O(n)|

Where `n` is the number of blocks. The space complexity is O(n) due to the storage of blocks and blank spaces. The time complexity is O(n^2) due to the nested loop to move blocks.

## Part 2 Solution

Defragment the disk by moving entire files to the first available blank space. The checksum of the disk is calculated after defragmentation.

### Algorithm

1. Parse the input string into files and blank spaces.
2. Move each file to the first available blank space.
3. Calculate the checksum of the disk.

|Time complexity|Space complexity|
|---|---|
|O(n^2)|O(n)|

Where `n` is the number of files. The space complexity is O(n) due to the storage of files and blank spaces. The time complexity is O(n^2) due to the nested loop to move files.
