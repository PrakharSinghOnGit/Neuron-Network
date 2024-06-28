import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        double[][] trainingInputs = loadInputs("train.txt");
        double[][] trainingOutputs = loadOutputs("train.txt");

        int[] layerSizes = {784, 128, 64, 10};  // Example layer sizes
        // int[] layerSizes = {2,2,22, 1};  // Example layer sizes
        Network nn = new Network(layerSizes);

        int epochs = 100;
        double learningRate = 0.01;

        nn.train(trainingInputs, trainingOutputs, epochs, learningRate);

        // Test the network after training
        double[][] testInput = loadInputs("test.txt");
        double[][] testOutput = loadOutputs("test.txt");
        int correct = 0;
        for (int i = 0; i < 10000; i++) {
            double[] result = nn.feedForward(testInput[i]);
            int maxIndex = 0;
            double maxValue = result[0];
            for (int j = 1; j < result.length; j++) {
                if (result[j] > maxValue) {
                    maxValue = result[j];
                    maxIndex = j;
                }
            }
            int correctIndex = 0;
            for (int j = 0; j < testOutput[i].length; j++) {
                if (testOutput[i][j] == 1) {
                    correctIndex = j;
                    break;
                }
            }
            if (maxIndex == correctIndex) {
                correct++;
            }
        }
        System.out.println("Error: " + (correct/10000.0)*100);
    }

    private static double[][] loadInputs(String fileName) throws Exception {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int count = 0;
        double[][] Data = new double[60000][784];
        while ((line = br.readLine()) != null && count < 60000) {
            String[] values = line.split(",");
            double[] pixels = new double[values.length];
            for (int i = 1; i < values.length; i++) {
                pixels[i] = Integer.parseInt(values[i]) / 255.0;
            }
            Data[count] = pixels;
            count++;
        }
        br.close();
        fr.close();
        return Data;
    }

    private static double[][] loadOutputs(String fileName) throws Exception {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        int count = 0;
        double[][] Data = new double[60000][784];
        while (br.readLine() != null && count < 60000) {
            double[] output = new double[10];
            output[Integer.parseInt(br.readLine().split(",")[0])] = 1;
            Data[count] = output;
            count++;
        }
        br.close();
        fr.close();
        return Data;
    }
}