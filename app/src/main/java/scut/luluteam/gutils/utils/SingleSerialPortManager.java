package scut.luluteam.gutils.utils;

import android.util.Log;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import edu.scut.luluteam.serialportlibrary.Device;
import edu.scut.luluteam.serialportlibrary.SerialPortFinder;
import edu.scut.luluteam.serialportlibrary.SerialPortManager;
import edu.scut.luluteam.serialportlibrary.listener.OnOpenSerialPortListener;
import edu.scut.luluteam.serialportlibrary.listener.OnSerialPortDataListener;
import scut.luluteam.gutils.app.App;
import scut.luluteam.gutils.utils.frame.guan.FrameManager;

/**
 * @author Guan
 * @date Created on 2019/3/22
 */
public class SingleSerialPortManager implements OnOpenSerialPortListener, OnSerialPortDataListener {

    private SerialPortManager mSerialPortManager;
    private SerialPortFinder serialPortFinder;

    private static final String TAG = "SingleSerialPortManager";
    private static final String KEY_SAVE_SERIAL_PORT_PATH = "key_save_serial_port_path";
    private static final String DEFAULT_SERIAL_PORT_PATH = "/dev/ttyS1";

    private static SingleSerialPortManager sspManager = null;


    /**
     * 单例
     *
     * @return
     */
    public static SingleSerialPortManager getInstance() {

        if (sspManager == null) {
            synchronized (SingleSerialPortManager.class) {
                if (sspManager == null) {
                    sspManager = new SingleSerialPortManager();
                }
            }
        }
        return sspManager;

    }


    private SingleSerialPortManager() {
        mSerialPortManager = new SerialPortManager();
        serialPortFinder = new SerialPortFinder();
    }

    /**
     * 参数为null的时候，打开默认端口
     *
     * @param devicePath
     */
    public void openSerialPort(String devicePath) {

        devicePath = StringUtils.isEmpty(devicePath) ? getSavedSerialPortPath() : devicePath;

        Log.i(TAG, "onCreate: device = " + devicePath);
        //先关闭
        mSerialPortManager.closeSerialPort();
        //再打开新的
        boolean openSerialPort = mSerialPortManager.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(this)
                .openSerialPort(new File(devicePath), 115200);
        if (openSerialPort) {
            saveOpenedSerialPort(devicePath);
        }

    }

    public void closeSerialPort() {
        mSerialPortManager.closeSerialPort();
    }

    public List<Device> getDevices() {
        return serialPortFinder.getDevices();
    }

    public void sendBytes(byte[] bytes) {
        mSerialPortManager.sendBytes(bytes);
    }

    public String getCurrentSerialPort() {
        return getSavedSerialPortPath();
    }

    //=========================================================

    private void saveOpenedSerialPort(String devicePath) {
        //TODO 保存已选择的串口数据
        SharedPreferencesUtil.putString(App.getAppContext(), KEY_SAVE_SERIAL_PORT_PATH, devicePath);

    }

    private String getSavedSerialPortPath() {
        String path = SharedPreferencesUtil.getString(App.getAppContext(), KEY_SAVE_SERIAL_PORT_PATH);
        if (StringUtils.isEmpty(path)) {
            return DEFAULT_SERIAL_PORT_PATH;
        }
        return path;
    }
    //=====================================================================================

    /**
     * 串口打开成功
     *
     * @param device 串口
     */
    @Override
    public void onSuccess(File device) {
        ToastManager.newInstance(String.format("串口 [%s] 打开成功", device.getPath()))
                .isLog(TAG).show();

    }

    /**
     * 串口打开失败
     *
     * @param device 串口
     * @param status status
     */
    @Override
    public void onFail(File device, Status status) {
        switch (status) {
            case NO_READ_WRITE_PERMISSION:
                ToastManager.newInstance(String.format("串口 [%s] 打开失败：没有读写权限", device.getPath()))
                        .isLog(TAG).show();
                break;
            case OPEN_FAIL:
                ToastManager.newInstance(String.format("串口 [%s] 打开失败：程序错误", device.getPath()))
                        .isLog(TAG).show();
            default:
                ToastManager.newInstance(String.format("串口 [%s] 打开失败：未知原因", device.getPath()))
                        .isLog(TAG).show();
                break;
        }
    }

    /**
     * 这个方法不断地被调用，表示成功接到数据。
     * 每次调用都会有一个byte[] 数组过来
     * 注意：这里的是子线程，不是主线程
     *
     * @param bytes 接收到的数据
     */
    @Override
    public void onDataReceived(byte[] bytes) {
        Log.i(TAG, "onDataReceived [ String ]: " + ByteUtil.byte2hex(bytes));
//        final byte[] finalBytes = bytes;
        FrameManager.put(bytes);
    }

    /**
     * @param bytes 发送的数据
     */
    @Override
    public void onDataSent(byte[] bytes) {
        Log.i(TAG, "onDataSent [ byte[] ]: " + Arrays.toString(bytes));
        Log.i(TAG, "onDataSent [ String ]: " + new String(bytes));
        final byte[] finalBytes = bytes;
        Log.e(TAG, String.format("发送\n%s", new String(finalBytes)));
    }
}
