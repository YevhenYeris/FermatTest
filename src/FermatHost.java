import parcs.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class FermatHost {

    public static void main(String[] args) throws Exception {

        String inputFile = System.getenv("INPUT_FILE");
        String outputFile = System.getenv("OUTPUT_FILE");
        String workersNumber = System.getenv("WORKERS_NUMBER");

        task curTask = new task();
        curTask.addJarFile("FermatTask.jar");

        String fileName = curTask.findFile(inputFile);
        int k = readKFromInput(fileName);
        ArrayList<Integer> values = readInputData(fileName);

        int numWorkers = Integer.parseInt(workersNumber);
        int chunkSize = values.size() / numWorkers;

        AMInfo info = new AMInfo(curTask, null);

        point[] points = new point[numWorkers];
        channel[] channels = new channel[numWorkers];

        for (int currentWorkerIndex = 0; currentWorkerIndex < numWorkers; currentWorkerIndex++) {
            points[currentWorkerIndex] = info.createPoint();
            channels[currentWorkerIndex] = points[currentWorkerIndex].createChannel();
            points[currentWorkerIndex].execute("FermatTask");

            int startIndex = currentWorkerIndex * chunkSize;
            int endIndex = (currentWorkerIndex == numWorkers - 1) ? values.size() - 1 : startIndex + chunkSize - 1;
            System.out.println("Worker " + currentWorkerIndex + " will process values from " + (startIndex + 1) + " to " + (endIndex + 1) + " inclusive.");
            ArrayList<Integer> data = encodeDataToTask(k, values, startIndex, endIndex);

            channels[currentWorkerIndex].write(data);
        }

        StringBuilder finalResult = new StringBuilder();

        for (int i = 0; i < numWorkers; i++) {
            System.out.println("Waiting for result from worker " + i + "...");
            boolean[] result = (boolean[]) channels[i].readObject();
            for (int j = 0; j < result.length; j++) {
                boolean isPrime = result[j];
                finalResult.append(values.get(i * chunkSize + j));
                finalResult.append(isPrime ? " is prime" : " is not prime");
            }
        }

        writeOutputToFile(outputFile, finalResult.toString());
        System.out.println("Task completed");
        curTask.end();
    }

    private static void writeOutputToFile(String filename, String data) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(data);
            System.out.println("Data written to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Integer> encodeDataToTask(
            int k,
            ArrayList<Integer> values,
            int startIndex,
            int endIndex
    ) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(k);
        result.addAll(values.subList(startIndex, endIndex + 1));
        return result;
    }

    private static int readKFromInput(String filename) throws Exception {
        Scanner sc = new Scanner(new File(filename));
        int k = Integer.parseInt(sc.nextLine());
        sc.close();
        return k;
    }

    private static ArrayList<Integer> readInputData(String filename) throws Exception {
        Scanner sc = new Scanner(new File(filename));
        sc.nextLine();
        ArrayList<Integer> values = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.isEmpty()) {
                break;
            }
            values.add(Integer.parseInt(line));
        }
        sc.close();
        return values;
    }
}
