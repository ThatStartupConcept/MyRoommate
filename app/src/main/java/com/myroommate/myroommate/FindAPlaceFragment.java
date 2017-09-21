package com.myroommate.myroommate;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

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

        final List<String> list0 = new ArrayList<String>();
        list0.add("Select One");

        List<String> list1 = Arrays.asList(getResources().getStringArray(R.array.locationnames));
        final int listsize1 = list1.size() - 1;

        final Spinner spinner1 = (Spinner)getActivity().findViewById(R.id.spinner1);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list1) {
            @Override
            public int getCount() {
                return (listsize1); // Truncate the list
            }
        };

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
        spinner1.setSelection(listsize1);

        final List<String> list2 = Arrays.asList(getResources().getStringArray(R.array.mumbainames));
        final List<String> list3 = Arrays.asList(getResources().getStringArray(R.array.chnnames));
        final List<String> list4 = Arrays.asList(getResources().getStringArray(R.array.blorenames));


        final Spinner spinner2 = (Spinner)getActivity().findViewById(R.id.spinner2);

        requestqueue = Volley.newRequestQueue(getContext());
        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerView);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int position2, long id) {
                getActivity().findViewById(R.id.recyclerView).setVisibility(View.GONE);

                final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list0) {

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {

                        final List<String> tempList;
                        final int tempSize;

                        switch (position2){
                            case 0:
                                tempSize=list2.size()-1;
                                tempList=list2;
                                break;
                            case 1:
                                tempSize=list3.size()-1;
                                tempList=list3;
                                break;
                            case 2:
                                tempSize=list4.size()-1;
                                tempList=list4;
                                break;
                            default:
                                tempSize=list0.size();
                                tempList=list0;
                                break;
                        }
                        ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, tempList) {
                            @Override
                            public int getCount() {
                                return (tempSize); // Truncate the list
                            }
                        };
                        spinner2.setAdapter(dataAdapter3);
                        return dataAdapter3.getDropDownView(position, convertView, parent);
                    }
                };

                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter2);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView2, View selectedItemView2, final int position3, long id2) {

                        if(!spinner1.getSelectedItem().toString().equals("Select One") && !spinner2.getSelectedItem().toString().equals("Select One")) {

                            final String locality = spinner2.getSelectedItem().toString();

                            listings = new ArrayList<Listing>();


                            StringRequest stringRequest= new StringRequest(Request.Method.POST, HttpURL , new Response.Listener<String>(){
                                @Override
                                public void onResponse(String stringResponse){
                                    try{
                                        JSONObject jsonResponse= new JSONObject(stringResponse);
                                        JSONArray jListings = jsonResponse.getJSONArray("listings");
                                        for(int i=0; i<jListings.length();i++){
                                            JSONObject listing = jListings.getJSONObject(i);
                                            String listingname = listing.getString("listingname");
                                            String address = listing.getString("address");
                                            String sublocality = listing.getString("sublocality");
                                            listings.add(new Listing(R.mipmap.ic_launcher,listingname,address,sublocality));
                                        }

                                        mAdapter.notifyDataSetChanged();

                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
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

