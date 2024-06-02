import parcs.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class FermatTestApplication {

    public static void main(String[] args) throws Exception {

        String inputValue = System.getenv("INPUT_VALUE");
        String testPrecision = System.getenv("TEST_PRECISION");
        String outputFile = System.getenv("OUTPUT_FILE");
        String workersNumberString = System.getenv("WORKERS_NUMBER");

        task task = new task();
        task.addJarFile("FermatTest.jar");

        int k = Integer.parseInt(testPrecision);
        ArrayList<Integer> values = getInputData(inputValue);

        int workersNumber = Integer.parseInt(workersNumberString);
        int chunkSize = values.size() / workersNumber;

        AMInfo amInfo = new AMInfo(task, null);

        point[] points = new point[workersNumber];
        channel[] channels = new channel[workersNumber];

        for (int i = 0; i < workersNumber; i++) {
            points[i] = amInfo.createPoint();
            channels[i] = points[i].createChannel();
            points[i].execute("FermatTest");

            int startIndex = i * chunkSize;
            int endIndex = (i == workersNumber - 1) ? values.size() - 1 : startIndex + chunkSize - 1;

            System.out.println("Worker #" + i + ": processing the range [" + (startIndex + 1) + ", " + (endIndex + 1) + "].");

            ArrayList<Integer> data = new ArrayList<>();
            data.add(k);
            data.addAll(values.subList(startIndex, endIndex + 1));

            channels[i].write(data);
        }

        StringBuilder finalResult = new StringBuilder();

        System.out.println("Starting timer...");
        long start = System.currentTimeMillis();

        for (int i = 0; i < workersNumber; i++) {
            System.out.println("Running worker #" + i + "...");
            boolean[] result = (boolean[]) channels[i].readObject();
            for (int j = 0; j < result.length; j++) {
                boolean isPrime = result[j];
                finalResult.append(values.get(i * chunkSize + j));
                finalResult.append(isPrime ? " is prime\n" : " is not prime\n");
            }
        }

        writeOutputData(outputFile, finalResult.toString());
        System.out.println("Finished.");

        long finish = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (double)(finish - start)/1000 + " seconds.");

        task.end();
    }

    private static void writeOutputData(String filename, String data) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(data);
            System.out.println("Output: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getPrecisionNumber(String filename) throws Exception {
        Scanner sc = new Scanner(new File(filename));
        int k = Integer.parseInt(sc.nextLine());
        sc.close();
        return k;
    }

    private static ArrayList<Integer> getInputData(String inputValue) throws Exception {
        int numbersTotal = Integer.parseInt(inputValue);
        ArrayList<Integer> values = new ArrayList<>();

        for (int i = 1; i <= numbersTotal; ++i) {
            values.add(i);
        }

        return values;
    }
}
