
import java.util.Random;

public class Neuron {
    private double[] weights;
    private double bias;
    private double output;

    public Neuron(int numInputs) {
        // Initialize weights using a random value based on a formula similar to Xavier initialization
        weights = new double[numInputs];
        Random random = new Random();
        double limit = Math.sqrt(2.0 / numInputs);
        for (int i = 0; i < numInputs; i++) {
            weights[i] = random.nextDouble() * limit;
        }
        bias = 0.0;
    }

    public double activate(double[] input) {
        output = sigmoid(dot(input, weights) + bias);
        return output;
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private double dot(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }
}

