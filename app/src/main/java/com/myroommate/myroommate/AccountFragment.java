package com.myroommate.myroommate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by G551JK-DM053H on 24-09-2017.
 */

public class AccountFragment extends Fragment {

    Button Logout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_account, container, false);

        Logout=(Button)RootView.findViewById(R.id.account_logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                getActivity().getSharedPreferences("logindetails", 0).edit().clear().apply();
                Snackbar snackbar = Snackbar
                        .make(view, "Successfully Logged Out.", Snackbar.LENGTH_LONG);
                snackbar.show();

                Fragment fragment = new HomeScreenFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
            });
        return RootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Account Details");
    }
}
