package com.myroommate.myroommate;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fr.ganfra.materialspinner.MaterialSpinner;

public class FindAPlaceFragment extends Fragment {

    private RecyclerView mRecyclerView;
    RVAdapter mAdapter;
    ProgressDialog progressDialog;
    private List<Listing> listings;
    RequestQueue requestqueue;
    String HttpURL = "https://myroommate.000webhostapp.com/GetListing.php";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_find_a_place, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestqueue = Volley.newRequestQueue(getActivity());
        mRecyclerView = getActivity().findViewById(R.id.recyclerView);

        final String[] locationArray =getResources().getStringArray(R.array.locationnames);
        final Spinner locationSpinner = (MaterialSpinner)getActivity().findViewById(R.id.fap_location);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, locationArray);
        dataAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
        locationSpinner.setAdapter(dataAdapter);

        final String[] emptyArray = getResources().getStringArray(R.array.empty);
        final String[] mumbaiArray = getResources().getStringArray(R.array.mumbainames);
        final String[] chennaiArray = getResources().getStringArray(R.array.chnnames);
        final String[] bangaloreArray = getResources().getStringArray(R.array.blorenames);

        final Spinner localitySpinner = (MaterialSpinner)getActivity().findViewById(R.id.fap_locality);
        final ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,emptyArray);
        emptyAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
        localitySpinner.setAdapter(emptyAdapter);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int positionLocation, long id) {
                getActivity().findViewById(R.id.recyclerView).setVisibility(View.GONE);

                final String[] tempList;
                switch (positionLocation){
                    case 0:
                        tempList=mumbaiArray;
                        break;
                    case 1:
                        tempList=chennaiArray;
                        break;
                    case 2:
                        tempList=bangaloreArray;
                        break;
                    default:
                        tempList=emptyArray;
                        break;
                }
                ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,tempList);
                dataAdapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
                localitySpinner.setAdapter(dataAdapter2);

                localitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView2, View selectedItemView2, final int positionLocality, long id2) {

                        if(locationSpinner.getSelectedItem()!=null && localitySpinner.getSelectedItem()!=null) {

                            final String locality = localitySpinner.getSelectedItem().toString();
                            listings = new ArrayList<Listing>();

                            StringRequest stringRequest= new StringRequest(Request.Method.POST, HttpURL , new Response.Listener<String>(){
                                @Override
                                public void onResponse(String stringResponse){

                                    if(stringResponse.equals("No listings available in this locality")) {
                                        Snackbar snackbar = Snackbar
                                                .make(getView(), stringResponse, Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }

                                    else {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(stringResponse);
                                            JSONArray jListings = jsonResponse.getJSONArray("listings");
                                            for (int i = 0; i < jListings.length(); i++) {
                                                JSONObject listing = jListings.getJSONObject(i);
                                                String listingname = listing.getString("listingname");
                                                String address = listing.getString("address");
                                                String sublocality = listing.getString("sublocality");
                                                listings.add(new Listing(R.mipmap.ic_launcher, listingname, address, sublocality));
                                            }

                                            mAdapter.notifyDataSetChanged();
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    VolleyLog.e("Error: ", error.toString());
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> parameters = new HashMap<String,String>();
                                    parameters.put("locality",locality);
                                    return parameters;
                                }

                            };

                            requestqueue.add(stringRequest);

                            initializeAdapter();

                            // use a linear layout manager
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            getActivity().findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Find A Place");
    }



    private void initializeAdapter(){
        mAdapter = new RVAdapter(listings);
        mRecyclerView.setAdapter(mAdapter);
    }
}

