# Problem I - Product Delivery

## Simplify

- The cities' stores are jealous of each other; therefore, they require you to ship more (or the same as the last) to them than the previous cities'.
- Minimize the number of supply procedures while satisfying the demand constraints of the cities.

## Think

You (the train and the supply company) ought to supply the same or more than your last supply as you move forward on the route; by a greedy approach, supplying each city with the minimum possible will leave you more space for future maneuver, and indeed that will lead to the optimal solution (I am not a proof type of person, but this should be easy to observe); the time complexity of this solution is $O(n)$, the minimum possible considering that reading the input is $O(n)$ anyway.

## Code

```kotlin
fun main() {
	val n = readln().toInt()
	
	var supplyProceduresCount = 1 // We should do at least 1 supply procedure.
	var previousSupply = -1
	repeat(n) {
		val (l, m) = readln().split(' ').map { it.toInt() }
		
		if (previousSupply <= l) {
			// Supply this city with its minimum demand.
			previousSupply = l
		} else if (previousSupply <= m) {
			// Supply this city as the previous one.
			// NOP
		} else {
			// Return to the depot and come back to supply this city with its minimum demand.
			supplyProceduresCount++
			previousSupply = l
		}
	}
	
	println(supplyProceduresCount)
}

// Judged with Kotlin 1.7.21.
```

## Over-engineering

Some test cases' inputs are massive (tens of MBs); considering using a buffered input reader with a bigger buffer might help halving the runtime on those tests.