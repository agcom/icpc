# Problem D - Fraction

## Simplify

Parse a given string by the following context free grammar (report error if any):

```yacc
BasicFraction:          '(' BasicFractionOrDigit BasicFractionOrDigit BasicFractionOrDigit ')';
BasicFractionOrDigit:   BasicFraction | Digit;
Digit:                  [1-9];
```

Then, evaluate the expression to a rational number as defined (`(a b c)` = $a + \frac{b}{c}$, where a, b, and c can be either digits or basic fractions themselves); finally, make the numerator and denominator relatively prime to each other.

## Think

Implement the logic for adding and dividing rational numbers; then, parse and evaluate by writing a simple recursive descent parser. Observe that $\frac{a}{b} + \frac{c}{d} = \frac{a \times lcm(b, d) + c \times lcm(b, d)}{lcm(b, d)}$, and $\frac{a}{b} / \frac{c}{d} = \frac{a \times d}{b \times c}$. Also, to make two numbers relatively prime to each other, divide them to their greatest common divisor (GCD). The time complexity of this solution would be $O(n)$ if GCD computation is considered $O(1)$ as it is pretty fast even for the largest possible numbers.

## Code

```kotlin
fun main() {
	readln() // Ignore n.
	val it = readln()
		.filter { it != ' ' }
		.toList().listIterator()
	
	val ans = it.parseAndEvalBasicFraction() ?: -1
	
	println(if (it.hasNext()) -1 else ans)
}

private fun ListIterator<Char>.parseAndEvalBasicFraction(): RelativelyPrimeRationalNumber? {
	// A basic fraction must start with an open parenthesis.
	when (nextOrNull()) {
		'(' -> {}
		else -> return null
	}
	
	fun parseAndEvalBasicFractionPart(): RelativelyPrimeRationalNumber? = nextOrNull().let {
		when {
			it == '(' -> {
				// Nested basic fraction.
				previous()
				parseAndEvalBasicFraction()
			}
			
			it != null && it.isDigit() -> RelativelyPrimeRationalNumber(it.digitToInt().toLong())
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

private class RelativelyPrimeRationalNumber(numerator: Long, denominator: Long = 1) {
	val numerator: Long
	val denominator: Long
	
	init {
		// Make sure they are relatively prime.
		val numDenomGcd = gcd(numerator, denominator)
		this.numerator = numerator / numDenomGcd
		this.denominator = denominator / numDenomGcd
	}
	
	infix operator fun div(other: RelativelyPrimeRationalNumber) = RelativelyPrimeRationalNumber(
		numerator * other.denominator,
		denominator * other.numerator
	)
	
	infix operator fun plus(other: RelativelyPrimeRationalNumber): RelativelyPrimeRationalNumber {
		val denominatorsLcm = lcm(denominator, other.denominator)
		return RelativelyPrimeRationalNumber(
			numerator * (denominatorsLcm / denominator) + other.numerator * (denominatorsLcm / other.denominator),
			denominatorsLcm
		)
	}
	
	override fun toString() = "$numerator $denominator"
}

private fun lcm(a: Long, b: Long) = (a * b) / gcd(a, b)

private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
```

## Over-engineering

> See [D.kt](../src/D.kt) for the over-engineered solution code.

- The `gcd` function can be implemented iteratively; thanks to Kotlin, that can happen automatically by adding the `tailrec` keyword to the function signature.
- There is no need to make all rational numbers relatively prime; thanks to the test cases that would not cause overflow, just make the final rational number relatively prime (renamed `RelativelyPrimeRationalNumber` to `RationalNumber`).
- Make use of the `n` input to acquire a big enough input buffer.