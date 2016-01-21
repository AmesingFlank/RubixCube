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
    Cube Jcube;
    public IntelliSolver(Cube Jin){
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
        HashSet<Node> Close = new HashSet<Node>();
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

    public static int heuristic(Cube cin){
        return heuristic1(cin);
    }

    public static int heuristic1(Cube cin){
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

        int[][] edges=new int[3][4];
        for (int a = 0; a < 3; a++) {
            int[][] s=new int[4][3];
            for (int i = 0; i <4 ; i++) {
                s[i]=getLocation(a,1,i,0);
            }
            int[][] d=new int[4][3];
            d[0]=getDestLocation(new int[]{cin.layers[a][1].rows[0].cells[0].c.color,cin.layers[a][1].rows[3].cells[2].c.color},colors);
            for (int i = 1; i <4 ; i++) {
                d[i]=getDestLocation(new int[]{cin.layers[a][1].rows[i].cells[0].c.color,cin.layers[a][1].rows[i-1].cells[2].c.color},colors);
            }

            sum+=getPhysicalDistance(s[0],d[0]);
            if(getPhysicalDistance(s[0],s[1])!=getPhysicalDistance(d[0],d[1])){
                sum+=getPhysicalDistance(s[1],d[1]);
                if(getPhysicalDistance(s[2],s[1])!=getPhysicalDistance(d[2],d[1])){
                        sum+=getPhysicalDistance(s[2],d[2]);
                    if(getPhysicalDistance(s[2],s[3])!=getPhysicalDistance(d[2],d[3])){
                        sum+=getPhysicalDistance(s[3],d[3]);
                    }
                }
                else {
                    if(getPhysicalDistance(s[3],s[1])!=getPhysicalDistance(d[3],d[1])){
                        sum+=getPhysicalDistance(s[3],d[3]);
                    }
                }
            }
            else {
                if(getPhysicalDistance(s[2],s[0])!=getPhysicalDistance(d[2],d[0])){
                    sum+=getPhysicalDistance(s[2],d[2]);
                    if(getPhysicalDistance(s[2],s[3])!=getPhysicalDistance(d[2],d[3])){
                        sum+=getPhysicalDistance(s[3],d[3]);
                    }
                }
                else {
                    if(getPhysicalDistance(s[3],s[0])!=getPhysicalDistance(d[3],d[0])){
                        sum+=getPhysicalDistance(s[3],d[3]);
                    }
                }
            }

           /* edges[a][0]=getPhysicalDistance(getLocation(a,1,0,0),
                    getDestLocation(new int[]{cin.layers[a][1].rows[0].cells[0].c.color,cin.layers[a][1].rows[3].cells[2].c.color},colors));
            edges[a][1]=getPhysicalDistance(getLocation(a,1,1,0),
                    getDestLocation(new int[]{cin.layers[a][1].rows[1].cells[0].c.color,cin.layers[a][1].rows[0].cells[2].c.color},colors));
            edges[a][2]=getPhysicalDistance(getLocation(a,1,2,0),
                    getDestLocation(new int[]{cin.layers[a][1].rows[2].cells[0].c.color,cin.layers[a][1].rows[1].cells[2].c.color},colors));
            edges[a][3]=getPhysicalDistance(getLocation(a,1,3,0),
                    getDestLocation(new int[]{cin.layers[a][1].rows[3].cells[0].c.color,cin.layers[a][1].rows[2].cells[2].c.color},colors));
                    */
        }

        int[] cornors=new int[8];

        cornors[0]=getPhysicalDistance(getLocation(Xaxis,0,0,0),
                getDestLocation(new int[]{cin.layers[Xaxis][0].rows[0].cells[0].c.color,cin.layers[Xaxis][0].rows[3].cells[2].c.color,cin.layers[Zaxis][0].rows[0].cells[0].c.color},colors));
        cornors[1]=getPhysicalDistance(getLocation(Xaxis,0,1,0),
                getDestLocation(new int[]{cin.layers[Xaxis][0].rows[1].cells[0].c.color,cin.layers[Xaxis][0].rows[0].cells[2].c.color,cin.layers[Zaxis][2].rows[0].cells[0].c.color},colors));
        cornors[2]=getPhysicalDistance(getLocation(Xaxis,0,2,0),
                getDestLocation(new int[]{cin.layers[Xaxis][0].rows[2].cells[0].c.color,cin.layers[Xaxis][0].rows[1].cells[2].c.color,cin.layers[Zaxis][2].rows[0].cells[2].c.color},colors));
        cornors[3]=getPhysicalDistance(getLocation(Xaxis,0,3,0),
                getDestLocation(new int[]{cin.layers[Xaxis][0].rows[3].cells[0].c.color,cin.layers[Xaxis][0].rows[2].cells[2].c.color,cin.layers[Zaxis][0].rows[0].cells[2].c.color},colors));

        cornors[4]=getPhysicalDistance(getLocation(Xaxis,2,0,0),
                getDestLocation(new int[]{cin.layers[Xaxis][2].rows[0].cells[0].c.color,cin.layers[Xaxis][2].rows[3].cells[2].c.color,cin.layers[Zaxis][0].rows[2].cells[2].c.color},colors));
        cornors[5]=getPhysicalDistance(getLocation(Xaxis,2,1,0),
                getDestLocation(new int[]{cin.layers[Xaxis][2].rows[1].cells[0].c.color,cin.layers[Xaxis][2].rows[0].cells[2].c.color,cin.layers[Zaxis][2].rows[2].cells[2].c.color},colors));
        cornors[6]=getPhysicalDistance(getLocation(Xaxis,2,2,0),
                getDestLocation(new int[]{cin.layers[Xaxis][2].rows[2].cells[0].c.color,cin.layers[Xaxis][2].rows[1].cells[2].c.color,cin.layers[Zaxis][2].rows[2].cells[0].c.color},colors));
        cornors[7]=getPhysicalDistance(getLocation(Xaxis,2,3,0),
                getDestLocation(new int[]{cin.layers[Xaxis][2].rows[3].cells[0].c.color,cin.layers[Xaxis][2].rows[2].cells[2].c.color,cin.layers[Zaxis][0].rows[2].cells[0].c.color},colors));


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(edges[i][j]==edges[i][j+1]){
                    edges[i][j]=0;
                }
            }
        }
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <4 ; j++) {
                sum+=edges[i][j];
            }
        }
        for (int i = 0; i < 8; i++) {
            sum+=cornors[i];
        }
        return sum;
    }

    public static int[] getDestLocation(int[] c,int[] colors){
        int length =c.length;
        int[] faces =new int[length];
        for (int i = 0; i < length; i++) {
            faces[i]=colors[c[i]];
        }
        return getLocationbyFace(faces);
    }

    public static int[] getLocationbyFace(int[] faces){
        int x,y,z;
        x=1;
        y=1;
        z=1;
        int length=faces.length;
        for (int i = 0; i <length; i++) {
            switch (faces[i]){
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

    public static int[] getLocation(int a,int l, int r, int c){
        int x,y,z;
        x=1;
        y=1;
        z=1;
        switch (a){
            case Xaxis:
                y=l;
                switch (r){
                    case 0:
                        z=0;
                        x=c;
                        break;
                    case 1:
                        x=2;
                        z=c;
                        break;
                    case 2:
                        z=2;
                        x=2-c;
                        break;
                    case 3:
                        x=0;
                        z=2-c;
                        break;
                }
                break;
            case Yaxis:
                z=l;
                switch (r){
                    case 0:
                        x=0;
                        y=c;
                        break;
                    case 1:
                        y=2;
                        x=c;
                        break;
                    case 2:
                        x=2;
                        y=2-c;
                        break;
                    case 3:
                        y=0;
                        x=2-c;
                        break;
                }
                break;
            case Zaxis:
                x=l;
                switch (r){
                    case 0:
                        y=0;
                        z=c;
                        break;
                    case 1:
                        z=2;
                        y=c;
                        break;
                    case 2:
                        y=2;
                        z=2-c;
                        break;
                    case 3:
                        z=0;
                        y=2-c;
                        break;
                }
                break;
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
