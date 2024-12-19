# Day 13 Puzzle

## Description

This puzzle involves analyzing a series of claw machines. Each machine has two buttons (A and B) that move the claw in specific directions. The goal is to determine the minimum number of tokens required to win a prize from each machine.

## Part 1 Solution

Read the input file and parse the claw machine configurations. Determine the minimum number of tokens required to win a prize from each machine by iterating over all possible combinations of button presses.

|Time complexity|Space complexity|
|---|---|
|O(n * 101^2)|O(n)|

Where `n` is the number of claw machines. The time complexity is O(n * 101^2) due to the need to explore all combinations of button presses, and the space complexity is O(n) due to the storage of the claw machines.

## Part 2 Solution

Read the input file and parse the claw machine configurations. Determine the minimum number of tokens required to win a prize from each machine by solving the system of linear equations for button presses.

|Time complexity|Space complexity|
|---|---|
|O(n)|O(n)|

Where `n` is the number of claw machines. The time complexity is O(n) due to the need to solve the system of equations for each machine, and the space complexity is O(n) due to the storage of the claw machines.
