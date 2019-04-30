package edu.scut.luluteam.serialportlibrary;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class SerialPort {

    static {
        System.loadLibrary("SerialPort");
    }

    private static final String TAG = SerialPort.class.getSimpleName();

    /**
     * 文件设置权限 666 可读 可写
     *
     * @param file 文件
     * @return 权限修改是否成功
     */
    boolean chmod666(File file) {
        if (null == file || !file.exists()) {
            // 文件不存在
            return false;
        }

        try {

            // 修改文件属性为 [可读 可写]
            String cmd = "chmod 666 " + file.getAbsolutePath() + "\n" + "exit\n";
            Process ps = Runtime.getRuntime().exec( cmd );

            if ( file.canRead() && file.canWrite() ) {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 打开串口
    protected native FileDescriptor open(String path, int baudRate, int flags);

    // 关闭串口
    protected native void close();
}
