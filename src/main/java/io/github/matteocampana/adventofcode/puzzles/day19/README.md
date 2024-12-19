# Day 19: Pattern Matching Puzzle

## Problem Statement

Given a list of target patterns and a list of designs, determine the number of designs that can be formed using the target patterns. Additionally, calculate the total number of ways to form all designs using the target patterns.

### Part 1

Count the number of designs that can be formed using the target patterns.

**Approach**: Use memoization to count the number of valid designs.

| **Part** | **Time Complexity** | **Space Complexity** |
|----------|---------------------|----------------------|
| Part 1   | O(n * m)            | O(n)                 |

- **Time Complexity:** O(n * m) where `n` is the number of designs and `m` is the number of patterns. This is because each design needs to be checked against all patterns.
- **Space Complexity:** O(n) for storing the memoization results.

### Part 2

Calculate the total number of ways to form all designs using the target patterns.

**Approach**: Use a recursive approach with memoization to calculate the total number of ways to form all designs.

| **Part** | **Time Complexity** | **Space Complexity** |
|----------|---------------------|----------------------|
| Part 2   | O(n * m)            | O(n)                 |

- **Time Complexity:** O(n * m) where `n` is the number of designs and `m` is the number of patterns. This is due to the recursive pattern matching with memoization.
- **Space Complexity:** O(n) for storing the memoization results.

## Explanation

- **Time Complexity:** 
  - Part 1: O(n * m) time complexity due to checking each design against the patterns, where `n` is the number of designs and `m` is the number of patterns.
  - Part 2: O(n * m) time complexity due to recursive pattern matching with memoization.
- **Space Complexity:** Linear space complexity for storing the memoization results.