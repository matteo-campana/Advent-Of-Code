# Day 10 Puzzle

## Description

The `countValidTrailheads` method is responsible for identifying valid trailheads in a topographic map. It iterates over each cell in the map and checks if it can be a starting point for a valid trail. A valid trail is defined as a path that starts at height 0 and increases by 1 until it reaches height 9. The method uses a breadth-first search (BFS) approach to explore all possible paths from each potential trailhead.

## Input

The input file ([`input.txt`](src\main\resources\input\day10\input.txt)) contains a grid of numbers representing the topographic map.

## Part 1 Solution

Identify all valid trailheads in the topographic map. A valid trailhead is a cell that starts at height 0 and can form a path that increases by 1 until it reaches height 9.

### Algorithm

1. Parse the input grid into a list of strings.
2. Iterate over each cell in the grid.
3. For each cell, use BFS to explore all possible paths.
4. Count the number of valid trailheads.

|Time complexity|Space complexity|
|---|---|
|O(n * m)|O(n * m)|

Where `n` is the number of rows and `m` is the number of columns in the grid. The space complexity is O(n * m) due to the storage of the grid and the BFS queue. The time complexity is O(n * m) due to the nested loop to iterate over each cell and the BFS exploration.

## Part 2 Solution

Identify all possible valid trailhead paths in the topographic map. A possible valid trailhead path is a path that starts at height 0 and can form a path that increases by 1 until it reaches height 9, considering all possible paths.

### Algorithm

1. Parse the input grid into a list of strings.
2. Iterate over each cell in the grid.
3. For each cell, use BFS to explore all possible paths.
4. Count the number of possible valid trailhead paths.

|Time complexity|Space complexity|
|---|---|
|O(n * m)|O(n * m)|

Where `n` is the number of rows and `m` is the number of columns in the grid. The space complexity is O(n * m) due to the storage of the grid and the BFS queue. The time complexity is O(n * m) due to the nested loop to iterate over each cell and the BFS exploration.