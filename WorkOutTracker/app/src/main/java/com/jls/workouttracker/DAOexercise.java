package com.jls.workouttracker;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jls.workouttracker.model.Exercise;

import java.util.HashMap;

public class DAOexercise {
    private FirebaseAuth fAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase fdb;

    public DAOexercise(boolean isPublic) {
        fAuth = FirebaseAuth.getInstance();
        fdb = FirebaseDatabase.getInstance();
        if (isPublic) {
            myRef = fdb.getReference("pworkouts");
        } else {
            myRef = fdb.getReference("user").child(fAuth.getUid()).child("wcustoms");
        }
    }

    public DAOexercise() {
        myRef = FirebaseDatabase.getInstance().getReference("pworkouts");
    }

    public Task<Void> add(Exercise ex) {
        return myRef.push().setValue(ex);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return myRef.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key) {
        return myRef.child(key).removeValue();
    }

    public Query get() {
        return myRef.orderByKey();
    }
}
