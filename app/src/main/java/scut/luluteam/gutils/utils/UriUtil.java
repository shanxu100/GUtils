package scut.luluteam.gutils.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by guan on 5/13/17.
 */

public class UriUtil {

    /**
     * 禁止实例化本工具类，因为实例化本工具类没有任何意义
     */
    private UriUtil() {
        throw new AssertionError();
    }


    /**
     * 根据文件据对路径，获取Uri
     *
     * @param context
     * @param path
     * @return
     */
    public static Uri getUriFromFilePath(Context context, String path) {

        Log.d("TAG", "filePath is " + path);
        if (path != null) {
            path = Uri.decode(path);
            Log.d("TAG", "path2 is " + path);
            ContentResolver cr = context.getContentResolver();
            StringBuffer buff = new StringBuffer();
            buff.append("(")
                    .append(MediaStore.Images.ImageColumns.DATA)
                    .append("=")
                    .append("'" + path + "'")
                    .append(")");
            Cursor cur = cr.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.ImageColumns._ID},
                    buff.toString(), null, null);
            int index = 0;
            for (cur.moveToFirst(); !cur.isAfterLast(); cur
                    .moveToNext()) {
                index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                // set _id value
                index = cur.getInt(index);
            }
            if (index == 0) {
                //do nothing
            } else {
                Uri uri_temp = Uri
                        .parse("content://media/external/images/media/"
                                + index);
                Log.d("TAG", "uri_temp is " + uri_temp);
                if (uri_temp != null) {
                    return uri_temp;
                }
            }

        }
        return null;
    }


    public static Uri fromFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {

            return Uri.fromFile(file);
        } else {

            return null;
        }
    }


    /**
     * 根据Uri获取绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     */
    @TargetApi(19)
    public static String getFilePathFromUri(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


}
