package com.example.todomvvm.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.todomvvm.R;
import com.example.todomvvm.edittask.AddEditTaskActivity;
import com.example.todomvvm.edittask.AddEditTaskViewModel;
import com.example.todomvvm.database.TaskEntry;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment {
    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;

    // Constant for logging
    private static final String TAG = AddEditTaskActivity.class.getSimpleName();

    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    // Fields for views
    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;

    //edit
    TextView taskID;

    private int mTaskId = DEFAULT_TASK_ID;

    AddEditTaskViewModel viewModel;

    public AddTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);
        MainViewModel.isDisplay = false;
        initViews(rootView);
        return rootView;
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews(View rootView) {
        mEditText = rootView.findViewById(R.id.editTextTaskDescription);
        mRadioGroup = rootView.findViewById(R.id.radioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radButton1:
                        MainViewModel.priority = PRIORITY_HIGH;
                        break;
                    case R.id.radButton2:
                        MainViewModel.priority = PRIORITY_MEDIUM;
                        break;
                    case R.id.radButton3:
                        MainViewModel.priority = PRIORITY_LOW;
                }
            }
        });

        //edit
        taskID = rootView.findViewById(R.id.taskID);

        mButton = rootView.findViewById(R.id.updateButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked() {
        String description = mEditText.getText().toString();
        int priority = MainViewModel.priority;
        Date date = new Date();
        final TaskEntry task = new TaskEntry(description, priority, date); //needs to be final to be executed in a different thread
        AddEditTaskViewModel viewModelAdd = ViewModelProviders.of(this).get(AddEditTaskViewModel.class);
        viewModelAdd.addTask(task);

        //go back to MainActivity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */
    public int getPriorityFromViews(View rootView) {
        int checkedId = ((RadioGroup) rootView.findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                MainViewModel.priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                MainViewModel.priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                MainViewModel.priority = PRIORITY_LOW;
        }
        return MainViewModel.priority;
    }
}
