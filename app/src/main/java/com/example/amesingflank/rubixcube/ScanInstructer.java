package com.example.amesingflank.rubixcube;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

/**
 * Created by AmesingFlank on 16/1/26.
 */
public class ScanInstructer extends View {

    Paint paint;
    int width=1440;
    int height=2308;


    String msg=" Touch a face to select it for scanning";
    public ScanInstructer(Context c,int w,int h){
        super(c);
        width=w;
        height=h;
        paint=new Paint();
        paint.setTextSize(80 * w / 1440);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setTextSize(80 * width / 1440);
        paint.setColor(Color.WHITE);
        canvas.drawText(msg, 0, getHeight() * 19 / 20, paint);

    }


}
