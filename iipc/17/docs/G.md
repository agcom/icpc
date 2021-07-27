# G

> Limits are self-assigned based on the minimal solution.

- Time limit: 2 seconds
- Memory limit: 256 MB

## Simplify

**Prime factors**' quantities of some numbers given as  **factorial division**s.

> Unfamiliar with prime factors? See [prime decomposition](https://en.wikipedia.org/wiki/Integer_factorization#Prime_decomposition).

## Think

First, let's say we're solving the problem for **only one number** (`n`).

The given number can be truly big; The maximum value for `n` is `10^6!`; Simply said, it's impossible to prime factorize such a big number.

> `69! = 1.7112245e+98`
>
> `10^6! = :|`

But, fortunately we're given `n` as a **factorial division** **`a! / b!`**; And we can use its factorial form (`a * (a-1) * ... * 1 / b * (b-1) * ... * 1`); Just prime factorize each given factor and sum them up!

> The product of each given factorial division (`a! / b!`) is always an integer; Because, a constraint says `b <= a`; So, we can always omit `b!` factors from the numerator, `a!`.
>
> For example, `69! / 66! = 69 * 68 * 67`.

Now, we have some consecutive factors `>= 1` (which as a group is potentially equal to `n`).

To satisfy the problem, we'll calculate **prime factors' quantity of each factor** and sum them up.

> An example,
>
> 1. Input; `a = 68, b = 66`
> 2. Find factors; `69! / 66! = 69 * 68 * 67`
> 3. Prime factorize each; `69 = 3 * 23`, `68 = 2 * 2 * 17`, `67 = 67`
> 4. Quantities; `69 -> 2`, `68 -> 3`, `67 -> 1`
> 5. Sum them up; `2 + 3 + 1 = 6`

To exactly satisfy the problem, we have to answer `k` test cases; The question is clearly asking for a dynamic solution.

So, the rest of this essay is optimizing; An efficient prime factorization algorithm and adding a dynamic flavor.

### Efficient prime factorization

The most efficient, non-probabilistic prime factorization algorithm is using **precomputed primes and dividing**.

> Without precomputed primes present, [wheel factorization](https://en.wikipedia.org/wiki/Wheel_factorization) can be used.

The maximum number we're willing to factorize independently, is `10^6`; We'll need primes till `sqrt(10^6)`.

> A number has no divisor bigger than it's root other than itself.

Finding primes less than or equal to `sqrt(10^6) = 1000` is pretty doable; There are 168 primes less than `1000`; You can generate them in less than 10 ms using [sieve of eratosthenes](https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes); And possibly, hard code them!

After saving the primes in an array, call it `primes`, prime factorizing is a minor problem; The following function counts prime factors of its input `f`.

```java
private int primeFactorsQuantity(int f) {
    int count = 0;
    for (int i = 0; i < primes.length && primes[i] * primes[i] <= f; i++) { // Until primes[i] <= sqrt(f)
        while (f % primes[i] == 0) {
            f /= primes[i];
            count++;
        }
    }
    
    if (f > 1) count++; // The remaning f is prime, which concludes 1 extra factor.
    
    return count;
}
```

If we assume iterating over the primes array is `O(1)` (maximum 168 iterations), then the function works in `O(log(n))`.

### Dynamic solving

If you solve the problem by prime factorizing each factor one by one and summing them up, you'll hit a time limit error.

```java
public int solve(final int a, final int b) {
	int count = 0;
	for (int f = b + 1; f <= a; f++) {
		count += primeFactorsQuantity(f);
	}
    return count;
}
```

Indeed, it's obvious; The `solve` function itself does **too many iterations**. Time complexity of the above solution is `O(Σ[i = 1, k](ai - bi + 1) * log(n))` and its space complexity is `O(1)`.

To lower the iterations, we can use the fact that the **factors are consecutive**.

Formally said, `solve(n, 1) = solve(n - 1, 1) + primeFactorsQuantity(n)`.

> For example, `solve(69, 1)` is equal to `solve(68, 1) + primeFactorsQuantity(69)`.

What about `solve(a, b)`? Given the `solve(a, 1)`, remove the `b!` prime factors' quantity; **`solve(a, b) = solve(a, 1) - solve(b, 1)`**.

So, for each given `a, b`, we'll cache the results `a -> solve(a, 1), b -> solve(b, 1)` and return `solve(a, 1) - solve(b, 1)`.

> You can also do a more flexible dynamic approach by only caching the result `(a, b) -> solve(a, b)`; For each call, you should probably find the nearest cached `(a, b)` and add the missing factors and/or remove the extra factors.

In the dynamic approach, each factor is calculated only once; So, the new time complexity is `O(max(ai) * log(n))` and the new space complexity is `O(max(ai))`.

## Code

For the complete code (including the over-engineerings), see the [associated code file](../src/G.java) in the [`src`](../src) directory.

```java
import java.io.*;
import java.util.*;

public final class G {

    // Fast IO
    private static final FastNonBlankReader in = new FastNonBlankReader(new BufferedReader(new InputStreamReader(System.in)));
    private static final PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

    public static void main(String[] args) {
        final int k = in.nextInt(); // Test cases

        final G solver = new G();
        for (int i = 0; i < k; i++) {
            final int a = in.nextInt(), b = in.nextInt();
            out.println(solver.solve(a, b));
        }

        out.flush(); // Mandatory
    }

    private final Map<Integer, Integer> cache = new HashMap<>(); // n -> solve(n, 1)

    {
        cache.put(1, 0); // The base case
    }

    public int solve(final int a, final int b) {
        ensureCached(a);
        ensureCached(b);
        return cache.get(a) - cache.get(b);
    }

    private void ensureCached(final int n) {
        if (!cache.containsKey(n)) calculate(n);
    }

    private void calculate(final int n) {
        int low = n - 1;
        while (!cache.containsKey(low)) low--; // Find the lowest cached (calculated) factor

        // Calculate low + 1 till n
        for (int i = low + 1; i <= n; i++) {
            cache.put(i, cache.get(i - 1) + primeFactorsQuantity(i));
        }
    }

    private static final int[] primes = {2, 3, 5, 7, ...};

    public int primeFactorsQuantity(int f) {
        ...
    }

    private static final class FastNonBlankReader implements Closeable {
        ...
    }

}
```

## Over-engineering

We can indeed write a more efficient program, reducing time and memory consumption; But, **the complexities will remain the same**.

### Dynamic prime factorization

We can optimize the `primeFactorsQuantity` by **caching the results for every number that passes by** (input itself and intermediate values).

> For example,
>
> 1. We call the function for the first time with `f = 69`.
> 2. The function hits `69` and `23` in its way; The results `69 -> 1 + 1 = 2`, `23 -> 1` will be saved.
> 3. We'll call the function second time with `f = 138`.
> 4. The function hits `69` eventually (after dividing by `2`), and it'll reuse `69 -> 2` by adding to `count`; Also, saves `138 -> 1 + 2 = 3`.

To simplify the implementation, we'll write a recursive function wrapped in a class.

```java
private static final class PrimeFactorization {

    private static final int[] primes = {2, 3, 5, ...};

    private final Map<Integer, Integer> cache = new HashMap<>();

    {
        cache.put(1, 0); // Base case
    }

    public int primeFactorsQuantity(final int f) {
        return primeFactorsQuantity(f, 0);
    }

    // pi: The primes array index to continue searching on.
    private int primeFactorsQuantity(final int f, final int pi) {
        final Integer ch = cache.get(f);
        if (ch != null) return ch;
        
        int count = 1; // If this variable wasn't assigned below, then f is a prime with 1 prime factor.
        for (int i = pi; i < primes.length && primes[i] * primes[i] <= f; i++) {
            if (f % primes[i] == 0) {
                count = primeFactorsQuantity(f / primes[i], i) + 1; // +1 for the current found prime factor (primes[i])
                break;
            }
        }
        
        cache.put(f, count);
        return count;
    }
    
}
```

> A concern may arise about stack overflow; The worst case goes deep only `log[2](10^6)`, which is 20 recursive calls; So, nothing to worry about.
>
> Precisely, the worst case is when the cache is empty and we call the function with `f = 2^19 = 524288`; It goes 20 levels deep to eventually reach the base case `f = 1`.

The `calculate` function now uses the `PrimeFactorization` class,

```java
private final PrimeFactorization pf = new PrimeFactorization();

private void calculate(final int n) {
    int low = n - 1;
    while (!cache.containsKey(low)) low--; // Find the lowest cached (calculated) factor
    
    // Calculate low + 1 till n
    for (int i = low + 1; i <= n; i++) {
        cache.put(i, cache.get(i - 1) + pf.primeFactorsQuantity(i));
    }
}
```

### Ditching hash-maps

`HashMap` indeed uses **a lot of memory**; The first solution is to specify a good pair of `initialCapacity, loadFactor` explicitly.

The main `cache`, in the worst case, contains `10^6` unique elements; We can set `initialCapacity = 10^6, loadFactor = 2`.

> The map never grows and the duplicate hashes will be linked to the same bucket; But, as we already know our keys' hashes are unique (`for every n in [1, 10^6], n % 2^ceil(log[2](10^6)) is a unique value`), it won't introduce a bottleneck.

The same can be applied to the `cache` of the dynamic prime factorization.

The **better solution** is to **ditch the hash-maps**; Instead use arrays; We can use arrays cause our **keys are unique and we can map them to indexes**.

> You should initially fill the cache with some out of range number (like `-1`) to indicate which indexes are not yet calculated.
>
> Also, for the sake of being zero based, you should allocate one more room or decrease every key by `1` when accessing the array.
>
> Using static size arrays are the best as it requires no reallocating and copying; But, can the memory allocate `10^6 * 4 bytes` at once? Yes, it's only `3.8 MB`.

Do this for both `cache`s and you'll be stunned by the resulting efficiency.

> This optimization has the potential to reduce the memory usage by `150 MB`! (*Java)
>
> Also, the program runs faster; Because no hash-map related operation is present now.

### IO buffers

We can also assign an appropriate input and output buffers' sizes to **minimize time consumption of IO**.

We should read 2 numbers per test case (separated by a space) plus a line terminator; The maximum characters of a number is `7` which is for test case `a = 10^6, b = 10^6`; There are `k` test cases (`k` is given in the first line); So, to buffer all of it (**read all before solving**), we'll need `k * (7 + 1 + 7 + 1)` characters; You can take the maximum `k = 10^5` (then, don't forget to add an additional `6 + 1` to the buffer size, for `k` itself) or first read `k` from the input without messing the input and then assign the input buffer size based on the read `k`.

Going with the first approach (maximum `k`) is much easier,

```java
private static final FastNonBlankReader in = new FastNonBlankReader(new BufferedReader(new InputStreamReader(System.in), (int) 1e6 * (7 + 1 + 7 + 1)));
```

For output, we should print a number plus a line terminator `k` times; The maximum characters of an output number is `7` which is for test case `a = 10^6, b = 1`; So, to buffer all of it (**delaying flush**), we'll need `k * (1 + 7)` characters; You can take the maximum `k = 10^5` or use the already read `k`.

Going with the latter approach,

```java
private static final FastNonBlankReader in = ...;
private static PrintWriter out;

public static void main(String[] args) {
    final int k = in.nextInt();
    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out), k * (1 + 7)));
    ...
}
```

> This optimization **at most** halves the runtime for each test. (*Java)