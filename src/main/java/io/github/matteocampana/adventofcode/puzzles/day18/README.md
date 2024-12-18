# Day 18: Grid Pathfinding Puzzle

## Problem Statement

Given a grid of size 71x71, some positions are corrupted. The goal is to find the shortest path from the top-left corner (0,0) to the bottom-right corner (70,70) while avoiding corrupted positions. Additionally, determine the first corrupted position that makes the shortest path invalid.

## Solution

The solution consists of two parts:

### Part 1

Find the shortest path from (0,0) to (70,70) avoiding corrupted positions.

### Part 2

Determine the first corrupted position that makes the shortest path invalid.

## Approach

1. **Input Parsing:** Read the corrupted positions from the input file.
2. **Grid Initialization:** Initialize a 71x71 grid and mark the corrupted positions.
3. **Shortest Path Calculation:** Use a breadth-first search (BFS) algorithm to find the shortest path from (0,0) to (70,70).
4. **Validation of Corrupted Positions:** Incrementally add corrupted positions and check if the shortest path is still valid.

## Time Complexity

| **Part** | **Time Complexity** | **Space Complexity** |
|----------|---------------------|----------------------|
| Part 1   | O(V + E)            | O(n)                 |
| Part 2   | O(n^2)              | O(n)                 |

## Explanation

- **Time Complexity:** 
  - Part 1: O(V + E) time complexity due to BFS traversal, where V is the number of vertices and E is the number of edges.
  - Part 2: Quadratic time complexity due to repeated BFS traversals for each corrupted position.
- **Space Complexity:** Linear space complexity for storing the grid and visited positions.

## Additional Notes

- The algorithm benefits from the data structure derived from the priority queue, which helps efficiently manage the nodes to be explored during the BFS traversal.
