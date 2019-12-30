package com.example.contactqr;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends AppCompatActivity {


    private static final int WRITE_PERMISSION_CODE = 2;
    public static final String MY_PREFS_NAME = "sharedPrefFile";
    public static String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";


    String TAG = "GenerateQRCode";
    EditText full_name, phone1, phone2;
    String qr_full_name, qr_phone1, qr_phone2;
    ImageView qrImage;
    Button start;
    Bitmap bmp;
    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qrImage = (ImageView) findViewById(R.id.QR_Image);
        full_name = (EditText) findViewById(R.id.edt_full_name);
        phone1 = (EditText) findViewById(R.id.edt_phone_1);
        phone2 = (EditText) findViewById(R.id.edt_phone_2);
        start = (Button) findViewById(R.id.start);



        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        if(prefs.getBoolean("activity_executed", false)){
            Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);
            resultIntent.putExtra("full_name", qr_full_name);
            resultIntent.putExtra("phone_1", qr_phone1);
            resultIntent.putExtra("phone_2", qr_phone2);
            startActivity(resultIntent);
            finish();
        }else {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("activity_executed",true);
            editor.apply();
        }
    }


    public void generate_qr(View view) {

        qr_full_name = full_name.getText().toString().trim();
        qr_phone1 = phone1.getText().toString().trim();
        qr_phone2 = phone2.getText().toString().trim();

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode( qr_full_name + "\n"+ qr_phone1 +"\n"+ qr_phone2,
                    BarcodeFormat.QR_CODE, 300, 300);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for(int x = 0; x <width; x++){
                for(int y = 0; y < height;y++){
                    bmp.setPixel(x,y,bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }
            for(int i = 0; i < perms.length; i++){
                if(ContextCompat.checkSelfPermission(this, perms[0] )!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION_CODE);

                }
            }

            //qrImage.setImageBitmap(bmp);
        }

        catch (WriterException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case WRITE_PERMISSION_CODE : {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    saveQr();
                    goToResultActivity();
                }
            }
        }
    }

    public void saveQr() {
        boolean save;
        String result;
        try {
            save = QRGSaver.save(savePath, "qr_code", bmp, QRGContents.ImageType.IMAGE_JPEG);
            result = save ? "Image Saved" : "Image Not Saved";
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToResultActivity(){
        Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);
        resultIntent.putExtra("image_bitmap", bmp);
        startActivity(resultIntent);
        finish();
    }
}
