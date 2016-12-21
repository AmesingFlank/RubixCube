package com.example.amesingflank.rubixcube;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by AmesingFlank on 16/1/17.
 */
public class Node {
    JavaCube Jcube;
    int h;

    Node parent;
    Move pathway;
    public Node(JavaCube cin, Node pin, Move min){
        Jcube=cin;
        parent=pin;
        pathway=min;
        h=IntelliSolver.heuristic(Jcube);
    }

    public int hashCode(){
        return Jcube.hashCode();
    }
    public boolean equals(Object o){
        return Jcube.equals(o);
    }
    public boolean checkFinished(){
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                if(Jcube.singleCubes[0][0][0].ColorArray[0]!=Jcube.singleCubes[i][j][0].ColorArray[0]){
                    return false;
                }
                if(Jcube.singleCubes[2][2][2].ColorArray[1]!=Jcube.singleCubes[i][j][2].ColorArray[1]){
                    return false;
                }
                if(Jcube.singleCubes[0][0][0].ColorArray[2]!=Jcube.singleCubes[0][i][j].ColorArray[2]){
                    return false;
                }
                if(Jcube.singleCubes[2][2][2].ColorArray[3]!=Jcube.singleCubes[2][i][j].ColorArray[3]){
                    return false;
                }
                if(Jcube.singleCubes[0][0][0].ColorArray[4]!=Jcube.singleCubes[i][0][j].ColorArray[4]){
                    return false;
                }
                if(Jcube.singleCubes[2][2][2].ColorArray[5]!=Jcube.singleCubes[i][2][j].ColorArray[5]){
                    return false;
                }
            }
        }
        return true;

    }
    public LinkedList<Node> getChildren(){
        LinkedList<Node> children=new LinkedList<>();

        JavaCube tempCube=Jcube.clone();

        for (int a = 0; a < 3; a++) {
            for (int l = 0; l < 3; l++) {
                tempCube.rotatePos(a,l);
                children.add(new Node(tempCube.clone(),this,new Move(new int[]{a,l,1},"Magic")));
                tempCube.rotateNeg(a, l);
            }
        }
        for (int a = 0; a < 3; a++) {
            for (int l = 0; l < 3; l++) {
                tempCube.rotateNeg(a, l);
                children.add(new Node(tempCube.clone(), this, new Move(new int[]{a, l, -1}, "Magic")));
                tempCube.rotatePos(a, l);
            }
        }
        return children;
    }

    public void Conduct(LinkedList<Node> Open,HashSet<Node> Close,HashSet<Node> Open2){
        LinkedList<Node> children=getChildren();
        int size=children.size();
        Open.remove(this);
        Open2.remove(this);
        Close.add(this);
        for (int i = 0; i < size; i++) {
            Node curr=children.get(i);
            if(!Open2.contains(curr) && !Close.contains(curr)){
                Open.addLast(curr);
                Open2.add(curr);
            }
        }

    }
}
