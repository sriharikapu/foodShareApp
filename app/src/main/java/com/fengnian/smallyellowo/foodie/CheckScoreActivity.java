package com.fengnian.smallyellowo.foodie;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.Adapter.CheckScoreAdapter;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.JifenBean;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.bean.results.ConfigResult;
import com.fengnian.smallyellowo.foodie.bean.results.PublishLimitResult;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.intentdatas.CheckScoreIntent;
import com.fengnian.smallyellowo.foodie.widgets.MyListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.fengnian.smallyellowo.foodie.appbase.SP.getConfig;

/**
 * Created by Administrator on 2016-10-18.
 */

public class CheckScoreActivity extends BaseActivity<CheckScoreIntent> implements View.OnClickListener {

    private TextView tv_score, tv_next_say, tv_prefect_content, tv_next, tv_every_prompt,
            tv_this_prompt, tv_not_prompt, tv_name, tv_item_score, tv_6, tv_9;
    private RelativeLayout rl_1, rl_head, rl_2, rl_4;

    private ListView lv_listview;
    int tv_every_prompt_flag = 1;
    int tv_this_prompt_flag = 1;
    int tv_not_prompt_flag = 1;
    Drawable img_slector, img_not_selector;
    CheckScoreAdapter adapter;
//    List<String> list;

    double Score;
    ConfigResult result;
    List<JifenBean.RichFeedBean.ListBean1> hang_list;

    int flag = 0;
    private List<Integer> mlist;

    private int tag = 0;

