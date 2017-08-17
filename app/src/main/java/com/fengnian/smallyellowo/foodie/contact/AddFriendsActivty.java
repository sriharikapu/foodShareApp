package com.fengnian.smallyellowo.foodie.contact;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.Adapter.AddFriendsAdapter;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.AddFriends;
import com.fengnian.smallyellowo.foodie.bean.publics.ContactPeople;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.publics.User;
import com.fengnian.smallyellowo.foodie.bean.results.AddFriendsResult;
import com.fengnian.smallyellowo.foodie.bean.results.ContactSearchResult;
import com.fengnian.smallyellowo.foodie.bean.results.GoodFriendsTuijianResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;
import com.fengnian.smallyellowo.foodie.zxing.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加朋友
 */

public class AddFriendsActivty extends BaseActivity<IntentData> implements View.OnClickListener {
    private RelativeLayout rl_1_1, rl_2, rl_3, rl_no_friend, rl_5, rl_0, rl_erweima, rl_saoyisao;
    //    private TextView tv_know;
    private ListView lv_listview;
    private AddFriendsAdapter adapter;

    private StringBuffer phoneName;
    private StringBuffer PhoneNumber;

    private List<ContactPeople> people_list;

    private List<AddFriends> addFriendsList; //认识的人

    private List<AddFriends> twoaddFriendsList; //认识的人
    private List<SYUser> needToFriendsList;// 成功邀请的人
    private List<SYUser> twoneedToFriendsList;// 成功邀请的人

    private EditText ed_search;
    private TextView tv_cancle, tv_search_content, tv_name, tv_content;
    private ImageView iv_header, iv_no_search, iv_delete, iv_have_new_tuijian, iv_add_crown;
    private User user;
    int currentapiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addFriendsList = new ArrayList<>();
        twoaddFriendsList = new ArrayList<>();
        needToFriendsList = new ArrayList<>();
        twoneedToFriendsList = new ArrayList<>();
        setContentView(R.layout.activty_add_friends);
        View view = View.inflate(this, R.layout.add_friend_part, null);
        rl_0 = (RelativeLayout) view.findViewById(R.id.rl_0);

        rl_erweima = (RelativeLayout) view.findViewById(R.id.rl_erweima);
        rl_erweima.setOnClickListener(this);
        rl_saoyisao = (RelativeLayout) view.findViewById(R.id.rl_saoyisao);
        rl_saoyisao.setOnClickListener(this);

        rl_1_1 = (RelativeLayout) view.findViewById(R.id.rl_1_1);
        ed_search = (EditText) view.findViewById(R.id.ed_search);
        ed_search.addTextChangedListener(watcher);
        iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        iv_delete.setOnClickListener(this);
        tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
//        tv_know=(TextView) view.findViewById(R.id.tv_know);
//        tv_know.setVisibility(View.GONE);

        rl_5 = (RelativeLayout) view.findViewById(R.id.rl_5);
        iv_have_new_tuijian = (ImageView) view.findViewById(R.id.iv_have_new_tuijian);


        rl_2 = (RelativeLayout) view.findViewById(R.id.rl_2);
        tv_search_content = (TextView) view.findViewById(R.id.tv_search_content);
        rl_3 = (RelativeLayout) view.findViewById(R.id.rl_3);
        iv_add_crown = (ImageView) view.findViewById(R.id.iv_add_crown);
        iv_header = (ImageView) view.findViewById(R.id.iv_header);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        initclick();
        lv_listview = findView(R.id.lv_listview);
        rl_no_friend = findView(R.id.rl_no_friend);

