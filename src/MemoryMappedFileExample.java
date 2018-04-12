import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MemoryMappedFileExample {

    public static long GB = 1024 * 1024 * 1024;

    public static void main(String[] args) throws Exception {
        Random rnd = new Random();
//        MappedByteBuffer out = new RandomAccessFile("javatext.txt", "rw")
//                .getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 2 * GB); //размер файла в байтах
//        for (int i = 0; i < 2 * GB / 4; i++) //сколько раз положить переменную int
//        {
//            int a = rnd.nextInt();
//            out.putInt(a);
//        }

//        File file = new File("java2.txt");
//        FileWriter fw = new FileWriter(file);
////        for (int i = 0; i < GB / 4; i++) //сколько раз положить переменную int
////        {
////            int a = rnd.nextInt();
////            fw.write(a);
////        }
//        fw.write(29);
//        System.out.println(file.length());

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("java2.txt", true))) {
            for (int i = 0; i < 2 * GB / 4; i++) {
                int value = rnd.nextInt();
                dos.writeInt(value);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


//        File fout = new File("myOutFile.txt");
//        FileOutputStream fos = new FileOutputStream(fout);
//        OutputStreamWriter osw = new OutputStreamWriter(fos);
//
//        for (int i = 0; i < 2 * GB; i++) //сколько раз положить переменную int
//        {
//            int a = rnd.nextInt();
//            osw.write(a);
//        }
//        osw.close();

//        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream("textfile.txt"));
//        for (int i = 0; i < GB ; i++) //сколько раз положить переменную int
//        {
//            int a = rnd.nextInt();
//            stream.write(a);
//        }
//        stream.close();


        System.out.println("Finished writing");

    }
}
