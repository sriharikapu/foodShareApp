package com.fengnian.smallyellowo.foodie.bean.publics;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.clusterutil.clustering.ClusterItem;

/**
 * Created by aaron on 2016/10/17.
 */

public class DotInfo implements ClusterItem {

//    public DotInfo(LatLng latLng) {
//        super(latLng);
//    }

    public String getDotId() {
        return dotId;
    }

    public void setDotId(String dotId) {
        this.dotId = dotId;
    }

    private String dotId;

    private  SYPoi poi;

    private String xgd;
    private  String ptype;

    private  float  starLevel;

    BitmapDescriptor bm=BitmapDescriptorFactory
            .fromResource(R.mipmap.dange_shouhu_zuobiao);

    public float getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(float starLevel) {
        this.starLevel = starLevel;
    }

    public SYPoi getPoi() {
        return poi;
    }

    public void setPoi(SYPoi poi) {
        this.poi = poi;
    }

    public String getXgd() {
        return xgd;
    }

    public void setXgd(String xgd) {
        this.xgd = xgd;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(poi.getLatitude(),poi.getLongitude());
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {

//        if(DeliciousFoodMapActivity.SetMarkIconStytle==1){
//            return BitmapDescriptorFactory
//                    .fromResource(R.mipmap.dange_shouhu_zuobiao);
//        }else if(DeliciousFoodMapActivity.SetMarkIconStytle==2){
//            return BitmapDescriptorFactory
//                    .fromResource(R.mipmap.jingmao_number_one);
//        }
         return bm;



       /* return BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);*/
    }

    public  void  setBitmapDescriptor(int flag){
        if(flag==1){
           bm= BitmapDescriptorFactory
                    .fromResource(R.mipmap.dange_shouhu_zuobiao);
        }else if(flag==2){
            bm= BitmapDescriptorFactory
                    .fromResource(R.mipmap.jingmao_number_one);
        }
        bm= BitmapDescriptorFactory
                .fromResource(R.mipmap.dange_shouhu_zuobiao);
    }



    private  int shareNum;
    private   int price ;

    private SYImage  img;

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public SYImage getImg() {
        return img;
    }

    public void setImg(SYImage img) {
        this.img = img;
    }
}
