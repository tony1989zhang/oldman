package com.firstaid.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.firstaid.bean.BaseBean;
import com.firstaid.bean.NearAmbulanceBean;
import com.firstaid.bean.NearAmbulanceDataBean;
import com.firstaid.map.DrivingRouteOverlay;
import com.firstaid.map.NearAmbulanceOverlay;
import com.firstaid.map.PoiOverlay;
import com.firstaid.oldman.App;
import com.firstaid.oldman.BaseFragmentActivity;
import com.firstaid.oldman.BuildConfig;
import com.firstaid.oldman.MainActivity;
import com.firstaid.oldman.R;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.util.Util;
import com.firstaid.view.TitleView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/12/16.
 */
public class FragmentNear extends BaseFragment implements View.OnClickListener, OnGetRoutePlanResultListener, OnGetPoiSearchResultListener {

    public static final String ACTION_LOCATED = "com.firstaid.oldman.ACTION_LOCATED";
    public static boolean ISROUTEPLAN;
    TitleView mTitleView;
    MapView mMapView;
    BaiduMap mBaidumap;
    RoutePlanSearch mSearch;
    LatLng endLat = null;
    public static LatLng startLat = null;

    View map_top_root;
    View map_top_bg_line;
    View map_top_jijiuche_icon;
    ImageView mTopJijiucheIcon;
    TextView mTopJijiuCheText;
    ImageView mTopHospitalIcon;
    TextView mTopHospitalText;
    ImageView mTop120Icon;
    TextView mTop120Text;
    View mBtnCall;
    private int mCurrentType = R.id.map_top_jijiuche;

    private View mNearTop2;
    private TextView mJiJiuCheTime;
    private TextView mJiJiuCheName;
    private TextView mJiJiuCheNo;
    TextView mRouteParam;

    private View mNearBottomBtnRoot;
    private EditText editTextSearch;

