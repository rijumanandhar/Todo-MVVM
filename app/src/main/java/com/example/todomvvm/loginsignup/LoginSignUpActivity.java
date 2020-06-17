package com.example.todomvvm.loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.todomvvm.R;
import com.example.todomvvm.main.AddTaskFragment;
import com.example.todomvvm.main.DisplayFragment;
import com.example.todomvvm.main.MainViewModel;

public class LoginSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);
        Fragment fragmentLogin = new LoginFragment();
        Fragment fragmentSignUp = new SignUpFragment();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (LoginSignUpViewModel.loginDisplayFragment){
            fragmentTransaction.replace(R.id.loginFrameLayout, fragmentLogin);
        }else{
            fragmentTransaction.replace(R.id.loginFrameLayout, fragmentSignUp);
        }
        fragmentTransaction.commit();
    }
}
