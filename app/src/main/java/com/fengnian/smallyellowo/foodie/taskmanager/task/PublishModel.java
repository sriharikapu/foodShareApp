package com.fengnian.smallyellowo.foodie.taskmanager.task;

import android.os.Parcel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fan.framework.config.Tool;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.http.FFResponseCode;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.BrodcastActions;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFrontCoverModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUploadImage;
import com.fengnian.smallyellowo.foodie.bean.publish.AliOssUploadUtil;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.bean.publish.SerializeUtil;
import com.fengnian.smallyellowo.foodie.bean.results.PublishResult;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lanbiao on 16/10/8.
 */
@DatabaseTable
public class PublishModel extends XTask implements Serializable {

    /**
     * 与发布接口的pubType一致
     */
    public static final int TYPE_FAST = 1;
    public static final int TYPE_EDIT = 2;

    public PublishResult getResponse() {
        return response;
    }

    public void setResponse(PublishResult response) {
        this.response = response;
    }

    private PublishResult response;


    public SYFeed getFeed() {
        return feed;
    }

    public void setFeed(SYFeed feed) {
        this.feed = feed;
    }

    SYFeed feed;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @DatabaseField(id = true)
    public String id;

    /**
     * 数据同步锁
     */
    private final static Object LOCK = new Object();

    /**
     * 发布同步锁
     */
    private final Object PUBLISH_LOCK = new Object();

    /**
     * 任务执行状态
     */
    @DatabaseField
    public int taskExecutState = XTaskExecutStateReady;
    /**
     * 类型速记还是美食编辑
     */
    @DatabaseField
    private int type; //1 suji   2 bianji

    @DatabaseField
    private boolean bshareToSmallYellowO;

    public String getFirstCustomId() {
        return firstCustomId;
    }

    public void setFirstCustomId(String firstCustomId) {
        this.firstCustomId = firstCustomId;
    }

    /**
     * 上次的本地数据的ID
     */
    @DatabaseField
    private String firstCustomId;

