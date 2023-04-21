import java.io.*;
import java.util.*;

public final class C {
	
	private static final FastNonBlankReader in = new FastNonBlankReader(new BufferedReader(new InputStreamReader(System.in))); // 16 KB default buffer
	private static final PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))); // 16 KB default buffer
	
	public static void main(final String[] args) {
		final int n = in.nextInt();
		final int[] as = new int[1 << n];
		for (int i = 0; i < as.length; i++) {
			as[i] = in.nextInt();
		}
		
		final List<Edge> swaps = new C(as).computeSwaps();
		out.println(swaps.size());
		for (Edge swap : swaps) {
			out.println(swap.u + " " + swap.v);
		}
		
		out.flush(); // Mandatory
	}
	
	private final int[] as;
	
	private final ArrayList<Integer>[] adjacency;
	private final int[] asSorted;
	private final Map<Integer, Integer> asReverse;
	private final List<Edge> swaps;
	
	public C(final int[] as) {
		this.as = as;
		
		// Init adj
		adjacency = new ArrayList[as.length];
		for (int i = 0; i < adjacency.length; i++) {
			adjacency[i] = new ArrayList<>(adjacency.length);
		}
		
		for (int i = 0; i < as.length - 1; i++) {
			for (int j = i + 1; j < as.length; j++) {
				if (areOneBitPosDiff(i, j)) {
					adjacency[i].add(j);
					adjacency[j].add(i);
				}
			}
		}
		
		// Init asSorted
		asSorted = Arrays.copyOf(as, as.length);
		Arrays.sort(as);
		
		// Init asRev
		asReverse = new HashMap<>(as.length * 2);
		for (int i = 0; i < as.length; i++) {
			final int a = as[i];
			asReverse.put(a, i);
		}
		
		// Init swaps
		swaps = new LinkedList<>();
	}
	
	private List<Edge> computeSwaps() {
		for (int i = 0; i < as.length; i++) {
			final int j = asReverse.get(asSorted[i]);
			if (i != j) {
				smartSwap(i, j);
			}
		}
		
		assert isSorted(as);
		assert swaps.size() < 12000;
		
		return swaps;
	}
	
	private static boolean isSorted(final int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i] > arr[i + 1]) {
				return false;
			}
		}
		
		return true;
	}
	
	private void smartSwap(final int i, final int j) {
		final LinkedList<Edge> shortestPathRes = findShortestPath(i, j);
		for (final Edge e : shortestPathRes) {
			immediateSwap(e.u, e.v);
			swaps.add(e);
		}
		
		final Iterator<Edge> shortestPathResRevIt = shortestPathRes.descendingIterator();
		shortestPathResRevIt.next();
		while (shortestPathResRevIt.hasNext()) {
			final Edge e = shortestPathResRevIt.next();
			immediateSwap(e.u, e.v);
			swaps.add(e);
		}
	}
	
	private LinkedList<Edge> findShortestPath(final int start, final int end) {
		final int[] parents = new int[adjacency.length];
		Arrays.fill(parents, -1);
		parents[start] = start;
		
		final boolean[] visiteds = new boolean[adjacency.length];
		visiteds[start] = true;
		
		final Queue<Integer> queue = new LinkedList<>();
		queue.add(start);
		
		while (!queue.isEmpty()) {
			final int current = queue.poll();
			
			for (final int neighbor : adjacency[current]) {
				if (!visiteds[neighbor]) {
					visiteds[neighbor] = true;
					parents[neighbor] = current;
					queue.add(neighbor);
				}
			}
		}
		
		final LinkedList<Edge> shortestPath = new LinkedList<>();
		int current = end;
		while (current != start) {
			final int parent = parents[current];
			shortestPath.addFirst(new Edge(parent, current));
			current = parent;
		}
		
		return shortestPath;
	}
	
	private void immediateSwap(final int i, final int j) {
		final int t = as[i];
		as[i] = as[j];
		as[j] = t;
		
		asReverse.put(as[i], i);
		asReverse.put(as[j], j);
	}
	
	private static boolean areOneBitPosDiff(final int i, final int j) {
		final int xor = i ^ j;
		return xor != 0 && ((xor) & (xor - 1)) == 0;
	}
	
	private static final class Edge {
		public final int u, v;
		
		public Edge(final int u, final int v) {
			this.u = u;
			this.v = v;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			else if (o == null || getClass() != o.getClass()) return false;
			
			final Edge other = (Edge) o;
			return (u == other.u && v == other.v) || (v == other.u && u == other.v);
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