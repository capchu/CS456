package com.cs456.capchu.pinchnzoom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Window;
import android.widget.ImageView;

import java.io.File;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class PinchAndZoom extends AppCompatActivity {
    ImageViewTouch mImage;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();
    protected static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pinch_and_zoom);


        Intent intent = getIntent();
        File f = (File) intent.getExtras().get("File");
        Bitmap b;
        BitmapFactory.Options bOpt = new BitmapFactory.Options();
        b = BitmapFactory.decodeFile(f.getAbsolutePath(), bOpt);

        Log.i(TAG, "Bitmap Made");
        int width = b.getWidth();
        int height = b.getHeight();
        Log.i(TAG, "Width: " + width + " -- Height: " + height);
        if( width > 4096){

        }
        if( height > 4096){

        }


        mImage = (ImageViewTouch) findViewById(R.id.image);
        mImage.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
        mImage.setImageBitmap(b);


    }


}
