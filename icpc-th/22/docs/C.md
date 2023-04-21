# C

> Limits are self-assigned based on the minimal found solution.

- Time limit: 2 seconds
- Memory limit: 256 MB
- Accepts multiple outputs as correct solution per test-case.
- A correct output per test-case must include only less than 12000 moves/swaps.

## Simplify

Sort a given array with limited swap operations; only rooms with indexes differing in one bit-position are allowed to be swapped together.

## Think

I believe there exists a simple solution for this based on customizing a sorting algorithm.

### Customized bubble-sort

We have

```java
private static boolean areOneBitPosDiff(final int i, final int j) {
	final int xor = i ^ j;
	return xor != 0 && (xor & (xor - 1)) == 0;
}

private void immediateSwap(final int i, final int j) {
	final int t = as[i];
	as[i] = as[j];
	as[j] = t;
}
```

; the `areOneBitPosDiff` relies on the fact that `xor-1` causes the right-most set bit to flip and creates a cascading wave of flips to its right bits (do a generic example with `xxx1000` to digest it); and the glue part

```java
for (int i = 0; i < as.length - 1; i++) {
	for (int j = 0; j < as.length - i - 1; j++) {
		if (as[j] > as[j + 1] && areOneBitPosDiff(j, j + 1)) {
			immediateSwap(j, j + 1);
		}
	}
}
```

Let us give a contradicting input that won't get sorted by this: `1 3 2 4`; it can not swap numbers 3 and 2 because of the limitation, more precisely said, it can not do an immediate swap there; to swap indexes 1 and 2, it needs to use the intermediate index 0; swap indexes 1 and 0, then indexes 0 and 2, but this causes numbers 2 and 1 to be misplaced which can be resolved by swapping indexes 1 and 0; so, there should be a logic to handle non-immediate swaps.

### Graph of possible swaps

I did not go more deep on customizing a sorting algorithm to solve this problem; I hopped into graphs as soon as its light bulb turned on, and let us see if it works.

- Construct a graph for the indexes, with edges connecting those that differ in one bit-position.
- Sort a copy of the array.
- Compare the sorted array with the original one to figure out the needed swaps.
- For each needed swap like `(u, v)`: 
  - Find the shortest path between nodes/indexes `u` and `v`, like `(u, a, b, ..., y, z, v)`.
  - Do a smart swap by moving on the path:
    - Immediate swap `(u, a)`.
    - Immediate swap `(a, b)`.
    - ...
    - Immediate swap `(y, z)`.
    - Immediate swap `(z, v)`.
  - Finally, the `u` and `v` are swapped, but everything in the way got a shift; recover from the shift:
    - Traverse the path in reverse, skip the first, and do immediate swaps:
      - Immediate swap `(y, z)`.
      - ...
      - Immediate swap `(a, b)`.
      - Immediate swap `(u, a)`.
  - Sorted.

Let us analyze the solution:
- Constructing the graph happens in `O((2^n)^2)` (max `n` is `10`).
- Sorting a copy of the array happens in `O((2^n)*log(2^n)) = O((2^n)*n)`.
- Finding the shortest path between two nodes can happen by a BFS (with graph edges stored in the adjacency list format) in `O((2^n)+C((2^n), 2))` (maximum number of edges is actually pretty low, 10240).
- Each smart swap, beside finding a shortest path, costs `O(C((2^n), 2))`.
- Just being lucky not getting over 12000 moves limit (actually, it may not be luck preventing that; there might be a calculable hard-limit for our solution).

## Code

See [`./../src/C.java`](./../src/C.java)

## Over-engineering

> Hard-coded adjacency list...

## TODO

- Recover phase proof
- Pattern finding approach
- ChatGPT suggestion follow-up