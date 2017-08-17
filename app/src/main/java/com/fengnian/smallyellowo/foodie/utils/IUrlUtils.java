package com.fengnian.smallyellowo.foodie.utils;

import com.fengnian.smallyellowo.foodie.appconfig.Constants;

/**
 * Created by elaine on 2017/5/24.
 */

public interface IUrlUtils {

    String BASE_URL = Constants.shareConstants().getNetHeaderAdress();

    interface Constans {
        String LOAGIN_CODE_QQ = "qq_login";
        String LOAGIN_CODE_SINA = "sina_login";
        String LOAGIN_CODE_WECHAT = "wechat_login";
        String SHARE_MESSAGE_WECHAT = "share_msg_wechat";

        String CALL_TOKEN = "token";
        String CALL_OPEN_ID = "open_id";
        String CALL_REFRESH_TOKEN = "refresh_token";

        String ACTIION_SELECT_CITY = "changed_city";
    }

    interface Discover {
        String CUSTOM_CIRCLE = BASE_URL + "/shop/getOriginalTradeArea.do";
        String CUSTOM_CIRCLE_RESTS = BASE_URL + "/shop/getOriginalTradeAreaShopList.do";

    }

    interface HomeUrl {
        String SELECTED_RECOMMEND = BASE_URL + "/unplannedRecommend/getRecommend.do";
        String SELECTED_RECOMMEND_DISLIKE = BASE_URL + "/unplannedRecommend/addToShopBlackList.do";
    }

    interface CommonUrl {
        String PAGE_URL = BASE_URL + "/page/getPage.do";
    }

    interface HtmlUrl {
        /**
         * 积分规则
         */
        String points240 = "http://static.tinydonuts.cn/points240.html";
        /**
         * 会员福利
         */
        String boon = "http://static.tinydonuts.cn/boon.html";
        /**
         * 会员俱乐部
         */
        String club = "http://static.tinydonuts.cn/N-Club2.5.0.html?会员俱乐部";

        /**
         * 用户协议
         */
        String agreement = "http://w.fengnian.cn/Wallet/agreement.html";

        /**
         * 用户协议
         */
        String integral_rule = "http://static.tinydonuts.cn/Integral_rule.html";

        /**
         * 幸运大转盘
         */
        String game = "http://m.tinydonuts.cn/lottery/1/#/index.html";

        /**
         * 美食问答入口
         */
        String cateQuestionAnswer = "http://static.tinydonuts.cn/cateQuestionAnswer/home";
    }

