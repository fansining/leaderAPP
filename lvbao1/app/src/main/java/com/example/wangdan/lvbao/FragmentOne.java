package com.example.wangdan.lvbao;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;

import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;



import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by wangdan on 2016/7/28.
 */
public class FragmentOne extends Fragment{
    private MapView mMapView;
    private ImageView mButton1;

    private boolean flag = true;
    private BaiduMap mBaiduMap;
    private Context context;
    private Animation showAnim = null;
    private Animation hideAnim = null;
    private Animation showAnim1 = null;
    private Animation hideAnim1= null;
    private ArcMenu mArcMenu;
    // 定位相关
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private boolean isFirstIn = true;
    private double mLatitude;
    private double mLongtitude;
    // 自定义定位图标
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    private MyLocationConfiguration.LocationMode mLocationMode;
    //卫星导航菜单
    private ImageView weixin,putong,luopan,reli,dingwei,lukuang;
    //导航功能
    private boolean flog = true;
    private LatLng mLastLocationData;
    private LatLng mDestLocationData;
    private String mSDCardPath = null;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    public static List<Activity> activityList = new LinkedList<Activity>();
    //poi检索
    private PoiSearch mPoiSearch;

    private final int pageCapacity = 50;
    private int pageNum = 0;
    private final int radius = 10000;
    private double latitude;
    private double longitude;
    private Overlay myOverlay;
    private  OverlayOptions options;
    private Marker maker;
    private  double latitude1;
    private  double longtitude1;
    private ArcMenu arcMenu;
    private Click listener = new Click();

    private int[] Imageid = {R.id.zhoubian,R.id.wanle,R.id.gouwu,R.id.canguan,R.id.jiudian,R.id.jingdian};
    private List<ImageView> imageViewList= new ArrayList<ImageView>();
    private boolean flag1=true;


   
    OnGetPoiSearchResultListener poiListener = null;
    List<String> List= new ArrayList<String>();
    private Bundle bundle = new Bundle();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
         SDKInitializer.initialize(getActivity().getApplicationContext());
         View view = inflater.inflate(R.layout.fragment_one, container, false);
         this.context = view.getContext();
         mMapView = (MapView) view.findViewById(R.id.mapView);
        initMap();
        //初始化定位
        initLocation();
        centerToMyLocation();
        //初试化卫星导航菜单


        showAnim = AnimationUtils.loadAnimation(context,R.animator.show);
        hideAnim = AnimationUtils.loadAnimation(context,R.animator.hide);
        showAnim1 = AnimationUtils.loadAnimation(context,R.animator.show1);
        hideAnim1 = AnimationUtils.loadAnimation(context,R.animator.hide1);

        mButton1 = (ImageView)view.findViewById(R.id.button1);
       weixin = (ImageView)view.findViewById(R.id.wexing);
        putong = (ImageView)view.findViewById(R.id.putong);
        luopan = (ImageView)view.findViewById(R.id.luopan);
        lukuang = (ImageView)view.findViewById(R.id.lukuang);
        dingwei = (ImageView)view.findViewById(R.id.dingwei);
        reli = (ImageView)view.findViewById(R.id.reli);
        mArcMenu = (ArcMenu)view.findViewById(R.id.arcmenu);
        arcMenu = (ArcMenu)view.findViewById(R.id.arcmenu);


        for (int i = 0; i < Imageid.length ; i++) {
           ImageView imageview = (ImageView)view.findViewById(Imageid[i]);
            imageview.setOnClickListener(listener);
            imageViewList.add(imageview);
        }

