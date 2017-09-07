package org.soundofhope.ad;

import android.app.Activity;
import android.content.Context;
import android.text.StaticLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static final String DFP_AD_UNIT_ID = "/6499/example/native";

    public static final String SIMPLE_TEMPLATE_ID = "10104090";

    //public static final StringBuffer headLine = new StringBuffer();
    //public static final StringBuffer templateHeadLine = new StringBuffer();
    //public static final StringBuffer templateCaption = new StringBuffer();
    public static final String CONTENT_HEADLINE = "headLine";
    public static final String NATIVE_TEMPLATE_HEADLINE = "headLine";
    public static final String NATIVE_TEMPLATE_CAPTION = "caption";

    private static int contentIdx = 0;
    public static final List<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>();

    private static int nativeTemplateIdx = 0;
    public static final List<Map<String, Object>> nativeTemplateList = new ArrayList<Map<String, Object>>();

    // An Activity's Context.
    private final Context mContext;

    public NativeAdManager(Context mContext ) {
        this.mContext = mContext;
    }

    public void initData() {

        //contentList.clear();

        for (int i = 0; i < 4; i++) {
            this.initContent();
            this.initNativeTemplate();
        }
    }

    private void initContent() {

        AdLoader.Builder builder = new AdLoader.Builder( mContext, DFP_AD_UNIT_ID);

        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd ad) {

                NativeContentAdView adView = (NativeContentAdView) ((Activity)mContext).getLayoutInflater()
                        .inflate(R.layout.ad_content, null);

                Map<String, Object> contentMap = new HashMap<String, Object>();
                contentMap.put( CONTENT_HEADLINE, ad.getHeadline() );
                addToContent( contentMap );

                System.out.println( "contentMap" + contentMap + " contentList: " + contentList.size() +
                            "  " + contentList );
            }
        });

        AdLoader adLoader = builder.build();

        adLoader.loadAd(new PublisherAdRequest.Builder().build());
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

                        addToNativeTemplate( nativeTemplateMap );
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

    public static void addToContent( Map<String, Object> contentMap ) {

        if( ! contentList.contains( contentMap )) {
            contentList.add( contentMap );
            return;

        }
    }
    public static void addToNativeTemplate( Map<String, Object> nativeTemplateMap ) {

        if( ! nativeTemplateList.contains( nativeTemplateMap )) {
            nativeTemplateList.add( nativeTemplateMap );
            return;
        }
    }

    public static Map<String, Object> getContent(  ) {

        System.out.println( "contentIdx=" + contentIdx + " contentList.size()=" + contentList.size() );

        if( contentList.size() == 0 ) {
            return null;
        }
        if( contentList.size() >= contentIdx ) {
            contentIdx = 0;
        }
        Map<String, Object> content = contentList.get( contentIdx );
        System.out.println( "contentIdx=" + contentIdx + " " + content );
        contentIdx++;

        return content ;
    }

    public static Map<String, Object> getNativeTemplate(  ) {

        System.out.println( "nativeTemplateIdx=" + nativeTemplateIdx
                + " nativeTemplateList.size()=" + nativeTemplateList.size() );

        if( nativeTemplateList.size() == 0 ) {
            return null;
        }
        if( nativeTemplateList.size() >= nativeTemplateIdx ) {
            nativeTemplateIdx = 0;
        }
        Map<String, Object> nativeTemplateMap = nativeTemplateList.get( nativeTemplateIdx );
        System.out.println( "nativeTemplateIdx=" + nativeTemplateIdx + " " + nativeTemplateMap );
        contentIdx++;

        return nativeTemplateMap ;
    }

}
