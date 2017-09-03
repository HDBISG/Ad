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

    // An Activity's Context.
    private final Context mContext;

    // The list of Native Express ads and menu items.
    private final List<Object> mRecyclerViewItems;


    /**
     * For this example app, the recyclerViewItems list contains only
     * {@link MenuItem} and {@link NativeExpressAdView} types.
     */
    public RecyclerViewAdapter(Context context, List<Object> recyclerViewItems) {
        this.mContext = context;
        this.mRecyclerViewItems = recyclerViewItems;

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
    public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder {

        NativeExpressAdViewHolder(View view) {
            super(view);
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

        if( position == 3 ) {
            return InitAd.NATIVE_EXPRESS_AD_VIEW_TYPE;
        }
        if( position == 6 ) {
            return InitAd.NATIVE_CONTENT_AD_VIEW_TYPE;
        }
        if( position == 9 ) {
            return InitAd.NATIVE_CUSTOM_TEMPLATE_AD_VIEW_TYPE;
        }
        return InitAd.MENU_ITEM_VIEW_TYPE;
    }

    /**
     * Creates a new view for a menu item view or a Native Express ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View menuItemLayoutView = null;

        switch (viewType) {
            case InitAd.MENU_ITEM_VIEW_TYPE:
                menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.menu_item_container, viewGroup, false);
                return new MenuItemViewHolder(menuItemLayoutView);

            case InitAd.NATIVE_EXPRESS_AD_VIEW_TYPE:
                View nativeExpressLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.native_express_ad_container,
                        viewGroup, false);
                return new NativeExpressAdViewHolder(nativeExpressLayoutView);

            case InitAd.NATIVE_CONTENT_AD_VIEW_TYPE:

                NativeContentAdView nativeContentLayoutView = (NativeContentAdView)LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.ad_content, viewGroup, false);
                return  new NativeContentAdViewHolder( nativeContentLayoutView );

            case InitAd.NATIVE_CUSTOM_TEMPLATE_AD_VIEW_TYPE:
                View adView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.ad_simple_custom_template, viewGroup, false);

                return  new NativeCustomTemplateHolder( adView );
            default:
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
            case InitAd.MENU_ITEM_VIEW_TYPE:
                MenuItemViewHolder menuItemHolder = (MenuItemViewHolder) holder;
                MenuItem menuItem = (MenuItem) mRecyclerViewItems.get(position);

                menuItemHolder.menuItemSeq.setText(menuItem.getSeq());
                break;

            case InitAd.NATIVE_EXPRESS_AD_VIEW_TYPE:
                NativeExpressAdViewHolder nativeExpressHolder =
                        (NativeExpressAdViewHolder) holder;
                NativeExpressAdView adView =
                        (NativeExpressAdView) mRecyclerViewItems.get(position);
                ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
                // The NativeExpressAdViewHolder recycled by the RecyclerView may be a different
                // instance than the one used previously for this position. Clear the
                // NativeExpressAdViewHolder of any subviews in case it has a different
                // AdView associated with it, and make sure the AdView for this position doesn't
                // already have a parent of a different recycled NativeExpressAdViewHolder.
                if (adCardView.getChildCount() > 0) {
                    adCardView.removeAllViews();
                }
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }

                // Add the Native Express ad to the native express ad view.
                adCardView.addView(adView);
                break;

            case InitAd.NATIVE_CONTENT_AD_VIEW_TYPE:

                NativeContentAdViewHolder nativeContentAdViewHolder = (NativeContentAdViewHolder) holder;

                nativeContentAdViewHolder.HeadlineView.setText( InitAd.getContent(InitAd.CONTENT_HEADLINE).toString() );
                //MenuItem menuItem = (MenuItem) mRecyclerViewItems.get(position);
                // fall through\
                //nativeContentAdViewHolder.menuHeadlineView.setText( "abc" );
                break;

            case InitAd.NATIVE_CUSTOM_TEMPLATE_AD_VIEW_TYPE:

                NativeCustomTemplateHolder nativeCustomTemplateHolder = (NativeCustomTemplateHolder) holder;
                nativeCustomTemplateHolder.headline.setText( InitAd.getNativeTemplate( InitAd.NATIVE_TEMPLATE_HEADLINE).toString() );
                nativeCustomTemplateHolder.caption.setText( InitAd.getNativeTemplate( InitAd.NATIVE_TEMPLATE_CAPTION).toString() );
                break;

            default:

                MenuItemViewHolder menuItemHolder1 = (MenuItemViewHolder) holder;
                MenuItem menuItem1 = (MenuItem) mRecyclerViewItems.get(position);

                menuItemHolder1.menuItemSeq.setText(menuItem1.getSeq());
                break;
        }
    }

}
