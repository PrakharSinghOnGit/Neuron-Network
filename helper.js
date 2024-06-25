import fs from "fs";
const { PNG } = require("pngjs");
import readline from "readline";

function arrayToImage(pixelArray, outputPath) {
  if (pixelArray.length !== 784) {
    throw new Error("The pixel array must have 784 elements.");
  }

  // Create a new PNG image with dimensions 28x28
  const png = new PNG({ width: 28, height: 28, colorType: 0 });

  // Populate the PNG data with pixel values
  for (let y = 0; y < 28; y++) {
    for (let x = 0; x < 28; x++) {
      const idx = y * 28 + x;
      const color = pixelArray[idx];

      // PNG data is in RGBA format, we set R, G, B to the color and A to 255
      const dataIdx = (y * 28 + x) * 4;
      png.data[dataIdx] = color; // Red
      png.data[dataIdx + 1] = color; // Green
      png.data[dataIdx + 2] = color; // Blue
      png.data[dataIdx + 3] = 255; // Alpha
    }
  }

  // Write the PNG file
  png
    .pack()
    .pipe(fs.createWriteStream(outputPath))
    .on("finish", () => {
      console.log(`Image saved to ${outputPath}`);
    })
    .on("error", (err) => {
      console.error("Error writing image:", err);
    });
}

async function readData(type, len) {
  return new Promise((resolve, reject) => {
    let lines = [];
    const fileStream = fs.createReadStream(type + ".txt");
    const rl = readline.createInterface({
      input: fileStream,
      crlfDelay: Infinity,
    });

    rl.on("line", (line) => {
      lines.push(line);
      if (lines.length >= len) {
        rl.close();
      }
    });

    rl.on("close", () => {
      let data = [];
      let arr = lines.map((line) =>
        line.split(",").map((x) => parseInt(x) / 255)
      );
      arr.forEach((x) => {
        let lbl = Array.from({ length: 10 }, () => 0);
        lbl[x.shift() * 255] = 1;
        data.push([x, lbl]);
      });
      resolve(data);
    });

    rl.on("error", (err) => {
      reject(err);
    });
  });
}

async function save(data, name) {
  Bun.write(name + ".json", JSON.stringify(data, null, 2), (err) => {
    if (err) {
      console.error(err);
    }
  });
}

export { arrayToImage, readData, save };
