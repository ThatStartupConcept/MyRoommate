package com.myroommate.myroommate;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ListYourPlaceFragment extends Fragment {

    Button Submit;
    EditText ListingName, Address, SubLocality, Pincode, Rent;
    String ListingNameHolder, AddressHolder, SubLocalityHolder, PincodeHolder;
    String LocationHolder, LocalityHolder;
    Integer RentHolder;
    String finalResult ;
    String HttpURL = "https://myroommate.000webhostapp.com/UserLogin.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    public static final String TITLE = "List Your Place";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final List<String> list0 = new ArrayList<String>();
        list0.add("Select One");

        List<String> list1 = Arrays.asList(getResources().getStringArray(R.array.locationnames));
        final int listsize1 = list1.size() - 1;

        final Spinner spinner1 = (Spinner)getActivity().findViewById(R.id.lyp_location);

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

        final Spinner spinner2 = (Spinner)getActivity().findViewById(R.id.lyp_locality);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int position2, long id) {
                LocalityHolder=null;
                final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list0) {
                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v;
                        if (position2 == 0) {
                            final int temp=list2.size()-1;
                            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list2) {
                                @Override
                                public int getCount() {
                                    return (temp); // Truncate the list
                                }
                            };
                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);
                        }
                        else if (position2 == 1) {
                            final int temp=list3.size()-1;
                            ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list3) {
                                @Override
                                public int getCount() {
                                    return (temp);
                                }
                            };
                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);
                        }
                        else if (position2 == 2) {
                            final int temp=list4.size()-1;
                            ArrayAdapter dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list4) {
                                @Override
                                public int getCount() {
                                    return (temp); // Truncate the list
                                }
                            };
                            spinner2.setAdapter(dataAdapter3);
                            v = dataAdapter3.getDropDownView(position, convertView, parent);
                        }
                        else {
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
                            LocationHolder=spinner1.getSelectedItem().toString();
                            LocalityHolder=spinner2.getSelectedItem().toString();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_list_your_place, container, false);

        //Assign Id'S
        ListingName = (EditText)RootView.findViewById(R.id.lyp_listing_name);
        Address = (EditText)RootView.findViewById(R.id.lyp_address);
        SubLocality = (EditText)RootView.findViewById(R.id.lyp_sub_locality);
        Pincode = (EditText)RootView.findViewById(R.id.lyp_pincode);
        Rent = (EditText)RootView.findViewById(R.id.lyp_rent);
        Submit = (Button)RootView.findViewById(R.id.lyp_submit);

        //Adding Click Listener on button.
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    Toast.makeText(getActivity(), "Submitted!", Toast.LENGTH_LONG).show();
                }
                else {

                    // If EditText is empty then this block will execute .
                    Toast.makeText(getActivity(), "Please fill all the form fields.", Toast.LENGTH_LONG).show();

                }


            }
        });

        return RootView;
    }

    protected void CheckEditTextIsEmptyOrNot(){

        ListingNameHolder=ListingName.getText().toString();
        AddressHolder=Address.getText().toString();
        SubLocalityHolder=SubLocality.getText().toString();
        PincodeHolder = Pincode.getText().toString();

        try {
            RentHolder = Integer.valueOf(Rent.getText().toString());
        }
        catch (NumberFormatException e){
            RentHolder=0;
        }


        if(TextUtils.isEmpty(LocationHolder) || TextUtils.isEmpty(LocalityHolder) || TextUtils.isEmpty(ListingNameHolder) || TextUtils.isEmpty(AddressHolder) || TextUtils.isEmpty(PincodeHolder) || RentHolder==0)
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }
}