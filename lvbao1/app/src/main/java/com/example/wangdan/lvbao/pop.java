package com.example.wangdan.lvbao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wangdan on 2016/8/2.
 */
public class pop extends Activity {
    private ListView list;
    private ListViewAdapter adapter;
    private Button begin;
    private Button cacel;
    private List<PoiInfo> resultList;
    private double lontitue;
    private double latitue;
    private double longitude1;
    private double latitude1;
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static List<Activity> activityList = new LinkedList<Activity>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop);

        Intent intent=getIntent();
        resultList = (List<PoiInfo>) this.getIntent().getSerializableExtra("加油站信息");
        Bundle bundle = intent.getExtras();
        longitude1= bundle.getDouble("log");
        latitude1= bundle.getDouble("lan");

        begin = (Button) findViewById(R.id.btn_begin);
        cacel = (Button)findViewById(R.id.btn_cancel);

        list = (ListView)findViewById(R.id.listview);

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeplanToNavi();
            }
        });

        cacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

      for (int i = 0; i < 10; i++) {
            System.out.println("i"+resultList.get(i).name);
        }
       adapter = new ListViewAdapter(pop.this,resultList);
        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void routeplanToNavi() {
        BNRoutePlanNode.CoordinateType coType = BNRoutePlanNode.CoordinateType.GCJ02;
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;


        BDLocation srcl = new BDLocation();
        srcl.setLatitude(latitude1);
        srcl.setLongitude(longitude1);

        BDLocation dest = new BDLocation();
        dest.setLatitude(latitue);
        dest.setLongitude(lontitue);

        BDLocation last1 = bd2gc(srcl);
        BDLocation dest1 = bd2gc(dest);

        sNode = new BNRoutePlanNode(last1.getLongitude(),last1.getLatitude(),"我的位置",null,coType);
        eNode = new BNRoutePlanNode(dest1.getLongitude(),dest1.getLatitude(),"终点位置",null,coType);




        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1,true, new DemoRoutePlanListener(sNode));
        }
    }


    private BDLocation  bd2gc(BDLocation bdl)
    {
        return LocationClient.getBDLocationInCoorType(bdl, BDLocation.BDLOCATION_BD09LL_TO_GCJ02);
    }



    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */

            for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {

                    return;
                }
            }
            Intent intent = new Intent(pop.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub

        }
    }






    public class ListViewAdapter extends BaseAdapter {

        private Context context;
        private List<PoiInfo> allPoi;

        // 用于记录每个RadioButton的状态，并保证只可选一个
        HashMap<String, Boolean> states = new HashMap<String, Boolean>();

        class ViewHolder {

            TextView tvName;
            RadioButton rb_state;
        }

        public ListViewAdapter(Context context, List<PoiInfo> allPoi) {
            // TODO Auto-generated constructor stub
            this.allPoi= allPoi;
            this.context = context;
        }

        @Override
        public int getCount() {
            return allPoi.size();
        }

        @Override
        public Object getItem(int position) {
            return allPoi.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater inflater = LayoutInflater.from(context);
            if(convertView==null)
            {
                convertView = inflater.inflate(R.layout.radiobutton,null);
                holder = new ViewHolder();

                holder.tvName = (TextView)convertView.findViewById(R.id.tv_device_name);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvName.setText((position+1)+"."+allPoi.get(position).name);
            final RadioButton radio = (RadioButton)convertView.findViewById(R.id.rb_light);
            holder.rb_state = radio;
            holder.rb_state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(String key:states.keySet())
                        states.put(key,false);
                    states.put(String.valueOf(position),radio.isChecked());
                    ListViewAdapter.this.notifyDataSetChanged();
                    latitue = allPoi.get(position).location.latitude;
                    lontitue = allPoi.get(position).location.longitude;
                    Log.i(latitue + "", lontitue + "");
                }

            });

            boolean res = false;
            if(states.get(String.valueOf(position))==null||states.get(String.valueOf(position))==false)
            {
                res = false;
                states.put(String.valueOf(position),false);
            }
            else
                res = true;

            holder.rb_state.setChecked(res);
            return convertView;
        }
    }

}
