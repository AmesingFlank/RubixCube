package com.example.amesingflank.rubixcube;

import android.opengl.Matrix;
import android.os.SystemClock;

import java.util.Random;

/**
 * Created by lbj on 2015/11/24.
 */
public class GLRubikCube {
    //魔方类，包含27个singlecube,还有一个CubeMatixes矩阵数组，存着每一个singlecube所对应着的modelvieMatrix*RotationMatrix。translateM()乘到矩阵上
    //每个singlecube的坐标都一样，但是各自有一个x-translate,y-translate,z-translate值，在绘画时，这三个值会被通过
    //renderer每检测到一点旋转，就把这个旋转角度交给这里，并生成新的旋转矩阵，然后乘到每一个singlecube所对应的矩阵上面
    //画图的时候是分别绘制27个singlecube，并把每个singlecube所对应的矩阵传给那个singlecube的draw（）函数。
    //因此每个singlecube的坐标都不会变化而且是一样的，变得只是他们所对应的矩阵。
    //当每一层旋转时，这一层所包含的singlecube所对应的矩阵就会改变，当旋转了90后，rotatePos() rotateNeg()这两个函数会改变这个singlecube的颜色
    
    GLSingleCube[][][] singleCubes=new GLSingleCube[3][3][3];
    public final int Xaxis=0;
    public final int Yaxis=1;
    public final int Zaxis=2;
    public static float translationfactor=0.52f;
    JavaCube Jcube=new JavaCube();
    public GLRubikCube(){
        Jcube=new JavaCube();
        float[][][][][] Colordata=GLCubeColor.Cube27data();
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                for (int k = 0; k <3 ; k++) {
                    singleCubes[i][j][k]=new GLSingleCube(Colordata[i][j][k],i,j,k);
                    xTranslates[i][j][k]=(i-1)*translationfactor;
                    yTranslates[i][j][k]=(j-1)*translationfactor;
                    zTranslates[i][j][k]=(k-1)*translationfactor;
                }
            }
        }
    }
    public void firstdraw(float[] s){
        //第一次画图，让CubeMatrixes等于传进来的modelviewmatrix的值
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                for (int k = 0; k <3 ; k++) {
                    singleCubes[i][j][k].draw(s);
                     mCubeMatrixes=s.clone();
                }
            }
        }
    }
    public void draw(float[] s){
        //画图，每个singlecube都根据自己的矩阵进行绘画
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                for (int k = 0; k <3 ; k++) {
                    singleCubes[i][j][k].draw(s);
                }
            }
        }
    }
    public float[][][] xTranslates=new float[3][3][3];
    public float[][][] yTranslates=new float[3][3][3];
    public float[][][] zTranslates=new float[3][3][3];

    public float[] mCubeMatrixes=new float[16];
    public float[] xMatrixes=new float[16];
    public float[] yMatrixes=new float[16];
    public float xangle=0;
    public float yangle=0;
    
    public void operate(int ain,int iin,float xRotationIn,float yRotationIn,float layerRotationIn){

        //控制整体旋转和层旋转，结果全都反映到CubeMatrixes里面
        //ain是要旋转的单层的方向，可以是X,Y,Z
        //inn是要旋转的单层的编号，可以是0，1，2
        //xRotationIn是整体旋转的x轴的度数
        //yRotationIn是整体旋转的y轴的度数
        //layerRotationIn是单层旋转的当前度数。
        xangle=xRotationIn;
        yangle=yRotationIn;

        float[] CubeMatrixes=new float[16];
        CubeMatrixes=mCubeMatrixes.clone();

        xMatrixes=new float[16];
        yMatrixes=new float[16];
        Matrix.setRotateM(xMatrixes,0,xRotationIn,0f,-1f,0f);
        Matrix.setRotateM(yMatrixes,0,yRotationIn,-1f,0,0);
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                for (int k = 0; k <3 ; k++) {
                    singleCubes[i][j][k].setxMatrixes(xMatrixes);
                    singleCubes[i][j][k].setyMatrixes(yMatrixes);
                }
            }
        }


        //控制单层旋转，把要旋转的层内的singlecube的矩阵先存到temp里，旋转过后再换回来，通过修改颜色保存旋转。

        float[] layerRotationMatix=new float[16];
        switch (ain){
            case Xaxis:
                Matrix.setRotateM(layerRotationMatix,0,layerRotationIn,0,-1f,0);
                for (int i = 0; i <3 ; i++) {
                    for (int j = 0; j <3 ; j++) {
                        singleCubes[i][iin][j].setxLRMatrixes(layerRotationMatix);
                    }
                }
                break;
            case Yaxis:
                Matrix.setRotateM(layerRotationMatix,0,layerRotationIn,0,0,-1f);
                for (int i = 0; i <3 ; i++) {
                    for (int j = 0; j <3 ; j++) {
                        singleCubes[i][j][iin].setyLRMatrixes(layerRotationMatix);
                    }
                }
                break;
            case Zaxis:
                Matrix.setRotateM(layerRotationMatix,0,layerRotationIn,-1f,0,0);
                for (int i = 0; i <3 ; i++) {
                    for (int j = 0; j <3 ; j++) {
                        singleCubes[iin][i][j].setzLRMatrixes(layerRotationMatix);
                    }
                }
                break;
        }


        draw(CubeMatrixes);


    }
    public void rotatePos(int ain,int iin){
        //正方向旋转修改颜色。
        GLSingleCube temp0;
        GLSingleCube temp1;
        switch (ain){
            case Xaxis:
                temp0=new GLSingleCube(singleCubes[0][iin][0].ColorArray,1,1,1);
                temp1=new GLSingleCube(singleCubes[1][iin][0].ColorArray,1,1,1);
                copycubePos(singleCubes[0][iin][2],singleCubes[0][iin][0],ain);
                copycubePos(singleCubes[2][iin][2],singleCubes[0][iin][2],ain);
                copycubePos(singleCubes[2][iin][0],singleCubes[2][iin][2],ain);
                copycubePos(temp0,singleCubes[2][iin][0],ain);
                copycubePos(singleCubes[0][iin][1],singleCubes[1][iin][0],ain);
                copycubePos(singleCubes[1][iin][2],singleCubes[0][iin][1],ain);
                copycubePos(singleCubes[2][iin][1],singleCubes[1][iin][2],ain);
                copycubePos(temp1,singleCubes[2][iin][1],ain);
                break;
            case Yaxis:
                temp0=new GLSingleCube(singleCubes[0][0][iin].ColorArray,1,1,1);
                temp1=new GLSingleCube(singleCubes[0][1][iin].ColorArray,1,1,1);
                copycubePos(singleCubes[2][0][iin],singleCubes[0][0][iin],ain);
                copycubePos(singleCubes[2][2][iin],singleCubes[2][0][iin],ain);
                copycubePos(singleCubes[0][2][iin],singleCubes[2][2][iin],ain);
                copycubePos(temp0,singleCubes[0][2][iin],ain);
                copycubePos(singleCubes[1][0][iin],singleCubes[0][1][iin],ain);
                copycubePos(singleCubes[2][1][iin],singleCubes[1][0][iin],ain);
                copycubePos(singleCubes[1][2][iin],singleCubes[2][1][iin],ain);
                copycubePos(temp1,singleCubes[1][2][iin],ain);
                break;
            case Zaxis:
                temp0=new GLSingleCube(singleCubes[iin][0][0].ColorArray,1,1,1);
                temp1=new GLSingleCube(singleCubes[iin][0][1].ColorArray,1,1,1);
                copycubePos(singleCubes[iin][2][0],singleCubes[iin][0][0],ain);
                copycubePos(singleCubes[iin][2][2],singleCubes[iin][2][0],ain);
                copycubePos(singleCubes[iin][0][2],singleCubes[iin][2][2],ain);
                copycubePos(temp0,singleCubes[iin][0][2],ain);
                copycubePos(singleCubes[iin][1][0],singleCubes[iin][0][1],ain);
                copycubePos(singleCubes[iin][2][1],singleCubes[iin][1][0],ain);
                copycubePos(singleCubes[iin][1][2],singleCubes[iin][2][1],ain);
                copycubePos(temp1,singleCubes[iin][1][2],ain);
                break;
        }
        Jcube.rotatePos(ain,iin);
    }


    public void rotateNeg(int ain,int iin){
        //反方向旋转修改颜色
        GLSingleCube temp0;
        GLSingleCube temp1;
        switch (ain){
            case Xaxis:
                temp0=new GLSingleCube(singleCubes[0][iin][0].ColorArray,1,1,1);
                temp1=new GLSingleCube(singleCubes[1][iin][0].ColorArray,1,1,1);
                copycubeNeg(singleCubes[2][iin][0], singleCubes[0][iin][0], ain);
                copycubeNeg(singleCubes[2][iin][2], singleCubes[2][iin][0], ain);
                copycubeNeg(singleCubes[0][iin][2], singleCubes[2][iin][2], ain);
                copycubeNeg(temp0, singleCubes[0][iin][2], ain);
                copycubeNeg(singleCubes[2][iin][1], singleCubes[1][iin][0], ain);
                copycubeNeg(singleCubes[1][iin][2], singleCubes[2][iin][1], ain);
                copycubeNeg(singleCubes[0][iin][1], singleCubes[1][iin][2], ain);
                copycubeNeg(temp1, singleCubes[0][iin][1], ain);
                break;
            case Yaxis:
                temp0=new GLSingleCube(singleCubes[0][0][iin].ColorArray,1,1,1);
                temp1=new GLSingleCube(singleCubes[0][1][iin].ColorArray,1,1,1);
                copycubeNeg(singleCubes[0][2][iin], singleCubes[0][0][iin], ain);
                copycubeNeg(singleCubes[2][2][iin], singleCubes[0][2][iin], ain);
                copycubeNeg(singleCubes[2][0][iin], singleCubes[2][2][iin], ain);
                copycubeNeg(temp0, singleCubes[2][0][iin], ain);
                copycubeNeg(singleCubes[1][2][iin], singleCubes[0][1][iin], ain);
                copycubeNeg(singleCubes[2][1][iin], singleCubes[1][2][iin], ain);
                copycubeNeg(singleCubes[1][0][iin], singleCubes[2][1][iin], ain);
                copycubeNeg(temp1, singleCubes[1][0][iin], ain);
                break;
            case Zaxis:
                temp0=new GLSingleCube(singleCubes[iin][0][0].ColorArray,1,1,1);
                temp1=new GLSingleCube(singleCubes[iin][0][1].ColorArray,1,1,1);
                copycubeNeg(singleCubes[iin][0][2], singleCubes[iin][0][0], ain);
                copycubeNeg(singleCubes[iin][2][2], singleCubes[iin][0][2], ain);
                copycubeNeg(singleCubes[iin][2][0], singleCubes[iin][2][2], ain);
                copycubeNeg(temp0, singleCubes[iin][2][0], ain);
                copycubeNeg(singleCubes[iin][1][2], singleCubes[iin][0][1], ain);
                copycubeNeg(singleCubes[iin][2][1], singleCubes[iin][1][2], ain);
                copycubeNeg(singleCubes[iin][1][0], singleCubes[iin][2][1], ain);
                copycubeNeg(temp1, singleCubes[iin][1][0], ain);
                break;
        }
        Jcube.rotateNeg(ain, iin);
    }

    public void copycubePos(GLSingleCube origin,GLSingleCube destination,int axis){
        float[][] colordata=origin.ColorArray;
        switch (axis){
            case Xaxis:
                destination.setColorArray(new float[][]{colordata[2],colordata[3],colordata[1],colordata[0],colordata[4],colordata[5]});
                break;
            case Yaxis:
                destination.setColorArray(new float[][]{colordata[0],colordata[1],colordata[4],colordata[5],colordata[3],colordata[2]});
                break;
            case Zaxis:
                destination.setColorArray(new float[][]{colordata[5],colordata[4],colordata[2],colordata[3],colordata[0],colordata[1]});
                break;
        }
    }

    public void copycubeNeg(GLSingleCube origin,GLSingleCube destination,int axis){
        float[][] colordata=origin.ColorArray;
        switch (axis){
            case Xaxis:
                destination.setColorArray(new float[][]{colordata[3],colordata[2],colordata[0],colordata[1],colordata[4],colordata[5]});
                break;
            case Yaxis:
                destination.setColorArray(new float[][]{colordata[0],colordata[1],colordata[5],colordata[4],colordata[2],colordata[3]});
                break;
            case Zaxis:
                destination.setColorArray(new float[][]{colordata[4],colordata[5],colordata[2],colordata[3],colordata[1],colordata[0]});
                break;
        }
    }


    public float[][][][] getVertices(){
        float[][][][] vertices=new float[2][2][2][4];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <2 ; j++) {
                for (int k = 0; k < 2; k++) {
                    float[] ans=new float[4];
                    //float[] temp=new float[]{(float)(2*(i-0.5)*factor),(float)(2*(j-0.5)*factor),(float)(2*(k-0.5)*factor),1f};
                    float[] temp=new float[]{(float)(2*(i-0.5)*0.25f),(float)(2*(j-0.5)*0.25f),(float)(2*(k-0.5)*0.25f),1f};
                    temp[0]+=xTranslates[2*i][2*j][2*k];
                    temp[1]+=yTranslates[2*i][2*j][2*k];
                    temp[2]+=zTranslates[2*i][2*j][2*k];
                   // Matrix.multiplyMV(vertices[i][j][k], 0, xMatrixes, 0,temp, 0 );
                   // Matrix.multiplyMV(vertices[i][j][k], 0, yMatrixes, 0, vertices[i][j][k], 0);
                    Matrix.multiplyMV(vertices[i][j][k], 0, xMatrixes, 0,temp, 0 );
                    Matrix.multiplyMV(vertices[i][j][k], 0, yMatrixes, 0, vertices[i][j][k], 0);

                    GLrenderer.devideByW(vertices[i][j][k]);
                }
            }
        }
        return vertices;
    }
    public float[][][][] getTriangles(){
        float[][][][] vertices=getVertices();
        float[][][][] triangles=new float[6][2][3][4];
        triangles[0][0]=new float[][]{vertices[0][1][0],vertices[0][0][0],vertices[1][0][0]};
        triangles[0][1]=new float[][]{vertices[1][0][0],vertices[1][1][0],vertices[0][1][0]};

        triangles[1][0]=new float[][]{vertices[0][1][1],vertices[0][0][1],vertices[1][0][1]};
        triangles[1][1]=new float[][]{vertices[1][0][1],vertices[1][1][1],vertices[0][1][1]};

        triangles[2][0]=new float[][]{vertices[0][1][0],vertices[0][0][0],vertices[0][0][1]};
        triangles[2][1]=new float[][]{vertices[0][1][0],vertices[0][1][1],vertices[0][0][1]};

        triangles[3][0]=new float[][]{vertices[1][1][0],vertices[1][0][0],vertices[1][0][1]};
        triangles[3][1]=new float[][]{vertices[1][1][0],vertices[1][1][1],vertices[1][0][1]};

        triangles[4][0]=new float[][]{vertices[1][0][0],vertices[0][0][0],vertices[0][0][1]};
        triangles[4][1]=new float[][]{vertices[1][0][0],vertices[1][0][1],vertices[0][0][1]};

        triangles[5][0]=new float[][]{vertices[1][1][0],vertices[0][1][0],vertices[0][1][1]};
        triangles[5][1]=new float[][]{vertices[1][1][0],vertices[1][1][1],vertices[0][1][1]};

        return triangles;
    }

    public  float[][] getCentrals(){
        float[][] centrals=new float[6][4];

        for (int i = 0; i < 6; i++) {
            centrals[i]=new float[]{0,0,0,1};

            switch (i){
                case 0:
                    centrals[i][2]-=1;
                    break;
                case 1:
                    centrals[i][2]+=1;
                    break;
                case 2:
                    centrals[i][0]-=1;
                    break;
                case 3:
                    centrals[i][0]+=1;
                    break;
                case 4:
                    centrals[i][1]-=1;
                    break;
                case 5:
                    centrals[i][1]+=1;
                    break;
            }
            Matrix.multiplyMV(centrals[i],0,xMatrixes,0,centrals[i],0);
            Matrix.multiplyMV(centrals[i], 0, yMatrixes, 0, centrals[i], 0);
            GLrenderer.devideByW(centrals[i]);
        }
        return centrals;
    }

    public void RandomRotate(){
        long t= SystemClock.currentThreadTimeMillis();
        Random LayerRandom =new Random(t);
        Random IndexRandom =new Random(t+429);
        Random dRandom =new Random(t+533429);
        for (int i = 0; i < 26; i++) {
            int layer=LayerRandom.nextInt(3);
            int index=IndexRandom.nextInt(3);
            boolean d=dRandom.nextBoolean();
            if(d){
                rotatePos(layer,index);
            }
            else {
                rotateNeg(layer,index);
            }

        }
    }

}
