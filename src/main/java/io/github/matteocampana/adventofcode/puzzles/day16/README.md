# Day 16: Maze Navigation Puzzle

Navigate through a maze from a start position (S) to an end position (E) while minimizing the cost. The cost includes moving forward and turning.

## Part 1: Minimum Cost Path

The solution involves using a priority queue to perform a breadth-first search (BFS) to find the minimum cost path from the start to the end. The BFS considers both moving forward and turning, with different costs associated with each action.

### Time and Space Complexity

| **Time Complexity** | **Space Complexity** |
|---------------------|----------------------|
| O(n * m * 4)        | O(n * m * 4)         |

- **Time Complexity:** The BFS explores each cell in the maze in all four possible directions.
- **Space Complexity:** The visited array and priority queue store states for each cell in all four possible directions.

- **n:** Number of rows in the maze.
- **m:** Number of columns in the maze.

## Part 2: Count Tiles in Best Paths

This part involves counting the number of tiles that are part of at least one of the best paths found in Part 1. The tiles are marked in the maze for visualization.

### Time and Space Complexity

| **Time Complexity** | **Space Complexity** |
|---------------------|----------------------|
| O(p * t)            | O(t)                 |

- **Time Complexity:** Counting tiles involves iterating over the best paths and marking the tiles.
- **Space Complexity:** A set is used to store the unique tiles that are part of the best paths.

- **p:** Number of best paths found.
- **t:** Number of tiles in each best path.
