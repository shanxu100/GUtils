package scut.luluteam.gutils.utils;

import java.io.IOException;

/**
 * Created by Guan on 2018/1/15.
 */

public class ChmodUtil {

    /**
     * 修改文件的权限
     * 举例：在cache文件夹中的文件只有rw-。可是安装apk文件需要rwx，因此需要修改文件的权限
     *
     * @param permission
     * @param fullPath
     */
    public static void chmod(String permission, String fullPath) {
        try {
            String cmd = "chmod " + permission + " " + fullPath;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
