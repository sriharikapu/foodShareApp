package com.fan.framework.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;

import java.lang.reflect.Field;
import java.util.List;

public abstract class MyBaseAdapterLoadMore<H, T> extends BaseAdapter {

	@SuppressWarnings("rawtypes")
	private Class clazz;

	private List<T> data;

	private boolean loadMore;

	private boolean hasMore = true;

	protected Activity activity;

	protected LayoutInflater inflater;

	private int layoutId;

	private boolean loading = false;
	private boolean loadingFailed = false;

	public void loadFaile() {
		loading = false;
		loadingFailed = true;
	}

	public void loadSuccess() {
		loading = false;
		loadingFailed = false;
	}

	public void reset() {
		if (data != null) {
			data.clear();
		}
		loading = false;
		loadingFailed = false;
		hasMore = false;
	}

	public MyBaseAdapterLoadMore(Activity activity, Class<? extends H> clazz, int layoutId) {
		this.clazz = clazz;
		this.activity = activity;
		this.layoutId = layoutId;
		this.inflater = activity.getLayoutInflater();
	}

	public MyBaseAdapterLoadMore(Activity activity, Class<? extends H> clazz, int layoutId, List<T> data) {
		this(activity, clazz, layoutId);
		this.data = data;
	}

	@Override
	public T getItem(int position) {
		return position < data.size() ? data.get(position) : null;
	}

	@Override
	public int getCount() {
		return (data == null ? 0 : data.size()) + (loadMore ? 1 : 0);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public final int getItemViewType(int position) {
		return position == getCount() - 1 && loadMore ? getViewTypeCount() - 1 : getItemViewType1(position);
	}

	public int getItemViewType1(int position) {
		return 0;
	}

	@Override
	public final int getViewTypeCount() {
		return getViewTypeCount1() + (loadMore ? 1 : 0);
	}

	public int getViewTypeCount1() {
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (loadMore && position == getCount() - 1) {
			if (!loadingFailed) {
				loadMore1();
			}
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ff_item_bottom_more, parent, false);
			}
			TextView tv = (TextView) ((ViewGroup) convertView).getChildAt(0);
			tv.setText(loadingFailed ? "加载失败,点击重试..." : (hasMore ? "正在加载..." : "没有更多"));
			if (loadingFailed) {
				tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						loadMore1();
					}
				});
			} else {
				tv.setOnClickListener(null);
			}
			return convertView;
		}
		H holder = null;
		if (convertView == null) {
			try {
				holder = (H) clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			int id = getItemViewId(position);
			convertView = inflater.inflate(id <= 0 ? layoutId : id, parent, false);
			Field[] t = clazz.getDeclaredFields();
			for (Field field : t) {
				Class<?> type = field.getType();
				boolean assignableFrom = View.class.isAssignableFrom(type);
				if (assignableFrom) {
					field.setAccessible(true);
					try {
						field.set(holder, convertView.findViewById(activity.getResources().getIdentifier(field.getName(), "id", activity.getPackageName())));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}
			onGetView(holder);
			convertView.setTag(holder);
		} else {
			holder = (H) convertView.getTag();
		}
		initView(holder, position, getItem(position));
		return convertView;
	}

	private void loadMore1() {
		if (!loading && hasMore) {
			loadingFailed = false;
			loading = true;
			loadMore();
			notifyDataSetChanged();
		}
	}

	public void loadMore() {
	}

	public int getItemViewId(int position) {
		return 0;
	}

	public void onGetView(H holder) {
	}

	public abstract void initView(H holder, int position, T item);

	public List<T> getData() {
		return data;
	}

	public void setDataWithNotify(List<T> data) {
		setData(data);
		notifyDataSetChanged();
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public void addDataWithNotify(List<T> data) {
		addData(data);
		notifyDataSetChanged();
	}

	public void addData(List<T> data) {
		if (data == null || data.isEmpty()) {
			return;
		}
		if (this.data == null) {
			this.data = data;
		} else {
			this.data.addAll(data);
		}
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

	public boolean isLoadMore() {
		return loadMore;
	}

	public void setLoadMore(boolean loadMore) {
		this.loadMore = loadMore;
	}

}
