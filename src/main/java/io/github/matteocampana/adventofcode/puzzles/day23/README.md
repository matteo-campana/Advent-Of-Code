# Day 23: Graph Triplets and Maximal Cliques

### Part 1

| Time complexity | Space complexity |
|-----------------|------------------|
| $O(V^{3})$         | $O(V)$           |

- **Time complexity**: Finding all triplets in a graph requires checking each combination of three vertices, leading to O(V^3) complexity.
- **Space complexity**: Storing the graph and triplets requires O(V) space.

## Part 2
1. Parse the input to build the graph.
2. Use the [Bron-Kerbosch algorithm](https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm#cite_note-5) to find the maximal clique in the graph.

| Time complexity | Space complexity |
|-----------------|------------------|
| $O(3^{(V/3)})$      | $O(V)$             |

- **Time complexity**: The Bron-Kerbosch algorithm has a worst-case time complexity of O(3^(V/3)) for finding maximal cliques.
- **Space complexity**: Storing the graph and intermediate sets requires O(V) space.
