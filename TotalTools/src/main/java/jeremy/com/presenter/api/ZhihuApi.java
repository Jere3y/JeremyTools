package jeremy.com.presenter.api;

import io.reactivex.Observable;
import jeremy.com.model.zhihu.News;
import jeremy.com.model.zhihu.NewsTimeLine;
import jeremy.com.model.zhihu.SplashImage;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface ZhihuApi {

    @GET("start-image/1080*1920")
    Observable<SplashImage> getSplashImage();

    @GET("news/latest")
    Observable<NewsTimeLine> getLatestNews();

    @GET("news/before/{time}")
    Observable<NewsTimeLine> getBeforetNews(@Path("time") String time);

    @GET("news/{id}")
    Observable<News> getDetailNews(@Path("id") String id);
}
