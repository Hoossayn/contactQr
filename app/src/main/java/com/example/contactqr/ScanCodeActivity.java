package com.example.contactqr;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    ZXingScannerView scannerView;
    String part1;
    String part2;
    String part3;
    String b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void handleResult(Result rawResult) {

        String result = rawResult.getText().trim();

        String[] parts = result.split("\n");
        part1 = parts[0];
        part2 = parts[1];
       // part3 = parts[2];
        String p3 = parts[2];

        for(int i = 0; i < p3.length();  i++){
            i =0;
            part3 = "";

        }/*else{
            part3 = parts[2];
        }*/

        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        contactIntent
                .putExtra(ContactsContract.Intents.Insert.NAME, part1)
                .putExtra(ContactsContract.Intents.Insert.PHONE, part2)
                .putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, part3 );

        startActivityForResult(contactIntent, 1);
        finish();

        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
