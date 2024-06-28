import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Network {
    Layer[] layers;

    public Network(int[] layerSizes) {
        layers = new Layer[layerSizes.length - 1];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new Layer(layerSizes[i + 1], layerSizes[i]);
        }
    }

    public double[] feedForward(double[] inputs) {
        double[] outputs = inputs;
        for (Layer layer : layers) {
            outputs = layer.feedForward(outputs);
        }
        return outputs;
    }

    public void train(double[][] trainingInputs, double[][] trainingOutputs, int epochs, double learningRate) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int epoch = 0; epoch < epochs; epoch++) {
            Future<?>[] futures = new Future[trainingInputs.length];

            for (int i = 0; i < trainingInputs.length; i++) {
                final int index = i;
                futures[i] = executor.submit(() -> {
                    double[] outputs = feedForward(trainingInputs[index]);
                    backpropagate(trainingInputs[index], trainingOutputs[index], outputs, learningRate);
                });
            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Epoch " + epoch + " complete");
        }

        executor.shutdown();
    }

    private void backpropagate(double[] inputs, double[] expectedOutputs, double[] outputs, double learningRate) {
        double[][] deltas = new double[layers.length][];
        for (int i = layers.length - 1; i >= 0; i--) {
            Layer layer = layers[i];
            deltas[i] = new double[layer.neurons.length];

            for (int j = 0; j < layer.neurons.length; j++) {
                Neuron neuron = layer.neurons[j];
                if (i == layers.length - 1) {
                    deltas[i][j] = (outputs[j] - expectedOutputs[j]) * neuron.sigmoidDerivative();
                } else {
                    double sum = 0.0;
                    Layer nextLayer = layers[i + 1];
                    for (int k = 0; k < nextLayer.neurons.length; k++) {
                        sum += nextLayer.neurons[k].weights[j] * deltas[i + 1][k];
                    }
                    deltas[i][j] = sum * neuron.sigmoidDerivative();
                }
            }
        }

        for (int i = layers.length - 1; i >= 0; i--) {
            Layer layer = layers[i];
            double[] layerInputs = (i == 0) ? inputs : layers[i - 1].feedForward(inputs);

            for (int j = 0; j < layer.neurons.length; j++) {
                Neuron neuron = layer.neurons[j];
                for (int k = 0; k < neuron.weights.length; k++) {
                    neuron.weights[k] -= learningRate * deltas[i][j] * layerInputs[k];
                }
                neuron.bias -= learningRate * deltas[i][j];
            }
        }
    }
}