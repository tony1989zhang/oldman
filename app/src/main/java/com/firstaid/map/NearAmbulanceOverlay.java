package com.firstaid.map;

import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.model.LatLng;
import com.firstaid.bean.NearAmbulanceDataBean;
import com.firstaid.oldman.App;
import com.firstaid.util.ConnectionManager;
import com.firstaid.util.ConnectionUtil;
import com.firstaid.util.LogUtil;
import com.firstaid.view.NearHospitalDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于显示poi的overly
 */
public class NearAmbulanceOverlay extends OverlayManager {

    private static final int MAX_POI_SIZE = 100;

    private List<NearAmbulanceDataBean> mNearAmbulances = null;

    @Override
    public List<OverlayOptions> getOverlayOptions() {
        return null;
    }

    /**
     * 构造函数
     *
     * @param baiduMap
     *            该 PoiOverlay 引用的 BaiduMap 对象
     */
    public NearAmbulanceOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    /**
     * 设置附近救护车数据
     * 
     * @param result
     *            设置POI数据
     */
    public void setData(List<NearAmbulanceDataBean> result) {
        this.mNearAmbulances = result;
    }
    BitmapDescriptor bitmapDescriptor = null;
    @Override
    public final List<OverlayOptions> getOverlayOptions(int iconId) {
        if(null == bitmapDescriptor) {
         bitmapDescriptor = BitmapDescriptorFactory.fromResource(iconId);
        }else{
            LogUtil.d(" bitmapDescriptor","  bitmapDescriptor");

        }

        if (mNearAmbulances == null || mNearAmbulances.size() == 0) {
            return null;
        }
        List<OverlayOptions> markerList = new ArrayList<OverlayOptions>();
        int markerSize = 0;
        for(NearAmbulanceDataBean bean : mNearAmbulances){
            Bundle bundle = new Bundle();
            bundle.putSerializable("item", bean);
            markerList.add(new MarkerOptions()
                    .icon(bitmapDescriptor).extraInfo(bundle)
                    .position(new LatLng(bean.latitude, bean.longitude)));
        }
//        if(bitmapDescriptor != null){
//            bitmapDescriptor.recycle();
//            LogUtil.d("        bitmapDescriptor.recycle();", "        bitmapDescriptor.recycle();");
//        }
        return markerList;
    }

    /**
     * 获取该 附近救护车信息
     * 
     * @return
     */
    public List<NearAmbulanceDataBean> getPoiResult() {
        return mNearAmbulances;
    }


    @Override
    public final boolean onMarkerClick(Marker marker) {
        if (!mOverlayList.contains(marker)) {
            return false;
        }
        if (marker.getExtraInfo() != null) {
            NearAmbulanceDataBean item = (NearAmbulanceDataBean)marker.getExtraInfo().getSerializable("item");
            NearHospitalDetailView view = new NearHospitalDetailView(App.getInstance().getApplicationContext(),item);
            LatLng ll = marker.getPosition();
            InfoWindow infoWindow = new InfoWindow(view, ll, -50);

            view.setName(item.unitName);
            view.setAddr(item.idCardNo);
            view.setPhone(item.bindingPhone);
            view.setIconUrl("");
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(ll));
            mBaiduMap.showInfoWindow(infoWindow);
            return true;
    }
        return false;
    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {
        // TODO Auto-generated method stub
        return false;
    }
}
