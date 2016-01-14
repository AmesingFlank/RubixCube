package com.example.amesingflank.rubixcube;

import java.util.LinkedList;

/**
 * Created by AmesingFlank on 16/1/5.
 */
public class OneTimeSolver extends Solver {
    public OneTimeSolver(Cube Jcube) {
        super(Jcube);
    }
    public LinkedList<int[]> getSolution(){
        LinkedList<int[]> Solution=new LinkedList<int[]>();
        int[] step=nextMove();
        while (step[0]!=999){
            Solution.add(step);
            if(step[2]==1){
                Jcube.rotatePos(step[0],step[1]);
            }
            else {
                if(step[2]==-1){
                    Jcube.rotateNeg(step[0],step[1]);
                }
            }
            step=nextMove();
        }
        return Solution;
    }
}
