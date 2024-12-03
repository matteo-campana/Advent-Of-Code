# Day 3 Puzzle

## Description

This puzzle involves extracting and calculating multiplications from an input file. The input contains lines with multiplication expressions in the format `mul(x,y)`. Additionally, there are special blocks marked by `do()` and `don't()` which enable or disable the extraction of multiplications.

## Input

The input file ([`input.txt`](src\main\resources\input\day3\input.txt)) contains lines with multiplication expressions and special blocks.

## Part 1 Solution

Extract all valid multiplication expressions in the format `mul(x,y)` from each line and calculate their sum.

|Time complexity|Space complexity|
|---|---|
|O(n)|O(1)|

Reading the input and extracting multiplications requires O(n) time and O(1) space.

## Part 2 Solution

Extract valid multiplication expressions considering the special blocks marked by `do()` and `don't()`. Calculate the sum of these multiplications.

|Time complexity|Space complexity|
|---|---|
|O(n)|O(1)|

Reading the input, handling special blocks, and extracting multiplications requires O(n) time and O(1) space.
