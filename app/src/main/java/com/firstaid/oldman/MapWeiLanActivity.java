package com.firstaid.oldman;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.firstaid.service.HeadSetService;
import com.firstaid.util.LogUtil;
import com.firstaid.util.SPUtil;
import com.firstaid.view.TitleView;

/**
 * Created by Administrator on 2016/1/13.
 */
public class MapWeiLanActivity extends BaseActivity {

    public static final int RADIUS = 1500;
    private TitleView mTitleView;
    private MapView mMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_weilan);
        mTitleView = (TitleView) findViewById(R.id.title_view);
        initSystemBarTint(mTitleView, Color.TRANSPARENT);
        mTitleView.setTitleBackVisibility(View.VISIBLE);
        mTitleView.setTitleBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleView.setTitle(R.string.map_weilan_title);
        mMapView = (MapView) findViewById(R.id.bmapView);
        configMap();

    }

    private LocationClient mLocClient;
    BitmapDescriptor btmapDes;
    private MyLocationListenner mMyLocationListenner = new MyLocationListenner();
    private void configMap(){
        mMapView.showZoomControls(false);
        BaiduMap baiduMap = mMapView.getMap();
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(14);
        baiduMap.animateMapStatus(u);
        baiduMap.setMyLocationEnabled(true);
        btmapDes = BitmapDescriptorFactory.fromResource(R.mipmap.icon_my_location2);
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

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            LogUtil.d("cx","-----------location  "+location.getLocType());
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                //gps定位成功
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
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

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mMapView.getMap().setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mMapView.getMap().animateMapStatus(u);
                mLocClient.stop();
                double latiCenter = Double.parseDouble(SPUtil.getInstant(MapWeiLanActivity.this).get("weilan_center_lat","0").toString());
                double longCenter = Double.parseDouble(SPUtil.getInstant(MapWeiLanActivity.this).get("weilan_center_long", "0").toString());
                if(latiCenter == 0 || longCenter == 0){
                    //not seted
                    LatLng llCenter = new LatLng(location.getLatitude()+0.003,location.getLongitude()+0.003);
                    addWeiLan(llCenter);
                    saveWeiLanCenter(llCenter);
                } else {
                    addWeiLan(new LatLng(latiCenter,longCenter));
                }

            }
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }
    private void addWeiLan(LatLng point){
        mMapView.getMap().clear();
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.weilan_center);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions options = new MarkerOptions()
                .position(point)  //设置marker的位置
                .icon(bitmap)  //设置marker图标
                .draggable(true);  //设置手势拖拽
        //将marker添加到地图上
        Marker marker = (Marker)(mMapView.getMap().addOverlay(options));

        OverlayOptions ooCircle = new CircleOptions().fillColor(0x803BCF67)
                .center(point).stroke(new Stroke(5, 0xAA3BCF67))
                .radius(RADIUS);
        mMapView.getMap().addOverlay(ooCircle);
        mMapView.getMap().setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                addWeiLan(marker.getPosition());
                saveWeiLanCenter(marker.getPosition());
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
            }
        });
    }
    private void saveWeiLanCenter(LatLng position){
        SPUtil.getInstant(this).save("weilan_center_lat", "" + position.latitude);
        SPUtil.getInstant(this).save("weilan_center_long", "" + position.longitude);
        Intent intent = new Intent();
        intent.setClass(this, HeadSetService.class);
        intent.putExtra("ext_cmd","weichan_changed");
        this.startService(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLocClient != null && mLocClient.isStarted()){
            mLocClient.stop();
        }
    }
}
