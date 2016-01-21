package com.example.amesingflank.rubixcube;

import java.io.Serializable;

/**
 * Created by AmesingFlank on 16/1/14.
 */
public class Move implements Serializable{
    int[] action=new int[]{6,6,6};
    String message="";
    public Move(int[] i,String m){
        action=i.clone();
        message=m;
    }
    public Move clone(){
        return new Move(action.clone(),message);
    }

}
