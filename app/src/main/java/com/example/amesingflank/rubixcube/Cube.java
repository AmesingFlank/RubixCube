package com.example.amesingflank.rubixcube;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by lbj on 2015/12/26.
 */
public class Cube implements Serializable{
    public final int Xaxis = 0;
    public final int Yaxis = 1;
    public final int Zaxis = 2;
    public layer[][] layers = new layer[3][3];

    //initiate unchanged cube
    public Cube(){
        int[] orange=new int[3];
        for (int i = 0; i < 3; i++) {
            orange[i]=0;
        }

        int[] red=new int[3];
        for (int i = 0; i < 3; i++) {
            red[i]=1;
        }

        int[] blue=new int[3];
        for (int i = 0; i < 3; i++) {
            blue[i]=2;
        }

        int[] green=new int[3];
        for (int i = 0; i < 3; i++) {
            green[i]=3;
        }

        int[] yellow=new int[3];
        for (int i = 0; i < 3; i++) {
            yellow[i]=4;
        }

        int[] white=new int[3];
        for (int i = 0; i < 3; i++) {
            white[i]=5;
        }

        for(int i=0;i<3;i++){
            layers[Xaxis][i]=new layer(new int[][]{orange,green,red,blue},this,Xaxis,i);
        }
        for(int i=0;i<3;i++){
            layers[Yaxis][i]=new layer(new int[][]{blue,white,green,yellow},this,Yaxis,i);
        }
        for(int i=0;i<3;i++){
            layers[Zaxis][i]=new layer(new int[][]{yellow,red,white,orange},this,Zaxis,i);
        }
        solver=new Solver(this);

    }
    public void rotatePos(int ain,int iin){
        layers[ain][iin].rotatePos();
    }
    public void rotateNeg(int ain,int iin){
        layers[ain][iin].rotateNeg();
    }

    public Solver solver;


