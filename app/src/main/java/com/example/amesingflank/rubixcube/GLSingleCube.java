package com.example.amesingflank.rubixcube;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by lbj on 2015/11/24.
 */
public class GLSingleCube {//单个的正方体，27个组成一个魔方
    private FloatBuffer vertexBuffer;

    private final int mProgram;


    static final int COORDS_PER_VERTEX = 3;
    public float squareCoords[] = {
            -0.25f , 0.25f , -0.25f ,
            -0.25f , -0.25f , -0.25f ,
            0.25f , -0.25f , -0.25f ,
            -0.25f , 0.25f , -0.25f ,
            0.25f , -0.25f , -0.25f ,
            0.25f , 0.25f , -0.25f ,//front
            -0.25f , 0.25f , 0.25f ,
            -0.25f , -0.25f , 0.25f ,
            0.25f , -0.25f , 0.25f ,
            -0.25f , 0.25f , 0.25f ,
            0.25f , -0.25f , 0.25f ,
            0.25f , 0.25f , 0.25f ,//back
            -0.25f , -0.25f , 0.25f ,
            -0.25f , -0.25f , -0.25f ,
            -0.25f , 0.25f , -0.25f ,
            -0.25f , -0.25f , 0.25f ,
            -0.25f , 0.25f , -0.25f ,
            -0.25f , 0.25f , 0.25f ,//left
            0.25f , -0.25f , 0.25f ,
            0.25f , -0.25f , -0.25f ,
            0.25f , 0.25f , -0.25f ,
            0.25f , -0.25f , 0.25f ,
            0.25f , 0.25f , -0.25f ,
            0.25f , 0.25f , 0.25f ,//right
            0.25f , -0.25f , -0.25f ,
            -0.25f , -0.25f , -0.25f ,
            -0.25f , -0.25f , 0.25f ,
            0.25f , -0.25f , -0.25f ,
            -0.25f , -0.25f , 0.25f ,
            0.25f , -0.25f , 0.25f ,//down
            0.25f , 0.25f , -0.25f ,
            -0.25f , 0.25f , -0.25f ,
            -0.25f , 0.25f , 0.25f ,
            0.25f , 0.25f , -0.25f ,
            -0.25f , 0.25f , 0.25f ,
            0.25f , 0.25f , 0.25f ,//up

    };

    float[][] ColorArray=GLCubeColor.ColorArray;

    public float[] xMatrixes=new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
    public float[] yMatrixes=new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
    public void setxMatrixes(float[] in){
        xMatrixes=in.clone();
    }
    public void setyMatrixes(float[] in){
        yMatrixes=in.clone();
    }

    public float[] xLRMatrixes=new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
    public void setxLRMatrixes(float[] in){
        xLRMatrixes=in.clone();
    }

    public float[] yLRMatrixes=new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
    public void setyLRMatrixes(float[] in){
        xLRMatrixes=in.clone();
    }

    public float[] zLRMatrixes=new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
    public void setzLRMatrixes(float[] in){
        xLRMatrixes=in.clone();
    }

   // public float[] ztMat=new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0f,0f,0f,1};
    //public int ztHandle;



    private final String vertexShaderCode =

            "uniform mat4 xLRMatrix;" +
            "uniform mat4 yLRMatrix;" +
            "uniform mat4 zLRMatrix;" +
            "uniform mat4 xMatrix;" +
            "uniform mat4 yMatrix;" +
            "uniform mat4 uMVPMatrix;" +
                    "uniform mat4 zt;"+
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "  gl_Position = xLRMatrix * gl_Position;" +
                    "  gl_Position = yLRMatrix * gl_Position;" +
                    "  gl_Position = zLRMatrix * gl_Position;" +
                    "  gl_Position = xMatrix * gl_Position;" +
                    "  gl_Position = yMatrix * gl_Position;" +
                 //   "gl_Position = zt * gl_Position;"+

