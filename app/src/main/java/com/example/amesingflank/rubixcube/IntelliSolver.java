package com.example.amesingflank.rubixcube;

import android.os.SystemClock;
import android.util.Log;
import android.widget.Switch;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by AmesingFlank on 16/1/16.
 */
public class IntelliSolver implements Serializable ,Cloneable{
    public static final int Xaxis = 0;
    public static final int Yaxis = 1;
    public static final int Zaxis = 2;

    final static int frontFace=0;
    final static int backFace=1;
    final static int leftFace=2;
    final static int rightFace=3;
    final static int downFace=4;
    final static int upFace=5;

    public int yellowRepresentative=color.yellow;
    public int whiteRepresentative=color.white;
    JavaCube Jcube;
    public IntelliSolver(JavaCube Jin){
        Jcube=Jin;
        //setColor();
    }


    public Move parentMove=new Move(new int[]{5,5,5},"99999");

    public HashSet<Cube> pastNodes=new HashSet<Cube>();

    public LinkedList<Move> getSolution2(){
        LinkedList<Move> solution=new LinkedList<>();

        Node start=new Node(Jcube.clone(),null,null);
        int minH=heuristic(start.Jcube);

        LinkedList<Node> Open = new LinkedList<Node>();
        HashSet<Node> Close = new HashSet<>();
        HashSet<Node> Open2 = new HashSet<Node>();

        Open.addFirst(start);
        Open2.add(start);

        Node curr=getBestNode(Open);


        while (!curr.checkFinished()){
            curr=getBestNode(Open);
            String os=" Open: "+String.valueOf(Open.size())+" ";
            String cs=" Close: "+String.valueOf(Close.size())+" ";

            Log.w("heuristic:  "+String.valueOf(curr.h),os+cs);
            if(curr.checkFinished()){
                break;
            }

            curr.Conduct(Open,Close,Open2);

        }

        while (curr.parent!=null){
            solution.addFirst(curr.pathway);
            curr=curr.parent;
        }
        return solution;

    }

    public static Node getBestNode(LinkedList<Node> Open){
        int size=Open.size();
        Node best=null;
        int bestH=9999999;
        long a= SystemClock.currentThreadTimeMillis();
        for (int i = 0; i <size ; i++) {
            if(Open.get(i).h<bestH){
                best=Open.get(i);
                bestH=best.h;
            }
        }
        long b=SystemClock.currentThreadTimeMillis();

        Log.w("Seleting Node took :  ",String.valueOf(b-a));

        return best;
    }

    public Move nextMove(){
        int currentHeuristic=heuristic(Jcube);
        if(currentHeuristic==0){
            return new Move(new int[]{999,999,999},"Finished");
        }
        int minheuristic=9999999;
        Move selectedMove=new Move(new int[]{5,5,5},"Magic");

        Move thisMove;
        for (int a = 0; a < 3; a++) {
            for (int l = 0; l < 3; l++) {
                Jcube.rotatePos(a,l);
                int h=heuristic(Jcube);
                if(h<minheuristic && (a!=parentMove.action[0] || l!=parentMove.action[1] || parentMove.action[2]!=-1) && !pastNodes.contains(Jcube)){
                    selectedMove=new Move(new int[]{a,l,1},String.valueOf(h));
                    minheuristic=h;
                }
                Jcube.rotateNeg(a,l);
            }
        }
        for (int a = 0; a < 3; a++) {
            for (int l = 0; l < 3; l++) {
                Jcube.rotateNeg(a, l);
                int h=heuristic(Jcube);
                if(h<minheuristic && (a!=parentMove.action[0] || l!=parentMove.action[1] || parentMove.action[2]!=1) && !pastNodes.contains(Jcube)){
                    selectedMove=new Move(new int[]{a,l,-1},String.valueOf(h));
                    minheuristic=h;
                }
                Jcube.rotatePos(a, l);
            }
        }

        if(selectedMove.action[2]==1){
            Jcube.rotatePos(selectedMove.action[0],selectedMove.action[1]);
        }
        else {
            if(selectedMove.action[2]==-1){
                Jcube.rotateNeg(selectedMove.action[0],selectedMove.action[1]);
            }
        }

        Cube temp=null;
        try {
            temp=(Cube)ObjectCloner.deepCopy(Jcube);
        } catch (Exception e) {
            e.printStackTrace();
        }
        pastNodes.add(temp);
        parentMove=selectedMove;
        return selectedMove;
    }

    public LinkedList<Move> getSolution(){
        LinkedList<Move> Solution=new LinkedList<Move>();
        Move step=nextMove();
        while (step.action[0]!=999){
            if(Solution.size()<1000){
                Log.w("current step: "+String.valueOf(Solution.size())+"  ", "Heuristic Value:  "+step.message);
            }
            Solution.add(step);

            step=nextMove();
        }
        return Solution;
    }

    public static int heuristic(JavaCube cin){
        return heuristic1(cin);
    }

