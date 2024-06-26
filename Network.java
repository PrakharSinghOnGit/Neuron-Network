import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Network {
    private List<Layer> layers;

    public Network(int[] sizes) {
        layers = new ArrayList<>();
        for (int i = 1; i < sizes.length; i++) {
            layers.add(new Layer(sizes[i], sizes[i - 1]));
        }
    }

    public double[] feedForward(double[] inputs) {
        double[] currentInput = inputs;
        for (Layer layer : layers) {
            currentInput = layer.activate(currentInput);
        }
        return currentInput;
    }
}
