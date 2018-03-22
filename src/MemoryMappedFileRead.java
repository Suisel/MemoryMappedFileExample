import java.io.File;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MemoryMappedFileRead {
    private static String bigFile = "javatext.txt";

    public static void main(String[] args) throws Exception {
        //Create file object
        File file = new File(bigFile);

        //Get file channel in readonly mode
        FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel();

        //Get direct byte buffer access using channel.map() operation
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

        // the buffer now reads the file as if it were loaded in memory.
        System.out.println(buffer.capacity());  //Get the size based on content size of file
        System.out.println(buffer.limit());

        long a = 0;
        long b = 0;
        BigInteger sum = BigInteger.valueOf(0);
        //You can read the file from this buffer the way you like.
        for (int i = 0; i < 536870911; i++) {

            b = 0;
            for (int j = 0; j < 4; j++) {
                a = buffer.get() << (8 * j);
                b = a | b;
            }
            BigInteger temp = BigInteger.valueOf(b);
            sum = sum.add(temp);
        }
        System.out.println(sum); //Print the sum

    }
}
