package org.soundofhope.ad;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ok on 3/9/17.
 */

public class NativeAdManager {

    // A menu item view type.
    public static final int MENU_ITEM_VIEW_TYPE = 0;

    // The Native  ad view type.
    public static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;  // admob;

    public static final int NATIVE_CONTENT_AD_VIEW_TYPE = 2;

    public static final int NATIVE_CUSTOM_TEMPLATE_AD_VIEW_TYPE = 3;


    public static final String CONTENT_HEADLINE = "headLine";
    public static final String NATIVE_TEMPLATE_HEADLINE = "headLine";
    public static final String NATIVE_TEMPLATE_CAPTION = "caption";


    public static final List<AdItem> adItemList = new ArrayList<>();

    //static List<NativeExpressAdView> nativeExpressAdViewList = new ArrayList<>();

    private String DFP_AD_UNIT_ID = "";

    private String SIMPLE_TEMPLATE_ID = "";

    private static int adWidth;

    MainActivity.AdSohListener adSohListener = null;

    static int m = 30;
    static int k = 10;
    static int d = 3;

    // An Activity's Context.
    private final Context mContext;

    public NativeAdManager(Context mContext, String name, boolean nctaEnabled
            , boolean refreshEnabled, Enum backfillType
            , String adUnitDFP, String simpleTemplateId, String adUnitAdMod
            , int adWidth
            , MainActivity.AdSohListener adSohListener ) {

        adItemList.clear();

        this.mContext = mContext;
        this.SIMPLE_TEMPLATE_ID = simpleTemplateId;
        this.DFP_AD_UNIT_ID = adUnitDFP;

        this.adSohListener = adSohListener;

        NativeAdManager.adWidth = adWidth;

    }

    public void appendAdList() {

        appendAdListViaType( AdType.NativeTemplate );

        for( int i=0; i< 5; i++ ) {
            loadNativeExpressAd();
        }
    }

    public void appendAdListViaType( AdType adType ) {

        switch (adType) {
            case NativeTemplate:
                new RefreshNativeTemplate().addNew( m );
                break;
            case AppInstall:
                break;
            case Content:
                new RefreshContent().addNew( k );
                break;
            case AdMob:
                break;
        }

    }

    public void refreshRenderdAd() {

        for( int i=0; i<adItemList.size(); i++ ) {

            AdItem adItem =  adItemList.get( i );

            if( adItem.isRenderd ) {

                System.out.println("refreshRenderdAd= " + i );

                switch (adItem.adType) {
                    case NativeTemplate:
                        new RefreshNativeTemplate().refreshRendered( i );
                        break;
                    case AppInstall:
                        break;
                    case Content:
                        new RefreshContent().refreshRendered( i );
                        break;
                    case AdMob:
                        break;
                }
                //一次仅一个，完成后再刷新第二个。
                break;
            }
        }
    }

