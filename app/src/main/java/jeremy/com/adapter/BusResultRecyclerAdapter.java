package jeremy.com.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;

import java.util.List;

import jeremy.com.R;
import jeremy.com.activity.BusRouteDetailActivity;
import jeremy.com.utils.AMapUtil;

/**
 * Created by Xin on 2017/3/24 0024,18:30.
 */

public class BusResultRecyclerAdapter extends RecyclerView.Adapter<BusResultRecyclerAdapter.MyHolder> {
    private Context mContext;
    private List<BusPath> mBusPathList;
    private BusRouteResult mBusRouteResult;
    private BusPath item;


    public BusResultRecyclerAdapter(Context context, BusRouteResult busrouteresult) {
        mContext = context;
        mBusRouteResult = busrouteresult;
        mBusPathList = busrouteresult.getPaths();
    }

    @Override
    public BusResultRecyclerAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_bus_result, null);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        item = mBusPathList.get(position);
        holder.title.setText(AMapUtil.getBusPathTitle(item));
        holder.des.setText(AMapUtil.getBusPathDes(item));
    }

    @Override
    public int getItemCount() {
        return mBusPathList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView des;

        MyHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.bus_path_title);
            des = (TextView) itemView.findViewById(R.id.bus_path_des);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext.getApplicationContext(),
                            BusRouteDetailActivity.class);
                    intent.putExtra("bus_path", item);
                    intent.putExtra("bus_result", mBusRouteResult);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
