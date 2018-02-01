package com.myroommate.myroommate;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.util.Base64.NO_CLOSE;
import static android.util.Base64.decode;

/**
 * Created by G551JK-DM053H on 24-01-2018.
 */

public class ListingDetailsFragment extends Fragment {


    String GetFullDetailsURL = "http://merakamraa.com/php/GetFullDetails.php";
    String GetAccountDetailsURL = "http://merakamraa.com/php/AccountDetails.php";
    String RoomDetailsURL = "http://merakamraa.com/php/RoomDetails.php";
    RequestQueue requestQueue;
    private FirebaseAuth mAuth;
    public static final String TITLE = "View Details";
    TextView full_listingName, full_locality, full_location, full_rent, full_ownerName, full_additionalInfo;
    RecyclerView mRecyclerView;
    ListingDetailsRoomAdapter mAdapter;
    int numberOfRooms;
    String primary_key;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View RootView = inflater.inflate(R.layout.fragment_listing_details, container, false);

        full_listingName = (TextView) RootView.findViewById(R.id.full_listingName);
        full_locality = (TextView) RootView.findViewById(R.id.full_locality);
        full_location = (TextView) RootView.findViewById(R.id.full_location);
        full_rent = (TextView) RootView.findViewById(R.id.full_rent);
        full_ownerName = (TextView) RootView.findViewById(R.id.full_ownerName);
        full_additionalInfo = (TextView) RootView.findViewById(R.id.full_additionalInfo);
        mRecyclerView = (RecyclerView) RootView.findViewById(R.id.ld_recyclerView);


        primary_key = this.getArguments().getString("primary_key");

        requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GetFullDetailsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {

                try {

                    JSONObject jsonResponse = new JSONObject(stringResponse);

                    String location = jsonResponse.getString("location");
                    String locality = jsonResponse.getString("locality");
                    String listingname = new String(decode(jsonResponse.getString("listingname"), NO_CLOSE));
                    String ownername = jsonResponse.getString("ownername");
                    String address = new String(decode(jsonResponse.getString("address"), NO_CLOSE));
                    String sublocality = jsonResponse.getString("sublocality");
                    String pincode = jsonResponse.getString("pincode");
                    int rent = jsonResponse.getInt("rent");
                    numberOfRooms = jsonResponse.getInt("numberOfRooms");


                    full_listingName.setText(listingname);
                    full_locality.setText(locality);
                    full_location.setText(location);
                    full_rent.setText(Integer.toString(rent));
                    full_ownerName.setText("Owner: " + ownername);
                    full_additionalInfo.setText("Address: " + address + sublocality + "\nPincode: " + pincode);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, RoomDetailsURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {

                        if (!stringResponse.equals("Listing not found.")) {

                            ArrayList<ArrayList<Integer>> listOfRooms = new ArrayList<ArrayList<Integer>>();

                            try {

                                JSONObject jsonResponse = new JSONObject(stringResponse);
                                JSONArray jListings = jsonResponse.getJSONArray("rooms");


                                for (int i = 0; i < jListings.length(); i++) {

                                    JSONObject listing = jListings.getJSONObject(i);
                                    ArrayList<Integer> roomDetails = new ArrayList<Integer>();                                                      // AUTOMATICALLY GENERATED CODE ENDS
                                    roomDetails.add(Integer.valueOf(listing.getString("roomID")));
                                    roomDetails.add(Integer.valueOf(listing.getString("isACAvailable")));
                                    roomDetails.add(Integer.valueOf(listing.getString("isABAvailable")));
                                    roomDetails.add(Integer.valueOf(listing.getString("numberOfBeds")));

                                    listOfRooms.add(roomDetails);
                                }

                                mAdapter = new ListingDetailsRoomAdapter(primary_key, getActivity(), getContext(), mRecyclerView, listOfRooms);
                                mRecyclerView.setAdapter(mAdapter);

                                // use a linear layout manager
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                mRecyclerView.setVisibility(View.VISIBLE);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {

                        }


                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.toString());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("primary_key", primary_key);
                        return parameters;
                    }

                };

                requestQueue.add(stringRequest2);


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("primary_key", primary_key);
                return parameters;
            }

        };

        requestQueue.add(stringRequest);


        return RootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(TITLE);
    }

    private void initializeAdapter() {

    }
}
