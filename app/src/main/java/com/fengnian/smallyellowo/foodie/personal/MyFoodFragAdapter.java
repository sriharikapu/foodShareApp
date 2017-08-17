package com.fengnian.smallyellowo.foodie.personal;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fan.framework.xtaskmanager.XTaskManager;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.RecyclerLoadMoreFooterView;
import com.fengnian.smallyellowo.foodie.View.RotationLoadingView;
import com.fengnian.smallyellowo.foodie.bean.PersionSYfeedC;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.FastDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.FastDetailIntent;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.CustomRatingBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fan.framework.xtaskmanager.xtask.XTask.XTaskExecutStateExecing;

/**
 * Created by chenglin on 2017-3-23.
 */

public class MyFoodFragAdapter extends RecyclerView.Adapter<MyFoodViewHolder> {
    private MyFoodListFragment mFragment;
    private List<DeliciousFoodModel.FoodListModel> mAllList = new ArrayList<>();
    //当二次编辑某条网络数据时--状态为正在发布--或者状态为发布失败-那么这个Map存的就是它对应的网络数据
    private Map<String, DeliciousFoodModel.FoodListModel> mNetUnsavedMap = new HashMap<>();
    private int mLoadState;
    public boolean isFilterEmpty = false;

    public MyFoodFragAdapter(MyFoodListFragment fragment) {
        mFragment = fragment;
    }

    public void setLoadState(int loadState) {
        mLoadState = loadState;
        this.notifyDataSetChanged();
    }

    public void setDataList(List<DeliciousFoodModel.SYSearchUserFoodModel> list) {
        //显示空页面
        if (list == null) {
            mAllList.clear();
            DeliciousFoodModel.FoodListModel emptyFoodListModel = new DeliciousFoodModel.FoodListModel();
            emptyFoodListModel.type = DeliciousFoodModel.EMPTY;
            mAllList.add(emptyFoodListModel);
            this.notifyDataSetChanged();
            return;
        }

        //初始化网络数据
        mAllList.clear();
        appendDataList(list);
    }

    public void appendDataList(List<DeliciousFoodModel.SYSearchUserFoodModel> list) {
        if (list == null) {
            return;
        }
        mAllList.addAll(buildInsertNativeData(list));
        this.notifyDataSetChanged();
    }

    public List<DeliciousFoodModel.FoodListModel> getDataList() {
        return mAllList;
    }

    public void removeItem(DeliciousFoodModel.FoodListModel item) {
        if (mAllList.size() > 0 && mAllList.contains(item)) {
            mAllList.remove(item);
            this.notifyDataSetChanged();
        }
    }

    /**
     * 重新构建列表所有的数据：把本地数据重新取出，和已经展示的网络数据放在一次，重新构建
     */
    public void rebuildAllList() {
        if (mAllList.size() <= 0) {
            return;
        }

        //取出列表中的网络数据
        List<DeliciousFoodModel.FoodListModel> netList = new ArrayList<>();
        for (DeliciousFoodModel.FoodListModel model : mAllList) {
            if (model.type == DeliciousFoodModel.MODEL_NET) {
                netList.add(model);
            }
        }

        //重新setDataList
        List<DeliciousFoodModel.SYSearchUserFoodModel> tempNetList = new ArrayList<>();
        for (DeliciousFoodModel.FoodListModel netModel : netList) {
            DeliciousFoodModel.SYSearchUserFoodModel netFoodModel = netModel.sYSearchUserFoodModel;
            if (!mNetUnsavedMap.containsKey(netFoodModel.foodNoteId)) {
                tempNetList.add(netFoodModel);
            }
        }
        setDataList(tempNetList);
    }


    /**
     * 网络数据加载完毕后，把剩下的本地数据都追加到尾端
     */
    public void addRestNativeData() {
        List<PublishModel> taskList = SYDataManager.shareDataManager().allTasks();
        if (taskList == null || taskList.size() <= 0) {
            return;
        }
        for (int i = 0; i < taskList.size(); i++) {
            SYFeed feed = taskList.get(i).getFeed();
            if (!isContainNativeItem(feed)) {
                addNativeItem(mAllList, feed);
            }
        }
        this.notifyDataSetChanged();
    }

