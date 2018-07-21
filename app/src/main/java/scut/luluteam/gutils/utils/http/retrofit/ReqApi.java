package scut.luluteam.gutils.utils.http.retrofit;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public interface ReqApi {

    /**
     * 以下所有接口均使用 @Url 注解
     * 所以 BASE_URL 并未起作用
     */
    String BASE_URL = "http://www.baidu.com/";


    @POST("{path}")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<String> doPostJson(@Url String url, @Body RequestBody json);

    @FormUrlEncoded
    @POST
    Observable<String> doPost(@Url String url, @Header("Content-Type") String contentType
            , @FieldMap Map<String, String> map);


    @GET
    Observable<String> doGet(@Url String url, @Header("Content-Type") String contentType
            , @QueryMap Map<String, String> map);

    @GET
    Observable<ResponseBody> doDownload(@Url String fileUrl);

}
