import java.io.*;
import java.util.StringTokenizer;

public final class A {
	
	private static final FastNonBlankReader in = new FastNonBlankReader(new BufferedReader(new InputStreamReader(System.in))); // 16 KB default buffer
	private static final PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out))); // 16 KB default buffer
	
	public static void main(final String[] args) {
		final int p = in.nextInt(), q = in.nextInt();
		if (p <= 50 && q <= 10) out.println("White");
		else if (q > 30) out.println("Red");
		else out.println("Yellow");
		
		out.flush(); // Mandatory
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