    /**
     * 按照传入的网络数据的时间线，插入本地数据
     */
    private List<DeliciousFoodModel.FoodListModel> buildInsertNativeData(final List<DeliciousFoodModel.SYSearchUserFoodModel> netList) {
        List<DeliciousFoodModel.FoodListModel> tempList = new ArrayList<>();
        long[] publishTimeArray = new long[netList.size()];
        List<PublishModel> taskList = SYDataManager.shareDataManager().allTasks();

        //处理网络数据
        for (int i = 0; i < netList.size(); i++) {
            DeliciousFoodModel.FoodListModel foodListModel = new DeliciousFoodModel.FoodListModel();
            foodListModel.type = DeliciousFoodModel.MODEL_NET;
            foodListModel.sYSearchUserFoodModel = netList.get(i);

            //构建 mNetUnsavedMap
            for (PublishModel publishModel : taskList) {
                SYFeed sYFeed = publishModel.getFeed();
                if (foodListModel.sYSearchUserFoodModel != null
                        && !TextUtils.isEmpty(foodListModel.sYSearchUserFoodModel.foodNoteId)
                        && foodListModel.sYSearchUserFoodModel.foodNoteId.equals(sYFeed.getFoodNoteId())) {
                    mNetUnsavedMap.put(foodListModel.sYSearchUserFoodModel.foodNoteId, foodListModel);
                    break;
                }
            }
            if (!mNetUnsavedMap.containsKey(foodListModel.sYSearchUserFoodModel.foodNoteId)) {
                tempList.add(foodListModel);
            }

            //构建网络数据的发布时间数组
            String pushTime = netList.get(i).pushTime;
            if (!TextUtils.isEmpty(pushTime) && TextUtils.isDigitsOnly(pushTime)) {
                publishTimeArray[i] = Long.parseLong(pushTime + "000");
            } else {
                publishTimeArray[i] = System.currentTimeMillis();
            }
        }

        //网络数据里面时间最小的那条
        long minPublishTime = FFUtils.getMinNum(publishTimeArray);

        //取本地数据和网络数据最小的那条对比
        if (taskList.size() > 0) {
            for (int i = 0; i < taskList.size(); i++) {
                SYFeed feed = taskList.get(i).getFeed();
                if (!isContainNativeItem(feed) && feed.getTimeStamp() > minPublishTime) {
                    addNativeItem(tempList, feed);
                }
            }
        }

        //再全部按照时间排序
        sortListByPublishTime(tempList);

        return tempList;
    }

    /**
     * 按照发布时间排序
     */
    private void sortListByPublishTime(List<DeliciousFoodModel.FoodListModel> tempList) {
        final Comparator<DeliciousFoodModel.FoodListModel> comparatorID = new Comparator<DeliciousFoodModel.FoodListModel>() {
            @Override
            public int compare(DeliciousFoodModel.FoodListModel item_1, DeliciousFoodModel.FoodListModel item_2) {
                if (item_1.getPublishTime() != item_2.getPublishTime()) {
                    return (int) (item_2.getPublishTime() - item_1.getPublishTime());
                } else {
                    return (int) (item_1.getPublishTime() - item_2.getPublishTime());
                }
            }
        };
        Collections.sort(tempList, comparatorID);
    }

    //by传入的foodNoteId移除item
    public DeliciousFoodModel.FoodListModel removeItem(String foodNoteId) {
        DeliciousFoodModel.FoodListModel foodListModel = null;
        if (TextUtils.isEmpty(foodNoteId)) {
            return foodListModel;
        }
        if (mAllList.size() > 0) {
            for (DeliciousFoodModel.FoodListModel item : mAllList) {
                if (item.type == DeliciousFoodModel.MODEL_NET && foodNoteId.equals(item.sYSearchUserFoodModel.foodNoteId)) {
                    mAllList.remove(item);
                    foodListModel = item;
                    this.notifyDataSetChanged();
                    break;
                } else if (item.type == DeliciousFoodModel.MODEL_NATIVE && foodNoteId.equals(item.nativeRichTextFood.getFoodNoteId())) {
                    mAllList.remove(item);
                    foodListModel = item;
                    this.notifyDataSetChanged();
                    break;
                }
            }
        }
        return foodListModel;
    }

