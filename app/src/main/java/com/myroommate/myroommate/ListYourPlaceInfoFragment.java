package com.myroommate.myroommate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by G551JK-DM053H on 10-09-2017.
 */

public class ListYourPlaceInfoFragment extends Fragment {

    public static boolean isRedirectedFromLYPInfo;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_list_your_place_info, container, false);

        sharedPreferences = getActivity().getSharedPreferences("logindetails",MODE_PRIVATE);

        Button listNowButton = (Button) RootView.findViewById(R.id.list_now_button);
        listNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if(currentUser!=null) {

                    Fragment fragment = new ListYourPlaceFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();

                }
                else {

                    isRedirectedFromLYPInfo = true;
                    Snackbar snackbar = Snackbar
                            .make(getView(), "Please login to continue.", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    Fragment fragment = new LoginRegisterFragment();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();

                }
            }
        });
        return RootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("List Your Place");
    }

}
