package scut.luluteam.gutils.utils.file;

import android.os.Looper;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by guan on 4/13/17.
 */

public class WriteToFile {

    private static String TAG = "WriteToFile";

    /**
     * 说明：
     * File.separator 文件路径的分隔符：因为不同环境下的分隔符不同，所以使用File.separator比“\”更加健壮
     * file.mkdirs 创建此抽象路径名指定的目录，包括所有必需但不存在的父目录
     * file.mkdir 创建此抽象路径名指定的目录
     */


    /**
     * 二进制文件的读写
     *
     * @param filePath 带路经的全文件名，即文件路径
     * @param bytes
     * @param isAppend
     */
    public static void writeToFileByBytes(String filePath, byte[] bytes, boolean isAppend) {
        checkIfRunningInMainThread();

        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePath, isAppend);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedOutputStream.write(bytes);

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            try {
                bufferedOutputStream.flush();
                fileOutputStream.close();
                bufferedOutputStream.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

    /**
     * 普通文件的写入,使用FileWriter，字符输出流
     *
     * @param filePath 带路经的全文件名，即文件路径
     * @param s
     * @param isAppend
     */
    public static void writeToFile(String filePath, String s, boolean isAppend) {

        checkIfRunningInMainThread();

        File file = new File(filePath);

        if (!file.isFile()) {
            System.out.println("目标文件不是normal文件，可能是文件夹。请检查后重试");
            return;
        }


        FileWriter fileWriter = null;
        try {

            fileWriter = new FileWriter(filePath, isAppend);
            fileWriter.write(s);

        } catch (Exception e) {

            System.out.println("文件写入失败：" + filePath);
            e.printStackTrace();

        } finally {

            try {
                if (fileWriter == null) {
                    return;
                }
                fileWriter.flush();
                fileWriter.close();
                System.out.println("文件写入完毕:" + filePath);

            } catch (IOException e) {

                System.out.println("FileWriter没有正常关闭:" + filePath);
                e.printStackTrace();

            }
        }

    }

    /**
     * 普通文件的写入,使用FileWriter，字符输出流
     *
     * @param filePath 带路经的全文件名，即文件路径
     * @param chars
     * @param isAppend
     */
    public static void writeToFileByChars(String filePath, char[] chars, boolean isAppend) {

        checkIfRunningInMainThread();

        File file = new File(filePath);

        if (!file.isFile()) {
            System.out.println("目标文件不是normal文件，可能是文件夹。请检查后重试");
            return;
        }


        FileWriter fileWriter = null;
        try {

            fileWriter = new FileWriter(filePath, isAppend);
            fileWriter.write(chars);

        } catch (Exception e) {

            System.out.println("文件写入失败：" + filePath);
            e.printStackTrace();

        } finally {

            try {
                if (fileWriter == null) {
                    return;
                }
                fileWriter.flush();
                fileWriter.close();
                System.out.println("文件写入完毕:" + filePath);

            } catch (IOException e) {

                System.out.println("FileWriter没有正常关闭:" + filePath);
                e.printStackTrace();

            }
        }


    }


    private static void checkIfRunningInMainThread() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            Log.e(TAG, "文件写入操作不应该在主线程中进行……");
            throw new IllegalStateException("文件写入操作不应该在主线程中进行……");
        }
    }
}
