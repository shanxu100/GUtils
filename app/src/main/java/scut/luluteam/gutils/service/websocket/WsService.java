package scut.luluteam.gutils.service.websocket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

//import scut.luluteam.gutils.network.websocket.WebSocketClient;


public class WsService extends Service {
    static final String URL = "ws://125.216.242.147:8080/bathProject/websocket";
    static final String port = "80";


    public WsService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        WebSocketClient.getInstance().start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
//        WebSocketClient.getInstance().stop();
        super.onDestroy();
    }
}
