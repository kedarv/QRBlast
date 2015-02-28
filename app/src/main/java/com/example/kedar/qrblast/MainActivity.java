package com.example.kedar.qrblast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.util.Base64;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    Map<Integer, String> map = new HashMap<Integer, String>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button bScan = (Button) findViewById(R.id.bScan);
        Button bSend = (Button) findViewById(R.id.bSend);
        final IntentIntegrator integrator = new IntentIntegrator(this);
        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), QRGenerator.class);
                startActivity(myIntent);
            }
        });
        bScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan QR");
                integrator.setResultDisplayDuration(0);
                integrator.setScanningRectangle(500, 500);
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String scanned = scanResult.getContents();
            Log.w("scanned content:", scanned);
            Log.w("info", "found code");

            int pos1 = scanned.indexOf("|");
            int pos2 = scanned.indexOf("|",pos1+1);
            int h1 = Integer.parseInt(scanned.substring(0,pos1));
            int h2 = Integer.parseInt(scanned.substring(pos1+1,pos2));
            String data = scanned.substring(pos2+1);
            System.out.println(h1);
            System.out.println(h2);
            System.out.println(data);


            map.put(h1,data);
            System.out.println(map.get(h1));


//            DBHelper helper;
//            helper = new DBHelper(this);
//            SQLiteDatabase db = helper.getWritableDatabase();
//            Log.w("Notice", "called getWritable");
//            String sql = "INSERT INTO data (contents) VALUES('"+scanned+"')" ;
//            Log.w("query", "" +sql);
//            db.execSQL(sql);
//            db.close();
//            helper.close();
//
//
//            helper = new DBHelper(this);
//            Cursor cursor = helper.getReadableDatabase().
//                    rawQuery("select * from data", null);
//            int countReults = cursor.getCount();
//            Log.w("countResults", "" + countReults);
//
//            int counter = 0;
//            cursor.moveToFirst();
//            while (counter < countReults) {
//                Log.w("data", "" + cursor.getString(cursor.getColumnIndex("contents")));
//                Log.w("uid", "" +  cursor.getInt(cursor.getColumnIndex("uid")));
//                counter++;
//            }
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
}
