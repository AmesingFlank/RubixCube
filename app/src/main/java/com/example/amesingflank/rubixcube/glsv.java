package com.example.amesingflank.rubixcube;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;

/**
 * Created by lbj on 2015/11/24.
 */
public class glsv extends GLSurfaceView {
    final GLrenderer mRenderer;

    public glsv(Context context){

        super(context);
        resetData(0);
        setEGLContextClientVersion(2);

        mRenderer = new GLrenderer();

        setRenderer(mRenderer);
    }


    private final float TOUCH_SCALE_FACTOR = 180.0f ;
    private float mPreviousX;
    private float mPreviousY;
    public int HasIntersection=0;
    int side0=9;
    int[] cubeIndex0=new int[3];
    int side1=9;
    int[] cubeIndex1=new int[3];

    public void resetData(int n){
        HasIntersection=n;
        side0=9;
        side1=9;
        for (int i = 0; i <3 ; i++) {
            cubeIndex0[i]=9;
            cubeIndex1[i]=9;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();
        float normalizedX=x;
        float normalizedY=getHeight()-y;


        switch (e.getAction()) {

            case MotionEvent.ACTION_DOWN:

                int[] res=mRenderer.handleTouch(normalizedX,normalizedY);
                HasIntersection=res[0];
                side0=res[1];
                for (int i = 0; i <3 ; i++) {
                    cubeIndex0[i]=res[i+2];
                }
                if(!mRenderer.isRotating){
                    mRenderer.timeflag=false;
                }

                //mRenderer.setRotation(2,0,-1);
                //旋转Z0层

                break;
            case MotionEvent.ACTION_MOVE:
                if(HasIntersection==0){
                    float dx = x - mPreviousX;
                    float dy = y - mPreviousY;
                    //int[] temp=mRenderer.handleTouch(normalizedX,normalizedY);
                    mRenderer.set_xyangle(dx*TOUCH_SCALE_FACTOR/((float)(getWidth())),
                            dy*TOUCH_SCALE_FACTOR/((float)(getWidth())));
                    requestRender();
                }
                else if(HasIntersection==1){

                    int[] temp=mRenderer.handleTouch(normalizedX,normalizedY);
                    HasIntersection=temp[0];
                    side1=temp[1];
                    for (int i = 0; i <3 ; i++) {
                        cubeIndex1[i]=temp[i+2];
                    }

                    if(side0==side1 && !mRenderer.isRotating){
                        xx:switch (side0){
                            case 0:
                                if(cubeIndex0[0]<cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(0, cubeIndex0[1], 1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]>cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(0, cubeIndex0[1], -1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]<cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(2, cubeIndex0[0], -1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]>cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(2, cubeIndex0[0], 1);
                                    resetData(2);
                                    break xx;
                                }
                                break ;
                            case 1:
                                if(cubeIndex0[0]<cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(0, cubeIndex0[1], -1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]>cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(0, cubeIndex0[1], 1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]<cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(2, cubeIndex0[0], 1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]>cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(2, cubeIndex0[0], -1);
                                    resetData(2);
                                    break xx;
                                }
                                break ;
                            case 2:
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]<cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(1, cubeIndex0[2], 1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]>cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(1, cubeIndex0[2], -1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]<cubeIndex1[2]){
                                    mRenderer.setRotation(0, cubeIndex0[1], -1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]>cubeIndex1[2]){
                                    mRenderer.setRotation(0, cubeIndex0[1], 1);
                                    resetData(2);
                                    break xx;
                                }
                                break ;
                            case 3:
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]<cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(1, cubeIndex0[2], -1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]>cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(1, cubeIndex0[2], 1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]<cubeIndex1[2]){
                                    mRenderer.setRotation(0, cubeIndex0[1], 1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]>cubeIndex1[2]){
                                    mRenderer.setRotation(0, cubeIndex0[1], -1);
                                    resetData(2);
                                    break xx;
                                }
                                break ;
                            case 4:
                                if(cubeIndex0[0]<cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(1, cubeIndex0[2], -1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]>cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(1, cubeIndex0[2], 1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]<cubeIndex1[2]){
                                    mRenderer.setRotation(2, cubeIndex0[0], 1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]>cubeIndex1[2]){
                                    mRenderer.setRotation(2, cubeIndex0[0], -1);
                                    resetData(2);
                                    break xx;
                                }
                                break ;
                            case 5:
                                if(cubeIndex0[0]<cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(1, cubeIndex0[2], 1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]>cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]==cubeIndex1[2]){
                                    mRenderer.setRotation(1, cubeIndex0[2], -1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]<cubeIndex1[2]){
                                    mRenderer.setRotation(2, cubeIndex0[0], -1);
                                    resetData(2);
                                    break xx;
                                }
                                if(cubeIndex0[0]==cubeIndex1[0] && cubeIndex0[1]==cubeIndex1[1] && cubeIndex0[2]>cubeIndex1[2]){
                                    mRenderer.setRotation(2, cubeIndex0[0], 1);
                                    resetData(2);
                                    break xx;
                                }
                                break ;
                        }
                        mRenderer.glrc.Jcube.ruinSolver();
                    }


                }
                //设置xy轴整体旋转角度
                break;
            case MotionEvent.ACTION_UP:
                resetData(0);
                //mRenderer.clear_xyangle();

        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }


}
