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