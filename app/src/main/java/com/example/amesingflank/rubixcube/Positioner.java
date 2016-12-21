package com.example.amesingflank.rubixcube;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by AmesingFlank on 16/1/26.
 */
public class Positioner extends View {
    Paint paint;
    int width=1440;
    int height=2308;
    int steps=0;




    public Positioner(Context c,int w,int h){
        super(c);
        width=w;
        height=h;
        paint=new Paint();

        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(width/120);

    }
    @Override
    protected void onDraw(Canvas canvas) {

        int edge=width/4;
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <3 ; j++) {
                canvas.drawCircle(edge*(i+1),edge*(j+1),edge/4,paint);
            }
        }
    }
}