    /**
     * 生成时间
     */
    @DatabaseField
    private long createTime;

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }


    public void setTaskExecutState(int executState) {
        taskExecutState = executState;
    }

    public int getTaskExecutState() {
        return taskExecutState;
    }

    /**
     * 当前用户
     */
    @DatabaseField
    private String createUser;
    /**
     * 序列化的SYFeed字符串
     */
    @DatabaseField
    private String feedString;
    /**
     * 预留以后新增功能后标记modle的不同版本
     */
    @DatabaseField
    private int version;


    public boolean isBshareToSmallYellowO() {
        return bshareToSmallYellowO;
    }

    public void setBshareToSmallYellowO(boolean bshareToSmallYellowO) {
        this.bshareToSmallYellowO = bshareToSmallYellowO;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFeedString() {
        return feedString;
    }

    public void setFeedString(String feedString) {
        this.feedString = feedString;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    private void notifyLock() {
        synchronized (PUBLISH_LOCK) {
            PUBLISH_LOCK.notify();
        }
    }

    public void uploadImageComplete() {
        if (!isImageNotAllSuccess()) {
            notifyLock();
        }
    }

    public void uploadImageFail() {
        notifyLock();
    }

    @Override
    public void taskExecutor() {
        if (isbCanceled()) {
            return;
        }

        if (!isValidataTask()) {
            return;
        }
        setTaskExecutState(XTaskExecutStateExecing);

        APP.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BrodcastActions.startTask(PublishModel.this);
            }
        });
        FFLogUtil.e("发布", "发布开始了");
        if (isImageNotAllSuccess()) {
            synchronized (PUBLISH_LOCK) {
                try {
                    startUploadAll();
                    PUBLISH_LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (taskExecutState == XTaskExecutStateFail) {
                PublishModelManager.dbSaveOrUpDate(PublishModel.this);
                FFLogUtil.e("发布", "发布失败");
                return;
            }
        }
        preUploadImages();
        Object[] parsms;
        if (FFUtils.isStringEmpty(firstCustomId)) {
            parsms = new Object[]{"bshareToSmallYellowO", isBshareToSmallYellowO() ? 1 : 0,
                    "pubType", feed.getFood().wasMeishiBianji() ? 2 : 1,
                    "startLevel", feed.getStarLevel(),
                    "tags", JSON.toJSONString(feed.getTags()),
                    "releaseTemplate", JSON.toJSONString(feed.getReleaseTemplate()),
                    "food", JSON.toJSONString(feed.getFood(), SerializerFeature.DisableCircularReferenceDetect)};
        } else {
            parsms = new Object[]{"bshareToSmallYellowO", isBshareToSmallYellowO() ? 1 : 0,
                    "pubType", feed.getFood().wasMeishiBianji() ? 2 : 1,
                    "startLevel", feed.getStarLevel(),
                    "tags", JSON.toJSONString(feed.getTags()),
                    "releaseTemplate", JSON.toJSONString(feed.getReleaseTemplate()),
                    "food", JSON.toJSONString(feed.getFood(), SerializerFeature.DisableCircularReferenceDetect),
                    "firstCusId", firstCustomId,
                    "id", feed.getFoodNoteId()};
        }
//        APP.post(Constants.shareConstants().getNetHeaderAdress() + "/notes/publishFoodNotesV250.do", new FFExtraParams().setSynchronized(true), new FFNetWorkCallBack<PublishResult>() {
        APP.post(IUrlUtils.Search.publishFoodNotesV250, new FFExtraParams().setSynchronized(true), new FFNetWorkCallBack<PublishResult>() {
            @Override
            public void onSuccess(PublishResult response, FFExtraParams extra) {
                feed.setId(response.getFeed().getId());
                feed.setFoodNoteId(response.getFeed().getFoodNoteId());
                setTaskExecutState(XTaskExecutStateComplete);
                PublishModel.this.response = response;
                SYDataManager.shareDataManager().removeTask(PublishModel.this);
                taskExecytorEnd();
                BaseActivity topActivity = BaseActivity.getTopActivity();
                if (topActivity != null) {
                    HashMap<String, String> event = new HashMap<String, String>();
                    event.put("account", SP.getUid());
                    event.put("type", feed.getFood().wasMeishiBianji() ? "美食编辑" : "美食速记");
                    event.put("model", String.valueOf(feed.getReleaseTemplate().indexCode)); // 1:微信; 2:QQ; 3:微博; 4:手机号码; 5:验证码登录
                    topActivity.pushEventAction("Yellow_091", event);
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                setTaskExecutState(XTaskExecutStateFail);
                PublishModelManager.dbSaveOrUpDate(PublishModel.this);
                taskExecytorEnd();
                return false;
            }

        }, parsms);

        //super.taskExecutor();
    }

    public void onUnSerializable() {
        setFeed(((SYFeed) SerializeUtil.readObject(getFeedString())));
        if (getFeed() != null)
            getFeed().onUnSerializable();
    }

    public void excuter2(final BaseActivity activity, final FFNetWorkCallBack<PublishResult> callBack, final DraftModel model) {
        if (isImageNotAllSuccess()) {
            final int id = activity.showProgressDialog("", false);
            new Thread() {
                @Override
                public void run() {
                    synchronized (PUBLISH_LOCK) {
                        try {
                            startUploadAll();
                            PUBLISH_LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.dismissProgressDialog(id);
                        }
                    });
                    if (taskExecutState == XTaskExecutStateFail) {
                        FFLogUtil.e("发布", "发布失败");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onBack(null);
                                callBack.onFail(null);
                                activity.showToast("发布失败！图片上传失败！");
                            }
                        });
                        return;
                    }
                    excuter2(activity, callBack, model);
                }
            }.start();
            return;

        }
        preUploadImages();
        Object[] parsms;
        if (FFUtils.isStringEmpty(firstCustomId)) {
            parsms = new Object[]{"bshareToSmallYellowO", isBshareToSmallYellowO() ? 1 : 0,
                    "pubType", feed.getFood().wasMeishiBianji() ? 2 : 1,
                    "startLevel", feed.getStarLevel(),
                    "tags", JSON.toJSONString(feed.getTags()),
                    "releaseTemplate", JSON.toJSONString(feed.getReleaseTemplate()),
                    "food", JSON.toJSONString(feed.getFood(), SerializerFeature.DisableCircularReferenceDetect)};
        } else {
            parsms = new Object[]{"bshareToSmallYellowO", isBshareToSmallYellowO() ? 1 : 0,
                    "pubType", feed.getFood().wasMeishiBianji() ? 2 : 1,
                    "startLevel", feed.getStarLevel(),
                    "tags", JSON.toJSONString(feed.getTags()),
                    "releaseTemplate", JSON.toJSONString(feed.getReleaseTemplate()),
                    "food", JSON.toJSONString(feed.getFood(), SerializerFeature.DisableCircularReferenceDetect),
                    "firstCusId", firstCustomId,
                    "id", feed.getFoodNoteId()};
        }
