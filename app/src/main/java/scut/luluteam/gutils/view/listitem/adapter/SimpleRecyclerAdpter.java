package scut.luluteam.gutils.view.listitem.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;



import java.util.List;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.view.listitem.holder.SimpleViewHolder;

import static scut.luluteam.gutils.view.listitem.holder.SimpleViewHolder.TYPE_EMPTY;
import static scut.luluteam.gutils.view.listitem.holder.SimpleViewHolder.TYPE_NORMAL;


/**
 * ListView样式，显示单一数据元素
 *
 * @param <T>
 */
public abstract class SimpleRecyclerAdpter<T> extends RecyclerView.Adapter<SimpleViewHolder> {

    private List<T> mDatas;

    private int layoutId;


    private static final String TAG = "SimpleRecyclerAdpter";


    public SimpleRecyclerAdpter(List<T> mDatas, int layoutId) {
        this.mDatas = mDatas;
        this.layoutId = layoutId;
    }

    public void setDataList(List<T> mDatas) {
        if (this.mDatas != null) {
            this.mDatas.clear();
            this.mDatas.addAll(mDatas);
        } else {
            this.mDatas = mDatas;
        }

        notifyDataSetChanged();
    }

    public void customRecyclerView(Context context, RecyclerView recyclerView) {
        //垂直线性布局——类似于list
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
//        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        //添加默认的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
    }


    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || mDatas.size() == 0) {
            return TYPE_EMPTY;
        }
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_NORMAL) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, layoutId, parent, false);
            SimpleViewHolder simpleViewHolder = new SimpleViewHolder(binding.getRoot(), binding, TYPE_NORMAL);
            return simpleViewHolder;
        } else {
            //viewType==TYPE_EMPTY
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.recycler_view_layout_empty, parent, false);
            SimpleViewHolder viewHolder = new SimpleViewHolder(binding.getRoot(), binding, TYPE_EMPTY);
            return viewHolder;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        if (holder.getType() == TYPE_EMPTY) {
            Log.e(TAG, "数据为空，显示默认layout");
        } else {
            if (position < mDatas.size()) {
                onCustomeView(holder.getBinding(), position, mDatas.get(position));
                holder.getBinding().executePendingBindings();
            } else {
                Log.e(TAG, "数组越界------position=" + position + " l ist.size=" + mDatas.size());
            }
        }

    }

    ;

    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() == 0) {
            return 1;
        }
        return mDatas.size();
    }


    protected abstract void onCustomeView(ViewDataBinding binding, int position, T data);
}