    interface Search {
        String search_shop = BASE_URL + "/shop/searchShop.do";
        String queryFoodEatShopListV250 = BASE_URL + "/eat/queryFoodEatShopListV250.do";
        String queryShopDishesV250 = BASE_URL + "/shop/queryShopDishesV250.do";
        String addFoodEatV250 = BASE_URL + "/eat/addFoodEatV250.do";
        String getMemberPage = BASE_URL + "/page/getMemberPage.do";
        String getShopRecordCount = BASE_URL + "/page/getShopRecordCount.do";
        String getExploitValueRanks = BASE_URL + "/user/getExploitValueRanks.do";
        String setTargetUserRemarkName = BASE_URL + "/user/setTargetUserRemarkName.do";
        String queryShopByCity = BASE_URL + "/shop/queryShopByCity.do";
        String queryShopByCityV270 = BASE_URL + "/shop/queryShopByCityV270.do";
        String queryShopInfoV250 = BASE_URL + "/shop/queryShopInfoV250.do";
        String listInteractMessageV250 = BASE_URL + "/message/listInteractMessageV250.do";
        String getDiffPlaceFood = BASE_URL + "/page/getDiffPlaceFood.do";
        String getMillionMerchant = BASE_URL + "/page/getMillionMerchant.do";
        String publishScoreCheck = BASE_URL + "/notes/publishScoreCheck.do";
        String queryCommonEatListV250 = BASE_URL + "/eat/queryCommonEatListV250.do";
        String queryProgessListV290 = BASE_URL + "/progess/queryProgessListV290.do";
        String visitorRegister = BASE_URL + "/visitorRegister.do";
        String getConfigV220 = BASE_URL + "/config/getConfigV220.do";
        String rptDeviceToken = BASE_URL + "/config/rptDeviceToken.do";
        String addCommentV250 = BASE_URL + "/comment/addCommentV250.do";
        String queryNoticeList = BASE_URL + "/notice/queryNoticeList.do";
        String addReadPgcIdV230 = BASE_URL + "/indexNew/addReadPgcIdV230.do";
        String browsePgcV250 = BASE_URL + "/browsePgcV250.do";
        String createDisShopDetailsHtmlV260 = BASE_URL + "/discover/createDisShopDetailsHtmlV260.do";
        String queryDiscoverShopDetailV260 = BASE_URL + "/discover/queryDiscoverShopDetailV260.do";
        String querShopFeedsByStarLevel = BASE_URL + "/discover/querShopFeedsByStarLevel.do";
        String discoverV260 = BASE_URL + "/discover/discoverV260.do";
        String hotSearches = BASE_URL + "/config/hotSearches.do";
        String questionFeedback = BASE_URL + "/questionFeedback.do";
        String queryShopDrawerInfoV250 = BASE_URL + "/shop/queryShopDrawerInfoV250.do";
        String getMerchantDetailsV250 = BASE_URL + "/eat/getMerchantDetailsV250.do";
        String getMerchantDetailsV280 = BASE_URL + "/eat/getMerchantDetailsV280.do";
        String createDisShopDetailsHtmlV250 = BASE_URL + "/discover/createDisShopDetailsHtmlV250.do";
        String delFoodEatV250 = BASE_URL + "/eat/delFoodEatV250.do";
        String queryFoodEatListV250 = BASE_URL + "/eat/queryFoodEatListV250.do";
        String getOssSettings = BASE_URL + "/oss/getOssSettings.do";
        String detectionVersonUpdate = BASE_URL + "/version/detectionVersonUpdate.do";
        String commonUserList = BASE_URL + "/recommend/commonUserList.do";
        String addressList = BASE_URL + "/relation/addressList.do";
        String initFriendUser = BASE_URL + "/recommend/initFriendUser.do";
        String getUserByNickName = BASE_URL + "/getUserByNickName.do";
        String addressUsersFriendList = BASE_URL + "/relation/addressUsersFriendList.do";
        String queryAttentionRecordListV250 = BASE_URL + "/relation/queryAttentionRecordListV250.do";
        String friendUserList = BASE_URL + "/recommend/friendUserList.do";
        String seachThink = BASE_URL + "/relation/seachThink.do";
        String getFoodNoteInfoV250 = BASE_URL + "/notes/getFoodNoteInfoV250.do";
        String queryDynamicDetailV250 = BASE_URL + "/dynamic/queryDynamicDetailV250.do";
        String queryDynamicDetailV320 = BASE_URL + "/dynamic/queryDynamicDetailV320.do";
        String removeFoodRecordByTypeV250 = BASE_URL + "/food/removeFoodRecordByTypeV250.do";
        String removeFoodNoteV250 = BASE_URL + "/notes/removeFoodNoteV250.do";
        String report = BASE_URL + "/report.do";
        String praiseOrUnpraiseV250 = BASE_URL + "/praise/praiseOrUnpraiseV250.do";
        String publishToDynamicV250 = BASE_URL + "/notes/publishToDynamicV250.do";
        String publishToDynamic = BASE_URL + "/notes/publishToDynamic.do";
        String removeFoodRecordByType = BASE_URL + "/food/removeFoodRecordByType.do";
        String removeFoodNote = BASE_URL + "/notes/removeFoodNote.do";
        String delFoodEatV200 = BASE_URL + "/eat/delFoodEatV200.do";
        String addFoodEat = BASE_URL + "/eat/addFoodEat.do";
        String praiseOrUnpraise = BASE_URL + "/praise/praiseOrUnpraise.do";
        String queryPraiseList = BASE_URL + "/praise/queryPraiseList.do";
        String queryDynamicFeedsV250 = BASE_URL + "/dynamic/queryDynamicFeedsV250.do";
        String querySocialPeopleRecommenders = BASE_URL + "/relation/querySocialPeopleRecommenders.do";
        String querySocialIndex = BASE_URL + "/relation/querySocialIndex.do";
        String homePage = BASE_URL + "/discover/homePage.do";
        String queryIndexMasterV250 = BASE_URL + "/indexNew/queryIndexMasterV250.do";
        String hasEatFoodV250 = BASE_URL + "/eat/hasEatFoodV250.do";
        String saveRecommendV250 = BASE_URL + "/saveRecommendV250.do";
        String queryPgcListV250 = BASE_URL + "/queryPgcListV250.do";
        String queryPgcListByTag = BASE_URL + "/queryPgcListByTag.do";
        String getRelationShipList = BASE_URL + "/relation/getRelationShipList.do";
        String queryIndexActivity = BASE_URL + "/index/queryIndexActivity.do";
        String queryIndexVicinity = BASE_URL + "/index/queryIndexVicinity.do";
        String queryIndexSelection = BASE_URL + "/index/queryIndexSelection.do";
        String sendMessageNew = BASE_URL + "/shortMessage/sendMessageNew.do";
        String validMessageCodeNew = BASE_URL + "/shortMessage/validMessageCodeNew.do";
        String sendMessage = BASE_URL + "/sendMessage.do";
        String userRegisterNew = BASE_URL + "/userRegisterNew.do";
        String updatePassWordNew = BASE_URL + "/user/updatePassWordNew.do";
        String updateUserInfoNew = BASE_URL + "/user/updateUserInfoNew.do";
        String firstSetNickname = BASE_URL + "/user/firstSetNickname.do";
        String userLoginByShortMessage = BASE_URL + "/userLoginByShortMessage.do";
        String validMessageCodeNewV280 = BASE_URL + "/shortMessage/validMessageCodeNewV280.do";
        String bindingPhoneNum = BASE_URL + "/user/bindingPhoneNum.do";
        String getIndexUserList = BASE_URL + "/relation/getIndexUserList.do";
        String queryMyCenterPtypesAndStreets = BASE_URL + "/myCenter/queryMyCenterPtypesAndStreets.do";
        String queryFoodNotesListByTypeV250 = BASE_URL + "/notes/queryFoodNotesListByTypeV250.do";
        String getInvitationInfo = BASE_URL + "/invitation/getInvitationInfo.do";
        String getInvitationCode = BASE_URL + "/invitation/getInvitationCode.do";
        String createInvitationHtml = BASE_URL + "/invitation/createInvitationHtml.do";
        String queryFoodNoteByCriteria = BASE_URL + "/myCenter/queryFoodNoteByCriteria.do";
        String searchFoodNoteByKeyWord = BASE_URL + "/myCenter/searchFoodNoteByKeyWord.do";
        String queryScoreList = BASE_URL + "/score/queryScoreList.do";
        String searchGuide = BASE_URL + "/myCenter/searchGuide.do";
        String apnsPushSetConfig = BASE_URL + "/config/apnsPushSetConfig.do";
        String getPushConfig = BASE_URL + "/config/getPushConfig.do";

