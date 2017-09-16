package org.soundofhope.ad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // A Native Express ad is placed in every nth position in the RecyclerView.
    public static final int ITEMS_PER_AD = 8;

    // The RecyclerView that holds and displays Native Express ads and menu items.
    private RecyclerView mRecyclerView;

    private Button addMoreBtn;

    private Button refreshRenderdMore;

    // The Native Express ad height.
    public static final int NATIVE_EXPRESS_AD_HEIGHT = 150;

    // The Native Express ad unit ID.
    public static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/1072772517";

    public static final String DFP_AD_UNIT_ID = "/6499/example/native";

    public static final String SIMPLE_TEMPLATE_ID = "10104090";

    // List of Native Express ads and MenuItems that populate the RecyclerView.
    public final List<Object> mRecyclerViewItems = new ArrayList<>();


    NativeAdManager nativeAdManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        addMoreBtn = (Button)findViewById( R.id.addMoreBtn);
        refreshRenderdMore = (Button)findViewById( R.id.refreshRenderdMore);

        final RecyclerView.Adapter adapter = new RecyclerViewAdapter(this, mRecyclerViewItems);

        addMenuItems( 20 );

        // Admob
        //addNativeExpressAds();
        //setUpAndLoadNativeExpressAds();

        // DFP
        nativeAdManager = new NativeAdManager( this,"name",true, true
                , null, DFP_AD_UNIT_ID, SIMPLE_TEMPLATE_ID, AD_UNIT_ID,
                new MainActivity.AdSohListener(){
                    @Override
                    public void callback( ){
                        //adapter.notifyDataSetChanged();
                    }
                } );
        nativeAdManager.appendAdList();

        // Specify a linear layout manager.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Specify an adapter.
        mRecyclerView.setAdapter(adapter);

        addMoreBtn.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View v) {
                 //Toast.makeText(MainActivity.this, "addMoreBtn", Toast.LENGTH_SHORT).show();
                 addMenuItems( 10 );
                 adapter.notifyDataSetChanged();
             }

        });

        refreshRenderdMore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "refreshRenderdMore", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void addMenuItems( int count ) {

        for (int i = 0; i < count ; ++i) {
            mRecyclerViewItems.add( new MenuItem( mRecyclerViewItems.size() + "" ) );
        }
    }
    /**
     * Adds Native Express ads to the items list.
     */
    private void addNativeExpressAds() {

        final NativeExpressAdView adView = new NativeExpressAdView(MainActivity.this);
        mRecyclerViewItems.add( 3, adView);

    }

    /**
     * Sets up and loads the Native Express ads.
     */
    private void setUpAndLoadNativeExpressAds() {
        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                final float scale = MainActivity.this.getResources().getDisplayMetrics().density;
                // Set the ad size and ad unit ID for each Native Express ad in the items list.

                    final NativeExpressAdView adView =
                            (NativeExpressAdView) mRecyclerViewItems.get( 3 );
                    final CardView cardView = (CardView) findViewById(R.id.ad_card_view);
                    final int adWidth = cardView.getWidth() - cardView.getPaddingLeft()
                            - cardView.getPaddingRight();
                    System.out.println("adWidth=" + adWidth );

                    AdSize adSize = new AdSize((int) (adWidth / scale), NATIVE_EXPRESS_AD_HEIGHT);
                    adView.setAdSize(adSize);
                    adView.setAdUnitId(AD_UNIT_ID);

                // Load the 3 Native Express ad in the items list.
                loadNativeExpressAd( 3 );
            }
        });
    }

    private void loadNativeExpressAd(final int index) {

        if (index >= mRecyclerViewItems.size()) {
            return;
        }

        Object item = mRecyclerViewItems.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            //throw new ClassCastException("Expected item at index " + index + " to be a Native" + " Express ad.");
            return;
        }
        final NativeExpressAdView adView = new NativeExpressAdView(MainActivity.this);

        // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous Native Express ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest.Builder().build());

    }

    public interface AdSohListener {

        public void callback( );
    }
}
