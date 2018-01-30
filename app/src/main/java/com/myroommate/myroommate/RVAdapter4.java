package com.myroommate.myroommate;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

public class RVAdapter4 extends RecyclerView.Adapter<RVAdapter4.BedImageHolder> {

    ArrayList<ArrayList<Integer>> listOfBeds;

    String BedDetailsURL;

    RVAdapter4(final ArrayList<ArrayList<Integer>> listOfBeds){

        this.listOfBeds = listOfBeds;

        // MAKE CALL TO 'beddb' HERE

    }

    public static class BedImageHolder extends RecyclerView.ViewHolder {

        TextView bedAvailability;
        ImageView bedImage;
        List<Integer> bedDetails;
        int bedID,isAvailable,isClicked = 0;

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

        if(bedImageHolder.isAvailable==0){

            bedImageHolder.bedImage.setImageResource(R.mipmap.bed_icon_grey);
            bedImageHolder.bedAvailability.setText("OCCUPIED");

        }

        else{

            bedImageHolder.bedImage.setImageResource(R.mipmap.bed_icon);
            bedImageHolder.bedAvailability.setText("VACANT");
        }


        if(bedImageHolder.isAvailable==1) {

            bedImageHolder.bedImage.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder adb = new AlertDialog.Builder(bedImageHolder.itemView.getContext());


                    adb.setView(bedImageHolder.itemView.getRootView());


                    adb.setTitle("Title of alert dialog");


                    adb.setIcon(android.R.drawable.ic_dialog_alert);


                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                           final RequestQueue requestQueue = Volley.newRequestQueue(bedImageHolder.itemView.getContext());

                            StringRequest stringRequest= new StringRequest(Request.Method.POST, BedDetailsURL , new Response.Listener<String>(){
                                @Override
                                public void onResponse(String stringResponse){

                                    if(stringResponse.equals("Available")){

                                        StringRequest stringRequest2= new StringRequest(Request.Method.POST, BedDetailsURL , new Response.Listener<String>(){
                                            @Override
                                            public void onResponse(String stringResponse){

                                                Snackbar snackbar4 = Snackbar
                                                        .make(bedImageHolder.itemView.getRootView(), "Booking success", Snackbar.LENGTH_LONG);
                                                snackbar4.show();

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
                                                return parameters;
                                            }

                                        };

                                        requestQueue.add(stringRequest2);

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
                                    parameters.put("bedID",Integer.toString(bedImageHolder.bedID));
                                    return parameters;
                                }

                            };

                            requestQueue.add(stringRequest);

                        } });


                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        } });
                    adb.show();

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
