import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class MemoryMappedFileExample {
    static long length = 536870912; //2Gb

    public static void main(String[] args) throws Exception {
        MappedByteBuffer out = new RandomAccessFile("howtodoinjava.dat", "rw")
                .getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length*4-1);
        for (int i = 0; i < length; i++) {
            byte[] bigInteger = bigIntGenerator().toByteArray();

            if (bigInteger[0] == 0) {
                byte[] tmp = new byte[bigInteger.length - 1];
                System.arraycopy(bigInteger, 1, tmp, 0, tmp.length);
                bigInteger = tmp;
            }

            for (int j = 0; j < bigInteger.length; j++) {
                out.put(bigInteger[j]);
            }
        }
        System.out.println("Finished writing");
    }

    private static BigInteger bigIntGenerator() {
        Random rnd = new Random();
        return new BigInteger(32, rnd);
    }
}




