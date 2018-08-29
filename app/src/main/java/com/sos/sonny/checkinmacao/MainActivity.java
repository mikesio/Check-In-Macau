package com.sos.sonny.checkinmacao;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PIC_REQUEST=1873;
    String imageDir;
    Bitmap thumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //push image drawable to sd card as the default icon in listview
        Bitmap defaultThumbNail = BitmapFactory.decodeResource(getResources(),R.drawable.defaulticon);
        File file = new File(Environment.getExternalStorageDirectory().toString(),"defaulticon.jpg");
        if(!file.exists()) {
            try {
                FileOutputStream outStream = new FileOutputStream(file);
                defaultThumbNail.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Button historyButton = (Button) findViewById(R.id.history_button);
        Button checkInButton = (Button) findViewById(R.id.checkIn_button);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HistoryActivity = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(HistoryActivity);
            }
        });
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBuiltInCamera(view);
            }
        });

        //Delete the existing db file; for developing purpose
        //this.deleteDatabase("/data/data/com.sos.sonny.checkinmacao/databases/checkInHistory.db");
    }

    //call the built-in camera
    private void callBuiltInCamera(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile=createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(photoFile !=null){
            Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider",photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
            startActivityForResult(cameraIntent,CAMERA_PIC_REQUEST);
        }
    }

    //create and save image to the app
    //if the app is deleted, all the existing pictures will also be deleted
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(timeStamp,".jpg",storageDir);

        imageDir = image.getAbsolutePath();
        return image;
    }

    //the addCurrentLocation() will be triggered only if the picture is taken successfully
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_PIC_REQUEST&&resultCode==RESULT_OK){
            addCurrentLocation();
        }
    }

    private void addCurrentLocation() {
        GPSTracker tracker = new GPSTracker(MainActivity.this);
        if (tracker == null) {return;}
        double lat=tracker.getLatitude();
        double lng=tracker.getLongitude();
        RecSQLiteOpenHelper recSQLiteOpenHelper = new RecSQLiteOpenHelper(this);
        SQLiteDatabase db = recSQLiteOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ID, Lat, Lng, Path from checkInHistory", null);
        Integer lastID = 1;
        while (c.moveToNext()){lastID = lastID + 1;}
        String SQLcmd = "insert into checkInHistory values (" + lastID.toString() + ", " + lat + ", " + lng+ ", " + "'"+imageDir + "')";
        db.execSQL(SQLcmd);
        db.close();
        Toast.makeText(this,"Check in data saved.  Lat: " + lat + "; Lng: " + lng , Toast.LENGTH_SHORT).show();

    }
}

