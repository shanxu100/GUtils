package scut.luluteam.gutils.activity.video.model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Guan
 * @date Created on 2018/5/28
 */
public class CameraListResult {

    private String code;
    private String msg;
    private Page page;
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

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

    /**
     * 只获取 ChannelName字段，组成一个List
     *
     * @return
     */
    public List<String> getChannelNameList() {
        List<String> channelNames = new LinkedList<>();
        if (data == null || data.size() == 0) {
            return channelNames;
        }
        for (Item item : data) {
            channelNames.add(item.getChannelName());
        }
        return channelNames;
    }

    /**
     * @return
     */
    public List<String> getAllInfoList() {
        List<String> list = new LinkedList<>();
        if (data == null || data.size() == 0) {
            return list;
        }
        StringBuilder sb = new StringBuilder();
        for (Item item : data) {
            sb.append("\n名称：  ").append(item.getChannelName()).append("\n\n");
            sb.append(item.getDeviceSerial()).append("       ");
            switch (item.getStatus()) {
                case 0:
                    sb.append("不在线");
                    break;
                case 1:
                    sb.append("在线");
                    break;
                default:
                    sb.append("未知");
            }
            sb.append("        ");
            switch (item.getVideoLevel()) {
                case 0:
                    sb.append("流畅");
                    break;
                case 1:
                    sb.append("均衡");
                    break;
                case 2:
                    sb.append("高清");
                    break;
                case 3:
                    sb.append("超清");
                    break;
                default:
            }
            sb.append("\n");
            list.add(sb.toString());
            sb.delete(0, sb.length());
        }
        return list;
    }

    public static class Page {
        private String total;
        private String page;
        private String size;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }

    public static class Item {
        private String deviceSerial;
        private int channelNo;
        private String channelName;
        private int status;
        private String isShared;
        private String picUrl;
        private int isEncrypt;
        private int videoLevel;

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

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getIsShared() {
            return isShared;
        }

        public void setIsShared(String isShared) {
            this.isShared = isShared;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public int getIsEncrypt() {
            return isEncrypt;
        }

        public void setIsEncrypt(int isEncrypt) {
            this.isEncrypt = isEncrypt;
        }

        public int getVideoLevel() {
            return videoLevel;
        }

        public void setVideoLevel(int videoLevel) {
            this.videoLevel = videoLevel;
        }
    }
}
