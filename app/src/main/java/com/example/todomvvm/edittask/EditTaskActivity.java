package com.example.todomvvm.edittask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.todomvvm.R;
import com.example.todomvvm.database.Repository;
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

    EditTaskViewModel viewModel;

    private EditPagerAdapter mEditPagerAdapter;
    private ViewPager mViewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        Log.d(TAG," onCreate");

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
                Log.d("Rijuedit","3. "+mTaskId);
            }
        }
        mEditPagerAdapter = new EditPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.ViewPager);
        Repository repository = new Repository(getApplication());
        LiveData<List<TaskEntry>> task = repository.getAllTask();
        task.observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(List<TaskEntry> taskEntries) {
                for (int i=0; i < taskEntries.size(); i++){
                    mEditPagerAdapter.addFragment(taskEntries.get(i).getId());
                    if (taskEntries.get(i).getId() == mTaskId){
                        position = i;
                        Log.d(TAG,"Position "+position+" has taskID "+mTaskId);
                        mViewPager.setCurrentItem(position,false);
                    }
                }
            }
        });
        mViewPager.setAdapter(mEditPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("Start","4. View Pager "+position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
