# D

- Time limit: 1 second
- Memory limit: 16 MB

## Simplify

For each team, count its **given goals**, **taken goals** and **score**; Then **sort** them as stated in the problem.

## Think

We're given a square matrix. Each room `(i, j)` shows half information about a game; The goals team `i` gave to `j`. For the full information, goals team `j` gave to `i`, we should check the room `(j, i)`.

We'll create an object for each team which holds given goals, taken goals and score. Then, **iterate** over the lower triangular of the given square matrix (iterate over every game) and update the related teams' objects.

Finally, **sort** the objects; First based on the scores (descending), then, given goals minus taken goals (descending) and then, team numbers (ascending).

- Time complexity (excluding reading inputs): `O(n * log(n))`
- Space complexity (excluding inputs): `O(n)`

> With inputs considered,
>
> - Time complexity: `O(n^2)`
> - Space complexity: `O(n^2)`

## Code

For the full code (including the over-engineerings), see the [associated code file](../src/D.java) in the [`src`](../src) folder.

```java
import java.io.*;
import java.util.*;

public final class D {

    private static final FastNonBlankReader in = ...;
    private static final PrintWriter out = ...;

    public static void main(String[] args) {
        final int n = in.nextInt();
        final int[][] mat = new int[n][n]; // Matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.nextInt();
            }
        }

        final int[] ans = new D(mat).solve(); // Answer
        for (final int t : ans) { // For each team
            out.print((char) ('a' + t)); // Map to team's character
        }
        out.println();

        out.flush(); // Mandatory
    }

    private final int[][] mat;

    public D(final int[][] mat) {
        this.mat = mat;
    }

    public int[] solve() {
        final Team[] ts = new Team[mat.length]; // Teams
        for (int id = 0; id < ts.length; id++) {
            ts[id] = new Team(id);
        }

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < i; j++) {
                final Team ti = ts[i], tj = ts[j];
                final int ij = mat[i][j], ji = mat[j][i]; // Goals i to j, goals j to i

                ti.givenGoals += ij;
                tj.takenGoals += ij;

                tj.givenGoals += ji;
                ti.takenGoals += ji;

                if (ij == ji) { // Draw game. 1 score for each.
                    ti.score++;
                    tj.score++;
                } else if (ij > ji) ti.score += 3; // Team i won. 3 score for the winner.
                else tj.score += 3; // Team j won. 3 score for the winner.
            }
        }

        Arrays.sort(
                ts,
                Comparator
                        .<Team, Integer>comparing(t -> t.score, Comparator.reverseOrder()) // First, based on the scores (descending).
                        .thenComparing(t -> t.givenGoals - t.takenGoals, Comparator.reverseOrder()) // Then, based on the given goals minus taken goals (descending).
                        .thenComparing(t -> t.id) // And then, based on the teams' ids (ascending).
        );

        return Arrays.stream(ts).map(t -> t.id).mapToInt(Integer::intValue).toArray();
    }

    private static final class Team {
        public int id, givenGoals = 0, takenGoals = 0, score = 0;

        public Team(final int id) {
            this.id = id;
        }
    }

    private static final class FastNonBlankReader implements Closeable {
        ...
    }

}
```

## Over-engineering

Goal difference means, number of goals scored by a team (given goals), minus the number of goals conceded (taken goals).

In `Team` class we can merge two variables `givenGoals` and `takenGoals` into one variable **`goalDiff`**.