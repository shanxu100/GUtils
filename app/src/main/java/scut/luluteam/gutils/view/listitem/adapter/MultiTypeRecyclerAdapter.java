package scut.luluteam.gutils.view.listitem.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scut.luluteam.gutils.R;
import scut.luluteam.gutils.view.listitem.holder.SimpleViewHolder;

import static scut.luluteam.gutils.view.listitem.holder.SimpleViewHolder.TYPE_DEFAULT;
import static scut.luluteam.gutils.view.listitem.holder.SimpleViewHolder.TYPE_EMPTY;
import static scut.luluteam.gutils.view.listitem.holder.SimpleViewHolder.TYPE_NORMAL;


public abstract class MultiTypeRecyclerAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    private Map<Class, Integer> paramsMap = new HashMap<>();
    private List<Object> mDatas;
    private static final String TAG = "MultiTypeRylAdapter";


    public MultiTypeRecyclerAdapter(List<Object> dataList, Param... params) {

        mDatas = dataList;
        if (params != null) {
            for (Param param : params) {
                paramsMap.put(param.clazz, param.itemLayoutId);
            }
        }

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
        if (mDatas != null && mDatas.size() != 0) {
            if (position < mDatas.size()) {
                Class clazz = mDatas.get(position).getClass();
                if (paramsMap.containsKey(clazz)) {
                    return paramsMap.get(clazz);
                }
            }
            return TYPE_DEFAULT;
        }
        return TYPE_EMPTY;
    }


    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_EMPTY) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.recycler_view_layout_empty, parent, false);
            SimpleViewHolder viewHolder = new SimpleViewHolder(binding.getRoot(), binding, TYPE_EMPTY);
            return viewHolder;
        }
        if (viewType == TYPE_DEFAULT) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, android.R.layout.simple_list_item_1, parent, false);
            SimpleViewHolder viewHolder = new SimpleViewHolder(binding.getRoot(), binding, TYPE_DEFAULT);
            return viewHolder;
        }
        int resId = viewType;
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, resId, parent, false);
        SimpleViewHolder simpleViewHolder = new SimpleViewHolder(binding.getRoot(), binding, TYPE_NORMAL);
        return simpleViewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {

        if (holder.getType() == TYPE_EMPTY) {
            Log.e(TAG, "数据为空，显示默认layout");
        } else if (holder.getType() == TYPE_DEFAULT) {
            Log.e(TAG, "未知数据类型，显示默认布局");
        } else {
            if (position < mDatas.size()) {
                onCustomeView(holder.getBinding(), position, mDatas.get(position));
                holder.getBinding().executePendingBindings();
            } else {
                Log.e(TAG, "数组越界------position=" + position + " l ist.size=" + mDatas.size());
            }
        }


    }


    @Override
    public int getItemCount() {
        if (mDatas == null || mDatas.size() == 0) {
            return 1;
        }
        return mDatas.size();
    }


    public void setDataList(List<Object> mDatas) {
        if (this.mDatas != null) {
            this.mDatas.clear();
            this.mDatas.addAll(mDatas);
        } else {
            this.mDatas = mDatas;
        }

        notifyDataSetChanged();
    }

    public static final class Param {

        /**
         * item 的布局
         */
        @IdRes
        public int itemLayoutId;

        /**
         * item 对应的数据.class
         */
        public Class clazz;

        public Param(int itemLayoutId, Class clazz) {
            this.itemLayoutId = itemLayoutId;
            this.clazz = clazz;
        }
    }

    protected abstract void onCustomeView(ViewDataBinding binding, int position, Object data);
}
