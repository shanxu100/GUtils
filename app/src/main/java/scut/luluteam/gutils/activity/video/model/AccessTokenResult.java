package scut.luluteam.gutils.activity.video.model;

import com.google.gson.Gson;

/**
 * @author Guan
 * @date Created on 2018/6/3
 */
public class AccessTokenResult {

    public static final String SAVED_ACCESSTOKEN="savedaccesstoken";

    private String msg;
    private String code;
    private AccessToken data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AccessToken getData() {
        return data;
    }

    public void setData(AccessToken data) {
        this.data = data;
    }

    public static class AccessToken {
        private String accessToken;
        private long expireTime;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
