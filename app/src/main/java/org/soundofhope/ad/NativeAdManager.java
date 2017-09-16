package org.soundofhope.ad;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdLoader;
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
    public static final int NATIVE_EXPRESS_AD_VIEW_TYPE = 1;

    public static final int NATIVE_CONTENT_AD_VIEW_TYPE = 2;

    public static final int NATIVE_CUSTOM_TEMPLATE_AD_VIEW_TYPE = 3;


    public static final String CONTENT_HEADLINE = "headLine";
    public static final String NATIVE_TEMPLATE_HEADLINE = "headLine";
    public static final String NATIVE_TEMPLATE_CAPTION = "caption";


    public static final List<AdItem> adItemList = new ArrayList<>();

    private String DFP_AD_UNIT_ID = "";

    private String SIMPLE_TEMPLATE_ID = "";

    MainActivity.AdSohListener adSohListener = null;

    static int m = 30;
    static int k = 10;
    static int d = 3;

    // An Activity's Context.
    private final Context mContext;

    public NativeAdManager(Context mContext, String name, boolean nctaEnabled
            , boolean refreshEnabled, Enum backfillType
            , String adUnitDFP, String simpleTemplateId, String adUnitAdMod
            , MainActivity.AdSohListener adSohListener ) {

        adItemList.clear();

        this.mContext = mContext;
        this.SIMPLE_TEMPLATE_ID = simpleTemplateId;
        this.DFP_AD_UNIT_ID = adUnitDFP;

        this.adSohListener = adSohListener;
    }

    public void appendAdList() {

        appendAdListViaType( AdType.NativeTemplate );

    }

    public void appendAdListViaType( AdType adType ) {

        switch (adType) {
            case NativeTemplate:
                new NativeAdManager.RefreshNativeTemplate().addNew( m );
                break;
            case AppInstall:
                break;
            case Content:
                new NativeAdManager.RefreshContent().addNew( k );
                break;
            case AdMob:
                break;
        }

    }

    public void refreshAd() {

        new NativeAdManager.RefreshContent().refreshRendered();
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

    class RefreshContent {

        public int refreshAdNum = 10;
        boolean isAddNew = true;

        RefreshContent( ) {
        }

        void addNew( int refreshAdNum ) {
            this.refreshAdNum = refreshAdNum;
            isAddNew = true;
            this.loadContent();
        }

        void refreshRendered() {

            refreshAdNum = 0;

            Iterator ite = adItemList.iterator();
            if(ite.hasNext()) {
                AdItem adItem = new AdItem();
                if( (AdType.Content == adItem.adType) && adItem.isRenderd ) {
                    ite.remove();
                    refreshAdNum ++;
                }
            }
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
                        if( ! addToAdList(adItem) ) {
                            refreshAdNum = 0;
                        }
                    }

                    refreshAdNum--;

                    if( refreshAdNum > 0 ) {
                        loadContent();
                    } else {
                        adSohListener.callback();
                    }

                    System.out.println( "contentMap" + contentMap + " adItemList: " + adItemList.size() );
                }
            });

            AdLoader adLoader = builder.build();

            adLoader.loadAd(new PublisherAdRequest.Builder().build());
        }
    }


    class RefreshNativeTemplate {

        public int refreshAdNum = 10;
        boolean isAddNew = true;

        RefreshNativeTemplate( ) {
        }

        void addNew( int refreshAdNum ) {
            this.refreshAdNum = refreshAdNum;
            isAddNew = true;
            this.loadContent();
        }

        void refreshRendered() {

            refreshAdNum = 0;

            Iterator ite = adItemList.iterator();
            if(ite.hasNext()) {
                AdItem adItem = new AdItem();
                if( (AdType.Content == adItem.adType) && adItem.isRenderd ) {
                    ite.remove();
                    refreshAdNum ++;
                }
            }
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

                            refreshAdNum--;
                            if( isAddNew ) {
                                if( ! addToAdList(adItem)) {
                                    // add failed;
                                    refreshAdNum = 0;
                                }
                            }

                            if( refreshAdNum > 0 ) {
                                loadContent();
                            } else {
                                // refresh RecyclerViewlist;
                                adSohListener.callback();
                                //
                                appendAdListViaType(AdType.Content);
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
