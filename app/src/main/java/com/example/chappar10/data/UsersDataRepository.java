package com.example.chappar10.data;

import android.net.Uri;

import com.example.chappar10.model.Location;
import com.example.chappar10.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class UsersDataRepository {
    private static UsersDataRepository instance;
    private UserListLiveData userListLiveData;
//    private ChatLiveData chatLiveData;

    private DatabaseReference usersDBRef;
    private StorageReference storageReference;

    private UsersDataRepository(){
        usersDBRef = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference().child("profile");
        userListLiveData = new UserListLiveData(usersDBRef);
    }


    public static synchronized UsersDataRepository getInstance() {
        if (instance == null)
            instance = new UsersDataRepository();
        return instance;
    }


    public void addLocation(String userId, Location location) {
        usersDBRef.child(userId).child("location").setValue(location);
    }

    public void addUser(User user) {
        usersDBRef.child(user.getUid()).setValue(user);
    }

    public void uploadProfilePicture(Uri uri, String uid) {
        storageReference.child(uid).child("profile_photo").putFile(uri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uriFinal -> {
                usersDBRef.child(uid).child("profileurl").setValue(uriFinal.toString());
            });
        });
    }

    public void updateStatus(String userId, User.Status status) {
        usersDBRef.child(userId).child("status").setValue(status);
    }

    public UserListLiveData getUsersListLiveData() {
        return userListLiveData;
    }


    public UserLiveData getUserLiveData(String uid) {
        return new UserLiveData(uid);
    }

//    public ChatLiveData getChatLiveData(String userId) {
////        return new ChatLiveData(userId);
//    }

}