                    "  gl_Position = uMVPMatrix * gl_Position;" +
                    "}";

    private int mMVPMatrixHandle;
    public int xMatrixHandle;
    public int yMatrixHandle;
    public int xLRMatrixHandle;
    public int yLRMatrixHandle;
    public int zLRMatrixHandle;

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public void setColorArray(float[][] cin){
        ColorArray=cin;
    }





    public GLSingleCube(float[][] cin,int x,int y,int z ) {
        setTranslation(x,y,z);
        ColorArray=cin;

        ByteBuffer bb = ByteBuffer.allocateDirect(
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        int vertexShader = GLrenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = GLrenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        GLES20.glLinkProgram(mProgram);

    }

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    float x_translate=0f;
    float y_translate=0f;
    float z_translate=0f;

    //设置这个立方体的移动
    public void setTranslation(float x,float y, float z){
        float f=0.52f;
        x_translate=(x-1)*f;
        y_translate=(y-1)*f;
        z_translate=(z-1)*f;
        doTranslation();
    }
    public void doTranslation(){

        for (int i = 0; i <36 ; i++) {
            squareCoords[i*3]+=x_translate;
            squareCoords[i*3+1]+=y_translate;
            squareCoords[i*3+2]+=z_translate;
        }
    }

    boolean inited=false;

    public void InitDraw(){
        if(!inited){
            inited=true;
            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
            xMatrixHandle=GLES20.glGetUniformLocation(mProgram,"xMatrix");
            yMatrixHandle=GLES20.glGetUniformLocation(mProgram,"yMatrix");
            xLRMatrixHandle=GLES20.glGetUniformLocation(mProgram,"xLRMatrix");
            yLRMatrixHandle=GLES20.glGetUniformLocation(mProgram,"yLRMatrix");
            zLRMatrixHandle=GLES20.glGetUniformLocation(mProgram,"zLRMatrix");
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        }
    }

    public void draw(float [] mvpMatrixin) {
        InitDraw();
        // Commit Proof
        float[] mvpMatrix=mvpMatrixin.clone();
        GLES20.glUseProgram(mProgram);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);


        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glUniformMatrix4fv(xMatrixHandle,1,false,xMatrixes,0);
        GLES20.glUniformMatrix4fv(yMatrixHandle,1,false,yMatrixes,0);

        GLES20.glUniformMatrix4fv(xLRMatrixHandle,1,false,xLRMatrixes,0);
        GLES20.glUniformMatrix4fv(yLRMatrixHandle,1,false,yLRMatrixes,0);
        GLES20.glUniformMatrix4fv(zLRMatrixHandle,1,false,zLRMatrixes,0);


        for (int i = 0; i <6; i++) {
            GLES20.glUniform4fv(mColorHandle, 1, ColorArray[i], 0);

            int i1=i*vertexCount/6;

            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, i1, 6);
        }

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        xLRMatrixes=new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        yLRMatrixes=new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        zLRMatrixes=new float[]{1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
    }

    public void SyncWithJavaSingleCube(JavaSingleCube jin){
        for (int i = 0; i < 6; i++) {
            switch (jin.ColorArray[i]){
                case JavaSingleCube.orange:
                    ColorArray[i]=GLCubeColor.orange;
                    break;
                case JavaSingleCube.red:
                    ColorArray[i]=GLCubeColor.red;
                    break;
                case JavaSingleCube.blue:
                    ColorArray[i]=GLCubeColor.blue;
                    break;
                case JavaSingleCube.green:
                    ColorArray[i]=GLCubeColor.green;
                    break;
                case JavaSingleCube.white:
                    ColorArray[i]=GLCubeColor.white;
                    break;
                case JavaSingleCube.yellow:
                    ColorArray[i]=GLCubeColor.yellow;
                    break;
                case JavaSingleCube.blank:
                    ColorArray[i]=GLCubeColor.blank;
                    break;
            }
        }
    }
}

