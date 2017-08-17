package com.fengnian.smallyellowo.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.config.Tool;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.SiftBean;
import com.fengnian.smallyellowo.foodie.bean.results.HotSearchWordResult;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.MoreClassAreaIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.RestSearchResultIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.SearchRestIntent;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.widgets.XCFlowLayout;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchRestActivity extends BaseActivity<SearchRestIntent> {

    View v_hotWord;
    XCFlowLayout flWordContainer;

    ImageView iv_area;
    ImageView iv_class;

    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.iv_clear)
    ImageView ivClear;
    @Bind(R.id.tv_search)
    TextView tvSearch;
    @Bind(R.id.lv_search)
    ListView lvSearch;
    View tv_custom_circle;

    List<String> history;
    private SearchHistoryAdapter adapter;

    private boolean bResult = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            RestSearchResultIntent intent = new RestSearchResultIntent();
            intent.setType(data.getIntExtra("type", 0));
            if (intent.getType() == RestSearchResultIntent.TYPE_AREA) {
                intent.setArea((SiftBean.BusinessListBean.BusinessGroup.Business) data.getParcelableExtra("item"));
            } else {
                intent.setContent(data.getStringExtra("content"));
            }
            startActivity(RestSearchResultActivity.class, intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_rest);
        setNotitle(true);
        ButterKnife.bind(this);
        v_hotWord = getLayoutInflater().inflate(R.layout.view_search_hot_word, null);
        tv_custom_circle = v_hotWord.findViewById(R.id.tv_custom_circle);
        tv_custom_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FFUtils.setSoftInputInvis(v.getWindowToken());
                startActivity(CustomCircleActivity.class, new IntentData());
            }
        });
        flWordContainer = (XCFlowLayout) v_hotWord.findViewById(R.id.fl_word_container);
        iv_area = (ImageView) v_hotWord.findViewById(R.id.iv_area);
        iv_class = (ImageView) v_hotWord.findViewById(R.id.iv_class);

        int width = (FFUtils.getDisWidth() - FFUtils.getPx(75)) / 2;
        int height = width * 167 / 500;
        iv_area.getLayoutParams().height = height;
        iv_area.getLayoutParams().width = width;
        iv_class.getLayoutParams().height = height;
        iv_class.getLayoutParams().width = width;
        iv_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> event = new HashMap<String, String>();
                event.put("account", SP.getUid());
                event.put("channel", Tool.getChannelName(SearchRestActivity.this));
                event.put("city", CityPref.getSelectedCity().getAreaName());
                pushEventAction("Yellow_101", event);

                MoreClassAreaIntent moreClassAreaIntent = new MoreClassAreaIntent();
                moreClassAreaIntent.setRequestCode(123);
                startActivity(MoreClassAreaActivity.class, moreClassAreaIntent.setArea(true));
            }
        });
        iv_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> event = new HashMap<String, String>();
                event.put("account", SP.getUid());
                event.put("channel", Tool.getChannelName(SearchRestActivity.this));
                event.put("city", CityPref.getSelectedCity().getAreaName());
                pushEventAction("Yellow_102", event);

                MoreClassAreaIntent moreClassAreaIntent = new MoreClassAreaIntent();
                moreClassAreaIntent.setRequestCode(123);
                startActivity(MoreClassAreaActivity.class, moreClassAreaIntent.setArea(false));
            }
        });

        List<HotSearchWordResult.HotWord> config = SP.getHotWord();
        initHotWord(config);
        history = SP.getHistory();
        if (SP.isLogin())
//            post(Constants.shareConstants().getNetHeaderAdress() + "/config/hotSearches.do", null, null, new FFNetWorkCallBack<HotSearchWordResult>() {
            post(IUrlUtils.Search.hotSearches, null, null, new FFNetWorkCallBack<HotSearchWordResult>() {
                @Override
                public void onSuccess(HotSearchWordResult response, FFExtraParams extra) {
                    initHotWord(response.getConfig());
                    SP.setHotWord(response.getConfig());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    return false;
                }
            });
        adapter = new SearchHistoryAdapter();
        lvSearch.setAdapter(adapter);
        ivClear.setVisibility(View.GONE);
        tvSearch.setText("取消");

        etInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_UP &&
                        keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    searchClick();
                    return true;
                }
                return false;
            }
        });

        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    searchClick();
                    return true;
                }
                return false;
            }
        });

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() == 0) {
                    ivClear.setVisibility(View.GONE);
                    tvSearch.setText("取消");
                } else {
                    tvSearch.setText("搜索");
                    ivClear.setVisibility(View.VISIBLE);
                }
            }
        });

        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FFUtils.setSoftInputVis(etInput, true);
            }
        }, 300);
        etInput.setText(getIntentData().getWord());
        etInput.setSelection(etInput.length());
    }


    public class SearchHistoryAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 1 + (FFUtils.isListEmpty(history) ? 0 : (2 + history.size()));
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            }

            if (position == 1) {
                return 1;
            }

            if (position == getCount() - 1) {
                return 3;
            }

            return 2;
        }

