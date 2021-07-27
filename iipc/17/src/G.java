import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public final class G {

    private static final FastNonBlankReader in = new FastNonBlankReader(new BufferedReader(new InputStreamReader(System.in), (int) 1e6 * (7 + 1 + 7 + 1)));
    private static PrintWriter out;

    public static void main(String[] args) {
        final int k = in.nextInt();
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out), k * (1 + 7)));

        final G solver = new G();
        for (int i = 0; i < k; i++) {
            final int a = in.nextInt(), b = in.nextInt();
            out.println(solver.solve(a, b));
        }

        out.flush(); // Mandatory
    }

    private final int[] cache = new int[1000000 + 1];
    private final PrimeFactorization pf = new PrimeFactorization();

    {
        cache[1] = 0; // The base case
        Arrays.fill(cache, 2, cache.length, -1);
    }

    public int solve(final int a, final int b) {
        ensureCached(a);
        ensureCached(b);
        return cache[a] - cache[b];
    }

    private void ensureCached(final int n) {
        if (cache[n] == -1) calculate(n);
    }

    private void calculate(final int n) {
        int low = n - 1;
        while (cache[low] == -1) low--; // Find the lowest cached (calculated) factor

        // Calculate low + 1 till n
        for (int i = low + 1; i <= n; i++) {
            cache[i] = cache[i - 1] + pf.primeFactorsQuantity(i);
        }
    }

    private static final class PrimeFactorization {

        private static final int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997};

        private final int[] cache = new int[1000000 + 1];

        {
            cache[1] = 0; // Base case
            Arrays.fill(cache, 2, cache.length, -1); // Index 0 is not used and 1 is assigned to 0 (base case).
        }

        public int primeFactorsQuantity(final int f) {
            return primeFactorsQuantity(f, 0);
        }

        // pi: The primes array index to continue searching on.
        private int primeFactorsQuantity(final int f, final int pi) {
            if (cache[f] != -1) return cache[f];

            int count = 1; // If this variable wasn't assigned below, then f is a prime with 1 prime factor.
            for (int i = pi; i < primes.length && primes[i] * primes[i] <= f; i++) {
                if (f % primes[i] == 0) {
                    count = primeFactorsQuantity(f / primes[i], i) + 1; // +1 for the current found prime factor (primes[i])
                    break;
                }
            }

            cache[f] = count;
            return count;
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