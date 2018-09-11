package View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

public class RoundBlurView extends RealtimeBlurView {
    private RectF mRectF;
    private Paint mPaint;

    public RoundBlurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRectF = new RectF();
        mPaint = new Paint();
    }

    @Override
    protected void drawBlurredBitmap(Canvas canvas, Bitmap blurBitmap, int overlayColor, float roundCornerRadius) {

        mRectF.right = getMeasuredWidth();
        mRectF.bottom = getMeasuredHeight();
        mPaint.reset();
        mPaint.setAntiAlias(true);
        BitmapShader shader = new BitmapShader(blurBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.postScale(mRectF.width() / blurBitmap.getWidth(), mRectF.height() / blurBitmap.getHeight());
        shader.setLocalMatrix(matrix);
        mPaint.setShader(shader);
        canvas.drawCircle(200,200, 200, mPaint);

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(overlayColor);
        canvas.drawCircle(200,200, 200, mPaint);
//        canvas.drawCircle(mRectF, roundCornerRadius, roundCornerRadius, mPaint);
    }
}
