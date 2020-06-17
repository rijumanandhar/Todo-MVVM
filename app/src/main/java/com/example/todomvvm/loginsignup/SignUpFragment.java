package com.example.todomvvm.loginsignup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.todomvvm.R;
import com.example.todomvvm.main.MainActivity;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private final String TAG = SignUpFragment.class.getSimpleName();

    private Button signUpBtn;
    private EditText nameText, usernameText,emailText, passwordText, cPasswordText;

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    LoginSignUpViewModel viewModel;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LoginSignUpViewModel.loginDisplayFragment = false;
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mAuth = FirebaseAuth.getInstance();

        viewModel = viewModel = ViewModelProviders.of(getActivity()).get(LoginSignUpViewModel.class);

        progressDialog = new ProgressDialog(getActivity());

        signUpBtn = rootView.findViewById(R.id.signUpButton);

        nameText = rootView.findViewById(R.id.nameText);
        usernameText = rootView.findViewById(R.id.userNameText);
        emailText = rootView.findViewById(R.id.emailText);
        passwordText =  rootView.findViewById(R.id.passwordText);
        cPasswordText = rootView.findViewById(R.id.cPasswordText);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUp();
            }
        });
        return rootView;
    }

    public void startSignUp(){
        final String name = nameText.getText().toString().trim();
        final String username = usernameText.getText().toString().trim();
        final String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String cPassword = cPasswordText.getText().toString().trim();

        //validation
        if (validate() == "success"){
            Log.d(TAG,"validate vitra");
            progressDialog.setMessage("Signing Up");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String user_id = mAuth.getCurrentUser().getUid();
                    Log.d(TAG,"user_id "+user_id);
                    viewModel.insertUser(user_id,name,email,username,"default");
                    progressDialog.dismiss();

                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            });
        }
    }

    public String validate (){
        return "success";
    }
}
