package com.example.amesingflank.rubixcube;

import java.util.LinkedList;

/**
 * Created by AmesingFlank on 16/1/21.
 */
public class JavaCube implements Cloneable{
    public JavaSingleCube[][][] singleCubes=new JavaSingleCube[3][3][3];
    public final int Xaxis=0;
    public final int Yaxis=1;
    public final int Zaxis=2;
    
    public JavaCube(){
        float[][][][][] Colordata=GLCubeColor.Cube27data();
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                for (int k = 0; k <3 ; k++) {
                    singleCubes[i][j][k]=new JavaSingleCube(Colordata[i][j][k],i,j,k);
                }
            }
        }
    }


    public void rotatePos(int ain,int iin){
        //正方向旋转修改颜色。
        JavaSingleCube temp0;
        JavaSingleCube temp1;
        switch (ain){
            case Xaxis:
                temp0=new JavaSingleCube(singleCubes[0][iin][0].ColorArray,1,1,1);
                temp1=new JavaSingleCube(singleCubes[1][iin][0].ColorArray,1,1,1);
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
                temp0=new JavaSingleCube(singleCubes[0][0][iin].ColorArray,1,1,1);
                temp1=new JavaSingleCube(singleCubes[0][1][iin].ColorArray,1,1,1);
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
                temp0=new JavaSingleCube(singleCubes[iin][0][0].ColorArray,1,1,1);
                temp1=new JavaSingleCube(singleCubes[iin][0][1].ColorArray,1,1,1);
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
        
    }

    public void rotateNeg(int ain,int iin){
        //反方向旋转修改颜色
        JavaSingleCube temp0;
        JavaSingleCube temp1;
        switch (ain){
            case Xaxis:
                temp0=new JavaSingleCube(singleCubes[0][iin][0].ColorArray,1,1,1);
                temp1=new JavaSingleCube(singleCubes[1][iin][0].ColorArray,1,1,1);
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
                temp0=new JavaSingleCube(singleCubes[0][0][iin].ColorArray,1,1,1);
                temp1=new JavaSingleCube(singleCubes[0][1][iin].ColorArray,1,1,1);
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
                temp0=new JavaSingleCube(singleCubes[iin][0][0].ColorArray,1,1,1);
                temp1=new JavaSingleCube(singleCubes[iin][0][1].ColorArray,1,1,1);
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

    }
    
    public void copycubePos(JavaSingleCube origin,JavaSingleCube destination,int axis){
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
    public void copycubeNeg(JavaSingleCube origin,JavaSingleCube destination,int axis){
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

    public static void SyncWithGLSingleCube(GLSingleCube glsc,JavaSingleCube jsc){
        float[][] colordata=glsc.ColorArray;
        jsc.setColorArray(new float[][]{colordata[0].clone(),colordata[1].clone(),colordata[2].clone(),colordata[3].clone(),colordata[4].clone(),colordata[5].clone()});
    }
    public void SyncWithGLCube(GLRubikCube glrc){
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    SyncWithGLSingleCube(glrc.singleCubes[x][y][z],singleCubes[x][y][z]);
                }
            }
        }
    }

    public LinkedList<Move> getSolution(){
        JavaCube temp=clone();
        RoutineSolver rs=new RoutineSolver(temp);
        LinkedList<Move> solution=rs.getSolution();
        return solution;
    }

    public JavaCube clone(){
        JavaCube clone=new JavaCube();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    clone.singleCubes[x][y][z]=singleCubes[x][y][z].clone();
                }
            }
        }
        return clone;
    }
}
class JavaSingleCube implements Cloneable{
    float[][] ColorArray=GLCubeColor.ColorArray;
    int X=1;
    int Y=1;
    int Z=1;
    public JavaSingleCube(float[][] cin,int x,int y,int z){
        ColorArray=cin;
        X=x;
        Y=y;
        Z=z;
    }
    public void setColorArray(float[][] cin){
        ColorArray=cin;
    }
    public JavaSingleCube clone(){
        JavaSingleCube clone=new JavaSingleCube(CopyColorArray(),X,Y,Z);
        return clone;
    }
    public float[][] CopyColorArray(){
        int l0=ColorArray.length;
        int l1=ColorArray[0].length;
        float[][] out =new float[l0][l1];
        for (int i = 0; i < l0; i++) {
            for (int j = 0; j < l1; j++) {
                out[i][j]=ColorArray[i][j];
            }
        }
        return out;
    }
}
