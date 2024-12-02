# Day 2 Puzzle

## Description

This puzzle involves reading a sequence of integers from an input file and checking if the sequence is "safe".
A sequence is considered "safe" if it satisfies the following criteria:
- The levels are either all increasing or all decreasing.
- Any two adjacent levels differ by at least one and at most three.

## Input

The input file ([`input.txt`](src\main\resources\input\day2\input.txt)) contains sequences of integers separated by whitespace. Each sequence is on a new line.

## Solution

Read the input file and parse the sequences of integers. For each sequence, check if it is "safe" according to the given criteria. Count the number of "safe" sequences.

|Time complexity|Space complexity|
|---|---|
|O(n)|O(1)|

Reading the input requires O(n) time and O(1) space. Checking each sequence for safety requires O(n) time and O(1) space.
