package scut.luluteam.gutils.retrofit;

import java.util.HashMap;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitParams {

    private String baseUrl;
    private boolean isCache;
    private Converter.Factory converterFactory;


    private RetrofitParams(String baseUrl, boolean isCache, Converter.Factory converterFactory) {
        this.baseUrl = baseUrl;
        this.isCache = isCache;
        this.converterFactory = converterFactory;
    }

    public static class Builder {
        private String baseUrl = "";
        private boolean isCache = false;
        private Converter.Factory converterFactory = ScalarsConverterFactory.create();

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setCache(boolean cache) {
            isCache = cache;
            return this;
        }

        public Builder setConverterFactory(Converter.Factory converterFactory) {
            this.converterFactory = converterFactory;
            return this;
        }

        public RetrofitParams build() {
            return new RetrofitParams(baseUrl, isCache, converterFactory);
        }

    }
}
