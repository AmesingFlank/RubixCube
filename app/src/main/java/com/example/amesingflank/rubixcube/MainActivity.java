package com.example.amesingflank.rubixcube;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
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


    String RestartStr="Restart";
    String DisarrangeStr="Disarrange";
    String Next_StepStr="Next Step";
    String Previous_StepStr="Previous Step";
    String SolveStr="Solve";
    String PauseStr="Pause";
    String ContinueStr="Continue";
    String AboutStr="About";
    String camStr="Scan Cube";

    Button scanBtn;
    Button finishscanBtn;
    ScanInstructer si;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setLan();

        final Camera camera;
        camera=Camera.open(0);

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



    public void setLan(){
        if(Locale.getDefault().getLanguage().equals("zh")){
            RestartStr="重组";
            DisarrangeStr="打乱";
            Next_StepStr="下一步";
            Previous_StepStr="上一步";
            SolveStr="求解";
            PauseStr="暂停";
            ContinueStr="继续";
            AboutStr="关于";
            camStr="扫描魔方";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.removeItem(0);
        menu.add(0, 2, 0, RestartStr);
        menu.add(0, 3, 0, DisarrangeStr);
        menu.add(0, 4, 0, Next_StepStr);
        menu.add(0, 5, 0, Previous_StepStr);
        menu.add(0, 6, 0, SolveStr);
        menu.add(0, 7, 0, PauseStr);
        menu.add(0, 8, 0, ContinueStr);
        menu.add(0, 9, 0, AboutStr);
        menu.add(0, 10, 0, camStr);
        menu.add(0,11,0,"Intellisolve");



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
                enterScan();
                break;
            case 11:
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
        if(ss!=null){
            ss.setVisibility(View.INVISIBLE);
            ss.steps=0;
            ss.msg="";
            ss.invalidate();
        }
        firstTime=true;




        scanBtn=(Button)findViewById(R.id.scanBTN);
        scanBtn.setVisibility(View.INVISIBLE);
        scanBtn.setText("Scan");
        scanBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(g.touchedFace!=-1){
                    Backup();
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, CubeScanner.class);
                    intent.putExtra("cube", JCubeBackup.clone());
                    intent.putExtra("scanIndex", g.touchedFace);
                    intent.putExtra("onScan", g.onScan);

                    startActivity(intent);
                    finish();
                }
            }
        });

        finishscanBtn=(Button)findViewById(R.id.finishscanBTN);
        finishscanBtn.setVisibility(View.INVISIBLE);
        finishscanBtn.setText("Finish Scan");
        finishscanBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishScan();
            }
        });

        g.onScan=false;

        hideScanners();
        checkIntent();


    }
    public void RandomeRotate(){

        if(!g.mRenderer.onCoherent && !g.mRenderer.isRotating && !g.onScan){

            g.mRenderer.glrc.RandomRotate();
            if(!firstTime && ss!=null)
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
        if(!g.mRenderer.hasSolution && !g.onScan){
            g.mRenderer.getSolution();
            if(g.mRenderer.hasSolution && g.mRenderer.Solution.getLast().action[0]==555){
                g.mRenderer.onCoherent=false;
                g.mRenderer.Paused=false;
                g.mRenderer.hasSolution=false;
                g.mRenderer.SolutionIndex=0;
                g.mRenderer.Solution=new LinkedList<>();
                new AlertDialog.Builder(this)
                        .setTitle("Faulty Cube")
                        .setMessage("No solution found. An error might have occurred during scanning.")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface di,int i){

                                    }
                                }
                        )
                        .show();
            }
            else {
                ss=new StepsShower(this,screenx,screeny);
                ss.setVisibility(View.INVISIBLE);
                g.mRenderer.setStpesShower(ss);
                addContentView(ss, wrap);
            }

        }
        if(g.mRenderer.hasSolution && !g.onScan && !g.mRenderer.onCoherent && g.mRenderer.Solution.size()!=0  ){

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
        //Backup();

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
        //finishScan();
        firstTime=false;
        if(!g.mRenderer.hasSolution && !g.onScan){
             g.mRenderer.getSolution();
            if(g.mRenderer.hasSolution && g.mRenderer.Solution.getLast().action[0]==555 ){
                g.mRenderer.onCoherent=false;
                g.mRenderer.Paused=false;
                g.mRenderer.hasSolution=false;
                g.mRenderer.SolutionIndex=0;
                g.mRenderer.Solution=new LinkedList<>();
                new AlertDialog.Builder(this)
                        .setTitle("Faulty Cube")
                        .setMessage("An error might have occurred during scanning")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface di,int i){

                                    }
                                }
                        )
                        .show();
            }
            else {
                ss=new StepsShower(this,screenx,screeny);
                ss.setVisibility(View.INVISIBLE);
                if(g.mRenderer.Solution.size()!=0 && g.mRenderer.SolutionIndex!=g.mRenderer.Solution.size()){
                    ss.setVisibility(View.VISIBLE);
                    ss.invalidate();
                }
                addContentView(ss, wrap);
                g.mRenderer.setStpesShower(ss);
            }

        }
        if (!g.mRenderer.isRotating && !g.mRenderer.onCoherent && !g.onScan) {
            Continue();

        }
    }

    public void Pause(){
        g.mRenderer.Paused=true;
        if(g.mRenderer.onCoherent){
            g.mRenderer.clear_Rotation();
        }
        g.mRenderer.onCoherent=false;


    }
    public void Continue(){

        if(g.mRenderer.hasSolution ){
            g.mRenderer.Paused=false;
            g.mRenderer.onCoherent=true;
            ss.setVisibility(View.VISIBLE);
            ss.invalidate();
        }
    }

    JavaCube JCubeBackup;
    int SolutionIndexBackup;
    boolean hasSolutionBackup;
    LinkedList<Move> SolutionBackup;
    int stepsBackup=0;

    public void Backup(){
        JCubeBackup=g.mRenderer.glrc.Jcube.clone();
        SolutionIndexBackup=g.mRenderer.SolutionIndex;
        hasSolutionBackup=g.mRenderer.hasSolution;
        SolutionBackup=g.mRenderer.Solution;
        if(ss!=null){
            stepsBackup=ss.steps;
        }

    }

    public void RestoreBackup() {
        if(JCubeBackup!=null){
            g.mRenderer.setSyncwithJavaCube(JCubeBackup);
        }
        g.mRenderer.SolutionIndex=SolutionIndexBackup;
        g.mRenderer.Solution=SolutionBackup;
        g.mRenderer.hasSolution=hasSolutionBackup;
        if(ss!=null && g.mRenderer.hasSolution){
            try {
                addContentView(ss,wrap);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            ss.steps=stepsBackup;
            g.mRenderer.setStpesShower(ss);
        }

    }


    public void enterSettings(){

        Pause();
        Backup();

        setContentView(R.layout.settingslayout);



        seekBar=(SeekBar)findViewById(R.id.seekBar);
        int progress=200- (int) (9.0 /RSC);
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
                int p = 200 - seekBar.getProgress();
                RSC = 9 / (float) p;

                LL.removeAllViews();
                restart();
                Pause();
                inSettings = false;
                RestoreBackup();

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

                LL.removeAllViews();
                restart();
                inSettings = false;
                RestoreBackup();

            }
        });

    }

    public void printAbout(){
        TextView name=(TextView)findViewById(R.id.textView6);
        name.setText("Created By Frank Lu 卢敦凡");
        TextView schoolEng=(TextView)findViewById(R.id.textView7);
        schoolEng.setText("High School Affiliated to the Renmin University of China, International Curriculum Center");
        TextView schoolCh=(TextView)findViewById(R.id.textView8);
        schoolCh.setText("人大附中 国际课程中心 RDFZ ICC");
        TextView email=(TextView)findViewById(R.id.textView10);
        email.setText("Email：ldfrank533@outlook.com");
    }
    public void enterAbout(){
        Pause();
        Backup();
        setContentView(R.layout.aboutlayout);
        inAbout=true;
        printAbout();
        Button AboutBTN=(Button)findViewById(R.id.AboutBTN);
        AboutBTN.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //g.mRenderer.RotationSpeedCoefficient = 90 / (1000 * temp);

                setContentView(R.layout.activity_main);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                restart();
                inAbout = false;
                RestoreBackup();
            }
        });
    }
    public void intelliSolve(){

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
                int p=200-seekBar.getProgress();
                RSC=9/(float)p;

                LL.removeAllViews();
                restart();
                Pause();
                inSettings=false;
                RestoreBackup();
            }
            else {
                if(inAbout){
                    setContentView(R.layout.activity_main);
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    setSupportActionBar(toolbar);
                    restart();
                    inAbout=false;
                    RestoreBackup();
                }
                else {
                    if(g.onScan){
                        finishScan();
                    }
                    else {
                        finish();
                    }
                    //System.exit(0);
                }
            }
        }

        return false;

    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    int scanIndex=0;

    public void enterScan() {

        if(checkCameraHardware(this)==false){
            Toast.makeText(this,"Camera not supported",Toast.LENGTH_SHORT);
        }
        else {
            si=new ScanInstructer(this,screenx,screeny);

            addContentView(si, wrap);

            Backup();
            RestoreBackup();
            SolutionBackup=new LinkedList<>();
            SolutionIndexBackup=0;
            hasSolutionBackup=false;
            g.mRenderer.onCoherent=false;
            g.mRenderer.Paused=false;
            g.mRenderer.hasSolution=false;
            g.mRenderer.Solution=new LinkedList<>();
            g.mRenderer.SolutionIndex=0;
           /* JCubeBackup=new JavaCube();
            for (int x = 0; x <3 ; x++) {
                for (int y = 0; y < 3; y++) {
                    for (int z = 0; z < 3; z++) {
                        for (int f = 0; f <6 ; f++) {
                            JCubeBackup.singleCubes[x][y][z].ColorArray[f]=JavaSingleCube.blank;
                        }
                    }
                }
            }*/
            if(ss!=null){
                ss.setVisibility(View.INVISIBLE);
            }
            g.onScan=true;
            showScanners();
        }


    }
    public void finishScan(){
        g.onScan=false;
        hideScanners();
        RestoreBackup();

    }

    public void hideScanners(){
        if(scanBtn!=null){
            scanBtn.setVisibility(View.INVISIBLE);
        }
        if(finishscanBtn!=null){
            finishscanBtn.setVisibility(View.INVISIBLE);
        }
        if(si!=null){
            si.setVisibility(View.INVISIBLE);
            si.msg="";
            si.invalidate();
        }
    }

    public void showScanners(){
        if(scanBtn!=null){
            scanBtn.setVisibility(View.VISIBLE);
        }
        if(finishscanBtn!=null){
            finishscanBtn.setVisibility(View.VISIBLE);
        }
        if(si!=null){
            si.setVisibility(View.VISIBLE);
            si.msg=" Touch a face to select it for scanning";
            si.invalidate();
        }
    }

    public void checkIntent(){
        scanIndex=getIntent().getIntExtra("scanIndex",-1);
        g.onScan=getIntent().getBooleanExtra("onScan",false);
        JavaCube tempCube=(JavaCube)getIntent().getSerializableExtra("cube");
        if(tempCube!=null && g.onScan){
            JCubeBackup=tempCube.clone();

        }
        getIntent().removeExtra("cube");
        if(g.onScan==false){
            hideScanners();
        }
        String picPath=getIntent().getStringExtra("picPath");
        getIntent().removeExtra("picPath");
        getIntent().removeExtra("onScan");
        getIntent().removeExtra("scanIndex");
        if(picPath!=null && g.onScan){
            si=new ScanInstructer(this,screenx,screeny);

            addContentView(si, wrap);

            if(JCubeBackup==null)
                JCubeBackup=new JavaCube();
            Bitmap result=getBitmapFromPath(picPath);
            String[][] thisface=getColorFromBitmap(result);
            getIntent().removeExtra("picPath");
            showScanners();
            switch (scanIndex){
                case 0:
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            switch (thisface[i][j]){
                                case "orange":
                                    JCubeBackup.singleCubes[i][2-j][0].ColorArray[scanIndex]=JavaSingleCube.orange;
                                    break;
                                case "red":
                                    JCubeBackup.singleCubes[i][2-j][0].ColorArray[scanIndex]=JavaSingleCube.red;
                                    break;
                                case "blue":
                                    JCubeBackup.singleCubes[i][2-j][0].ColorArray[scanIndex]=JavaSingleCube.blue;
                                    break;
                                case "green":
                                    JCubeBackup.singleCubes[i][2-j][0].ColorArray[scanIndex]=JavaSingleCube.green;
                                    break;
                                case "yellow":
                                    JCubeBackup.singleCubes[i][2-j][0].ColorArray[scanIndex]=JavaSingleCube.yellow;
                                    break;
                                case "white":
                                    JCubeBackup.singleCubes[i][2-j][0].ColorArray[scanIndex]=JavaSingleCube.white;
                                    break;
                            }
                        }
                    }
                    break;
                case 1:
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            switch (thisface[i][j]){
                                case "orange":
                                    JCubeBackup.singleCubes[2-i][2-j][2].ColorArray[scanIndex]=JavaSingleCube.orange;
                                    break;
                                case "red":
                                    JCubeBackup.singleCubes[2-i][2-j][2].ColorArray[scanIndex]=JavaSingleCube.red;
                                    break;
                                case "blue":
                                    JCubeBackup.singleCubes[2-i][2-j][2].ColorArray[scanIndex]=JavaSingleCube.blue;
                                    break;
                                case "green":
                                    JCubeBackup.singleCubes[2-i][2-j][2].ColorArray[scanIndex]=JavaSingleCube.green;
                                    break;
                                case "yellow":
                                    JCubeBackup.singleCubes[2-i][2-j][2].ColorArray[scanIndex]=JavaSingleCube.yellow;
                                    break;
                                case "white":
                                    JCubeBackup.singleCubes[2-i][2-j][2].ColorArray[scanIndex]=JavaSingleCube.white;
                                    break;
                            }
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            switch (thisface[i][j]){
                                case "orange":
                                    JCubeBackup.singleCubes[0][2-j][2-i].ColorArray[scanIndex]=JavaSingleCube.orange;
                                    break;
                                case "red":
                                    JCubeBackup.singleCubes[0][2-j][2-i].ColorArray[scanIndex]=JavaSingleCube.red;
                                    break;
                                case "blue":
                                    JCubeBackup.singleCubes[0][2-j][2-i].ColorArray[scanIndex]=JavaSingleCube.blue;
                                    break;
                                case "green":
                                    JCubeBackup.singleCubes[0][2-j][2-i].ColorArray[scanIndex]=JavaSingleCube.green;
                                    break;
                                case "yellow":
                                    JCubeBackup.singleCubes[0][2-j][2-i].ColorArray[scanIndex]=JavaSingleCube.yellow;
                                    break;
                                case "white":
                                    JCubeBackup.singleCubes[0][2-j][2-i].ColorArray[scanIndex]=JavaSingleCube.white;
                                    break;
                            }
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            switch (thisface[i][j]){
                                case "orange":
                                    JCubeBackup.singleCubes[2][2-j][i].ColorArray[scanIndex]=JavaSingleCube.orange;
                                    break;
                                case "red":
                                    JCubeBackup.singleCubes[2][2-j][i].ColorArray[scanIndex]=JavaSingleCube.red;
                                    break;
                                case "blue":
                                    JCubeBackup.singleCubes[2][2-j][i].ColorArray[scanIndex]=JavaSingleCube.blue;
                                    break;
                                case "green":
                                    JCubeBackup.singleCubes[2][2-j][i].ColorArray[scanIndex]=JavaSingleCube.green;
                                    break;
                                case "yellow":
                                    JCubeBackup.singleCubes[2][2-j][i].ColorArray[scanIndex]=JavaSingleCube.yellow;
                                    break;
                                case "white":
                                    JCubeBackup.singleCubes[2][2-j][i].ColorArray[scanIndex]=JavaSingleCube.white;
                                    break;
                            }
                        }
                    }
                    break;
                case 4:
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            switch (thisface[i][j]){
                                case "orange":
                                    JCubeBackup.singleCubes[i][0][j].ColorArray[scanIndex]=JavaSingleCube.orange;
                                    break;
                                case "red":
                                    JCubeBackup.singleCubes[i][0][j].ColorArray[scanIndex]=JavaSingleCube.red;
                                    break;
                                case "blue":
                                    JCubeBackup.singleCubes[i][0][j].ColorArray[scanIndex]=JavaSingleCube.blue;
                                    break;
                                case "green":
                                    JCubeBackup.singleCubes[i][0][j].ColorArray[scanIndex]=JavaSingleCube.green;
                                    break;
                                case "yellow":
                                    JCubeBackup.singleCubes[i][0][j].ColorArray[scanIndex]=JavaSingleCube.yellow;
                                    break;
                                case "white":
                                    JCubeBackup.singleCubes[i][0][j].ColorArray[scanIndex]=JavaSingleCube.white;
                                    break;
                            }
                        }
                    }
                    break;
                case 5:
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            switch (thisface[i][j]){
                                case "orange":
                                    JCubeBackup.singleCubes[i][2][2-j].ColorArray[scanIndex]=JavaSingleCube.orange;
                                    break;
                                case "red":
                                    JCubeBackup.singleCubes[i][2][2-j].ColorArray[scanIndex]=JavaSingleCube.red;
                                    break;
                                case "blue":
                                    JCubeBackup.singleCubes[i][2][2-j].ColorArray[scanIndex]=JavaSingleCube.blue;
                                    break;
                                case "green":
                                    JCubeBackup.singleCubes[i][2][2-j].ColorArray[scanIndex]=JavaSingleCube.green;
                                    break;
                                case "yellow":
                                    JCubeBackup.singleCubes[i][2][2-j].ColorArray[scanIndex]=JavaSingleCube.yellow;
                                    break;
                                case "white":
                                    JCubeBackup.singleCubes[i][2][2-j].ColorArray[scanIndex]=JavaSingleCube.white;
                                    break;
                            }
                        }
                    }
                    break;

            }
            RestoreBackup();

        }

    }

    private Bitmap getBitmapFromPath(String path) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(path);
            bitmap = BitmapFactory.decodeStream(fis);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(),
                    matrix, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
    public String[][] getColorFromBitmap(Bitmap bitmap){
        int width=bitmap.getWidth();
        int edge=width/4;
        String[][] ans=new String[3][3];
        for (int i = 0; i <3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                int c=bitmap.getPixel((i+1)*edge,(j+1)*edge);
                String s=getBestMatchingColorName(c);
                ans[i][j]=s;
            }
        }
        return ans;
    }

    private String getBestMatchingColorName(int pixelColor) {

        float hsv[]=new float[3];
        Color.colorToHSV(pixelColor,hsv);
        float s=hsv[1];
        if(s<0.35){
            return "white";
        }
        float h=hsv[0];
        if(h<11 || h>280){
            return "red";
        }
        if(h>=11 && h<40){
            return "orange";
        }
        if(h>=40 && h<70){
            return "yellow";
        }
        if(h>=70 && h<167){
            return "green";
        }
        if(h>=167 && h<=280){
            return "blue";
        }
        return "white";

    }




}
