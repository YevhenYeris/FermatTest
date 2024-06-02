import parcs.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class FermatTestApplication {

    public static void main(String[] args) throws Exception {

        String inputValueString = System.getenv("INPUT_VALUE");
        System.out.println("Read INPUT_VALUE: " + inputValueString + ".");

        String testPrecision = System.getenv("TEST_PRECISION");
        System.out.println("Read TEST_PRECISION: " + testPrecision + ".");

        String outputFile = System.getenv("OUTPUT_FILE");
        System.out.println("Read OUTPUT_FILE: " + outputFile + ".");

        String workersNumberString = System.getenv("WORKERS_NUMBER");
        System.out.println("Read WORKERS_NUMBER: " + workersNumberString + ".");

        task task = new task();
        task.addJarFile("FermatTest.jar");

        int k = Integer.parseInt(testPrecision);
        int inputValue = Integer.parseInt(inputValueString);

        int workersNumber = Integer.parseInt(workersNumberString);
        int chunkSize = inputValue / workersNumber;

        AMInfo amInfo = new AMInfo(task, null);

        point[] points = new point[workersNumber];
        channel[] channels = new channel[workersNumber];

        for (int i = 0; i < workersNumber; i++) {
            points[i] = amInfo.createPoint();
            channels[i] = points[i].createChannel();
            points[i].execute("FermatTest");

            int startIndex = i * chunkSize;
            int endIndex = (i == workersNumber - 1) ? inputValue - 1 : startIndex + chunkSize - 1;

            System.out.println("Worker #" + i + ": processing the range [" + (startIndex + 1) + ", " + (endIndex + 1) + "].");

            ArrayList<Integer> data = new ArrayList<>();
            data.add(k);
            data.add(startIndex + 1);
            data.add(endIndex + 1);

            System.out.println("Data: [" + data.get(0) + ", " + data.get(1) + ", " + data.get(2) + "].");

            channels[i].write(data);
        }

        StringBuilder finalResult = new StringBuilder();

        System.out.println("Starting timer...");
        long start = System.currentTimeMillis();

        for (int i = 0; i < workersNumber; i++) {
            System.out.println("Running worker #" + i + "...");
            boolean[] result = (boolean[]) channels[i].readObject();
            for (int j = 0; j < result.length; j++) {
                System.out.println("Result i: " + j);
                System.out.println("i * chunkSize + j: " + i * chunkSize + j);
                boolean isPrime = result[j];
                finalResult.append(i * chunkSize + j);
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
}
