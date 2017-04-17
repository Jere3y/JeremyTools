package jeremy.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jeremy.com.R;
import jeremy.com.bean.FileInfo;

/**
 * 这个适配器主要是实现了onClick事件。也可以对文件进行排序
 * Created by Xin on 2017/3/21 0021,16:43.
 */

public class RecyclerExplorerAdapter extends RecyclerView.Adapter<RecyclerExplorerAdapter.MyHolder> {
    private Context context;
    private List<FileInfo> fileInfoList;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerExplorerAdapter(Context context, List<FileInfo> fileInfoList) {
        this.context = context;
        this.fileInfoList = fileInfoList;

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    /**
     * 在adapter中实现点击功能，因为本身不提供。
     *
     * @param mOnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.rececler_explorer_item, parent, false));

    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        //在这里设置监听
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
        FileInfo fileInfo = fileInfoList.get(position);
        if (fileInfo.isDir()) {
            holder.iv_file_icon.setBackgroundResource(R.mipmap.ic_dir);
        } else {
            holder.iv_file_icon.setBackgroundResource(R.mipmap.ic_file);
        }
        holder.tv_file_name.setText(fileInfo.getName());

    }

    @Override
    public int getItemCount() {
        return fileInfoList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView iv_file_icon;
        TextView tv_file_name;

        public MyHolder(View itemView) {
            super(itemView);
            iv_file_icon = (ImageView) itemView.findViewById(R.id.iv_file_icon);
            tv_file_name = (TextView) itemView.findViewById(R.id.tv_file_name);
        }
    }

}
