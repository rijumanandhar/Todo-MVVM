package com.example.todomvvm.main;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.todomvvm.R;
import com.example.todomvvm.database.TaskEntry;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment {

    // Constant for default task id to be used when not in update mode
    private static final int DEFAULT_TASK_ID = -1;

    // Constant for logging
    private static final String TAG = AddTaskFragment.class.getSimpleName();

    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    // Fields for views
    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;
    Switch mSwitch;
    TextView mTextView;

    //edit
    TextView taskID;

    private int mTaskId = DEFAULT_TASK_ID;

    MainViewModel viewModelAdd;

    public AddTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);
        MainViewModel.listDisplayFragment = false;
        viewModelAdd = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        initViews(rootView);
        return rootView;
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews(final View rootView) {
        mEditText = rootView.findViewById(R.id.editTextTaskDescription);
        mRadioGroup = rootView.findViewById(R.id.radioGroup);
        mSwitch = rootView.findViewById(R.id.reminderSetSwitch);
        mTextView = rootView.findViewById(R.id.remiderTextView);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    viewModelAdd.setReminder(true);

                }
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                    viewModelAdd.setDescription(mEditText.getText().toString());
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radButton1:
                        viewModelAdd.setPriority(PRIORITY_HIGH);
                        setPriorityInViews(PRIORITY_HIGH,rootView);
                        break;
                    case R.id.radButton2:
                        viewModelAdd.setPriority(PRIORITY_MEDIUM);
                        setPriorityInViews(PRIORITY_MEDIUM,rootView);
                        Log.d(TAG,"Priority set to medium : "+viewModelAdd.getPriority());
                        break;
                    case R.id.radButton3:
                        viewModelAdd.setPriority(PRIORITY_LOW);
                        setPriorityInViews(PRIORITY_LOW,rootView);
                        Log.d(TAG,"Priority set to medium : "+viewModelAdd.getPriority());
                }
            }
        });

        mButton = rootView.findViewById(R.id.updateButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });

        Log.d(TAG,"Getting priority from viewmodel : "+viewModelAdd.getPriority());
        setPriorityInViews(viewModelAdd.getPriority(),rootView);
        if (viewModelAdd.getDescription() != null){
            mEditText.setText(viewModelAdd.getDescription());
        }
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
                rootView.findViewById(R.id.radButton1).setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                rootView.findViewById(R.id.radButton2).setBackgroundColor(Color.TRANSPARENT);
                rootView.findViewById(R.id.radButton3).setBackgroundColor(Color.TRANSPARENT);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton2);
                rootView.findViewById(R.id.radButton2).setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                rootView.findViewById(R.id.radButton1).setBackgroundColor(Color.TRANSPARENT);
                rootView.findViewById(R.id.radButton3).setBackgroundColor(Color.TRANSPARENT);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton3);
                rootView.findViewById(R.id.radButton3).setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                rootView.findViewById(R.id.radButton2).setBackgroundColor(Color.TRANSPARENT);
                rootView.findViewById(R.id.radButton1).setBackgroundColor(Color.TRANSPARENT);
        }
    }

}