//        View.OnClickListener hotWordClick = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String word = (String) v.getTag();
//                RestSearchResultIntent intent = new RestSearchResultIntent();
//                intent.setKeyWord(word);
//                intent.setType(RestSearchResultIntent.TYPE_Key_WORD);
//                startActivity(RestSearchResultActivity.class, intent);
//                finish();
//            }
//        };

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {
                case 0:
                    return v_hotWord;
                case 1: {
                    if (convertView == null) {
                        convertView = getLayoutInflater().inflate(R.layout.item_search_history_title, parent, false);
                    }
                }
                break;
                case 2: {
                    TextView tvWord;
                    if (convertView == null) {
                        convertView = getLayoutInflater().inflate(R.layout.item_search_history, parent, false);
                        tvWord = (TextView) convertView.findViewById(R.id.tv_word);
                        convertView.setTag(R.id.ff_tag_imageLoader, tvWord);
                    } else {
                        tvWord = (TextView) convertView.getTag(R.id.ff_tag_imageLoader);
                    }
                    tvWord.setText(history.get(position - 2));
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean has = false;
                            String string = history.get(position - 2);
                            for (int i = 0; i < history.size(); i++) {
                                String str = history.get(i);
                                if (str.equals(string)) {
                                    has = true;
                                    history.remove(i);
                                    history.add(0, str);
                                    break;
                                }
                            }
                            if (!has) {
                                history.add(0, string);
                            }
                            SP.setHistory(history);
                            if (isFrom(RestSearchResultActivity.class)) {
                                setResult(10, new Intent().putExtra("word", string));
                            } else {
                                pushEvent103(string, "搜索历史记录");
                                RestSearchResultIntent intent = new RestSearchResultIntent();
                                intent.setKeyWord(string);
                                intent.setType(RestSearchResultIntent.TYPE_Key_WORD);
                                startActivity(RestSearchResultActivity.class, intent);
                            }
                            finish();
                        }
                    });
                    convertView.setTag(history.get(position - 2));
                }
                break;
                case 3: {
                    if (convertView == null) {
                        convertView = getLayoutInflater().inflate(R.layout.item_search_clear_history, parent, false);
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                history.clear();
                                SP.setHistory(history);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
                break;
            }
            return convertView;
        }

    }


    View.OnClickListener hotWordClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HotSearchWordResult.HotWord tag = (HotSearchWordResult.HotWord) v.getTag();
            if (tag.getType() == 1) {
                tag.setId(tag.getCode());
            }
            if (isFrom(RestSearchResultActivity.class)) {
                Intent intent = new Intent();
                intent.putExtra("word", tag);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                HotSearchWordResult.HotWord word = tag;

                pushEvent103(word.getName(), "热门推荐");

                RestSearchResultIntent intent = new RestSearchResultIntent();
                intent.setWord(word);
                intent.setType(RestSearchResultIntent.TYPE_HOT_WORD);
                startActivity(RestSearchResultActivity.class, intent);
                finish();
            }
        }
    };

    private void pushEvent103(String keyword, String enter){
        HashMap<String, String> event = new HashMap<String, String>();
        event.put("account", SP.getUid());
        event.put("channel", Tool.getChannelName(SearchRestActivity.this));
        event.put("city", CityPref.getSelectedCity().getAreaName());
        event.put("keyword", keyword);
        event.put("enter", "搜索历史记录");
        pushEventAction("Yellow_103", event);
    }

    private void initHotWord(List<HotSearchWordResult.HotWord> args) {
        flWordContainer.removeAllViews();
        for (int i = 0; i < args.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.item_hot_word, flWordContainer, false);
            TextView tv = (TextView) ((ViewGroup) view).getChildAt(0);
            tv.setText(args.get(i).getName());
            tv.setTag(args.get(i));
            tv.setOnClickListener(hotWordClick);
            flWordContainer.addView(view);
        }
//        flWordContainer.getLayoutParams().height = flWordContainer.getMeasuredHeight();
    }

    @OnClick({R.id.iv_clear, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                etInput.setText("");
                break;
            case R.id.tv_search:
                this.searchClick();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (etInput.getText().toString().trim().length() == 0) {
            setResult(10, new Intent().putExtra("word", ""));
        }
        super.onBackPressed();
    }


    private void searchClick() {
        if (bResult)
            return;
        bResult = true;
        boolean has = false;
        String string = etInput.getText().toString().trim();
        if (string.length() == 0) {
            finish();
            return;
        } else {
            for (int i = 0; i < history.size(); i++) {
                String str = history.get(i);
                if (str.equals(string)) {
                    has = true;
                    history.remove(i);
                    history.add(0, str);
                    break;
                }
            }
            if (!has) {
                history.add(0, string);
            }
            while (history.size() > 10) {
                history.remove(10);
            }
            SP.setHistory(history);
        }

        if (isFrom(RestSearchResultActivity.class)) {
            if (TextUtils.isEmpty(string)) {
                setResult(10);
            } else {
                setResult(10, new Intent().putExtra("word", string));
            }
        } else {

            pushEvent103(string, "输入关键字");

            RestSearchResultIntent intent = new RestSearchResultIntent();
            intent.setKeyWord(string);
            intent.setType(RestSearchResultIntent.TYPE_Key_WORD);
            startActivity(RestSearchResultActivity.class, intent);
        }
        finish();
    }
}
