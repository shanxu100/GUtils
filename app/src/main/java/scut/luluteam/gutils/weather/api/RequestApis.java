package scut.luluteam.gutils.weather.api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import scut.luluteam.gutils.weather.bean.Response;

public interface RequestApis {

    String weatherDataUrl = "https://free-api.heweather.com/";

    String key = "c3292385f67f4441bb03283c0a7a9f73";


    @GET("s6/weather/now")
    Call<Response> getNowWeather(@Query("location") String location, @Query("key") String key);

}
