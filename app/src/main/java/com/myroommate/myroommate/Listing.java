package com.myroommate.myroommate;

/**
 * Created by G551JK-DM053H on 01-09-2017.
 */

public class Listing {
    int listingPhoto;
    String listingName;
    String ownerName;
    String subLocality;


    Listing(int listingPhoto, String listingName, String ownerName, String subLocality) {
        this.listingPhoto = listingPhoto;
        this.listingName = listingName;
        this.ownerName = ownerName;
        this.subLocality = subLocality;

    }
}