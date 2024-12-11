# Day 11: Stone Transformation Puzzle

Transform stones based on specific rules and count the number of stones after a certain number of transformations (blinks).

## Solution

The solution consists of two parts:

### Part 1 & Part 2

| **Time Complexity** | **Space Complexity** |
|---------------------|----------------------|
| O(n * 2^blink) | O(n * 2^blink) |

## Explanation

- **Time Complexity:** Exponential due to each stone potentially generating two new stones at each blink.
- **Space Complexity:** Exponential due to storage of all generated stones at each blink.