    private void initNativeTemplate(){

        AdLoader.Builder builder = new AdLoader.Builder(mContext, DFP_AD_UNIT_ID);

        builder.forCustomTemplateAd(SIMPLE_TEMPLATE_ID,
                new NativeCustomTemplateAd.OnCustomTemplateAdLoadedListener() {
                    @Override
                    public void onCustomTemplateAdLoaded(NativeCustomTemplateAd ad) {

                        Map<String, Object> nativeTemplateMap = new HashMap<String, Object>();

                        nativeTemplateMap.put( NATIVE_TEMPLATE_HEADLINE, ad.getText("Headline") );
                        nativeTemplateMap.put( NATIVE_TEMPLATE_CAPTION, ad.getText("Caption") );

                    }
                },
                new NativeCustomTemplateAd.OnCustomClickListener() {
                    @Override
                    public void onCustomClick(NativeCustomTemplateAd ad, String s) {
                        Toast.makeText(mContext,
                                "A custom click has occurred in the simple template",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        AdLoader adLoader = builder.build();

        adLoader.loadAd(new PublisherAdRequest.Builder().build());
    }

    public static AdItem getAd( int idx, boolean render ) {

        if( adItemList != null && idx < adItemList.size() ) {
            AdItem adItem = adItemList.get( idx );
            if( render ) {
                adItem.isRenderd = true;
            }
            return adItem;
        }
        return null;
    }

    public static AdItem getAd( AdType adType, boolean render ) {

        for( AdItem adItem: adItemList ) {

            if( adItem.isRenderd ) {
                continue;
            }
            if( adItem.adType == adType ) {
                if (render) {
                    adItem.isRenderd = true;
                }
                return adItem;
            }
        }
        return null;
    }


    private void loadNativeExpressAd( ) {



        final NativeExpressAdView adView = new NativeExpressAdView( mContext );
       /* final float scale = MainActivity.this.getResources().getDisplayMetrics().density;
        // Set the ad size and ad unit ID for each Native Express ad in the items list.

        final NativeExpressAdView adView =
                (NativeExpressAdView) mRecyclerViewItems.get( 3 );
        final CardView cardView = (CardView) findViewById(R.id.ad_card_view);
        final int adWidth = cardView.getWidth() - cardView.getPaddingLeft()
                - cardView.getPaddingRight();*/
        int adWidth = 736;

        final float scale = mContext.getResources().getDisplayMetrics().density;

        AdSize adSize = new AdSize((int) (adWidth / scale), MainActivity.NATIVE_EXPRESS_AD_HEIGHT);
        adView.setAdSize(adSize);
        adView.setAdUnitId( MainActivity.AD_UNIT_ID);

        // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous Native Express ad loaded successfully, call this method again to
                // load the next ad in the items list.
                //loadNativeExpressAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                //loadNativeExpressAd(index + ITEMS_PER_AD);
            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest.Builder().build());

        //nativeExpressAdViewList.add( adView );
        Map<String, Object> adMobMap = new HashMap<String, Object>();

        adMobMap.put( "view", adView );

        AdItem adItem = new AdItem();
        adItem.adType = AdType.AdMob;
        adItem.adAttributesMap.putAll( adMobMap );

        adItemList.add( adItem );

    }
    class RefreshContent {

        boolean isAddNew = true;
        public int refreshAdNum = 10;
        int idx = 0;

        RefreshContent( ) {
        }

        void addNew( int refreshAdNum ) {
            isAddNew = true;
            this.refreshAdNum = refreshAdNum;
            this.loadContent();
        }

        void refreshRendered( int idx ) {
            isAddNew = false;
            this.idx = idx;
            loadContent();
        }

        private void loadContent() {

            AdLoader.Builder builder = new AdLoader.Builder( mContext, DFP_AD_UNIT_ID);

            builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                @Override
                public void onContentAdLoaded(NativeContentAd ad) {

                    Map<String, Object> contentMap = new HashMap<String, Object>();

                    contentMap.put( CONTENT_HEADLINE, ad.getHeadline() );

                    AdItem adItem = new AdItem();
                    adItem.adType = AdType.Content;
                    adItem.adAttributesMap.putAll( contentMap );

                    if( isAddNew ) {

                        refreshAdNum--;
                        if( ! addToAdList(adItem) ) {
                            refreshAdNum = 0;
                        }
                        if( refreshAdNum > 0 ) {
                            loadContent();
                        } else {
                            adSohListener.callback();
                        }
                    } else {
                        adItemList.set(idx, adItem);
                        refreshRenderdAd();
                    }
                    System.out.println( "contentMap" + contentMap + " adItemList: " + adItemList.size() );
                }
            });

            AdLoader adLoader = builder.build();

            adLoader.loadAd(new PublisherAdRequest.Builder().build());
        }
    }


    class RefreshNativeTemplate {

        boolean isAddNew = true;
        public int refreshAdNum = 10;
        int idx;

        RefreshNativeTemplate( ) {
        }

        void addNew( int refreshAdNum ) {
            isAddNew = true;
            this.refreshAdNum = refreshAdNum;
            this.loadContent();
        }

        void refreshRendered( int idx ) {

            isAddNew = false;
            this.idx = idx;
            loadContent();
        }

        private void loadContent() {

            AdLoader.Builder builder = new AdLoader.Builder(mContext, DFP_AD_UNIT_ID);

            builder.forCustomTemplateAd(SIMPLE_TEMPLATE_ID,
                    new NativeCustomTemplateAd.OnCustomTemplateAdLoadedListener() {
                        @Override
                        public void onCustomTemplateAdLoaded(NativeCustomTemplateAd ad) {

                            Map<String, Object> nativeTemplateMap = new HashMap<String, Object>();

                            nativeTemplateMap.put( NATIVE_TEMPLATE_HEADLINE, ad.getText("Headline") );
                            nativeTemplateMap.put( NATIVE_TEMPLATE_CAPTION, ad.getText("Caption") );

                            AdItem adItem = new AdItem();
                            adItem.adType = AdType.NativeTemplate;
                            adItem.adAttributesMap.putAll( nativeTemplateMap );

                            if( isAddNew ) {
                                refreshAdNum--;
                                if( ! addToAdList(adItem)) {
                                    // add failed;
                                    refreshAdNum = 0;
                                }

                                if( refreshAdNum > 0 ) {
                                    loadContent();
                                } else {
                                    // refresh RecyclerViewlist;
                                    adSohListener.callback();
                                    //
                                    appendAdListViaType(AdType.Content);
                                }
                            } else {
                                adItemList.set(idx, adItem);
                                refreshRenderdAd();
                            }

                        }
                    },
                    new NativeCustomTemplateAd.OnCustomClickListener() {
                        @Override
                        public void onCustomClick(NativeCustomTemplateAd ad, String s) {
                            Toast.makeText(mContext,
                                    "A custom click has occurred in the simple template",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            AdLoader adLoader = builder.build();

            adLoader.loadAd(new PublisherAdRequest.Builder().build());
        }
    }



    private static boolean addToAdList( AdItem adItem ) {
        if( isSameDtimes( adItem ) ) {
            return false;
        }

        if( isRepeatKtimes( adItem )) {
            return false;
        }

        adItemList.add( adItem );
        return true;
    }

    private static boolean isRepeatKtimes( AdItem adItem ) {
        int repeatTimes = 0;
        for (AdItem adItemTemp: adItemList ) {

            if( adItemTemp.isRenderd ) {
                continue;
            }
            if( adItem.equals( adItemTemp)) {
                repeatTimes++;
            }
            if( repeatTimes >= k ) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSameDtimes( AdItem adItem ) {

        if (adItemList.size() < d) {
            return false;
        }
        int sameTimes = 0;
        for( int i = adItemList.size()-1; i>=0; i-- ) {
            AdItem adItemTemp = adItemList.get( i );
            if( adItemTemp.equals( adItem ) ) {
                sameTimes ++;
            } else {
                return false;
            }
            if(sameTimes >= d ) {
                return true;
            }
        }
        return false;
    }

}
