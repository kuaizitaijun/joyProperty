package com.joy.property.shop.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * 只为顶部ScrollView使用
 * 如果使用了其它的可拖拽的控件，请仿照该类实现isAtBottom方法
 */
public class CustScrollView2 extends ScrollView {
    private static final int TOUCH_IDLE = 0;
    private static final int TOUCH_INNER_CONSIME = 1; // touch事件由ScrollView内部消费
    private static final int TOUCH_DRAG_LAYOUT = 2; // touch事件由上层的DragLayout去消费

    boolean isAtBottom; // 按下的时候是否在底部
    private int mTouchSlop = 4; // 判定为滑动的阈值，单位是像素
    private int scrollMode;
    private float downY;
   private boolean isUp=true;
    private int lastPosition;
    public CustScrollView2(Context arg0) {
        this(arg0, null);
    }

    public CustScrollView2(Context arg0, AttributeSet arg1) {
        this(arg0, arg1, 0);
    }

    public CustScrollView2(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //子View一定要Clickable才行，否则onInterceptTouchEvent工作不按正常来
        getChildAt(0).setClickable(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = ev.getRawY();
            isAtBottom = isAtBottom();
            scrollMode = TOUCH_IDLE;
        if (getScrollY()==0){
            getParent().requestDisallowInterceptTouchEvent(false);
        }else
            getParent().requestDisallowInterceptTouchEvent(true);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
        if (getScrollY()==0&&ev.getRawY()-downY>0){
            getParent().requestDisallowInterceptTouchEvent(false);
        }else
            getParent().requestDisallowInterceptTouchEvent(true);
         downY=ev.getRawY();
        }

        return super.onInterceptTouchEvent(ev);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (scrollMode == TOUCH_DRAG_LAYOUT) {
//            return false;
//        }
     //   return super.onTouchEvent(ev);
   // }

    private boolean isAtBottom() {
        return getScrollY() + getMeasuredHeight() >= computeVerticalScrollRange() - 2;
    }


}
