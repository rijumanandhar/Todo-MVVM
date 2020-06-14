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
import com.example.todomvvm.database.Reminder;
import com.example.todomvvm.database.TaskEntry;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment {

    // Constant for logging
    private static final String TAG = AddTaskFragment.class.getSimpleName();

    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    // Fields for views
    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mAddButton;
    Switch mSwitch;
    TextView mTextView;
    Button mBackButton;

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
        mBackButton = rootView.findViewById(R.id.backButton);
        mAddButton = rootView.findViewById(R.id.addButton);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    viewModelAdd.setReminder(true);
                    Date date = new Date();
                    viewModelAdd.setRemindDate(date);
                    mTextView.setText("Alarm set to "+viewModelAdd.getRemindDate().toString());
                    mTextView.setVisibility(View.VISIBLE);
                }else{
                    viewModelAdd.setReminder(false);
                    viewModelAdd.setRemindDate(null);
                    mTextView.setVisibility(View.INVISIBLE);
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

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClicked();
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetViewModel();
                replaceFragment();
            }
        });

        setPriorityInViews(viewModelAdd.getPriority(),rootView);
        if (viewModelAdd.getDescription() != null){
            mEditText.setText(viewModelAdd.getDescription());
        }

        if (viewModelAdd.isReminder()){
            mSwitch.setChecked(true);
            mTextView.setText("Alarm set to "+viewModelAdd.getRemindDate().toString());
        }
    }

    /**
     * onAddButtonClicked is called when the "Add" button is clicked.
     * It retrieves user input and inserts that new task data and reminder into the underlying database.
     */
    public void onAddButtonClicked() {
        String description = mEditText.getText().toString();
        int priority = viewModelAdd.getPriority();
        Date date = new Date();
        TaskEntry task = new TaskEntry(description, priority, date); //needs to be final to be executed in a different thread
        viewModelAdd.addTask(task);
        if (viewModelAdd.isReminder()){
            Log.d(TAG,"Reminder true and added");
            Reminder reminder = new Reminder(0,viewModelAdd.getRemindDate(),"set"); //instantiate
            viewModelAdd.addReminder(task,reminder);
        }
        resetViewModel();
        replaceFragment();
    }

    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     * @param rootView
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

    /**
     * resetViewModel is called to set all the attributes to default
     *
     */
    public void resetViewModel(){
        viewModelAdd.setDescription(null);
        viewModelAdd.setPriority(1);
        viewModelAdd.setReminder(false);
        viewModelAdd.setRemindDate(null);
    }

    /**
     * replaceFragment is called to replace AddFragment to DisplayFragment
     *
     */
    public void replaceFragment(){
        //replace fragment
        Fragment fragment = new DisplayFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}