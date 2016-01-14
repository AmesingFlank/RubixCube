package com.example.amesingflank.rubixcube;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by AmesingFlank on 15/12/28.
 */
public class Solver implements Serializable{

    public final int Xaxis = 0;
    public final int Yaxis = 1;
    public final int Zaxis = 2;
    public int yellowRepresentative=color.yellow;
    public int whiteRepresentative=color.white;
    Cube Jcube;
    public Solver(Cube Jin){
        Jcube=Jin;
        //setColor();
    }
    public void setColor(){
        yellowRepresentative=Jcube.layers[Zaxis][1].rows[2].cells[1].c.color;
        whiteRepresentative=Jcube.layers[Zaxis][1].rows[0].cells[1].c.color;
    }

    private int routineIndex=1;
    private boolean onRoutine=false;
    private int[][] currentRoutine;

    boolean haveNextSingleInstruction=false;
    int[] nextSingleInstruction;

    boolean finishedWhiteCrossWithYellowCenter=false;
    boolean finished1stLayer=false;
    boolean finished2ndLayer=false;
    boolean finishedYellowCross=false;
    boolean finishedTopYellowFace=false;
    boolean readyforFinal=false;
    boolean finishedEverything;


    public int[] nextMove(){
        if (finishedEverything || checkFinished()){
            return new int[]{999,999,999};
        }

        if(haveNextSingleInstruction){
            haveNextSingleInstruction=false;
            int[] NSItemp=nextSingleInstruction.clone();
            nextSingleInstruction=new int[3];
            return NSItemp;
        }
        if(onRoutine){
            if (routineIndex==currentRoutine.length){
                onRoutine=false;
                routineIndex=1;

                return nextMove();
            }
            routineIndex++;
            return currentRoutine[routineIndex-1];
        }
        else{
            if(Jcube.layers[Yaxis][0].rows[1].cells[1].c.color==whiteRepresentative &&
                    Jcube.layers[Yaxis][1].rows[1].cells[0].c.color==whiteRepresentative &&
                    Jcube.layers[Yaxis][1].rows[1].cells[2].c.color==whiteRepresentative &&
                    Jcube.layers[Yaxis][2].rows[1].cells[1].c.color==whiteRepresentative ){
                finishedWhiteCrossWithYellowCenter=true;
            }
            if(!finishedWhiteCrossWithYellowCenter){
                if(Jcube.layers[Zaxis][1].rows[2].cells[1].c.color!=yellowRepresentative){
                    return MoveYellowCentral();
                }
                if(!finishedWhiteCrossWithYellowCenter ){
                    return WhiteCrossWithYellowCentral();
                }

            }
            if(!finished1stLayer){
                if(Jcube.layers[Yaxis][1].rows[1].cells[1].c.color!=whiteRepresentative && (Jcube.layers[Yaxis][0].rows[3].cells[1].c.color!=whiteRepresentative||
                        Jcube.layers[Yaxis][2].rows[3].cells[1].c.color!=whiteRepresentative ||
                        Jcube.layers[Yaxis][1].rows[3].cells[0].c.color!=whiteRepresentative ||
                        Jcube.layers[Yaxis][1].rows[3].cells[2].c.color!=whiteRepresentative)){
                    return getWhiteCenter();
                }
                if(Jcube.layers[Yaxis][1].rows[1].cells[1].c.color!=whiteRepresentative || (Jcube.layers[Yaxis][0].rows[1].cells[1].c.color!=whiteRepresentative||
                        Jcube.layers[Yaxis][2].rows[1].cells[1].c.color!=whiteRepresentative ||
                        Jcube.layers[Yaxis][1].rows[1].cells[0].c.color!=whiteRepresentative ||
                        Jcube.layers[Yaxis][1].rows[1].cells[2].c.color!=whiteRepresentative)){
                    this.currentRoutine=ReverseWholeCubeByZ();
                    onRoutine=true;
                    return new int[]{2,0,1};
                }
                if(Jcube.layers[Yaxis][0].rows[1].cells[0].c.color!=whiteRepresentative||
                        Jcube.layers[Yaxis][0].rows[1].cells[2].c.color!=whiteRepresentative||
                        Jcube.layers[Yaxis][2].rows[1].cells[0].c.color!=whiteRepresentative||
                        Jcube.layers[Yaxis][2].rows[1].cells[2].c.color!=whiteRepresentative||
                        Jcube.layers[Xaxis][2].rows[0].cells[0].c.color!=Jcube.layers[Xaxis][2].rows[0].cells[1].c.color||
                        Jcube.layers[Xaxis][2].rows[0].cells[1].c.color!=Jcube.layers[Xaxis][2].rows[0].cells[2].c.color||
                        Jcube.layers[Xaxis][2].rows[1].cells[0].c.color!=Jcube.layers[Xaxis][2].rows[1].cells[1].c.color||
                        Jcube.layers[Xaxis][2].rows[1].cells[1].c.color!=Jcube.layers[Xaxis][2].rows[1].cells[2].c.color||
                        Jcube.layers[Xaxis][2].rows[2].cells[0].c.color!=Jcube.layers[Xaxis][2].rows[2].cells[1].c.color||
                        Jcube.layers[Xaxis][2].rows[2].cells[1].c.color!=Jcube.layers[Xaxis][2].rows[2].cells[2].c.color||
                        Jcube.layers[Xaxis][2].rows[3].cells[0].c.color!=Jcube.layers[Xaxis][2].rows[3].cells[1].c.color||
                        Jcube.layers[Xaxis][2].rows[3].cells[1].c.color!=Jcube.layers[Xaxis][2].rows[3].cells[2].c.color){
                    return get1stLayer();
                }
                else {
                    finished1stLayer=true;
                }
            }

            if (finished1stLayer){
                if(Jcube.layers[Zaxis][1].rows[2].cells[1].c.color==whiteRepresentative){
                    onRoutine=true;
                    currentRoutine=ReverseWholeCubeByZ();
                    return currentRoutine[0];
                }

                if (!finished2ndLayer){
                    if(Jcube.layers[Xaxis][1].rows[0].cells[0].c.color!=Jcube.layers[Xaxis][1].rows[0].cells[1].c.color||
                            Jcube.layers[Xaxis][1].rows[0].cells[1].c.color!=Jcube.layers[Xaxis][1].rows[0].cells[2].c.color||
                            Jcube.layers[Xaxis][1].rows[1].cells[0].c.color!=Jcube.layers[Xaxis][1].rows[1].cells[1].c.color||
                            Jcube.layers[Xaxis][1].rows[1].cells[1].c.color!=Jcube.layers[Xaxis][1].rows[1].cells[2].c.color||
                            Jcube.layers[Xaxis][1].rows[2].cells[0].c.color!=Jcube.layers[Xaxis][1].rows[2].cells[1].c.color||
                            Jcube.layers[Xaxis][1].rows[2].cells[1].c.color!=Jcube.layers[Xaxis][1].rows[2].cells[2].c.color||
                            Jcube.layers[Xaxis][1].rows[3].cells[0].c.color!=Jcube.layers[Xaxis][1].rows[3].cells[1].c.color||
                            Jcube.layers[Xaxis][1].rows[3].cells[1].c.color!=Jcube.layers[Xaxis][1].rows[3].cells[2].c.color){


                        if(Jcube.layers[Xaxis][2].rows[0].cells[1].c.color!=yellowRepresentative &&
                                Jcube.layers[Yaxis][0].rows[1].cells[1].c.color!=yellowRepresentative){
                            if(Jcube.layers[Xaxis][2].rows[0].cells[1].c.color==Jcube.layers[Xaxis][1].rows[0].cells[1].c.color){
                                if(Jcube.layers[Yaxis][0].rows[1].cells[1].c.color==Jcube.layers[Xaxis][1].rows[3].cells[1].c.color){
                                    onRoutine=true;
                                    currentRoutine=SecondLayer0();
                                    return currentRoutine[0];
                                }
                                if(Jcube.layers[Yaxis][0].rows[1].cells[1].c.color==Jcube.layers[Xaxis][1].rows[1].cells[1].c.color){
                                    onRoutine=true;
                                    currentRoutine=SecondLayer1();
                                    return currentRoutine[0];
                                }
                            }
                            else {
                                haveNextSingleInstruction=true;
                                nextSingleInstruction=new int[]{0,1,1};
                                return new int[]{0,0,1};
                            }
                        }
                        else {

                            if((Jcube.layers[Yaxis][0].rows[1].cells[1].c.color==yellowRepresentative ||Jcube.layers[Xaxis][2].rows[0].cells[1].c.color==yellowRepresentative ) &&
                                    (Jcube.layers[Yaxis][1].rows[1].cells[0].c.color==yellowRepresentative ||Jcube.layers[Xaxis][2].rows[3].cells[1].c.color==yellowRepresentative ) &&
                                    (Jcube.layers[Yaxis][1].rows[1].cells[2].c.color==yellowRepresentative ||Jcube.layers[Xaxis][2].rows[1].cells[1].c.color==yellowRepresentative ) &&
                                    (Jcube.layers[Yaxis][2].rows[1].cells[1].c.color==yellowRepresentative ||Jcube.layers[Xaxis][2].rows[2].cells[1].c.color==yellowRepresentative ) ){
                                if(Jcube.layers[Xaxis][1].rows[0].cells[0].c.color!=Jcube.layers[Xaxis][1].rows[0].cells[1].c.color ||
                                        Jcube.layers[Xaxis][1].rows[3].cells[1].c.color!=Jcube.layers[Xaxis][1].rows[3].cells[2].c.color){
                                    onRoutine=true;
                                    currentRoutine=SecondLayer0();
                                    return currentRoutine[0];
                                }
                                if(Jcube.layers[Xaxis][1].rows[0].cells[2].c.color!=Jcube.layers[Xaxis][1].rows[0].cells[1].c.color &&
                                        Jcube.layers[Xaxis][1].rows[1].cells[1].c.color!=Jcube.layers[Xaxis][1].rows[1].cells[0].c.color){
                                    onRoutine=true;
                                    currentRoutine=SecondLayer1();
                                    return currentRoutine[0];
                                }
                                haveNextSingleInstruction=true;
                                nextSingleInstruction=new int[]{0,1,1};
                                return new int[]{0,0,1};

                            }
                            else {
                                return new int[]{0,2,1};
                            }

                        }

                    }
                    else {
                        finished2ndLayer=true;
                    }
                }

                if(finished2ndLayer){
                    if(Jcube.layers[Yaxis][0].rows[1].cells[1].c.color==yellowRepresentative &&
                            Jcube.layers[Yaxis][1].rows[1].cells[0].c.color==yellowRepresentative &&
                            Jcube.layers[Yaxis][1].rows[1].cells[2].c.color==yellowRepresentative &&
                            Jcube.layers[Yaxis][2].rows[1].cells[1].c.color==yellowRepresentative){
                        finishedYellowCross=true;
                    }
                    if(!finishedYellowCross){
                        if(( Jcube.layers[Yaxis][0].rows[1].cells[1].c.color==yellowRepresentative &&
                                (Jcube.layers[Yaxis][2].rows[1].cells[1].c.color==yellowRepresentative || Jcube.layers[Yaxis][1].rows[1].cells[0].c.color==yellowRepresentative)) ||
                                (Jcube.layers[Xaxis][2].rows[0].cells[1].c.color==yellowRepresentative && Jcube.layers[Xaxis][2].rows[3].cells[1].c.color==yellowRepresentative)){
                            onRoutine=true;
                            currentRoutine=YellowCrossRoutine();
                            return currentRoutine[0];
                        }
                        else {
                            return new int[]{0,2,1};
                        }
                    }
                    if(finishedYellowCross){
                        if(!finishedTopYellowFace){
                            if(Jcube.layers[Yaxis][0].rows[1].cells[0].c.color==yellowRepresentative &&
                                    Jcube.layers[Yaxis][0].rows[1].cells[1].c.color==yellowRepresentative &&
                                    Jcube.layers[Yaxis][0].rows[1].cells[2].c.color==yellowRepresentative &&
                                    Jcube.layers[Yaxis][1].rows[1].cells[0].c.color==yellowRepresentative &&
                                    Jcube.layers[Yaxis][1].rows[1].cells[1].c.color==yellowRepresentative &&
                                    Jcube.layers[Yaxis][1].rows[1].cells[2].c.color==yellowRepresentative &&
                                    Jcube.layers[Yaxis][2].rows[1].cells[0].c.color==yellowRepresentative &&
                                    Jcube.layers[Yaxis][2].rows[1].cells[1].c.color==yellowRepresentative &&
                                    Jcube.layers[Yaxis][2].rows[1].cells[2].c.color==yellowRepresentative ){
                                finishedTopYellowFace=true;
                            }
                            else {
                                return getTopYellowFace();
                            }
                        }

                        if(finishedTopYellowFace){
                            if(!readyforFinal){
                                if(Jcube.layers[Xaxis][2].rows[0].ReadyforFinal() &&
                                        Jcube.layers[Xaxis][2].rows[1].ReadyforFinal() &&
                                        Jcube.layers[Xaxis][2].rows[2].ReadyforFinal() &&
                                        Jcube.layers[Xaxis][2].rows[3].ReadyforFinal() && Jcube.layers[Yaxis][1].rows[1].cells[1].c.color==yellowRepresentative){
                                    readyforFinal=true;
                                }
                                else {
                                    return getReadyForFinal();
                                }
                            }
                            if (readyforFinal){
                                if(Jcube.layers[Xaxis][2].rows[0].cells[0].c.color==Jcube.layers[Xaxis][2].rows[0].cells[1].c.color &&
                                        Jcube.layers[Xaxis][2].rows[0].cells[2].c.color==Jcube.layers[Xaxis][2].rows[0].cells[1].c.color &&
                                        Jcube.layers[Xaxis][2].rows[1].cells[0].c.color==Jcube.layers[Xaxis][2].rows[1].cells[1].c.color &&
                                        Jcube.layers[Xaxis][2].rows[1].cells[2].c.color==Jcube.layers[Xaxis][2].rows[1].cells[1].c.color &&
                                        Jcube.layers[Xaxis][2].rows[2].cells[0].c.color==Jcube.layers[Xaxis][2].rows[2].cells[1].c.color &&
                                        Jcube.layers[Xaxis][2].rows[2].cells[2].c.color==Jcube.layers[Xaxis][2].rows[2].cells[1].c.color &&
                                        Jcube.layers[Xaxis][2].rows[3].cells[0].c.color==Jcube.layers[Xaxis][2].rows[3].cells[1].c.color &&
                                        Jcube.layers[Xaxis][2].rows[3].cells[2].c.color==Jcube.layers[Xaxis][2].rows[3].cells[1].c.color &&

                                        Jcube.layers[Xaxis][2].rows[0].cells[1].c.color==Jcube.layers[Xaxis][1].rows[0].cells[1].c.color){
                                    finishedEverything=true;
                                }
                                else {
                                    return getFinal();
                                }
                                if (finishedEverything){
                                    return new int[]{999,999,999};
                                }

                            }

                        }
                    }

                }
                
            }

        }

        return new int[]{4,4,4};


    }
    public int[] MoveYellowCentral(){
        if (Jcube.layers[Xaxis][1].rows[0].cells[1].c.color==yellowRepresentative){
            return new int[]{2,1,-1};
        }
        else {
            if (Jcube.layers[Xaxis][1].rows[2].cells[1].c.color==yellowRepresentative){
                return new int[]{2,1,1};
            }
            else {
                if(Jcube.layers[Zaxis][1].rows[0].cells[1].c.color==yellowRepresentative){
                    return new int[]{2,1,1};
                }
                else {
                    if(Jcube.layers[Yaxis][1].rows[0].cells[1].c.color==yellowRepresentative){
                        return new int[]{1,1,1};
                    }
                    else {
                        if(Jcube.layers[Yaxis][1].rows[2].cells[1].c.color==yellowRepresentative){
                            return new int[]{1,1,-1};
                        }
                        else {
                            return new int[]{4,4,4};
                        }
                    }
                }
            }
        }
    }
    public int[] WhiteCrossWithYellowCentral(){
        if(Jcube.layers[Yaxis][0].rows[1].cells[1].c.color==whiteRepresentative){
            return new int[]{0,2,1};
        }
        else {
            if(Jcube.layers[Xaxis][2].rows[0].cells[1].c.color==whiteRepresentative){
                return new int[]{1,0,1};
            }
        }
        if(Jcube.layers[Xaxis][1].rows[1].cells[0].c.color==whiteRepresentative){
            return new int[]{1,0,-1};
        }
        if(Jcube.layers[Xaxis][1].rows[3].cells[2].c.color==whiteRepresentative){
            return new int[]{1,0,1};
        }
        boolean X1Flag =false;
        xx:for (int i = 0; i <4 ; i++) {
            if(Jcube.layers[Xaxis][1].rows[i].cells[0].c.color==whiteRepresentative ||
                    Jcube.layers[Xaxis][1].rows[i].cells[2].c.color==whiteRepresentative){
                X1Flag=true;
            }

        }
        if(X1Flag){
            return new int[]{0,1,1};
        }

        if(Jcube.layers[Xaxis][0].rows[0].cells[1].c.color==whiteRepresentative){
            return new int[]{1,0,1};
        }

        if(Jcube.layers[Yaxis][0].rows[3].cells[1].c.color==whiteRepresentative){
            return new int[]{1,0,1};
        }
        else {
            return new int[]{0,0,1};
        }
    }

