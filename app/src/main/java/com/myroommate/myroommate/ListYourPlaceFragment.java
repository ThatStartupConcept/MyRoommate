package com.myroommate.myroommate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;

import static com.myroommate.myroommate.MainActivity.hideKeyboardFrom;

public class ListYourPlaceFragment extends Fragment {

    private RecyclerView mRecyclerView;


    Button Submit, numberOfRoomsMinus, numberOfRoomsPlus;
    EditText ListingName, Address, SubLocality, Pincode, Rent, numberOfRooms;
    String ListingNameHolder, AddressHolder, SubLocalityHolder, PincodeHolder;
    String LocationHolder, LocalityHolder;
    Integer RentHolder, roomCounter, i;
    String HttpURL = "http://merakamraa.com/php/AddListing.php";
    String RoomURL = "http://merakamraa.com/php/AddRoom.php";
    String BedURL = "http://merakamraa.com/php/AddBed.php";
    Boolean CheckEditText;
    Boolean everythingWorked = true;
    RequestQueue requestqueue;

    String InfoURL = "http://merakamraa.com/php/GetListingInfo.php";
    private int locationcount = 0;

    LYPAddRoomAdapter mAdapter;

    private ArrayList<String[]> listOfLists = new ArrayList<String[]>();

    private ArrayList<String[]> listOfLocationArrays = new ArrayList<String[]>();

    private ArrayList<String> locationList = new ArrayList<String>();

    private String regexNameCheck = "^[A-Za-z'\\s]+$";

    private String regexNumberCheck = "^[0-9]+$";

    private String regexOtherCheck = "^[A-Za-z0-9'\\s]+$";

