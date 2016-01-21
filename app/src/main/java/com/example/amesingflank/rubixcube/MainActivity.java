package com.example.amesingflank.rubixcube;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.logging.Handler;

import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    float RSC=0.15f;

    glsv g;
    LinearLayout LL;
    LinearLayout.LayoutParams wrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams match = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
    public SeekBar seekBar;



    DisplayMetrics dm;
    public int screenx=1440;
    public int screeny=2560;
    StepsShower ss;
    boolean inSettings=false;
    boolean inAbout=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        LL=(LinearLayout)findViewById(R.id.LL);
        wrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        match = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenx=dm.widthPixels;
        screeny=dm.heightPixels;
        RSC=0.15f;
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        restart();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.removeItem(0);
        menu.add(0, 2, 0, "Restart");
        menu.add(0, 3, 0, "Disarrange");
        menu.add(0, 4, 0, "Next Step");
        menu.add(0, 5, 0, "Previous Step");
        menu.add(0, 6, 0, "Solve");
        menu.add(0, 7, 0, "Pause");
        menu.add(0, 8, 0, "Continue");
        menu.add(0, 9, 0, "About");
        menu.add(0,10,0,"Intelli Solve");



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case 2:
                restart();
                break;
            case 3:
                RandomeRotate();
                break;
            case 4:
                NextStep();
                break;
            case 5:
                PreviousStep();
                break;
            case 6:
                solve();
                break;
            case 7:
                Pause();
                break;
            case 8:
                Continue();
                break;
            case 9:
                enterAbout();
                break;
            case 10:
                intelliSolve();
                break;

        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            enterSettings();
        }

        return super.onOptionsItemSelected(item);
    }


    public void showFinished(){
        Toast.makeText(this, "Finished!", Toast.LENGTH_SHORT).show();
    }


    public void restart(){

        LL=(LinearLayout)findViewById(R.id.LL);
        wrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        match = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        LL.removeAllViews();
        g=new glsv(this);

        ViewTreeObserver vto = LL.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LL.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int width = LL.getMeasuredWidth();
                int height = LL.getMeasuredHeight();
                g.mRenderer.setWH(width, height);
            }
        });
        g.mRenderer.RotationSpeedCoefficient=RSC;

        LL.addView(g, wrap);
        if(!firstTime){
            ss.setVisibility(View.INVISIBLE);
            ss.steps=0;
            ss.msg="";
            ss.invalidate();
        }
        firstTime=true;


    }
    public void RandomeRotate(){

        if(!g.mRenderer.onCoherent && !g.mRenderer.isRotating){

            g.mRenderer.glrc.RandomRotate();
            if(!firstTime)
                ss.setVisibility(View.INVISIBLE);

            g.mRenderer.Solution=new LinkedList<Move>();
            g.mRenderer.SolutionIndex=0;
            g.mRenderer.hasSolution=false;
            if(g.mRenderer.ss!=null){
                g.mRenderer.ss.setVisibility(View.INVISIBLE);
                g.mRenderer.ss.steps=0;
                g.mRenderer.ss.msg="";
            }
        }
    }

    public boolean firstTime=true;


    public void NextStep(){
        firstTime=false;
        if(!g.mRenderer.hasSolution){
            g.mRenderer.getSolution();
            ss=new StepsShower(this,screenx,screeny);
            ss.setVisibility(View.INVISIBLE);
            g.mRenderer.setStpesShower(ss);
            addContentView(ss, wrap);
        }
        if(!g.mRenderer.onCoherent && g.mRenderer.Solution.size()!=0 && g.mRenderer.hasSolution){

            g.mRenderer.timeflag = false;
            g.mRenderer.isRotating = false;
            g.mRenderer.time0 = SystemClock.currentThreadTimeMillis();
            int[] ans = g.mRenderer.Solution.get(g.mRenderer.SolutionIndex).action;
            g.mRenderer.setRotation(ans[0], ans[1], ans[2]);
            ss.msg=g.mRenderer.Solution.get(g.mRenderer.SolutionIndex).message;
            g.mRenderer.SolutionIndex++;

            ss.steps++;
            ss.setVisibility(View.VISIBLE);
            ss.invalidate();

            if(g.mRenderer.SolutionIndex==g.mRenderer.Solution.size()){
                g.mRenderer.hasSolution=false;
                g.mRenderer.SolutionIndex=0;
                ss.steps=0;
                g.mRenderer.Solution=new LinkedList<Move>();
            }


        }

    }

    public void PreviousStep(){
        if(g.mRenderer.hasSolution && g.mRenderer.SolutionIndex!=0 && !g.mRenderer.onCoherent){
            g.mRenderer.timeflag = false;
            g.mRenderer.isRotating = false;
            g.mRenderer.time0 = SystemClock.currentThreadTimeMillis();
            int[] ans = g.mRenderer.Solution.get(g.mRenderer.SolutionIndex-1).action;
            g.mRenderer.setRotation(ans[0], ans[1], -ans[2]);
            ss.msg="Reversing Move";
            g.mRenderer.SolutionIndex--;

            ss.steps--;
            ss.setVisibility(View.VISIBLE);
            ss.invalidate();
        }
    }
    public void solve(){
        firstTime=false;
        if(!g.mRenderer.hasSolution){
             g.mRenderer.getSolution();
            ss=new StepsShower(this,screenx,screeny);
            ss.setVisibility(View.INVISIBLE);
            if(g.mRenderer.Solution.size()!=0 && g.mRenderer.SolutionIndex!=g.mRenderer.Solution.size()){
                ss.setVisibility(View.VISIBLE);
            }
            addContentView(ss, wrap);
            g.mRenderer.setStpesShower(ss);
        }
        if (!g.mRenderer.isRotating && !g.mRenderer.onCoherent) {
            Continue();

        }
    }
    public void Pause(){
        g.mRenderer.onCoherent=false;
        g.mRenderer.Paused=true;
        if(g.mRenderer.onCoherent){
            g.mRenderer.clear_Rotation();
        }
    }
    public void Continue(){

        if(!firstTime){
            g.mRenderer.Paused=false;
            g.mRenderer.onCoherent=true;
        }
    }
    public void enterSettings(){


        setContentView(R.layout.settingslayout);


        seekBar=(SeekBar)findViewById(R.id.seekBar);
        int progress=200-(int)(9.0/RSC);
        seekBar.setProgress(progress);

        inSettings=true;


        Button ConfirmBTN=(Button)findViewById(R.id.ConfirmBTN);
        ConfirmBTN.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //g.mRenderer.RotationSpeedCoefficient = 90 / (1000 * temp);

                setContentView(R.layout.activity_main);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                int p=200-seekBar.getProgress();
                RSC=9/(float)p;

                restart();
                inSettings=false;

            }
        });

        Button CclBTN=(Button)findViewById(R.id.cclbtn);
        CclBTN.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //g.mRenderer.RotationSpeedCoefficient = 90 / (1000 * temp);

                setContentView(R.layout.activity_main);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                restart();
                inSettings=false;

            }
        });

    }

    public void enterAbout(){
        setContentView(R.layout.aboutlayout);
        inAbout=true;
        Button AboutBTN=(Button)findViewById(R.id.AboutBTN);
        AboutBTN.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //g.mRenderer.RotationSpeedCoefficient = 90 / (1000 * temp);

                setContentView(R.layout.activity_main);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                restart();
                inAbout=false;
            }
        });
    }


    public void intelliSolve(){
    /*    int n=10000;

        Cube[] css=new Cube[n];
        long aa=SystemClock.currentThreadTimeMillis();
        for (int i = 0; i <n ; i++) {
            css[i]=g.mRenderer.glrc.Jcube.clone();
          //  Log.w("Logged:   ",String.valueOf(i));
        }
        long bb=SystemClock.currentThreadTimeMillis();

        Log.w("Time: ",String.valueOf(bb-aa));*/


       firstTime=false;
        if(!g.mRenderer.hasSolution){
            g.mRenderer.getIntelliSolution();
            ss=new StepsShower(this,screenx,screeny);
            ss.setVisibility(View.INVISIBLE);
            if(g.mRenderer.Solution.size()!=0 && g.mRenderer.SolutionIndex!=g.mRenderer.Solution.size()){
                ss.setVisibility(View.VISIBLE);
            }
            addContentView(ss, wrap);
            g.mRenderer.setStpesShower(ss);
        }
        if (!g.mRenderer.isRotating && !g.mRenderer.onCoherent) {
            Continue();

        }
    }




    @Override
    protected void onPause(){
        super.onPause();
        g.mRenderer.clear_Rotation();

    }
    @Override
    protected void onResume(){
        super.onResume();
        g.mRenderer.clear_Rotation();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            if(inSettings){
                setContentView(R.layout.activity_main);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                int p=seekBar.getProgress();

                RSC=9/(float)p;
                restart();
                inSettings=false;
            }
            else {
                if(inAbout){
                    setContentView(R.layout.activity_main);
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    setSupportActionBar(toolbar);
                    restart();
                    inAbout=false;
                }
                else {
                    //System.exit(0);
                    finish();
                }
            }
        }

        return false;

    }


    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

}
