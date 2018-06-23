package scut.luluteam.gutils.test.okhttp;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * BaseInterceptor
 * Created by LIUYONGKUI726 on 2016-06-30.
 * {@link # https://github.com/NeglectedByBoss/RetrofitClient}
 */
public class BaseInterceptor implements Interceptor{
    private Map<String, String> headers;
    public BaseInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request()
                .newBuilder();
        if (headers != null && headers.size() > 0) {
            Iterator<Map.Entry<String, String>> iterator=headers.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, String> entry=iterator.next();
                builder.addHeader(entry.getKey(),entry.getValue()).build();
            }
        }
        return chain.proceed(builder.build());

    }
}