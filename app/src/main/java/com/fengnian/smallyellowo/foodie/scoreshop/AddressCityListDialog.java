package com.fengnian.smallyellowo.foodie.scoreshop;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.widgets.wheelview.ArrayWheelAdapter;
import com.fengnian.smallyellowo.foodie.widgets.wheelview.CityModel;
import com.fengnian.smallyellowo.foodie.widgets.wheelview.DistrictModel;
import com.fengnian.smallyellowo.foodie.widgets.wheelview.OnWheelChangedListener;
import com.fengnian.smallyellowo.foodie.widgets.wheelview.ProvinceModel;
import com.fengnian.smallyellowo.foodie.widgets.wheelview.WheelView;
import com.fengnian.smallyellowo.foodie.widgets.wheelview.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by chenglin on 2017-5-5.
 */

public class AddressCityListDialog extends Dialog implements OnWheelChangedListener {
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private OnFinishListener mFinishListener;

    protected String[] mProvinceDatas;//所有省
    protected String mCurrentProviceName;//当前省的名称
    protected String mCurrentCityName;//当前市的名称
    protected String mCurrentDistrictName = "";//当前区的名称
    protected String mCurrentZipCode = "";//当前区的邮政编码
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();//key - 省 value - 市
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();//key - 市 values - 区
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();//key - 区 values - 邮编

    public AddressCityListDialog(Context context) {
        this(context, R.style.dialog);
    }

    public AddressCityListDialog(Context context, OnFinishListener listener) {
        this(context, R.style.dialog);
        mFinishListener = listener;
    }

    public AddressCityListDialog(Context mcontext, int themeResId) {
        super(mcontext, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(R.style.dialog_theme_animation_bottom);
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.BOTTOM;
        param.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(param);

        setContentView(R.layout.address_city_list_layout);
        setCanceledOnTouchOutside(true);

        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        setUpListener();
        setUpData();

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.tv_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mFinishListener != null) {
                    SYAddressModel syAddressModel = new SYAddressModel();
                    syAddressModel.province = mCurrentProviceName;
                    syAddressModel.city = mCurrentCityName;
                    syAddressModel.region = mCurrentDistrictName;
                    mFinishListener.onFinish(syAddressModel);
                }
            }
        });
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(getContext(), mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(getContext(), areas));
        mViewDistrict.setCurrentItem(0);
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(getContext(), cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }


    /**
     * 解析省市区的XML数据
     */
    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getContext().getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

}
