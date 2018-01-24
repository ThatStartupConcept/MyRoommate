package com.myroommate.myroommate;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by G551JK-DM053H on 24-01-2018.
 */

public class ListingDetailsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View RootView = inflater.inflate(R.layout.fragment_listing_details, container, false);
        return RootView;
    }
}
