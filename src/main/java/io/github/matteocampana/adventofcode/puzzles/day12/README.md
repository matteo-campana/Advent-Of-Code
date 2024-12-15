# Day 12 Puzzle

## Description

This puzzle involves analyzing a garden represented as a 2D grid of characters. Each character represents a type of plant. The goal is to calculate the total cost of fencing regions of the garden based on specific rules.

## Input

The input file ([`test.txt`](src\main\resources\input\day12\test.txt)) contains lines representing the garden grid. Each line is a row of the grid.

## Part 1 Solution

Read the input file and parse the garden grid. Determine the total cost of fencing regions using the area and perimeter of each region.

|Time complexity|Space complexity|
|---|---|
|O(n * m)|O(n * m)|

Where `n` is the number of rows and `m` is the number of columns in the garden grid. The time complexity is O(n * m) due to the need to explore each cell, and the space complexity is O(n * m) due to the storage of the visited cells and regions.

## Part 2 Solution

Read the input file and parse the garden grid. Determine the total cost of fencing regions using the area and the number of sides of the fence.

|Time complexity|Space complexity|
|---|---|
|O(n * m)|O(n * m)|

Where `n` is the number of rows and `m` is the number of columns in the garden grid. The time complexity is O(n * m) due to the need to explore each cell, and the space complexity is O(n * m) due to the storage of the visited cells and regions.
