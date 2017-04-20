package jeremy.com.view.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Xin on 2017/4/20 0020,20:58.
 */

public class BottomNavigationBehavior extends CoordinatorLayout.Behavior<View> {

    private int mHeight;
    private int lastOffset = 0;

    public BottomNavigationBehavior() {
        super();
    }

    public BottomNavigationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {

        //因为Behavior只对CoordinatorLayout的直接子View生效，因此将依赖关系转移到AppBarLayout
        boolean result = dependency instanceof AppBarLayout;
        if (result) {
            mHeight = dependency.getHeight();
        }
        return result;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();

        //得到依赖View的滑动距离
        int offset = ((AppBarLayout.Behavior) params.getBehavior()).getTopAndBottomOffset();

        child.setTranslationY(-offset);
        return true;

    }
}
