package com.udacity.popularmoviesstage1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HP Pavilion on 3/25/2018.
 */

public class MovieRecordAdapter extends ArrayAdapter<MovieRecord> {

    public MovieRecordAdapter(Activity context, List<MovieRecord> movieRecords) {
        super(context, 0, movieRecords);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieRecord movieRecord = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie,
                    parent, false);
        }

        ImageView posterView = (ImageView) convertView.findViewById(R.id.poster_view);
        Picasso.with(getContext()).load(movieRecord.posterPath).into(posterView);

        return convertView;
    }
}
