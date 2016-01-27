package com.cs456.capchu.pinchnzoom;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity{

    private Camera mCamera;
    private SurfaceView surfaceView;
    private CameraPreview mPreview;
    private Camera.PictureCallback mPictureCallback;
    private ImageView imageView;
    int activeCam = -1;
    int numCams = 0;
    int frontCamera = -1;
    int backCamera = -1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Button captureButton;
    protected static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numCams = checkCameraHardware(this);
        Log.i(TAG, "numberOfCameras: " + numCams);

        //if you have more than one camera make them swappable
        if(numCams > 1) {
            FloatingActionButton cameraSwap = (FloatingActionButton) findViewById(R.id.swap_camera);
            cameraSwap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCamera.stopPreview();
                    releaseCamera();
                    selectCamera();
                    mCamera = getCameraInstance(activeCam);

                    mCamera.setDisplayOrientation(90);
                    try {
                        mCamera.setPreviewDisplay(mPreview.getHolder());
                    } catch (IOException e) {
                        Log.i(TAG, "Can't Set Preview Display: " + e.getMessage());
                    }
                    mCamera.startPreview();
                }
            });
        }


        FloatingActionButton takePicture = (FloatingActionButton) findViewById(R.id.button_capture);
        takePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCamera.takePicture(null, null, mPicture);
                Log.i(TAG, "Picture Taken");
                Uri u = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                File f = getOutputMediaFile(MEDIA_TYPE_IMAGE);

                //View Pic Activity Stuff Here
            }
        });


        //Create an instance of Camera
        selectCamera();
        mCamera = getCameraInstance(backCamera);

        //Create our preiview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        mCamera.setDisplayOrientation(90);
        preview.addView(mPreview);
    }

    private void selectCamera(){
        if(activeCam == -1){
            activeCam = backCamera;
        }else if(activeCam == backCamera){
            activeCam = frontCamera;
        }else{
            activeCam = backCamera;
        }


    }

    //Code From Stack Overflow "Safer Way to Open Camera"
    public static Camera getCameraInstance(int toOpen){
        Camera c = null;
        try{
            c = Camera.open(toOpen);
            //Camera.Parameters params = c.getParameters();
            //params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            //c.setParameters(params);
        }catch(Exception e){
            Log.i(TAG, "Camera Not Available: " + e.getMessage());
        }
        return c;
    }


    @Override
    protected void onPause(){
        super.onPause();
        mCamera.stopPreview();
        releaseCamera();
    }

    private void releaseCamera(){
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    private int checkCameraHardware(Context context)
    {
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            //Has Camera
            int numCams = Camera.getNumberOfCameras();
            for (int i = 0; i < numCams; i++){
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);;
                if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
                    Log.i(TAG, "Front Camera found: " + i);
                    frontCamera = i;
                }else if(info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                    Log.i(TAG, "Back Camera found: " + i);
                    backCamera = i;
                }
            }
            return numCams;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.i(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Log.i(TAG, "Wrote Picture to File!");
            } catch (FileNotFoundException e) {
                Log.i(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.i(TAG, "Error accessing file" + e.getMessage());
            }
        }
    };

    private static final File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES), "PinchNZoom");

        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Log.i(TAG, "Failed to create directory" );
                return null;
            }
        }

        //create media file name
        String timeStamp = new SimpleDateFormat("yyyyHHdd__HHmmss").format(new Date());
        File mediaFile;
        if(type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            Log.i(TAG, "Picture Location: " + mediaFile.getAbsolutePath() );
        }else{
            return null;
        }

        return mediaFile;
    }





    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }
}


