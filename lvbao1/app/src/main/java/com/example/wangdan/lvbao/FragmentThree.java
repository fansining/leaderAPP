package com.example.wangdan.lvbao;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.example.page4.Comment;
import com.example.page4.CustomImageView;
import com.example.page4.Image;
import com.example.page4.ItemView;
import com.example.page4.Item;
import com.example.page4.NineGridlayout;
import com.example.page4.ScreenTools;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.smssdk.gui.DefaultContactViewItem;

/**
 * Created by wangdan on 2016/7/28.
 */
public class FragmentThree extends Fragment {
    View v;

    Context context;
    private ListView mListview;
    private MyAdapter myAdapter;

    private String[][] images=new String[][]{
             {"file:///android_asset/img2.jpg","250","250"}
            ,{"file:///android_asset/img3.jpg","250","250"}
            ,{"file:///android_asset/img4.jpg","250","250"}
            ,{"file:///android_asset/img5.jpg","250","250"}
            ,{"file:///android_asset/img6.jpg","250","250"}
            ,{"file:///android_asset/img7.jpg","250","250"}
            ,{"file:///android_asset/img8.jpg","250","250"}
            ,{"file:///android_asset/img9.jpg","250","250"}
            ,{"file:///android_asset/img8.jpg","250","250"}
            ,{"file:///android_asset/img1.jpeg","1280","800"}
            ,{"file:///android_asset/img10.jpg","640","960"}
    };

