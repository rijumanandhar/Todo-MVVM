package com.example.todomvvm.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.todomvvm.R;


public class MainActivity extends AppCompatActivity /*implements TaskAdapter.ItemClickListener*/ {
    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
//    private RecyclerView mRecyclerView;
//    private TaskAdapter mAdapter;

//    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragmentDisplay = new DisplayFragment();
        Fragment fragmentAdd = new AddTaskFragment();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (MainViewModel.listDisplayFragment){
            fragmentTransaction.replace(R.id.fragment_container, fragmentDisplay);
        }else{
            fragmentTransaction.replace(R.id.fragment_container, fragmentAdd);
        }
        fragmentTransaction.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

}
