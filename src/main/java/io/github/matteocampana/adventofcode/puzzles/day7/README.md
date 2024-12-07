# Day 7 Puzzle

## Description

This puzzle involves reading a list of target values and sequences of numbers from an input file. The goal is to determine if the target value can be achieved using specific operations on the sequence of numbers.

## Input

The input file ([`input.txt`](src\main\resources\input\day7\input.txt)) contains lines of target values and sequences of numbers. Each line is formatted as `target: number1 number2 ... numberN`.

## Part 1 Solution

Read the input file and parse the target values and sequences of numbers. Determine if the target value can be achieved using addition and multiplication operations.

|Time complexity|Space complexity|
|---|---|
|O(2^n)|O(n)|

Evaluating all possible combinations of addition and multiplication requires O(2^n) time, where `n` is the number of numbers in the sequence. The space complexity is O(n) due to the recursion stack.

## Part 2 Solution

Read the input file and parse the target values and sequences of numbers. Determine if the target value can be achieved using addition, multiplication, and concatenation operations.

|Time complexity|Space complexity|
|---|---|
|O(3^n)|O(n)|

Evaluating all possible combinations of addition, multiplication, and concatenation requires O(3^n) time, where `n` is the number of numbers in the sequence. The space complexity is O(n) due to the recursion stack.
