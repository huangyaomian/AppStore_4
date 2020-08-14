package com.hym.appstore.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.view.ActionProvider;

import com.hym.appstore.R;

public class BadgeActionProvider extends ActionProvider {

    private ImageView mIcon;
    private TextView mTxtBadge;

    private View.OnClickListener mOnClickListener;

    public BadgeActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        int size = getContext().getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(size,size);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.menu_badge_provider, null, false);
        view.setLayoutParams(layoutParams);

        this.mIcon = view.findViewById(R.id.img_icon);
        this.mTxtBadge = view.findViewById(R.id.txt_badge);

        view.setOnClickListener(new BadgeMenuClickListener());
        return view;
    }

    public void setIcon(Drawable drawable){
        this.mIcon.setImageDrawable(drawable);
    }

    public void setIcon(@DrawableRes int res){
        this.mIcon.setImageResource(res);
    }

    public void setText(CharSequence c){
        showBadge();
        this.mTxtBadge.setText(c);
    }

    public void setBadge(int num){
        mTxtBadge.setText(num +"");
    }

    public int getBadgeNum(){
        try {
            return Integer.parseInt(mTxtBadge.getText().toString());
        }catch (NumberFormatException e){
            e.printStackTrace();
            return 0;
        }

    }

    public void hideBadge(){
        this.mTxtBadge.setVisibility(View.GONE);
    }

    public void showBadge(){
        this.mTxtBadge.setVisibility(View.VISIBLE);
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }


    private class BadgeMenuClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
        }
    }


}