    public int[] getWhiteCenter(){
        if(Jcube.layers[Yaxis][0].rows[1].cells[1].c.color!=whiteRepresentative){
            return new int[]{0,2,1};
        }
        if(Jcube.layers[Xaxis][1].rows[0].cells[1].c.color!=Jcube.layers[Xaxis][2].rows[0].cells[1].c.color){
            haveNextSingleInstruction=true;
            nextSingleInstruction=new int[]{0,0,1};
            return new int[]{0,1,1};
        }
        haveNextSingleInstruction=true;
        nextSingleInstruction=new int[]{1,0,1};
        return new int[]{1,0,1};
    }

    public int[][] ReverseWholeCubeByZ(){
        int[][] ans=new int[6][3];
        ans[0]=new int[]{2,0,1};
        ans[1]=new int[]{2,0,1};
        ans[2]=new int[]{2,1,1};
        ans[3]=new int[]{2,1,1};
        ans[4]=new int[]{2,2,1};
        ans[5]=new int[]{2,2,1};
        return ans;
    }
    public int[] get1stLayer(){
        if (Jcube.layers[Xaxis][0].rows[0].cells[0].c.color==whiteRepresentative ){
            if(Jcube.layers[Xaxis][0].rows[3].cells[2].c.color==Jcube.layers[Xaxis][2].rows[3].cells[1].c.color &&
                    Jcube.layers[Zaxis][0].rows[0].cells[0].c.color==Jcube.layers[Xaxis][2].rows[0].cells[1].c.color){
                currentRoutine=WhiteCorner0();
                onRoutine=true;
                return new int[]{1,0,-1};
            }
            else {
                haveNextSingleInstruction=true;
                nextSingleInstruction=new int[]{0,1,1};
                return new int[]{0,2,1};
            }
        }
        if (Jcube.layers[Xaxis][0].rows[3].cells[2].c.color==whiteRepresentative ){
            if(Jcube.layers[Zaxis][0].rows[0].cells[0].c.color==Jcube.layers[Xaxis][2].rows[3].cells[1].c.color &&
                    Jcube.layers[Xaxis][0].rows[0].cells[0].c.color==Jcube.layers[Xaxis][2].rows[0].cells[1].c.color){
                currentRoutine=WhiteCorner1();
                onRoutine=true;
                return new int[]{2,0,1};
            }
            else {
                haveNextSingleInstruction=true;
                nextSingleInstruction=new int[]{0,1,1};
                return new int[]{0,2,1};
            }
        }
        if(Jcube.layers[Zaxis][0].rows[0].cells[0].c.color==whiteRepresentative  ){
            if(!(Jcube.layers[Zaxis][0].rows[2].cells[2].c.color==whiteRepresentative &&
                            Jcube.layers[Xaxis][2].rows[0].cells[0].c.color== Jcube.layers[Xaxis][2].rows[0].cells[1].c.color&&
                            Jcube.layers[Xaxis][2].rows[3].cells[2].c.color== Jcube.layers[Xaxis][2].rows[3].cells[1].c.color)){
                if(Jcube.layers[Xaxis][0].rows[0].cells[2].c.color==whiteRepresentative){
                    return new int[]{0,0,-1};
                }
                currentRoutine=MoveBottomWhite();
                onRoutine=true;
                return new int[]{2,0,1};
            }
            else {
                haveNextSingleInstruction=true;
                nextSingleInstruction=new int[]{0,1,1};
                return new int[]{0,2,1};
            }
        }
        if(Jcube.layers[Xaxis][2].rows[0].cells[0].c.color==whiteRepresentative  ){
            currentRoutine=WhiteCorner0();
            onRoutine=true;
            return new int[]{1,0,-1};
        }
        if(Jcube.layers[Xaxis][2].rows[3].cells[2].c.color==whiteRepresentative  ){
            currentRoutine=WhiteCorner1();
            onRoutine=true;
            return new int[]{2,0,1};
        }
        if(Jcube.layers[Zaxis][0].rows[2].cells[2].c.color==whiteRepresentative &&
                !(Jcube.layers[Xaxis][2].rows[0].cells[0].c.color== Jcube.layers[Xaxis][2].rows[0].cells[1].c.color&&
                Jcube.layers[Xaxis][2].rows[3].cells[2].c.color== Jcube.layers[Xaxis][2].rows[3].cells[1].c.color)){
            currentRoutine=WhiteCorner1();
            onRoutine=true;
            return new int[]{2,0,1};
        }
        if (Jcube.layers[Xaxis][0].rows[0].cells[2].c.color==whiteRepresentative ||
                Jcube.layers[Xaxis][0].rows[1].cells[0].c.color==whiteRepresentative ||
                Jcube.layers[Xaxis][0].rows[1].cells[2].c.color==whiteRepresentative ||
                Jcube.layers[Xaxis][0].rows[2].cells[0].c.color==whiteRepresentative ||
                Jcube.layers[Xaxis][0].rows[2].cells[2].c.color==whiteRepresentative ||
                Jcube.layers[Xaxis][0].rows[3].cells[0].c.color==whiteRepresentative ||

                Jcube.layers[Yaxis][0].rows[3].cells[2].c.color==whiteRepresentative ||
                Jcube.layers[Yaxis][0].rows[3].cells[0].c.color==whiteRepresentative ||
                Jcube.layers[Yaxis][2].rows[3].cells[2].c.color==whiteRepresentative ||
                Jcube.layers[Yaxis][2].rows[3].cells[0].c.color==whiteRepresentative ){
            return new int[]{0,0,1};
        }

        else {
            haveNextSingleInstruction=true;
            nextSingleInstruction=new int[]{0,1,1};
            return new int[]{0,2,1};
        }

    }
    public int[][] WhiteCorner0(){
        int[][] ans=new int[3][3];
        ans[0]=new int[]{1,0,-1};
        ans[1]=new int[]{0,0,-1};
        ans[2]=new int[]{1,0,1};
        return ans;
    }
    public int[][] WhiteCorner1(){
        int[][] ans=new int[3][3];
        ans[0]=new int[]{2,0,1};
        ans[1]=new int[]{0,0,1};
        ans[2]=new int[]{2,0,-1};
        return ans;
    }
    public int[][] MoveBottomWhite(){
        int[][] ans=new int[3][3];
        ans[0]=new int[]{2,0,1};
        ans[1]=new int[]{0,0,-1};
        ans[2]=new int[]{2,0,-1};
        return ans;
    }
    public int[][] SecondLayer0(){
        int[][] ans=new int[8][3];
        ans[0]=new int[]{0,2,1};
        ans[1]=new int[]{2,0,-1};
        ans[2]=new int[]{0,2,-1};
        ans[3]=new int[]{2,0,1};
        ans[4]=new int[]{0,2,-1};
        ans[5]=new int[]{1,0,1};
        ans[6]=new int[]{0,2,1};
        ans[7]=new int[]{1,0,-1};
        return ans;
    }
    public int[][] SecondLayer1(){
        int[][] ans=new int[8][3];
        ans[0]=new int[]{0,2,-1};
        ans[1]=new int[]{2,2,-1};
        ans[2]=new int[]{0,2,1};
        ans[3]=new int[]{2,2,1};
        ans[4]=new int[]{0,2,1};
        ans[5]=new int[]{1,0,-1};
        ans[6]=new int[]{0,2,-1};
        ans[7]=new int[]{1,0,1};
        return ans;
    }
    public int[][] YellowCrossRoutine(){
        int[][] ans=new int[6][3];
        ans[0]=new int[]{2,0,1};
        ans[1]=new int[]{1,0,1};
        ans[2]=new int[]{0,2,-1};
        ans[3]=new int[]{1,0,-1};
        ans[4]=new int[]{0,2,1};
        ans[5]=new int[]{2,0,-1};
        return ans;
    }
    public int[][] LittleFish1(){
        int[][] ans=new int[9][3];
        ans[0]=new int[]{2,2,1};
        ans[1]=new int[]{0,2,1};
        ans[2]=new int[]{2,2,-1};
        ans[3]=new int[]{0,2,1};
        ans[4]=new int[]{2,2,1};
        ans[5]=new int[]{0,2,1};
        ans[6]=new int[]{0,2,1};
        ans[7]=new int[]{2,2,-1};
        ans[8]=new int[]{0,2,1};
        return ans;
    }
    public int[][] LittleFish2(){
        int[][] ans=new int[9][3];
        ans[0]=new int[]{2,0,1};
        ans[1]=new int[]{0,2,-1};
        ans[2]=new int[]{2,0,-1};
        ans[3]=new int[]{0,2,-1};
        ans[4]=new int[]{2,0,1};
        ans[5]=new int[]{0,2,-1};
        ans[6]=new int[]{0,2,-1};
        ans[7]=new int[]{2,0,-1};
        ans[8]=new int[]{0,2,-1};
        return ans;
    }
    public int[] getTopYellowFace(){
        int NumofYellow=0;
        if(Jcube.layers[Yaxis][0].rows[1].cells[0].c.color==yellowRepresentative){
            NumofYellow++;
        }
        if(Jcube.layers[Yaxis][0].rows[1].cells[2].c.color==yellowRepresentative){
            NumofYellow++;
        }
        if(Jcube.layers[Yaxis][2].rows[1].cells[0].c.color==yellowRepresentative){
            NumofYellow++;
        }
        if(Jcube.layers[Yaxis][2].rows[1].cells[2].c.color==yellowRepresentative){
            NumofYellow++;
        }

        if(NumofYellow==0){
            if(Jcube.layers[Yaxis][2].rows[0].cells[2].c.color!=yellowRepresentative){
                return new int[]{0,2,1};
            }
            else {
                onRoutine=true;
                currentRoutine=LittleFish1();
                return currentRoutine[0];
            }
        }
        if (NumofYellow==2){
            if(Jcube.layers[Xaxis][2].rows[2].cells[2].c.color!=yellowRepresentative){
                return new int[]{0,2,1};
            }
            else {
                onRoutine=true;
                currentRoutine=LittleFish1();
                return currentRoutine[0];
            }
        }
        if (NumofYellow==1){
            if(Jcube.layers[Xaxis][2].rows[0].cells[2].c.color==yellowRepresentative || Jcube.layers[Xaxis][2].rows[1].cells[2].c.color==yellowRepresentative){
                if(Jcube.layers[Yaxis][2].rows[1].cells[2].c.color==yellowRepresentative){
                    onRoutine=true;
                    currentRoutine=LittleFish2();
                    return currentRoutine[0];
                }
                else {
                    return new int[]{0,2,1};
                }
            }
            else {
                if(Jcube.layers[Yaxis][2].rows[1].cells[0].c.color==yellowRepresentative){
                    onRoutine=true;
                    currentRoutine=LittleFish1();
                    return currentRoutine[0];
                }
                else {
                    return new int[]{0,2,1};
                }
            }

        }
        return new int[]{4,4,4};

    }

