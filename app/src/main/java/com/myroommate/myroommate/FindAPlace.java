package com.myroommate.myroommate;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindAPlace extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RVAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Listing> listings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_a_place);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        final List<String> list0 = new ArrayList<String>();
        list0.add("Select One");

        List<String> list1 = Arrays.asList(getResources().getStringArray(R.array.locationnames));
        final int listsize1 = list1.size() - 1;


        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1) {
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


        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int position2, long id) {

                final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(FindAPlace.this, android.R.layout.simple_spinner_item, list0) {

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {

                        View v;


                        if (position2 == 0) {
                            final int temp=list2.size()-1;
                            ArrayAdapter dataAdapter3 = new ArrayAdapter(FindAPlace.this, android.R.layout.simple_spinner_item, list2) {
                                @Override
                                public int getCount() {
                                    return (temp); // Truncate the list
                                }
                            };

                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);
                            spinner2.setSelection(temp);


                        } else if (position2 == 1) {
                            final int temp=list3.size()-1;
                            ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(FindAPlace.this, android.R.layout.simple_spinner_item, list3) {
                                @Override
                                public int getCount() {
                                    return (temp);
                                }
                            };

                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);
                            spinner2.setSelection(temp);


                        } else if (position2 == 2) {
                            final int temp=list4.size()-1;
                            ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(FindAPlace.this, android.R.layout.simple_spinner_item, list4) {
                                @Override
                                public int getCount() {
                                    return (temp); // Truncate the list
                                }
                            };
                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);
                            spinner2.setSelection(temp);


                        } else {
                            ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(FindAPlace.this, android.R.layout.simple_spinner_item, list0) {

                            };
                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);
                            spinner2.setSelection(0);
                        }

                        return v;

                    }


                };


                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter2);

                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView2, View selectedItemView2, final int position3, long id2) {

                        // TextView text2 = (TextView) findViewById(R.id.textView2);

                        Resources res = getResources();


                        if((position2!=0 && position2!=3 && position3<=1) || (position2==0 && position3<=2)) {

                            TypedArray housing = res.obtainTypedArray(R.array.housing);

                            TypedArray location = res.obtainTypedArray(housing.getResourceId(position2, 0));

                            TypedArray locality = res.obtainTypedArray(location.getResourceId(position3,0));

                            String[] listing;

                            listings = new ArrayList<>();

                            // specify an adapter (see also next example)
                            mAdapter = new RVAdapter(listings);
                            mRecyclerView.setAdapter(mAdapter);
                            initializeAdapter();

                            for(int i=0;i<locality.getIndexCount();i++){
                                listing = res.getStringArray(locality.getResourceId(i,0));
                                listings.add(new Listing(R.mipmap.listing_image,listing[0],listing[1],listing[2]));
                            }



                            findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
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



    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(listings);
        mRecyclerView.setAdapter(adapter);
    }
}

