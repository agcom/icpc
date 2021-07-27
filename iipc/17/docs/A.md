# A

> Limits are self-assigned based on the minimal solution.

- Time limit: 0.2 seconds
- Memory limit: 16 MB

## Simplify

Size of the **set** `{every x such that x ∈ N and x <= n and (a | x or b | x or c | x or d | x)}`.

> The notation `a | b` means that `a` divides `b`; For example, `3 | 6`, since `3 * 2 = 6`.
>
> `n`, `a`, `b`, `c` and `d` are given constants.

## Think

Imagine instead of `a, b, c, d`, we were given only one number, `a`.

Now, coin the definition; `{every x = z * a such that z ∈ [1, n / a]} = s(a)`;`s(a)` contains **multiplies of `a`** not bigger than `n`.

**`|s(a)|`**? For each unique `z`, `x = z * a` is a unique number; So, the set contains `⌊n / a⌋ - 1 + 1 = ⌊n / a⌋` members.

> `|s|` notation means size of the set `s`.

By the above definition, the full question asks for **`|s(a) ∪ s(b) ∪ s(c) ∪ s(d)|`**; And according to sets inclusivity/exclusivity,

```
|s(a) ∪ s(b) ∪ s(c) ∪ s(d)|
	= |s(a)| + |s(b)| + |s(c)| + |s(d)|
		- (|s(a) ∩ s(b)| + |s(a) ∩ s(c)| + |s(a) ∩ s(d)| + |s(b) ∩ s(c)| + |s(b) ∩ s(d)| + |s(c) ∩ s(d)|)
		+ |s(a) ∩ s(b) ∩ s(c)| + |s(a) ∩ s(b) ∩ s(d)| + |s(a) ∩ s(c) ∩ s(d)| + |s(b) ∩ s(c) ∩ s(d)|
		- |s(a) ∩ s(b) ∩ s(c) ∩ s(d)|
```

> [What is the inclusion-exclusion principle for 4 sets?](https://math.stackexchange.com/questions/688019/what-is-the-inclusion-exclusion-principle-for-4-sets)

Before we would be able to use the above formula, we should define the intersections, `|s(a) ∩ s(b) ∩ ...|`; The trick is to use least common multiple (LCM); **`s(a) ∩ s(b) ∩ ... = s(lcm(a, b, ...))`**.

> For example, `s(8) ∩ s(4) = s(8)`, `s(6) ∩ s(4) ∩ s(3) = s(12)`.

So we need to calculate,

```
|s(a) ∪ s(b) ∪ s(c) ∪ s(d)|
	= |s(a)| + |s(b)| + |s(c)| + |s(d)|
		- (|s(lcm(a, b))| + |s(lcm(a, c))| + |s(lcm(a, d))| + |s(lcm(b, c))| + |s(lcm(b, d))| + |s(lcm(c, d))|)
		+ |s(lcm(a, b, c))| + |s(lcm(a, b, d))| + |s(lcm(a, c, d))| + |s(lcm(b, c, d))|
		- |s(lcm(a, b, c, d))|
```

Calculating `|s(a)|` works in `O(1)` (discussed above) and [finding LCMs](https://en.wikipedia.org/wiki/Euclidean_algorithm#Algorithmic_efficiency) in `O(log(max(b, c, d)))`.

## Code

For the complete code, see the [associated code file](../src/A.java) in the [`src`](../src) directory.

```java
import java.io.*;
import java.util.*;

public final class A {

    private static final FastNonBlankReader in = ...;
    private static final PrintWriter out = ...;

    public static void main(String[] args) {
        final int n = in.nextInt(),
                a = in.nextInt(), b = in.nextInt(), c = in.nextInt(), d = in.nextInt();

        out.println(new A(n, a, b, c, d).solve());

        out.flush(); // Mandatory
    }

    private final int n, a, b, c, d;

    public A(final int n, final int a, final int b, final int c, final int d) {
        this.n = n;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public int solve() {
        return s(a) + s(b) + s(c) + s(d)
                - (s(lcm(a, b)) + s(lcm(a, c)) + s(lcm(a, d)) + s(lcm(b, c)) + s(lcm(b, d)) + s(lcm(c, d)))
                + s(lcm(a, b, c)) + s(lcm(a, b, d)) + s(lcm(a, c, d)) + s(lcm(b, c, d))
                - s(lcm(a, b, c, d));
    }

    private int s(final int a) {
        return n / a;
    }

    private static int lcm(final int... arr) {
        int res = arr[0];
        for (int i = 1; i < arr.length; i++) {
            res = lcm(res, arr[i]);
        }
        return res;
    }

    private static int lcm(final int a, final int b) {
        return (a * b) / gcd(a, b);
    }

    private static int gcd(final int a, final int b) {
        if (b == 0) return a;
        else return gcd(b, a % b);
    }

    private static final class FastNonBlankReader implements Closeable {
        ...
    }

}
```