    public LinkedList<int[]> getSolution(){
        Cube temp=null;
        try {
            temp=(Cube)ObjectCloner.deepCopy(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OneTimeSolver ots=new OneTimeSolver(temp);
        return ots.getSolution();
    }

    public void ruinSolver(){
        solver=new Solver(this);
    }

}


class layer implements Serializable{
    public row[] rows=new row[4];
    public Cube cube;
    public int axis;
    int index;

    public final int Xaxis=0;
    public final int Yaxis=1;
    public final int Zaxis=2;
    //i[4][3]
    public layer(int[][] i,Cube ci,int ai,int ii){
        for (int j = 0; j < 4; j++) {
            rows[j]=new row(i[j],this,j);
        }
        cube=ci;
        axis=ai;
        index=ii;
    }
    public void rotateNeg(){
        for (int i = 0; i <3 ; i++) {
            rotatePos();
        }
    }
    public void rotatePos(){
        row temprow=rows[3].clone();
        for(int i=3;i>0;i--){
            rows[i]=rows[i-1].clone();

            //rows[i].index++;
        }
        rows[0]=temprow.clone();

        if(axis==Xaxis){
            if(index==0){
                row[] tr=new row[4];
                tr[0]= cube.layers[Yaxis][0].rows[3].clone();
                tr[1]= cube.layers[Zaxis][2].rows[0].clone();
                tr[2]= cube.layers[Yaxis][2].rows[3].clone();
                tr[3]= cube.layers[Zaxis][0].rows[0].clone();

                cube.layers[Yaxis][0].rows[3]=tr[3].clone();
                cube.layers[Zaxis][2].rows[0]=tr[0].clone().getinverserow();
                cube.layers[Yaxis][2].rows[3]=tr[1].clone();
                cube.layers[Zaxis][0].rows[0]=tr[2].getinverserow();

                cube.layers[Yaxis][1].rows[3].cells[0]=new cell(cube.layers[Zaxis][2].rows[0].cells[1].c.color);
                cube.layers[Yaxis][1].rows[3].cells[2]=new cell(cube.layers[Zaxis][0].rows[0].cells[1].c.color);
                cube.layers[Zaxis][1].rows[0].cells[0]=new cell(cube.layers[Yaxis][0].rows[3].cells[1].c.color);
                cube.layers[Zaxis][1].rows[0].cells[2]=new cell(cube.layers[Yaxis][2].rows[3].cells[1].c.color);


                row[] ttr=new row[4];
                for (int i = 0; i <4 ; i++) {
                    ttr[i]=this.rows[i].clone();
                }
                for (int i = 0; i <3 ; i++) {
                    cube.layers[Zaxis][i].rows[3].cells[2]=new cell(ttr[0].cells[i].c.color);
                    cube.layers[Zaxis][i].rows[1].cells[0]=new cell(ttr[2].cells[2-i].c.color);

                    cube.layers[Yaxis][i].rows[0].cells[0]=new cell(ttr[3].cells[2-i].c.color);
                    cube.layers[Yaxis][i].rows[2].cells[2]=new cell(ttr[1].cells[i].c.color);
                }


            }
            else{
                if(index==1){
                    row[] tr=new row[4];
                    for (int i = 0; i <4 ; i++) {
                        tr[i]=this.rows[i].clone();
                    }
                    for (int i = 0; i <3 ; i++) {
                        cube.layers[Zaxis][i].rows[3].cells[1]=new cell(tr[0].cells[i].c.color);
                        cube.layers[Zaxis][i].rows[1].cells[1]=new cell(tr[2].cells[2-i].c.color);

                        cube.layers[Yaxis][i].rows[0].cells[1]=new cell(tr[3].cells[2-i].c.color);
                        cube.layers[Yaxis][i].rows[2].cells[1]=new cell(tr[1].cells[i].c.color);
                    }
                }
                else{
                    if(index==2){
                        row[] tr=new row[4];
                        tr[0]= cube.layers[Yaxis][0].rows[1].clone();
                        tr[1]= cube.layers[Zaxis][2].rows[2].clone();
                        tr[2]= cube.layers[Yaxis][2].rows[1].clone();
                        tr[3]= cube.layers[Zaxis][0].rows[2].clone();

                        cube.layers[Yaxis][0].rows[1]=tr[3].clone();
                        cube.layers[Zaxis][2].rows[2]=tr[0].clone().getinverserow();
                        cube.layers[Yaxis][2].rows[1]=tr[1].clone();
                        cube.layers[Zaxis][0].rows[2]=tr[2].clone().getinverserow();

                        cube.layers[Yaxis][1].rows[1].cells[0]=new cell(cube.layers[Zaxis][0].rows[2].cells[1].c.color);
                        cube.layers[Yaxis][1].rows[1].cells[2]=new cell(cube.layers[Zaxis][2].rows[2].cells[1].c.color);
                        cube.layers[Zaxis][1].rows[2].cells[0]=new cell(cube.layers[Yaxis][2].rows[1].cells[1].c.color);
                        cube.layers[Zaxis][1].rows[2].cells[2]=new cell(cube.layers[Yaxis][0].rows[1].cells[1].c.color);


                        row[] ttr=new row[4];
                        for (int i = 0; i <4 ; i++) {
                            ttr[i]=this.rows[i].clone();
                        }
                        for (int i = 0; i <3 ; i++) {
                            cube.layers[Zaxis][i].rows[3].cells[0]=new cell(ttr[0].cells[i].c.color);
                            cube.layers[Zaxis][i].rows[1].cells[2]=new cell(ttr[2].cells[2-i].c.color);

                            cube.layers[Yaxis][i].rows[0].cells[2]=new cell(ttr[3].cells[2-i].c.color);
                            cube.layers[Yaxis][i].rows[2].cells[0]=new cell(ttr[1].cells[i].c.color);
                        }
                    }
                }
            }

        }
        else {
            if(axis==Yaxis){
                if(index==0){
                    row[] tr=new row[4];
                    tr[0]= cube.layers[Zaxis][0].rows[3].clone();
                    tr[1]= cube.layers[Xaxis][2].rows[0].clone();
                    tr[2]= cube.layers[Zaxis][2].rows[3].clone();
                    tr[3]= cube.layers[Xaxis][0].rows[0].clone();

                    cube.layers[Zaxis][0].rows[3]=tr[3].clone();
                    cube.layers[Xaxis][2].rows[0]=tr[0].clone().getinverserow();
                    cube.layers[Zaxis][2].rows[3]=tr[1].clone();
                    cube.layers[Xaxis][0].rows[0]=tr[2].getinverserow();

                    cube.layers[Zaxis][1].rows[3].cells[0]=new cell(cube.layers[Xaxis][2].rows[0].cells[1].c.color);
                    cube.layers[Zaxis][1].rows[3].cells[2]=new cell(cube.layers[Xaxis][0].rows[0].cells[1].c.color);
                    cube.layers[Xaxis][1].rows[0].cells[0]=new cell(cube.layers[Zaxis][0].rows[3].cells[1].c.color);
                    cube.layers[Xaxis][1].rows[0].cells[2]=new cell(cube.layers[Zaxis][2].rows[3].cells[1].c.color);


                    row[] ttr=new row[4];
                    for (int i = 0; i <4 ; i++) {
                        ttr[i]=this.rows[i].clone();
                    }
                    for (int i = 0; i <3 ; i++) {
                        cube.layers[Xaxis][i].rows[3].cells[2]=new cell(ttr[0].cells[i].c.color);
                        cube.layers[Xaxis][i].rows[1].cells[0]=new cell(ttr[2].cells[2-i].c.color);

                        cube.layers[Zaxis][i].rows[0].cells[0]=new cell(ttr[3].cells[2-i].c.color);
                        cube.layers[Zaxis][i].rows[2].cells[2]=new cell(ttr[1].cells[i].c.color);
                    }


                }
                else{
                    if(index==1){
                        row[] tr=new row[4];
                        for (int i = 0; i <4 ; i++) {
                            tr[i]=this.rows[i].clone();
                        }
                        for (int i = 0; i <3 ; i++) {
                            cube.layers[Xaxis][i].rows[3].cells[1]=new cell(tr[0].cells[i].c.color);
                            cube.layers[Xaxis][i].rows[1].cells[1]=new cell(tr[2].cells[2-i].c.color);

                            cube.layers[Zaxis][i].rows[0].cells[1]=new cell(tr[3].cells[2-i].c.color);
                            cube.layers[Zaxis][i].rows[2].cells[1]=new cell(tr[1].cells[i].c.color);
                        }
                    }
                    else{
                        if(index==2){
                            row[] tr=new row[4];
                            tr[0]= cube.layers[Zaxis][0].rows[1].clone();
                            tr[1]= cube.layers[Xaxis][2].rows[2].clone();
                            tr[2]= cube.layers[Zaxis][2].rows[1].clone();
                            tr[3]= cube.layers[Xaxis][0].rows[2].clone();

                            cube.layers[Zaxis][0].rows[1]=tr[3].clone();
                            cube.layers[Xaxis][2].rows[2]=tr[0].clone().getinverserow();
                            cube.layers[Zaxis][2].rows[1]=tr[1].clone();
                            cube.layers[Xaxis][0].rows[2]=tr[2].clone().getinverserow();

                            cube.layers[Zaxis][1].rows[1].cells[0]=new cell(cube.layers[Xaxis][0].rows[2].cells[1].c.color);
                            cube.layers[Zaxis][1].rows[1].cells[2]=new cell(cube.layers[Xaxis][2].rows[2].cells[1].c.color);
                            cube.layers[Xaxis][1].rows[2].cells[0]=new cell(cube.layers[Zaxis][2].rows[1].cells[1].c.color);
                            cube.layers[Xaxis][1].rows[2].cells[2]=new cell(cube.layers[Zaxis][0].rows[1].cells[1].c.color);


                            row[] ttr=new row[4];
                            for (int i = 0; i <4 ; i++) {
                                ttr[i]=this.rows[i].clone();
                            }
                            for (int i = 0; i <3 ; i++) {
                                cube.layers[Xaxis][i].rows[3].cells[0]=new cell(ttr[0].cells[i].c.color);
                                cube.layers[Xaxis][i].rows[1].cells[2]=new cell(ttr[2].cells[2-i].c.color);

                                cube.layers[Zaxis][i].rows[0].cells[2]=new cell(ttr[3].cells[2-i].c.color);
                                cube.layers[Zaxis][i].rows[2].cells[0]=new cell(ttr[1].cells[i].c.color);
                            }
                        }
                    }
                }


            }
            else {
                if(axis==Zaxis){
                    if(index==0){
                        row[] tr=new row[4];
                        tr[0]= cube.layers[Xaxis][0].rows[3].clone();
                        tr[1]= cube.layers[Yaxis][2].rows[0].clone();
                        tr[2]= cube.layers[Xaxis][2].rows[3].clone();
                        tr[3]= cube.layers[Yaxis][0].rows[0].clone();

                        cube.layers[Xaxis][0].rows[3]=tr[3].clone();
                        cube.layers[Yaxis][2].rows[0]=tr[0].clone().getinverserow();
                        cube.layers[Xaxis][2].rows[3]=tr[1].clone();
                        cube.layers[Yaxis][0].rows[0]=tr[2].getinverserow();

                        cube.layers[Xaxis][1].rows[3].cells[0]=new cell(cube.layers[Yaxis][2].rows[0].cells[1].c.color);
                        cube.layers[Xaxis][1].rows[3].cells[2]=new cell(cube.layers[Yaxis][0].rows[0].cells[1].c.color);
                        cube.layers[Yaxis][1].rows[0].cells[0]=new cell(cube.layers[Xaxis][0].rows[3].cells[1].c.color);
                        cube.layers[Yaxis][1].rows[0].cells[2]=new cell(cube.layers[Xaxis][2].rows[3].cells[1].c.color);


                        row[] ttr=new row[4];
                        for (int i = 0; i <4 ; i++) {
                            ttr[i]=this.rows[i].clone();
                        }
                        for (int i = 0; i <3 ; i++) {
                            cube.layers[Yaxis][i].rows[3].cells[2]=new cell(ttr[0].cells[i].c.color);
                            cube.layers[Yaxis][i].rows[1].cells[0]=new cell(ttr[2].cells[2-i].c.color);

                            cube.layers[Xaxis][i].rows[0].cells[0]=new cell(ttr[3].cells[2-i].c.color);
                            cube.layers[Xaxis][i].rows[2].cells[2]=new cell(ttr[1].cells[i].c.color);
                        }


                    }
                    else{
                        if(index==1){
                            row[] tr=new row[4];
                            for (int i = 0; i <4 ; i++) {
                                tr[i]=this.rows[i].clone();
                            }
                            for (int i = 0; i <3 ; i++) {
                                cube.layers[Yaxis][i].rows[3].cells[1]=new cell(tr[0].cells[i].c.color);
                                cube.layers[Yaxis][i].rows[1].cells[1]=new cell(tr[2].cells[2-i].c.color);

                                cube.layers[Xaxis][i].rows[0].cells[1]=new cell(tr[3].cells[2-i].c.color);
                                cube.layers[Xaxis][i].rows[2].cells[1]=new cell(tr[1].cells[i].c.color);
                            }
                        }
                        else{
                            if(index==2){
                                row[] tr=new row[4];
                                tr[0]= cube.layers[Xaxis][0].rows[1].clone();
                                tr[1]= cube.layers[Yaxis][2].rows[2].clone();
                                tr[2]= cube.layers[Xaxis][2].rows[1].clone();
                                tr[3]= cube.layers[Yaxis][0].rows[2].clone();

                                cube.layers[Xaxis][0].rows[1]=tr[3].clone();
                                cube.layers[Yaxis][2].rows[2]=tr[0].clone().getinverserow();
                                cube.layers[Xaxis][2].rows[1]=tr[1].clone();
                                cube.layers[Yaxis][0].rows[2]=tr[2].clone().getinverserow();

                                cube.layers[Xaxis][1].rows[1].cells[0]=new cell(cube.layers[Yaxis][0].rows[2].cells[1].c.color);
                                cube.layers[Xaxis][1].rows[1].cells[2]=new cell(cube.layers[Yaxis][2].rows[2].cells[1].c.color);
                                cube.layers[Yaxis][1].rows[2].cells[0]=new cell(cube.layers[Xaxis][2].rows[1].cells[1].c.color);
                                cube.layers[Yaxis][1].rows[2].cells[2]=new cell(cube.layers[Xaxis][0].rows[1].cells[1].c.color);


                                row[] ttr=new row[4];
                                for (int i = 0; i <4 ; i++) {
                                    ttr[i]=this.rows[i].clone();
                                }
                                for (int i = 0; i <3 ; i++) {
                                    cube.layers[Yaxis][i].rows[3].cells[0]=new cell(ttr[0].cells[i].c.color);
                                    cube.layers[Yaxis][i].rows[1].cells[2]=new cell(ttr[2].cells[2-i].c.color);

                                    cube.layers[Xaxis][i].rows[0].cells[2]=new cell(ttr[3].cells[2-i].c.color);
                                    cube.layers[Xaxis][i].rows[2].cells[0]=new cell(ttr[1].cells[i].c.color);
                                }
                            }
                        }
                    }


                }
            }
        }
    }
}

class row implements Serializable{
    public cell[] cells=new cell[3];
    public layer l;
    int index;
    public  row (int[] i,layer li,int ii){
        for (int j = 0; j < 3; j++) {
            //System.out.println(i[j]);
            cells[j]=new cell(i[j]);
        }
        l=li;
        index=ii;
    }
    public row getinverserow(){
        row out=this.clone();
        out.cells[0]=this.cells[2];
        out.cells[2]=this.cells[0];
        return out;
    }
    public row clone(){
        row out=this;
        out=new row(new int[]{cells[0].c.color,cells[1].c.color,cells[2].c.color},l,index);
        return out;
    }
    public boolean ReadyforFinal(){
        if (cells[0].c.color==cells[2].c.color){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean XOX(){
        if(cells[0].c.color==cells[2].c.color && cells[0].c.color!=cells[1].c.color){
            return true;
        }
        else return false;
    }
    public boolean finished(){
        if(ReadyforFinal()&&cells[0].c.color==cells[1].c.color){
            return true;
        }
        else return false;
    }
}

class cell implements Serializable{
    public color c;
    public cell(int i){
        c=new color(i);
    }
}
class color implements Serializable{
    public int color;
    public static final int orange=0;
    public static final int red=1;
    public static final int blue=2;
    public static final int green=3;
    public static final int yellow=4;
    public static final int white=5;
    public String nameofcolor;
    public color (int i){
        this.color=i;
        switch (i){
            case 0:
                nameofcolor="orange";
                break;
            case 1:
                nameofcolor="red";
                break;
            case 2:
                nameofcolor="blue";
                break;
            case 3 :
                nameofcolor="green";
                break;
            case 4:
                nameofcolor="yellow";
                break;
            case 5:
                nameofcolor="white";
                break;
        }
    }
}
