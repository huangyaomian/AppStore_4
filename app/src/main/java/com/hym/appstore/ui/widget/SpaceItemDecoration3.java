package com.hym.appstore.ui.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration3 extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemDecoration3(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        //由于每行都只有2个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = space;
        }
    }
}