    private boolean regexChecker;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestqueue = Volley.newRequestQueue(getActivity());
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.lyp_recyclerView);

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

                final String[] locationArray = locationList.toArray(new String[locationList.size()]);
                final String[] emptyArray = getResources().getStringArray(R.array.empty);

                final Spinner locationSpinner = (MaterialSpinner) getActivity().findViewById(R.id.lyp_location);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, locationArray);
                dataAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
                locationSpinner.setAdapter(dataAdapter);


                final Spinner localitySpinner = (MaterialSpinner) getActivity().findViewById(R.id.lyp_locality);
                final ArrayAdapter<String> emptyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, emptyArray);
                emptyAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_layout);
                localitySpinner.setAdapter(emptyAdapter);

                locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, final int positionLocation, long id) {

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
                                    LocationHolder = locationSpinner.getSelectedItem().toString();
                                    LocalityHolder = localitySpinner.getSelectedItem().toString();
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_list_your_place, container, false);

        //Assign Id'S
        ListingName = (EditText) RootView.findViewById(R.id.lyp_listing_name);
        Address = (EditText) RootView.findViewById(R.id.lyp_address);
        SubLocality = (EditText) RootView.findViewById(R.id.lyp_sub_locality);
        Pincode = (EditText) RootView.findViewById(R.id.lyp_pincode);
        Rent = (EditText) RootView.findViewById(R.id.lyp_rent);
        Submit = (Button) RootView.findViewById(R.id.lyp_submit);
        numberOfRoomsMinus = (Button) RootView.findViewById(R.id.lyp_roomNumberMinus);
        numberOfRoomsPlus = (Button) RootView.findViewById(R.id.lyp_roomnumberPlus);
        numberOfRooms = (EditText) RootView.findViewById(R.id.lyp_numberOfRooms);

        roomCounter = Integer.valueOf(numberOfRooms.getText().toString());

        requestqueue = Volley.newRequestQueue(getContext());

        numberOfRoomsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    roomCounter = Integer.valueOf(numberOfRooms.getText().toString());
                    if (roomCounter > 0) {
                        roomCounter--;
                        numberOfRooms.setText(Integer.toString(roomCounter));
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(getView(), "How do you make a house with less than zero rooms? :thinking:", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        numberOfRooms.setText("0");
                    }
                } catch (NumberFormatException e) {

                }
            }
        });

        numberOfRoomsPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    roomCounter = Integer.valueOf(numberOfRooms.getText().toString());
                    roomCounter++;
                    numberOfRooms.setText(Integer.toString(roomCounter));
                } catch (NumberFormatException e) {

                }
            }
        });

        numberOfRooms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    roomCounter = Integer.valueOf(numberOfRooms.getText().toString());


                    if (roomCounter > 0 && roomCounter <= 6) {

                        initializeAdapter();
                        getActivity().findViewById(R.id.lyp_recyclerView).setVisibility(View.VISIBLE);
                    } else if (roomCounter < 0) {
                        Snackbar snackbar = Snackbar
                                .make(getView(), "How do you make a house with less than zero rooms? :thinking:", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        numberOfRooms.setText("0");
                    } else if (roomCounter > 6) {
                        Snackbar snackbar = Snackbar
                                .make(getView(), "We do not currently list houses of over 6 bedrooms. Sorry!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        numberOfRooms.setText("6");
                    } else {
                        getActivity().findViewById(R.id.lyp_recyclerView).setVisibility(View.GONE);
                    }
                } catch (NumberFormatException e) {
                    numberOfRooms.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Adding Click Listener on button.
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboardFrom(getContext(), view);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                final FirebaseUser currentUser = mAuth.getCurrentUser();


                // Checking whether EditText is Empty or Not
                CheckEditTextIsValidOrNot();

                if (CheckEditText) {
                    for (int i = 0; i < roomCounter; i++) {
                        if (mAdapter.numberOfBeds[i] == null) {
                            CheckEditText = false;
                        }
                    }
                }

                CheckEditTextIsValidOrNot();

                currentUser.getIdToken(true)
                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    final String idToken = task.getResult().getToken();

                                    if (CheckEditText) {
                                        // If EditText is not empty and CheckEditText = True then this block will execute.

                                        Snackbar snackbar1 = Snackbar
                                                .make(getView(), "Adding your listing, please wait...", Snackbar.LENGTH_LONG);
                                        snackbar1.show();

                                        final String numberOfRooms = Integer.toString(mAdapter.getItemCount());

                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpURL, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String stringResponse) {
                                                if (!stringResponse.equals("Your listing was added.")) {
                                                    everythingWorked = false;
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
                                                parameters.put("location", LocationHolder);
                                                parameters.put("locality", LocalityHolder);
                                                parameters.put("listingname", ListingNameHolder);
                                                parameters.put("ownername", currentUser.getDisplayName());
                                                parameters.put("address", AddressHolder);
                                                parameters.put("sublocality", SubLocalityHolder);
                                                parameters.put("pincode", PincodeHolder);
                                                parameters.put("rent", RentHolder.toString());
                                                parameters.put("numberOfRooms", numberOfRooms);

                                                parameters.put("firebase_token", idToken);
                                                return parameters;
                                            }
                                        };

                                        requestqueue.add(stringRequest);

                                        if (everythingWorked) {

                                            for (i = 0; i < mAdapter.getItemCount(); i++) {

                                                final String numberOfBeds = Integer.toString(mAdapter.numberOfBeds[i]);
                                                final String isACAvailable = Integer.toString(mAdapter.isACAvailable[i]);
                                                final String isABAvailable = Integer.toString(mAdapter.isABAvailable[i]);

                                                Snackbar snackbar2 = Snackbar
                                                        .make(getView(), "Number of beds in room " + Integer.toString(i + 1) + " is " + numberOfBeds, Snackbar.LENGTH_LONG);
                                                snackbar2.show();

                                                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, RoomURL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String stringResponse) {
                                                        if (!stringResponse.equals("Roomdb updated")) {
                                                            everythingWorked = false;
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

                                                        parameters.put("listingname", ListingNameHolder);
                                                        parameters.put("firebase_token", idToken);

                                                        parameters.put("numberOfBeds", numberOfBeds);
                                                        parameters.put("isACAvailable", isACAvailable);
                                                        parameters.put("isABAvailable", isABAvailable);

                                                        return parameters;
                                                    }
                                                };


                                                requestqueue.add(stringRequest2);
                                            }
                                        }

                                        if (everythingWorked) {

                                            Snackbar snackbar3 = Snackbar
                                                    .make(getView(), "Your listing has been added.", Snackbar.LENGTH_LONG);
                                            snackbar3.show();

                                        } else {
                                            Snackbar snackbar4 = Snackbar
                                                    .make(getView(), "Something went wrong, please try again", Snackbar.LENGTH_LONG);
                                            snackbar4.show();
                                        }

                                        everythingWorked = true;


                                    } else {

                                        // If EditText is empty then this block will execute .
                                        Snackbar snackbar5 = Snackbar
                                                .make(getView(), "Please fill all form fields with valid info", Snackbar.LENGTH_LONG);
                                        snackbar5.show();

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

    protected void CheckEditTextIsValidOrNot() {

        ListingNameHolder = ListingName.getText().toString();
        AddressHolder = Address.getText().toString();
        SubLocalityHolder = SubLocality.getText().toString();
        PincodeHolder = Pincode.getText().toString();

        try {
            RentHolder = Integer.valueOf(Rent.getText().toString());
        } catch (NumberFormatException e) {
            RentHolder = 0;
        }

        CheckEditText = !(TextUtils.isEmpty(LocationHolder) || TextUtils.isEmpty(LocalityHolder) || TextUtils.isEmpty(ListingNameHolder) || TextUtils.isEmpty(AddressHolder) || TextUtils.isEmpty(PincodeHolder) || RentHolder == 0 || roomCounter == 0);

        regexChecker = ListingNameHolder.matches(regexOtherCheck) && AddressHolder.matches(regexOtherCheck) && SubLocalityHolder.matches(regexNameCheck) && PincodeHolder.matches(regexNumberCheck);

        CheckEditText = CheckEditText && regexChecker;
    }

    private void initializeAdapter() {
        mAdapter = new LYPAddRoomAdapter(roomCounter);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

    }
}