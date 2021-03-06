package scut.luluteam.gutils.activity.video.other;

/**
 * @author Guan
 * @date Created on 2018/5/28
 */
public class EZOPENUtil {

    private static final String TAG = "EZOPENUtil";




    /**
     * 获取EZOPEN协议的 直播地 址
     *
     * @param deviceSerial
     * @param channelNo
     * @return
     */
    public static String getLiveUrl(String deviceSerial, int channelNo) {

        StringBuilder sb = new StringBuilder(VideoConstant.BASE_VIDEO_URL);
        sb.append("/")
                .append(deviceSerial)
                .append("/")
                .append(channelNo)
                .append(".live");
        return sb.toString();
    }

    /**
     * 获取EZOPEN协议的 回放 地址
     *
     * @param deviceSerial
     * @param channelNo
     * @return
     */
    public static String getRecUrl(String deviceSerial, int channelNo) {

        StringBuilder sb = new StringBuilder(VideoConstant.BASE_VIDEO_URL);
        sb.append("/")
                .append(deviceSerial)
                .append("/")
                .append(channelNo)
                .append(".rec");
        return sb.toString();
    }


}
