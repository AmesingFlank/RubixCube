package com.example.amesingflank.rubixcube;

import android.util.Log;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Created by AmesingFlank on 15/12/28.
 */
public class Solver implements Serializable,Cloneable{

    public LinkedList<Move> getSolution(){
        LinkedList<Move> Solution=new LinkedList<Move>();
        Move step=nextMove();
        while (step.action[0]!=999){
            Log.w(String.valueOf("        "+step.action[0])+"   "+String.valueOf(step.action[1])+"   "+String.valueOf(step.action[2])+"   ",step.message);
            Solution.add(step);
            if(step.action[2]==1){
                Jcube.rotatePos(step.action[0],step.action[1]);
            }
            else {
                if(step.action[2]==-1){
                    Jcube.rotateNeg(step.action[0],step.action[1]);
                }
            }
            step=nextMove();
        }
        return Solution;
    }

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
    private Move[] currentRoutine;

    boolean haveNextSingleInstruction=false;
    Move nextSingleInstruction;

    boolean finishedWhiteCrossWithYellowCenter=false;
    boolean finished1stLayer=false;
    boolean finished2ndLayer=false;
    boolean finishedYellowCross=false;
    boolean finishedTopYellowFace=false;
    boolean readyforFinal=false;
    boolean finishedEverything;

    public void setNextSingleInstruction(Move m){
        nextSingleInstruction=m;
        haveNextSingleInstruction=true;
    }
    public void setCurrentRoutine(Move[] m){
        currentRoutine=m;
        onRoutine=true;
    }


