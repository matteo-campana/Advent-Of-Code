# Day 4 Puzzle

## Description

This puzzle involves reading a grid of characters from an input file and identifying specific patterns within the grid. The patterns are matched in various orientations and positions within the grid.

## Input

The input file ([`input.txt`](src\main\resources\input\day4\input.txt)) contains a grid of characters. Each line in the file represents a row in the grid.

## Part 1 Solution

Read the input file and parse the grid of characters. Identify occurrences of the pattern "XMAS" in horizontal, vertical, and diagonal orientations.

|Time complexity|Space complexity|
|---|---|
|O(n * m)|O(m)|

Reading the input requires O(n * m) time, where `n` is the number of rows and `m` is the number of columns. Identifying the patterns requires O(n * m) time. The space complexity is O(m) since we only store up to 4 rows at a time.

## Part 2 Solution

Read the input file and parse the grid of characters. Identify occurrences of multiple patterns in various orientations and positions within the grid.

|Time complexity|Space complexity|
|---|---|
|O(n * m * p)|O(m)|

Reading the input requires O(n * m) time, where `n` is the number of rows and `m` is the number of columns. Identifying the patterns requires O(n * m * p) time, where `p` is the number of patterns. The space complexity is O(m) since we only store up to 3 rows at a time.
