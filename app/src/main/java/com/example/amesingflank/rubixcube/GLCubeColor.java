package com.example.amesingflank.rubixcube;

/**
 * Created by lbj on 2015/11/24.
 */
public class GLCubeColor {
    static float[] white={1f,1f,1f,1f};
    static float[] yellow={1f,1f,0f,1f};
    static float[] green={0f,1f,0f,1f};
    static float[] blue={0f,0f,1f,1f};
    static float[] orange={1f,0.5f,0f,1f};
    static float[] red={1f,0f,0f,1f};
    static float[] blank={0.5f,0.5f,0.5f,1f};
    static float[][] ColorArray={white,yellow,green,blue,orange,red};

    //front      back     left        right       down        up
    //orange     red      blue        green       white       yellow

    static float[][][][][] Cube27data(){
        // The Color of the the 27 Cubes,eacb definec in the order of front-back-keft-right-down-up
        float[][][][][]ans=new float[3][3][3][6][4];
        ans[0][0][0]=new float[][]{orange,blank,blue,blank,yellow,blank};
        ans[0][0][1]=new float[][]{blank,blank,blue,blank,yellow,blank};
        ans[0][0][2]=new float[][]{blank,red,blue,blank,yellow,blank};

        ans[0][1][0]=new float[][]{orange,blank,blue,blank,blank,blank};
        ans[0][1][1]=new float[][]{blank,blank,blue,blank,blank,blank};
        ans[0][1][2]=new float[][]{blank,red,blue,blank,blank,blank};

        ans[0][2][0]=new float[][]{orange,blank,blue,blank,blank,white};
        ans[0][2][1]=new float[][]{blank,blank,blue,blank,blank,white};
        ans[0][2][2]=new float[][]{blank,red,blue,blank,blank,white};




        ans[1][0][0]=new float[][]{orange,blank,blank,blank,yellow,blank};
        ans[1][0][1]=new float[][]{blank,blank,blank,blank,yellow,blank};
        ans[1][0][2]=new float[][]{blank,red,blank,blank,yellow,blank};

        ans[1][1][0]=new float[][]{orange,blank,blank,blank,blank,blank};
        ans[1][1][1]=new float[][]{blank,blank,blank,blank,blank,blank};
        ans[1][1][2]=new float[][]{blank,red,blank,blank,blank,blank};

        ans[1][2][0]=new float[][]{orange,blank,blank,blank,blank,white};
        ans[1][2][1]=new float[][]{blank,blank,blank,blank,blank,white};
        ans[1][2][2]=new float[][]{blank,red,blank,blank,blank,white};




        ans[2][0][0]=new float[][]{orange,blank,blank,green,yellow,blank};
        ans[2][0][1]=new float[][]{blank,blank,blank,green,yellow,blank};
        ans[2][0][2]=new float[][]{blank,red,blank,green,yellow,blank};

        ans[2][1][0]=new float[][]{orange,blank,blank,green,blank,blank};
        ans[2][1][1]=new float[][]{blank,blank,blank,green,blank,blank};
        ans[2][1][2]=new float[][]{blank,red,blank,green,blank,blank};

        ans[2][2][0]=new float[][]{orange,blank,blank,green,blank,white};
        ans[2][2][1]=new float[][]{blank,blank,blank,green,blank,white};
        ans[2][2][2]=new float[][]{blank,red,blank,green,blank,white};
        return ans;
    }

}

