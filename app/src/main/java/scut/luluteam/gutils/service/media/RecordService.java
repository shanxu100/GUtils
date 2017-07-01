package scut.luluteam.gutils.service.media;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;

public class RecordService extends Service {
    public RecordService() {
        MediaRecorder mediaRecorder;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
