package com.cs456.capchu.pinchnzoom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.net.Uri;

import java.io.File;

public class OLDImageViewActivity extends AppCompatActivity {
    protected static final String TAG = "ViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Made it to imageView");
        //setContentView(R.layout.activity_image_view);

        Intent intent = getIntent();
        File f = (File) intent.getExtras().get("File");
        Bitmap b;
        BitmapFactory.Options bOpt = new BitmapFactory.Options();
        b = BitmapFactory.decodeFile(f.getAbsolutePath(), bOpt);


        TouchImageView img = new TouchImageView(this);
        img.setImageBitmap(b);
        img.setMaxZoom(10f);
        img.setRotation(270f);
        int h = img.viewHeight;
        int w = img.viewWidth;
        Log.i(TAG, "Height: " + h + " -- Width: " + w);
        //img.setCropToPadding(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            img.setClipToOutline(true);
        }
        setContentView(img);
    }

}
