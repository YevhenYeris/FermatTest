import parcs.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FermatTest implements AM {

    public void run(AMInfo info) {
        ArrayList<Integer> data = (ArrayList<Integer>) info.parent.readObject();
        int k = data.get(0);

        boolean[] results = new boolean[data.get(2) - data.get(1) + 1];

//        for (int i = data.get(1); i < data.get(2); i++) {
//            results[i - 1] = fermatTest(i, k);
//        }

        for (int i = 0; i < data.get(1); i++) {
            results[i] = fermatTest(i, k);
        }

        for (int i = data.get(1); i < data.get(2); i++) {
            results[i] = fermatTest(i, k);
        }

        for (int i = data.get(2); i < data.get(3); i++) {
            results[i] = fermatTest(i, k);
        }

        for (int i = data.get(3); i < data.get(4); i++) {
            results[i] = fermatTest(i, k);
        }

        for (int i = data.get(4); i < data.get(5); i++) {
            results[i] = fermatTest(i, k);
        }

        info.parent.write(results);
    }

    static long power(long x, long y, long p) {
        long res = 1;

        for (long i = 0; i < y; i++) {
            long tempRes = 0;

            for (long j = 0; j < x; j++) {
                tempRes = (tempRes + res) % p;
            }
            res = tempRes;
        }

        return res;
    }

    static boolean fermatTest(int n, int k) {
        if (n <= 1 || n == 4) return false;
        if (n <= 3) return true;

        for (int i = 0; i < k; i++) {
            int a = 2 + (int)(Math.random() * (n - 4));

            if (power(a, n - 1, n) != 1) {
                return false;
            }
        }
        return true;
    }
}
