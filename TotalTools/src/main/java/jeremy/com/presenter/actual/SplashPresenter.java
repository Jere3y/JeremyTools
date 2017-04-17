package jeremy.com.presenter.actual;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import jeremy.com.R;
import jeremy.com.model.zhihu.SplashImage;
import jeremy.com.presenter.BasePresenter;
import jeremy.com.view.iview.ISplashView;


public class SplashPresenter extends BasePresenter<ISplashView> {

    private Context context;
    private ISplashView splashView;
    private ImageView coverImg;

    public SplashPresenter(Context context) {
        this.context = context;
    }

    public void getSplashImage() {
        splashView = getView();
        if (splashView != null) {
            coverImg = splashView.getCoverImg();

            zhihuApi.getSplashImage()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<SplashImage>() {
                        @Override
                        public void accept(@NonNull SplashImage splashImage) throws Exception {
                            disPlayImage(splashImage, coverImg);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                            Toast.makeText(context, R.string.load_error, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void disPlayImage(SplashImage splashImage, ImageView iv) {
        Glide.with(context).load(splashImage.getImg()).centerCrop().into(iv);
    }

    public void destroyImg() {
        Glide.clear(coverImg);
    }

}
