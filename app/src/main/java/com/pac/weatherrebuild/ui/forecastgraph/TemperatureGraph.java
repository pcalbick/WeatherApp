package com.pac.weatherrebuild.ui.forecastgraph;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.pac.weatherrebuild.R;

public class TemperatureGraph extends View {

    private static final String TAG = "TempGraph";

    private boolean mShowGraph;

    private int width;
    private int height;

    private int[] barBounds;
    private int[] viewBounds;

    private String tempLo = "";
    private String tempHi = "";
    private Paint textPaint;
    private float textHeight;
    private int textColor;

    private float barHeight;
    private float barPadding = 0;
    private RectF mBarRect = new RectF();
    private Paint mBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint shadowPaint;

    public TemperatureGraph(Context context) {
        super(context);

        init(null);
    }

    public TemperatureGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TemperatureGraph,
                0, 0);
        try {
            setBarPadding(a.getDimension(R.styleable.TemperatureGraph_barPadding, 0));
            setBarHeight(a.getInt(R.styleable.TemperatureGraph_barHeight, 0));
            setTextColor(a.getColor(R.styleable.TemperatureGraph_textColor, 0));
            setTextHeight(a.getFloat(R.styleable.TemperatureGraph_textHeight, 0));
            setBarColor(a.getColor(R.styleable.TemperatureGraph_barColor,0));
        } finally {
            a.recycle();
        }

        init(attrs);
    }

    public TemperatureGraph(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        mBarPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        if(textHeight == 0){
            textHeight = textPaint.getTextSize();
        } else {
            textPaint.setTextSize(textHeight);
        }

        shadowPaint = new Paint(0);
        shadowPaint.setColor(0xff101010);
        shadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }

    private Rect getTextBounds(String text){
        Rect rect = new Rect();
        textPaint.getTextBounds(text,0,text.length(),rect);
        return rect;
    }

    public void setTextColor(int color){
        textColor = color;
    }

    public void setTextHeight(float height) {
        textHeight = height;
    }

    public void setText(String lo, String hi){
        tempLo = lo;
        tempHi = hi;
    }

    public void setBounds(int[] graph, int[] bounds){
        barBounds = graph;
        viewBounds = bounds;
    }

    public void setBar(int[] graph, int[] bounds){
        if(barHeight == 0){  barHeight = 8f; }

        float length = width/((float)(bounds[1]-bounds[0])/((float)(graph[1]-graph[0])));
        float slength = width/((float)(bounds[1]-bounds[0])/((float)(graph[0]-bounds[0])));

        float left = slength + barPadding;
        float top = (height / 2f) - (barHeight / 2);
        float right = left + length - barPadding;
        float bottom = (height / 2f) + (barHeight / 2);

        mBarRect = new RectF(left, top, right, bottom);
    }

    public void setBarPadding(float padding) {
        barPadding = padding;
    }

    public void setBarHeight(float height){
        barHeight = height;
    }

    public void setBarColor(int color){
        mBarPaint.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wmode = MeasureSpec.getMode(widthMeasureSpec);
        if(wmode != MeasureSpec.UNSPECIFIED){
            //add bar padding here?
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);

            setBar(barBounds,viewBounds);
            requestLayout();
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cornerRadius = 10;
        canvas.drawRoundRect(mBarRect,cornerRadius,cornerRadius,mBarPaint);

        float textY = mBarRect.top + getTextBounds(tempLo).height() + ((barHeight-getTextBounds(tempLo).height())/2);
        float textPadding = 5;

        if(getTextBounds(tempHi).width()+getTextBounds(tempLo).width() + textPadding*2 > mBarRect.width()){
            canvas.drawText(tempLo, mBarRect.left - textPadding - getTextBounds(tempLo).width(),
                    textY, textPaint);
            canvas.drawText(tempHi, mBarRect.right + textPadding,
                    textY, textPaint);
        }
        else {
            canvas.drawText(tempLo, mBarRect.left + textPadding,
                    textY, textPaint);
            canvas.drawText(tempHi, mBarRect.right - textPadding - getTextBounds(tempHi).width(),
                    textY, textPaint);
        }
    }
}
