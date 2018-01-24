package com.myroommate.myroommate;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by G551JK-DM053H on 01-09-2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ListingViewHolder> {

    List<Listing> listings;

    RVAdapter(List<Listing> listings){
        this.listings=listings;
    }

    public static class ListingViewHolder extends RecyclerView.ViewHolder {
        CardView card_view;
        ImageView listing_listingPhoto;
        TextView listing_listingName;
        TextView listing_ownerName;
        TextView listing_subLocality;

        ListingViewHolder(View itemView) {
            super(itemView);
            card_view = (CardView)itemView.findViewById(R.id.card_view);
            listing_listingPhoto = (ImageView)itemView.findViewById(R.id.listing_photo);
            listing_listingName = (TextView)itemView.findViewById(R.id.listing_name);
            listing_ownerName = (TextView)itemView.findViewById(R.id.owner_name);
            listing_subLocality = (TextView)itemView.findViewById(R.id.sub_locality);
        }
    }

    @Override
    public ListingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listing_card_layout, viewGroup, false);
        ListingViewHolder lvh = new ListingViewHolder(v);
        return lvh;
    }

    @Override
    public void onBindViewHolder(final ListingViewHolder personViewHolder, int i) {
        personViewHolder.listing_listingPhoto.setImageResource(listings.get(i).listingPhoto);
        personViewHolder.listing_listingName.setText(listings.get(i).listingName);
        personViewHolder.listing_ownerName.setText(listings.get(i).ownerName);
        personViewHolder.listing_subLocality.setText(listings.get(i).subLocality);

        personViewHolder.card_view.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(final View v){ // Makes cards generated in ListYourPlaceFragment clickable

                Snackbar snackbar = Snackbar
                        .make(personViewHolder.itemView, personViewHolder.listing_listingName.getText().toString(), Snackbar.LENGTH_LONG);
                snackbar.show();

                // TODO - Link to a new Fragment which will show contents of card

            }

        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

}
