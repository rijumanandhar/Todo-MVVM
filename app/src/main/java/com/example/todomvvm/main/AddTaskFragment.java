package com.example.todomvvm.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
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

    AddTaskViewModel viewModelAdd;

    public AddTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);
        DisplayViewModel.isDisplay = false;
        viewModelAdd = ViewModelProviders.of(this).get(AddTaskViewModel.class);
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
                        viewModelAdd.setPriority(PRIORITY_HIGH);
                        break;
                    case R.id.radButton2:
                        viewModelAdd.setPriority(PRIORITY_MEDIUM);
                        Log.d(TAG,"Priority set to medium : "+viewModelAdd.getPriority());
                        break;
                    case R.id.radButton3:
                        viewModelAdd.setPriority(PRIORITY_LOW);
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

        Log.d(TAG,"Getting priority from viewmodel : "+viewModelAdd.getPriority());
        setPriorityInViews(viewModelAdd.getPriority(),rootView);
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked() {
        String description = mEditText.getText().toString();
        int priority = viewModelAdd.getPriority();
        Date date = new Date();
        final TaskEntry task = new TaskEntry(description, priority, date); //needs to be final to be executed in a different thread
        viewModelAdd.addTask(task);

        //replace fragment
        Fragment fragment = new DisplayFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    public void setPriorityInViews(int priority, View rootView) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }

}
