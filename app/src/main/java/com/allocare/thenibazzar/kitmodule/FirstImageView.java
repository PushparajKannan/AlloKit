package com.allocare.thenibazzar.kitmodule;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.allocare.thenibazzar.R;

public class FirstImageView extends ImageView {

    private Path mMaskPath;
    private Paint mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int radiusConer=50;
    private int leftCorner=0;
    private int rightCorner=0;
    private int topCorner=0;
    private int bottomCorner=0;

    public FirstImageView(Context context) {
        super(context);
    }

    public FirstImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //firstCall(context,attrs);

    }

    public FirstImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // firstCall(context,attrs);

    }

    public FirstImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //  firstCall(context,attrs);
    }

    void firstCall(Context context,AttributeSet attrs)
    {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FirstImageView, 0, 0);


            //  strokeColor = a.getColor(R.styleable.CircleImageView_strokeColor, Color.TRANSPARENT);
            //  strokeWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_strokeWidth, 0);
            // highlightEnable = a.getBoolean(R.styleable.CircleImageView_highlightEnable, true);
            // highlightColor = a.getColor(R.styleable.CircleImageView_highlightColor, DEF_PRESS_HIGHLIGHT_COLOR);
            radiusConer = (int) a.getDimension(R.styleable.FirstImageView_corner,0);
            leftCorner = (int) a.getDimension(R.styleable.FirstImageView_corner,0);
            radiusConer = (int) a.getDimension(R.styleable.FirstImageView_corner,0);
            radiusConer = (int) a.getDimension(R.styleable.FirstImageView_corner,0);
            radiusConer = (int) a.getDimension(R.styleable.FirstImageView_corner,0);
            a.recycle();
            // setCornerRadius(radiusConer);
        }
    }


    /**
     * Set the corner radius to use for the RoundedRectangle.
     */
   /* public void setCornerRadius(int cornerRadius) {
        radiusConer = cornerRadius;
        generateMaskPath(getWidth(), getHeight());
        invalidate();
    }*/


   /* @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        if (w != oldW || h != oldH) {
            generateMaskPath(w, h);
        }
    }

    private void generateMaskPath(int w, int h) {
        mMaskPath = new Path();
        mMaskPath.addRoundRect(new RectF(0,0,w,h), radiusConer, radiusConer, Path.Direction.CW);
        mMaskPath.setFillType(Path.FillType.INVERSE_WINDING);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(canvas.isOpaque()) { // If canvas is opaque, make it transparent
            canvas.saveLayerAlpha(0, 0, canvas.getWidth(), canvas.getHeight(), 255, Canvas.ALL_SAVE_FLAG);
        }

        super.onDraw(canvas);

        if(mMaskPath != null) {
            canvas.drawPath(mMaskPath, mMaskPaint);
        }
    }*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int w = MeasureSpec.getSize(widthMeasureSpec);
            int h = w * d.getIntrinsicHeight() / d.getIntrinsicWidth();
            setMeasuredDimension(w, h);


           /* float[] f = new float[9];
            getImageMatrix().getValues(f);

            // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
            final float scaleX = f[Matrix.MSCALE_X];
            final float scaleY = f[Matrix.MSCALE_Y];

            // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
            //final Drawable d = getDrawable();
            final int origW = d.getIntrinsicWidth();
            final int origH = d.getIntrinsicHeight();

            // Calculate the actual dimensions
            final int actW = Math.round(origW * scaleX);
            final int actH = Math.round(origH * scaleY);

            ////Log.e("DBG", "[" + origW + "," + origH + "] -> [" + actW + "," + actH + "] & scales: x=" + scaleX + " y=" + scaleY);*/
        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);    }




    // Get image matrix values and place them in an array


}