    public static int heuristic1(JavaCube cin){
        int[] faces=new int[6];
        faces[frontFace]=cin.singleCubes[1][1][0].ColorArray[0];
        faces[backFace]=cin.singleCubes[1][1][2].ColorArray[1];
        faces[leftFace]=cin.singleCubes[0][1][1].ColorArray[2];
        faces[rightFace]=cin.singleCubes[2][1][1].ColorArray[3];
        faces[downFace]=cin.singleCubes[1][0][1].ColorArray[4];
        faces[upFace]=cin.singleCubes[1][2][1].ColorArray[5];
        int[] colors=new int[6 ];
        for (int c = 0; c < 6; c++) {
            for (int f = 0; f < 6; f++) {
                if(faces[f]==c){
                    colors[c]=f;
                }
            }
        }

        int sum=0;

        for (int x = 0; x <3 ; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    sum+=getPhysicalDistance(new int[]{x,y,z},getDestLocation(cin.singleCubes[x][y][z],colors));
                }
            }
        }
        return sum;
    }

    public static int[] getDestLocation(JavaSingleCube c,int[] colors){

        LinkedList<Integer> faces =new LinkedList<>();
        for (int i = 0; i < 6; i++) {
            if(c.ColorArray[i]!=JavaSingleCube.blank){
                faces.add(c.ColorArray[i]);
            }
        }

        return getLocationbyFace(faces);
    }

    public static int[] getLocationbyFace(LinkedList<Integer> faces){
        int x,y,z;
        x=1;
        y=1;
        z=1;
        int length=faces.size();
        for (int i = 0; i <length; i++) {
            switch (faces.get(i)){
                case 0:
                    z=0;
                    break;
                case 1:
                    z=2;
                    break;
                case 2:
                    x=0;
                    break;
                case 3:
                    x=2;
                    break;
                case 4:
                    y=0;
                    break;
                case 5:
                    y=2;
                    break;
            }

        }
        return new int[]{x,y,z};
    }


    public static int getPhysicalDistance(int[] loc0,int[] loc1){
        int sum=0;
        for (int i = 0; i < 3; i++) {
            sum+=Math.abs(loc0[i]-loc1[i]);
        }
        return sum;

    }


    public static int heuristic0(Cube cin){
        int[] faces=new int[6];
        faces[frontFace]=cin.layers[Xaxis][1].rows[0].cells[1].c.color;
        faces[backFace]=cin.layers[Xaxis][1].rows[2].cells[1].c.color;
        faces[leftFace]=cin.layers[Yaxis][1].rows[0].cells[1].c.color;
        faces[rightFace]=cin.layers[Yaxis][1].rows[2].cells[1].c.color;
        faces[downFace]=cin.layers[Zaxis][1].rows[0].cells[1].c.color;
        faces[upFace]=cin.layers[Zaxis][1].rows[2].cells[1].c.color;
        int[] colors=new int[6 ];
        for (int c = 0; c < 6; c++) {
            for (int f = 0; f < 6; f++) {
                if(faces[f]==c){
                    colors[c]=f;
                }
            }
        }

        int sum=0;
        for (int l = 0; l < 3; l++) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c <3 ; c++) {
                    int destFace=colors[cin.layers[Xaxis][l].rows[r].cells[c].c.color];
                    int face=getFace(Xaxis,r);
                    int distance=getDistance(destFace,face);
                    sum+=distance;
                }
            }
        }
        for (int l = 0; l < 3; l++) {
            for (int c = 0; c <3 ; c++) {
                int destFace=colors[cin.layers[Zaxis][l].rows[0].cells[c].c.color];
                int face=getFace(Zaxis,0);
                int distance=getDistance(destFace,face);
                sum+=distance;
            }
            for (int c = 0; c <3 ; c++) {
                int destFace=colors[cin.layers[Zaxis][l].rows[2].cells[c].c.color];
                int face=getFace(Zaxis,2);
                int distance=getDistance(destFace,face);
                sum+=distance;
            }
        }

        return sum;
    }
    public static int getDistance(int a,int b){
        int c=Math.max(a,b);
        int d=Math.min(a,b);
        if(c%2==0 && d==c+1){
            return 2;
        }
        else {
            if (d==c){
                return 0;
            }
            else {
                return 1;
            }
        }
    }
    public static int getFace(int axis,int row){
        switch (axis){
            case Xaxis:
                switch (row){
                    case 0:
                        return frontFace;
                    case 1:
                        return rightFace;
                    case 2:
                        return backFace;
                    case 3:
                        return leftFace;
                }
                break;
            case Yaxis:
                switch (row){
                    case 0:
                        return leftFace;
                    case 1:
                        return upFace;
                    case 2:
                        return rightFace;
                    case 3:
                        return downFace;
                }
                break;
            case Zaxis:
                switch (row){
                    case 0:
                        return downFace;
                    case 1:
                        return backFace;
                    case 2:
                        return upFace;
                    case 3:
                        return frontFace;
                }
                break;
        }
        return 0;
    }
}
