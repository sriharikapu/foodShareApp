package com.fengnian.smallyellowo.foodie;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.base.UI.drag_sort.DragSortListView;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateSort.SYReleaseTemplateSort_HalfAllow;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateSort.SYReleaseTemplateSort_NotAllow;

public class SortActivity extends BaseActivity<IntentData> {

    @Bind(R.id.dragSortListView)
    DragSortListView dragSortListView;
    @Bind(R.id.btn_ok)
    Button btnOk;
    private List<SYRichTextPhotoModel> list;

    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    SYRichTextPhotoModel item = list.get(from);

                    list.remove(item);
                    list.add(to, item);
                    adapter.notifyDataSetChanged();
                }
            };
    private MyBaseAdapter<Holder, SYRichTextPhotoModel> adapter;

    @OnClick(R.id.btn_ok)
    public void onClick() {
        SYDataManager.shareDataManager().addDraft(SYDataManager.shareDataManager().draftsOfFirst());
        finish();
    }

    public static class Holder {
        ImageView iv_img;
        TextView tv_content;
        ImageView iv_sort;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        ButterKnife.bind(this);
        setBackVisible(false);
        setTitle("美食编辑");
        DraftModel model = SYDataManager.shareDataManager().draftsOfFirst();
        list = model.getFeed().getFood().getRichTextLists();
        if (list == null) {
            list = new ArrayList<>();
        }

        getMenuContainer().removeAllViews();

        final int sort = model.getFeed().getReleaseTemplate().sort;
        dragSortListView.setHas(sort == SYReleaseTemplateSort_HalfAllow);

        if (sort == SYReleaseTemplateSort_NotAllow) {
            showToast("当前模板不支持排序");
            finish();
            return;
        }

        adapter = new MyBaseAdapter<Holder, SYRichTextPhotoModel>(Holder.class, this, 0, list) {
            @Override
            public void initView(View convertView, Holder holder, int position, SYRichTextPhotoModel item) {
                if (getItemViewType(position) == 0) {//图片
                    FFImageLoader.loadMiddleImage(context(), item.getPhoto().getImageAsset().pullProcessedImageUrl(), holder.iv_img);
//                    holder.tv_content.setText(item_image_gallery.getContent());
                } else if (getItemViewType1(position) == 1) {
                    if (sort == SYReleaseTemplateSort_HalfAllow && position == 0) {
                        holder.iv_sort.setVisibility(View.INVISIBLE);
                    } else {
                        holder.iv_sort.setVisibility(View.VISIBLE);
                    }
                    holder.tv_content.setText(item.getContent());
                }
            }

            @Override
            public int getViewTypeCount1() {
                return 2;
            }

            @Override
            public int getItemViewType1(int position) {
                return getItem(position).getPhoto() == null ? 1 : 0;
            }

            @Override
            public int getItemViewId(int position) {
                if (getItemViewType1(position) == 0) {
                    return R.layout.item_sort_img;
                } else {
                    return R.layout.item_sort_text;
                }
            }
        };
        dragSortListView.setDropListener(onDrop);
        dragSortListView.setAdapter(adapter);
    }
}
