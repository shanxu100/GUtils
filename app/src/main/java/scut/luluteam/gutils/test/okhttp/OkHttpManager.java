package scut.luluteam.gutils.test.okhttp;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

public class OkHttpManager {

    private static final String TAG = "OkHttpManager";
    private static final int DEFAULT_TIMEOUT = 30;

    private Cache cache = null;
    private BaseInterceptor baseInterceptor = null;
    private CaheInterceptor caheInterceptor = null;


    private static OkHttpClient mOkHttpClient;


    private OkHttpManager(Context context) {

    }

    /**
     * 获取OkHttpClient的单例
     *
     * @return
     */
    public static OkHttpClient getClientInstance() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpManager.class) {
                if (mOkHttpClient == null) {
                    initOkHttpClient();
                }
            }
        }
        return mOkHttpClient;
    }

    /**
     * 初始化OkHttpClient
     */
    private static void initOkHttpClient() {
        //        File httpCacheDirectory = new File(context.getCacheDir(), "OkHttp_cache");
//        cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        mOkHttpClient = new OkHttpClient.Builder()
//                .addNetworkInterceptor(
//                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//                .cookieJar(new NovateCookieManger(context))
//                .cache(cache)
//                .addInterceptor(new BaseInterceptor(null))
//                .addInterceptor(new CaheInterceptor(context))
//                .addNetworkInterceptor(new CaheInterceptor(context))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(sslParams.unSafeHostnameVerifier)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // TODO 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .connectionPool(new ConnectionPool(8, 10, TimeUnit.SECONDS))
                .build();
    }




}
