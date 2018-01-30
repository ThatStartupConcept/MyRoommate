package com.myroommate.myroommate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


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

            bedImageHolder.bedImage.setImageResource(R.mipmap.bed_icon_grey);
            bedImageHolder.bedAvailability.setText("OCCUPIED");

        }

        else{

            bedImageHolder.bedImage.setImageResource(R.mipmap.bed_icon);
            bedImageHolder.bedAvailability.setText("VACANT");
        }



        bedImageHolder.bedImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(bedImageHolder.isAvailable==1) {


                    if (bedImageHolder.isClicked == 1) {

                        bedImageHolder.bedImage.setBackgroundColor(Color.TRANSPARENT);
                        bedImageHolder.isClicked = 0;

                    } else {

                        bedImageHolder.bedImage.setBackgroundColor(R.color.colorAccent);
                        bedImageHolder.isClicked = 1;
                    }
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
