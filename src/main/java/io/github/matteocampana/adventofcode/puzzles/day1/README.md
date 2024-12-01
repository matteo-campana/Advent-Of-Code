# Day 1 Puzzle

## Description

This puzzle involves reading pairs of integers from an input file, sorting each list of integers, and calculating the total distance between corresponding elements in the two sorted lists.

## Input

The input file ([`input.txt`](src\main\resources\input\day1\input.txt))  contains pairs of integers separated by whitespace. Each pair is on a new line.

## Solution

Read the input file and parse the integers into two lists. Sort the lists and calculate the total distance between corresponding elements.

|Time complexity|Space complexity|
|---|---|
|O(n log n)|O(n)|

Read the input require O(n) time and O(n) space. Sorting the lists require O(n log n) time and O(n) space. Calculating the total distance require O(n) time and O(1) space.
