# Day 8 Puzzle

## Description

Find all antinodes in the boundary of the map.

## Input

The input file ([`input.txt`](src\main\resources\input\day8\input.txt)) contains a 2D grid of characters representing antennas and empty spaces.

## Part 1 Solution

Condition for a valid antinode coordinate:
- One antenna must be at twice the distance of the other.
- The antinode must be on the same line as the two antennas.

We are looking for a point $P(x, y)$ in a 2D grid that satisfies:

It is in line with two antennas, $A_{1}(x_{1}, y_{1})$ and $A_{2}(x_{2}, y_{2})$.
One antenna must be twice as far from $P$ as the other:
$d_{2} = 2 \cdot d_{1}$

### Formula Derivation:

#### Distance Equation:
$$
d_{1} = \sqrt{(x − x_{1})^2 + (y − y_{1})^2}
$$
$$
d_{2} = \sqrt{(x − x_{2})^2 + (y − y_{2})^2}
$$
Substitute $d_{2} = 2 \cdot d_{1}$:
$$
(x − x_{2})^2 + (y − y_{2})^2 = 2 \cdot ((x − x_{1})^2 + (y − y_{1})^2)
$$
Square Both Sides:
$$
(x − x_{2})^2 + (y − y_{2})^2 = 4 \cdot [(x − x_{1})^2 + (y − y_{1})^2]
$$
Expand Terms:
$$
(x^2 − 2x \cdot x_{2} + x_{2}^2) + (y^2 − 2y \cdot y_{2} + y_{2}^2) = 4(x^2 − 2x \cdot x_{1} + x_{1}^2) + 4(y^2 − 2y \cdot y_{1} + y_{1}^2]
$$
Simplify: Group like terms and solve for $x$ and $y$. This results in a quadratic equation for the possible coordinates of $P(x, y)$.

|Time complexity|Space complexity|
|---|---|
|O(n^2)|O(n)|


Where `n` is the number of antennas. The space complexity is O(n) due to the storage of antinodes. The time complexity is O(n^2) due to the nested loop to compare all pairs of antennas.


## Part 2 Solution

Calculate antinodes for all pairs of antennas with the same identifier and remove antinodes that are outside the map bounds.

|Time complexity|Space complexity|
|---|---|
|O(n^2)|O(n)|

Where `n` is the number of antennas. The space complexity is O(n^2) due to the storage of antinodes. The time complexity is O(n^2) due to the nested loop to compare all pairs of antennas.

