# J

> Limits are self-assigned based on the minimal solution.

- Time limit: 1 second
- Memory limit: 128 MB

## Simplify

Is there a **subset** of the given array which `m` would divide **sum** of its elements?

> Each array element is known by pair of `(index, value)`; Thus, `(0, 10)` and `(1, 10)` are not the same elements.

## Think

Obviously, the `Ѡ(2^n)` solution (generating and checking all subsets) doesn't fit into the limits (time and memory).

Let's try to **remove the edges** of the `Ѡ(2^n)` solution.

- Only a **`YES` or `NO`** is required; There might be no need to keep track of the subsets' elements.
- **`mod`** (`%`) operation plays an important rule in the question, but, it's not considered as first class in this solution.

To ease talking, I'll use the phrase **"sum of a set"** as an abbreviate for "sum of elements' of a set".

Let's answer a small question. Imagine there exist 3 sets in which `2` would divide sum of any (`sum % 2 = 0`); **What new sets we can obtain** if we add element `1` to any?

We don't know what the new sums would be, but surely they would fit into this equation: `sum % 2 = 1`.

So now, we have 3 sets with `sum % 2 = 0` and 3 sets with `sum % 2 = 1`.

What new sets we can obtain if we add element `7` to any of the existing sets?

3 new sets in which `sum % 2 = 1` and 3 new sets in which `sum % 2 = 0`; Let's illustrate in a table.

| After operation | `sum % 2 = 0` | `sum % 2 = 1` |
| :-------------: | :-----------: | :-----------: |
|                 |       3       |       0       |
|  + element `1`  |       3       |     0 + 3     |
|  + element `7`  |     3 + 3     |     3 + 3     |

By this **dynamic** approach, we can solve the problem in `O(n * m)`; We have `n` elements to add and in each step, we have to check and update `m` rooms (`sum % m = 0, sum % m = 1, ..., sum % m = m - 1`).

Also, be aware of **duplicate leaks**; In each iteration of adding a new element, we should construct a new row based on the previous row and not update the existing row (previous row, a single row). Also, not adding the base cases (one element sets) at the appropriate time would cause duplicate leaks.

Finally, we can check if there is any set in the `sum % m = 0` room and answer with `YES` or `NO`.

Assume the given elements lie in the `as` array,

```java
public boolean solve() {
	int[] s = new int[m]; // The previous row
    
    for (final int a : as) { // a; The new element to add
		final int[] st = new int[m]; // The new row
        
		for (int i = 0; i < m; i++) { // i; sum % m = i
			final int ii = ((a % m) + i) % m; // ii; The new sets' (adding a) room
			st[ii] = s[ii] + s[i];
		}
        
	    st[a % m]++; // Adding the base case, single element set {a}
		s = st;
	}
    
    return s[0] > 0; // true -> YES, false -> NO
}
```

According to my brain cells, There is a flaw within this code; **Overflow**!

What's the maximum value that a room should hold? Potentially, quantity of all non-empty subsets, `2^n - 1`, which wouldn't fit in any primitive structure.

> The maximum value for `n`  is `5 * 10^5`.

Let's use `BigInteger`! Surely the time limit truck will hit you; The amount of subsets is just too big for any calculation.

There is another edge that we can dismiss; **Countings**. We can **use booleans** instead of numbers; In the `i`th room, `true` for "there exist 1 or more sets in which `sum % m = i`" and `false` for the opposite.

Now, the solution is purely `ϴ(n * m)`. Still time limits.

Fortunately, the problem can't be solved by a more efficient approach (reducing the upper-bound complexity), but we can change the tight-bound (`ϴ`) by adding a simple **greedy** check.

After each iteration, if the `sum % m = 0` room is non-empty (`true`), then the answer is `YES`; Else, carry on.

Now its `O(n * m)` and `Ѡ(m)`. We can still reduce the lower-bound to `Ѡ(min(m, n))` by doing an **early iteration** over the elements (checking subsets with only a single element).

## Code

For the complete code (including the over-engineerings), see the [associated code file](../src/J.java) in the [`src`](../src) directory.

```java
import java.io.*;
import java.util.*;

public final class J {

    private static final FastNonBlankReader in = ...;
    private static final PrintWriter out = ...;

    public static void main(String[] args) {
        final int n = in.nextInt(), m = in.nextInt();
        final int[] as = new int[n];
        for (int i = 0; i < n; i++) {
            as[i] = in.nextInt();
        }

        out.println(new J(as, m).solve() ? "YES" : "NO");

        out.flush(); // Mandatory
    }

    private final int[] as;
    private final int m;

    public J(final int[] as, final int m) {
        this.as = as;
        this.m = m;
    }

    public boolean solve() {
        for (final int a : as) { // Early iteration; Checking singular subsets.
            if (a % m == 0) return true;
        }

        boolean[] s = new boolean[m]; // The previous row

        for (final int a : as) { // a; The new element to add
            final boolean[] st = new boolean[m]; // The new row

            for (int i = 0; i < m; i++) { //i; sum % m = i
                final int ii = ((a % m) + i) % m; // ii; The new sets' (adding a) room
                st[ii] = s[ii] || s[i];
            }

            st[a % m] = true; // Adding the base case, single element set {a}
            s = st;

            if (s[0]) return true; // Greedy check
        }

        return false;
    }

    private static final class FastNonBlankReader implements Closeable {
        ...
    }

}
```

## Over-engineering

### Space complexity

Without any deallocating, space complexity of the minimal solution is `Ѡ(min(n, m))` and `O(n * m)`. We can reduce the upper-bound to `O(max(n, 2 * m))` by allocating only 2 arrays and **swapping** them in each iteration.

Although, keep in mind that the act of deallocating (by user or garbage collector) may happen to do that by **default**!