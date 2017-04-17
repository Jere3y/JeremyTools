package jeremy.com.presenter.api;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import jeremy.com.MyApp;
import jeremy.com.utils.StateUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofit {

    public ZhihuApi ZhihuApiService;
    public GankApi GankApiService;
    public DailyApi DailyApiService;
    public static final String ZHIHU_BASE_URL = "http://news-at.zhihu.com/api/4/";
    public static final String GANK_BASE_URL = "http://gank.io/api/";
    public static final String DAILY_BASE_URL = "http://app3.qdaily.com/app3/";

    public ZhihuApi getZhihuApiService() {
        return ZhihuApiService;
    }

    public GankApi getGankApiService() {
        return GankApiService;
    }

    public DailyApi getDailyApiService() {
        return DailyApiService;
    }

    ApiRetrofit() {
        //cache url
        File httpCacheDirectory = new File(MyApp.mContext.getCacheDir(), "responses");
        int cacheSize = 20 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                CacheControl.Builder cacheBuilder = new CacheControl.Builder();
                cacheBuilder.maxAge(0, TimeUnit.SECONDS);
                cacheBuilder.maxStale(365, TimeUnit.DAYS);
                CacheControl cacheControl = cacheBuilder.build();

                Request request = chain.request();
                if (!StateUtils.isNetworkAvailable(MyApp.mContext)) {
                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();

                }
                Response response = chain.proceed(request);
                if (StateUtils.isNetworkAvailable(MyApp.mContext)) {
                    int maxAge = 300; // 有网时,直接联网读取
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public ,max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // 没网,缓存4周
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(cache).build();

        Retrofit retrofit_zhihu = new Retrofit.Builder()
                .baseUrl(ZHIHU_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        Retrofit retrofit_gank = new Retrofit.Builder()
                .baseUrl(GANK_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        Retrofit retrofit_daily = new Retrofit.Builder()
                .baseUrl(DAILY_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        ZhihuApiService = retrofit_zhihu.create(ZhihuApi.class);
        GankApiService = retrofit_gank.create(GankApi.class);
        DailyApiService = retrofit_daily.create(DailyApi.class);
    }

}
