# Day 1 Puzzle

## Description

This puzzle involves reading pairs of integers from an input file, sorting each list of integers, and calculating the total distance between corresponding elements in the two sorted lists. Additionally, it calculates a similarity score based on the frequency of integers in the second list.

## Input

The input file ([`input.txt`](src\main\resources\input\day1\input.txt)) contains pairs of integers separated by whitespace. Each pair is on a new line.

## Part 1 Solution

Read the input file and parse the integers into two lists. Sort the lists and calculate the total distance between corresponding elements.

|Time complexity|Space complexity|
|---|---|
|O(n log n)|O(n)|

Reading the input requires O(n) time and O(n) space. Sorting the lists requires O(n log n) time and O(n) space. Calculating the total distance requires O(n) time and O(1) space.

## Part 2 Solution

Compute the similarity score based on the frequency of integers in the second list.

|Time complexity|Space complexity|
|---|---|
|O(n)|O(n)|

Computing the similarity score requires O(n) time and O(n) space.
