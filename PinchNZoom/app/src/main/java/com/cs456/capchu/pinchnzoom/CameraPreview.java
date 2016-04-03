package com.cs456.capchu.pinchnzoom;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.Camera;
import android.view.ViewGroup;

import java.io.IOException;

/**
 * Created by capchu on 1/20/2016.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.Parameters camParam;
    protected static final String TAG = "MainActivity";

    public CameraPreview(Context context, Camera camera){
        super(context);
        mCamera = camera;

        //Install a SurfaceHolder.Callback so we get notified when
        //the underlying surface is created or destroyed
        mHolder = getHolder();
        mHolder.addCallback(this);
        //Depreciated setting, required pre 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            camParam = mCamera.getParameters();



            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }catch (IOException e){
            Log.i(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //If preview can change or rotate do that here
        //Make sure to stop the preview before resizing or reformatting it

        if(mHolder.getSurface() == null){
            //Preview surface does not exist
            return;
        }

        //stop prieview before making changes
        try{
            mCamera.stopPreview();
        }catch (Exception e){
            Log.i(TAG, "Could not stop preview: " + e.getMessage());
        }

        //start preveiw with new settings
        try{
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }catch (Exception e){
            Log.i(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //Apparently we are taking care of reeasing the camera in our activity?
    }
}














