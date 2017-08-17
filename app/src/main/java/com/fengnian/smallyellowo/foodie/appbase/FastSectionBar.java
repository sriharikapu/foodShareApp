package com.fengnian.smallyellowo.foodie.appbase;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;

import com.fengnian.smallyellowo.foodie.R;

/**
 * Created by lanbiao on 16/8/15.
 */
public class FastSectionBar  extends View{

    private static final char[] WORDS = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z','#'
    };

    //字大小
    private int wordSize = 12;
    private  int preHeight = wordSize + 2;

    private ListView mListView;
    private FastSection mSectionIndex;

    //垂直顶部位置
    private int mVCenterTop;
    private Paint mPaint;


    public FastSectionBar(Context context) {
        super(context);
        init();
    }

    public FastSectionBar(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public FastSectionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int convertDipToPixel(float dip,Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dip * (metrics.densityDpi / 160f) + 0.5);
        return px;
    }

    private void init(){
        wordSize = convertDipToPixel(wordSize,getContext());
        preHeight = convertDipToPixel(preHeight,getContext());

        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.index_word));
        mPaint.setTextSize(preHeight);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
    }

    public void setListView(ListView listView){
        mListView = listView;
        Adapter adapter = mListView.getAdapter();
        if(adapter == null || !(adapter instanceof FastSection))
            throw new RuntimeException("ListView must set Adapter or Adapter must implements Indexer interface");
        mSectionIndex = (FastSection) adapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mVCenterTop = (int) (getMeasuredHeight() / 2f - (preHeight * WORDS.length) / 2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < WORDS.length; i++){
            float xPosition = getMeasuredWidth() / 2f; //getWidth() / 2f - mPaint.measureText(String.valueOf(WORDS[i])) / 2f;
            float yPosition = (i + 1) * preHeight + mVCenterTop;
            canvas.drawText(String.valueOf(WORDS[i]),xPosition,yPosition,mPaint);
            //canvas.drawText(String.valueOf(WORDS[i]),getMeasuredHeight()/2,getMeasuredHeight()/2,mPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int startY = (int) event.getY() - mVCenterTop;
        int index = startY / preHeight;
        if(index >= WORDS.length){
            index = WORDS.length - 1;
        }else if(index < 0){
            index = 0;
        }

        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
            int position = mSectionIndex.getStartPositionOfSection(String.valueOf(WORDS[index]));
            if(position == -1)
                return true;

            mListView.setSelection(position);
        }
        return super.onTouchEvent(event);
    }
}