      //  startAnim();
        //卫星导航菜单
        mArcMenu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                switch (pos) {
                    case 0:
                        mBaiduMap.clear();
                        flog = true;

                        mDestLocationData = null;
                        mBaiduMap = mMapView.getMap();

                        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
                        mBaiduMap.setMapStatus(msu);
                        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        break;
                    case 2:
                        if (mLocationMode.equals(MyLocationConfiguration.LocationMode.COMPASS))
                            mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
                        else
                            mLocationMode = MyLocationConfiguration.LocationMode.COMPASS;
                        break;
                    case 3:
                        if (mBaiduMap.isTrafficEnabled())
                            mBaiduMap.setTrafficEnabled(false);
                        else
                            mBaiduMap.setTrafficEnabled(true);
                        break;
                    case 4:
                        if (mBaiduMap.isBaiduHeatMapEnabled())
                            mBaiduMap.setBaiduHeatMapEnabled(false);
                        else
                            mBaiduMap.setBaiduHeatMapEnabled(true);
                        break;
                    case 5:
                        centerToMyLocation();
                        break;
                    case 6:
                        initListener();
                        break;
                }

            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (flag == true) {

                    arcMenu.startAnimation(hideAnim1);
                    arcMenu.setVisibility(View.GONE);
                    flag = false;
                } else if (flag == false) {

                    arcMenu.startAnimation(showAnim1);
                    arcMenu.setVisibility(View.VISIBLE);
                    flag = true;
                }
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {

                return false;
            }
        });
       mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
           @Override
           public void onMapLongClick(LatLng latLng) {
               if (flog == true) {
                   addmaker(latLng);
                   flog = false;
               } else if (flog == false) {

                   maker.setPosition(latLng);
                   mBaiduMap.hideInfoWindow();
               }
               mDestLocationData = latLng;

           }
       });


        //初始化导航
        if (initDirs()) {
            initNavi();
        }

        return view;
    }

    public void search(String keyword){
            mBaiduMap.clear();
            mPoiSearch = PoiSearch.newInstance();
            poi listener = new poi();
            mPoiSearch.setOnGetPoiSearchResultListener(listener);
            mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword(keyword)
                    .sortType(PoiSortType.distance_from_near_to_far).location(new LatLng(latitude, longitude))
                    .pageNum(pageNum).radius(radius));
        }


    public class Click implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.i("进入点击事件",v.getId()+"");
            String keyword;
            switch (v.getId())
            {
                case R.id.zhoubian:
                    if(flag1)
                    startAnim();
                    else
                    closeAnim();
                    break;
                case R.id.jingdian:
                    keyword = "景点";
                    search(keyword);
                    break;
                case R.id.jiudian:
                    keyword = "酒店";
                    search(keyword);
                    break;
                case R.id.canguan:
                    keyword = "餐馆";
                    search(keyword);
                    break;
                case R.id.gouwu:
                    keyword = "娱乐场所";
                    search(keyword);
                    break;
                case R.id.wanle:
                    keyword = "KTV";
                    search(keyword);
                    break;
                default:
                    break;
            }
        }
        }




    private void startAnim()
    {
        Log.i("进入点击事件","startAnim");

        for (int i = 0; i <Imageid.length ; i++) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageViewList.get(i),"translationY",550,550-i*110);
            animator.setDuration(500);
            animator.setStartDelay(i * 300);
            animator.setInterpolator(new BounceInterpolator());
            animator.start();
        }
        flag1 = false;
    }
    private void closeAnim()
    {
        for (int i = 0; i <Imageid.length ; i++) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(imageViewList.get(i),"translationY",550-i*110,550);
            animator.setDuration(500);
            animator.setStartDelay(i * 300);
            animator.setInterpolator(new BounceInterpolator());
            animator.start();
        }
        flag1 = true;
    }

    private void addmaker(LatLng latLng)
   {

       Toast.makeText(getActivity().getApplicationContext(),"设置目的地成功",Toast.LENGTH_SHORT).show();
       options = new MarkerOptions().position(latLng)
               .icon(BitmapDescriptorFactory.fromResource(R.drawable.dingdian_03_01))
               .zIndex(5);
       maker = (Marker)(mBaiduMap.addOverlay(options));

    }
   //检索周边服务
    public class poi implements OnGetPoiSearchResultListener {
       @Override
       public void onGetPoiResult(PoiResult poiResult) {
           if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
               Toast.makeText(getActivity(), "未找到结果", Toast.LENGTH_LONG)
                       .show();
               return;
           }
           if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {

              //利用bundle传递景点信息
               List<PoiInfo> allPoi = poiResult.getAllPoi();
               String[] datas = new String[10];
               for (int i = 0; i < 10; i++) {
                   List.add(i+1+" "+allPoi.get(i).name);
                   System.out.println(allPoi.get(i).name);
               }

               bundle = new Bundle();
               bundle.putSerializable("加油站信息", (Serializable)allPoi);
               LatLng center = new LatLng(longitude,latitude);
               PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
               mBaiduMap.setOnMarkerClickListener(overlay);
               overlay.setData(poiResult);
               overlay.addToMap();
               overlay.zoomToSpan();
               BitmapDescriptor centerBitmap = BitmapDescriptorFactory
                       .fromResource(R.drawable.icon_geo);
               MarkerOptions ooMarker = new MarkerOptions().position(center).icon(centerBitmap);
               mBaiduMap.addOverlay(ooMarker);

               OverlayOptions ooCircle = new CircleOptions().fillColor( 0xCCCCCC00 )
                       .center(center).stroke(new Stroke(5, 0xFFFF00FF ))
                       .radius(radius);
               mBaiduMap.addOverlay(ooCircle);
           }
           if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD)
           {
           }
       }

       @Override
       public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
               if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
                   Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
                           .show();
               } else {
                   Toast.makeText(getActivity(), poiDetailResult.getName() + ": " + poiDetailResult.getAddress(), Toast.LENGTH_SHORT)
                           .show();
               }
       }
       @Override
       public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

       }
   }


    private class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }
        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            bundle.putDouble("log",longtitude1);
            bundle.putDouble("lan",latitude1);
            Intent intent = new Intent(getActivity(), pop.class);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
    }

    private BDLocation  bd2gc(BDLocation bdl)
    {
        return LocationClient.getBDLocationInCoorType(bdl,BDLocation.BDLOCATION_BD09LL_TO_GCJ02);
    }


    private void initListener() {
                    if(mDestLocationData == null)
                        Toast.makeText(getActivity().getApplicationContext(),"长按地图设置目的地",Toast.LENGTH_SHORT).show();
                    else if(mDestLocationData!=null)
                        routeplanToNavi();

        }



    private void routeplanToNavi() {
        BNRoutePlanNode.CoordinateType coType = BNRoutePlanNode.CoordinateType.GCJ02;
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;


        BDLocation srcl = new BDLocation();
        srcl.setLatitude(mLastLocationData.latitude);
        srcl.setLongitude(mLastLocationData.longitude);

        BDLocation dest = new BDLocation();
        dest.setLatitude(mDestLocationData.latitude);
        dest.setLongitude(mDestLocationData.longitude);

        BDLocation last1 = bd2gc(srcl);
        BDLocation dest1 = bd2gc(dest);

        sNode = new BNRoutePlanNode(last1.getLongitude(),last1.getLatitude(),"我的位置",null,coType);
        eNode = new BNRoutePlanNode(dest1.getLongitude(),dest1.getLatitude(),"终点位置",null,coType);




        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(getActivity(), list, 1,true, new DemoRoutePlanListener(sNode));
        }
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
            Intent intent = new Intent(getActivity(), BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(getActivity(),"算路失败", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    String authinfo = null;

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    showToastMsg("Handler : TTS play end");
                    break;
                }
                default :
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
//            showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
//            showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    public void showToastMsg(final String msg) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void initNavi() {

        BNOuterTTSPlayerCallback ttsCallback = null;

        BaiduNaviManager.getInstance().init(getActivity(), mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
            @Override
            public void onAuthResult(int status, String msg) {
                if (0 == status) {
                    authinfo = "key校验成功!";
                } else {
                    authinfo = "key校验失败, " + msg;
                }
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                      //  Toast.makeText(getActivity(), authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            public void initSuccess() {
               /* Toast.makeText(getActivity(), "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                initSetting();*/
            }

            public void initStart() {
                /*Toast.makeText(getActivity(), "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();*/
            }

            public void initFailed() {
               /* Toast.makeText(getActivity(), "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();*/
            }


        }, null, ttsHandler, null);

    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }



    //初始化地图
    private void initMap() {
        mBaiduMap = mMapView.getMap();
        this.context = getActivity().getApplicationContext();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
        mBaiduMap.setMapStatus(msu);

    }
    //初始化定位
    private void initLocation()
    {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
        mLocationClient = new LocationClient(context);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        // 初始化图标
        mIconLocation = BitmapDescriptorFactory
                .fromResource(R.mipmap.navi_map_gps_locked);
        myOrientationListener = new MyOrientationListener(context);

        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });

    }

    //定位方向传感器
    @Override
    public void onStart()
    {
        super.onStart();
        // 开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
        // 开启方向传感器
        myOrientationListener.start();
    }

    @Override
    public void onStop()
    {
        super.onStop();

        // 停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // 停止方向传感器
        myOrientationListener.stop();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
        @Override
        public void onResume() {
            super.onResume();
            //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
            mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    private void centerToMyLocation()
    {
        LatLng latLng = new LatLng(mLatitude, mLongtitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }

    private class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {

            MyLocationData data = new MyLocationData.Builder()//
                    .direction(mCurrentX)//
                    .accuracy(location.getRadius())//
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude())//
                    .build();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            mBaiduMap.setMyLocationData(data);
            // 设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mLocationMode, true, mIconLocation);
            mBaiduMap.setMyLocationConfigeration(config);

            // 更新经纬度
            mLatitude = location.getLatitude();
            mLongtitude = location.getLongitude();

            if (isFirstIn)
            {
                LatLng latLng = new LatLng(location.getLatitude(),
                        location.getLongitude());
                mLastLocationData = latLng;
                latitude1 = mLastLocationData.latitude;
                longtitude1 = mLastLocationData.longitude;
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirstIn = false;

               Toast.makeText(context, location.getAddrStr(),
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
}