package com.fengnian.smallyellowo.foodie.bean.publish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.fan.framework.base.FFDao;
import com.fan.framework.base.XData;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.FileUitl;
import com.fan.framework.utils.TimeUtils;
import com.fengnian.smallyellowo.foodie.FastEditActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFrontCoverModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.results.SYShopLocationResult;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.fragments.MyCameraFragment;

import java.util.ArrayList;
import java.util.List;

import static com.fan.framework.base.FFApplication.app;

public class DraftModelManager {

    private static DraftModel model;

    public static DraftModel getCurrentDraft() {
        if (model == null) {
            model = SYDataManager.shareDataManager().draftsOfFirst();
        }
        if (model == null) {
            model = create(false);
        }
        return model;
    }

    public static void deleteCurrentDraftModel() {
        model = null;
    }


    public static boolean hasDraft() {
        return !FFUtils.isListEmpty(SYDataManager.shareDataManager().allDrafts());
    }

    /**
     * 当图片选择完成
     *
     * @param list
     */
    public static void onImagesPicked1(final Activity activity, final DraftModel draft, final List<String> list, int index) {
        ArrayList<MyCameraFragment.TempPic> l = new ArrayList<>();
        for (String path : list) {
            l.add(new MyCameraFragment.TempPic(new String[]{path, path}, ""));
        }
        onImagesPicked(activity, draft, l, index);
    }