    private Timer timer;
    TimerTask timerTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("onCreateView", "onCreateView");
        View root = inflater.inflate(R.layout.fragment_near, container, false);
        initViews(root);
        return root;
    }

    @Override
    public void onStart() {
        LogUtil.d("onStart", "onStart");
        super.onStart();

    }

    public void setData(){
        if (ISROUTEPLAN) {

            if (null != endLat && null != startLat) {
                setRoute(startLat, endLat);
            }
        }
    }
    private void initViews(View root) {
        mTitleView = (TitleView) root.findViewById(R.id.title_view);
        if (this.getActivity() instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) this.getActivity()).initSystemBarTint(mTitleView, Color.TRANSPARENT);
        }
        if (BuildConfig.hasProduct) {
            mTitleView.setVisibility(View.GONE);
            root.findViewById(R.id.mapsearch_id).setVisibility(View.VISIBLE);
            EditText et = (EditText) root.findViewById(R.id.editTextSearch);
            et.clearFocus(); //清除焦点
        } else {
            mTitleView.setVisibility(View.VISIBLE);
            root.findViewById(R.id.mapsearch_id).setVisibility(View.GONE);
        }
        mTitleView.setTitle(R.string.title_near);
        mMapView = (MapView) root.findViewById(R.id.bmapView);
        mBaidumap = mMapView.getMap();
        mSearch = RoutePlanSearch.newInstance();

        mSearch.setOnGetRoutePlanResultListener(listener);
        map_top_root = root.findViewById(R.id.map_top_root);
        map_top_bg_line = root.findViewById(R.id.map_top_bg_line);
        map_top_jijiuche_icon = root.findViewById(R.id.map_top_jijiuche_icon);
        root.findViewById(R.id.map_top_jijiuche).setOnClickListener(this);
        root.findViewById(R.id.map_top_hospital).setOnClickListener(this);
        root.findViewById(R.id.map_top_120).setOnClickListener(this);
        mBtnCall = root.findViewById(R.id.btn_call);
        mBtnCall.setOnClickListener(this);
        root.findViewById(R.id.btn_locate).setOnClickListener(this);
        mTopJijiucheIcon = (ImageView) root.findViewById(R.id.map_top_jijiuche_icon);
        mTopJijiuCheText = (TextView) root.findViewById(R.id.map_top_jijiuche_text);
        mTopHospitalIcon = (ImageView) root.findViewById(R.id.map_top_hospital_icon);
        mTopHospitalText = (TextView) root.findViewById(R.id.map_top_hospital_text);
        mTop120Icon = (ImageView) root.findViewById(R.id.map_top_120_icon);
        mTop120Text = (TextView) root.findViewById(R.id.map_top_120_text);
        mNearTop2 = root.findViewById(R.id.near_top2);
        mNearTop2.setOnClickListener(this);
        mJiJiuCheTime = (TextView) root.findViewById(R.id.item_time);
        mJiJiuCheName = (TextView) root.findViewById(R.id.item_name);
        mJiJiuCheNo = (TextView) root.findViewById(R.id.item_no);


        //搜搜框
        editTextSearch = (EditText) root.findViewById(R.id.editTextSearch);
        root.findViewById(R.id.tv_search).setOnClickListener(this);

        //地图时间与距离的弹现

        mRouteParam = (TextView) root.findViewById(R.id.tv_routeParam);
        mRouteParam.setVisibility(View.GONE);


        mNearBottomBtnRoot = root.findViewById(R.id.near_bottom_btn_root2);
        root.findViewById(R.id.btn_on_car).setOnClickListener(this);
        root.findViewById(R.id.btn_not_on_car).setOnClickListener(this);
        configMap();
        updateTopBgLine();
        initPoiSearch();
        updateLayoutByAlarmState(mAlarmState);
    }

    PoiSearch poiSearch;

    private void initPoiSearch() {
        poiSearch = PoiSearch.newInstance();
        // 设置检索监听器
        poiSearch.setOnGetPoiSearchResultListener(this);
    }

    private void initRoutePlanSearch() {
        mRoutePlanSearch = RoutePlanSearch.newInstance();
        mRoutePlanSearch.setOnGetRoutePlanResultListener(this);
    }

    private void updateTopBgLine() {
        int[] position = new int[2];
        map_top_root.getLocationOnScreen(position);
        int top1 = position[1];
        map_top_jijiuche_icon.getLocationOnScreen(position);
        int top2 = position[1];
        map_top_jijiuche_icon.measure(0, 0);
        int height = map_top_jijiuche_icon.getMeasuredHeight();
        int left = (App.getInstance().mScreenWidth / 3 - height) / 2;
        if (top1 == 0) {
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_MAP_TOP_LINE, 100);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) map_top_bg_line.getLayoutParams();
            params.width = App.getInstance().mScreenWidth - ((left + height / 2) * 2);
            params.leftMargin = left + height / 2;
            params.topMargin = top2 - top1 + height / 2;
            map_top_bg_line.setLayoutParams(params);
        }
    }

    private LocationClient mLocClient;
    BitmapDescriptor btmapDes;
    private MyLocationListenner mMyLocationListenner = new MyLocationListenner();

    @Override
    public void onDestroyView() {
        LogUtil.d("onDestroyView", "onDestroyView");
        if (null != timerTask) {
            timerTask.cancel();
            LogUtil.d("timerTask", "timerTask:");
            timerTask = null;
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        super.onDestroyView();
    }

    private void configMap() {
        mMapView.showZoomControls(false);
        BaiduMap baiduMap = mMapView.getMap();
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(14);
        baiduMap.animateMapStatus(u);
        baiduMap.setMyLocationEnabled(true);
        btmapDes = BitmapDescriptorFactory.fromResource(R.mipmap.icon_my_location);
        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, btmapDes));
        mMapView.getMap().setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, btmapDes));
