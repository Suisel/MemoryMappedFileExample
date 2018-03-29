import java.io.File;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.Callable;

public class MyWorker implements Callable<Data> {

    public File fileName;
    public long pos;
    public long size;

    public MyWorker(File fileName, long pos, long size) {
        this.fileName = fileName;
        this.pos = pos;
        this.size = size;
    }

    @Override
    public Data call() throws Exception {
        FileChannel fileChannel = new RandomAccessFile(this.fileName, "r").getChannel();
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, this.pos, this.size);

        // считаем сумму
        long a = 0;
        long b = 0;
        long c = 255;
        BigInteger sum = BigInteger.valueOf(0);
        BigInteger min = new BigInteger("4294967296");
        BigInteger max = BigInteger.valueOf(-1);
        //You can read the file from this buffer the way you like.
        for (int i = 0; i < size; i++) {

            b = 0;
            c = 255;
            for (int j = 0; j < 4; j++) {
                a = buffer.get() << (8 * j);
                b = b | (a & c);
                c = c << (8 * j);
            }
            BigInteger temp = BigInteger.valueOf(b);
            if (temp.compareTo(max) == 1)
                max = temp;
            if (temp.compareTo(min) == -1)
                min = temp;
            sum = sum.add(temp);
        }
        //, min, max


        return new Data(max, min, sum);
    }
}
