package com.example.todomvvm.edittask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.todomvvm.R;
import com.example.todomvvm.database.TaskEntry;

import java.util.List;

public class EditTaskActivity extends AppCompatActivity {

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Constant for default task id to be used when not in update mode
    public static final int DEFAULT_TASK_ID = -1;
    // Constant for logging
    private static final String TAG = EditTaskActivity.class.getSimpleName();

    private int mTaskId = DEFAULT_TASK_ID, position = 0;

    private EditPagerAdapter mEditPagerAdapter;
    private ViewPager mViewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Log.d(TAG," onCreate");
        receiveIntent();

        mEditPagerAdapter = new EditPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.ViewPager);

        LiveData<List<TaskEntry>> task = new EditTaskViewModel(getApplication()).getAllTasks();
        task.observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(List<TaskEntry> taskEntries) {
                for (int i=0; i < taskEntries.size(); i++){
                    mEditPagerAdapter.addFragment(taskEntries.get(i).getId());
                    if (taskEntries.get(i).getId() == mTaskId){
                        position = i;
                        mViewPager.setCurrentItem(position,false);
                    }
                }
            }
        });
        mViewPager.setAdapter(mEditPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //set updated taskTd in viewmodel
                EditTaskViewModel.viewModelTaskId = mEditPagerAdapter.getIdFromPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Receives intent and sets mTaskId
     * **/
    public void receiveIntent(){
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            if (EditTaskViewModel.userClick) {
                // populate the UI
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);
                EditTaskViewModel.viewModelTaskId = mTaskId;
                EditTaskViewModel.userClick = false;
                Log.d(TAG," get Intent "+mTaskId);
            }else{
                mTaskId = EditTaskViewModel.viewModelTaskId;
            }
        }
    }
}