    /**
     * 当图片选择完成
     *
     * @param list
     */
    public static void onImagesPicked(final Activity activity, final DraftModel draft, final List<MyCameraFragment.TempPic> list, final int index) {

        int id = ((BaseActivity) activity).showProgressDialog("", false);
        final int idd = id;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    NativeRichTextFood rtl = (NativeRichTextFood) draft.getFeed().getFood();
                    for (int i = 0; i < list.size(); i++) {
                        MyCameraFragment.TempPic path = list.get(i);
                        rtl.addImage(path.getPaths()[0], path.hasFilter() ? path.getPaths()[1] : null, path.getFilterCode(), index < 0 ? -1 : index + i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    app.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (activity instanceof FastEditActivity) {
                                ((FastEditActivity) activity).dismissProgressDialog(idd);
                                return;
                            }
                            ((BaseActivity) activity).dismissProgressDialog(idd);
                            activity.setResult(Activity.RESULT_OK);
                            activity.finish();
                        }
                    });
                }
            }
        };
        if (activity instanceof FastEditActivity) {
            thread.run();
        } else {
            thread.start();
        }

    }


    public static DraftModel create(boolean isFast) {
        DraftModel modle = new DraftModel();
        modle.setCreateUser(SP.getUid());
        modle.setCreateTime(System.currentTimeMillis());
        modle.setLastEditTime(System.currentTimeMillis());
        modle.setType(isFast ? DraftModel.TYPE_FAST : DraftModel.TYPE_RICH);
        modle.setFeed(new SYFeed());
        modle.getFeed().setFood(new NativeRichTextFood());
        modle.getFeed().setUser(SP.getUser());
        modle.getFeed().setId(null);
        modle.setShareToSmallYellowO(true);
        modle.getFeed().getFood().setFrontCoverModel(new SYFrontCoverModel());
//        modle.getFeed().getFood().setId(modle.getFeed().getId());
        modle.getFeed().setTimeStamp(System.currentTimeMillis());
        modle.setId(modle.getFeed().getFood().getId());
        if (!isFast) {
            SYRichTextPhotoModel text = new SYRichTextPhotoModel();
            modle.getFeed().getFood().getRichTextLists().add(text);
        }
        return modle;
    }

    public static DraftModel edit(SYFeed pm) {
        return edit(pm, false);
    }

    public static DraftModel edit(SYFeed pm, boolean isToSuji) {
        boolean isRichText = true;
        SYFeed feed = SerializeUtil.readObject(SerializeUtil.serializeObject(pm));
        if (feed.getFood().getPoi() != null && feed.getFood().getPoi().getTitle() == null) {
            feed.getFood().setPoi(null);
        }
        if (!(feed.getFood() instanceof NativeRichTextFood)) {
            NativeRichTextFood food = new NativeRichTextFood();
            SYRichTextFood food1 = feed.getFood();
            feed.setFood(food);
            food.setHeadImage(food1.getHeadImage());
            food.setId(food1.getId());
            food.setContent(food1.getContent());
            food.setCreateTime(food1.getCreateTime());
            food.setTotalPrice(food1.getTotalPrice());
            food.setRichTextLists(food1.getRichTextLists());
            food.setPoi(food1.getPoi());
            food.setNumberOfPeople(food1.getNumberOfPeople());
            food.setFrontCoverModel(food1.getFrontCoverModel());
            food.setFoodType(food1.getFoodType());
            food.setDishesNameList(food1.getDishesNameList());

        }
        if (!pm.getFood().wasMeishiBianji() && !isToSuji) {
            //把content作为一条文本
            SYRichTextFood food1 = feed.getFood();
            SYRichTextPhotoModel item = new SYRichTextPhotoModel();
            item.setContent(food1.getContent());
            feed.getFood().getRichTextLists().add(0, item);
        }
        DraftModel draft = new DraftModel();
        draft.setFirstComtomId(feed.getFood().getId());
        draft.setFeed(feed);
        draft.setShareToSmallYellowO(true);
        draft.setCreateUser(SP.getUid());
        draft.setCreateTime(System.currentTimeMillis());
        draft.setLastEditTime(System.currentTimeMillis());
        draft.setType(isRichText ? DraftModel.TYPE_RICH : DraftModel.TYPE_FAST);
        draft.setSource(pm.getFood().wasMeishiBianji() ? DraftModel.SOURCE_TYPE_RICH : DraftModel.SOURCE_TYPE_FAST);
        draft.setSource_data(pm.getFoodNoteId() != null ? DraftModel.SOURCE_DATA_NET : DraftModel.SOURCE_DATA_TASK);
        feed.getFood().setId(XData.uuid());
        draft.setId(feed.getFood().getId());
        FileUitl.copyDir(CachImageManager.getImagePathWithId(pm.getFood().getId()), CachImageManager.getImagePathWithId(feed.getFood().getId()));
        for (SYRichTextPhotoModel model : feed.getFood().getRichTextLists()) {
            if (model.getPhoto() != null) {
                model.getPhoto().getImageAsset().setPublishId(feed.getFood().getId());
//
//                if (!model.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl().startsWith("http")) {//本地图片
//                    model.getPhoto().getImageAsset().getOriginalImage().getImage().setUrl(
//                            CachImageManager.getImagePathWithId(feed.getFood().getId(), model.getPhoto().getImageAsset().getId(), CachImageManager.IMG_TYPE_ORIGINAL));
//                }
//
//                if (model.getPhoto().getImageAsset().getProcessedImage() != null) {
//                    if (!model.getPhoto().getImageAsset().getProcessedImage().getImage().getUrl().startsWith("http")) {//本地图片
//                        model.getPhoto().getImageAsset().getProcessedImage().getImage().setUrl(
//                                CachImageManager.getImagePathWithId(feed.getFood().getId(), model.getPhoto().getImageAsset().getId(), CachImageManager.IMG_TYPE_PROCESS));
//                    }
//                }
            }
        }

//        SYFrontCoverModel frontCoverModel = draft.getFeed().getFood().getFrontCoverModel();
//        if(frontCoverModel != null){
//            SYRichTextPhotoModel richTextPhotoModel = frontCoverModel.getFrontCoverContent();
//            if(richTextPhotoModel != null){
//                if(FFUtils.isStringEmpty(richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl())){
//                    draft.getFeed().getFood().setFrontCoverModel(null);
//                }
//            }
//        }

        return draft;
    }

    /**
     * 将modle保存到数据库增改
     *
     * @param modle
     */
    public static boolean dbSaveOrUpDate(DraftModel modle) {
        modle.setFeedString(SerializeUtil.serializeObject(modle.feed));
        modle.setLastEditTime(System.currentTimeMillis());
        return FFDao.saveOrUpdate(modle);
    }

    //    /**
