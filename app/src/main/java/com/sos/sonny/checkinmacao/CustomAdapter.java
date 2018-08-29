package com.sos.sonny.checkinmacao;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by Bo on 22/10/2016.
 */

public class CustomAdapter extends ArrayAdapter{
    List<String> thumbNailList;
    List<Double> latList;
    List<Double> lngList;
    Activity context;
    int resource;

    public CustomAdapter(Activity context, int resource,List<String> thumbNailList,List<Double> latList,List<Double> lngList) {
        super(context, resource, thumbNailList);
        this.context=context;
        this.thumbNailList=thumbNailList;
        this.latList=latList;
        this.lngList=lngList;
        this.resource=resource;
    }

    public View getView(final int position, View view, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(resource,null,true);

        ImageView thumbNail = (ImageView) rowView.findViewById(R.id.thumbNail);
        TextView latitudeView = (TextView) rowView.findViewById(R.id.latitudeView);
        TextView longitudeView = (TextView) rowView.findViewById(R.id.longitudeView);
        Log.d("thumbNailList Element",thumbNailList.get(position)+"=default?");
        Log.d("thumbNailList Element","default");
        Log.d("Match",String.valueOf(thumbNailList.get(position)=="default"));
//        //if (thumbNailList.get(position).equals("default")){
//            Log.d("Image:","Default Icon");
//            thumbNail.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.defaulticon));
//        //}
//        else {
        Log.d("Image:","from SD Card");
        thumbNail.setImageBitmap(BitmapFactory.decodeFile(thumbNailList.get(position)));
        //}
        latitudeView.setText("Lat: "+String.valueOf(latList.get(position)));
        longitudeView.setText("Lng: "+String.valueOf(lngList.get(position)));

        thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Image:","is clicked!!");
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(android.content.Intent.ACTION_VIEW);
                //Log.d("URI",Uri.get(position));
//                if (thumbNailList.get(position).equals("default")){
//                    galleryIntent.setDataAndType(Uri.parse("android.resource://com.sos.sonny.checkinmacao/" + R.drawable.defaulticon), "image/*");
//                }else{
                    galleryIntent.setDataAndType(Uri.parse("file://" + thumbNailList.get(position)), "image/*");
                //}
                context.startActivity(galleryIntent);
            }
        });

        return rowView;
    }
}
