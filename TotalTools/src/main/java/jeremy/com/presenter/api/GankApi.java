package jeremy.com.presenter.api;


import io.reactivex.Observable;
import jeremy.com.model.gank.GankData;
import jeremy.com.model.gank.Meizhi;
import jeremy.com.model.gank.Video;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface GankApi {

    @GET("data/福利/10/{page}")
    Observable<Meizhi> getMeizhiData(@Path("page") int page);

    @GET("day/{year}/{month}/{day}")
    Observable<GankData> getGankData(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    @GET("data/休息视频/10/{page}")
    Observable<Video> getVideoData(@Path("page") int page);
}
