import java.io.*;
import java.util.*;

public final class D {

    private static final FastNonBlankReader in = new FastNonBlankReader(new BufferedReader(new InputStreamReader(System.in))); // 16 KB default buffer
    private static final PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))); // 16 KB default buffer

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

                ti.goalDiff += ij - ji;
                tj.goalDiff += ji - ij;

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
                        .thenComparing(t -> t.goalDiff, Comparator.reverseOrder()) // Then, based on the goal diff (given goals minus taken goals) (descending).
                        .thenComparing(t -> t.id) // And then, based on the teams' ids (ascending).
        );

        return Arrays.stream(ts).map(t -> t.id).mapToInt(Integer::intValue).toArray();
    }

    private static final class Team {
        public int id, goalDiff = 0, score = 0;

        public Team(final int id) {
            this.id = id;
        }
    }

    private static final class FastNonBlankReader implements Closeable {
        private final BufferedReader br;
        private StringTokenizer st;

        private FastNonBlankReader(final BufferedReader bufferedReader) {
            br = bufferedReader;
            st = new StringTokenizer("");
        }

        public String nextLine() {
            if (st.hasMoreTokens()) {
                final StringBuilder sb = new StringBuilder();
                do {
                    sb.append(st.nextToken());
                } while (st.hasMoreTokens());
                return sb.toString();
            } else return readLine();
        }

        public String next() {
            while (!st.hasMoreTokens()) {
                st = new StringTokenizer(readLine());
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        private String readLine() {
            try {
                return br.readLine();
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public void close() {
            st = null;
            try {
                br.close();
            } catch (final IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}