package com.android.nazirshuqair.lastpick.listscreenfiles;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.nazirshuqair.lastpick.R;
import com.android.nazirshuqair.lastpick.detailsfiles.DetailsActivity;
import com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments.FeaturedFragment;
import com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments.MasterListFragment;
import com.android.nazirshuqair.lastpick.model.Resturant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by nazirshuqair on 12/13/14.
 */
public class RandomDeletionActivity extends Activity implements MasterListFragment.MasterClickListener, FeaturedFragment.RestoreClickListener{

    private List<Resturant> resturantsList;
    private List<Resturant> restoredList;

    MasterListFragment listFragment;

    boolean startStop = false;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            if (resturantsList.size() > 2) {
                randomDeletion();
            }else {
                timerHandler.removeCallbacks(timerRunnable);
                startStop = false;
                displayFeatured();
                return;
            }

            timerHandler.postDelayed(this, 2000);
        }
    };

    MenuItem startDeletion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listscreen);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c8198cff")));


        Intent i = getIntent(); // RETRIEVE OUR INTENT
        setTitle(i.getStringExtra("userInput"));
        resturantsList = i.getParcelableArrayListExtra("venues"); // GET PARCELABLE VENUES
        restoredList = new LinkedList<Resturant>();
        for (Resturant resturant: resturantsList){
            restoredList.add(resturant);
        }
        listFragment = null;
        if(savedInstanceState == null) {
            listFragment = MasterListFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.activity_listscreen, listFragment, MasterListFragment.TAG).commit();
        }
        listFragment.updateDisplay(resturantsList, false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Intent intent = getIntent();

        if (intent.getBooleanExtra("edit", true)) {
            startDeletion = menu.add("Pick for Me!");
            startDeletion.setShowAsAction(1);

            startDeletion.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    startDeletion();

                    return false;
                }
            });

        }

        return super.onCreateOptionsMenu(menu);

    }

    public void startDeletion(){

        if (startStop) {
            timerHandler.removeCallbacks(timerRunnable);
            startStop = false;
            startDeletion.setTitle("Pick for Me!");
        } else {
            timerHandler.postDelayed(timerRunnable, 0);
            startStop = true;
            startDeletion.setTitle("Pause");
        }
    }


    public void randomDeletion(){
        Random r = new Random();
        int i1 = r.nextInt(resturantsList.size() - 0) + 0;
        resturantsList.remove(i1);
        listFragment.updateDisplay(resturantsList, true);
    }

    public void displayFeatured(){
        startDeletion.setTitle("");
        String name = resturantsList.get(0).getName();
        String phone = resturantsList.get(0).getFormattedPhone();
        String address = resturantsList.get(0).getFormattedAddress();
        String imgUrl = resturantsList.get(0).getImgUrl();
        FeaturedFragment frag = FeaturedFragment.newInstance(name, phone, address, imgUrl);
        getFragmentManager().beginTransaction().replace(R.id.activity_listscreen, frag, FeaturedFragment.TAG).commit();
    }

    @Override
    public void restore() {
        resturantsList.clear();
        for (Resturant resturant: restoredList){
            resturantsList.add(resturant);
        }
        getFragmentManager().beginTransaction().replace(R.id.activity_listscreen, listFragment, MasterListFragment.TAG).commit();
        listFragment.updateDisplay(resturantsList, true);
        startDeletion.setTitle("Pick for Me!");

    }

    @Override
    protected void onDestroy() {

        timerHandler.removeCallbacks(timerRunnable);

        super.onDestroy();
    }

    @Override
    public void pushItemSelected(int _position) {

        timerHandler.removeCallbacks(timerRunnable);
        startStop = false;
        startDeletion.setTitle("Pick for Me!");

        Intent intent = new Intent(RandomDeletionActivity.this, DetailsActivity.class);
        Resturant resturant = resturantsList.get(_position);

        intent.putExtra("name", resturant.getName());
        intent.putExtra("distance", String.valueOf(Math.round(resturant.getDistance() * 1000) / 1000));
        intent.putExtra("phone", resturant.getFormattedPhone());
        intent.putExtra("address", resturant.getFormattedAddress());
        intent.putExtra("review", resturant.getText());
        intent.putExtra("username", resturant.getFirstName());
        intent.putExtra("rating", String.valueOf(resturant.getRating()));
        intent.putExtra("lat", resturant.getLat());
        intent.putExtra("lng", resturant.getLng());

        startActivity(intent);

    }
}
