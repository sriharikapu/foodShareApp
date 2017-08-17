package com.fan.framework.base;

/**
 * Created by lanbiao on 16/8/16.
 */
public interface FFBaseAdapterDelegate {

    /**
     * 判断该position是否存在对应的数据
     * @param position 列表对应的position
     * @return 如存在数据position对应的item，可能存在null
     */
    Object getItem(int position);

    /**
     * 获取总的item的个数
     * @return 返回除数据源对应的view以外的view的个数
     */
    int getItemCount();

    /**
     * 获取item的type个数
     * @return 返回viewType的个数
     */
    int getViewTypeCount();

    /**
     * 获取position对应viewType
     * @param position item的position
     * @return viewType类型
     */
    int getItemViewType(int position);

    /**
     * 获取item对应的id
     * @param position item的position
     * @return itemId返回值
     */
    long getItemId(int position);

    /**
     * 获取position对应的viewHolder的class
     * @param position item的position
     * @return 返回对应的holder的class信息
     */
    Class getClass(int position);

    /**
     * 获得item的对应的layoutId
     * @param position item对应的position
     * @return  position对应的layoutId
     */
    int getItemLayoutId(int position);
}
