import parcs.*;

import java.util.ArrayList;
import java.util.Random;

public class FermatTask implements AM {

    public void run(AMInfo info) {
        ArrayList<Integer> data = (ArrayList<Integer>) info.parent.readObject();
        int k = data.get(0);
        boolean[] results = new boolean[data.size() - 1];

        for (int i = 1; i < data.size(); i++) {
            results[i - 1] = fermatTest(data.get(i), k);
        }

        info.parent.write(results);
    }

    static long power(long x, long y, long p) {
        long res = 1; // Initialize result
        x = x % p; // Update x if it is more than or equal to p

        while (y > 0) {
            // If y is odd, multiply x with the result
            if ((y & 1) == 1) {
                res = (res * x) % p;
            }

            // y must be even now
            y = y >> 1; // y = y/2
            x = (x * x) % p; // Change x to x^2
        }
        return res;
    }

    // Method to check if a number is prime using Fermat's little theorem
    static boolean fermatTest(int n, int k) {
        // Corner cases
        if (n <= 1 || n == 4) return false;
        if (n <= 3) return true;

        // Try k times
        for (int i = 0; i < k; i++) {
            // Pick a random number in [2, n-2]
            // n-4 is chosen to avoid overflow when n is large
            int a = 2 + (int)(Math.random() % (n - 4));

            // Fermat's little theorem
            if (power(a, n - 1, n) != 1) {
                return false;
            }
        }
        return true;
    }

}
