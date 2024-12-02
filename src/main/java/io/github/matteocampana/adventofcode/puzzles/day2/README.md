# Day 2 Puzzle

## Description

This puzzle involves reading a sequence of integers from an input file and checking if the sequence is "safe".
A sequence is considered "safe" if it satisfies the following criteria:
- The levels are either all increasing or all decreasing.
- Any two adjacent levels differ by at least one and at most three.

### Part 1

Check if the sequence is "safe" according to the given criteria.

### Part 2

In Part 2, if a sequence is not "safe", attempt to sanitize it by removing one element and rechecking. If the sanitized sequence is "safe", it is considered valid.

## Input

The input file ([`input.txt`](src\main\resources\input\day2\input.txt)) contains sequences of integers separated by whitespace. Each sequence is on a new line.

## Solution

Read the input file and parse the sequences of integers. For each sequence, check if it is "safe" according to the given criteria. Count the number of "safe" sequences.

### Part 1

|Time complexity|Space complexity|
|---|---|
|O(n)|O(1)|

Reading the input requires O(n) time and O(1) space. Checking each sequence for safety requires O(n) time and O(1) space.

### Part 2

|Time complexity|Space complexity|
|---|---|
|O(n^2)|O(n)|

Reading the input requires O(n) time and O(1) space. Checking each sequence for safety and attempting to sanitize it requires O(n^2) time and O(n) space.
