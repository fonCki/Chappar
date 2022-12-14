package com.example.chappar10.data;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


// This is the only class making use of the Firebase Auth library
public class AccessAuth {
    private static AccessAuth instance;
    private final FirebaseAuth firebaseAuth;

    private AccessAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static synchronized AccessAuth getInstance() {
        if (instance == null) {
            synchronized (AccessAuth.class) {
                if (instance == null) {
                    instance = new AccessAuth();
                }
            }
        }
        return instance;
    }


    //Sign in with email and password and return the user id
    public Task<String> signIn(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password).continueWith(task -> {
            if (task.isSuccessful()) {
                return firebaseAuth.getCurrentUser().getUid();
            } else {
                throw task.getException();
            }
        });
    }

    //Create a new user with the email and password and return the user id
    public Task<String> signUp(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).continueWith(task -> {
            if (task.isSuccessful()) {
                return firebaseAuth.getCurrentUser().getUid();
            } else {
                throw task.getException();
            }
        });
    }

    public String getMyUserID() {
        if (firebaseAuth.getCurrentUser() != null) {
            return firebaseAuth.getCurrentUser().getUid();
        } else {
            return null;
        }
    }

    public Task updatePassword(String newPassword) {
        return firebaseAuth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("AccessAuth", "updatePassword: success");
            } else {
                Log.i("AccessAuth", "updatePassword: failure");
            }
        });
    }

    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public Task deleteMyUser() {
        return firebaseAuth.getCurrentUser().delete();
    }
}