    public Move nextMove(){
        if (finishedEverything || checkFinished()){
            return new Move(new int[]{999,999,999},"");

        }

        if(haveNextSingleInstruction){
            haveNextSingleInstruction=false;
            Move NSItemp=nextSingleInstruction.clone();
            nextSingleInstruction=new Move(new int[]{6,6,6},"");
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
                    setCurrentRoutine(ReverseWholeCubeByZ());
                    return currentRoutine[0];
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
                                setNextSingleInstruction(new Move(new int[]{0,1,1},"Assembling middle layer"));
                                return new Move(new int[]{0,0,1},"Assembling middle layer");
                            }
                        }
                        else {

                            if((Jcube.layers[Yaxis][0].rows[1].cells[1].c.color==yellowRepresentative ||Jcube.layers[Xaxis][2].rows[0].cells[1].c.color==yellowRepresentative ) &&
                                    (Jcube.layers[Yaxis][1].rows[1].cells[0].c.color==yellowRepresentative ||Jcube.layers[Xaxis][2].rows[3].cells[1].c.color==yellowRepresentative ) &&
                                    (Jcube.layers[Yaxis][1].rows[1].cells[2].c.color==yellowRepresentative ||Jcube.layers[Xaxis][2].rows[1].cells[1].c.color==yellowRepresentative ) &&
                                    (Jcube.layers[Yaxis][2].rows[1].cells[1].c.color==yellowRepresentative ||Jcube.layers[Xaxis][2].rows[2].cells[1].c.color==yellowRepresentative ) ){
                                if(Jcube.layers[Xaxis][1].rows[0].cells[0].c.color!=Jcube.layers[Xaxis][1].rows[0].cells[1].c.color ||
                                        Jcube.layers[Xaxis][1].rows[3].cells[1].c.color!=Jcube.layers[Xaxis][1].rows[3].cells[2].c.color){
                                    setCurrentRoutine(SecondLayer0());
                                    return currentRoutine[0];
                                }
                                if(Jcube.layers[Xaxis][1].rows[0].cells[2].c.color!=Jcube.layers[Xaxis][1].rows[0].cells[1].c.color &&
                                        Jcube.layers[Xaxis][1].rows[1].cells[1].c.color!=Jcube.layers[Xaxis][1].rows[1].cells[0].c.color){
                                    setCurrentRoutine(SecondLayer1());
                                    return currentRoutine[0];
                                }

                                setNextSingleInstruction(new Move(new int[]{0,1,1},"Assembling middle layer"));
                                return new Move(new int[]{0,0,1},"Assembling middle layer");

                            }
                            else {
                                return new Move(new int[]{0,2,1},"Assembling middle layer");
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
                            return new Move(new int[]{0,2,1},"Assembling yellow cross");
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
                                    return new Move(new int[]{999,999,999},"Finished");
                                }

                            }

                        }
                    }

                }
                
            }

        }

        return new Move(new int[]{4,4,4},"");


    }
    public Move MoveYellowCentral(){
        int[] action;
        if (Jcube.layers[Xaxis][1].rows[0].cells[1].c.color==yellowRepresentative){
            action= new int[]{2,1,-1};
        }
        else {
            if (Jcube.layers[Xaxis][1].rows[2].cells[1].c.color==yellowRepresentative){
                action= new int[]{2,1,1};
            }
            else {
                if(Jcube.layers[Zaxis][1].rows[0].cells[1].c.color==yellowRepresentative){
                    action= new int[]{2,1,1};
                }
                else {
                    if(Jcube.layers[Yaxis][1].rows[0].cells[1].c.color==yellowRepresentative){
                        action= new int[]{1,1,1};
                    }
                    else {
                        if(Jcube.layers[Yaxis][1].rows[2].cells[1].c.color==yellowRepresentative){
                            action= new int[]{1,1,-1};
                        }
                        else {
                            action= new int[]{4,4,4};
                        }
                    }
                }
            }
        }
        return new Move(action,"Assembling a white cross");
    }
    public Move WhiteCrossWithYellowCentral(){
        int[] action=new int[3];
        if(Jcube.layers[Yaxis][0].rows[1].cells[1].c.color==whiteRepresentative){
            action= new int[]{0,2,1};
            return new Move(action,"Assembling a white cross");
        }
        else {
            if(Jcube.layers[Xaxis][2].rows[0].cells[1].c.color==whiteRepresentative){
                action= new int[]{1,0,1};
                return new Move(action,"Assembling a white cross");
            }
        }
        if(Jcube.layers[Xaxis][1].rows[1].cells[0].c.color==whiteRepresentative){
            action= new int[]{1,0,-1};
            return new Move(action,"Assembling a white cross");
        }
        if(Jcube.layers[Xaxis][1].rows[3].cells[2].c.color==whiteRepresentative){
            action= new int[]{1,0,1};
            return new Move(action,"Assembling a white cross");
        }
        boolean X1Flag =false;
        xx:for (int i = 0; i <4 ; i++) {
            if(Jcube.layers[Xaxis][1].rows[i].cells[0].c.color==whiteRepresentative ||
                    Jcube.layers[Xaxis][1].rows[i].cells[2].c.color==whiteRepresentative){
                X1Flag=true;
            }

        }
        if(X1Flag){
            action= new int[]{0,1,1};
            return new Move(action,"Assembling a white cross");
        }

        if(Jcube.layers[Xaxis][0].rows[0].cells[1].c.color==whiteRepresentative){
            action= new int[]{1,0,1};
            return new Move(action,"Assembling a white cross");
        }

        if(Jcube.layers[Yaxis][0].rows[3].cells[1].c.color==whiteRepresentative){
            action= new int[]{1,0,1};
            return new Move(action,"Assembling a white cross");
        }
        else {
            action= new int[]{0,0,1};
            return new Move(action,"Assembling a white cross");
        }
    }

    public Move getWhiteCenter(){
        if(Jcube.layers[Yaxis][0].rows[1].cells[1].c.color!=whiteRepresentative){
            return new Move(new int[]{0,2,1},"Assembling a white cross");
        }
        if(Jcube.layers[Xaxis][1].rows[0].cells[1].c.color!=Jcube.layers[Xaxis][2].rows[0].cells[1].c.color){
            setNextSingleInstruction(new Move(new int[]{0,0,1},"Assembling a white cross"));
            return new Move(new int[]{0,1,1},"Assembling a white cross");
        }

        setNextSingleInstruction(new Move(new int[]{1,0,1},"Assembling a white cross"));
        return new Move(new int[]{1,0,1},"Assembling a white cross");
    }

    public Move[] ReverseWholeCubeByZ(){
        Move[] ans=new Move[6];
        ans[0]=new Move(new int[]{2,0,1},"Flipping the cube over");
        ans[1]=new Move(new int[]{2,0,1},"Flipping the cube over");
        ans[2]=new Move(new int[]{2,1,1},"Flipping the cube over");
        ans[3]=new Move(new int[]{2,1,1},"Flipping the cube over");
        ans[4]=new Move(new int[]{2,2,1},"Flipping the cube over");
        ans[5]=new Move(new int[]{2,2,1},"Flipping the cube over");
        return ans;
    }
    public Move get1stLayer(){
        if (Jcube.layers[Xaxis][0].rows[0].cells[0].c.color==whiteRepresentative ){
            if(Jcube.layers[Xaxis][0].rows[3].cells[2].c.color==Jcube.layers[Xaxis][2].rows[3].cells[1].c.color &&
                    Jcube.layers[Zaxis][0].rows[0].cells[0].c.color==Jcube.layers[Xaxis][2].rows[0].cells[1].c.color){
                setCurrentRoutine(WhiteCorner0());
                return currentRoutine[0];
            }
            else {
                setNextSingleInstruction(new Move(new int[]{0,1,1},"Assembling white layer"));
                return new Move(new int[]{0,2,1},"Assembling white layer");
            }
        }
        if (Jcube.layers[Xaxis][0].rows[3].cells[2].c.color==whiteRepresentative ){
            if(Jcube.layers[Zaxis][0].rows[0].cells[0].c.color==Jcube.layers[Xaxis][2].rows[3].cells[1].c.color &&
                    Jcube.layers[Xaxis][0].rows[0].cells[0].c.color==Jcube.layers[Xaxis][2].rows[0].cells[1].c.color){
                setCurrentRoutine(WhiteCorner1());
                return currentRoutine[0];
            }
            else {
                setNextSingleInstruction(new Move(new int[]{0,1,1},"Assembling white layer"));
                return new Move(new int[]{0,2,1},"Assembling white layer");
            }
        }
        if(Jcube.layers[Zaxis][0].rows[0].cells[0].c.color==whiteRepresentative  ){
            if(!(Jcube.layers[Zaxis][0].rows[2].cells[2].c.color==whiteRepresentative &&
                            Jcube.layers[Xaxis][2].rows[0].cells[0].c.color== Jcube.layers[Xaxis][2].rows[0].cells[1].c.color&&
                            Jcube.layers[Xaxis][2].rows[3].cells[2].c.color== Jcube.layers[Xaxis][2].rows[3].cells[1].c.color)){
                if(Jcube.layers[Xaxis][0].rows[0].cells[2].c.color==whiteRepresentative){
                    return new Move(new int[]{0,0,-1},"Assembling white layer");
                }
                if(Jcube.layers[Xaxis][2].rows[0].cells[0].c.color==whiteRepresentative){
                    setCurrentRoutine(MoveBottomWhite2());
                    return currentRoutine[0];
                }
                setCurrentRoutine(MoveBottomWhite());
                return currentRoutine[0];
            }
            else {
                setNextSingleInstruction(new Move(new int[]{0,1,1},"Assembling white layer"));
                return new Move(new int[]{0,2,1},"Assembling white layer");
            }
        }
        if(Jcube.layers[Xaxis][2].rows[0].cells[0].c.color==whiteRepresentative  ){
            setCurrentRoutine(WhiteCorner0());
            return currentRoutine[0];
        }
        if(Jcube.layers[Xaxis][2].rows[3].cells[2].c.color==whiteRepresentative  ){
            setCurrentRoutine(WhiteCorner1());
            return currentRoutine[0];
        }
        if(Jcube.layers[Zaxis][0].rows[2].cells[2].c.color==whiteRepresentative &&
                !(Jcube.layers[Xaxis][2].rows[0].cells[0].c.color== Jcube.layers[Xaxis][2].rows[0].cells[1].c.color&&
                Jcube.layers[Xaxis][2].rows[3].cells[2].c.color== Jcube.layers[Xaxis][2].rows[3].cells[1].c.color)){
            setCurrentRoutine(WhiteCorner1());
            return currentRoutine[0];
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
            return new Move(new int[]{0,0,1},"Assembling white layer");
        }

        else {
            setNextSingleInstruction(new Move(new int[]{0,1,1},"Assembling white layer"));
            return new Move(new int[]{0,2,1},"Assembling white layer");
        }

    }
    public Move[] WhiteCorner0(){
        Move[] ans=new Move[3];
        ans[0]=new Move(new int[]{1,0,-1},"Assembling white layer");
        ans[1]=new Move(new int[]{0,0,-1},"Assembling white layer");
        ans[2]=new Move(new int[]{1,0,1},"Assembling white layer");
        return ans;
    }
    public Move[] WhiteCorner1(){
        Move[] ans=new Move[3];
        ans[0]=new Move(new int[]{2,0,1},"Assembling white layer");
        ans[1]=new Move(new int[]{0,0,1},"Assembling white layer");
        ans[2]=new Move(new int[]{2,0,-1},"Assembling white layer");
        return ans;
    }
    public Move[] MoveBottomWhite(){
        Move[] ans=new Move[3];
        ans[0]=new Move(new int[]{2,0,1},"Assembling white layer");
        ans[1]=new Move(new int[]{0,0,-1},"Assembling white layer");
        ans[2]=new Move(new int[]{2,0,-1},"Assembling white layer");
        return ans;
    }
    public Move[] MoveBottomWhite2(){
        Move[] ans=new Move[3];
        ans[0]=new Move(new int[]{2,0,1},"Assembling white layer");
        ans[1]=new Move(new int[]{0,0,1},"Assembling white layer");
        ans[2]=new Move(new int[]{2,0,-1},"Assembling white layer");
        return ans;
    }

    public Move[] SecondLayer0(){
        Move[] ans=new Move[8];
        ans[0]=new Move(new int[]{0,2,1},"Assembling middle layer");
        ans[1]=new Move(new int[]{2,0,-1},"Assembling middle layer");
        ans[2]=new Move(new int[]{0,2,-1},"Assembling middle layer");
        ans[3]=new Move(new int[]{2,0,1},"Assembling middle layer");
        ans[4]=new Move(new int[]{0,2,-1},"Assembling middle layer");
        ans[5]=new Move(new int[]{1,0,1},"Assembling middle layer");
        ans[6]=new Move(new int[]{0,2,1},"Assembling middle layer");
        ans[7]=new Move(new int[]{1,0,-1},"Assembling middle layer");
        return ans;
    }
    public Move[] SecondLayer1(){
        Move[] ans=new Move[8];
        ans[0]=new Move(new int[]{0,2,-1},"Assembling middle layer");
        ans[1]=new Move(new int[]{2,2,-1},"Assembling middle layer");
        ans[2]=new Move(new int[]{0,2,1},"Assembling middle layer");
        ans[3]=new Move(new int[]{2,2,1},"Assembling middle layer");
        ans[4]=new Move(new int[]{0,2,1},"Assembling middle layer");
        ans[5]=new Move(new int[]{1,0,-1},"Assembling middle layer");
        ans[6]=new Move(new int[]{0,2,-1},"Assembling middle layer");
        ans[7]=new Move(new int[]{1,0,1},"Assembling middle layer");
        return ans;
    }

    public Move[] YellowCrossRoutine(){
        Move[] ans=new Move[6];
        ans[0]=new Move(new int[]{2,0,1},"Assembling yellow cross");
        ans[1]=new Move(new int[]{1,0,1},"Assembling yellow cross");
        ans[2]=new Move(new int[]{0,2,-1},"Assembling yellow cross");
        ans[3]=new Move(new int[]{1,0,-1},"Assembling yellow cross") ;
        ans[4]=new Move(new int[]{0,2,1},"Assembling yellow cross");
        ans[5]=new Move(new int[]{2,0,-1},"Assembling yellow cross");
        return ans;
    }
    public Move[] LittleFish1(){
        Move[] ans=new Move[9];
        ans[0]=new Move(new int[]{2,2,1},"Assembling top yellow surface");
        ans[1]=new Move(new int[]{0,2,1},"Assembling top yellow surface");
        ans[2]=new Move(new int[]{2,2,-1},"Assembling top yellow surface");
        ans[3]=new Move(new int[]{0,2,1},"Assembling top yellow surface");
        ans[4]=new Move(new int[]{2,2,1},"Assembling top yellow surface");
        ans[5]=new Move(new int[]{0,2,1},"Assembling top yellow surface");
        ans[6]=new Move(new int[]{0,2,1},"Assembling top yellow surface");
        ans[7]=new Move(new int[]{2,2,-1},"Assembling top yellow surface");
        ans[8]=new Move(new int[]{0,2,1},"Assembling top yellow surface");
        return ans;
    }
    public Move[] LittleFish2(){
        Move[] ans=new Move[9];
        ans[0]=new Move(new int[]{2,0,1},"Assembling top yellow surface");
        ans[1]=new Move(new int[]{0,2,-1},"Assembling top yellow surface");
        ans[2]=new Move(new int[]{2,0,-1},"Assembling top yellow surface");
        ans[3]=new Move(new int[]{0,2,-1},"Assembling top yellow surface");
        ans[4]=new Move(new int[]{2,0,1},"Assembling top yellow surface");
        ans[5]=new Move(new int[]{0,2,-1},"Assembling top yellow surface");
        ans[6]=new Move(new int[]{0,2,-1},"Assembling top yellow surface");
        ans[7]=new Move(new int[]{2,0,-1},"Assembling top yellow surface");
        ans[8]=new Move(new int[]{0,2,-1},"Assembling top yellow surface");
        return ans;
    }

    public Move getTopYellowFace(){
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
                return new Move(new int[]{0,2,1},"Assembling top yellow surface");
            }
            else {
                setCurrentRoutine(LittleFish1());
                return currentRoutine[0];
            }
        }
        if (NumofYellow==2){
            if(Jcube.layers[Xaxis][2].rows[2].cells[2].c.color!=yellowRepresentative){
                return new Move(new int[]{0,2,1},"Assembling top yellow surface");
            }
            else {
                setCurrentRoutine(LittleFish1());
                return currentRoutine[0];
            }
        }
        if (NumofYellow==1){
            if(Jcube.layers[Xaxis][2].rows[0].cells[2].c.color==yellowRepresentative || Jcube.layers[Xaxis][2].rows[1].cells[2].c.color==yellowRepresentative){
                if(Jcube.layers[Yaxis][2].rows[1].cells[2].c.color==yellowRepresentative){
                    setCurrentRoutine(LittleFish2());
                    return currentRoutine[0];
                }
                else {
                    return new Move(new int[]{0,2,1},"Assembling top yellow surface");
                }
            }
            else {
                if(Jcube.layers[Yaxis][2].rows[1].cells[0].c.color==yellowRepresentative){
                    setCurrentRoutine(LittleFish1());
                    return currentRoutine[0];
                }
                else {
                    return new Move(new int[]{0,2,1},"Assembling top yellow surface");
                }
            }

        }
        return new Move(new int[]{4,4,4},"Assembling top yellow surface");

    }

    public Move getReadyForFinal(){
        if(Jcube.layers[Xaxis][1].rows[0].cells[1].c.color!=yellowRepresentative){
            setCurrentRoutine(rotateCubeByZDown());
            return currentRoutine[0];
        }
        if (Jcube.layers[Yaxis][0].rows[2].ReadyforFinal() &&
                Jcube.layers[Yaxis][0].rows[0].ReadyforFinal() &&
                Jcube.layers[Yaxis][0].rows[1].ReadyforFinal() &&
                Jcube.layers[Yaxis][0].rows[3].ReadyforFinal()){
            setCurrentRoutine(rotateCubeByZUp());
            return currentRoutine[0];
        }
        else {

            if (Jcube.layers[Yaxis][0].rows[2].ReadyforFinal()) {
                setCurrentRoutine(R2D2());
                return currentRoutine[0];
            }
            if (!Jcube.layers[Yaxis][0].rows[2].ReadyforFinal() &&
                    !Jcube.layers[Yaxis][0].rows[0].ReadyforFinal() &&
                    !Jcube.layers[Yaxis][0].rows[1].ReadyforFinal() &&
                    !Jcube.layers[Yaxis][0].rows[3].ReadyforFinal()) {
                setCurrentRoutine(R2D2());
                return currentRoutine[0];
            }
            return new Move(new int[]{1,0,1},"Assembling the top layer");
        }


    }
    public Move[] rotateCubeByZDown(){
        Move[] ans=new Move[3];
        ans[0]=new Move(new int[]{2,0,1},"Assembling the top layer");
        ans[1]=new Move(new int[]{2,1,1},"Assembling the top layer");
        ans[2]=new Move(new int[]{2,2,1},"Assembling the top layer");
        return ans;
    }
    public Move[] rotateCubeByZUp(){
        Move[] ans=new Move[3];
        ans[0]=new Move(new int[]{2,0,-1},"Assembling the top layer");
        ans[1]=new Move(new int[]{2,1,-1},"Assembling the top layer");
        ans[2]=new Move(new int[]{2,2,-1},"Assembling the top layer");
        return ans;
    }
    public Move[] R2D2(){
        Move[] ans=new Move[12];
        ans[0]=new Move(new int[]{2,2,1},"Assembling the top layer");
        ans[1]=new Move(new int[]{2,2,1},"Assembling the top layer");
        ans[2]=new Move(new int[]{0,0,-1},"Assembling the top layer");
        ans[3]=new Move(new int[]{0,0,-1},"Assembling the top layer");

        ans[4]=new Move(new int[]{2,2,1},"Assembling the top layer");
        ans[5]=new Move(new int[]{0,2,1},"Assembling the top layer");
        ans[6]=new Move(new int[]{2,2,-1},"Assembling the top layer");

        ans[7]=new Move(new int[]{0,0,-1},"Assembling the top layer");
        ans[8]=new Move(new int[]{0,0,-1},"Assembling the top layer");

        ans[9]=new Move(new int[]{2,2,1},"Assembling the top layer");
        ans[10]=new Move(new int[]{0,2,-1},"Assembling the top layer");
        ans[11]=new Move(new int[]{2,2,1},"Assembling the top layer");
        return ans;
    }
    public Move getFinal(){
        if(Jcube.layers[Xaxis][2].rows[0].XOX() &&
                Jcube.layers[Xaxis][2].rows[1].XOX() &&
                Jcube.layers[Xaxis][2].rows[2].XOX() &&
                Jcube.layers[Xaxis][2].rows[3].XOX() ){
            setCurrentRoutine(LF12());
            return currentRoutine[0];
        }
        else {
            if(!Jcube.layers[Xaxis][2].rows[0].XOX()){
                if(Jcube.layers[Xaxis][2].rows[0].cells[1].c.color!=Jcube.layers[Xaxis][1].rows[0].cells[1].c.color){

                    setNextSingleInstruction(new Move(new int[]{0,0,1},"Assembling the top layer"));
                    return new Move(new int[]{0,1,1},"Assembling the top layer");
                }
                else {
                    if(Jcube.layers[Xaxis][1].rows[2].cells[1].c.color==Jcube.layers[Xaxis][2].rows[3].cells[1].c.color){
                        setCurrentRoutine(LF12());
                        return currentRoutine[0];
                    }
                    if(Jcube.layers[Xaxis][1].rows[2].cells[1].c.color==Jcube.layers[Xaxis][2].rows[1].cells[1].c.color){
                        setCurrentRoutine(LF12());
                        return currentRoutine[0];
                    }
                    else return new Move(new int[]{4,4,4},"Assembling the top layer");
                }
            }
            else {
                return new Move(new int[]{0,2,1},"Assembling the top layer");
            }
        }

    }
    public Move[] LF12(){
        Move[] ans=new Move[18];

        ans[0]=new Move(new int[]{2,2,1},"Assembling the top layer");
        ans[1]=new Move(new int[]{0,2,1},"Assembling the top layer");
        ans[2]=new Move(new int[]{2,2,-1},"Assembling the top layer");
        ans[3]=new Move(new int[]{0,2,1},"Assembling the top layer");
        ans[4]=new Move(new int[]{2,2,1},"Assembling the top layer");
        ans[5]=new Move(new int[]{0,2,1},"Assembling the top layer");
        ans[6]=new Move(new int[]{0,2,1},"Assembling the top layer");
        ans[7]=new Move(new int[]{2,2,-1},"Assembling the top layer");
        ans[8]=new Move(new int[]{0,2,1},"Assembling the top layer");

        ans[9]=new Move(new int[]{2,0,1},"Assembling the top layer");
        ans[10]=new Move(new int[]{0,2,-1},"Assembling the top layer");
        ans[11]=new Move(new int[]{2,0,-1},"Assembling the top layer");
        ans[12]=new Move(new int[]{0,2,-1},"Assembling the top layer");
        ans[13]=new Move(new int[]{2,0,1},"Assembling the top layer");
        ans[14]=new Move(new int[]{0,2,-1},"Assembling the top layer");
        ans[15]=new Move(new int[]{0,2,-1},"Assembling the top layer");
        ans[16]=new Move(new int[]{2,0,-1},"Assembling the top layer");
        ans[17]=new Move(new int[]{0,2,-1},"Finished");

        return ans;
    }

    public Move[] LF21(){
        Move[] ans=new Move[18];


        ans[0]=new Move(new int[]{2,0,1},"Assembling the top layer");
        ans[1]=new Move(new int[]{0,2,-1},"Assembling the top layer");
        ans[2]=new Move(new int[]{2,0,-1},"Assembling the top layer");
        ans[3]=new Move(new int[]{0,2,-1},"Assembling the top layer");
        ans[4]=new Move(new int[]{2,0,1},"Assembling the top layer");
        ans[5]=new Move(new int[]{0,2,-1},"Assembling the top layer");
        ans[6]=new Move(new int[]{0,2,-1},"Assembling the top layer");
        ans[7]=new Move(new int[]{2,0,-1},"Assembling the top layer");
        ans[8]=new Move(new int[]{0,2,-1},"Assembling the top layer");

        ans[9]=new Move(new int[]{2,2,1},"Assembling the top layer");
        ans[10]=new Move(new int[]{0,2,1},"Assembling the top layer");
        ans[11]=new Move(new int[]{2,2,-1},"Assembling the top layer");
        ans[12]=new Move(new int[]{0,2,1},"Assembling the top layer");
        ans[13]=new Move(new int[]{2,2,1},"Assembling the top layer");
        ans[14]=new Move(new int[]{0,2,1},"Assembling the top layer");
        ans[15]=new Move(new int[]{0,2,1},"Assembling the top layer");
        ans[16]=new Move(new int[]{2,2,-1},"Assembling the top layer");
        ans[17]=new Move(new int[]{0,2,1},"Finished");

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

    public Solver clone(){
        Solver clone=null;
        try {
            clone=(Solver)super.clone();
        }
        catch (CloneNotSupportedException e){
            e.printStackTrace();
        }

        return clone;
    }
}
