package com.fengnian.smallyellowo.foodie.taskmanager.task;

import android.support.annotation.Nullable;

import com.fan.framework.base.FFDao;
import com.fan.framework.base.XData;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.xtaskmanager.XTaskManager;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFoodPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUploadImage;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.publish.SerializeUtil;
import com.fengnian.smallyellowo.foodie.bean.results.PublishResult;

import java.util.List;

import static com.fengnian.smallyellowo.foodie.datamanager.SYDataManager.shareDataManager;

/**
 * Created by Administrator on 2016-10-8.
 */
public class PublishModelManager {
    public static PublishModel createAndSave(DraftModel draft) {
        PublishModel modle = new PublishModel();
        modle.setId(draft.getId());
        draft.getFeed().setTimeStamp(System.currentTimeMillis());
        modle.setFirstCustomId(draft.getFirstComtomId());
        ((NativeRichTextFood) draft.getFeed().getFood()).setTask(modle);
        modle.setFeed(draft.getFeed());
        modle.getFeed().setEatState(1);
        modle.setCreateTime(System.currentTimeMillis());
        modle.setCreateUser(SP.getUid());
        modle.setBshareToSmallYellowO(draft.isShareToSmallYellowO());
        modle.setType(draft.getFeed().getFood().wasMeishiBianji() ? PublishModel.TYPE_EDIT : PublishModel.TYPE_FAST);
        modle.setFeedString(SerializeUtil.serializeObject(modle.feed));
        return modle;
    }

    public static boolean isPublishFail(SYFeed feed) {
        for (PublishModel model : shareDataManager().allTasks()) {
            if (model.getFeed().getFood().getId().equals(feed.getFood().getId())) {
                return model.getTaskExecutState() == PublishModel.XTaskExecutStateFail;
            }
        }
        return false;
    }


    /**
     * 将modle保存到数据库增改
     *
     * @param modle
     */
    public static void dbSaveOrUpDate(PublishModel modle) {
        modle.setFeedString(SerializeUtil.serializeObject(modle.feed));
        FFDao.saveOrUpdate(modle);
    }
//
//    /**
//     * 获取之前保存的
//     *
//     * @return
//     */
//    public static PublicshModel dbQuery(String id) {
//        return FFDao.findById(PublicshModel.class, "ID", id);
//    }

    /**
     * 获取之前保存的
     *
     * @return
     */
    public static List<PublishModel> dbQueryForAll() {
        List<PublishModel> list = FFDao.findAllById(PublishModel.class, "createUser", SP.getUid());
        return list;
    }

    /**
     * 删除一条
     *
     * @param modle
     */
    public static void dbDelete(PublishModel modle) {
        FFDao.delete(modle);
    }

    /**
     * 删除一条
     *
     * @param id
     */
    public static void dbDelete(String id) {
        FFDao.deleteByID(PublishModel.class, id);
    }

    public static PublishModel publish(BaseActivity activity,FFNetWorkCallBack<PublishResult> callBack,DraftModel model, boolean isFast) {
        if (!isFast)
            PublishModelManager.initHeadImage(model);
        final PublishModel task = PublishModelManager.createAndSave(model);
        task.excuter2(activity,callBack,model);
        return task;
    }

    private static void initHeadImage(DraftModel model) {
        if (model == null){
            return;
        }
        SYRichTextPhotoModel headImage = getHeadImage(model.getFeed());
        if (headImage != null) {
            model.getFeed().getFood().setHeadImage(headImage);
        }
    }

    @Nullable
    public static SYRichTextPhotoModel getHeadImage(SYFeed feed) {
        SYRichTextPhotoModel headImage = null;

        if (feed == null || feed.getFood() == null) {
            return headImage;
        }

        if (feed.getFood().getFrontCoverModel() != null &&
                feed.getFood().getFrontCoverModel().getFrontCoverContent() != null &&
                feed.getFood().getFrontCoverModel().getFrontCoverContent().getPhoto() != null &&
                feed.getFood().getFrontCoverModel().getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage() != null &&
                !FFUtils.isStringEmpty(feed.getFood().getFrontCoverModel().getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getImage().getUrl())) {
            headImage = SerializeUtil.copy(feed.getFood().getFrontCoverModel().getFrontCoverContent());
        } else {
            for (int i = 0; i < feed.getFood().getRichTextLists().size(); i++) {
                SYFoodPhotoModel photo = feed.getFood().getRichTextLists().get(i).getPhoto();
                if (photo != null) {
                    headImage = SerializeUtil.copy(feed.getFood().getRichTextLists().get(i));
                    break;
                }
            }
        }

        if (headImage != null) {
            String assetId = XData.uuid();

            if (headImage.getPhoto().getImageAsset().getProcessedImage() != null) {
                headImage.getPhoto().getImageAsset().setOriginalImage(headImage.getPhoto().getImageAsset().getProcessedImage());
            }
            headImage.getPhoto().getImageAsset().setId(assetId);
            headImage.getPhoto().getImageAsset().getOriginalImage().setAssetId(assetId);
            headImage.getPhoto().getImageAsset().setProcessedImage(null);//头图不需要处理过的图直接使用原图
            headImage.getPhoto().getImageAsset().getOriginalImage().setUploadStatus(SYUploadImage.UPLOAD_STATUS_INIT);
        }
        return headImage;
    }

    public static boolean isNative(SYFeed feed) {
        for (PublishModel model : shareDataManager().allTasks()) {
            if (model.getFeed().getFood().getId().equals(feed.getFood().getId())) {
                return true;
            }
        }
        return false;
    }

    public static PublishModel getTask(SYFeed feed) {
        for (PublishModel model : shareDataManager().allTasks()) {
            if (model.getFeed().getFood().getId().equals(feed.getFood().getId())) {
                return model;
            }
        }
        return null;
    }
}
