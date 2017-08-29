package org.soundofhope.ad;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd;

import java.util.List;
import java.util.Locale;

/**
 * The {@link RecyclerViewAdapter} class.
 * <p>The adapter provides access to the items in the {@link MenuItemViewHolder}
 * or the {@link NativeContentAdViewHolder}.</p>
 */
class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    // The Native  ad view type.
    private static final int NATIVE_CONTENT_AD_VIEW_TYPE = 1;

    private static final int NATIVE_CUSTOM_TEMPLATE_AD_VIEW_TYPE = 2;

    private static final String DFP_AD_UNIT_ID = "/6499/example/native";

    private static final String SIMPLE_TEMPLATE_ID = "10104090";
    // An Activity's Context.
    private final Context mContext;

    // The list of Native Express ads and menu items.
    private final List<Object> mRecyclerViewItems;

    final static StringBuffer headLine = new StringBuffer();
    final static StringBuffer templateHeadLine = new StringBuffer();
    final static StringBuffer templateCaption = new StringBuffer();

    /**
     * For this example app, the recyclerViewItems list contains only
     * {@link MenuItem} and {@link NativeExpressAdView} types.
     */
    public RecyclerViewAdapter(Context context, List<Object> recyclerViewItems) {
        this.mContext = context;
        this.mRecyclerViewItems = recyclerViewItems;

        this.initContent();
        this.initNativeTemplate();
    }

    private void initContent() {

        AdLoader.Builder builder = new AdLoader.Builder( mContext, DFP_AD_UNIT_ID);

        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd ad) {

                NativeContentAdView adView = (NativeContentAdView) ((Activity)mContext).getLayoutInflater()
                        .inflate(R.layout.ad_content, null);
                //populateContentAdView(ad, adView);
                System.out.println( "11111111111" + ad.getHeadline() );

                headLine.append( ad.getHeadline() );
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
                        templateHeadLine.append( ad.getText("Headline") );
                        templateCaption.append( ad.getText("Caption") );
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

    /**
     * The {@link MenuItemViewHolder} class.
     * Provides a reference to each view in the menu item view.
     */
    public class MenuItemViewHolder extends RecyclerView.ViewHolder {

        private TextView menuItemSeq;

        MenuItemViewHolder(View view) {
            super(view);
            menuItemSeq = (TextView) view.findViewById(R.id.menu_item_seq);
        }
    }


    public class NativeContentAdViewHolder extends RecyclerView.ViewHolder {

        private TextView HeadlineView;
        private TextView BodyView;
        private TextView CallToActionView;
        private TextView AdvertiserView;


        NativeContentAdViewHolder(View view) {
            super(view);

            HeadlineView = (TextView) view.findViewById(R.id.contentad_headline);
            BodyView = (TextView) view.findViewById(R.id.contentad_body);
            CallToActionView = (TextView) view.findViewById(R.id.contentad_call_to_action);
            AdvertiserView = (TextView) view.findViewById(R.id.contentad_advertiser);
        }
    }

    public class NativeCustomTemplateHolder extends RecyclerView.ViewHolder {

        private TextView headline ;
        private TextView caption ;

        NativeCustomTemplateHolder(View view) {
            super(view);

            headline = (TextView) view.findViewById(R.id.simplecustom_headline);
            caption = (TextView) view.findViewById(R.id.simplecustom_caption);
        }
        //headline.setText(nativeCustomTemplateAd.getText("Headline"));
        //caption.setText(nativeCustomTemplateAd.getText("Caption"));
    }



    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }

    /**
     * Determines the view type for the given position.
     */
    @Override
    public int getItemViewType(int position) {
        /*return (position % MainActivity.ITEMS_PER_AD == 0) ? NATIVE_EXPRESS_AD_VIEW_TYPE
                : MENU_ITEM_VIEW_TYPE;*/
        if( position == 3 ) {
            return NATIVE_CONTENT_AD_VIEW_TYPE;
        }
        if( position == 6 ) {
            return NATIVE_CUSTOM_TEMPLATE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }

    /**
     * Creates a new view for a menu item view or a Native Express ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View menuItemLayoutView = null;

        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.menu_item_container, viewGroup, false);
                return new MenuItemViewHolder(menuItemLayoutView);
            case NATIVE_CONTENT_AD_VIEW_TYPE:

                NativeContentAdView nativeContentLayoutView = (NativeContentAdView)LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.ad_content, viewGroup, false);
                return  new NativeContentAdViewHolder( nativeContentLayoutView );
                // fall through
            case NATIVE_CUSTOM_TEMPLATE_AD_VIEW_TYPE:
                View adView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.ad_simple_custom_template, viewGroup, false);
                return  new NativeCustomTemplateHolder( adView );
            default:
               /* View nativeExpressLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.native_express_ad_container,
                        viewGroup, false);
                return new NativeExpressAdViewHolder(nativeExpressLayoutView);*/
                menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.menu_item_container, viewGroup, false);
                return new MenuItemViewHolder(menuItemLayoutView);
        }

    }


    /**
     * Populates a {@link NativeContentAdView} object with data from a given
     * {@link NativeContentAd}.
     *
     * @param nativeContentAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateContentAdView(NativeContentAd nativeContentAd,
                                       NativeContentAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setLogoView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));
        // The MediaView will display a video asset if one is present in the ad, and the first image
        // asset otherwise.
        adView.setMediaView((MediaView) adView.findViewById(R.id.contentad_media));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        // These assets aren't guaranteed to be in every NativeContentAd, so it's important to
        // check before trying to display them.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);

        System.out.println( nativeContentAd.getHeadline() );
        System.out.println( nativeContentAd.getBody() );
        System.out.println( nativeContentAd.getCallToAction() );
        System.out.println( nativeContentAd.getAdvertiser() );
    }

    /**
     *  Replaces the content in the views that make up the menu item view and the
     *  Native Express ad view. This method is invoked by the layout manager.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {
            case MENU_ITEM_VIEW_TYPE:
                MenuItemViewHolder menuItemHolder = (MenuItemViewHolder) holder;
                MenuItem menuItem = (MenuItem) mRecyclerViewItems.get(position);

                menuItemHolder.menuItemSeq.setText(menuItem.getSeq());
                break;

            case NATIVE_CONTENT_AD_VIEW_TYPE:

                NativeContentAdViewHolder nativeContentAdViewHolder = (NativeContentAdViewHolder) holder;

                System.out.println( "2222222" + headLine.toString() );
                nativeContentAdViewHolder.HeadlineView.setText( headLine.toString() );
                //MenuItem menuItem = (MenuItem) mRecyclerViewItems.get(position);
                // fall through\
                //nativeContentAdViewHolder.menuHeadlineView.setText( "abc" );
                break;
            case NATIVE_CUSTOM_TEMPLATE_AD_VIEW_TYPE:

                NativeCustomTemplateHolder nativeCustomTemplateHolder = (NativeCustomTemplateHolder) holder;
                nativeCustomTemplateHolder.headline.setText( templateHeadLine.toString() );
                nativeCustomTemplateHolder.caption.setText( templateCaption.toString() );
                break;
            default:

                MenuItemViewHolder menuItemHolder1 = (MenuItemViewHolder) holder;
                MenuItem menuItem1 = (MenuItem) mRecyclerViewItems.get(position);

                menuItemHolder1.menuItemSeq.setText(menuItem1.getSeq());
                break;
        }
    }

}