    private PublishLimitResult limitResult;
    String isRichText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_check_score);
        setNotitle(true);
        getContainer().setBackgroundColor(0);
        limitResult = getIntentData().getResult();
        isRichText = getIntentData().getIsRichText();
        rl_head = (RelativeLayout) findViewById(R.id.rl_head);
        rl_head.setOnClickListener(this);

        rl_1 = (RelativeLayout) findViewById(R.id.rl_1);
        rl_1.setOnClickListener(this);
        tv_score = (TextView) findViewById(R.id.tv_score);
        rl_2 = (RelativeLayout) findViewById(R.id.rl_2);
        lv_listview = (MyListView) findViewById(R.id.lv_listview);

        rl_4 = (RelativeLayout) findViewById(R.id.rl_4);
        tv_9 = (TextView) findViewById(R.id.tv_9);

        tv_6 = (TextView) findViewById(R.id.tv_6);


        tv_next_say = (TextView) findViewById(R.id.tv_next_say);
        tv_prefect_content = (TextView) findViewById(R.id.tv_prefect_content);
        tv_next_say.setOnClickListener(this);
        tv_prefect_content.setOnClickListener(this);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_next.setOnClickListener(this);
        tv_every_prompt = (TextView) findViewById(R.id.tv_every_prompt);
        tv_this_prompt = (TextView) findViewById(R.id.tv_this_prompt);
        tv_not_prompt = (TextView) findViewById(R.id.tv_not_prompt);

        tv_every_prompt.setOnClickListener(this);
        tv_this_prompt.setOnClickListener(this);
        tv_not_prompt.setOnClickListener(this);

        img_slector = this.getResources().getDrawable(R.mipmap.new_duihao);
        img_not_selector = this.getResources().getDrawable(R.mipmap.new_quanquan);
        adapter = new CheckScoreAdapter(this, sum_list, list_bean5);
        lv_listview.setAdapter(adapter);
        if (limitResult.getPublishLimit().getLimitScore() == 0) {
            getScoreResult();
            DecimalFormat df = new DecimalFormat("#.#");
            double score = Double.parseDouble(df.format(Score));
            if (score == 0.0) {
                tv_score.setText("0");//获取的积分
            } else {
                tv_score.setText(score + "");
            }
            if (Score - all_score(hang_list) < 0) {
                lv_listview.setVisibility(View.VISIBLE);
                tv_next.setVisibility(View.GONE);
                tv_next_say.setVisibility(View.VISIBLE);
                tv_prefect_content.setVisibility(View.VISIBLE);
            } else {
                tv_next.setVisibility(View.VISIBLE);
                tv_next_say.setVisibility(View.GONE);
                tv_prefect_content.setVisibility(View.GONE);
                lv_listview.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        } else {
            tv_score.setText("0");
            tv_6.setVisibility(View.GONE);
            rl_4.setVisibility(View.VISIBLE);
            tv_9.setText(limitResult.getPublishLimit().getMsg());
            tv_next.setVisibility(View.VISIBLE);
            tv_next_say.setVisibility(View.GONE);
            tv_prefect_content.setVisibility(View.GONE);
            lv_listview.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }

        mlist = new ArrayList<>();

        img_slector.setBounds(0, 0, img_slector.getMinimumWidth(), img_slector.getMinimumHeight());
        img_not_selector.setBounds(0, 0, img_not_selector.getMinimumWidth(), img_not_selector.getMinimumHeight());
        hidesoft();//主动隐藏键盘
        if (adapter.getCount() > 0) {
            tv_6.setVisibility(View.VISIBLE);
        } else {
            tv_6.setVisibility(View.GONE);
        }
        setOnePrompt();
    }


    void hidesoft() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void setOnePrompt() {


        tv_every_prompt.setCompoundDrawables(img_slector, null, null, null);

        tv_this_prompt.setCompoundDrawables(img_not_selector, null, null, null);

        tv_not_prompt.setCompoundDrawables(img_not_selector, null, null, null);
        tag = 0;
    }


    private void setTwoPrompt() {
        tv_every_prompt.setCompoundDrawables(img_not_selector, null, null, null);

        tv_this_prompt.setCompoundDrawables(img_slector, null, null, null);

        tv_not_prompt.setCompoundDrawables(img_not_selector, null, null, null);
        tag = 1;
    }


    private void setThreePrompt() {
        tv_every_prompt.setCompoundDrawables(img_not_selector, null, null, null);

        tv_this_prompt.setCompoundDrawables(img_not_selector, null, null, null);

        tv_not_prompt.setCompoundDrawables(img_slector, null, null, null);
        tag = 2;
    }

    private double all_score(List<JifenBean.RichFeedBean.ListBean1> list) {
        double all_score = 0;
        for (int b = 0; b < list.size(); b++) {
            all_score += list.get(b).getScore();
        }

        return all_score;

    }

    private int mon;
    boolean text_describe;
    boolean hotel_name;
    boolean people_num;
    boolean total_price;
    boolean have_food_style;//是否存在就餐类型
    boolean have_comment_tag_and_dishes;//否存在评价标签和菜品
    boolean have_cover_title;//是否存在封面标题
    boolean have_cover_image;
    boolean have_comment_score; //是否存在评分

    String food_type; //是否选择 餐的

    private boolean fast_text_describe;//快速编辑文字描述

    private void getScoreResult() {
        DraftModel model = FastEditActivity.draft != null ? FastEditActivity.draft : SYDataManager.shareDataManager().draftsOfFirst();
        mon = model.allImageCount();//图片张数//
        text_describe = model.haveWordsDescription(); //;// 是否存在文字描述
        fast_text_describe = model.fasthaveWordsDescription();//快速编辑的是否有描述
        hotel_name = model.haveRestaurantName(); //;//是否存在餐厅名字
        people_num = model.foodPeopleCount();//是否存在就餐人数
        total_price = model.totalPrice();//是否存在总价
        have_food_style = model.haveFoodStyle();//是否存在就餐类型
        have_comment_tag_and_dishes = model.haveCommentTagAndDishes();//否存在评价标签和菜品
        have_cover_image = model.haveCoverImage();
        have_cover_title = model.haveCoverTitle();//是否存在封面标题
        have_comment_score = model.haveCommentScorce(); //是否存在美食评分
        result = getInstanceConfig();
        JifenBean.RichFeedBean richfeed;
        if ("Yes".equals(isRichText))
            richfeed = result.getConfig().getJifen().getRichFeed();
        else richfeed = result.getConfig().getJifen().getShortFeed();

        hang_list = richfeed.getList();
        if (hang_list == null)
            hang_list = new ArrayList<>();

        int length_hang = hang_list.size();
        for (int i = 0; i < length_hang; i++) {
            JifenBean.RichFeedBean.ListBean1 bean1 = hang_list.get(i);
            List<JifenBean.RichFeedBean.ListBean1.ListBean2> list_bean2 = bean1.getList();
            if (getValue(list_bean2)) {
                Score += bean1.getScore();
            } else {
                Sum_Target_bean sum_bean = new Sum_Target_bean();
                sum_bean.mmmmmlist = new ArrayList<>();
                int length_bean2 = list_bean2.size();
                for (int k = 0; k < length_bean2; k++) {
                    JifenBean.RichFeedBean.ListBean1.ListBean2 bean2 = list_bean2.get(k);
                    int code = bean2.getItemCode();
                    if (!getIsCode(code)) {
                        Target_bean target_bean = new Target_bean();
                        target_bean.setContent(getBean(code).getContent() + "  ");
                        sum_bean.mmmmmlist.add(target_bean);
                        sum_bean.setScore(bean1.getScore());
                    }
                }
                sum_list.add(sum_bean);
            }
        }
    }

    List<Sum_Target_bean> sum_list = new ArrayList<>();


    public class Sum_Target_bean {
        private double score;
        private double id;
        private List<Target_bean> mmmmmlist;

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public double getId() {
            return id;
        }

        public void setId(double id) {
            this.id = id;
        }

        public List<Target_bean> getMmmmmlist() {
            return mmmmmlist;
        }

        public void setMmmmmlist(List<Target_bean> mmmmmlist) {
            this.mmmmmlist = mmmmmlist;
        }
    }

    public class Target_bean {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    /**
     * 某一行是否内容是否得分
     *
     * @param list
     * @return
     */
    private boolean getValue(List<JifenBean.RichFeedBean.ListBean1.ListBean2> list) {
        boolean flag = true;
        int len = list.size();
        for (int i = 0; i < len; i++) {
            JifenBean.RichFeedBean.ListBean1.ListBean2 bean2 = list.get(i);
            flag = getIsCode(bean2.getItemCode());
            if (!flag) {
                break;
            }
        }
        return flag;
    }

    List<JifenBean.ItemListBean5> list_bean5 = new ArrayList<>();

    /**
     * 某一行的单个内容是否满足
     *
     * @param code
     * @return
     */
    private boolean getIsCode(int code) {
        if (code == 1) {
            if ((mon - Integer.valueOf(getBean(code).getMinValue())) >= 0) {
                return true;
            }
        } else if (code == 2) {
            if ("Yes".equals(isRichText))
                return text_describe;
            else return fast_text_describe;
        } else if (code == 3) {
            return hotel_name;
        } else if (code == 4) {
            return people_num;
        } else if (code == 5) {
            return total_price;
        } else if (code == 6) {
            return have_food_style;
        } else if (code == 7) {
            return have_comment_tag_and_dishes;
        } else if (code == 8) {
            return have_comment_tag_and_dishes;
        } else if (code == 9) {
            return have_cover_image;
        } else if (code == 10) {
            return have_cover_title;
        } else if (code == 11) {
            return have_comment_score;
        }
        return false;
    }

    /**
     * 获取需要的集合的对象
     *
     * @param Code
     * @return
     */
    private JifenBean.ItemListBean5 getBean(int Code) {
//        ConfigResult result = getConfig();
        List<JifenBean.ItemListBean5> list = getInstanceConfig().getConfig().getJifen().getItemList();
        for (int i = 0; i < list.size(); i++) {
            JifenBean.ItemListBean5 bean5 = list.get(i);
            int tartget_code = bean5.getItemCode();
            if (Code == tartget_code) return bean5;
        }
        return null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_head:
                finish();
                break;
            case R.id.rl_1:
                //nothing
                break;
            case R.id.tv_next_say:  //下次 再说
            {
                boolean bThisPrompt = true;
                if (tag == 0)
                    bThisPrompt = true;
                else if (tag == 1)
                    bThisPrompt = false;
                else if (tag == 2)
                    bThisPrompt = false;

//                if("Yes".equals(isRichText)) {
                setResult(RESULT_OK, new Intent().putExtra("bThisPrompt", bThisPrompt));
                finish();
//                }else{
//
//                    setResult(RESULT_OK, new Intent().putExtra("bThisPrompt", bThisPrompt));
//                    finish();
//                }
                break;
            }
            case R.id.tv_prefect_content: //完善内容
                finish();
                break;
            case R.id.tv_next: {
                //提交请求
                boolean bThisPrompt = true;
                if (tag == 0)
                    bThisPrompt = true;
                else if (tag == 1)
                    bThisPrompt = false;
                else if (tag == 2)
                    bThisPrompt = false;
                setResult(RESULT_OK, new Intent().putExtra("bThisPrompt", bThisPrompt));
                finish();
                break;
            }
            case R.id.tv_every_prompt:
                //每次提示
                //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                setOnePrompt();
                SP.setCheckScore(true);
                break;
            case R.id.tv_this_prompt:
                //本次不再提示
                setTwoPrompt();
                SP.setCheckScore(true);
                break;
            case R.id.tv_not_prompt:
                //永不提示
                setThreePrompt();
                SP.setCheckScore(false);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0,0);
    }

    private ConfigResult getInstanceConfig() {
        if (result == null) {
            result = getConfig();
        }
        return result;
    }
}
