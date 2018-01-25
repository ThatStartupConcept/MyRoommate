package com.myroommate.myroommate;

/**
 * Created by G551JK-DM053H on 01-09-2017.
 */

public class Listing {
    int listingPhoto;
    String primary_key;
    String location;
    String locality;
    String listingName;
    String ownerName;
    String address;
    String subLocality;
    String pincode;
    Integer rent;


    Listing(int listingPhoto, String primary_key, String location, String locality, String listingName, String ownerName, String address, String subLocality, String pincode, Integer rent) {
        this.listingPhoto = listingPhoto;
        this.primary_key = primary_key;
        this.location = location;
        this.locality = locality;
        this.listingName = listingName;
        this.ownerName = ownerName;
        this.address = address;
        this.subLocality = subLocality;
        this.pincode = pincode;
        this.rent = rent;

    }
}