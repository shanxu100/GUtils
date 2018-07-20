package scut.luluteam.gutils.view.listitem.holder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 */
public class SimpleViewHolder extends RecyclerView.ViewHolder {

    public static final int TYPE_EMPTY = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_DEFAULT = 2;


    private ViewDataBinding binding;
    private int type;

    public SimpleViewHolder(View itemView, ViewDataBinding binding, int type) {
        super(itemView);
        this.binding = binding;
        this.type = type;
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

    public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
