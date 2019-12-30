package com.example.contactqr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.MultiFormatReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 1;
    public static ImageView qrImage;
    String imagePath = MainActivity.savePath;
    Bitmap bm;
    String fullname, phone1, phone2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Intent intent = getIntent();
        qrImage = (ImageView) findViewById(R.id.QR_Image);
        bm = (Bitmap) getIntent().getParcelableExtra("image_bitmap");


            fullname = getIntent().getStringExtra("full_name");
            phone1 = getIntent().getStringExtra("phone_1");
            phone2 = getIntent().getStringExtra("phone_2");


        qrImage.setImageBitmap(bm);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_PERMISSION_CODE : {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startActivity(new Intent(getApplicationContext(), Edit.class));
                }
            }
        }
    }

    public void scan_qr(View view) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);

    }


    @Override
    protected void onResume() {
        super.onResume();
        setImage(imagePath);
    }
    public void setImage(String imagePath){
        try{
            File imageFile = new File(imagePath, "qr_code.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));
            qrImage.setImageBitmap(bitmap);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // return super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    public void goToEditActivity(MenuItem item) {

        Intent editIntent = new Intent(ResultActivity.this, EditActivity.class);
        editIntent.putExtra("full_name", fullname);
        editIntent.putExtra("phone_1", phone1);
        editIntent.putExtra("phone_2", phone2);
        startActivity(editIntent);
        finish();

    }
}