//        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
//                MyLocationConfiguration.LocationMode.NORMAL, true, null));
        mLocClient = new LocationClient(App.getInstance().getApplicationContext());
        mLocClient.registerLocationListener(mMyLocationListenner);
        initLocation();
        mLocClient.start();
        LogUtil.d("cx", "---mLocClient.start() --");
    }

    private void initLocation() {
        final LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(false);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setAddrType("all");//返回的定位结果包含地址信息
        mLocClient.setLocOption(option);
    }

    private boolean isFirstLoc = true;

    private String mProvince = "";
    private String mCity = "";

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            LogUtil.d("cx", "-----------location  " + location.getLocType());
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                //gps定位成功
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                //网络定位成功
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                //离线定位成功，离线定位结果也是有效的
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                //服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因
                return;
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                //网络不同导致定位失败，请检查网络是否通畅
                return;
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                return;
            }
            mProvince = location.getProvince();
            mCity = location.getCity();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mMapView.getMap().setMyLocationData(locData);
            Intent intent = new Intent();
            intent.setAction(ACTION_LOCATED);
            intent.putExtra("ext_provice", mProvince);
            intent.putExtra("ext_city", mCity);
            intent.putExtra("ext_country", location.getCountry());
            mTitleView.getContext().sendBroadcast(intent);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mMapView.getMap().animateMapStatus(u);
