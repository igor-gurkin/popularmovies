package com.udacity.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    TextView mItemTitleView;
    TextView mItemVoteAverageView;
    ImageView mItemPosterView;
    TextView mItemOverviewView;
    TextView mItemReleaseDate;
    TextView mItemOriginalLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mItemTitleView = (TextView) findViewById(R.id.item_title_view);
        mItemVoteAverageView = (TextView) findViewById(R.id.item_vote_average_view);
        mItemPosterView = (ImageView) findViewById(R.id.item_poster_view);
        mItemOverviewView = (TextView) findViewById(R.id.item_overview_view);
        mItemReleaseDate = (TextView) findViewById(R.id.item_release_date_view);
        mItemOriginalLanguage = (TextView) findViewById(R.id.item_original_language_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        MovieRecord movieRecord = intent.getExtras().getParcelable("object_parcel");

        if (movieRecord != null) {
            mItemTitleView.setText(movieRecord.title);

            StringBuilder voteAverageString = new StringBuilder().append("Average vote: ")
                    .append(String.valueOf(movieRecord.voteAverage));
            mItemVoteAverageView.setText(voteAverageString.toString());

            mItemOverviewView.setText(movieRecord.overview);
            mItemReleaseDate.setText(movieRecord.releaseDate.substring(0,4));

            StringBuilder originalLanguageString = new StringBuilder().append("Language: ")
                    .append(movieRecord.originalLanguage);
            mItemOriginalLanguage.setText(originalLanguageString.toString());
            Picasso.with(this).load(movieRecord.posterPath).into(mItemPosterView);
        }
    }
}
