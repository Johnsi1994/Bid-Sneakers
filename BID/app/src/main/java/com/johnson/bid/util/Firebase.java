package com.johnson.bid.util;

import com.google.firebase.firestore.FirebaseFirestore;

public class Firebase {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static FirebaseFirestore getFirestore() {
        return db;
    }

}