    //弹出viewpager
    private ArrayList<View> pageview;
    private ImageView[] imageViews;
    int image[] = {R.drawable.img10, R.drawable.img9, R.drawable.img8,R.drawable.img7,R.drawable.img7};
    private LayoutInflater inflater;
    private View layout;
    private PopupWindow menuWindow;
    private Boolean flag = true;
    private ViewPager viewPager;
    private ViewGroup group;
    private ImageView imageView;
    private ImageButton viewbutton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_three, container, false);
        context = getActivity();
        mListview = (ListView)v.findViewById(R.id.listview);
        myAdapter = new MyAdapter(getContext(), getData());
        mListview.setAdapter(myAdapter);
        setListViewHeightBasedOnChildren(mListview);

        return v;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    private ArrayList<Item> getData(){

        ArrayList<Item> data = new ArrayList<>();
        //这里单独添加一条单条的测试数据，用来测试单张的时候横竖图片的效果
        ArrayList<Image> singleList1=new ArrayList<>();
        singleList1.add(new Image(images[10][0],Integer.parseInt(images[10][1]),Integer.parseInt(images[10][2])));
        data.add(new Item(R.drawable.xiaona,"雯雯","今天很感动","是吧",singleList1));

        for(int i = 0; i<9; i++)
        {
            ArrayList<Image> singleList=new ArrayList<>();
            for(int j=0;j<=i;j++){
            singleList.add(new Image(images[j][0],Integer.parseInt(images[j][1]),Integer.parseInt(images[j][2])));
            }
            data.add(new Item(R.drawable.xiaona,"小娜","你哈","对于",singleList));


        }
        return data;
    }
        private class MyAdapter extends BaseAdapter implements ItemView.OnCommentListener{
        private Context context;
        private ArrayList<Item> mData;
        private Map<Integer,ItemView> mCachedViews = new HashMap<>();

        public MyAdapter(Context context,ArrayList<Item> mData){
            this.context = context;
            this.mData = mData;

        }
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            View view;
            List<Image> itemList = mData.get(position).getDatalist();

           if(convertView!=null)
            {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
            }
           else
            {
                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listview_item,null,false);
                viewHolder = new ViewHolder();
                viewHolder.ivMore = (NineGridlayout) view.findViewById(R.id.iv_ngrid_layout);
                viewHolder.ivOne = (CustomImageView) view.findViewById(R.id.iv_oneimage);

               view.setTag(viewHolder);
            }
            //判断view对象是否是特定类ItemView的实例
            if(view instanceof ItemView)
            {
              Item data = (Item)getItem(position);
                ((ItemView)view).setData(data);
                ((ItemView)view).setPosition(position);
                ((ItemView)view).setCommentListener(this);
                cacheView(position, (ItemView)view);

            }
            if (itemList.isEmpty() || itemList.isEmpty()) {
                viewHolder.ivMore.setVisibility(View.GONE);
                viewHolder.ivOne.setVisibility(View.GONE);
            } else if (itemList.size() == 1) {
                viewHolder.ivMore.setVisibility(View.GONE);
                viewHolder.ivOne.setVisibility(View.VISIBLE);

                handlerOneImage(viewHolder, itemList.get(0));
            } else {
                viewHolder.ivMore.setVisibility(View.VISIBLE);
                viewHolder.ivOne.setVisibility(View.GONE);

                viewHolder.ivMore.setImagesData(itemList);
            }
            return view;
        }

            private void handlerOneImage(ViewHolder viewHolder, Image image) {
                int totalWidth;
                int imageWidth;
                int imageHeight;
                ScreenTools screentools = ScreenTools.instance(context);
                totalWidth = screentools.getScreenWidth() - screentools.dip2px(80);
                imageWidth = screentools.dip2px(image.getWidth());
                imageHeight = screentools.dip2px(image.getHeight());
                if (image.getWidth() <= image.getHeight()) {
                    if (imageHeight > totalWidth) {
                        imageHeight = totalWidth;
                        imageWidth = (imageHeight * image.getWidth()) / image.getHeight();
                    }
                } else {
                    if (imageWidth > totalWidth) {
                        imageWidth = totalWidth;
                        imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
                    }
                }
                ViewGroup.LayoutParams layoutparams = viewHolder.ivOne.getLayoutParams();
                layoutparams.height = imageHeight;
                layoutparams.width = imageWidth;
                viewHolder.ivOne.setLayoutParams(layoutparams);
                viewHolder.ivOne.setClickable(true);
                viewHolder.ivOne.setScaleType(android.widget.ImageView.ScaleType.FIT_XY);
                viewHolder.ivOne.setImageUrl(image.getUrl());


            }

        @Override
        public void onComment(int position)
        {

            showCommentView(position);
        }

        private void cacheView(int position,ItemView view)
        {

            Iterator<Map.Entry<Integer, ItemView>> entries = mCachedViews.entrySet().iterator();

            while (entries.hasNext()) {

                Map.Entry<Integer, ItemView> entry = entries.next();
                if (entry.getValue() == view && entry.getKey() != position) {
                    mCachedViews.remove(entry.getKey());
                    break;
                }
            }

            mCachedViews.put(position, view);



        }

        private void showCommentView(final int position) {

            Boolean flag ;
            flag = true;
            if(flag==true) {

                inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.popcomment, null);

                menuWindow = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
               // menuWindow.showAsDropDown(, 0, layout.getHeight());
                menuWindow.setFocusable(true);
                menuWindow.setBackgroundDrawable(new BitmapDrawable());
                menuWindow.showAtLocation(getActivity().findViewById(R.id.linear),
                        Gravity.BOTTOM, 0, 0);

                menuWindow.setOutsideTouchable(true);
                menuWindow.update();

                Button btn = (Button) layout.findViewById(R.id.submit);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuWindow.dismiss();
                        EditText et = (EditText) layout.findViewById(R.id.edit);
                        String s = et.getText().toString();

                        if (!TextUtils.isEmpty(s)) {

                            // update model
                            Comment comment = new Comment(s);
                            mData.get(position).getComments().add(comment);

                            // update view maybe
                            ItemView itemView = mCachedViews.get(position);

                            if (itemView != null && position == itemView.getPosition()) {
                                itemView.addComment();
                            }

                            et.setText("");

                        }
                    }
                });
            }
            else{
                menuWindow.dismiss();
            }
        }

    }
    class ViewHolder {
        public NineGridlayout ivMore;
        public CustomImageView ivOne;
    }
}

