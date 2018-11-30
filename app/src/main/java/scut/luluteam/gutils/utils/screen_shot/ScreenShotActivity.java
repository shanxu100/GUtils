package scut.luluteam.gutils.utils.screen_shot;

import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import scut.luluteam.gutils.utils.ShowUtil;


/**
 * Created by guan on 5/31/17.
 */

public class ScreenShotActivity extends Activity {

    private static final int REQUEST_MEDIA_PROJECTION = 0x2893;
    private MediaProjectionManager mediaProjectionManager;
    private String TAG = "ScreenShotActivity";
    //private  ScreenShotListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestScreenShot();
    }

    public void requestScreenShot() {
        if (Build.VERSION.SDK_INT >= 21) {
            mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        } else {
            ShowUtil.Toast("版本过低,无法截屏");
            this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == RESULT_OK) {
                //ShowUtil.Toast("user Permitted");
                Log.e(TAG, "ScreenShot has been Permitted by user");
                ScreenShot.setInitData(resultCode, data);
                this.finish();
            } else if (resultCode == RESULT_CANCELED) {
                ShowUtil.Toast("ScreenShot has been Canceled");
                this.finish();
            }
        }

    }


}
