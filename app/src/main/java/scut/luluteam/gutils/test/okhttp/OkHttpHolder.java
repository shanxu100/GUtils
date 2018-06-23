package scut.luluteam.gutils.test.okhttp;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import scut.luluteam.gutils.test.android.CaheInterceptor;
import scut.luluteam.gutils.test.android.NovateCookieManger;

public class OkHttpHolder {

    private static final String TAG = "OkHttpHolder";
    private static final int DEFAULT_TIMEOUT = 30;
    private static OkHttpClient mOkHttpClient;
    private static OkHttpClient mOkHttpClientForAndroid;


    private OkHttpHolder(Context context) {

    }

    /**
     * 获取OkHttpClient的单例
     *
     * @return
     */
    public static OkHttpClient getClientInstance() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpHolder.class) {
                if (mOkHttpClient == null) {
                    initOkHttpClient();
                }
            }
        }
        return mOkHttpClient;
    }

    /**
     * 仅用于Android。如果用于java项目，把此方法删除即可
     *
     * @param context
     * @return
     */
    public static OkHttpClient getClientInstanceForAndroid(Context context) {
        if (mOkHttpClientForAndroid == null) {
            synchronized (OkHttpHolder.class) {
                if (mOkHttpClientForAndroid == null) {
                    HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
                    File httpCacheDirectory = new File(context.getCacheDir(), "OkHttp_cache");
                    Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
                    mOkHttpClientForAndroid = new OkHttpClient.Builder()
                            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .addInterceptor(new BaseInterceptor(null))

                            // TODO 增加缓存，是如何工作的？
                            .cookieJar(new NovateCookieManger(context))
                            .cache(cache)
                            .addInterceptor(new CaheInterceptor(context))
                            .addNetworkInterceptor(new CaheInterceptor(context))

                            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                            .hostnameVerifier(sslParams.unSafeHostnameVerifier)
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                            // TODO 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                            .connectionPool(new ConnectionPool(8, 10, TimeUnit.SECONDS))
                            .build();
                }
            }
        }
        return mOkHttpClientForAndroid;
    }

    /**
     * 初始化OkHttpClient
     */
    private static void initOkHttpClient() {

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        mOkHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(sslParams.unSafeHostnameVerifier)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                // TODO 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                .connectionPool(new ConnectionPool(8, 10, TimeUnit.SECONDS))
                .build();
    }


}
