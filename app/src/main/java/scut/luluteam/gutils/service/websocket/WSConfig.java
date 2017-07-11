package scut.luluteam.gutils.service.websocket;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by guan on 7/11/17.
 */

public class WSConfig {

    public static final String WSHOST = "";

    public static URI getWSHOST_URL() throws URISyntaxException {

        return new URI(WSHOST);

    }
}
