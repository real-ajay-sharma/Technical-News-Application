package com.example.technews;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Activity context, ArrayList<News> news){
        super(context,0,news);

    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News object = getItem(position);
        TextView title = listItemView.findViewById(R.id.title);
        title.setText(object.getTitle());

        TextView description = listItemView.findViewById(R.id.description);
        description.setText(object.getDescription());

        ImageView imageView = listItemView.findViewById(R.id.image);
       // imageView.setImageBitmap(object.getBitmapFromURL(object.getImageUrl()))
        String url = object.getImageUrl();
        Picasso.with(getContext()).load(url).into(imageView);


        return listItemView;
    }

}
