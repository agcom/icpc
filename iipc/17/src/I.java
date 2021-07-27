import java.io.*;
import java.util.*;

public final class I {

    private static final FastNonBlankReader in = new FastNonBlankReader(new BufferedReader(new InputStreamReader(System.in))); // 16 KB default buffer
    private static final PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))); // 16 KB default buffer

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
                out.flush();
            }
        }

        out.flush(); // Mandatory
    }

    private final int[] ms;

    private final TreeMap<Integer, Integer> fs; // Frees; Charity index -> free space (for money)
    private final int[] bs; // Balances

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

        fs = new TreeMap<>(msm);
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