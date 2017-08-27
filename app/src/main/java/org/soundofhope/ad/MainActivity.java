package org.soundofhope.ad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.ads.NativeExpressAdView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // A Native Express ad is placed in every nth position in the RecyclerView.
    public static final int ITEMS_PER_AD = 8;

    // The RecyclerView that holds and displays Native Express ads and menu items.
    private RecyclerView mRecyclerView;

    // List of Native Express ads and MenuItems that populate the RecyclerView.
    private List<Object> mRecyclerViewItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        addMenuItems();
        //addNativeExpressAds();

        // Specify a linear layout manager.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Specify an adapter.
        RecyclerView.Adapter adapter = new RecyclerViewAdapter(this, mRecyclerViewItems);
        mRecyclerView.setAdapter(adapter);

    }

    private void addMenuItems() {

        for (int i = 0; i < 30; ++i) {
            mRecyclerViewItems.add( new MenuItem( i + "" ) );
        }
    }
    /**
     * Adds Native Express ads to the items list.
     */
    private void addNativeExpressAds() {

        // Loop through the items array and place a new Native Express ad in every ith position in
        // the items List.
        for (int i = 0; i <= mRecyclerViewItems.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(MainActivity.this);
            mRecyclerViewItems.add(i, adView);
        }
    }

}
