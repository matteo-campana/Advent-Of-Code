# Day 5: Puzzle Description

## Puzzle Description
In this puzzle, you are given a set of rules and a series of updates. Each rule specifies an order between two pages. Your task is to determine if the updates follow the given rules. If an update does not follow the rules, you need to reorder it according to the rules.

## Input
The input file is divided into two sections:
1. Rules: Each rule is in the format `x|y`, indicating that page `x` should come before page `y`.
2. Updates: Each update is a comma-separated list of pages.

Example:

## Part 1 Solution
Process each update and check if it follows the given rules. If it does, add the middle page of the update to the sum of correctly ordered updates.

|Time complexity|Space complexity|
|---|---|
|O(r * u)|O(1)|

Checking if an update is in the correct order requires O(r * u) time, where `r` is the number of rules and `u` is the number of pages in an update. The space complexity is O(1) as we only need to store the sum of middle pages.

## Part 2 Solution
If an update does not follow the given rules, reorder it according to the rules and add the middle page of the reordered update to the sum of reordered updates.

|Time complexity|Space complexity|
|---|---|
|O(u * log(u) + r * u)|O(u)|

Reordering an update requires O(u * log(u) + r * u) time, where `r` is the number of rules and `u` is the number of pages in an update. The space complexity is O(u), where `u` is the number of pages in an update.
