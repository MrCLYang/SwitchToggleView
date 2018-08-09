package com.example.yangchenglei.twelfday_switchtoggleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;

/**
 * 作者：杨成雷
 * 时间：2018/8/6:14:30
 */
public class SwitchToggleView extends View {

    private final static int STATE_NONE = 0;
    private final static int STATE_DOWN = 1;
    private final static int STATE_MOVE = 2;
    private final static int STATE_UP = 3;
    private Bitmap mSwitchBackground;
    private Bitmap mSwitchSlide;
    private Paint mPaint;
    private int mState = STATE_NONE;//用来标记状态

    private boolean isOpened = true;//默认是关闭的
    private float currentx;
    private int slideWidth;
    private float left;
    private float top;
    private OnSwitchListenter mListenter;


    public SwitchToggleView(Context context) {
        super(context, null);
    }

    public SwitchToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwitchBackground(int id) {
        mSwitchBackground = BitmapFactory.decodeResource(getResources(), id);
    }

    public void setSwitchSlide(int id) {
        mSwitchSlide = BitmapFactory.decodeResource(getResources(), id);
    }

    //
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置自己的大小
        if (mSwitchBackground != null) {
            int height = mSwitchBackground.getHeight();
            int width = mSwitchBackground.getWidth();
            setMeasuredDimension(width, height);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景的显示

        if (mSwitchBackground != null) {
            left = 0;
            top = 0;
            canvas.drawBitmap(mSwitchBackground, left, top, mPaint);
        }
        if (mSwitchSlide == null) {
            return;
        }

        slideWidth = mSwitchSlide.getWidth();
        int switchWidth = mSwitchBackground.getWidth();
        switch (mState) {
            case STATE_DOWN:
            case STATE_MOVE:
                //当我们按下down的时候
                //如果滑块是关闭的时候，点击滑块左侧没效果，点击右侧的时候，滑块的中线和按下的x坐标对齐
                if (!isOpened) {
                    if (currentx < slideWidth / 2) {
                        //绘制在左侧
                        canvas.drawBitmap(mSwitchSlide, 0, 0, mPaint);
                    } else {
                        //点击滑块的右侧，滑块的中线和按下的x坐标对齐
                        float left = currentx - slideWidth / 2f;
                        float maxLeft = switchWidth - slideWidth;
                        if (left > maxLeft) {
                            left = maxLeft;
                        }
                        canvas.drawBitmap(mSwitchSlide, left, 0, mPaint);
                    }

                } else {
                    //如果滑块是打开的
                    float middle = switchWidth - slideWidth / 2f;
                    if (currentx > middle) {
                        //绘制为打开
                        canvas.drawBitmap(mSwitchSlide, switchWidth - slideWidth, 0, mPaint);
                    } else {
                        float left = currentx - slideWidth / 2f;
                        if (left < 0) {
                            left = 0;
                        }
                        canvas.drawBitmap(mSwitchSlide, left, 0, mPaint);
                    }

                }
                break;
            case STATE_UP:
                //是否是打开还是关闭
                if (!isOpened) {
                    canvas.drawBitmap(mSwitchSlide, left, 0, mPaint);
                } else {
                    canvas.drawBitmap(mSwitchSlide, switchWidth - slideWidth, 0, mPaint);
                }
                break;
            case STATE_NONE:
                //绘制滑块,空状态
                if (!isOpened) {
                    canvas.drawBitmap(mSwitchSlide, left, 0, mPaint);
                } else {
                    canvas.drawBitmap(mSwitchSlide, switchWidth - slideWidth, 0, mPaint);
                }
                break;
            default:
                break;

        }


        super.onDraw(canvas);
    }
    //view的行为操作

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mState = STATE_DOWN;
                currentx = event.getX();
                invalidate();//触发刷新--主线程调用
                //postInvalidate();//触发刷新--子线程调用
                break;
            case MotionEvent.ACTION_UP:
                mState = STATE_UP;
                //判断状态的改变的中轴值
                currentx = event.getX();
                int slideWidth = mSwitchSlide.getWidth();
                int switchWidth = mSwitchBackground.getWidth();
                if (switchWidth / 2f > currentx && isOpened) {
                    //关闭状态
                    isOpened = false;
                    if (mListenter != null) {
                        mListenter.onSwitchChanged(isOpened);
                    }

                } else if (switchWidth / 2f <= currentx && !isOpened) {
                    isOpened = true;
                    if (mListenter != null) {
                        mListenter.onSwitchChanged(isOpened);
                    }
                }

                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                mState = STATE_MOVE;
                currentx = event.getX();
                invalidate();

                break;
            default:
                break;

        }

        //设置为true 就是只消费touch事件
        return true;
    }

    public void setOnSwitchListener(OnSwitchListenter listener) {
        this.mListenter = listener;
    }

    public interface OnSwitchListenter {
        //开关状态改变
        void onSwitchChanged(boolean isOpened);
    }
}