        iv_no_search = findView(R.id.iv_no_search);
//        adapter =new AddFriendsAdapter(viewHolder.class ,R.layout.item_add_friends,addFriendsList,this);
        adapter = new AddFriendsAdapter(this, addFriendsList, needToFriendsList);
//        adapter=new AddFriendsAdapter(this,addFriendsList,needToFriendsList);
        lv_listview.addHeaderView(view);
        lv_listview.setAdapter(adapter);
        people_list = new ArrayList<>();
//        currentapiVersion=android.os.Build.VERSION.SDK_INT;
//        if(currentapiVersion>=23)
//            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD);


    }


    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (editable.length() > 0) {
                iv_delete.setVisibility(View.VISIBLE);
            } else {
                iv_delete.setVisibility(View.GONE);
            }

            if (editable.length() > 0) {
                setSearch();
                tv_search_content.setText(editable);
                rl_3.setVisibility(View.GONE);
                rl_no_friend.setVisibility(View.GONE);

            } else {
                setNotSearch();

            }


        }
    };

    private void setSearch() {
//        rl_1_1.setVisibility(View.GONE);
//        rl_5.setVisibility(View.GONE);
        rl_0.setVisibility(View.GONE);

//        tv_know.setVisibility(View.GONE);
        rl_2.setVisibility(View.VISIBLE);
        iv_no_search.setVisibility(View.GONE);
        twoaddFriendsList.addAll(addFriendsList);
        twoneedToFriendsList.addAll(needToFriendsList);
        addFriendsList.clear();
        needToFriendsList.clear();
        adapter.notifyDataSetChanged();

    }

    private void setNotSearch() {
//        rl_1_1.setVisibility(View.VISIBLE);
//        rl_5.setVisibility(View.VISIBLE);
        rl_0.setVisibility(View.VISIBLE);
//        tv_know.setVisibility(View.VISIBLE);
        rl_2.setVisibility(View.GONE);
        rl_3.setVisibility(View.GONE);
        addFriendsList.addAll(twoaddFriendsList);
        needToFriendsList.addAll(twoneedToFriendsList);
        twoaddFriendsList.clear();
        twoneedToFriendsList.clear();
        if (addFriendsList.size() == 0 && needToFriendsList.size() == 0) {
//            tv_know.setVisibility(View.GONE);
            rl_no_friend.setVisibility(View.VISIBLE);
        } else {
//            tv_know.setVisibility(View.VISIBLE);
            rl_no_friend.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //未授权nothing
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD);
        } else {//已授权
            getContat();
        }
    }

    public final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD = 1230;


    private void Allow_permission() {
        phoneName = new StringBuffer();
        PhoneNumber = new StringBuffer();
        people_list.clear();
        if (isFinishing()) return;
        getContactList();
        set_numberlist_and_name();
        if (PhoneNumber.length() > 0 && phoneName.length() > 0) {
            PhoneNumber.deleteCharAt(PhoneNumber.length() - 1);
            phoneName.deleteCharAt(phoneName.length() - 1);
            getAddFriendsList();
        } else {
            PhoneNumber.append("");
            phoneName.append("");
            getAddFriendsList();
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


    private void getContat() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Allow_permission();
            }
        }).start();
    }

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
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //用户授权

