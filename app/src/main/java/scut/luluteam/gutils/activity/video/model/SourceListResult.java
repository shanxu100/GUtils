package scut.luluteam.gutils.activity.video.model;

import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/5/28
 */
public class SourceListResult {

    private String code;
    private String msg;
    private List<Item> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

    public static class Item {
        private String deviceSerial;
        private int channelNo;
        private String hls;
        private String hlsHd;
        private String rtmp;
        private String rtmpHd;
        private int status;
        private int exception;
        private String ret;
        private String desc;

        public String getDeviceSerial() {
            return deviceSerial;
        }

        public void setDeviceSerial(String deviceSerial) {
            this.deviceSerial = deviceSerial;
        }

        public int getChannelNo() {
            return channelNo;
        }

        public void setChannelNo(int channelNo) {
            this.channelNo = channelNo;
        }

        public String getHls() {
            return hls;
        }

        public void setHls(String hls) {
            this.hls = hls;
        }

        public String getHlsHd() {
            return hlsHd;
        }

        public void setHlsHd(String hlsHd) {
            this.hlsHd = hlsHd;
        }

        public String getRtmp() {
            return rtmp;
        }

        public void setRtmp(String rtmp) {
            this.rtmp = rtmp;
        }

        public String getRtmpHd() {
            return rtmpHd;
        }

        public void setRtmpHd(String rtmpHd) {
            this.rtmpHd = rtmpHd;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getException() {
            return exception;
        }

        public void setException(int exception) {
            this.exception = exception;
        }

        public String getRet() {
            return ret;
        }

        public void setRet(String ret) {
            this.ret = ret;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
