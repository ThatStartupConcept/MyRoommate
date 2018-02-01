package com.myroommate.myroommate;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Object;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.content.ContentValues.TAG;
import static android.util.Base64.NO_CLOSE;
import static android.util.Base64.decode;

public class FindAPlaceFragment extends Fragment {

    private RecyclerView mRecyclerView;
    FAPListingCardAdapter mAdapter;
    ProgressDialog progressDialog;
    private List<Listing> listings;
    RequestQueue requestqueue;
    String HttpURL = "http://merakamraa.com/php/GetListing.php";
    String InfoURL = "http://merakamraa.com/php/GetListingInfo.php";
    private int locationcount = 0;

    private ArrayList<String[]> listOfLists = new ArrayList<String[]>();

    private ArrayList<String[]> listOfLocationArrays = new ArrayList<String[]>();

    private ArrayList<String> locationList = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_find_a_place, container, false);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestqueue = Volley.newRequestQueue(getActivity());
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.fap_recyclerView);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, InfoURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {

                try {
                    JSONObject jsonResponse = new JSONObject(stringResponse);
                    JSONArray jListings = jsonResponse.getJSONArray("listinginfo");

                    int localitynumber = 0;
                    JSONObject listing;
                    JSONObject prevlisting;
                    String prev_location_name;
                    String location_name;
                    String locality_name;

                    for (int i = 0; i < jListings.length(); i++) {

                        listing = jListings.getJSONObject(i);

                        location_name = listing.getString("Location");
                        locality_name = listing.getString("Locality");
                        prev_location_name = location_name;

                        if (i > 0) {
                            prevlisting = jListings.getJSONObject(i - 1);
                            prev_location_name = prevlisting.getString("Location");
                        }


                        if (i == 0) {
                            listOfLists.add(new String[200]);
                            locationList.add(location_name);
                            locationcount++;
                            localitynumber = 0;
                        }
                        if (i > 0) {
                            if (!location_name.equals(prev_location_name)) {
                                listOfLists.add(new String[200]);
                                locationList.add(location_name);
                                String[] temp = Arrays.copyOf(listOfLists.get(locationcount - 1), localitynumber);
                                listOfLocationArrays.add(temp);

                                locationcount++;
                                localitynumber = 0;
                            }
                        }

                        listOfLists.get(locationcount - 1)[localitynumber] = (locality_name);

                        localitynumber++;

                        if (i == (jListings.length() - 1)) {
                            String[] temp = Arrays.copyOf(listOfLists.get(locationcount - 1), localitynumber);
                            listOfLocationArrays.add(temp);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String locationArray[] = locationList.toArray(new String[locationList.size()]);
                final String[] emptyArray = getResources().getStringArray(R.array.empty);

                final Spinner locationSpinner = (MaterialSpinner) getActivity().findViewById(R.id.fap_location);
                ArrayAdapter<String> dataAdapter;
                dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, locationArray);
                dataAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
                locationSpinner.setAdapter(dataAdapter);


                final Spinner localitySpinner = (MaterialSpinner) getActivity().findViewById(R.id.fap_locality);
                final ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, emptyArray);
                emptyAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
                localitySpinner.setAdapter(emptyAdapter);

                locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int positionLocation, long id) {
                        getActivity().findViewById(R.id.fap_recyclerView).setVisibility(View.GONE);


                        String[] tempList;
                        if (positionLocation == -1) {
                            tempList = emptyArray;
                        } else {
                            tempList = listOfLocationArrays.get(positionLocation);
                        }

                        locationcount = 0;


                        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tempList);
                        dataAdapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
                        localitySpinner.setAdapter(dataAdapter2);

                        localitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView2, View selectedItemView2, final int positionLocality, long id2) {

                                if (!locationSpinner.getSelectedItem().toString().equals("Select City") && !localitySpinner.getSelectedItem().toString().equals("Select Locality")) {

                                    final String locality = localitySpinner.getSelectedItem().toString();
                                    listings = new ArrayList<Listing>();

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpURL, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String stringResponse) {

                                            if (stringResponse.equals("No listings available in this locality")) {
                                                Snackbar snackbar = Snackbar
                                                        .make(getView(), stringResponse, Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                            } else {
                                                try {
                                                    JSONObject jsonResponse = new JSONObject(stringResponse);
                                                    JSONArray jListings = jsonResponse.getJSONArray("listings");
                                                    for (int i = 0; i < jListings.length(); i++) {
                                                        JSONObject listing = jListings.getJSONObject(i);
                                                        String primary_key = listing.getString("primary_key");
                                                        String location = listing.getString("location");
                                                        String locality = listing.getString("locality");
                                                        String listingname = new String(decode(listing.getString("listingname"), NO_CLOSE));
                                                        String ownername = listing.getString("ownername");
                                                        String address = new String(decode(listing.getString("address"), NO_CLOSE));
                                                        String sublocality = listing.getString("sublocality");
                                                        String pincode = listing.getString("pincode");
                                                        int rent = listing.getInt("rent");
                                                        listings.add(new Listing(R.mipmap.ic_launcher, primary_key, location, locality, listingname, ownername, address, sublocality, pincode, rent));
                                                    }

                                                    mAdapter.notifyDataSetChanged();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
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
                                            parameters.put("locality", locality);
                                            return parameters;
                                        }

                                    };

                                    requestqueue.add(stringRequest);

                                    initializeAdapter();

                                    // use a linear layout manager
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    mRecyclerView.setLayoutManager(mLayoutManager);

                                    getActivity().findViewById(R.id.fap_recyclerView).setVisibility(View.VISIBLE);

                                    mAdapter.setListener(new FAPListingCardAdapter.ChangeListener() {
                                        @Override
                                        public void onChange() {


                                            Bundle args = new Bundle();
                                            args.putString("primary_key", mAdapter.primary_key);
                                            Fragment fragment = new ListingDetailsFragment();
                                            fragment.setArguments(args);
                                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.content_frame, fragment);
                                            ft.commit();

                                        }
                                    });
                                }
                            }

                            public void onNothingSelected(AdapterView<?> parentView2) {
                            }
                        });
                    }

                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.toString());
            }
        });

        requestqueue.add(stringRequest);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Find A Place");
    }


    private void initializeAdapter() {
        mAdapter = new FAPListingCardAdapter(listings);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void createSpinners() {


    }
}

