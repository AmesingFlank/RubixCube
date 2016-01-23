package com.example.amesingflank.rubixcube;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.view.View;

import java.util.LinkedList;
import java.util.logging.Handler;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by lbj on 2015/11/24.
 */
public class GLrenderer implements GLSurfaceView.Renderer{
    // Context myc;
    GLRubikCube glrc;



    public void setWH(int widthIN,int heightIN){
        thiswidth=widthIN;
        thisheight=heightIN;
    }

    public GLrenderer(){
        super();
        //setWH(1440,2308);

    }


    public float[] mMVPMatrix = new float[16];
    public float[] mProjectionMatrix = new float[16];
    public float[] mViewMatrix = new float[16];




    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        glrc=new GLRubikCube();

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LESS);


    }
    public float RotationSpeedCoefficient=0.15f;

    public void onDrawFrame(GL10 unused) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(0.55f,0.68f,1f,0f);

        if(need2Sync){
            SyncwithJavaCube();
        }
        if(firstdrawflag==false){
            glrc.firstdraw(mMVPMatrix);
            firstdrawflag=true;
        }
        else {
            if(!timeflag){
                timeflag=true;
                time0=SystemClock.uptimeMillis();
            }
            isRotating=rotationAxis<3;
            float angle=0;
            if(isRotating  || onCoherent){
                long time=SystemClock.uptimeMillis()-time0;
                angle=(int)(time)*RotationSpeedCoefficient;
                if(angle>=85){
                    angle=90f;
                }
            }

            glrc.operate(rotationAxis,rotationIndex,xangle,yangle,angle*rotationDirection);


            if (angle==90f &&(isRotating  || onCoherent)){
                clear_Rotation();
                timeflag=false;
            }
        }


    }
    public  boolean isRotating=false;
    public long time0;
    boolean timeflag=false;
    public int rotationAxis=5;
    public int rotationIndex=5;
    public int rotationDirection=0;
    public void setRotation(int ain,int iin,int din){//设置要旋转的层，direction=1就是正方向，-1就是反方向
        rotationAxis=ain;
        rotationIndex=iin;
        rotationDirection=din;

    }
    public void clear_Rotation(){
        //结束旋转
        switch (rotationDirection){
            case 1:
                glrc.rotatePos(rotationAxis,rotationIndex);
                break;
            case -1:
                glrc.rotateNeg(rotationAxis,rotationIndex);
        }
        isRotating=false;
        setRotation(5, 5, 0);
        timeflag=false;
        if(onCoherent){
            if(SolutionIndex==Solution.size()){
                SolutionIndex=0;
                Solution=new LinkedList<Move>();
                onCoherent=false;
                Paused=false;
                hasSolution=false;
                ss.msg="Finished";
                ss.postInvalidate();
            }
            else {

                if(!Paused){
                    int[] thisStep=Solution.get(SolutionIndex).action;
                    setRotation(thisStep[0],thisStep[1],thisStep[2]);
                    ss.msg=Solution.get(SolutionIndex).message;
                    SolutionIndex++;
                }

                if(SolutionIndex!=Solution.size()){
                    ss.steps++;
                    ss.postInvalidate();
                }

            }
            ss.setVisibility(View.VISIBLE);
            ss.postInvalidate();
        }
    }
    public boolean onCoherent=false;
    public boolean Paused=false;
    public LinkedList<Move> Solution=new LinkedList<Move>();
    public int SolutionIndex=0;
    public boolean hasSolution=false;
    public void getSolution(){
        Solution=glrc.Jcube.getSolution();
        if (Solution.size()!=0){
            hasSolution=true;
        }
    }
    public void getIntelliSolution(){
       // Solution=glrc.Jcube.getIntelliSolution();
        //if (Solution.size()!=0){
          //  hasSolution=true;
        //}
    }
    StepsShower ss;
    public void setStpesShower(StepsShower sin){
        ss=sin;
    }



    public final float initialAngle=30;
    public boolean firstdrawflag=false;
    public float xangle=initialAngle;
    public float yangle=initialAngle;

    public void set_xyangle(float xin,float yin){

        int maximumAngle=55;

        xangle+=xin;

        yangle+=yin;
        if(yangle>=maximumAngle){
            yangle=maximumAngle;
        }
        if(yangle<=-maximumAngle){
            yangle=-maximumAngle;
        }

    }
    public int[] handleTouch(float x,float y){
        int HasIntersection=0;
        int w=thiswidth;
        int h=thisheight;
        float[] startCoord=new float[4];
        float[] endCoord=new float[4];

        GLU.gluUnProject(x, y, 0f, mViewMatrix, 0, mProjectionMatrix, 0, new int[]{0, 0, w, h}, 0, startCoord, 0);
        GLU.gluUnProject(x, y, 1f, mViewMatrix, 0, mProjectionMatrix, 0, new int[]{0, 0, w, h}, 0, endCoord, 0);
        //这里改变的值很奇怪

        devideByW(startCoord);
        devideByW(endCoord);

        Point3DFloat startP=new Point3DFloat(startCoord[0],startCoord[1],startCoord[2]);
        Point3DFloat endP=new Point3DFloat(endCoord[0],endCoord[1],endCoord[2]);


        float[][][][] triangleData=glrc.getTriangles();
        float[][] centralData=glrc.getCentrals();

        int SideIndex0=9;
        int SideIndex1=9;
        int FinalSideIndex=9;
        int TriangleIndex0=9;
        int TriangleIndex1=9;
        int FinalTriangleIndex=9;
        float[] res0=new float[3];
        float[] res1=new float[3];
        float[] FinalResults=new float[3];
        boolean got0=false;

        xx:for (int i = 0; i <6 ; i++) {
            for (int j = 0; j <2 ; j++) {
                Point3DFloat p0=getPointFromCord(triangleData[i][j][0]);
                Point3DFloat p1=getPointFromCord(triangleData[i][j][1]);
                Point3DFloat p2=getPointFromCord(triangleData[i][j][2]);
                float[] res=new float[3];
                res=IntersectionChecker.getresults(startP,endP,p0,p1,p2);
                if(res[0]!=0){
                    HasIntersection=1;
                    if (!got0){
                        SideIndex0=i;
                        TriangleIndex0=j;
                        res0=res;
                        got0=true;
                    }
                    else {
                        SideIndex1=i;
                        TriangleIndex1=j;
                        res1=res;
                        break xx;
                    }
                }

            }
        }
        if (HasIntersection==1){
            if(centralData[SideIndex0][2]<centralData[SideIndex1][2]){
                FinalSideIndex=SideIndex0;
                FinalTriangleIndex=TriangleIndex0;
                FinalResults=res0;
            }
            else {
                FinalSideIndex=SideIndex1;
                FinalTriangleIndex=TriangleIndex1;
                FinalResults=res1;
            }
            int[] cubeIndex ;
            cubeIndex=getSingleCubeIndex(FinalSideIndex,FinalTriangleIndex,FinalResults[1],FinalResults[2]);
            return new int[]{HasIntersection,FinalSideIndex,cubeIndex[0],cubeIndex[1],cubeIndex[2]};
        }
        else {
            return new int[]{0,9,9,9,9};
        }
    }
    public static Point3DFloat getPointFromCord(float[] in)
    {
        float[] temp=in.clone();
        devideByW(temp);
        return new Point3DFloat(temp[0],temp[1],temp[2]);
    }
    public static int[] getSingleCubeIndex(int sideIndex,int triangleIndex,float  u,float v){
        float factor=GLRubikCube.translationfactor+0.25f;
        int x,y,z;
        x=0;y=0;z=0;
        switch (sideIndex){
            case 0:
                z=0;
                switch (triangleIndex){
                    case 0:
                        x=comparePos( u);
                        y=comparePos( v);
                        break;
                    case 1:
                        x=compareNeg(u);
                        y=compareNeg(v);
                        break;
                }
                break;
            case 1:
                z=2;
                switch (triangleIndex){
                    case 0:
                        x=comparePos(u);
                        y=comparePos(v);
                        break;
                    case 1:
                        x=compareNeg(u);
                        y=compareNeg(v);
                        break;
                }
                break;
            case 2:
                x=0;
                switch (triangleIndex){
                    case 0:
                        y=comparePos(v);
                        z=comparePos(u);
                        break;
                    case 1:
                        y=compareNeg(u);
                        z=compareNeg(v);
                        break;
                }
                break;
            case 3:
                x=2;
                switch (triangleIndex){
                    case 0:
                        y=comparePos(v);
                        z=comparePos(u);
                        break;
                    case 1:
                        y=compareNeg(u);
                        z=compareNeg(v);
                        break;
                }
                break;
            case 4:
                y=0;
                switch (triangleIndex){
                    case 0:
                        x=comparePos(v);
                        z=comparePos(u);
                        break;
                    case 1:
                        x=compareNeg(u);
                        z=compareNeg(v);
                }
                break;
            case 5:
                y=2;
                switch (triangleIndex){
                    case 0:
                        x=comparePos(v);
                        z=comparePos(u);
                        break;
                    case 1:
                        x=compareNeg(u);
                        z=compareNeg(v);
                }
                break;
        }if(x==4||y==4||z==4){
            return new int[]{1,1,1};
        }
        return new int[]{x,y,z};
    }
    public static int comparePos(float uv){
        float ratio =1/uv;
        if(ratio>3){
            return 0;
        }
        else {
            if(ratio<3 && ratio>1.5){
                return 1;
            }
            else {
                if(ratio<1.5)
                    return 2;
                else
                    return 4;
            }
        }
    }
    public static int compareNeg(float uv){
        float ratio =1/uv;
        if(ratio>3){
            return 2;
        }
        else {
            if(ratio<5 && ratio>1.5){
                return 1;
            }
            else {
                if(ratio<1.5)
                    return 0;
                else
                    return 4;
            }
        }
    }
    public static void devideByW(float[] in){
        if(in[3]!=0){
            in[0]/=in[3];
            in[1]/=in[3];
            in[2]/=in[3];
        }
    }

    public int thiswidth;
    public int thisheight;
    public void onSurfaceChanged(GL10 unused, int width, int height) {

        //thiswidth=width;
        //thisheight=height;
        GLES20.glViewport(0, 0, thiswidth, thisheight);

        float ratio = (float) thiswidth / thisheight;

        Matrix.frustumM(mProjectionMatrix, 0, ratio, -ratio, -1, 1, 3, 10);

        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -6.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

    }
    public static int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


    JavaCube tobeSynced=new JavaCube();

    public void SyncwithJavaCube(){
        glrc.Jcube=tobeSynced.clone();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    glrc.singleCubes[x][y][z].SyncWithJavaSingleCube(tobeSynced.singleCubes[x][y][z]);
                }
            }
        }
        need2Sync=false;
    }
    public void setSyncwithJavaCube(JavaCube jin){
        tobeSynced=jin.clone();
        need2Sync=true;
    }
    boolean need2Sync=true;

}
