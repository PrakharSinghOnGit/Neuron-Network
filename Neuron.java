import java.util.Random;

class Neuron {
    double[] weights;
    double bias;
    double output;

    public Neuron(int numInputs) {
        Random rand = new Random();
        weights = new double[numInputs];
        for (int i = 0; i < numInputs; i++) {
            weights[i] = rand.nextGaussian();
        }
        bias = rand.nextGaussian();
    }

    public double activate(double[] inputs) {
        double sum = bias;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * inputs[i];
        }
        output = sigmoid(sum);
        return output;
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public double sigmoidDerivative() {
        return output * (1 - output);
    }
}