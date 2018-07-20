package scut.luluteam.gutils.model;

import java.util.List;

public class UserResult {

    private boolean result;
    private List<UserInfo> userInfoList;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public static class UserInfo {

        private String username;
        private String nickname;
        private String password;
        /**
         * 优先级
         * 0：超级管理员
         * 1：一级管理员
         * 2：二级管理员
         */
        private int priority;
        /**
         * 表示这个用户是谁创建的
         */
        private String superior = "";


        private String name;
        private String phoneNum;
        private String email;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public String getSuperior() {
            return superior;
        }

        public void setSuperior(String superior) {
            this.superior = superior;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "username='" + username + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", password='" + password + '\'' +
                    ", priority=" + priority +
                    ", superior='" + superior + '\'' +
                    ", name='" + name + '\'' +
                    ", phoneNum='" + phoneNum + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserResult{" +
                "result=" + result +
                ", userInfoList=" + userInfoList.get(0).toString() +
                '}';
    }
}
