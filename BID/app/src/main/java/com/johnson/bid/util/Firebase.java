package com.johnson.bid.util;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Firebase {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    public static FirebaseFirestore getFirestore() {
        return db;
    }

    public static StorageReference getStorage() {
        return mStorageRef;
    }

}
