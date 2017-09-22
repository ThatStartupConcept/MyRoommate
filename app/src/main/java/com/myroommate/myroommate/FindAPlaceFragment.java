package com.myroommate.myroommate;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

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

        requestqueue = Volley.newRequestQueue(getContext());
        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerView);

        final String[] locationArray =getResources().getStringArray(R.array.locationnames);
        final Spinner spinner1 = (MaterialSpinner)getActivity().findViewById(R.id.spinner1);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, locationArray);
        dataAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
        spinner1.setAdapter(dataAdapter);

        final String[] emptyArray = getResources().getStringArray(R.array.empty);
        final String[] mumbaiArray = getResources().getStringArray(R.array.mumbainames);
        final String[] chennaiArray = getResources().getStringArray(R.array.chnnames);
        final String[] bangaloreArray = getResources().getStringArray(R.array.blorenames);

        final Spinner spinner2 = (MaterialSpinner)getActivity().findViewById(R.id.spinner2);
        final ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,emptyArray);
        emptyAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
        spinner2.setAdapter(emptyAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int position2, long id) {
                getActivity().findViewById(R.id.recyclerView).setVisibility(View.GONE);

                final String[] tempList;
                switch (position2){
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
                spinner2.setAdapter(dataAdapter2);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView2, View selectedItemView2, final int position3, long id2) {

                        if(!spinner1.getSelectedItem().toString().equals("Select City") && !spinner2.getSelectedItem().toString().equals("Select Locality")) {

                            final String locality = spinner2.getSelectedItem().toString();

                            listings = new ArrayList<Listing>();

                            StringRequest stringRequest= new StringRequest(Request.Method.POST, HttpURL , new Response.Listener<String>(){
                                @Override
                                public void onResponse(String stringResponse){

                                    if(stringResponse.equals("No listings available in this locality")) {
                                        Toast.makeText(getActivity(), stringResponse, Toast.LENGTH_LONG).show();
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

