import java.io.*;
import java.util.*;

public final class A {

    private static final FastNonBlankReader in = new FastNonBlankReader(new BufferedReader(new InputStreamReader(System.in))); // 16 MB default buffer
    private static final PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))); // 16 MB default buffer

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