    //所有的数据中是否已经包含了传入的这条本地数据
    private boolean isContainNativeItem(SYFeed nativeRichTextFood) {
        if (nativeRichTextFood == null) {
            return false;
        }
        if (mAllList.size() > 0) {
            for (DeliciousFoodModel.FoodListModel model : mAllList) {
                if (model.type == DeliciousFoodModel.MODEL_NATIVE) {
                    if (model.nativeRichTextFood == nativeRichTextFood) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 移除二次编辑的本地数据item，如果有网络数据，那么就恢复网络数据
     */
    public void removeReEditNativeItem(String foodNoteId) {
        if (mNetUnsavedMap.containsKey(foodNoteId)) {
            mAllList.add(mNetUnsavedMap.get(foodNoteId));
            mNetUnsavedMap.remove(foodNoteId);
            sortListByPublishTime(mAllList);
            notifyDataSetChanged();
        }
    }

    /**
     * 当本地数据发送成功时，移除mNetUnsavedMap中的数据
     */
    public void removeUnsavedMapWhenPublishSuccess(PublishModel task, SYFeed feed) {
        if (feed != null && !TextUtils.isEmpty(feed.getFoodNoteId())) {
            if (mNetUnsavedMap.containsKey(feed.getFoodNoteId())) {
                mNetUnsavedMap.remove(feed.getFoodNoteId());
            }
        }
    }

    @Override
    public MyFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mFragment.getActivity(), R.layout.user_delicious_food_item, null);

        MyFoodViewHolder holder = new MyFoodViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyFoodViewHolder holder, int position) {
        DeliciousFoodModel.FoodListModel item = mAllList.get(position);
        if (item == null) {
            return;
        }
        if (getItemCount() == 1 && item.type == DeliciousFoodModel.EMPTY) {
            setEmptyView(true, holder, item);
            return;
        } else {
            setEmptyView(false, holder, item);
        }

        setOnItemClickListener(holder, item);
        setFoodImage(holder, item);
        setLoadMoreState(holder, item, position);
        setTitle(holder, item);
        setRatingBar(holder, item);
        setBusinessName(holder, item);
        setLocation(holder, item);
        setTime(holder, item);
        setPictureCount(holder, item);
        setCameraOrRich(holder, item);
        setShareState(holder, item);
        setPublishState(holder, item);
    }

    private void setEmptyView(boolean isEmpty, MyFoodViewHolder holder, final DeliciousFoodModel.FoodListModel item) {
        if (holder.emptyImageView != null) {
            holder.convertView.removeView(holder.emptyImageView);
            holder.emptyImageView = null;
        }

        if (isEmpty) {
            holder.item_root.setVisibility(View.GONE);
            holder.line_bottom.setVisibility(View.GONE);
            holder.footerView.setVisibility(View.GONE);

            holder.emptyImageView = new ImageView(mFragment.getActivity());
            LinearLayout.LayoutParams emptyLayoutParams = null;
            if (isFilterEmpty) {
                holder.emptyImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                holder.emptyImageView.setImageResource(R.drawable.food_list_filter_empty);
                int height = DisplayUtil.screenHeight
                        - FFUtils.getStatusbarHight(mFragment.getActivity())
                        - mFragment.getResources().getDimensionPixelSize(R.dimen.profile_bg_height)
                        - mFragment.getResources().getDimensionPixelSize(R.dimen.user_tab_height)
                        - mFragment.getResources().getDimensionPixelSize(R.dimen.user_tab_filter_item_height)
                        - DisplayUtil.dip2px(58f);
                emptyLayoutParams = new LinearLayout.LayoutParams(DisplayUtil.screenWidth, height);
            } else {
                holder.emptyImageView.setImageResource(R.drawable.profile_empty_item);
                emptyLayoutParams = new LinearLayout.LayoutParams(-1, -2);
            }
            holder.convertView.addView(holder.emptyImageView, 0, emptyLayoutParams);
        } else {
            holder.item_root.setVisibility(View.VISIBLE);
            holder.line_bottom.setVisibility(View.VISIBLE);
            holder.footerView.setVisibility(View.VISIBLE);
        }
    }

    private void setOnItemClickListener(MyFoodViewHolder holder, final DeliciousFoodModel.FoodListModel item) {
        if (item == null) {
            return;
        }
        holder.item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FFUtils.checkNet()) {
                    mFragment.showToast(mFragment.getString(R.string.lsq_network_connection_interruption));
                    return;
                }

                if (item.type == DeliciousFoodModel.MODEL_NET) {
                    DeliciousFoodModel.SYSearchUserFoodModel sySearchUserFoodModel = item.sYSearchUserFoodModel;
                    if (sySearchUserFoodModel == null) {
                        return;
                    }
                    boolean isMeishiBianji = !sySearchUserFoodModel.isShorthandFood;//富文本美食编辑
                    if (isMeishiBianji) {
                        DynamicDetailIntent intent = new DynamicDetailIntent(sySearchUserFoodModel.foodNoteId);
                        intent.setMineMode(true);
                        mFragment.startActivity(DynamicDetailActivity.class, intent);
                    } else {//速记
                        FastDetailIntent intent = new FastDetailIntent(sySearchUserFoodModel.foodNoteId);
                        intent.setMineMode(true);
                        mFragment.startActivity(FastDetailActivity.class, intent);
                    }
                } else if (item.type == DeliciousFoodModel.MODEL_NATIVE) {
                    SYFeed syFeed = item.nativeRichTextFood;
                    if (syFeed == null) {
                        return;
                    }
                    NativeRichTextFood nativeRichTextFood = (NativeRichTextFood) syFeed.getFood();
                    if (nativeRichTextFood == null || nativeRichTextFood.getTask() == null) {
                        return;
                    }

                    if (syFeed.getFood().wasMeishiBianji()) {//富文本美食编辑
                        if (nativeRichTextFood.getTask().taskExecutState == XTaskExecutStateExecing) {
                            mFragment.showToast(mFragment.getString(R.string.food_publishing_disable_edit));
                            return;
                        } else {
                            DynamicDetailIntent intent = new DynamicDetailIntent(syFeed, true, false);
                            intent.setRequestCode(123);
                            mFragment.startActivity(DynamicDetailActivity.class, intent);
                        }

                    } else {//速记
                        if (nativeRichTextFood.getTask().taskExecutState == XTaskExecutStateExecing) {
                            mFragment.showToast(mFragment.getString(R.string.food_publishing_disable_edit));
                            return;
                        } else {
                            FastDetailIntent intent = new FastDetailIntent(syFeed, true);
                            intent.setRequestCode(123);
                            mFragment.startActivity(FastDetailActivity.class, intent);
                        }
                    }
                }
            }
        });


        holder.item_root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (FFUtils.checkNet()) {
                    mFragment.mHelper.getPopWindow(item);
                } else {
                    mFragment.showToast("您的网络不好哦!");
                }
                return true;
            }
        });
    }

    private void setFoodImage(MyFoodViewHolder holder, DeliciousFoodModel.FoodListModel item) {
        holder.image.setImageResource(0);
        if (item != null) {
            if (item.type == DeliciousFoodModel.MODEL_NET) {
                if (!TextUtils.isEmpty(item.sYSearchUserFoodModel.foodImage)) {
                    FFImageLoader.loadSmallImage((FFContext) mFragment.getActivity(), item.sYSearchUserFoodModel.foodImage, holder.image);
                }
            } else if (item.type == DeliciousFoodModel.MODEL_NATIVE) {
                SYFeed syFeed = item.nativeRichTextFood;
                FFImageLoader.loadSmallImage((FFContext) mFragment.getActivity(), syFeed.getFood().pullCoverImage(), holder.image);
            }
        }
    }

    private void setLoadMoreState(MyFoodViewHolder holder, final DeliciousFoodModel.FoodListModel item, int position) {
        if (position == getItemCount() - 1) {
            holder.footerView.setVisibility(View.VISIBLE);
            if (mLoadState == RecyclerLoadMoreFooterView.LOADING) {
                holder.footerView.setLoading();
            } else {
                holder.footerView.setLoadFinish();
            }
        } else {
            holder.footerView.setVisibility(View.GONE);
        }

    }


    private void setTitle(MyFoodViewHolder holder, DeliciousFoodModel.FoodListModel item) {
        holder.tv_title.setText("");
        if (item != null) {
            if (item.type == DeliciousFoodModel.MODEL_NET) {
                if (!TextUtils.isEmpty(item.sYSearchUserFoodModel.foodTitle)) {
                    holder.tv_title.setText(item.sYSearchUserFoodModel.foodTitle);
                }
            } else if (item.type == DeliciousFoodModel.MODEL_NATIVE) {
                SYFeed syFeed = item.nativeRichTextFood;
                if (syFeed.getFood() != null) {
                    holder.tv_title.setText(syFeed.getFood().getTitle());
                }
            }
        }
    }

    private void setRatingBar(MyFoodViewHolder holder, DeliciousFoodModel.FoodListModel item) {
        holder.rating_bar.setVisibility(View.INVISIBLE);
        //设置ratingBar高度
        int ratingBarHeight = mFragment.getResources().getDrawable(R.drawable.rating_detail_light).getIntrinsicHeight();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.rating_bar.getLayoutParams();
        params.height = ratingBarHeight;
        holder.rating_bar.setLayoutParams(params);

        if (item != null) {
            if (item.type == DeliciousFoodModel.MODEL_NET) {
                if (item.sYSearchUserFoodModel.starLevel > 0) {
                    holder.rating_bar.setRating((float) item.sYSearchUserFoodModel.starLevel);
                    holder.rating_bar.setVisibility(View.VISIBLE);
                }
            } else if (item.type == DeliciousFoodModel.MODEL_NATIVE) {
                SYFeed syFeed = item.nativeRichTextFood;
                if (syFeed.getStarLevel() > 0) {
                    holder.rating_bar.setRating(syFeed.getStarLevel());
                    holder.rating_bar.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setBusinessName(MyFoodViewHolder holder, DeliciousFoodModel.FoodListModel item) {
        holder.tv_business_name.setText("");
        final String str = "无商户";
        if (item != null) {
            if (item.type == DeliciousFoodModel.MODEL_NET) {
                if (!TextUtils.isEmpty(item.sYSearchUserFoodModel.merchantName)) {
                    holder.tv_business_name.setText(item.sYSearchUserFoodModel.merchantName);
                } else {
                    holder.tv_business_name.setText(str);
                }
            } else if (item.type == DeliciousFoodModel.MODEL_NATIVE) {
                SYFeed syFeed = item.nativeRichTextFood;
                holder.tv_business_name.setText(mFragment.mHelper.getRestName(syFeed));
            }
        }

        if (str.equals(holder.tv_business_name.getText().toString())) {
            holder.tv_business_name.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.profile_business_empty_icon, 0, 0, 0);
        } else {
            holder.tv_business_name.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.profile_business_icon, 0, 0, 0);
        }
    }

    private void setLocation(MyFoodViewHolder holder, DeliciousFoodModel.FoodListModel item) {
        holder.tv_location.setVisibility(View.GONE);
        if (item != null) {
            if (item.type == DeliciousFoodModel.MODEL_NET) {
                if (!TextUtils.isEmpty(item.sYSearchUserFoodModel.street)) {
                    holder.tv_location.setVisibility(View.VISIBLE);
                    holder.tv_location.setText(item.sYSearchUserFoodModel.street);
                }
            } else if (item.type == DeliciousFoodModel.MODEL_NATIVE) {
                //本地数据无商圈
            }
        }
    }

    private void setTime(MyFoodViewHolder holder, DeliciousFoodModel.FoodListModel item) {
        holder.tv_time.setVisibility(View.GONE);
        if (item != null) {
            if (item.type == DeliciousFoodModel.MODEL_NET) {
                if (!TextUtils.isEmpty(item.sYSearchUserFoodModel.pushTime) && TextUtils.isDigitsOnly(item.sYSearchUserFoodModel.pushTime)) {
                    holder.tv_time.setVisibility(View.VISIBLE);
                    holder.tv_time.setText(TimeUtils.getTime("MM-dd", Long.parseLong(item.sYSearchUserFoodModel.pushTime + "000")));
                }
            } else if (item.type == DeliciousFoodModel.MODEL_NATIVE) {
                SYFeed syFeed = item.nativeRichTextFood;
                holder.tv_time.setVisibility(View.VISIBLE);
                holder.tv_time.setText(TimeUtils.getTime("MM-dd", syFeed.getTimeStamp()));
            }
        }
    }

    private void setPictureCount(MyFoodViewHolder holder, DeliciousFoodModel.FoodListModel item) {
        holder.tv_pic_count.setVisibility(View.GONE);
        if (item != null) {
            if (item.type == DeliciousFoodModel.MODEL_NET) {
                if (item.sYSearchUserFoodModel.foodImageCount > 0) {
                    holder.tv_pic_count.setVisibility(View.VISIBLE);
                    holder.tv_pic_count.setText("" + item.sYSearchUserFoodModel.foodImageCount);
                }
            } else if (item.type == DeliciousFoodModel.MODEL_NATIVE) {
                SYFeed syFeed = item.nativeRichTextFood;
                holder.tv_pic_count.setVisibility(View.VISIBLE);
                holder.tv_pic_count.setText(syFeed.getFood().allImageContent().size() + "");
            }
        }
    }

    private void setCameraOrRich(MyFoodViewHolder holder, DeliciousFoodModel.FoodListModel item) {
        boolean isShort = false;

        if (item != null) {
            if (item.type == DeliciousFoodModel.MODEL_NET) {
                isShort = item.sYSearchUserFoodModel.isShorthandFood;
            } else if (item.type == DeliciousFoodModel.MODEL_NATIVE) {
                SYFeed syFeed = item.nativeRichTextFood;
                isShort = !syFeed.getFood().wasMeishiBianji();
            }
        }

        if (item != null && isShort) {
            holder.profile_camera.setVisibility(View.VISIBLE);
        } else {
            holder.profile_camera.setVisibility(View.GONE);
        }
    }

    private void setShareState(MyFoodViewHolder holder, DeliciousFoodModel.FoodListModel item) {
        holder.tv_share_status.setVisibility(View.GONE);
        if (item != null) {
            if (item.type == DeliciousFoodModel.MODEL_NET) {
                if (!item.sYSearchUserFoodModel.isSharedToAct) {
                    holder.tv_share_status.setVisibility(View.VISIBLE);
                }
            } else if (item.type == DeliciousFoodModel.MODEL_NATIVE) {
                SYFeed syFeed = item.nativeRichTextFood;
                boolean bo1 = syFeed instanceof PersionSYfeedC && !(((PersionSYfeedC) syFeed).isSharedToAct());
                boolean bo2 = syFeed.getFood() instanceof NativeRichTextFood && !((NativeRichTextFood) syFeed.getFood()).getTask().isBshareToSmallYellowO();
                if (bo1 || bo2) {
                    holder.tv_share_status.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_share_status.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setPublishState(MyFoodViewHolder holder, DeliciousFoodModel.FoodListModel item) {
        holder.tv_publish_status.setVisibility(View.GONE);
        holder.rotation_loadingview.setVisibility(View.GONE);
        holder.rotation_loadingview.stopRotationAnimation();
        if (item.type == DeliciousFoodModel.MODEL_NATIVE) {
            final SYFeed syFeed = item.nativeRichTextFood;
            if (((NativeRichTextFood) syFeed.getFood()).getTask().getTaskExecutState() == XTask.XTaskExecutStateFail) {
                holder.tv_publish_status.setVisibility(View.VISIBLE);

                holder.tv_publish_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        XTaskManager.taskManagerWithTask(((NativeRichTextFood) syFeed.getFood()).getTask());
                        notifyDataSetChanged();
                    }
                });
            } else if (((NativeRichTextFood) syFeed.getFood()).getTask().getTaskExecutState() == XTask.XTaskExecutStateExecing) {
                holder.rotation_loadingview.setVisibility(View.VISIBLE);
                holder.rotation_loadingview.startRotationAnimation();
            }
        }
    }

    /**
     * 构建--当选择了未保存时，只筛选未保存的数据，不请求网络数据
     */
    public void buildNativeList() {
        mAllList.clear();

        if (mFragment.mParams.isElite.equals("1")) {
            notifyDataSetChanged();
            if (getItemCount() <= 0) {
                mFragment.setEmptyView();
            }
            return;
        }

        List<PublishModel> taskList = SYDataManager.shareDataManager().allTasks();
        if (taskList == null || taskList.size() <= 0) {
            notifyDataSetChanged();
            if (getItemCount() <= 0) {
                mFragment.setEmptyView();
            }
            return;
        }

        if (taskList.size() > 0) {
            for (int i = 0; i < taskList.size(); i++) {
                SYFeed feed = taskList.get(i).getFeed();
                addNativeItem(mAllList, feed);
            }
        }

        //再全部按照时间排序
        sortListByPublishTime(mAllList);
        notifyDataSetChanged();

        //显示空页面
        if (getItemCount() <= 0) {
            mFragment.setEmptyView();
        }
    }

    private void addNativeItem(List<DeliciousFoodModel.FoodListModel> list, SYFeed feed) {
        DeliciousFoodModel.FoodListModel foodListModel = new DeliciousFoodModel.FoodListModel();
        foodListModel.type = DeliciousFoodModel.MODEL_NATIVE;
        foodListModel.nativeRichTextFood = feed;
        boolean isBianjie = feed.getFood().wasMeishiBianji();//富文本美食编辑

        if (mFragment.mParams.pubType.equals("1")) {//1--速记
            if (!isBianjie) {
                list.add(foodListModel);
            }
        } else if (mFragment.mParams.pubType.equals("2")) {//2--编辑
            if (isBianjie) {
                list.add(foodListModel);
            }
        } else {
            list.add(foodListModel);
        }
    }

    /**
     * 设置筛选为空的状态，而不是没数据为空
     */
    public void setFilterEmptyState(boolean isEmpty) {
        isFilterEmpty = isEmpty;
    }

    @Override
    public int getItemCount() {
        return mAllList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
