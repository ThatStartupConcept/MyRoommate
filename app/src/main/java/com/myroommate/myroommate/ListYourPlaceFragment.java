package com.myroommate.myroommate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.HashMap;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;

import static com.myroommate.myroommate.MainActivity.hideKeyboardFrom;

public class ListYourPlaceFragment extends Fragment {

    Button Submit;
    EditText ListingName, Address, SubLocality, Pincode, Rent;
    String ListingNameHolder, AddressHolder, SubLocalityHolder, PincodeHolder;
    String LocationHolder, LocalityHolder;
    Integer RentHolder;
    String HttpURL = "http://merakamraa.com/php/AddListing.php";
    Boolean CheckEditText ;
    RequestQueue requestQueue;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[] locationArray =getResources().getStringArray(R.array.locationnames);
        final Spinner locationSpinner = (MaterialSpinner)getActivity().findViewById(R.id.lyp_location);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, locationArray);
        dataAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
        locationSpinner.setAdapter(dataAdapter);

        final String[] emptyArray = getResources().getStringArray(R.array.empty);
        final String[] mumbaiArray = getResources().getStringArray(R.array.mumbainames);
        final String[] chennaiArray = getResources().getStringArray(R.array.chnnames);
        final String[] bangaloreArray = getResources().getStringArray(R.array.blorenames);

        final Spinner localitySpinner = (MaterialSpinner)getActivity().findViewById(R.id.lyp_locality);
        final ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,emptyArray);
        emptyAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
        localitySpinner.setAdapter(emptyAdapter);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int positionLocation, long id) {

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

                        if(!locationSpinner.getSelectedItem().toString().equals("Select City") && !localitySpinner.getSelectedItem().toString().equals("Select Locality")) {
                            LocationHolder=locationSpinner.getSelectedItem().toString();
                            LocalityHolder=localitySpinner.getSelectedItem().toString();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_list_your_place, container, false);

        //Assign Id'S
        ListingName = (EditText)RootView.findViewById(R.id.lyp_listing_name);
        Address = (EditText)RootView.findViewById(R.id.lyp_address);
        SubLocality = (EditText)RootView.findViewById(R.id.lyp_sub_locality);
        Pincode = (EditText)RootView.findViewById(R.id.lyp_pincode);
        Rent = (EditText)RootView.findViewById(R.id.lyp_rent);
        Submit = (Button)RootView.findViewById(R.id.lyp_submit);
        requestQueue= Volley.newRequestQueue(getContext());

        //Adding Click Listener on button.
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboardFrom(getContext(), view);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                currentUser.getIdToken(true)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    final String idToken = task.getResult().getToken();

                if(CheckEditText){
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    StringRequest stringRequest= new StringRequest(Request.Method.POST, HttpURL , new Response.Listener<String>(){
                        @Override
                        public void onResponse(String stringResponse){
                            Snackbar snackbar = Snackbar
                                    .make(getView(), stringResponse, Snackbar.LENGTH_LONG);
                            snackbar.show();
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
                            parameters.put("location",LocationHolder);
                            parameters.put("locality",LocalityHolder);
                            parameters.put("listingname",ListingNameHolder);
                            parameters.put("address",AddressHolder);
                            parameters.put("sublocality",SubLocalityHolder);
                            parameters.put("pincode",PincodeHolder);
                            parameters.put("rent",RentHolder.toString());
                            parameters.put("firebase_token",idToken);
                            return parameters;
                        }
                    };

                    requestQueue.add(stringRequest);

                }
                else {

                    // If EditText is empty then this block will execute .
                    Snackbar snackbar = Snackbar
                            .make(getView(), "Please fill all the form fields.", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
                                } else {
                                    // Handle error -> task.getException();
                                }
                            }
                        });
            }
        });

        return RootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("List Your Place");
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