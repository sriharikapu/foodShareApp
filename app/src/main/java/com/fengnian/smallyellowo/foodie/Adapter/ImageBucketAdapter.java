package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fan.framework.base.FFContext;
import com.fan.framework.config.Value;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager;
import com.fengnian.smallyellowo.foodie.bean.publics.GalleryInfo;
import com.fengnian.smallyellowo.foodie.bean.publics.ImageItem;
import com.fengnian.smallyellowo.foodie.bean.publics.PhotoInfo;
import com.fengnian.smallyellowo.foodie.release.GalleryAndPhotoActvity;
import com.fengnian.smallyellowo.foodie.release.PicPhotoActvity;
import com.fengnian.smallyellowo.foodie.release.ScrollPhotoActvity;
import com.fengnian.smallyellowo.foodie.utils.Config;
import com.fengnian.smallyellowo.foodie.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;


public class ImageBucketAdapter extends BaseAdapter {
    FFContext ffcontext;
    String ismemo;//1 是水单  other  不是
    //    private boolean IsFragementAactvity;
    private int height;
    private int temp_index;
    private GalleryInfo Info;
    private int stytle;

    public ImageBucketAdapter(FFContext ffcontext, GalleryInfo info) {
        this.ffcontext = ffcontext;
        this.ismemo = info.getIsmemo();
        height = (int) ((FFUtils.getDisWidth() - FFUtils.getPx(8)) / 3);
//        this.IsFragementAactvity = isFragementAactvity;
        this.temp_index = info.getIndex();

        stytle = info.getTemplate_type();
        this.Info = info;
    }


    @Override
    public int getCount() {
        return Value.PICS_LIST.size();
    }

