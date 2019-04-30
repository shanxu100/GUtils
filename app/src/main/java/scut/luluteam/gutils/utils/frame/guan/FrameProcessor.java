package scut.luluteam.gutils.utils.frame.guan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程处理业务逻辑
 *
 * @author Guan
 */
public class FrameProcessor {
    private static final String TAG = "FrameProcessor";

    private static ExecutorService executorService = Executors.newFixedThreadPool(2);


    public static void process(final CustomFrame customFrame) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (customFrame == null) {
                    return;
                }
                if (!checkCRC(customFrame)) {
                    reportError(customFrame.toBytes(), "CRC16 Check Error");
                    return;
                }
                //处理数据部分
                onMessage(customFrame);
            }
        });


    }

    /**
     * 对封装好的数据对象做具体的业务处理
     *
     * @param customFrame
     */
    private static void onMessage(CustomFrame customFrame) {

//        Log.i(TAG, "识别一个帧：" + ByteUtil.byte2hex(customFrame.toBytes()));


    }

    /**
     * 校验CRC
     *
     * @param customFrame
     * @return
     */
    private static boolean checkCRC(CustomFrame customFrame) {
//        byte[] length_data = new byte[CustomFrame.LENGTH_SIZE + customFrame.getIntLength()];
//        System.arraycopy(customFrame.getLength(), 0, length_data, 0, CustomFrame.LENGTH_SIZE);
//        System.arraycopy(customFrame.getData(), 0, length_data, CustomFrame.LENGTH_SIZE, customFrame.getIntLength());
//        return CheckCRC.check(length_data, customFrame.getCrc16());
        return true;
    }

    /**
     * 报告错误，并将 帧 打印出来
     *
     * @param data
     * @param errInfo
     */
    protected static void reportError(byte[] data, String errInfo) {
//        String frame_str = ByteUtil.byte2hex(data);
//        ToastManager.newInstance("CustomFrame Error... " + errInfo + " Frame: " + frame_str)
//                .isLog(TAG).show();
    }
}
