package com.myroommate.myroommate;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindAPlace extends Fragment {

    private RecyclerView mRecyclerView;
    private RVAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Listing> listings;

    public static FindAPlace newInstance() {

        return new FindAPlace();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.activity_find_a_place, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.recyclerView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

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

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int position2, long id) {
                getActivity().findViewById(R.id.recyclerView).setVisibility(View.GONE);

                final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list0) {

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {

                        View v;


                        if (position2 == 0) {
                            final int temp=list2.size()-1;
                            ArrayAdapter dataAdapter3 = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, list2) {
                                @Override
                                public int getCount() {
                                    return (temp); // Truncate the list
                                }
                            };

                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);



                        } else if (position2 == 1) {
                            final int temp=list3.size()-1;
                            ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list3) {
                                @Override
                                public int getCount() {
                                    return (temp);
                                }
                            };

                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);


                        } else if (position2 == 2) {
                            final int temp=list4.size()-1;
                            ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list4) {
                                @Override
                                public int getCount() {
                                    return (temp); // Truncate the list
                                }
                            };
                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);


                        } else {
                            ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list0) {

                            };
                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);
                        }

                        return v;

                    }


                };


                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter2);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView2, View selectedItemView2, final int position3, long id2) {

                        Resources res = getResources();


                        if(!spinner1.getSelectedItem().toString().equals("Select One") && !spinner2.getSelectedItem().toString().equals("Select One")) {

                            TypedArray housing = res.obtainTypedArray(R.array.housing);

                            TypedArray location = res.obtainTypedArray(housing.getResourceId(position2, 0));

                            TypedArray locality = res.obtainTypedArray(location.getResourceId(position3,0));

                            String[] listing;

                            listings = new ArrayList<>();

                            for(int i=0;i<locality.length();i++){
                                listing = res.getStringArray(locality.getResourceId(i,0));
                                listings.add(new Listing(R.mipmap.listing_image,listing[0],listing[1],listing[2]));
                            }

                            initializeAdapter();

                            getActivity().findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parentView2)
                    {

                    }

                });


            }

            public void onNothingSelected(AdapterView<?> parentView)
            {
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

