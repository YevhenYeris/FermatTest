import parcs.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FermatTest implements AM {

    public void run(AMInfo info) {
        ArrayList<Integer> data = (ArrayList<Integer>) info.parent.readObject();
        int k = data.get(0);

        ArrayList<Integer> numbers = getInputData(data.get(1), data.get(2));

        writeOutputData("test.txt", "Processing numbers: [" + numbers.get(0) + ", " + numbers.get(numbers.size() - 1) + "].");

        boolean[] results = new boolean[numbers.size()];

        for (int i = 0; i < numbers.size(); i++) {
            results[i] = fermatTest(numbers.get(i), k);
        }

        info.parent.write(results);
    }

    private static void writeOutputData(String filename, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(data);
            System.out.println("Output: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static ArrayList<Integer> getInputData(int startIndex, int endIndex) {
        ArrayList<Integer> values = new ArrayList<>();

        for (int i = startIndex; i <= endIndex; ++i) {
            values.add(i);
        }

        return values;
    }
}
