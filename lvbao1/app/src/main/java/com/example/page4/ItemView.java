package com.example.page4;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.wangdan.lvbao.R;


/**
 * Created by cmm on 15/10/31.
 */
public class ItemView extends LinearLayout implements View.OnClickListener {

    private int mPosition;
    private Item mData;

    private ImageView mPortraitView;
    private TextView mUserNameView;
    private TextView mContentView;
    private TextView mCreatedAtView;
    private LinearLayout mCommentLayout;
    private View mMoreView;
    private TextView zhan;
    private ImageView line;

    private PopupWindow mMorePopupWindow;
    private int mShowMorePopupWindowWidth;
    private int mShowMorePopupWindowHeight;

    private OnCommentListener mCommentListener;

    public ItemView(Context context) {
        super(context);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public interface OnCommentListener {
        void onComment(int position);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mPortraitView = (ImageView) findViewById(R.id.portrait);
        mUserNameView = (TextView) findViewById(R.id.nick_name);
        mContentView = (TextView) findViewById(R.id.content);
        mCreatedAtView = (TextView) findViewById(R.id.created_at);
        mCommentLayout = (LinearLayout) findViewById(R.id.comment_layout);
        mMoreView = findViewById(R.id.more_btn);
        zhan = (TextView)findViewById(R.id.zhan);
        line = (ImageView)findViewById(R.id.line);

    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public void setCommentListener(OnCommentListener l) {
        this.mCommentListener = l;
    }

    public void setData(Item data) {

        mData = data;

        mPortraitView.setImageResource(data.getPortraitId());
        mUserNameView.setText(data.getNickName());
        mContentView.setText(data.getContent());

        updateComment();

        mMoreView.setOnClickListener(this);
    }

    /**
     * 弹出点赞和评论框
     *
     * @param moreBtnView
     */
    private void showMore(View moreBtnView) {

        if (mMorePopupWindow == null) {

            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = li.inflate(R.layout.layout_more, null, false);

            mMorePopupWindow = new PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mMorePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mMorePopupWindow.setOutsideTouchable(true);
            mMorePopupWindow.setTouchable(true);

            content.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            mShowMorePopupWindowWidth = content.getMeasuredWidth();
            mShowMorePopupWindowHeight = content.getMeasuredHeight();

            View parent = mMorePopupWindow.getContentView();

            TextView like = (TextView) parent.findViewById(R.id.like);
            TextView comment = (TextView) parent.findViewById(R.id.comment);

            // 点赞的监听器
            like.setOnClickListener(this);
            comment.setOnClickListener(this);
        }

        else if (mMorePopupWindow.isShowing()) {
            mMorePopupWindow.dismiss();
        }
        else {
            int heightMoreBtnView = moreBtnView.getHeight();

            mMorePopupWindow.showAsDropDown(moreBtnView, -mShowMorePopupWindowWidth,
                    -(mShowMorePopupWindowHeight + heightMoreBtnView) / 2);
        }
    }

    private void updateComment() {
        if (mData.hasComment()) {
             //设置文本与图片相对位置时
            mCreatedAtView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.bg_top);
            mCommentLayout.removeAllViews();
            mCommentLayout.setVisibility(View.VISIBLE);

            for (Comment c : mData.getComments()) {
                TextView t = new TextView(getContext());
                t.setLayoutParams(new LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
                t.setBackgroundColor(getResources().getColor(R.color.colorCommentLayoutBg));
               // t.setTextSize(14);
                t.setPadding(5, 2, 0, 3);
                t.setLineSpacing(3, (float) 1.5);
                t.setText(c.getComment());
                mCommentLayout.addView(t);
            }
        } else {
            mCreatedAtView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            mCommentLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.more_btn) {

            showMore(v);
        }
        else if (id == R.id.comment) {

            if (mCommentListener != null) {

               mCommentListener.onComment(mPosition);

                if (mMorePopupWindow != null && mMorePopupWindow.isShowing()) {
                    mMorePopupWindow.dismiss();
                }
            }

        }
        else if (id == R.id.like)
        {
            zhan.setVisibility(VISIBLE);
            line.setVisibility(VISIBLE);
        }
    }

    public int getPosition() {
        return mPosition;
    }

    public void addComment() {
        updateComment();
    }
}
