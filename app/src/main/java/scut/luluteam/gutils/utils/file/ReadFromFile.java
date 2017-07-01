package scut.luluteam.gutils.utils.file;

import android.os.Looper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guan on 4/13/17.
 */

public class ReadFromFile {


    private static String TAG = "ReadFromFile";

    /**
     * 对于windows下，\r\n这两个字符在一起时，表示一个换行。
     * 但如果这两个字符分开显示时，会换两次行。
     * 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
     */

    /**
     * 随机读取文件内容
     * <p>
     * 未完待续……
     *
     * @param filePath
     */
    public static void readFileByRandomAccess(String filePath) {

        checkIfRunningInMainThread();


        RandomAccessFile randomFile = null;
        try {
            printLog("随机读取一段文件内容：");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(filePath, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 读文件的起始位置
            int beginIndex = (fileLength > 4) ? 0 : 0;
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
            // 将一次读取的字节数赋给byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     *
     * @param filePath
     * @return
     */
    public static List<String> readFileByLines(String filePath) {

        checkIfRunningInMainThread();


        File file = new File(filePath);

        if (!file.exists()) {
            printLog("文件不存在，读取失败！");
            return null;
        }

        BufferedReader reader = null;

        List<String> lines = new ArrayList<>();

        int line = 0;

        try {
            printLog("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //printLog("line " + line + ": " + tempString);
                lines.add(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            printLog("数据读取完毕，行数：" + line);
            return lines;
        }
    }


    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     *
     * @param filePath
     */
    public static void readFileByBytes(String filePath, FileListener listener) {

        checkIfRunningInMainThread();

        File file = new File(filePath);
        if (!file.exists()) {
            printLog("文件不存在，读取失败！");
            return;
        }

        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            printLog("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] bytes = new byte[1024];
            int length = 0;
            fileInputStream = new FileInputStream(filePath);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            ReadFromFile.printAvailableBytes(bufferedInputStream);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((length = bufferedInputStream.read(bytes)) != -1) {

                listener.onReadByBytesCallback(bytes, false);
                //System.out.write(bytes, 0, len);//好方法，第一个参数是数组，第二个参数是开始位置，第三个参数是长度
            }
            listener.onReadByBytesCallback(bytes, true);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (IOException e) {
            }

        }
    }


    /**
     * 以字符的形式读取文件
     *
     * @param filePath
     */
    public static void readFileByChars(String filePath, FileListener listener) {

        checkIfRunningInMainThread();


        FileReader fileReader = null;
        char[] cbuf = new char[1024];
        //len表示每次读如的数据长度，如果等于-1，则表示文件读取完毕
        int length = 0;
        try {
            fileReader = new FileReader(filePath);
            //每次把读入的数据存放到cbuf中，
            while ((length = fileReader.read(cbuf)) != -1) {
                //如何对读取的数据进行处理？
                listener.onReadByCharsCallback(cbuf, false);
            }
            listener.onReadByCharsCallback(cbuf, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示输入流中还剩的字节数
     *
     * @param in
     */
    private static void printAvailableBytes(InputStream in) {
        try {
            printLog("当前字节输入流中的字节数为:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printLog(String message) {
        Log.d(TAG, message);
        System.out.println(message);
    }


    private static void checkIfRunningInMainThread() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            Log.e(TAG, "文件读取操作不应该在主线程中进行……");
            throw new IllegalStateException("文件读取操作不应该在主线程中进行……");
        }
    }

    //=================================================================
    public static class FileListener {

        public void onReadByCharsCallback(char[] chars, boolean isDone) {

        }

        public void onReadByBytesCallback(byte[] bytes, boolean isDone) {

        }
    }
}
