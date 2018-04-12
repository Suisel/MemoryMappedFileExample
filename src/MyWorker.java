import java.io.File;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
        try {
            FileChannel fileChannel = new RandomAccessFile(this.fileName, "r").getChannel();
            ByteBuffer buffer = fileChannel
                    .map(FileChannel.MapMode.READ_ONLY, this.pos, this.size)
                    .order(ByteOrder.BIG_ENDIAN);

            // считаем сумму
            long a = 0;
            long b = 0;
            long c = 255;
            BigInteger sum = BigInteger.valueOf(0);
            BigInteger min = BigInteger.valueOf(Long.MAX_VALUE);
            BigInteger max = BigInteger.valueOf(-1);
            //You can read the file from this buffer the way you like.


            for (int i = 0; i < size / 4 - 1; i++) {
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
            fileChannel.close();
            //, min, max

            //System.out.println(sum);
            return new Data(max, min, sum);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
