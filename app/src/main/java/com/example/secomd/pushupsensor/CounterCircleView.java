package com.example.secomd.pushupsensor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static android.R.attr.path;

/*
Custom view to show progress in number of pushups done
 */

public class CounterCircleView extends View {
    private String mExampleString = "wow"; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private double angle = -Math.PI/2;


    public CounterCircleView(Context context) {
        super(context);
        init(null, 0);
    }

    public CounterCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CounterCircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        setWillNotDraw(false);
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CounterCircleView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.CounterCircleView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.CounterCircleView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.CounterCircleView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.CounterCircleView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.CounterCircleView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        //invalidateTextPaintAndMeasurements();
    }

   /* private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }


*/
    private void clear() {
        //path = new Path();
        invalidate();
    }

    public void updateProgress(int count, int maxCount) {
        angle = 2*Math.PI*count/maxCount - Math.PI/2;

        clear();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("from onDraw",Double.toString(angle));
        super.onDraw(canvas);

        int count = 5;
        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();


        float xCenter = getWidth();
        float yCenter = getHeight();
        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;


        // Draw the text.
        /*canvas.drawText(Integer.toString(count),
                paddingLeft + (contentWidth - mTextWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);*/

        //Draw a circle
        Paint circlePaint = new Paint();
        float radius = 200;
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(10);
        circlePaint.setAntiAlias(true);
        canvas.drawCircle((xCenter/2),yCenter/2,radius,circlePaint);

        double startx = (xCenter/2)+(radius-20)*Math.cos(angle);
        double starty = (yCenter/2)+(radius-20)*Math.sin(angle);
        double stopx =(xCenter/2)+(radius+20)*Math.cos(angle);
        double stopy = (yCenter/2)+(radius+20)*Math.sin(angle);
        Paint linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5);
        linePaint.setAntiAlias(true);
        canvas.drawLine((float) startx,(float)starty,(float)stopx,(float)stopy,linePaint);

       // canvas.drawArc((xCenter-100)/2,(yCenter + 100)/2,(xCenter+100)/2,(yCenter - 100)/2, 0,360, )
        // Draw the example drawable on top of the text.
        /*
        if (mExampleDrawable != null) {
            mExampleDrawable.setBounds(paddingLeft, paddingTop,
                    paddingLeft + contentWidth, paddingTop + contentHeight);
            mExampleDrawable.draw(canvas);
        }
        */
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        //invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        //invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        //invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
