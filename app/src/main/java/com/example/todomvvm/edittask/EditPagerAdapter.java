package com.example.todomvvm.edittask;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LiveData;

import com.example.todomvvm.database.Repository;
import com.example.todomvvm.database.TaskDao;
import com.example.todomvvm.database.TaskEntry;

import java.util.ArrayList;
import java.util.List;

public class EditPagerAdapter extends FragmentPagerAdapter {
    // Constant for logging
    private static final String TAG = EditPagerAdapter.class.getSimpleName();
    private final List<Integer> mFragmentId= new ArrayList<>();

    public EditPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        Log.d(TAG," Created");
    }

    public void addFragment(int title){
        mFragmentId.add(title);
        notifyDataSetChanged();
        Log.d(TAG," addFragment Implemented");
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG," Fragment instance returned "+position);
        return EditTaskFragment.newInstance(mFragmentId.get(position));
    }

    @Override
    public int getCount() {
        Log.d(TAG," Fragment size returned "+mFragmentId.size());
        return mFragmentId.size();
    }
}
