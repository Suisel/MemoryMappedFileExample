import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class MemoryMappedFileExample {

    public static void main(String[] args) throws Exception {
        Random rnd = new Random();
        MappedByteBuffer out = new RandomAccessFile("javatext.txt", "rw")
                .getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 2147483647); //размер файла в байтах
        for (int i = 0; i < 536870911; i++) //сколько раз положить переменную int
        {
            int a = rnd.nextInt();
            out.putInt(a);
            //System.out.println(a);
        }

        System.out.println("Finished writing");

    }
}
