package com.example.todomvvm.edittask;

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
    private final List<Integer> mFragmentId= new ArrayList<>();

    public EditPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addFragment(int title){
        mFragmentId.add(title);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return EditTaskFragment.newInstance(mFragmentId.get(position));
    }

    @Override
    public int getCount() {
        return mFragmentId.size();
    }
}
