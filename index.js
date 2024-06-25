import { readData, arrayToImage, save } from "./helper";

class Neuron {
  constructor(numInputs) {
    this.weights = Array.from(
      { length: numInputs },
      () => Math.random() * Math.sqrt(2 / numInputs)
    );
    this.bias = 0;
  }

  activate(input) {
    this.output = this.sigmoid(this.dot(input, this.weights) + this.bias);
    return this.output;
  }

  sigmoid(x) {
    return 1 / (1 + Math.exp(-x));
  }

  dot(a, b) {
    return a.reduce((sum, ai, i) => sum + ai * b[i], 0);
  }
}

class Layer {
  constructor(numNeurons, numInputs) {
    this.neurons = Array.from(
      { length: numNeurons },
      () => new Neuron(numInputs)
    );
  }

  activate(inputs) {
    return this.neurons.map((neuron) => neuron.activate(inputs));
  }
}

class Network {
  constructor(sizes) {
    this.layers = [];
    for (let i = 1; i < sizes.length; i++) {
      this.layers.push(new Layer(sizes[i], sizes[i - 1]));
    }
  }

  feedForward(inputs) {
    return this.layers.reduce((input, layer) => layer.activate(input), inputs);
  }
}

class Trainer {
  constructor(network, initialLearningRate = 0.1) {
    this.network = network;
    this.learningRate = initialLearningRate;
  }

  updateLearningRate(epoch) {
    this.learningRate = this.learningRate / (1 + 0.01 * epoch);
  }

  train(trainingData, epochs) {
    for (let epoch = 0; epoch < epochs; epoch++) {
      console.log(`Epoch ${epoch + 1}`);
      this.updateLearningRate(epoch);
      for (let [input, target] of trainingData) {
        this.backPropagate(input, target);
      }
    }
  }

  backPropagate(input, target) {
    // Feed forward
    let outputs = [input];
    for (let layer of this.network.layers) {
      outputs.push(layer.activate(outputs[outputs.length - 1]));
    }

    // Calculate error for output layer
    let errors = outputs[outputs.length - 1].map(
      (output, i) => target[i] - output
    );

    // BackPropagate errors and update weights
    for (let l = this.network.layers.length - 1; l >= 0; l--) {
      let layer = this.network.layers[l];
      let nextErrors = [];

      for (let n = 0; n < layer.neurons.length; n++) {
        let neuron = layer.neurons[n];
        let error = errors[n];
        let delta = error * neuron.output * (1 - neuron.output);

        for (let w = 0; w < neuron.weights.length; w++) {
          let input = outputs[l][w];
          neuron.weights[w] += this.learningRate * delta * input;
          nextErrors[w] = (nextErrors[w] || 0) + neuron.weights[w] * delta;
        }

        neuron.bias += this.learningRate * delta;
      }

      errors = nextErrors;
    }
  }
}

const network = new Network([784, 28, 28, 10]);
console.log("Network created");

const trainingData = await readData("train", 60000);
console.log("Data loaded");

const trainer = new Trainer(network);
trainer.train(trainingData, 1000);

await save(network, "network");

let test = await readData("test", 10);
let results = test.map(([input, target]) => {
  let output = network.feedForward(input);
  let guess = output.indexOf(Math.max(...output));
  return { guess, target: target[0] };
});
console.log(results);