    @Override
    public Object getItem(int position) {
        return Value.PICS_LIST.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class Holder {
        private ImageView iv;
        private ImageView selected;
        private RelativeLayout rl_1;
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        final Holder holder;
        if (view == null) {
            holder = new Holder();
            view = View.inflate((Context) ffcontext.context(), R.layout.item_image_bucket, null);
            holder.iv = (ImageView) view.findViewById(R.id.image);
            holder.selected = (ImageView) view.findViewById(R.id.isselected);
            holder.rl_1 = (RelativeLayout) view.findViewById(R.id.rl_1);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        if ("1".equals(ismemo)) {
            holder.rl_1.setVisibility(View.GONE);
            holder.selected.setVisibility(View.GONE);
        } else {
            holder.rl_1.setVisibility(View.VISIBLE);
            holder.selected.setVisibility(View.VISIBLE);
        }

        final ImageItem item = Value.PICS_LIST.get(position);
        final String thumbPath = item.getImagePath();

        FFImageLoader.loadMiddleImage(ffcontext, thumbPath, holder.iv);// "file://"+


//		FFImageLoader.loadNativeImage(ffcontext, thumbPath, holder.iv, height, height, R.drawable.alpha);


//        if (item_image_gallery.isSelected) {
//            holder.selected.setBackgroundResource(R.mipmap.picture_selected);
//
//        } else {
//            holder.selected.setBackgroundResource(R.mipmap.picture_no_selected);
//        }

        setui(item, holder);
        holder.rl_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ImageUtil.getImageSize(thumbPath) > 12) {
                    ffcontext.showToast("选择的相片不能太大哦~");
                    return;
                }

                int pictureCount = RichTextModelManager.getConfigByIndex(stytle).picture.pictureCount;
                if (item.isSelected) {
                    item.setSelected(false);
                    holder.selected.setBackgroundResource(R.mipmap.picture_no_selected);
                    // 从集合 中删除
                    for (int x = 0; x < Config.list.size(); x++) {
                        ImageItem imgitem = Config.list.get(x);
                        if (item.getImageId().equals(imgitem.getImageId())) {
                            Config.list.remove(imgitem);

                        }
                    }
                    int len = Config.list.size() + Info.getNum();
                    if (pictureCount != -1) {
                        GalleryAndPhotoActvity.tv_right_title.setText("下一步(" + len + "/"+pictureCount+")");
                        GalleryAndPhotoActvity.refreshCompleteUI();
                        if (PicPhotoActvity.tv_right_title != null) {
                            PicPhotoActvity.tv_right_title.setText("下一步(" + len + "/"+pictureCount+")");
                            PicPhotoActvity.refreshCompleteUI();
                        }
                    } else {
                        GalleryAndPhotoActvity.tv_right_title.setText("下一步(" + len + ")");
                        GalleryAndPhotoActvity.refreshCompleteUI();
                        if (PicPhotoActvity.tv_right_title != null) {
                            PicPhotoActvity.tv_right_title.setText("下一步(" + len + ")");
                            PicPhotoActvity.refreshCompleteUI();
                        }
                    }
                } else {
                    if (pictureCount != -1) {
                        if (((Config.list.size() + Info.getNum()) > pictureCount)) {
                            APP.app.showToast("最多选择" + pictureCount + "张图片哦", null);
                            return;
                        }
                    }
                    item.setSelected(true);
                    holder.selected.setBackgroundResource(R.mipmap.picture_selected);
                    //从集合中添加
                    Config.list.add(item);
                    int len = Info.getNum() + Config.list.size();
                    if (pictureCount != -1) {
                        GalleryAndPhotoActvity.tv_right_title.setText("下一步(" + len + "/"+pictureCount+")");
                        GalleryAndPhotoActvity.refreshCompleteUI();
                        if (PicPhotoActvity.tv_right_title != null) {
                            PicPhotoActvity.tv_right_title.setText("下一步(" + len + "/"+pictureCount+")");
                            PicPhotoActvity.refreshCompleteUI();
                        }
                    } else {
                        GalleryAndPhotoActvity.tv_right_title.setText("下一步(" + len + ")");
                        GalleryAndPhotoActvity.refreshCompleteUI();
                        if (PicPhotoActvity.tv_right_title != null) {
                            PicPhotoActvity.tv_right_title.setText("下一步(" + len + ")");
                            PicPhotoActvity.refreshCompleteUI();
                        }
                    }


                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("1".equals(ismemo)) {
                    //跳转水单
//                    Intent intent = new Intent((Context) ffcontext, UploadMemoPhotoActivty.class);
//                    intent.putExtra("url", item_image_gallery.getImagePath());
//                    ((Context) ffcontext).startActivity(intent);
//                    if (IsFragementAactvity) {
//                        ((BaseFragmentActivity) ffcontext).finish();
//                    } else {
//                        ((BaseActivity) ffcontext).finish();
//                    }

//					((BaseActivity)ffcontext).startActivity(UploadMemoPhotoActivty.class, new IntentData());
                } else {
                    //在某种情况下会crash，错误日志：android.os.TransactionTooLargeException: data parcel size 1085032 bytes
                    //意思是写入parcel的数据过大引起的。这里捕获下异常： by chenglin 2017年7月27日16:18:57
                    try {
                        PhotoInfo info = new PhotoInfo();
                        info.setPos(position);
                        info.setIndex(temp_index);
                        info.setSty(stytle);
                        info.setNum(Info.getNum());
                        info.setRequestCode(10086);
                        ((BaseActivity) ffcontext).startActivity(ScrollPhotoActvity.class, info);
                    }catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        return view;
    }


    void setui(ImageItem item, Holder holder) {
        for (int pos = 0; pos < Config.list.size(); pos++) {
            ImageItem temp = Config.list.get(pos);
            if (temp.getImageId().equals(item.getImageId())) {
                item.setSelected(true);
                holder.selected.setBackgroundResource(R.mipmap.picture_selected);
                return;
            } else {
                item.setSelected(false);
                holder.selected.setBackgroundResource(R.mipmap.picture_no_selected);
            }
        }
    }

}
