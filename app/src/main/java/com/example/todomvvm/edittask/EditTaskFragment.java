package com.example.todomvvm.edittask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.todomvvm.R;
import com.example.todomvvm.database.Reminder;
import com.example.todomvvm.database.TaskEntry;
import com.example.todomvvm.main.MainActivity;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTaskFragment extends Fragment {

    // Constant for logging
    private static final String TAG = EditTaskFragment.class.getSimpleName();

    // Constants for priority
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    // Fields for views
    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mUpdateButton;
    Switch mSwitch;
    TextView mTextView;
    Button mBackButton;

    Context mContext;

    private int mTaskId;

    EditTaskViewModel viewModel;

    //edit
    TextView taskID;

    View rootView;

    public static final String ARGS_NAME = "EditTaskFragment";

    public EditTaskFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int mTaskId){
        Bundle args = new Bundle();
        args.putInt(ARGS_NAME, mTaskId);
        EditTaskFragment fragment = new EditTaskFragment();
        fragment.setArguments(args);
        Log.d(TAG," Fragment Instance created with "+mTaskId);
        return  fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_task, container, false);
        initViews();
        Log.d(TAG," onCreate");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null){
            //get mTaskId from bundle
            mTaskId = args.getInt(ARGS_NAME);
            Log.d("RijuStart","1. "+mTaskId);
            //EditTaskViewModel.mTaskId = mTaskId;
            EditTaskViewModelFactory factory = new EditTaskViewModelFactory(getActivity().getApplication(),mTaskId);
            viewModel = ViewModelProviders.of(this, factory).get(EditTaskViewModel.class);
            viewModel.getTask().observe(getActivity(), new Observer<TaskEntry>() {
                @Override
                public void onChanged(final TaskEntry taskEntry) {
                    viewModel.getTask().removeObserver(this);
                    try{
                        viewModel.getReminder(taskEntry.getId()).observe(getActivity(), new Observer<Reminder>() {
                            @Override
                            public void onChanged(Reminder reminder) {
                                viewModel.getReminder(taskEntry.getId()).removeObserver(this);
                                populateUI(taskEntry,reminder);
                                //Log.d(TAG,"TaskID: "+taskEntry.getId()+" ReminderTaskId: "+reminder.getTaskId());
                            }
                        });

                    }catch (NullPointerException e){
                        populateUI(taskEntry,null);
                    }


                }
            });
            Log.d(TAG," onStart viewModel implemented");
        }
    }

    /**
     * initViews is called from onCreate to init the member variable views
     */
    private void initViews() {
        mEditText = rootView.findViewById(R.id.editTextTaskDescription);
        mRadioGroup = rootView.findViewById(R.id.radioGroup);
        mSwitch = rootView.findViewById(R.id.reminderSetSwitch);
        mTextView = rootView.findViewById(R.id.remiderTextView);
        mBackButton = rootView.findViewById(R.id.backButton);
        mUpdateButton = rootView.findViewById(R.id.addButton);

        //edit
        taskID = rootView.findViewById(R.id.taskID);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radButton1:
                        setPriorityInViews(PRIORITY_HIGH);
                        break;
                    case R.id.radButton2:
                        setPriorityInViews(PRIORITY_MEDIUM);
                        break;
                    case R.id.radButton3:
                        setPriorityInViews(PRIORITY_LOW);
                }
            }
        });

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdateButtonClicked();
            }
        });
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void  onUpdateButtonClicked() {
        String description = mEditText.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();
        final TaskEntry task = new TaskEntry(description, priority, date); //needs to be final to be executed in a different thread
        task.setId(mTaskId);
        viewModel.updateTask(task);

        //go back to MainActivity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private void populateUI(TaskEntry task, Reminder reminder) {
        if (task == null && reminder == null) {
            return;
        }else if (task != null && reminder == null) {
            taskID.setText(task.getId()+"");
            mEditText.setText(task.getDescription());
            setPriorityInViews(task.getPriority());
        }
        else {
            taskID.setText(task.getId()+"");
            mEditText.setText(task.getDescription());
            setPriorityInViews(task.getPriority());
            mSwitch.setChecked(true);
            mTextView.setText("Reminder Set At"+reminder.getRemindDate().toString());
        }

    }

    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */
    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) rootView.findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton1);
                rootView.findViewById(R.id.radButton1).setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryLight));
                Log.d("Color","Priority High");
                rootView.findViewById(R.id.radButton2).setBackgroundColor(Color.TRANSPARENT);
                rootView.findViewById(R.id.radButton3).setBackgroundColor(Color.TRANSPARENT);
                //rootView.findViewById(R.id.radButton1).setBackgroundColor(Color.rgb(255,236,221));
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton2);
                Log.d("Color","Priority Medium");
                rootView.findViewById(R.id.radButton2).setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                rootView.findViewById(R.id.radButton1).setBackgroundColor(Color.TRANSPARENT);
                rootView.findViewById(R.id.radButton3).setBackgroundColor(Color.TRANSPARENT);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton3);
                Log.d("Color","Priority Low");
                rootView.findViewById(R.id.radButton3).setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                rootView.findViewById(R.id.radButton2).setBackgroundColor(Color.TRANSPARENT);
                rootView.findViewById(R.id.radButton1).setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
