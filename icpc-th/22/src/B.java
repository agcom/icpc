import java.util.Scanner;

public class B {
	public static void main(final String[] args) {
		final Scanner scanner = new Scanner(System.in);
		final int n = scanner.nextInt();
		final int[] stats = new int[n];
		for (int i = 0; i < n; i++) {
			stats[i] = scanner.nextInt();
		}
		
		int delta = 0;
		for (int i = 1; i < stats.length; i++) {
			if (stats[i] > stats[i - 1]) {
				delta += stats[i] - stats[i - 1];
				stats[i] = stats[i - 1];
			}
		}
		
		System.out.println(delta);
	}
}