//     * 获取之前保存的草稿
//     *
//     * @return
//     */
    public static DraftModel dbQuery() {
        DraftModel model1 = FFDao.findById(DraftModel.class, "createUser", SP.getUid());
        if (model1 != null && model1.getFeedString() != null) {
            model1.setFeed((SYFeed) SerializeUtil.readObject(model1.getFeedString()));
            model1.onUnSerializable();
        }
        return model1;
    }

    /**
     * 获取之前保存的
     *
     * @return
     */
    public static List<DraftModel> dbQueryForAll() {
        return FFDao.findAllById(DraftModel.class, "createUser", SP.getUid());
    }

    /**
     * 删除一条草稿
     *
     * @param modle
     */
    public static void dbDelete(DraftModel modle) {
        FFDao.delete(modle);
    }


    public static void addPoi(SYShopLocationResult.SYShopLocationModel bean) {
        addPoi(getCurrentDraft(), bean);
    }

    public static void addPoi(DraftModel draft, SYShopLocationResult.SYShopLocationModel bean) {
        boolean isCustom = false;
        if (!FFUtils.isStringEmpty(bean.getIsCustom())) {
            isCustom = Integer.parseInt(bean.getIsCustom()) != 0;
        }

        if (!isCustom) {
            isCustom = bean.getMerchantType() > 0 ? true : false;
        }
        addPoi(draft, bean, isCustom);
    }


    public static void addPoi(DraftModel draft, SYShopLocationResult.SYShopLocationModel bean, boolean isCustom) {
        SYPoi poi = new SYPoi();
        poi.setTitle(bean.getTitle());
        poi.setCategory(bean.getCategory());
        poi.setAddress(bean.getAddress());
        if (bean.getAd_info() != null) {
            poi.setCity(bean.getAd_info().getCity());
            poi.setProvince(bean.getAd_info().getProvince());
        } else {
            poi.setCity("");
            poi.setProvince("");
        }
        poi.setId(bean.getId());

        if (bean.getLocation() != null && 0 != bean.getLocation().getLat()) {
            poi.setLatitude(bean.getLocation().getLat());
        }

        if (bean.getLocation() != null && 0 != bean.getLocation().getLng()) {
            poi.setLongitude(bean.getLocation().getLng());
        }
        poi.setIsCustom(isCustom ? 1 : 0);
        draft.getFeed().getFood().setPoi(poi);
    }

    public static boolean dbDeleteAll() {
        return FFDao.deleteAll(DraftModel.class, "createUser", SP.getUid());
    }

    private void editData(SYFeed feed) {
        if (feed == null)
            return;
        SYRichTextFood baseFood = feed.getFood();
        if (baseFood == null)
            return;

        if (baseFood instanceof NativeRichTextFood) {
            //本地数据的编辑
            NativeRichTextFood food = (NativeRichTextFood) baseFood;
            if (food.wasMeishiBianji()) {
                //本地富文本的二次编辑
            } else {
                //本地速记转富文本的二次编辑
            }
        } else {
            //网络数据的编辑
            if (baseFood.wasMeishiBianji()) {
                //网络富文本的二次编辑
            } else {
                //网络速记转富文本的二次编辑
            }
        }
    }

    private void editDraftModel(DraftModel draftModel) {
        if (draftModel == null)
            return;


    }

    /**
     * 二次编辑富文本
     *
     * @param feed
     */
    public void editToRichData(Context context, final SYFeed feed) {
        if (SYDataManager.shareDataManager().allDrafts().size() > 0) {
            new EnsureDialog.Builder(context).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).setNegativeButton("继续编辑", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    DraftModel draftModel = SYDataManager.shareDataManager().draftsOfFirst();
                    editDraftModel(draftModel);
                }
            }).setNeutralButton("创建新编辑", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    editData(feed);
                }
            });
        } else {
            editData(feed);
        }
    }


    private void startRichNewData() {

    }

    /**
     * 发布一个富文本
     */
    private void startRichData(Context context) {
        if (SYDataManager.shareDataManager().allDrafts().size() > 0) {
            new EnsureDialog.Builder(context).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setNegativeButton("继续编辑", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DraftModel draftModel = SYDataManager.shareDataManager().draftsOfFirst();
                    editDraftModel(draftModel);
                }
            }).setNeutralButton("创建新编辑", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startRichNewData();
                }
            });
        } else {
            startRichNewData();
        }
    }

    /**
     * 发布一个速记
     */
    private void startShorthandData() {

    }

    /**
     * 编辑数据到速记
     */
    private void editToShorthandData() {

    }
}
