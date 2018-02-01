package com.myroommate.myroommate;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.R.drawable;

import java.util.List;

/**
 * Created by G551JK-DM053H on 01-09-2017.
 */

public class LYPAddRoomAdapter extends RecyclerView.Adapter<LYPAddRoomAdapter.RoomListViewHolder> {

    int numberOfRooms;

    Integer[] numberOfBeds = new Integer[6];
    Integer[] isACAvailable = new Integer[6];
    Integer[] isABAvailable = new Integer[6];

    LYPAddRoomAdapter(int numberOfRooms) {

        this.numberOfRooms = numberOfRooms;

    }

    public static class RoomListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout outerLinearLayout, innerLinearLayout;
        TextView beds_textview, features, roomNumber;
        EditText beds_edittext;
        CheckedTextView isACAvailable, isABAvailable;

        RoomListViewHolder(View itemView) {
            super(itemView);
            outerLinearLayout = (LinearLayout) itemView.findViewById(R.id.listplace_room_outerLinearLayout);
            roomNumber = (TextView) itemView.findViewById(R.id.listplace_room_roomNumber);
            innerLinearLayout = (LinearLayout) itemView.findViewById(R.id.listplace_room_innerLinearLayout);
            beds_textview = (TextView) itemView.findViewById(R.id.listplace_room_beds_textview);
            features = (TextView) itemView.findViewById(R.id.listplace_room_features);
            beds_edittext = (EditText) itemView.findViewById(R.id.listplace_room_beds_edittext);
            isACAvailable = (CheckedTextView) itemView.findViewById(R.id.listplace_room_isACAvailable);
            isABAvailable = (CheckedTextView) itemView.findViewById(R.id.listplace_room_isABAvailable);
        }
    }

    @Override
    public RoomListViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_linear_layout, viewGroup, false);
        final RoomListViewHolder rlvh = new RoomListViewHolder(v);


        return rlvh;
    }

    @Override
    public void onBindViewHolder(final RoomListViewHolder roomViewHolder, final int i) {

        String roomNumberString = "Room " + Integer.toString(i + 1);

        roomViewHolder.roomNumber.setText(roomNumberString);

        roomViewHolder.beds_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {

                    if (Integer.parseInt(s.toString()) < 1) {
                        Snackbar snackbar = Snackbar
                                .make(roomViewHolder.itemView, "Number of beds cannot be less than 1", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        roomViewHolder.beds_edittext.setText("1");
                    }

                    numberOfBeds[i] = Integer.valueOf(s.toString());

                } catch (NumberFormatException e) {

                    numberOfBeds[i] = null;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        numberOfBeds[i] = 2;
        isACAvailable[i] = 0;
        isABAvailable[i] = 0;

        roomViewHolder.isACAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (roomViewHolder.isACAvailable.isChecked()) {
                    roomViewHolder.isACAvailable.setCheckMarkDrawable(drawable.checkbox_off_background);
                    roomViewHolder.isACAvailable.setChecked(false);
                    isACAvailable[i] = 0;
                } else {
                    roomViewHolder.isACAvailable.setCheckMarkDrawable(drawable.checkbox_on_background);
                    roomViewHolder.isACAvailable.setChecked(true);
                    isACAvailable[i] = 1;
                }

            }
        });

        roomViewHolder.isABAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomViewHolder.isABAvailable.isChecked()) {
                    roomViewHolder.isABAvailable.setCheckMarkDrawable(drawable.checkbox_off_background);
                    roomViewHolder.isABAvailable.setChecked(false);
                    isABAvailable[i] = 0;
                } else {
                    roomViewHolder.isABAvailable.setCheckMarkDrawable(drawable.checkbox_on_background);
                    roomViewHolder.isABAvailable.setChecked(true);
                    isABAvailable[i] = 1;
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

        return numberOfRooms;
    }


}
