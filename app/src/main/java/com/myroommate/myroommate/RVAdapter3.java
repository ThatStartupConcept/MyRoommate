package com.myroommate.myroommate;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.R.drawable;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static android.util.Base64.NO_CLOSE;
import static android.util.Base64.decode;

/**
 * Created by G551JK-DM053H on 01-09-2017.
 */

public class RVAdapter3 extends RecyclerView.Adapter<RVAdapter3.RoomCardHolder> {

    int numberOfRooms;
    String primary_key;
    Activity currentActivity;
    Context currentContext;
    View currentView;
    RequestQueue requestQueue;
    String BedDetailsURL = "http://merakamraa.com/php/BedDetails.php";

    private ArrayList<ArrayList<Integer>> listOfRooms;
    StringRequest stringRequest;


    RVAdapter3(String primary_key, final Activity currentActivity, Context currentContext, final View currentView, final ArrayList<ArrayList<Integer>> listOfRooms){

        this.currentView = currentView;
        this.currentActivity = currentActivity;
        this.currentContext = currentContext;
        this.numberOfRooms = numberOfRooms;
        this.primary_key = primary_key;
        this.listOfRooms = listOfRooms;

        // MAKE CALL TO 'roomdb' HERE


    }

    public static class RoomCardHolder extends RecyclerView.ViewHolder {

        CardView room_card;
        CheckedTextView isACAvailable,isABAvailable;
        TextView roomNumber,noFeatures;
        RecyclerView rc_recyclerView;
        List<Integer> roomDetails;
        int  roomID, isACAvailableInt, isABAvailableInt, numberOfBeds;


        RoomCardHolder(View itemView) {
            super(itemView);
            room_card = (CardView) itemView.findViewById(R.id.room_card);
            isACAvailable = (CheckedTextView) itemView.findViewById(R.id.room_isACAvailable);
            isABAvailable = (CheckedTextView) itemView.findViewById(R.id.room_isABAvailable);
            roomNumber = (TextView) itemView.findViewById(R.id.room_roomNumber);
            noFeatures = (TextView) itemView.findViewById(R.id.room_noFeatures);
            rc_recyclerView = (RecyclerView) itemView.findViewById(R.id.rc_recyclerView);

        }
    }

    @Override
    public RoomCardHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_card_layout, viewGroup, false);
        final RoomCardHolder rch = new RoomCardHolder(v);

        
        

        return rch;
    }

    @Override
    public void onBindViewHolder(final RoomCardHolder roomCardHolder, final int i) {

        roomCardHolder.roomNumber.setText("Room Number "+Integer.toString(i+1));

        roomCardHolder.roomDetails = listOfRooms.get(i);

        roomCardHolder.roomID = roomCardHolder.roomDetails.get(0);
        roomCardHolder.isACAvailableInt = roomCardHolder.roomDetails.get(1);
        roomCardHolder.isABAvailableInt = roomCardHolder.roomDetails.get(2);
        roomCardHolder.numberOfBeds = roomCardHolder.roomDetails.get(3);

        if(roomCardHolder.isACAvailableInt==0){
            roomCardHolder.isACAvailable.setCheckMarkDrawable(drawable.checkbox_off_background);
        }

        if(roomCardHolder.isABAvailableInt==0){
            roomCardHolder.isABAvailable.setCheckMarkDrawable(drawable.checkbox_off_background);
        }

        if(roomCardHolder.isACAvailableInt==0 && roomCardHolder.isABAvailableInt==0){
            roomCardHolder.isACAvailable.setVisibility(View.GONE);
            roomCardHolder.isABAvailable.setVisibility(View.GONE);
            roomCardHolder.noFeatures.setText("No special features. FeelsBadMan");
        }

        requestQueue = Volley.newRequestQueue(currentContext);

        stringRequest= new StringRequest(Request.Method.POST, BedDetailsURL , new Response.Listener<String>(){
            @Override
            public void onResponse(String stringResponse){

                if(!stringResponse.equals("Listing not found.")) {

                    ArrayList<ArrayList<Integer>> listOfBeds = new ArrayList<ArrayList<Integer>>();

                    try {

                        JSONObject jsonResponse = new JSONObject(stringResponse);
                        JSONArray jListings = jsonResponse.getJSONArray("beds");

                        for (int j = 0; j < jListings.length(); j++) {

                            JSONObject listing = jListings.getJSONObject(j);
                            ArrayList<Integer> bedDetails = new ArrayList<Integer>();                                                      // AUTOMATICALLY GENERATED CODE ENDS
                            bedDetails.add(Integer.valueOf(listing.getString("bedID")));
                            bedDetails.add(Integer.valueOf(listing.getString("isAvailable")));

                            listOfBeds.add(bedDetails);
                        }

                        final RVAdapter4 mAdapter = new RVAdapter4(listOfBeds);
                        roomCardHolder.rc_recyclerView.setAdapter(mAdapter);

                        // use a linear layout manager
                        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(currentContext,LinearLayoutManager.HORIZONTAL,false);
                        roomCardHolder.rc_recyclerView.setLayoutManager(mLayoutManager);
                        roomCardHolder.rc_recyclerView.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{

                    Snackbar snackbar3 = Snackbar
                            .make(currentView, "Listing issue", Snackbar.LENGTH_LONG);
                    snackbar3.show();

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
                parameters.put("roomID",Integer.toString(roomCardHolder.roomID));
                return parameters;
            }

        };

        requestQueue.add(stringRequest);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {

        return listOfRooms.size();
    }

    public void bookNow(){

    }


}