//                mLocClient.stop();
                if (mRealFirstLoc) {
                    handlerTopItemClick(mCurrentType);
                    mRealFirstLoc = false;
                }
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    boolean mRealFirstLoc = true;

    @Override
    public void onDestroy() {
        mLocClient.stop();
        // 关闭定位图层
        mMapView.getMap().setMyLocationEnabled(false);
        mMapView.onDestroy();
        mSearch.destroy();
        mMapView = null;
        mSearch = null;

        if (null != timerTask) {
            timerTask.cancel();
            LogUtil.d("timerTask", "timerTask:");
            timerTask = null;
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        super.onDestroy();
    }

    @Override
    public void onResume() {
        LogUtil.d("onResume", "onResume");
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onStop() {

        LogUtil.d("onStop", "onStop");
        super.onStop();
    }

    @Override
    public void onPause() {


        LogUtil.d("onPause", "onPause");
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();

        super.onPause();
    }

    NearAmbulanceOverlay overlay = null;
    private static final int MSG_UPDATE_MAP_TOP_LINE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_MAP_TOP_LINE: {
                    updateTopBgLine();
                    break;

                }
                case R.id.map_top_jijiuche: {
                    String ret = (String) msg.obj;
                    {
                        LogUtil.d("cx", "latitude " + ret);
                        mNearAmbulanceBean = App.getInstance().getBeanFromJson(ret, NearAmbulanceBean.class);
                        testData(mNearAmbulanceBean);
                        if (mNearAmbulanceBean.success) {
                            if (mNearAmbulanceBean.data == null || mNearAmbulanceBean.data.size() == 0) {
                                Toast.makeText(mTitleView.getContext(), "附近没有急救车", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(mTitleView.getContext(), R.string.no_near_ambulance, Toast.LENGTH_SHORT).show();
                            } else {
                                mMapView.getMap().clear();
                                double endLatitude = mMapView.getMap().getLocationData().latitude;
                                double endLongitude = mMapView.getMap().getLocationData().longitude;
                                LogUtil.d("endLatitudeAndendLongitude", "endLatitudeAndendLongitude:" + endLatitude + ":" + endLongitude);
                                endLat = new LatLng(endLatitude, endLongitude);


                                overlay = new NearAmbulanceOverlay(mMapView.getMap());
                                mMapView.getMap().setOnMarkerClickListener(overlay);
                                overlay.setData(mNearAmbulanceBean.data);
                                LogUtil.d("jiJIuChe", "jiJIuche:" + mNearAmbulanceBean.data);
                                overlay.addToMap(R.mipmap.map_overlay_icon_jijiuche);
                                overlay = null;
                            }
                            LogUtil.d("ISROUTEPLAN", "ISROUTEPLAN:" + ISROUTEPLAN);
                            if (ISROUTEPLAN) {

                                if (null != endLat && null != startLat) {
                                    setRoute(startLat, endLat);
                                }
                            }
                        } else {
                            Toast.makeText(mTitleView.getContext(), R.string.load_data_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case R.id.map_top_hospital: {
                    String ret = (String) msg.obj;
                    LogUtil.d("cx", "near hospital " + ret);
                    mNearAmbulanceBean = App.getInstance().getBeanFromJson(ret, NearAmbulanceBean.class);
                    testData(mNearAmbulanceBean);
                    if (mNearAmbulanceBean.success) {
                        mMapView.getMap().clear();
                        if (mNearAmbulanceBean.data == null || mNearAmbulanceBean.data.size() == 0) {
                            Toast.makeText(mTitleView.getContext(), "附近没有医院", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(mTitleView.getContext(), R.string.no_near_ambulance, Toast.LENGTH_SHORT).show();

                        } else {
                            overlay = new NearAmbulanceOverlay(mMapView.getMap());
                            mMapView.getMap().setOnMarkerClickListener(overlay);
                            overlay.setData(mNearAmbulanceBean.data);

                            overlay.addToMap(R.mipmap.map_overlay_icon_hospital);
                            overlay = null;
                        }

                    } else {
                        Toast.makeText(mTitleView.getContext(), R.string.load_data_failed, Toast.LENGTH_SHORT).show();
                    }

                    break;
                }
                case R.id.map_top_120: {
                      String ret = (String) msg.obj;
                        LogUtil.d("cx", "near 120 " + ret);
                        mNearAmbulanceBean = App.getInstance().getBeanFromJson(ret, NearAmbulanceBean.class);
                        testData(mNearAmbulanceBean);
                        if (mNearAmbulanceBean.success) {
                            if (mNearAmbulanceBean.data == null || mNearAmbulanceBean.data.size() == 0) {
                                Toast.makeText(mTitleView.getContext(), "附近没有120中心", Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(mTitleView.getContext(), R.string.no_near_ambulance, Toast.LENGTH_SHORT).show();
                            } else {

                                mMapView.getMap().clear();
                                NearAmbulanceOverlay overlay = new NearAmbulanceOverlay(mMapView.getMap());
                                mMapView.getMap().setOnMarkerClickListener(overlay);
                                overlay.setData(mNearAmbulanceBean.data);
                                overlay.addToMap(R.mipmap.map_overlay_icon_jijiuche);
                            }

                        } else {
                            Toast.makeText(mTitleView.getContext(), R.string.load_data_failed, Toast.LENGTH_SHORT).show();
                        }
//                    ConnectionManager.getInstance().onKeyAlarm(mTitleView.getContext(), "64", "13666280315", new ConnectionUtil.OnDataLoadEndListener() {
//                        @Override
//                        public void OnLoadEnd(String ret) {
//                            Log.e("ret", "ret:" + ret);
//                            BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
//                            if (baseBean.success) {
//
//                            } else {
//
//                            }
//                            Toast.makeText(mTitleView.getContext(), "" + baseBean.msg, Toast.LENGTH_SHORT).show();
//                        }
//                    });
                    break;
                }
            }
        }
    };


    @Override
    public void onClick(View v) {
        mRouteParam.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.map_top_jijiuche:
            case R.id.map_top_hospital:
            case R.id.map_top_120: {
                handlerTopItemClick(v.getId());
                break;
            }
            case R.id.btn_locate: {
                isFirstLoc = true;
                configMap();
                break;
            }
            case R.id.btn_call: {
                handlerOneKeyCall();

                break;
            }
            case R.id.btn_on_car: {
                mAlarmState = ALARM_STATE_NOMAL;
                updateLayoutByAlarmState(mAlarmState);
                
                break;
            }
            case R.id.btn_not_on_car: {
                mAlarmState = ALARM_STATE_NOMAL;
                updateLayoutByAlarmState(mAlarmState);

                break;
            }
            case R.id.tv_search: {

                LogUtil.d("TAG", "TAG:" + editTextSearch.getText().toString());
                if (null == editTextSearch.getText() || "".equals(editTextSearch.getText())) {
                    Toast.makeText(getActivity(), "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    nearbySearch("" + editTextSearch.getText().toString());
                }
                break;
            }
        }
    }

    RoutePlanSearch mRoutePlanSearch = null;    // 搜索模块，也可去掉地图模块独立使用

    private void handlerOneKeyCall() {
        switch (mCurrentType) {
            case R.id.map_top_hospital:
            case R.id.map_top_120: {
            }
            case R.id.map_top_jijiuche: {

//                if (mNearAmbulanceBean == null || mNearAmbulanceBean.data == null || mNearAmbulanceBean.data.size() == 0 || mMapView.getMap().getLocationData() == null) {
                    Util.call120(mTitleView.getContext());
//                } else {
//                    MyLocationData locationData = mMapView.getMap().getLocationData();
//                    ConnectionManager.getInstance().addAlarm(mTitleView.getContext(), App.getInstance().getMoblieNo(mTitleView.getContext()),
//                            "" + System.currentTimeMillis(), App.getInstance().getCustName(mTitleView.getContext()), "报警标题", mProvince, mCity,
//                            locationData.latitude + "," + locationData.longitude, new ConnectionUtil.OnDataLoadEndListener() {
//                                @Override
//                                public void OnLoadEnd(String ret) {
//                                    BaseBean bean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
//                                    Toast.makeText(mTitleView.getContext(), bean.msg, Toast.LENGTH_SHORT).show();
//                                    LogUtil.d("cx", "------------addAlarm  " + ret);
//                                }
//                            });
                   /* NearAmbulanceDataBean nearestBean = null;
                    LatLng myLl = new LatLng(mMapView.getMap().getLocationData().latitude,
                            mMapView.getMap().getLocationData().longitude);
                    for(NearAmbulanceDataBean dataBean : mNearAmbulanceBean.data){
                        if(nearestBean == null){
                            nearestBean = dataBean;
                        } else {
                            LatLng nearestLl = new LatLng(nearestBean.abscissa,nearestBean.ordinate);
                            double nearestDistance = DistanceUtil.getDistance(myLl,nearestLl);
                            LatLng thisLl = new LatLng(dataBean.abscissa,dataBean.ordinate);
                            double thisDistance = DistanceUtil.getDistance(myLl,thisLl);
                            if(thisDistance < nearestDistance){
                                nearestBean = dataBean;
                            }
                        }
                    }
                    if(nearestBean == null){
                        Util.call120(mTitleView.getContext());
                    } else {
                        LogUtil.d("cx","nearestBean "+nearestBean.unitName);
                    }*/

//                }
                break;
            }

        }
    }

    private Request mNearBySearchRequest;
    NearAmbulanceBean mNearAmbulanceBean;

    private void handlerTopItemClick(int id) {
        mTopJijiucheIcon.setImageResource(R.mipmap.map_top_not_selected);
        mTopHospitalIcon.setImageResource(R.mipmap.map_top_not_selected);
        mTop120Icon.setImageResource(R.mipmap.map_top_not_selected);
        mTopJijiuCheText.setTextColor(0xff666666);
        mTopHospitalText.setTextColor(0xff666666);
        mTop120Text.setTextColor(0xff666666);
        mCurrentType = id;

        if (mNearBySearchRequest != null && !mNearBySearchRequest.isCanceled()) {
            mNearBySearchRequest.cancel();
        }
        switch (id) {
            case R.id.map_top_jijiuche: {
//                if(null != timerTask){
//                    timerTask.cancel();
//                    timer.cancel();
//                    LogUtil.d("timerTask", "timerTask:");
//                    timerTask = null;
//                    timer = null;
//                }
//                timer = new Timer();
//                timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
                jiJiuCheAmbulance();
//
//                    }
//                };
//                timer.schedule(timerTask, 10, 15000);
                break;
            }
            case R.id.map_top_hospital: {
                hospitalAmbulance();

                break;
            }
            case R.id.map_top_120: {
                Ambulance120();
                break;
            }
        }
    }

    private void Ambulance120() {
        mMapView.getMap().clear();
        mTop120Icon.setImageResource(R.mipmap.map_top_120);
        mTop120Text.setTextColor(mTopJijiuCheText.getResources().getColor(R.color.title_bg_color));
        if (mMapView.getMap().getLocationData() != null) {
            if(null != timerTask){
                timerTask.cancel();
                timerTask = null;
            }
            if(null != timer){
                timer.cancel();
                timer = null;
            }
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    mNearBySearchRequest = ConnectionManager.getInstance().queryNear120(mTitleView.getContext(), "" + mMapView.getMap().getLocationData().latitude
                            , "" + mMapView.getMap().getLocationData().longitude, new ConnectionUtil.OnDataLoadEndListener() {
                        @Override
                        public void OnLoadEnd(String ret) {
                            Message message = new Message();
                            message.what =R.id.map_top_120;
                            message.obj = ret;
                        }
                    });
                }
            };
            timer.schedule(timerTask,200,15000);

        } else {
            Toast.makeText(mTitleView.getContext(), R.string.can_not_search_near, Toast.LENGTH_SHORT).show();
        }
    }

    private void hospitalAmbulance() {
        mMapView.getMap().clear();
        mTopHospitalIcon.setImageResource(R.mipmap.map_top_hospital);
        mTopHospitalText.setTextColor(mTopJijiuCheText.getResources().getColor(R.color.title_bg_color));
        if (mMapView.getMap().getLocationData() != null) {
            if (null != timerTask) {
                timerTask.cancel();
                timerTask = null;
            }
            if (null != timer) {
                timer.cancel();
                timer = null;
            }
            timer = new Timer();
            timerTask = new TimerTask() {

                @Override
                public void run() {
                    mNearBySearchRequest = ConnectionManager.getInstance().queryNearHospital(mTitleView.getContext(), "" + mMapView.getMap().getLocationData().latitude
                            , "" + mMapView.getMap().getLocationData().longitude, new ConnectionUtil.OnDataLoadEndListener() {
                        @Override
                        public void OnLoadEnd(String ret) {
                            Message message = new Message();
                            message.what = R.id.map_top_hospital;
                            message.obj = ret;
                        }
                    });
                }
            };
            timer.schedule(timerTask,200,15000);

        } else {
            Toast.makeText(mTitleView.getContext(), R.string.can_not_search_near, Toast.LENGTH_SHORT).show();
        }
    }

    private void jiJiuCheAmbulance() {

        mTopJijiucheIcon.setImageResource(R.mipmap.map_top_jijiuche);
        mTopJijiuCheText.setTextColor(mTopJijiuCheText.getResources().getColor(R.color.title_bg_color));
        mMapView.getMap().clear();
        if (mMapView.getMap().getLocationData() != null) {


            if (null != timerTask) {
                timerTask.cancel();
                LogUtil.d("timerTask", "timerTask:");
                timerTask = null;
            }
            if (null != timer) {
                timer.cancel();
                timer = null;
            }

            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {

//

                    mNearBySearchRequest = ConnectionManager.getInstance().queryNearAmbulance(mTitleView.getContext(), "" + mMapView.getMap().getLocationData().latitude
                            , "" + mMapView.getMap().getLocationData().longitude, new ConnectionUtil.OnDataLoadEndListener() {
                        @Override
                        public void OnLoadEnd(String ret) {
                            Message message = new Message();
                            message.what = mCurrentType;
                            message.obj = ret;
                            mHandler.sendMessage(message);
                            //发送
                        }
                    });
                }
            };
            timer.schedule(timerTask, 100, 10000);

        } else {
            Toast.makeText(mTitleView.getContext(), R.string.can_not_search_near, Toast.LENGTH_SHORT).show();
        }
    }

    private void testData(NearAmbulanceBean bean) {
        if (!BuildConfig.addTestData) {
            return;
        }
        if (bean != null && (bean.data == null || bean.data.size() == 0)) {
            double baseLat = mMapView.getMap().getLocationData().latitude;
            double baseLon = mMapView.getMap().getLocationData().longitude;
            bean.data = new ArrayList<NearAmbulanceDataBean>();
            for (int i = 0; i < 10; i++) {
                NearAmbulanceDataBean item = new NearAmbulanceDataBean();
                item.unitName = "机构" + i;
                item.idCardNo = "京A11223" + i;
                item.bindingPhone = "15859254561";
                item.latitude = baseLat + i * 0.01;
                item.longitude = baseLon + i * 0.01;
                bean.data.add(item);
            }
        }
    }

    private void nearbySearch(String keyword) {
        LogUtil.d("keyword", "keyword:" + keyword);
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.location(new LatLng(mMapView.getMap().getLocationData().latitude, mMapView.getMap().getLocationData().longitude));
        nearbySearchOption.keyword(keyword);
        nearbySearchOption.radius(1000 * 10);// 检索半径，单位是米
        nearbySearchOption.pageNum(0);
        poiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求
    }


    @Override
    public void onGetPoiResult(PoiResult result) {
        LogUtil.d("OnGetPoiResult", "OnGetPoiResult；" + result);
        if (editTextSearch != null)
            editTextSearch.setText("");

        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(mTitleView.getContext(), "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mMapView.getMap().clear();

            PoiOverlay overlay = new MyPoiOverlay(mMapView.getMap());
            mMapView.getMap().setOnMarkerClickListener(overlay);
            overlay.setData(result);
            switch (mCurrentType) {
                case R.id.map_top_jijiuche: {
                    overlay.addToMap(R.mipmap.map_overlay_icon_jijiuche);
                    break;
                }
                case R.id.map_top_hospital: {
                    overlay.addToMap(R.mipmap.map_overlay_icon_hospital);
                    break;
                }
                case R.id.map_top_120: {
                    overlay.addToMap(R.mipmap.map_overlay_icon_120);
                    break;
                }

            }

            overlay.zoomToSpan();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(mTitleView.getContext(), strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mTitleView.getContext(), "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(mTitleView.getContext(), poiDetailResult.getName() + ": " + poiDetailResult.getAddress() + "  " + poiDetailResult.getTelephone(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            poiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    ProgressDialog mLoadingDialog;

    protected void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialog(mTitleView.getContext());
            mLoadingDialog.setCancelable(false);
        }
        mLoadingDialog.show();
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }


    private int mAlarmState = ALARM_STATE_NOMAL;
    private static final int ALARM_STATE_NOMAL = 1;
    private static final int ALARM_STATE_ALRM_RECEIVED = 2;

    private void updateLayoutByAlarmState(int state) {
        switch (state) {
            case ALARM_STATE_NOMAL: {
                mNearTop2.setVisibility(View.GONE);
                mNearBottomBtnRoot.setVisibility(View.GONE);
                mBtnCall.setVisibility(View.VISIBLE);
                break;
            }
            case ALARM_STATE_ALRM_RECEIVED: {
                mNearTop2.setVisibility(View.VISIBLE);
                mNearBottomBtnRoot.setVisibility(View.VISIBLE);
                mBtnCall.setVisibility(View.INVISIBLE);
                break;
            }
        }
    }

    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    RouteLine route = null;

    public void setRoute(LatLng start, LatLng end) {
        LogUtil.d("setRoute", "setRoute");
        PlanNode stNode = PlanNode.withLocation(start);
        PlanNode enNode = PlanNode.withLocation(end);
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_en);
        }
    }


    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
            LogUtil.d("onGetWalkingRouteResult", "onGetWalkingRouteResult:" + result);
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult result) {
            LogUtil.d("onGetTransitRouteResult", "onGetTransitRouteResult:" + result);
        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult result) {
            //获取驾车线路规划结果
            LogUtil.d("onGetDrivingRouteResult", "DrivingRouteResult:" + result);
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                nodeIndex = -1;
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
//                    routeOverlay = overlay;
                int distance = route.getDistance();
                int duration = route.getDuration();
//                Toast.makeText(getActivity(),"距离:" + distance + "时间:" + String.valueOf(duration / 60) +"."+ String.valueOf(duration % 60),Toast.LENGTH_SHORT).show();
                mRouteParam.setVisibility(View.VISIBLE);
                mRouteParam.setText("急救车距离您:" + distance + "米," + "估计用时:" + String.valueOf(duration / 60) + "." + String.valueOf(duration % 60) + "分");
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
                mAlarmState = ALARM_STATE_ALRM_RECEIVED;
                updateLayoutByAlarmState(mAlarmState);
            }

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult result) {
            //获取驾车线路规划结果
            LogUtil.d("onGetBikingRouteResult", "onGetBikingRouteResult:" + result);
        }
    };
}


