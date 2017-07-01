package scut.luluteam.gutils.utils.file;

/**
 * Created by guan on 5/18/17.
 */

public class FileUtil {

    /**
     * 禁止实例化本工具类，因为实例化本工具类没有任何意义
     */
    private FileUtil() {
        throw new AssertionError();
    }

    public static String getFileNameFromPath(String path) {
        System.out.println("getFileNameFromPath===" + path.substring(path.lastIndexOf("/") + 1));
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
