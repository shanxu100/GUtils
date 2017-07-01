package scut.luluteam.gutils.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.File;

/**
 * Created by guan on 4/11/17.
 */

public class Domine implements Parcelable {
    public String SaveName;//对于下载的文件，指定保存的文件名称，
    public String FileUrl;
    public String SavePath;//文件在本地的存放路径，上传和下载时
    //public String Filekey;//上传文件时，指定


    private String TAG = "BaseFile";

    /**
     * 知识点：Android系统中Parcelable和Serializable的区别
     * http://www.jianshu.com/p/a60b609ec7e7
     * http://lydia-fly.iteye.com/blog/2029269
     */

    public Domine(String saveName, String fileUrl, String savePath) {

        this.SaveName = saveName;
        this.FileUrl = fileUrl;
        this.SavePath = savePath;
    }

    public File getFileFromSavePath() {
        if (SavePath == null) {
            Log.e(TAG, "SavePath is null.请指定文件存储地址");
            return null;
        }

        File file = new File(SavePath);

        if (!file.isFile()) {
            Log.e(TAG, "请选择标准文件");
            return null;
        } else {
            return file;
        }
    }


    protected Domine(Parcel in) {
        SaveName = in.readString();
        FileUrl = in.readString();
        SavePath = in.readString();
    }

    public static final Creator<Domine> CREATOR = new Creator<Domine>() {
        @Override
        public Domine createFromParcel(Parcel in) {
            return new Domine(in);
        }

        @Override
        public Domine[] newArray(int size) {
            return new Domine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SaveName);
        dest.writeString(FileUrl);
        dest.writeString(SavePath);
    }
}
