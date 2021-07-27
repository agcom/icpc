import java.io.*;
import java.util.*;

public final class J {

    private static final FastNonBlankReader in = new FastNonBlankReader(new BufferedReader(new InputStreamReader(System.in))); // 16 KB default buffer
    private static final PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))); // 16 KB default buffer

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
        boolean[] st = new boolean[m]; // The new row

        for (final int a : as) { // a; The new element to add

            for (int i = 0; i < m; i++) { //i; sum % m = i
                final int ii = ((a % m) + i) % m; // ii; The new sets' (adding a) room
                st[ii] = s[ii] || s[i];
            }

            st[a % m] = true; // Adding the base case, single element set {a}
            // Swap s and st
            final boolean[] t = s;
            s = st;
            st = t;

            if (s[0]) return true; // Greedy check
        }

        return false;
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