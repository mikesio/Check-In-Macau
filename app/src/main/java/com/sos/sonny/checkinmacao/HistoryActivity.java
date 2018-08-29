package com.sos.sonny.checkinmacao;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends ListActivity {
    ListView listView;
    List<Double> LatList;
    List<Double> LngList;
    List<String> Lat_LngList;
    List<String> thumbNailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        DB2ListView();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void DB2ListView() {
        RecSQLiteOpenHelper recSQLiteOpenHelper = new RecSQLiteOpenHelper(this);
        SQLiteDatabase db = recSQLiteOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ID, Lat, Lng, Path from checkInHistory", null);
        LatList =new ArrayList<Double>();
        LngList = new ArrayList<Double>();
        thumbNailList=new ArrayList<String>();
        Lat_LngList = new ArrayList<String>();
        while (c.moveToNext()){
            Integer id1 = c.getInt(c.getColumnIndex("ID"));
            Double lat = c.getDouble(c.getColumnIndex("Lat"));
            Double lng = c.getDouble(c.getColumnIndex("Lng"));
            String thumbnail =c.getString(c.getColumnIndex("Path"));
            Lat_LngList.add("Lat: " + lat.toString() + "; " + "Lng: " + lng.toString());
            LatList.add(lat);
            LngList.add(lng);
            thumbNailList.add(thumbnail);
        }
        db.close();

        CustomAdapter adapter = new CustomAdapter(this,R.layout.list,thumbNailList,LatList,LngList);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Double currentLat = LatList.get(position);
        Double currentLng = LngList.get(position);
        Intent MapsActivity = new Intent(this, MapsActivity.class);
        Bundle bData = new Bundle();
        bData.putDouble("currentLat", currentLat);
        bData.putDouble("currentLng", currentLng);
        MapsActivity.putExtras(bData);
        startActivity(MapsActivity);
    }
}