    public int[] getReadyForFinal(){
        if(Jcube.layers[Xaxis][1].rows[0].cells[1].c.color!=yellowRepresentative){
            onRoutine=true;
            currentRoutine=rotateCubeByZDown();
            return currentRoutine[0];
        }
        if (Jcube.layers[Yaxis][0].rows[2].ReadyforFinal() &&
                Jcube.layers[Yaxis][0].rows[0].ReadyforFinal() &&
                Jcube.layers[Yaxis][0].rows[1].ReadyforFinal() &&
                Jcube.layers[Yaxis][0].rows[3].ReadyforFinal()){
            onRoutine=true;
            currentRoutine=rotateCubeByZUp();
            return currentRoutine[0];
        }
        else {

            if (Jcube.layers[Yaxis][0].rows[2].ReadyforFinal()) {
                onRoutine = true;
                currentRoutine = R2D2();
                return currentRoutine[0];
            }
            if (!Jcube.layers[Yaxis][0].rows[2].ReadyforFinal() &&
                    !Jcube.layers[Yaxis][0].rows[0].ReadyforFinal() &&
                    !Jcube.layers[Yaxis][0].rows[1].ReadyforFinal() &&
                    !Jcube.layers[Yaxis][0].rows[3].ReadyforFinal()) {
                onRoutine = true;
                currentRoutine = R2D2();
                return currentRoutine[0];
            }
            return new int[]{1,0,1};
        }


    }
    public int[][] rotateCubeByZDown(){
        int[][] ans=new int[3][3];
        ans[0]=new int[]{2,0,1};
        ans[1]=new int[]{2,1,1};
        ans[2]=new int[]{2,2,1};
        return ans;
    }
    public int[][] rotateCubeByZUp(){
        int[][] ans=new int[3][3];
        ans[0]=new int[]{2,0,-1};
        ans[1]=new int[]{2,1,-1};
        ans[2]=new int[]{2,2,-1};
        return ans;
    }
    public int[][] R2D2(){
        int[][] ans=new int[12][3];
        ans[0]=new int[]{2,2,1};
        ans[1]=new int[]{2,2,1};
        ans[2]=new int[]{0,0,-1};
        ans[3]=new int[]{0,0,-1};

        ans[4]=new int[]{2,2,1};
        ans[5]=new int[]{0,2,1};
        ans[6]=new int[]{2,2,-1};

        ans[7]=new int[]{0,0,-1};
        ans[8]=new int[]{0,0,-1};

        ans[9]=new int[]{2,2,1};
        ans[10]=new int[]{0,2,-1};
        ans[11]=new int[]{2,2,1};
        return ans;
    }
    public int[] getFinal(){
        if(Jcube.layers[Xaxis][2].rows[0].XOX() &&
                Jcube.layers[Xaxis][2].rows[1].XOX() &&
                Jcube.layers[Xaxis][2].rows[2].XOX() &&
                Jcube.layers[Xaxis][2].rows[3].XOX() ){
            onRoutine=true;
            currentRoutine=LF12();
            return currentRoutine[0];
        }
        else {
            if(!Jcube.layers[Xaxis][2].rows[0].XOX()){
                if(Jcube.layers[Xaxis][2].rows[0].cells[1].c.color!=Jcube.layers[Xaxis][1].rows[0].cells[1].c.color){
                    haveNextSingleInstruction=true;
                    nextSingleInstruction=new int[] {0,0,1};
                    return new int[]{0,1,1};
                }
                else {
                    if(Jcube.layers[Xaxis][1].rows[2].cells[1].c.color==Jcube.layers[Xaxis][2].rows[3].cells[1].c.color){
                        onRoutine=true;
                        currentRoutine=LF21();
                        return currentRoutine[0];
                    }
                    if(Jcube.layers[Xaxis][1].rows[2].cells[1].c.color==Jcube.layers[Xaxis][2].rows[1].cells[1].c.color){
                        onRoutine=true;
                        currentRoutine=LF12();
                        return currentRoutine[0];
                    }
                    else return new int[]{4,4,4};
                }
            }
            else {
                return new int[]{0,2,1};
            }
        }

    }
    public int[][] LF12(){
        int[][] ans=new int[18][3];

        ans[0]=new int[]{2,2,1};
        ans[1]=new int[]{0,2,1};
        ans[2]=new int[]{2,2,-1};
        ans[3]=new int[]{0,2,1};
        ans[4]=new int[]{2,2,1};
        ans[5]=new int[]{0,2,1};
        ans[6]=new int[]{0,2,1};
        ans[7]=new int[]{2,2,-1};
        ans[8]=new int[]{0,2,1};

        ans[9]=new int[]{2,0,1};
        ans[10]=new int[]{0,2,-1};
        ans[11]=new int[]{2,0,-1};
        ans[12]=new int[]{0,2,-1};
        ans[13]=new int[]{2,0,1};
        ans[14]=new int[]{0,2,-1};
        ans[15]=new int[]{0,2,-1};
        ans[16]=new int[]{2,0,-1};
        ans[17]=new int[]{0,2,-1};

        return ans;
    }

    public int[][] LF21(){
        int[][] ans=new int[18][3];

        ans[0]=new int[]{2,0,1};
        ans[1]=new int[]{0,2,-1};
        ans[2]=new int[]{2,0,-1};
        ans[3]=new int[]{0,2,-1};
        ans[4]=new int[]{2,0,1};
        ans[5]=new int[]{0,2,-1};
        ans[6]=new int[]{0,2,-1};
        ans[7]=new int[]{2,0,-1};
        ans[8]=new int[]{0,2,-1};

        ans[9]=new int[]{2,2,1};
        ans[10]=new int[]{0,2,1};
        ans[11]=new int[]{2,2,-1};
        ans[12]=new int[]{0,2,1};
        ans[13]=new int[]{2,2,1};
        ans[14]=new int[]{0,2,1};
        ans[15]=new int[]{0,2,1};
        ans[16]=new int[]{2,2,-1};
        ans[17]=new int[]{0,2,1};
        return ans;
    }

    public boolean checkFinished(){

        for (int l = 0; l <3 ; l++) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j <4 ; j++) {
                    if(!Jcube.layers[l][i].rows[j].finished()){
                        return false;
                    }
                }
            }
        }

        return true;

    }
}
