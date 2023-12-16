fun main() {
	readln() // Ignore n.
	val it = readln()
		.filter { it != ' ' }
		.toList().listIterator()
	
	val ans = it.parseAndEvalBasicFraction()?.toRelativelyPrime() ?: -1
	
	println(if (it.hasNext()) -1 else ans)
}

private fun RationalNumber.toRelativelyPrime(): RationalNumber {
	val numDenomGcd = gcd(numerator, denominator)
	return RationalNumber(numerator / numDenomGcd, denominator / numDenomGcd)
}

private fun ListIterator<Char>.parseAndEvalBasicFraction(): RationalNumber? {
	// A basic fraction must start with an open parenthesis.
	when (nextOrNull()) {
		'(' -> {}
		else -> return null
	}
	
	fun parseAndEvalBasicFractionPart(): RationalNumber? = nextOrNull().let {
		when {
			it == '(' -> {
				// Nested basic fraction.
				previous()
				parseAndEvalBasicFraction()
			}
			
			it != null && it.isDigit() -> RationalNumber(it.digitToInt().toLong())
			else -> return null
		}
	}
	
	val a = parseAndEvalBasicFractionPart() ?: return null
	val b = parseAndEvalBasicFractionPart() ?: return null
	val c = parseAndEvalBasicFractionPart() ?: return null
	
	// A basic fraction must end with a close parenthesis.
	when (nextOrNull()) {
		')' -> {}
		else -> return null
	}
	
	return a + b / c
}

private fun ListIterator<Char>.nextOrNull(): Char? = if (hasNext()) next() else null

private class RationalNumber(val numerator: Long, val denominator: Long = 1) {
	infix operator fun div(other: RationalNumber) = RationalNumber(
		numerator * other.denominator,
		denominator * other.numerator
	)
	
	infix operator fun plus(other: RationalNumber): RationalNumber {
		val denominatorsLcm = lcm(denominator, other.denominator)
		return RationalNumber(
			numerator * (denominatorsLcm / denominator) + other.numerator * (denominatorsLcm / other.denominator),
			denominatorsLcm
		)
	}
	
	override fun toString() = "$numerator $denominator"
}

private fun lcm(a: Long, b: Long) = (a * b) / gcd(a, b)

private tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)