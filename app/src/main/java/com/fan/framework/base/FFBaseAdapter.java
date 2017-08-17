package com.fan.framework.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fengnian.smallyellowo.foodie.appbase.APP;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanbiao on 16/8/16.
 */
public abstract class FFBaseAdapter<HOLDER,MODEL> extends BaseAdapter  {

    //item的layoutId
    private int layoutId;

    //数据源
    private List<MODEL> dataList;

    private LayoutInflater inflater;

    //默认的viewHolder的类类型
    private Class<? extends HOLDER> viewHolderClass;
    //上下文
    public FFContext  mcontext;



    //adapter的代理对象
    public FFBaseAdapterDelegate delegate;

    public FFBaseAdapter(Class<? extends HOLDER> viewHolderClass, int layoutId, List<MODEL> dataList, FFContext context){
        this(viewHolderClass,layoutId,context);
        if(dataList !=null) {
            this.dataList = dataList;
        }
        this.mcontext=context;
    }


    public FFBaseAdapter(Class<? extends HOLDER> viewHolderClass, int layoutId,FFContext context){
        this.layoutId = layoutId;
        this.viewHolderClass = viewHolderClass;
        inflater = LayoutInflater.from(APP.app);
        if(dataList == null) {
            dataList = new ArrayList();
        }
        this.mcontext=context;
    }


    public void setDataList(List<MODEL> dataList) {
        this.dataList = dataList;
    }

    public List<MODEL> getDataList() {
        return dataList;
    }

    public void resetDataSource(){
        if(getDataList() != null){
            getDataList().clear();
        }
    }

    public void addData(List<MODEL> dataList){
        if(dataList == null || dataList.isEmpty()){
            return;
        }

        if(getDataList() == null){
            setDataList(dataList);
        }
        else {
            getDataList().addAll(dataList);
        }
        notifyDataSetChanged();
    }


    public abstract void setViewData(Object viewHolder,int position,Object model);

    @Override
    public Object getItem(int position) {
        Object object = position < getDataList().size() ? getDataList().get(position) : null;
        if(delegate != null) {
            object = delegate.getItem(position);
        }
        return object;
    }

    @Override
    public int getCount() {
        int count = getDataList().size();
        if(delegate != null){
            count = delegate.getItemCount();
        }
        return count;
    }

    @Override
    public long getItemId(int position) {
        long itemId = position;
        if(delegate != null){
            itemId = delegate.getItemId(position);
        }
        return itemId;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 1;
        if(delegate != null){
            viewType = delegate.getItemViewType(position);
        }
        return viewType;
    }

    @Override
    public int getViewTypeCount() {
        int count = 1;
        if(delegate != null){
            count = delegate.getViewTypeCount();
        }
        return count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object viewHolder = null;
        if(convertView == null){
            Class clazz = null;
            try{
                if(delegate != null){
                    clazz = delegate.getClass(position);
                    viewHolder = clazz.newInstance();
                    int layout_Id = delegate.getItemLayoutId(position);
                    convertView = inflater.inflate(layout_Id,parent,false);
                }else{
                    viewHolder = viewHolderClass.newInstance();
                    convertView = inflater.inflate(layoutId,parent,false);
                    clazz = viewHolderClass;
                }
            } catch (InstantiationException e){
                e.printStackTrace();
            } catch (IllegalAccessException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

            Field[] t = clazz.getDeclaredFields();
            for (Field field : t) {
                Class<?> type = field.getType();
                boolean assignableFrom = View.class.isAssignableFrom(type);
                if (assignableFrom) {
                    field.setAccessible(true);
                    try {
                        field.set(viewHolder, convertView.findViewById(APP.app.getResources().getIdentifier(field.getName(), "id", APP.app.getPackageName())));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = convertView.getTag();
        }

        Object item = getItem(position);
        setViewData(viewHolder,position,item);
        click( convertView,item,position);
        return convertView;
    }

    /**
     * 点击事件处理逻辑
     */
    public  void   click(View convertView, Object modle,int position){
    }
}
