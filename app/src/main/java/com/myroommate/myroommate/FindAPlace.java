package com.myroommate.myroommate;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import static com.myroommate.myroommate.R.id.parent;

public class FindAPlace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_a_place);

        final List<String> list0 = new ArrayList<String>();
        list0.add("Select One");

        List<String> list1 = new ArrayList<String>();
        list1.add("Mumbai");
        list1.add("Chennai");
        list1.add("Bangalore");
        list1.add("Select One");

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

        final List<String> list2 = new ArrayList<String>();
        list2.add("Chembur");
        list2.add("Vashi");
        list2.add("Panvel");
        list1.add("Select One");

        final int listsize2 = list2.size() - 1;

        final List<String> list3 = new ArrayList<String>();
        list3.add("Tambaram");
        list3.add("Anna Nagar");
        list3.add("Select One");

        final int listsize3 = list3.size() - 1;

        final List<String> list4 = new ArrayList<String>();
        list4.add("Ulsoor");
        list4.add("J P Nagar");
        list4.add("Select One");

        final int listsize4 = list4.size() - 1;


        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int position2, long id) {


                final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(FindAPlace.this, android.R.layout.simple_spinner_item, list0) {

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {

                        View v = null;

                        if (position2 == 0) {
                            ArrayAdapter dataAdapter3 = new ArrayAdapter(FindAPlace.this, android.R.layout.simple_spinner_item, list2) {
                                @Override
                                public int getCount() {
                                    return (listsize2); // Truncate the list
                                }
                            };
                            spinner2.setSelection(listsize2);
                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);

                        } else if (position2 == 1) {
                            ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(FindAPlace.this, android.R.layout.simple_spinner_item, list3) {
                                @Override
                                public int getCount() {
                                    return (listsize3); // Truncate the list
                                }
                            };
                            spinner2.setSelection(listsize3);
                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);
                        } else if (position2 == 2) {
                            ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(FindAPlace.this, android.R.layout.simple_spinner_item, list4) {
                                @Override
                                public int getCount() {
                                    return (listsize4); // Truncate the list
                                }
                            };
                            spinner2.setSelection(listsize4);
                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);
                        } else {
                            ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(FindAPlace.this, android.R.layout.simple_spinner_item, list0) {

                            };
                            spinner2.setSelection(0);
                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);
                        }

                        return v;

                    }


                };


                dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(dataAdapter2);
            }

            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });

    }



}

