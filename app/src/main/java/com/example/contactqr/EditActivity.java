package com.example.contactqr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGSaver;

import static com.example.contactqr.MainActivity.savePath;

public class EditActivity extends AppCompatActivity {

    Bitmap bmp;
    EditText fullname, phone1, phone2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        fullname = (EditText)findViewById(R.id.edt_full_name);
        phone1 = (EditText)findViewById(R.id.edt_phone_1);
        phone2 = (EditText)findViewById(R.id.edt_phone_2);

        fullname.setText(getIntent().getStringExtra("full_name"));
        phone1.setText(getIntent().getStringExtra("phone_1"));
        phone2.setText(getIntent().getStringExtra("phone_2"));

    }

    public void save_qr(View view) {
        String name = fullname.getText().toString();
        String phone_1 = phone1.getText().toString();
        String phone_2 = phone2.getText().toString();
        generateQr(name, phone_1, phone_2);


    }

    public void generateQr(String fullname, String phone1, String phone2){

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode( fullname + "\n"+ phone1+"\n"+ phone2,
                    BarcodeFormat.QR_CODE, 300, 300);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for(int x = 0; x <width; x++){
                for(int y = 0; y < height;y++){
                    bmp.setPixel(x,y,bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                }
            }
            /*for(int i = 0; i < perms.length; i++){
                if(ContextCompat.checkSelfPermission(this, perms[0] )!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION_CODE);

                }
            }*/

            saveQr();
            //qrImage.setImageBitmap(bmp);
        }

        catch (WriterException e)
        {
            e.printStackTrace();
        }

        goToResultActivity();
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
        Intent resultIntent = new Intent(EditActivity.this, ResultActivity.class);
        resultIntent.putExtra("image_bitmap", bmp);
        startActivityForResult(resultIntent, 200);
        finish();
    }
}
