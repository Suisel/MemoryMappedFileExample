import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class MemoryMappedFileRead {
    final static int size = 536870911;
    final static int numThreads = 4;
    private static String bigFile = "javatext.txt";

    public static void main(String[] args) throws Exception {
//        //Create file object
//        File file = new File(bigFile);
//
//        //Get file channel in readonly mode
//        FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel();
//
//        //Get direct byte buffer access using channel.map() operation
//        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
//
//        // the buffer now reads the file as if it were loaded in memory.
//        System.out.println(buffer.capacity());  //Get the size based on content size of file
//        System.out.println(buffer.limit());
//
//        long a = 0;
//        long b = 0;
//        long c = 255;
//        BigInteger sum = BigInteger.valueOf(0);
//        //You can read the file from this buffer the way you like.
//        for (int i = 0; i < 536870911; i++) {
//
//            b = 0;
//            c = 255;
//            for (int j = 0; j < 4; j++) {
//                a = buffer.get() << (8 * j);
//                b = b | (a & c);
//                c = c << (8 * j);
//            }
//            BigInteger temp = BigInteger.valueOf(b);
//            sum = sum.add(temp);
//
//        }
//        System.out.println(sum); //Print the sum

        File file = new File(bigFile);
        final ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        List<Future<Data>> futures = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            //вычисляем смещение и длину куска для каждого потока, передаем в конструктор
            final MyWorker worker = new MyWorker(file, size / 4, i * size / 4);

            // запускаем задачи на выполнение, сохраняем Future
            final Future<Data> future = executor.submit(worker);
            futures.add(future);
        }


        final List<Data> results = futures.stream().map(dataFuture -> {
            try {
                return dataFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getStackTrace());
                return null;
            }
        }).collect(Collectors.toList());

        BigInteger sum = BigInteger.valueOf(0);
        BigInteger min = new BigInteger("4294967296");
        BigInteger max = BigInteger.valueOf(-1);
        for (Data result : results) {
            sum.add(result.sum);
            if (min.compareTo(result.min) == 1)
                min = result.min;
            if (max.compareTo(result.max) == -1)
                max = result.max;
        }

        System.out.println("Sum = " + sum);
        System.out.println("Min = " + min);
        System.out.println("Max = " + max);
    }
}
