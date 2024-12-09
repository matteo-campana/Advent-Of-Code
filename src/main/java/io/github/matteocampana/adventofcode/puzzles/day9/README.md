# Day 9 Puzzle

## Description

The puzzle involves defragmenting a disk represented by a string of numbers. Each number represents a block of data or a blank space.

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
