package com.fengnian.smallyellowo.foodie.contact;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.Adapter.ContactAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.AddFriends;
import com.fengnian.smallyellowo.foodie.bean.publics.ContactPeople;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.AddFriendsResult;
import com.fengnian.smallyellowo.foodie.bean.results.ChangeAttionStusResults;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

import static com.fengnian.smallyellowo.foodie.receivers.PushManager.onNewFrientClick;

/**
 * 通讯录页面
 */

public class ContactActivty extends BaseActivity<IntentData> {
    private TextView tv_is_need_attion;
    private ListView lv_listview;
    private ContactAdapter adapter;

    private StringBuffer phoneName;
    private StringBuffer PhoneNumber;

    private List<AddFriends> addFriendsList;
    private RelativeLayout rl_1;
    int currentapiVersion;
    private List<ContactPeople> people_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        rl_1 = findView(R.id.rl_1);
        addFriendsList = new ArrayList<>();

        tv_is_need_attion = findView(R.id.tv_is_need_attion);
        lv_listview = findView(R.id.lv_listview);
        adapter = new ContactAdapter(ContactActivty.viewHolder.class, R.layout.item_add_friends, addFriendsList, this);
        lv_listview.setAdapter(adapter);
        currentapiVersion = android.os.Build.VERSION.SDK_INT;
        people_list = new ArrayList<>();
        if (currentapiVersion >= 23 && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, AddFriendsActivty.MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD);
        } else {
            getContat();
        }

    }


    /**
     * @param followId    (关注/取消关注)的用户userid
     * @param followState 1.关注0取消关注
     */
    private void checkIsAttion(String followId, String followState, int postion, final SYUser user) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
        //?flag=2
//        post(Constants.shareConstants().getNetHeaderAdress() + "/attention/attentionOrNotV250.do", "", extra, new FFNetWorkCallBack<ChangeAttionStusResults>() {
        post(IUrlUtils.UserCenter.attentionOrNotV250, "", extra, new FFNetWorkCallBack<ChangeAttionStusResults>() {
            @Override
            public void onSuccess(ChangeAttionStusResults response, FFExtraParams extra) {
                if (response == null) return;
                if ("success".equals(response.getResult())) {
                    String AttionState = response.getAttentionState();
                    if ("01".equals(AttionState)) {//wo关注
                        user.setByFollowMe(true);
                    } else if ("00".equals(AttionState) || "10".equals(AttionState)) {//关注wo
                        user.setByFollowMe(false);
                    } else if ("11".equals(AttionState)) { //相互关注
                        user.setByFollowMe(true);
                        user.setFollowMe(true);
                    }
                    adapter.notifyDataSetChanged();
                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "followId", followId, "followState", followState);
    }


    private void getContactFriendList() {
        //PhoneNumber.length()>0 &&phoneName.length()>0
        if (PhoneNumber != null && phoneName != null) {
            FFExtraParams extra = new FFExtraParams();
            extra.setDoCache(true);
            extra.setSynchronized(false);
            extra.setKeepWhenActivityFinished(false);
            extra.setProgressDialogcancelAble(true);
//            post(Constants.shareConstants().getNetHeaderAdress() + "/relation/addressUsersFriendList.do", "", extra, new FFNetWorkCallBack<AddFriendsResult>() {
            post(IUrlUtils.Search.addressUsersFriendList, "", extra, new FFNetWorkCallBack<AddFriendsResult>() {
                @Override
                public void onSuccess(AddFriendsResult response, FFExtraParams extra) {
                    if (response.getErrorCode() == 0) {

                        if (response.getFollowOthers().size() == 0) {
                            tv_is_need_attion.setVisibility(View.GONE);
                            rl_1.setVisibility(View.VISIBLE);
                        } else {
                            tv_is_need_attion.setVisibility(View.VISIBLE);
                            rl_1.setVisibility(View.GONE);
                        }
                        addFriendsList.addAll(response.getFollowOthers());
                        adapter.notifyDataSetChanged();
                        onNewFrientClick();
                    } else showToast(response.getErrorMessage());
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    return false;
                }
            }, "mobiles", PhoneNumber, "names", phoneName);
        } else {
            showToast("未获取到联系人信息");
        }
    }


    private final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 12226;

    /**
     * 动态申请  权限后用用的选择
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case AddFriendsActivty.MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContat();
                } else {
                    showToast("未获得访问权限，不能操作！！");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void getContat() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Allow_permission();
            }
        }).start();
    }

    private void Allow_permission() {
        phoneName = new StringBuffer();
        PhoneNumber = new StringBuffer();
        people_list.clear();
        if (isFinishing()) return;
        getContactList();
        set_numberlist_and_name();
        int phone_number = PhoneNumber.length();
        int phone_name = phoneName.length();
        if (phone_number > 0 && phone_name > 0) {
            PhoneNumber.deleteCharAt(phone_number - 1);
            phoneName.deleteCharAt(phone_name - 1);
            addFriendsList.clear();
            getContactFriendList();
        } else {
            PhoneNumber.append("");
            phoneName.append("");
            getContactFriendList();
        }

    }

    private void set_numberlist_and_name() {
        int len = people_list.size();
        for (int i = 0; i < len; i++) {
            int number_length = people_list.get(i).getNummber_list().size();
            for (int y = 0; y < number_length; y++) {
                phoneName.append(people_list.get(i).getName() + ",");
                PhoneNumber.append(people_list.get(i).getNummber_list().get(y) + ",");
            }
        }
    }

    /**
     * 获取手机联系人
     */
    Cursor cursor;

    private void getContactList() {
        int id = showProgressDialog("");
        Cursor phones = null;
        cursor = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            }


            while (cursor.moveToNext()) {
                ContactPeople people = new ContactPeople();
                if (isFinishing()) {
                    if (cursor != null) cursor.close();
                    return;
                }
                String contactId = cursor.getString(contactIdIndex);
                String name = cursor.getString(nameIndex);
//            phoneName.append(name+",");
                if (TextUtils.isEmpty(name)) {
                    name = "tempname";
                }
                people.setName(name);
            /*
             * 查找该联系人的phone信息
             */
                phones = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                        null, null);

                int phoneIndex = 0;
                if (phones != null) {
                    if (phones.getCount() > 0) {
                        phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    }
                    List<String> mlist = new ArrayList<>();
                    while (phones.moveToNext()) {

                        String phoneNumber = phones.getString(phoneIndex);
//                PhoneNumber.append(phoneNumber+",");
                        if (TextUtils.isEmpty(phoneNumber)) {
                            phoneNumber = "1234567891000";
                        }
                        mlist.add(phoneNumber);
                    }
                    people.setNummber_list(mlist);
                    people_list.add(people);
                    if (phones != null)
                        phones.close();
                }
            }
        }
        if (phones != null)
            phones.close();
        if (cursor != null) cursor.close();
        dismissProgressDialog(id);
    }

    public static class viewHolder {
        public TextView tv_know;
        public ImageView iv_header, iv_add_crown;
        public TextView tv_name;
        public TextView tv_contanct_name;
        public TextView tv_is_attion;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
