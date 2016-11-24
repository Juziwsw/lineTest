package com.example.wsw.linetest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by WSW on 2016/11/20.
 */

public class LineView extends View {
    private Paint xyPaint;//画XY轴
    private Paint titlePaint;// 画分割说明
    //设置标记，对XY轴区分，方便平均分割
    private final int Y = 1;
    private final int X = 0;

    //原点坐标
    private int originX;
    private int originY;
    //X,Y轴的长度
    private int xLong;
    private int yLong;
    //对x,y轴进行分割，x为5格，y为4格
    private int splitXNum = 4;
    private int splitYNum = 3;

    //标题文字大小
    private int titleSize = DensityUtils.dp2px(getContext(),20);

    private int [][]icons = {{1,2},{2,2},{3,1},{4,1}};


    public LineView(Context context) {
        this(context,null);
    }

    public LineView(Context context, AttributeSet attrs) {
        this(context, null,1);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //x轴的长度是幕宽度的4/5，y轴的长度是屏幕高度的1/3；
        xLong = w*4/5;
        yLong = h/3;
        originX = 100;
        originY = h/2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawLine(canvas);
        drawSplit(canvas);
        drawExplain(canvas);
        drawIcon(canvas);
    }

    /**
     * 绘制XY轴
     * @param canvas
     */
    private void drawLine(Canvas canvas){
        Path path = new Path();
        path.moveTo(originX,originY);
        path.lineTo(originX + xLong,originY);
        path.moveTo(originX,originY);
        path.lineTo(originX,originY - yLong);
        canvas.drawPath(path,xyPaint);


    }

    /**
     * 绘制分割线
     * @param canvas
     */
    private void drawSplit(Canvas canvas){
        Path path = new Path();
        for (int i = 0; i < splitXNum; i++){
            path.moveTo(getPoint(i,X).x,getPoint(i,X).y);
            path.lineTo(getPoint(i,X).x,getPoint(i,X).y - 15);
        }
        for (int i = 0; i < splitYNum; i++){
            path.moveTo(getPoint(i,Y).x,getPoint(i,Y).y);
            path.lineTo(getPoint(i,Y).x + 15,getPoint(i,Y).y);
        }
        canvas.drawPath(path,xyPaint);
    }
    /**
     * 绘制分割线说明
     * @param canvas
     */
    private void drawExplain(Canvas canvas){
        for (int i = 0; i < splitXNum; i++){
            int x = getPoint(i,X).x - (int)(titlePaint.measureText(i+"")/2);
            int y = getPoint(i,X).y + (int)(titlePaint.descent() - titlePaint.ascent());
            canvas.drawText(i+"",x,y,titlePaint);
        }
        for (int i = 0; i < splitYNum; i++){
            int x = getPoint(i,Y).x - (int)(titlePaint.measureText(i+"")) - 5;
            int y = getPoint(i,Y).y + (int)(-titlePaint.ascent())/2;
            canvas.drawText(i+"",x,y,titlePaint);
        }

    }
    private void drawIcon(Canvas canvas){
        Path path = new Path();
        for (int i = 0; i <= icons.length; i++){
            if (i == 0){
                path.moveTo(originX,originY);
            }else {
                path.lineTo(getIconPoint(i-1).x,getIconPoint(i-1).y);
            }
        }
        canvas.drawPath(path,xyPaint);
    }
    private void initPaint(){
        xyPaint = new Paint();
        xyPaint.setAntiAlias(true);
        xyPaint.setColor(Color.BLUE);
        xyPaint.setStyle(Paint.Style.STROKE);

        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(titleSize);
        titlePaint.setColor(Color.BLUE);
        titlePaint.setStyle(Paint.Style.FILL);


    }

    /**
     * 获取分割点的坐标，也包括下标注释的坐标
     * @param position
     * @param style
     * @return
     */
    private Point getPoint(int position ,int style){
        int x = 0;
        int y = 0;
        //x轴
        if (style == X){
            if (position == 0){
                x =  xLong/5 + originX;
                y = originY;
            }else if (position == 1){
                x =  xLong/5 * 2 + originX;
                y = originY;
            }else if (position == 2){
                x =  xLong/5 * 3 + originX;
                y = originY;
            }else if (position == 3){
                x = xLong/5 * 4 + originX;
                y = originY;
            }
        }else if (style == Y){//y轴
            if (position == 0){
                x = originX;
                y = originY - yLong/4;
            }else if (position == 1){
                x = originX;
                y = originY - yLong/4*2;
            }else if (position == 2){
                x = originX;
                y = originY - yLong/4*3;
            }

        }
        return new Point(x,y);
    }

    private Point getIconPoint(int position){
        int x = 0;
        int y = 0;
        x = getPoint(icons[position][0]-1,X).x;
        y = getPoint(icons[position][1]-1,Y).y;
        return new Point(x,y);
    }
}
