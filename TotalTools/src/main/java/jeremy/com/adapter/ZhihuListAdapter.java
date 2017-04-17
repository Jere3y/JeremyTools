package jeremy.com.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jeremy.com.R;
import jeremy.com.model.zhihu.NewsTimeLine;
import jeremy.com.model.zhihu.Stories;
import jeremy.com.model.zhihu.TopStories;
import jeremy.com.utils.ScreenUtil;
import jeremy.com.view.activity.ZhihuWebActivity;
import jeremy.com.view.widget.TopStoriesViewPager;

public class ZhihuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private NewsTimeLine newsTimeLine;
    private int status = 1;
    public static final int LOAD_MORE = 0;
    public static final int LOAD_PULL_TO = 1;
    public static final int LOAD_NONE = 2;
    public static final int LOAD_END = 3;
    private static final int TYPE_TOP = -1;
    private static final int TYPE_FOOTER = -2;

    public ZhihuListAdapter(Context context, NewsTimeLine newsTimeLine) {
        this.context = context;
        this.newsTimeLine = newsTimeLine;
    }

    @Override
    public int getItemViewType(int position) {
        if (newsTimeLine.getTop_stories() != null) {
            if (position == 0) {
                return TYPE_TOP;
            } else if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return position;
            }
        } else if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return position;
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof TopStoriesViewHolder) {
            TopStoriesViewHolder topStoriesViewHolder = (TopStoriesViewHolder) holder;
            topStoriesViewHolder.vp_top_stories.startAutoRun();
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof TopStoriesViewHolder) {
            TopStoriesViewHolder topStoriesViewHolder = (TopStoriesViewHolder) holder;
            topStoriesViewHolder.vp_top_stories.stopAutoRun();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP) {
            View rootView = View.inflate(parent.getContext(), R.layout.item_zhihu_top_stories, null);
            return new TopStoriesViewHolder(rootView);
        } else if (viewType == TYPE_FOOTER) {
            View view = View.inflate(parent.getContext(), R.layout.activity_view_footer, null);
            return new FooterViewHolder(view);
        } else {
            View rootView = View.inflate(parent.getContext(), R.layout.item_zhihu_stories, null);
            return new StoriesViewHolder(rootView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.bindItem();
        } else if (holder instanceof TopStoriesViewHolder) {
            TopStoriesViewHolder topStoriesViewHolder = (TopStoriesViewHolder) holder;
            topStoriesViewHolder.bindItem(newsTimeLine.getTop_stories());
        } else if (holder instanceof StoriesViewHolder) {
            StoriesViewHolder storiesViewHolder = (StoriesViewHolder) holder;
            storiesViewHolder.bindItem(newsTimeLine.getStories().get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return newsTimeLine.getStories().size() + 2;
    }


    /**
     * footer view
     */
    class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_load_prompt)
        TextView tv_load_prompt;
        @BindView(R.id.progress)
        ProgressBar progress;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.instance(context).dip2px(40));
            itemView.setLayoutParams(params);
        }

        private void bindItem() {
            switch (status) {
                case LOAD_MORE:
                    progress.setVisibility(View.VISIBLE);
                    tv_load_prompt.setText("正在加载...");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_PULL_TO:
                    progress.setVisibility(View.GONE);
                    tv_load_prompt.setText("上拉加载更多");
                    itemView.setVisibility(View.VISIBLE);
                    break;
                case LOAD_NONE:
                    System.out.println("LOAD_NONE----");
                    progress.setVisibility(View.GONE);
                    tv_load_prompt.setText("已无更多加载");
                    break;
                case LOAD_END:
                    itemView.setVisibility(View.GONE);
                default:
                    break;
            }
        }
    }


    /**
     * TopStories
     */
    class TopStoriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.vp_top_stories)
        TopStoriesViewPager vp_top_stories;
        @BindView(R.id.tv_top_title)
        TextView tv_top_title;

        public TopStoriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bindItem(List<TopStories> topList) {
            vp_top_stories.init(topList, tv_top_title, new TopStoriesViewPager.ViewPagerClickListenner() {
                @Override
                public void onClick(TopStories item) {
                    context.startActivity(ZhihuWebActivity.newIntent(context, item.getId()));
                }
            });
        }
    }

    /**
     * Stories
     */
    class StoriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_stories)
        CardView card_stories;
        @BindView(R.id.tv_stories_title)
        TextView tv_stories_title;
        @BindView(R.id.iv_stories_img)
        ImageView iv_stories_img;

        public StoriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ScreenUtil screenUtil = ScreenUtil.instance(context);
            int screenWidth = screenUtil.getScreenWidth();
            card_stories.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

        }

        public void bindItem(final Stories stories) {
            tv_stories_title.setText(stories.getTitle());
            String[] images = stories.getImages();
            Glide.with(context).load(images[0]).centerCrop().into(iv_stories_img);

            card_stories.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(ZhihuWebActivity.newIntent(context, stories.getId()));
                }
            });
        }
    }

    // change recycler state
    public void updateLoadStatus(int status) {
        this.status = status;
        notifyDataSetChanged();
    }
}