//                    Allow_permission();
                    getContat();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    showToast("未获得访问权限，不能操作！！");
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getAddFriendsList() {

        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/relation/addressList.do", "", extra, new FFNetWorkCallBack<AddFriendsResult>() {
        post(IUrlUtils.Search.addressList, "", extra, new FFNetWorkCallBack<AddFriendsResult>() {
            @Override
            public void onSuccess(AddFriendsResult response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    addFriendsList.clear();
                    needToFriendsList.clear();
                    addFriendsList.addAll(response.getFollowOthers());
                    needToFriendsList.addAll(response.getInvateUser());
                    if (addFriendsList.size() == 0 && needToFriendsList.size() == 0) {
//                        tv_know.setVisibility(View.GONE);
                        rl_no_friend.setVisibility(View.VISIBLE);
                    } else {
//                        tv_know.setVisibility(View.VISIBLE);
                        rl_no_friend.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                if (addFriendsList.size() == 0 && needToFriendsList.size() == 0) {
//                    tv_know.setVisibility(View.GONE);
                    rl_no_friend.setVisibility(View.VISIBLE);
                } else {
//                    tv_know.setVisibility(View.VISIBLE);
                    rl_no_friend.setVisibility(View.GONE);
                }
                return false;
            }
        }, "mobiles", PhoneNumber, "names", phoneName);

    }


    /**
     * 是否有好友推荐
     */
    private void getInvitefiriend() {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/recommend/initFriendUser.do", null, extra, new FFNetWorkCallBack<GoodFriendsTuijianResult>() {
        post(IUrlUtils.Search.initFriendUser, null, extra, new FFNetWorkCallBack<GoodFriendsTuijianResult>() {
            @Override
            public void onSuccess(GoodFriendsTuijianResult response, FFExtraParams extra) {
                if (response.judge()) {
                    //
                    if (response.getUnReadNum() > 0) {
                        iv_have_new_tuijian.setVisibility(View.VISIBLE);
                    }

                } else showToast(response.getErrorMessage());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        });
    }

    private void initclick() {
        rl_1_1.setOnClickListener(this);
        rl_5.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        rl_2.setOnClickListener(this);
        rl_3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_erweima:
                startActivity(MySelfErWeiMaActivity.class, new IntentData());
                break;
            case R.id.rl_saoyisao:
                Intent in = new Intent(this, CaptureActivity.class);
                startActivity(in);
                break;
            case R.id.rl_1_1:
                startActivity(ContactActivty.class, new IntentData());
                break;
            case R.id.rl_5:
                //TODO: 2017-1-13
                startActivity(RecommendActivity.class, new IntentData());
                break;
            case R.id.rl_2:
                rl_2.setVisibility(View.GONE);
                String nickname = ed_search.getText().toString().trim();
                zhengqueSearcha(nickname);
                break;

            case R.id.rl_3:
                SYUser Syuser = new SYUser();
                if (user == null) user = new User();
                Syuser.setId(user.getAccount());
                SYImage ima = new SYImage();
                ima.setUrl(user.getHeadImage());
                Syuser.setHeadImage(ima);
                Syuser.setUserType(user.getUserType());
                Syuser.setPersonalDeclaration(user.getPersonalitySignature());
                UserInfoIntent userinfo = new UserInfoIntent();
                userinfo.setUser(Syuser);
                IsAddCrownUtils.ActivtyStartAct(Syuser, userinfo, this);

                break;
            case R.id.tv_cancle:
                //// TODO: 2016-9-12   取消键盘 和
                ed_search.setText("");
                setNotSearch();
                break;

            case R.id.iv_delete:
//                rl_1_1.setVisibility(View.GONE);
//                rl_5.setVisibility(View.GONE);
                rl_0.setVisibility(View.GONE);

//                tv_know.setVisibility(View.GONE);
                rl_2.setVisibility(View.GONE);
                iv_no_search.setVisibility(View.GONE);
                ed_search.setText("");
//                addFriendsList.clear();
//                needToFriendsList.clear();
//                adapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 精确搜索的请求
     */
    private void zhengqueSearcha(String nickname) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/getUserByNickName.do", "", extra, new FFNetWorkCallBack<ContactSearchResult>() {
        post(IUrlUtils.Search.getUserByNickName, "", extra, new FFNetWorkCallBack<ContactSearchResult>() {
            @Override
            public void onSuccess(ContactSearchResult response, FFExtraParams extra) {
                if ("success".equals(response.getRestate())) {
                    rl_no_friend.setVisibility(View.GONE);
                    user = response.getRedata().getUser();
                    user.setUserType(response.getUserType());
                    if (user == null) {
                        iv_no_search.setVisibility(View.VISIBLE);
                        return;
                    }
                    iv_no_search.setVisibility(View.GONE);
                    rl_3.setVisibility(View.VISIBLE);

                    if (response.getUserType() == 1) {//
                        iv_add_crown.setVisibility(View.VISIBLE);
                    } else {
                        iv_add_crown.setVisibility(View.GONE);
                    }

                    FFImageLoader.loadAvatar(AddFriendsActivty.this, user.getHeadImage(), iv_header);
                    tv_name.setText(user.getNickname());
                    tv_content.setText(user.getPersonalitySignature());

                } else {
                    iv_no_search.setVisibility(View.VISIBLE);
                    rl_no_friend.setVisibility(View.GONE);
                    showToast(response.getMessage());
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {

                return false;
            }
        }, "nickname", nickname);
    }


    /**
     * 获取手机联系人
     */
//    Cursor cursor;

    private void getContactList() {
        int id = showProgressDialog("");
        Cursor phones = null;
        Cursor cursor = this.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;
        if (cursor != null && !cursor.isClosed()) {
            if (cursor.getCount() > 0) {
                contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            }


            while (cursor.moveToNext()) {
                ContactPeople people = new ContactPeople();
                if (isFinishing()) {
                    cursor.close();
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

        public ImageView iv_header;
        public TextView tv_name;
        public TextView tv_contanct_name;
        public TextView tv_is_attion;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
