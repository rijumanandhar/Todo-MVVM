package com.example.todomvvm.loginsignup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.todomvvm.database.Repository;

public class LoginSignUpViewModel extends AndroidViewModel {

    //constant to track which fragment is displayed
    public static boolean loginDisplayFragment = true;

    Repository repository;

    public LoginSignUpViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void insertUser(String uid,String name, String email, String username, String picture){
        repository.insertUserData(uid,name,email,username,picture);
    }
}
