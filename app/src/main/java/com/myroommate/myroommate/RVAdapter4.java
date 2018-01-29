package com.myroommate.myroommate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
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
import android.R.color;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


/**
 * Created by G551JK-DM053H on 01-09-2017.
 */

public class RVAdapter4 extends RecyclerView.Adapter<RVAdapter4.BedImageHolder> {

    ArrayList<ArrayList<Integer>> listOfBeds;

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

            bedImageHolder.bedImage.setImageResource(drawable.ic_delete);
            bedImageHolder.bedAvailability.setText("OCCUPIED");
            bedImageHolder.bedAvailability.setVisibility(View.VISIBLE);

        }

        else{

            bedImageHolder.bedImage.setImageResource(R.drawable.ic_menu_home);
            bedImageHolder.bedAvailability.setText("VACANT");
        }



        bedImageHolder.bedImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(bedImageHolder.isClicked==1){

                    bedImageHolder.bedImage.setImageResource(R.drawable.ic_menu_home);
                    bedImageHolder.bedAvailability.setText("VACANT");
                    bedImageHolder.isClicked = 0;

                }

                else{

                    bedImageHolder.bedImage.setImageResource(drawable.ic_delete);
                    bedImageHolder.bedAvailability.setText("OCCUPIED");
                    bedImageHolder.isClicked = 1;
                }
            }
        });


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
