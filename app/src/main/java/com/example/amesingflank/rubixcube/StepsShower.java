package com.example.amesingflank.rubixcube;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

/**
 * Created by AmesingFlank on 16/1/9.
 */
public class StepsShower extends View{

    Paint paint;
    int width=1440;
    int height=2308;
    int steps=0;

    Canvas c;
    public StepsShower(Context c,int w,int h){
        super(c);
        width=w;
        height=h;
        paint=new Paint();
        paint.setTextSize(80 * w / 1440);
        paint.setColor(Color.BLUE);




    }
    @Override
    protected void onDraw(Canvas canvas) {
        Shader mShader = new LinearGradient(0,0,width*5/18,getHeight()/6,new int[] {Color.RED,Color.YELLOW,Color.GREEN},null, Shader.TileMode.REPEAT);
        paint.setShader(mShader);
        canvas.drawText("Steps: "+String.valueOf(steps),width/100,getHeight()/6,paint);
        c=canvas;


    }


}
