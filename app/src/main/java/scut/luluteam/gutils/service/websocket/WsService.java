package scut.luluteam.gutils.service.websocket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import scut.luluteam.gutils.network.websocket.WebSocketClient;

public class WsService extends Service {
    private Thread websocketThread;

    public WsService() {
        websocketThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(" start websocket thread ");
                WebSocketClient.getInstance().start();
            }
        }, "WebSocketThread in service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        websocketThread.start();
        //WebSocketClient.getInstance().start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        WebSocketClient.getInstance().stop();
        super.onDestroy();
    }
}