        String getInvitationAuthority = BASE_URL + "/invitation/getInvitationAuthority.do";
        String queryNewVersonInformation = BASE_URL + "/version/queryNewVersonInformation.do";
        String queryFoodEatByCriteria = BASE_URL + "/myCenter/queryFoodEatByCriteria.do";
        String searchFoodEatByKeyWord = BASE_URL + "/myCenter/searchFoodEatByKeyWord.do";
        String inupConsignee = BASE_URL + "/consignee/inupConsignee.do";
        String getLastConsigneeInfo = BASE_URL + "/consignee/getLastConsigneeInfo.do";
        String queryUserScoreAllHistoryList = BASE_URL + "/shopmall/queryUserScoreAllHistoryList.do";
        String mallIndex = BASE_URL + "/shopmall/mallIndex.do";
        String queryMallProductInfo = BASE_URL + "/shopmall/queryMallProductInfo.do";
        String shopExchange = BASE_URL + "/shopmall/shopExchange.do";
        String shopErrorFeedback = BASE_URL + "/shop/shopErrorFeedback.do";
        String publishFoodNotesV250 = BASE_URL + "/notes/publishFoodNotesV250.do";
        String delCommentV210 = BASE_URL + "/comment/delCommentV210.do";
        String judgeUserType = BASE_URL + "/user/judgeUserType.do";
        String detectSensitiveWord = BASE_URL + "/notes/detectSensitiveWord.do";
        String queryPgcTag = BASE_URL + "/config/queryPgcTag.do";
        String inferencePic = BASE_URL + "/activity/inferencePic.do";
        String list = BASE_URL + "/city/list.do";
        String switchCity = BASE_URL + "/city/switchCity.do";
    }

