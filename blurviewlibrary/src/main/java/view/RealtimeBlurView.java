package view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import com.lyd.jk.blurviewlibrary.R;

import util.FastBlur;


/**
 * 指定区域内的高斯模糊简单实现  参考github开源项目RealtimeBlurView
 * 只是实现不同
 * created by lyd 2018/09/04
 */
public class RealtimeBlurView extends View {

    private static final String TAG = "RealtimeBlurView2";

    private int mOverlayColor; // default #aaffffff
    private float mBlurRadius; // default 10dp (0 < r <= 25)
    private float mScaleFractor;//
    private float mRoundCornerRadius;

    private Bitmap mBlurredBitmap;
    private Bitmap mBlurringBitmap;
    private Canvas mBlurringCanvas;

    private Paint mPaint;
    private BlurPreDrawListener mBlurPreDrawListener;

    private View mTargetView;

    private Rect mRectSrc, mRectDst;


    public RealtimeBlurView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RealtimeBlurView);
        mBlurRadius = a.getFloat(R.styleable.RealtimeBlurView_realtimeBlurRadius,8);
        mOverlayColor = a.getColor(R.styleable.RealtimeBlurView_realtimeOverlayColor, 0x04000000);
        mScaleFractor = a.getFloat(R.styleable.RealtimeBlurView_realtimeDownsampleFactor,12);
        mRoundCornerRadius=a.getDimension(R.styleable.RealtimeBlurView_realtimeBlurRoundCornerRadius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics()));
        a.recycle();

        mPaint = new Paint();
        mRectSrc = new Rect();
        mRectDst = new Rect();
        mPaint.setColor(mOverlayColor);
        mBlurPreDrawListener = new BlurPreDrawListener();

    }

    public RealtimeBlurView bindView(View targetView) {
        mTargetView = targetView;
        post(new Runnable() {
            @Override
            public void run() {
                mTargetView.getViewTreeObserver().addOnPreDrawListener(mBlurPreDrawListener);
            }
        });
        return this;
    }

    private class BlurPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        @Override
        public boolean onPreDraw() {
            if (canBlur() && prepare()) {
                int[] targetLocation = new int[2];
                getLocationOnScreen(targetLocation);
                int saveC = mBlurringCanvas.save();//少了这个判断不出效果
                try {
                    mBlurringCanvas.scale(1 / mScaleFractor, 1 / mScaleFractor);
                    mBlurringCanvas.translate(-targetLocation[0],-targetLocation[1]);
                    mTargetView.draw(mBlurringCanvas);
                }catch (RuntimeException e){

                }finally {
                    mBlurringCanvas.restoreToCount(saveC);//少了这个判断不出效果
                }
                mBlurredBitmap = FastBlur.doBlur(mBlurringBitmap, (int) mBlurRadius,true);
            }

            return true;
        }
    }

    protected boolean prepare() {//实时更新关键代码，可参考上面错误代码
        if (mBlurRadius == 0) {
            return false;
        }

        if (mBlurringBitmap == null) {//少了这个判断不出效果
            mBlurringBitmap = Bitmap.createBitmap((int) (getMeasuredWidth() / mScaleFractor),
                (int) (getMeasuredHeight() / mScaleFractor), Bitmap.Config.RGB_565);
        }
        if (mBlurringBitmap == null) {
            return false;
        }
        if (mBlurringCanvas==null) {
            mBlurringCanvas = new Canvas(mBlurringBitmap);
        }
        if (mBlurredBitmap==null) {
            mBlurredBitmap = Bitmap.createBitmap((int) (getMeasuredWidth() / mScaleFractor),
                (int) (getMeasuredHeight() / mScaleFractor), Bitmap.Config.RGB_565);
        }

        if (mBlurredBitmap == null) {
            return false;
        }
        return true;
    }


    //为性能上考虑，这个值根据实际逻辑开启，默认开启
    protected boolean canBlur(){
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBlurredBitmap==null){
            return;
        }
        drawBlurredBitmap(canvas, mBlurredBitmap, mOverlayColor,mRoundCornerRadius);
    }

    protected void drawBlurredBitmap(Canvas canvas, Bitmap blurredBitmap, int overlayColor,float roundCornerRadius) {
        if (blurredBitmap != null) {
            mRectSrc.right = blurredBitmap.getWidth();
            mRectSrc.bottom = blurredBitmap.getHeight();
            mRectDst.right = getWidth();
            mRectDst.bottom = getHeight();
            canvas.drawBitmap(blurredBitmap, mRectSrc, mRectDst, null);
        }
        mPaint.setColor(overlayColor);
        canvas.drawRect(mRectDst, mPaint);
    }


    protected View getTargetView() {
        Context ctx = getContext();
        for (int i = 0; i < 4 && ctx != null && !(ctx instanceof Activity) && ctx instanceof ContextWrapper; i++) {
            ctx = ((ContextWrapper) ctx).getBaseContext();
        }
        if (ctx instanceof Activity) {
            return ((Activity) ctx).getWindow().getDecorView();
        } else {
            return null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        mTargetView = getTargetView();
        super.onAttachedToWindow();
        if (mBlurPreDrawListener != null && mTargetView != null) {
            mTargetView.getViewTreeObserver().addOnPreDrawListener(mBlurPreDrawListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBlurPreDrawListener != null && mTargetView != null) {
            mTargetView.getViewTreeObserver().removeOnPreDrawListener(mBlurPreDrawListener);
        }
    }


}
