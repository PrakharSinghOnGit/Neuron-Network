import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
// import java.util.Arrays;


class Helper {
    void arrayToImage(double[] arr, int Size, String filename) {
        BufferedImage image = new BufferedImage(Size, Size, BufferedImage.TYPE_INT_RGB);
        int index = 0;
        for (int y = 0; y < Size; y++) {
            for (int x = 0; x < Size; x++) {
                int c = (int)(arr[index]*256);
                image.setRGB(x, y, (c << 16) | (c << 8) | c);
                index++;
            }
        }
        File output = new File(filename);
        try {
            ImageIO.write(image, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    double[][][] readData(String filename, int len) throws Exception {
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int count = 0;
        double[][][] Data = new double[len][2][784];
        while ((line = br.readLine()) != null && count < len) {
            String[] values = line.split(",");
            double[] pixels = new double[values.length];
            double[] output = new double[10];
            output[Integer.parseInt(values[0])] = 1;
            for (int i = 1; i < values.length; i++) {
                pixels[i] = Integer.parseInt(values[i]) / 255.0;
            }
            Data[count][0] = output;
            Data[count][1] = pixels;
            count++;
        }
        br.close();
        fr.close();
        return Data;
    }
}