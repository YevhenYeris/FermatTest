import parcs.*;

import java.util.ArrayList;

public class FermatTest implements AM {

    public void run(AMInfo info) {
        System.out.println("From worker");
        ArrayList<Integer> data = (ArrayList<Integer>) info.parent.readObject();
        int k = data.get(0);

        System.out.println("Received k = " + k);

        boolean[] results = new boolean[data.size() - 1];

        System.out.println("Result size is = " + results.length);

        for (int i = 1; i < data.size(); i++) {
            results[i - 1] = fermatTest(data.get(i), k);
        }

        System.out.println("Process completed.");

        info.parent.write(results);
    }

    static long nestedPower(long x, long y, long p) {
        long res = 1;
        for (long i = 0; i < y; i++) {
            res = (res * x) % p;
        }
        return res;
    }

    static boolean fermatTest(int n, int k) {
        if (n <= 1 || n == 4) return false;
        if (n <= 3) return true;

        for (int i = 0; i < k; i++) {
            int a = 2 + (int)(Math.random() * (n - 4));

            if (nestedPower(a, n - 1, n) != 1) {
                return false;
            }
        }
        return true;
    }
}
