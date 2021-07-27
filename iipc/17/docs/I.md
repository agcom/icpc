# I

> Limits are self-assigned based on the minimal solution.

- Time limit: 3 seconds
- Memory limit: 256 MB

## Simplify

**Distribute the money** on the limited charities in a defined order.

## Think

Obviously, the problem can be solved in `O(q*n)` time complexity; But, it hits the time limit, barely.

> More precisely, if there are `b` balance requests and `d` deposit requests (`b + d = q`), the problem can be solved in `O(b + d * n)` or `O(d + b * n)`.

There is a more efficient solution. The idea is to remove charities as soon as they **reach their limit**.

So, for each deposit, we'll find the first charity **higher than or equal** to the target charity (using binary search), then, deposit into the found charity and its higher charities. In the mean time, don't forget the above idea.

Each balance will be fulfilled in `O(log(n))` and each deposit in `O(h * log(n))` where `h` is the number of charities the money been distributed on.

> For each deposit, finding the first charity takes `O(log(n))` and finding the latter successors would take `O((h - 1) * log(n))`.

Notice that in each deposit, `h - 1` charities will be removed (reached their limits). There are only `n` charities to be removed; So, sum of `h - 1`s over all deposits is at most `n`.

The overall time complexity is `O(n * log(n) + b * log(n) + d * log(n)) = O((n + q) * log(n))`, where `b` is number of balance requests and `d` is number of deposit requests (`b + d = q`).

> This is just an upper bound.

## Code

For the full code (including the over-engineerings), see the associated [code file](../src/I.java) in the [`src`](../src) directory.

```java
import java.io.*;
import java.util.*;

public final class I {

    private static final FastNonBlankReader in = ...;
    private static final PrintWriter out = ...;

    public static void main(String[] args) {
        final int n = in.nextInt(), q = in.nextInt();
        final int[] ms = new int[n]; // Maxes
        for (int i = 0; i < ms.length; i++) {
            ms[i] = in.nextInt();
        }

        final I solver = new I(ms);
        for (int i = 0; i < q; i++) {
            final int type = in.nextInt(), target = in.nextInt() - 1; // Zero based
            if (type == 1) { // Deposit
                final int amount = in.nextInt();
                solver.deposit(target, amount);
            } else { // Balance
                final int balance = solver.balance(target);
                out.println(balance);
            }
        }

        out.flush(); // Mandatory
    }

    private final int[] ms;

    private final TreeMap<Integer, Integer> fs; // Frees; Charity index -> free space (for money)

    public I(final int[] ms) {
        this.ms = ms;
        fs = new TreeMap<>();
        for (int i = 0; i < ms.length; i++) {
            fs.put(i, ms[i]);
        }
    }

    public void deposit(final int target, int amount) {
        final Iterator<Map.Entry<Integer, Integer>> it = fs.tailMap(target, true).entrySet().iterator();
        while (it.hasNext() && amount > 0) {
            final Map.Entry<Integer, Integer> entry = it.next();
            final int index = entry.getKey(), free = entry.getValue();

            if (amount >= free) it.remove();
            else entry.setValue(free - amount);
            
            amount -= free;
        }
    }

    public int balance(final int target) {
        return ms[target] - fs.getOrDefault(target, 0); // Max - free; 0 if it's removed from the map.
    }

    private static final class FastNonBlankReader implements Closeable {
        ...
    }

}
```

## Over-engineering

### More efficient balance requests

The balance requests can be fulfilled in `O(1)` if we **keep track of the balances**.

```java
private final int[] bs; // Balances

public I(final int[] ms) {
    ...
    bs = new int[ms.length];
}

public void deposit(final int target, int amount) {
	final Iterator<Map.Entry<Integer, Integer>> it = fs.tailMap(target, true).entrySet().iterator();
	while (it.hasNext() && amount > 0) {
		final Map.Entry<Integer, Integer> entry = it.next();
		final int index = entry.getKey(), free = entry.getValue();

		if (amount >= free) {
			it.remove();
			bs[index] = ms[index];
		} else {
            entry.setValue(free - amount);
            bs[index] += amount;
        }
		
		amount -= free;
	}
}

public int balance(final int target) {
    return bs[target];
}
```

This changes the overall time complexity to `O(n * log(n) + b + d * log(n)) = O((n + d) * log(n) + b)`.

> This optimization actually increases the overall execution time! I guess this is because of the added memory access constants (`bs`) when doing deposits. The `q = b + d` and/or `n` are just not big enough.

### Self-balancing binary search tree linear initialization

Initializing the self-balancing binary search tree takes `O(n * log(n))`. We can reduce that to `O(n)` if we're **already inserting the elements in order** (which indeed we are).

Java does it internally if we pass a `SortedMap` to the `putAll` method. So, we should create a `SortedMap` implementation with our ordered data inside.

I used the fastest way, using `LinkedHashMap`,

```java
public I(final int[] ms) {
    this.ms = ms;

	final class MockedSortedMap extends LinkedHashMap<Integer, Integer> implements SortedMap<Integer, Integer> {
		public MockedSortedMap(int initialCapacity) {
			super(initialCapacity, Float.MAX_VALUE); // Don't grow
		}

		@Override
		public Comparator<? super Integer> comparator() {
			return Integer::compareTo;
        }

	    private void unsupported() {
			throw new UnsupportedOperationException("Dummy sorted HashMap");
		}

	    @Override
		public SortedMap<Integer, Integer> subMap(Integer fromKey, Integer toKey) {
	            unsupported();
	        return null;
		}

        @Override
	    public SortedMap<Integer, Integer> headMap(Integer toKey) {
		    unsupported();
			return null;
        }

	    @Override
		public SortedMap<Integer, Integer> tailMap(Integer fromKey) {
            unsupported();
	        return null;
		}
            
	    @Override
		public Integer firstKey() {
                unsupported();
	        return null;
		}

	    @Override
		public Integer lastKey() {
			unsupported();
			return null;
		}
	}

	final MockedSortedMap msm = new MockedSortedMap(ms.length);
	for (int i = 0; i < ms.length; i++) {
		msm.put(i, ms[i]);
	}
    
    fs = new TreeMap<>(msm); // Does O(n) initialization
    bs = new int[ms.length];
}
```

This over-engineering doesn't changes the time complexity.

> Using this `MockedSortedMap`, it actually increases the overall execution time! This is because the `q = b + d` and/or `n` are just not big enough to beat this `MockedSortedMap` constant overhead.
>
> Using a very custom and efficient implementation of the `SortedMap` resolves this.