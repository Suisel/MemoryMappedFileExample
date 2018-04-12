import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.sun.deploy.util.BufferUtil.GB;

public class MemoryMappedFileRead {

    final static int numThreads = 4;
    private static String bigFile = "java2.txt";

    public static void main(String[] args) throws Exception {

        File file = new File(bigFile);
        final ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        List<Future<Data>> futures = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            long chunkSize = 2 * GB / numThreads;
            //вычисляем смещение и длину куска для каждого потока, передаем в конструктор
            final MyWorker worker = new MyWorker(file, i * chunkSize, chunkSize);

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
        BigInteger min = BigInteger.valueOf(Long.MAX_VALUE);
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