//        activity.post(Constants.shareConstants().getNetHeaderAdress() + "/notes/publishFoodNotesV250.do", "", new FFExtraParams().setProgressDialogcancelAble(false), new FFNetWorkCallBack<PublishResult>() {
        activity.post(IUrlUtils.Search.publishFoodNotesV250, "", new FFExtraParams().setProgressDialogcancelAble(false), new FFNetWorkCallBack<PublishResult>() {
            @Override
            public void onSuccess(PublishResult response, FFExtraParams extra) {
                feed.setId(response.getFeed().getId());
                feed.setFoodNoteId(response.getFeed().getFoodNoteId());
                setTaskExecutState(XTaskExecutStateComplete);
                BrodcastActions.publishSuccessed(PublishModel.this, response.getFeed());
                PublishModel.this.response = response;
                SYDataManager.shareDataManager().removeDraft(model);
                extra.setObj(PublishModel.this);
                callBack.onSuccess(response, extra);
                BaseActivity topActivity = BaseActivity.getTopActivity();
                if (topActivity != null) {
                    HashMap<String, String> event = new HashMap<String, String>();
                    event.put("account", SP.getUid());
                    event.put("type", feed.getFood().wasMeishiBianji() ? "美食编辑" : "美食速记");
                    event.put("model", String.valueOf(feed.getReleaseTemplate().indexCode)); // 1:微信; 2:QQ; 3:微博; 4:手机号码; 5:验证码登录
                    topActivity.pushEventAction("Yellow_091", event);
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                setTaskExecutState(XTaskExecutStateFail);
                return callBack.onFail(extra);
            }

            @Override
            public boolean onFailContext(PublishResult response, FFExtraParams extra) {
                return callBack.onFailContext(response, extra);
            }

            @Override
            public boolean onFailNet(String msg, FFResponseCode ffResponseCode, FFExtraParams extra) {
                return callBack.onFailNet(msg, ffResponseCode, extra);
            }

            @Override
            public void onFailPublic(PublishResult response, FFExtraParams extraParams) {
                callBack.onFailPublic(response, extraParams);
            }

            @Override
            public void onBack(FFExtraParams extra) {
                callBack.onBack(extra);
            }
        }, parsms);
    }


    /**
     * @return
     */
    private boolean isImageNotAllSuccess() {
        synchronized (LOCK) {

            SYRichTextPhotoModel headImage = getFeed().getFood().getHeadImage();
            if (headImage != null) {
                if (headImage.isTextPhotoModel()) {
                    if (headImage.getPhoto().getImageAsset().getOriginalImage() != null &&
                            !FFUtils.isStringEmpty(headImage.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl()) &&
                            headImage.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        return true;
                    }
                    if (headImage.getPhoto().getImageAsset().getProcessedImage() != null &&
                            !FFUtils.isStringEmpty(headImage.getPhoto().getImageAsset().getProcessedImage().getImage().getUrl()) &&
                            headImage.getPhoto().getImageAsset().getProcessedImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        return true;
                    }
                }
            }

            SYFrontCoverModel frontCoverModel = getFeed().getFood().getFrontCoverModel();
            if (frontCoverModel != null &&
                    frontCoverModel.getFrontCoverContent() != null) {
                if (frontCoverModel.getFrontCoverContent().isTextPhotoModel()) {
                    if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage() != null &&
                            !FFUtils.isStringEmpty(frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getImage().getUrl()) &&
                            frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        return true;
                    }
                    if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage() != null &&
                            !FFUtils.isStringEmpty(frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage().getImage().getUrl()) &&
                            frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        return true;
                    }
                }
            }

            List<SYRichTextPhotoModel> list = getFeed().getFood().getRichTextLists();
            for (int i = 0; i < list.size(); i++) {
                SYRichTextPhotoModel richTextPhotoModel = list.get(i);
                if (richTextPhotoModel.isTextPhotoModel()) {
                    if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage() != null &&
                            !FFUtils.isStringEmpty(richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl()) &&
                            richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        return true;
                    }
                    if (richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage() != null &&
                            !FFUtils.isStringEmpty(richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage().getImage().getUrl()) &&
                            richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * @return
     */
    private void preUploadImages() {
        synchronized (LOCK) {
            SYRichTextPhotoModel headImageModel = getFeed().getFood().getHeadImage();
            if (headImageModel != null) {
                if (headImageModel.isTextPhotoModel()) {
                    if (headImageModel.getPhoto().getImageAsset().getProcessedImage() == null) {
                        headImageModel.getPhoto().getImageAsset().setProcessedImage(headImageModel.getPhoto().getImageAsset().getOriginalImage());
                    }
                }
            }

            SYFrontCoverModel frontCoverModel = getFeed().getFood().getFrontCoverModel();
            if (frontCoverModel != null &&
                    frontCoverModel.getFrontCoverContent() != null) {
                if (frontCoverModel.getFrontCoverContent().isTextPhotoModel()) {
                    if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage() == null) {
                        frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().setProcessedImage(frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage());
                    }
                }
            }

            List<SYRichTextPhotoModel> list = getFeed().getFood().getRichTextLists();
            for (int i = 0; i < list.size(); i++) {
                SYRichTextPhotoModel richTextPhotoModel = list.get(i);
                if (richTextPhotoModel.isTextPhotoModel()) {
                    if (richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage() == null) {
                        richTextPhotoModel.getPhoto().getImageAsset().setProcessedImage(richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage());
                    }
                }
            }
        }
    }

    /**
     * @return
     */
    private boolean startUploadAll() {
        synchronized (LOCK) {

            //头图的上传
            SYRichTextPhotoModel headImageModel = getFeed().getFood().getHeadImage();
            if (headImageModel != null) {
                if (headImageModel.isTextPhotoModel()) {
                    if (headImageModel.getPhoto().getImageAsset().getOriginalImage() != null &&
                            !FFUtils.isStringEmpty(headImageModel.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl()) &&
                            headImageModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        AliOssUploadUtil.getInstance().ossUpload(headImageModel.getPhoto().getImageAsset().getOriginalImage());
//                        SYUploadImage img = headImageModel.getPhoto().getImageAsset().getOriginalImage();
//                        FFLogUtil.e("头图开始上传",img.getImage().getUrl() + "   " + img.hashCode());
                    }
                    if (headImageModel.getPhoto().getImageAsset().getProcessedImage() != null &&
                            !FFUtils.isStringEmpty(headImageModel.getPhoto().getImageAsset().getProcessedImage().getImage().getUrl()) &&
                            headImageModel.getPhoto().getImageAsset().getProcessedImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        AliOssUploadUtil.getInstance().ossUpload(headImageModel.getPhoto().getImageAsset().getProcessedImage());
                    }
                }
            }

            //封面的上传
            SYFrontCoverModel frontCoverModel = getFeed().getFood().getFrontCoverModel();
            if (frontCoverModel != null &&
                    frontCoverModel.getFrontCoverContent() != null) {
                if (frontCoverModel.getFrontCoverContent().isTextPhotoModel()) {
                    if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage() != null &&
                            !FFUtils.isStringEmpty(frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getImage().getUrl()) &&
                            frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        AliOssUploadUtil.getInstance().ossUpload(frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage());
                    }
                    if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage() != null &&
                            !FFUtils.isStringEmpty(frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage().getImage().getUrl()) &&
                            frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        AliOssUploadUtil.getInstance().ossUpload(frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage());
                    }
                }
            }

            //富文本数据的上传
            List<SYRichTextPhotoModel> list = getFeed().getFood().getRichTextLists();
            for (int i = 0; i < list.size(); i++) {
                SYRichTextPhotoModel richTextPhotoModel = list.get(i);
                if (richTextPhotoModel.isTextPhotoModel()) {
                    if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage() != null &&
                            !FFUtils.isStringEmpty(richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl()) &&
                            richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        AliOssUploadUtil.getInstance().ossUpload(richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage());
//                        SYUploadImage img = richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage();
//                        FFLogUtil.e("图片开始上传",img.getImage().getUrl() + "   " + img.hashCode());
                    }
                    if (richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage() != null &&
                            !FFUtils.isStringEmpty(richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage().getImage().getUrl()) &&
                            richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage().getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        AliOssUploadUtil.getInstance().ossUpload(list.get(i).getPhoto().getImageAsset().getProcessedImage());
                    }
                }
            }
            return false;
        }
    }

    /**
     * 0 全部成功
     * 1有失败的
     * 2有正在上传的
     * 3有未开始的 //应该不会先不处理
     *
     * @return
     */
    private int getImageUploadStatus() {
        synchronized (LOCK) {
            int status = 0;
            SYRichTextPhotoModel headImageModel = getFeed().getFood().getHeadImage();
            if (headImageModel != null) {
                if (headImageModel.isTextPhotoModel()) {
                    if (headImageModel.getPhoto().getImageAsset().getOriginalImage() != null &&
                            !FFUtils.isStringEmpty(headImageModel.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl())) {
                        if (headImageModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {

                        } else if (headImageModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL) {
                            status = 1;
                        } else if (headImageModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOADING) {
                            status = 2;
                        }
                    }


                    if (headImageModel.getPhoto().getImageAsset().getProcessedImage() != null &&
                            !FFUtils.isStringEmpty(headImageModel.getPhoto().getImageAsset().getProcessedImage().getImage().getUrl())) {
                        if (headImageModel.getPhoto().getImageAsset().getProcessedImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        } else if (headImageModel.getPhoto().getImageAsset().getProcessedImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL) {
                            status = 1;
                        } else if (headImageModel.getPhoto().getImageAsset().getProcessedImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOADING) {
                            status = 2;
                        }
                    }
                }
            }

            if (status != 0)
                return status;


            SYFrontCoverModel frontCoverModel = getFeed().getFood().getFrontCoverModel();
            if (frontCoverModel != null &&
                    frontCoverModel.getFrontCoverContent() != null) {
                if (frontCoverModel.getFrontCoverContent().isTextPhotoModel()) {
                    if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage() != null &&
                            !FFUtils.isStringEmpty(frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getImage().getUrl())) {
                        if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {

                        } else if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL) {
                            status = 1;
                        } else if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOADING) {
                            status = 2;
                        }
                    }

                    if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage() != null &&
                            !FFUtils.isStringEmpty(frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage().getImage().getUrl())) {
                        if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                        } else if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL) {
                            status = 1;
                        } else if (frontCoverModel.getFrontCoverContent().getPhoto().getImageAsset().getProcessedImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOADING) {
                            status = 2;
                        }
                    }
                }
            }

            if (status != 0)
                return status;


            List<SYRichTextPhotoModel> list = getFeed().getFood().getRichTextLists();
            for (int i = 0; i < list.size(); i++) {
                SYRichTextPhotoModel richTextPhotoModel = list.get(i);
                if (richTextPhotoModel.isTextPhotoModel()) {
                    if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage() != null &&
                            !FFUtils.isStringEmpty(richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getImage().getUrl())) {
                        if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                            continue;
                        } else if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL) {
                            status = 1;
                            break;
                        } else if (richTextPhotoModel.getPhoto().getImageAsset().getOriginalImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOADING) {
                            status = 2;
                        }
                    }

                    if (richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage() != null &&
                            !FFUtils.isStringEmpty(richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage().getImage().getUrl())) {
                        if (richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                            continue;
                        } else if (richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL) {
                            status = 1;
                            break;
                        } else if (richTextPhotoModel.getPhoto().getImageAsset().getProcessedImage().getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOADING) {
                            status = 2;
                        }
                    }
                }
            }
            return status;
        }
    }


    @Override
    public void taskResultCallBack() {
        APP.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getTaskExecutState() == XTaskExecutStateComplete) {//成功
                    BrodcastActions.publishSuccessed(PublishModel.this, response.getFeed());
                } else {//失败
                    BrodcastActions.publishFailed(PublishModel.this);
                }
            }
        });
    }

    public PublishModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.response, flags);
        dest.writeParcelable(this.feed, flags);
        dest.writeString(this.id);
        dest.writeInt(this.type);
        dest.writeByte(this.bshareToSmallYellowO ? (byte) 1 : (byte) 0);
        dest.writeString(this.firstCustomId);
        dest.writeLong(this.createTime);
        dest.writeString(this.createUser);
        dest.writeString(this.feedString);
        dest.writeInt(this.version);
    }

    protected PublishModel(Parcel in) {
        this.response = in.readParcelable(PublishResult.class.getClassLoader());
        this.feed = in.readParcelable(SYFeed.class.getClassLoader());
        this.id = in.readString();
        this.type = in.readInt();
        this.bshareToSmallYellowO = in.readByte() != 0;
        this.firstCustomId = in.readString();
        this.createTime = in.readLong();
        this.createUser = in.readString();
        this.feedString = in.readString();
        this.version = in.readInt();
    }

    public static final Creator<PublishModel> CREATOR = new Creator<PublishModel>() {
        @Override
        public PublishModel createFromParcel(Parcel source) {
            return new PublishModel(source);
        }

        @Override
        public PublishModel[] newArray(int size) {
            return new PublishModel[size];
        }
    };
}