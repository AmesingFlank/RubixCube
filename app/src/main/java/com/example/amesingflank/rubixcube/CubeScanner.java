package com.example.amesingflank.rubixcube;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by AmesingFlank on 16/1/26.
 */

public class CubeScanner extends Activity implements SurfaceHolder.Callback{
    DisplayMetrics dm;
    public int screenx=1440;
    public int screeny=2560;
    float ratio=1.33333f;
    LinearLayout.LayoutParams wrap = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams match = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);


    /**
     * Camera回调，通过data[]保持图片数据信息
     */
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Intent intent = new Intent(CubeScanner.this, MainActivity.class);
                intent.putExtra("picPath", pictureFile.getAbsolutePath());
                JavaCube tempCube=(JavaCube)getIntent().getSerializableExtra("cube");
                intent.putExtra("cube",tempCube);
                intent.putExtra("scanIndex",getIntent().getIntExtra("scanIndex",-1));
                intent.putExtra("onScan",getIntent().getBooleanExtra("onScan", true));
                getIntent().removeExtra("cube");
                getIntent().removeExtra("scanIndex");
                getIntent().removeExtra("onScan");
                getIntent().removeExtra("picPath");
                startActivity(intent);
                CubeScanner.this.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private SurfaceView mCameraPreview;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanlayout);
        dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenx=dm.widthPixels;
        screeny=dm.heightPixels;
        initViews();


    }

    private void initViews() {
        mCameraPreview = (SurfaceView) findViewById(R.id.sv_camera);
        mCameraPreview.setLayoutParams(new LinearLayout.LayoutParams(screenx,(int)((float)screenx*ratio)));
        mSurfaceHolder = mCameraPreview.getHolder();
        mSurfaceHolder.addCallback(this);
        mCameraPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });
        Button captureBTN=(Button)findViewById(R.id.button);
        captureBTN.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    capture();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }


    public void capture() {
        Camera.Parameters params = mCamera.getParameters();
        params.setPictureFormat(ImageFormat.JPEG);
        // params.setPreviewSize(screenx, screenx);
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(params);
        // 使用自动对焦功能
        /*mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    mCamera.takePicture(null, null, mPictureCallback);
                }
            }
        });*/
        mCamera.takePicture(null, null, mPictureCallback);
    }

    /**
     * 获取图片保持路径
     *
     * @return pic Path
     */
    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "RubixCubeScanner");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "temp.png");
        return mediaFile;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, mSurfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mSurfaceHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setStartPreview(mCamera, mSurfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.checkCameraHardware(this) && (mCamera == null)) {
            mCamera = getCamera();
            if (mSurfaceHolder != null) {
                setStartPreview(mCamera, mSurfaceHolder);

            }
        }
    }

    /**
     * 初始化相机
     *
     * @return camera
     */
    private Camera getCamera() {
        Camera camera;
        try {
            camera = Camera.open(0);
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }

    /**
     * 检查是否具有相机功能
     *
     * @param context context
     * @return 是否具有相机功能
     */
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    /**
     * 在SurfaceView中预览相机内容
     *
     * @param camera camera
     * @param holder SurfaceHolder
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            Camera.Parameters parameters = camera.getParameters();

            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            Camera.Size prevSize = getBestSize(previewSizes);
            parameters.setPreviewSize(prevSize.width, prevSize.height);

            List<Camera.Size> picSizes = parameters.getSupportedPictureSizes();
            Camera.Size picSize = getBestSize(picSizes);
            parameters.setPictureSize(picSize.width, picSize.height);
            camera.setParameters(parameters);

            ratio=(float)prevSize.width/(float)prevSize.height;
            mCameraPreview.setLayoutParams(new LinearLayout.LayoutParams(screenx,(int)((float)screenx*ratio)));
            //holder.setFixedSize(holder.getSurfaceFrame().width(), (int) ((float) holder.getSurfaceFrame().width()));
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
            addContentView(new Positioner(this,screenx,screeny),wrap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Camera.Size getBestSize(List<Camera.Size> in){
        int bestIndex=-1;
        int bestDimension=0;
        int length=in.size();
        for (int i = 0; i < length; i++) {
            float thisRatio=(float)in.get(i).width/(float)in.get(i).height;
            if(thisRatio<=1.35 && thisRatio>=1.3 && in.get(i).height>bestDimension){
                bestIndex=i;
                bestDimension=in.get(i).height;
            }
        }

        return in.get(bestIndex);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }
}
