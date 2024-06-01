import parcs.*;

import java.util.ArrayList;

public class FermatTest implements AM {

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
        long res = 1;
        x = x % p;

        while (y > 0) {
            if ((y & 1) == 1) {
                res = (res * x) % p;
            }

            y = y >> 1;
            x = (x * x) % p;
        }
        return res;
    }

    static boolean fermatTest(int n, int k) {
        if (n <= 1 || n == 4) return false;
        if (n <= 3) return true;

        for (int i = 0; i < k; i++) {
            int a = 2 + (int)(Math.random() % (n - 4));

            if (power(a, n - 1, n) != 1) {
                return false;
            }
        }
        return true;
    }

}
