import java.io.File;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MemoryMappedFileRead {
    private static String BigTextFile = "OutputFile.txt";

    public static void main(String[] args) throws Exception {
        //Create file object
        File TextFile = new File(BigTextFile);

        //Get file channel in readonly mode
        FileChannel TextFileChannel = new RandomAccessFile(TextFile, "rw").getChannel();

        //Get direct byte buffer access using channel.map() operation
        MappedByteBuffer TextBuffer = TextFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, TextFileChannel.size());

        // the buffer now reads the file as if it were loaded in memory.
        // System.out.println(TextBuffer.isLoaded());  //prints false
        //System.out.println(TextBuffer.capacity());  //Get the size based on content size of file

        //You can read the file from this buffer the way you like.
        BigInteger Result = BigInteger.valueOf(0);
        for (int i = 0; i < 536870912; i++) {
            byte[] byteArray = new byte[4];
            for (int j = 0; j < 4; j++){
                byteArray[j] = TextBuffer.get();
            }
            BigInteger bigInteger = new BigInteger(byteArray);
            //BigInteger num = BigInteger.valueOf(TextBuffer.getInt());
            System.out.println(bigInteger);
            Result = Result.add(bigInteger);
        }
        System.out.println(Result);
    }
}