    interface Login {
        String qqLogin = BASE_URL + "/qq/qqLogin.do";
        String weiboLogin = BASE_URL + "/weibo/weiboLogin.do";
        String wechatLogin = BASE_URL + "/wechat/wechatLogin.do";
    }

    interface UserCenter {
        // 查询第三方绑定信息
        String bindInfo = BASE_URL + "/user/bindInfo.do";
        String bindingWeChat = BASE_URL + "/user/bindingWeChat.do";
        String bindingQq = BASE_URL + "/user/bindingQq.do";
        String bindingWeibo = BASE_URL + "/user/bindingWeibo.do";
        String getIndexPersonalInfoV310 = BASE_URL + "/user/getIndexPersonalInfoV310.do";
        String attentionOrNotV250 = BASE_URL + "/attention/attentionOrNotV250.do";
        String attentionOrNot = BASE_URL + "/attention/attentionOrNot.do";
        String queryOthersFoodNotesClassificationV250 = BASE_URL + "/notes/queryOthersFoodNotesClassificationV250.do";
        String getIndexPersonalInfoV250 = BASE_URL + "/user/getIndexPersonalInfoV250.do";
        String queryMyFoodRecordsV250 = BASE_URL + "/dynamic/queryMyFoodRecordsV250.do";
        String userLoginNew = BASE_URL + "/userLoginNew.do";
        String sendMessageNewV280 = BASE_URL + "/shortMessage/sendMessageNewV280.do";

        String testReRequest = BASE_URL + "/testReRequest.do";
    }

    interface WorkDining {
        String onePersonFoodList = BASE_URL + "/discover/onePersonFood/onePersonFoodList.do";
        String createVoteActivity = BASE_URL + "/weekdayLunch/createVoteActivity.do";
        String delOneFoodTjById = BASE_URL + "/discover/onePersonFood/delOneFoodTjById.do";
        String queryFoodKindNumberRanges = BASE_URL + "/discover/multipleMeals/queryFoodKindNumberRanges.do";
        String multipleMealsRecommendedList = BASE_URL + "/discover/multipleMeals/multipleMealsRecommendedList.do";
        String addShopToWeekdayLunch = BASE_URL + "/weekdayLunch/addShopToWeekdayLunch.do";
        String getUserWeekdayLunchList = BASE_URL + "/weekdayLunch/getUserWeekdayLunchList.do";
        String delShopToWeekdayLunch = BASE_URL + "/weekdayLunch/delShopToWeekdayLunch.do";
        String workingMealList = BASE_URL + "/discover/workingMeal/workingMealList.do";
        String getShopRandomByAccount = BASE_URL + "/weekdayLunch/getShopRandomByAccount.do";
        String addBatchFoodEatV250 = BASE_URL + "/eat/addBatchFoodEatV250.do";

    }
}
