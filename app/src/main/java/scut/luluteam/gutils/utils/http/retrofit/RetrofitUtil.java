package scut.luluteam.gutils.utils.http.retrofit;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import scut.luluteam.gutils.utils.http.okhttp.OkHttpManager;


public class RetrofitUtil {

    private static final String TAG = "RetrofitUtil";
    public static final String defaultContentType = "application/x-www-form-urlencoded; charset=utf-8";
    private static RetrofitUtil instance;


    private ReqApi reqApi;

    private Action defaultOnCompleteAction;
    private Consumer<Throwable> defaultOnErrorConsumer;

    public RetrofitUtil() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(ReqApi.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
                //返回String的类型的
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpManager.getOkHttpClient());
        reqApi = retrofitBuilder.build().create(ReqApi.class);

        defaultOnCompleteAction = new Action() {
            @Override
            public void run() throws Exception {
                Log.i(TAG, "complete http request ");
            }
        };
        defaultOnErrorConsumer = new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, "error on http request");
                throwable.printStackTrace();
            }
        };
    }

    private static RetrofitUtil getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtil.class) {
                if (instance == null) {
                    instance = new RetrofitUtil();
                }
            }
        }
        return instance;
    }

    private static ReqApi getReqApi() {
        return getInstance().reqApi;
    }


    //==================================================================
    //
    //==================================================================

    public static <T> T create(String baseUrl, Class<T> classOfType) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpManager.getOkHttpClient());
        return retrofitBuilder.build().create(classOfType);
    }

    public static void commomPostAsyn(String url, Map<String, String> params, String contentType, final Callback callback) {
        final Disposable disposable =
                getReqApi().doPost(url, contentType, params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                callback.onData(s);
                            }
                        }, getInstance().defaultOnErrorConsumer, getInstance().defaultOnCompleteAction);
    }

    public static void commomPostAsyn(String url, Map<String, String> params, final Callback callback) {
        commomPostAsyn(url, params, defaultContentType, callback);
    }

    public static void commomGetAsyn(String url, Map<String, String> params, String contentType, final Callback callback) {
        Disposable disposable =
                getReqApi().doGet(url, contentType, params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                callback.onData(s);
                            }
                        }, getInstance().defaultOnErrorConsumer, getInstance().defaultOnCompleteAction);

    }

    public static void commomGetAsyn(String url, Map<String, String> params, final Callback callback) {
        commomGetAsyn(url, params, defaultContentType, callback);
    }

    public static void commonPostJson(String url, String json, final Callback callback) {
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            byte[] postData = new byte[0];
            if (json != null) {
                postData = json.getBytes("UTF-8");
            }
            RequestBody jsonBody = RequestBody.create(JSON, postData);
            Disposable disposable =
                    getReqApi().doPostJson(url, jsonBody)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    callback.onData(s);
                                }
                            }, getInstance().defaultOnErrorConsumer, getInstance().defaultOnCompleteAction);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    //=========================================================================
    //
    //=========================================================================


    public interface Callback {

        void onData(String data);

    }
}
