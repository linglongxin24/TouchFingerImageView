package cn.bluemobi.dylan.touchfingerimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * 自定义仿去哪儿手指按下图片缩放和显示指纹的效果
 * Created by yuandl on 2016-10-31.
 */

public class TouchFingerImageView extends ImageView {

    /**
     * 指纹的图片
     */
    private Bitmap fingerBitmap;
    /**
     * 图片按下的状态标识
     */
    private boolean state = false;
    /**
     * 点击事件
     */
    private OnClickListener onClickListener;

    /**
     * 默认的构造函数
     *
     * @param context
     * @param attrs
     */
    public TouchFingerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**获取指纹图片*/
        fingerBitmap = zoom(BitmapFactory.decodeResource(getResources(), R.mipmap.finger), 300, 300);

    }

    /**
     * 图片的缩放方法
     *
     * @param bitmap    源图片资源
     * @param newWidth  缩放后的宽
     * @param newHeight 缩放后的高
     * @return Bitmap    缩放后的图片资源
     */
    public Bitmap zoom(Bitmap bitmap, int newWidth, int newHeight) {
        // 获取这个图片的宽和高
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        // 计算宽高缩放率
        float rateWidth = ((float) newWidth) / width;
        float rateHeight = ((float) newHeight) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(rateWidth, rateHeight);
        //创建一个新的缩放后的bitmap
        Bitmap zoomBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width,
                (int) height, matrix, true);
        return zoomBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**获取源资源图片文件**/
        Bitmap bitmap = ((BitmapDrawable) this.getDrawable()).getBitmap();
        Matrix matrix0 = new Matrix();
        /**
         * 平移指纹图片使指纹居中显示
         */
        matrix0.postTranslate(this.getWidth() / 2 - fingerBitmap.getWidth() / 2,
                this.getHeight() / 2 - fingerBitmap.getHeight() / 2);
        /**绘制源资源图片文件**/
        canvas.drawBitmap(zoom(bitmap, getWidth(), getHeight()), 0, 0, null);
        if (state) {
            Matrix matrix = new Matrix();
            /**
             * 平移指纹图片使指纹居中显示
             */
            matrix.postTranslate(this.getWidth() / 2 - fingerBitmap.getWidth() / 2,
                    this.getHeight() / 2 - fingerBitmap.getHeight() / 2);
            canvas.drawBitmap(fingerBitmap, matrix, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float begin = 1.0f;
        float end = 0.95f;
        /** 收缩动画**/
        Animation beginAnimation = new ScaleAnimation(begin, end, begin, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        /** 伸展动画**/
        Animation finishAnimation = new ScaleAnimation(end, begin, end, begin,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        /** 设置动画持续时间和保留动画结果 **/
        beginAnimation.setDuration(200);
        /**设置动画停留在最后一个的状态**/
        beginAnimation.setFillAfter(true);
        finishAnimation.setDuration(200);
        finishAnimation.setFillAfter(true);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://手指按下时
                startAnimation(beginAnimation);
                state = true;
                invalidate();
                if (onClickListener != null) {
                    onClickListener.onClick(this);
                }
                break;
            case MotionEvent.ACTION_UP:
                startAnimation(finishAnimation);
                state = false;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                startAnimation(finishAnimation);
                state = false;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
