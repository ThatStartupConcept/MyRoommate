package com.myroommate.myroommate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by G551JK-DM053H on 01-09-2017.
 */

public class BedImageAdapter extends RecyclerView.Adapter<BedImageAdapter.BedImageHolder> {

    ArrayList<ArrayList<Integer>> listOfBeds;

    String BedAvailableURL = "http://merakamraa.com/php/CheckBedAvailability.php", BedBookURL = "http://merakamraa.com/php/BookBed.php";

    BedImageAdapter(final ArrayList<ArrayList<Integer>> listOfBeds) {

        this.listOfBeds = listOfBeds;

        // MAKE CALL TO 'beddb' HERE

    }

    public static class BedImageHolder extends RecyclerView.ViewHolder {

        TextView bedAvailability;
        ImageView bedImage;
        List<Integer> bedDetails;
        int bedID, isAvailable, isClicked = 0;

        BedImageHolder(View itemView) {
            super(itemView);
            bedImage = (ImageView) itemView.findViewById(R.id.bed_bedImage);
            bedAvailability = (TextView) itemView.findViewById(R.id.bed_bedAvailability);
        }
    }

    @Override
    public BedImageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bed_availability_layout, viewGroup, false);
        BedImageHolder bih = new BedImageHolder(v);
        return bih;
    }

    @Override
    public void onBindViewHolder(final BedImageHolder bedImageHolder, final int i) {

        bedImageHolder.bedDetails = listOfBeds.get(i);

        bedImageHolder.bedID = bedImageHolder.bedDetails.get(0);
        bedImageHolder.isAvailable = bedImageHolder.bedDetails.get(1);

        if (bedImageHolder.isAvailable == 0) {

            bedImageHolder.bedImage.setImageResource(R.mipmap.bed_icon_grey);
            bedImageHolder.bedAvailability.setText("OCCUPIED");

        } else {

            bedImageHolder.bedImage.setImageResource(R.mipmap.bed_icon);
            bedImageHolder.bedAvailability.setText("VACANT");
        }


        if (bedImageHolder.isAvailable == 1) {

            bedImageHolder.bedImage.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {

                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    final FirebaseUser currentUser = mAuth.getCurrentUser();

                    currentUser.getIdToken(true)
                            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {

                                        final String idToken = task.getResult().getToken();


                                        AlertDialog.Builder adb = new AlertDialog.Builder(bedImageHolder.itemView.getContext());


                                        adb.setTitle("Book this Bed?");


                                        adb.setIcon(android.R.drawable.ic_dialog_alert);


                                        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                final RequestQueue requestQueue = Volley.newRequestQueue(bedImageHolder.itemView.getContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, BedAvailableURL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String stringResponse) {

                                                        if (stringResponse.equals("Available")) {

                                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, BedBookURL, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String stringResponse) {

                                                                    if(stringResponse.equals("Booked")) {

                                                                        // TODO - Implement payment gateway

                                                                        Snackbar snackbar4 = Snackbar
                                                                                .make(bedImageHolder.itemView.getRootView(), "Booking success", Snackbar.LENGTH_LONG);
                                                                        snackbar4.show();

                                                                        // Refresh layout
                                                                        ((RecyclerView)bedImageHolder.itemView.getRootView().findViewById(R.id.ld_recyclerView)).getAdapter().notifyDataSetChanged();
                                                                    }

                                                                    else{
                                                                        Snackbar snackbar4 = Snackbar
                                                                                .make(bedImageHolder.itemView.getRootView(), "Something went wrong, please try again", Snackbar.LENGTH_LONG);
                                                                        snackbar4.show();

                                                                        ((RecyclerView)bedImageHolder.itemView.getRootView().findViewById(R.id.ld_recyclerView)).getAdapter().notifyDataSetChanged();
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
                                                                    parameters.put("firebase_token", idToken);
                                                                    parameters.put("bedID", Integer.toString(bedImageHolder.bedID));
                                                                    return parameters;
                                                                }

                                                            };

                                                            requestQueue.add(stringRequest2);


                                                        }

                                                        else if(stringResponse.equals("Booked")){

                                                            // Sorry, the bed you were trying to select has already been booked! Please try another bed

                                                            // Refresh layout
                                                            ((RecyclerView)bedImageHolder.itemView.getRootView().findViewById(R.id.ld_recyclerView)).getAdapter().notifyDataSetChanged();

                                                        }

                                                        else if(stringResponse.equals("Something went wrong")){

                                                            // Sorry, something went wrong! Please try another bed
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
                                                        parameters.put("bedID", Integer.toString(bedImageHolder.bedID));
                                                        return parameters;
                                                    }

                                                };

                                                requestQueue.add(stringRequest);

                                            }
                                        });


                                        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {


                                            }
                                        });

                                        AlertDialog alert = adb.create();

                                        alert.show();

                                    } else {

                                        // Please login to continue
                                    }
                                }
                            });

                }
            });
        }


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {

        return listOfBeds.size();
    }

}
