package jeremy.com.presenter.api;

import io.reactivex.Observable;
import jeremy.com.model.daily.DailyTimeLine;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DailyApi {

    @GET("homes/index/{num}.json")
    Observable<DailyTimeLine> getDailyTimeLine(@Path("num") String num);

    @GET("options/index/{id}/{num}.json")
    Observable<DailyTimeLine> getDailyFeedDetail(@Path("id") String id, @Path("num") String num);
}
