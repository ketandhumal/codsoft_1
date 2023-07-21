package com.example.flashlightapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private ImageView TorchImage;
    private String cId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton flashBtn = findViewById(R.id.FlashButton);
         TorchImage = findViewById(R.id.TorchImage);


        //Checking availability of camera flashlight
        PackageManager packageManager = getPackageManager();

        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
        {
            Toast.makeText(this, "Flashlight is not available", Toast.LENGTH_SHORT).show();
            flashBtn.setEnabled(false);
            return;
        }

         //Camera Manager Object to perform flashlight related operations
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{
            cId = cameraManager.getCameraIdList()[0];
        }
        catch (CameraAccessException e)
        {
            e.printStackTrace();
        }

        //Toggle Button Functionality
        flashBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {

            try{
                cameraManager.setTorchMode(cId,isChecked);
                updateTorchImage( isChecked);
            }catch(CameraAccessException e){
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Failed to access the camera.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Turn off the flashlight when the app is stopped
    @Override
    protected void onStop()
    {
        super.onStop();
        try {
          cameraManager.setTorchMode(cId,false);
        }catch(CameraAccessException e){
            e.printStackTrace();
        }
    }

    //changing image of torch
    private void updateTorchImage(boolean isTorchOn) {
        if (isTorchOn) {
            TorchImage.setImageResource(R.drawable.ontorch);
        } else {
            TorchImage.setImageResource(R.drawable.offtorch);
        }
    }
}