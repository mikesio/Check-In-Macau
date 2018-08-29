package com.sos.sonny.checkinmacao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Sonny on 28/09/2016.
 */

public class RecSQLiteOpenHelper extends SQLiteOpenHelper {

    private static String DB_Name = "checkInHistory.db";
    private static int VERSION = 100;
    private Bitmap defaultThumbnail;
    private String defaultThumbnailDir = "/storage/sdcard/Android/data/com.sos.sonny.checkinmacao/files/Pictures/defaulticon.jpg";
    Context context;

    public RecSQLiteOpenHelper(Context context){
        this(context, DB_Name, null, VERSION);
        this.context=context;
    }

    public RecSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        defaultThumbnail = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaulticon);
        db.execSQL("Create Table checkInHistory (ID Integer primary key, Lat real, Lng real,Path Text)");
        db.execSQL("insert into checkInHistory values (1, 22.157328, 113.559762, '/storage/sdcard/defaulticon.jpg')");
        db.execSQL("insert into checkInHistory values (2, 22.190049, 113.543151, '/storage/sdcard/defaulticon.jpg')");
        db.execSQL("insert into checkInHistory values (3, 22.202886, 113.546996, '/storage/sdcard/defaulticon.jpg')");
        db.execSQL("insert into checkInHistory values (4, 22.1428869, 113.563922, '/storage/sdcard/defaulticon.jpg')");
        db.execSQL("insert into checkInHistory values (5, 22.131644, 113.562769, '/storage/sdcard/defaulticon.jpg')");
        //Log.d("SQLite", "onCreate!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 100 && newVersion == 200){
            db.execSQL("Drop Table checkInHistory");
            onCreate(db);
        }
    }
}
