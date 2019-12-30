package com.example.contactqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class Edit : AppCompatActivity(), ZXingScannerView.ResultHandler  {


    var scannerView: ZXingScannerView ? = null
    var fullname: String? = ""
    var phone1: String?= ""
    var phone2: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)

    }

    override fun handleResult(rawResult: Result?) {

        val result = rawResult?.getText()?.trim { it <= ' ' }

        val parts = result?.split( Regex("\n"))
        fullname = parts?.get(0)
        phone1 = parts?.getOrNull(1)
        phone2 = parts?.getOrNull(2)


        val contactIntent = Intent(ContactsContract.Intents.Insert.ACTION)
        contactIntent.type = ContactsContract.RawContacts.CONTENT_TYPE

        contactIntent
                .putExtra(ContactsContract.Intents.Insert.NAME, fullname)
                .putExtra(ContactsContract.Intents.Insert.PHONE, phone1)
                .putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, phone2)

        startActivityForResult(contactIntent, 1)
        finish()

        onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        scannerView?.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        scannerView?.setResultHandler(this)
        scannerView?.startCamera()
    }

}
