package com.example.todomvvm.edittask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.todomvvm.R;
import com.example.todomvvm.database.Repository;
import com.example.todomvvm.database.TaskEntry;

import java.util.Date;
import java.util.List;

public class AddEditTaskActivity extends AppCompatActivity {

    // Extra for the task ID to be received in the intent
    public static final String EXTRA_TASK_ID = "extraTaskId";
    // Extra for the task ID to be received after rotation
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;
    // Constant for logging
    private static final String TAG = AddEditTaskActivity.class.getSimpleName();
    // Fields for views
    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;

    //edit
    TextView taskID;

    private int mTaskId = DEFAULT_TASK_ID;

    AddEditTaskViewModel viewModel;

    private EditPagerAdapter mEditPagerAdapter;
    private ViewPager mViewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

//        initViews();
//
//        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
//            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
//        }

        mEditPagerAdapter = new EditPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.ViewPager);
        Repository repository = new Repository(getApplication());
        LiveData<List<TaskEntry>> task = repository.getAllTask();
        //LiveData<Integer> noTask = repository.getTaskCount();
        for (int i=0; i < task.sum(); i++){

        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText(R.string.update_button);
            //Create Bundle
            if (mTaskId == DEFAULT_TASK_ID) {
                // populate the UI
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);
//                AddEditTaskViewModelFactory factory = new AddEditTaskViewModelFactory(getApplication(),mTaskId);
//                viewModel = ViewModelProviders.of(this, factory).get(AddEditTaskViewModel.class);
//                viewModel.getTask().observe(this, new Observer<TaskEntry>() {
//                    @Override
//                    public void onChanged(TaskEntry taskEntry) {
//                        viewModel.getTask().removeObserver(this);
//                        populateUI(taskEntry);
//                    }
//                });

            }
        }
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putInt(INSTANCE_TASK_ID, mTaskId);
//        super.onSaveInstanceState(outState);
//    }

//    /**
//     * initViews is called from onCreate to init the member variable views
//     */
//    private void initViews() {
//        mEditText = findViewById(R.id.editTextTaskDescription);
//        mRadioGroup = findViewById(R.id.radioGroup);
//
//        //edit
//        taskID = findViewById(R.id.taskID);
//
//        mButton = findViewById(R.id.updateButton);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onSaveButtonClicked();
//            }
//        });
//    }

//    /**
//     * populateUI would be called to populate the UI when in update mode
//     *
//     * @param task the taskEntry to populate the UI
//     */
//    private void populateUI(TaskEntry task) {
//        if (task == null) {
//            return;
//        } else {
//            taskID.setText(task.getId()+"");
//            mEditText.setText(task.getDescription());
//            setPriorityInViews(task.getPriority());
//        }
//
//    }

//    /**
//     * onSaveButtonClicked is called when the "save" button is clicked.
//     * It retrieves user input and inserts that new task data into the underlying database.
//     */
//    public void onSaveButtonClicked() {
//        String description = mEditText.getText().toString();
//        int priority = getPriorityFromViews();
//        Date date = new Date();
//        final TaskEntry task = new TaskEntry(description, priority, date); //needs to be final to be executed in a different thread
//        if ( mTaskId != DEFAULT_TASK_ID){
//            task.setId(mTaskId);
//            viewModel.updateTask(task);
//        }else{
//            AddEditTaskViewModel viewModelAdd = ViewModelProviders.of(this).get(AddEditTaskViewModel.class);
//            viewModelAdd.addTask(task);
//        }
//        finish(); //goes back to MainActivity
//    }

//    /**
//     * getPriority is called whenever the selected priority needs to be retrieved
//     */
//    public int getPriorityFromViews() {
//        int priority = 1;
//        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
//        switch (checkedId) {
//            case R.id.radButton1:
//                priority = PRIORITY_HIGH;
//                break;
//            case R.id.radButton2:
//                priority = PRIORITY_MEDIUM;
//                break;
//            case R.id.radButton3:
//                priority = PRIORITY_LOW;
//        }
//        return priority;
//    }

//    /**
//     * setPriority is called when we receive a task from MainActivity
//     *
//     * @param priority the priority value
//     */
//    public void setPriorityInViews(int priority) {
//        switch (priority) {
//            case PRIORITY_HIGH:
//                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
//                break;
//            case PRIORITY_MEDIUM:
//                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
//                break;
//            case PRIORITY_LOW:
//                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
//        }
//    }
}
