// import java.util.Arrays;

public class index {
    public static void main(String[] args) throws Exception {
        int count = 2;
        Helper h = new Helper();
        double[][][] Data = h.readData("test.txt", count);
        h.arrayToImage(Data[0][1], 28, "a.png");
    }
}
