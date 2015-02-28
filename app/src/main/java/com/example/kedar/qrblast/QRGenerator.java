package com.example.kedar.qrblast;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;


public class QRGenerator extends Activity {
    private static final int OPEN_DOCUMENT_REQUEST = 1;
    private void openDocument() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        startActivityForResult(intent, OPEN_DOCUMENT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_DOCUMENT_REQUEST) {
            if (resultCode != RESULT_OK)
                return;

            Uri uri = data.getData();
            String base64 = "";
            try {
                String text = readTextFromUri(uri);
                byte[] data64 = null;
                    try {
                        data64 = text.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                base64 = Base64.encodeToString(data64, Base64.DEFAULT);
                //Log.w("base64", base64);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<String> splitted = new ArrayList<String>();
            int i = 0;
            while (i < base64.length()) {
                splitted.add(base64.substring(i, Math.min(i + 900, base64.length())));
                i += 900;
            }
            new LongOperation().execute(splitted);
        }
    }
    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();
        reader.close();
        return stringBuilder.toString();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);// String to be encoded with Base64
        openDocument();
    }

    private class LongOperation extends AsyncTask <ArrayList<String>, Void, String> {
        @Override
        protected String doInBackground(ArrayList<String>... params) {
            ArrayList<String> splitted = params[0];
            int size = splitted.size();
            String appended = "";
            while(true) {
                int counter = 1;
                for (String str : splitted) {
                    QRCodeWriter writer = new QRCodeWriter();
                    try {
                        appended = counter + "|" + size + "|" + str;
                        Log.w("data", appended);
                        BitMatrix bitMatrix = writer.encode(appended, BarcodeFormat.QR_CODE, 768, 768);
                        int width = bitMatrix.getWidth();
                        int height = bitMatrix.getHeight();
                        final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                        for (int x = 0; x < width; x++) {
                            for (int y = 0; y < height; y++) {
                                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((ImageView) findViewById(R.id.qr_result)).setImageBitmap(bmp);
                            }
                        });
                        counter++;
                        Thread.sleep(1800);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qrgenerator, menu);
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
