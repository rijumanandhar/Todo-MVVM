package com.example.todomvvm.edittask;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todomvvm.R;
import com.example.todomvvm.database.Reminder;
import com.example.todomvvm.database.TaskEntry;
import com.example.todomvvm.main.MainActivity;
import com.example.todomvvm.main.NotificationAlertReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
    TextView mDateView;
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

            EditTaskViewModelFactory factory = new EditTaskViewModelFactory(getActivity().getApplication(),mTaskId);
            viewModel = ViewModelProviders.of(this, factory).get(EditTaskViewModel.class);
            viewModel.getTask().observe(getActivity(), new Observer<TaskEntry>() {
                @Override
                public void onChanged(final TaskEntry taskEntry) {
                    viewModel.getTask().removeObserver(this);
                    populateTaskUI(taskEntry);
                }
            });

            viewModel.getReminder(mTaskId).observe(getActivity(), new Observer<Reminder>() {
                @Override
                public void onChanged(Reminder reminder) {
                    if (reminder != null){
                        viewModel.setReminderId(reminder.getReminderId());
                        Log.d("Riju","1. getReminderID"+reminder.getReminderId());
                        populateReminderUI(reminder);
                    }
                }
            });
            Log.d(TAG," onStart viewModel implemented");
        }
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private void populateTaskUI(TaskEntry task) {
        if (task == null) {
            return;
        }else {
            taskID.setText(task.getId()+"");
            mEditText.setText(task.getDescription());
            setPriorityInViews(task.getPriority());
        }
    }

    private void populateReminderUI(Reminder reminder){
        if (reminder == null){
            return;
        }else{
            viewModel.setReminderBool(true);
            mSwitch.setChecked(true);
            mTextView.setVisibility(View.VISIBLE);
            updateTimeText(reminder.getRemindDate());
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
        mDateView = rootView.findViewById(R.id.remiderDateView);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceView();
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (viewModel.isReminder()){
                        if(viewModel.getUpdateDate()!=null){
                            updateTimeText(viewModel.getUpdateDate());
                        }
                    }else{
                        mTextView.setVisibility(View.VISIBLE);
                        mDateView.setVisibility(View.VISIBLE);
                    }
                }else{
                    viewModel.setReminderBool(false);
                    viewModel.setUpdateDate(null);
                    mTextView.setVisibility(View.INVISIBLE);
                    mDateView.setVisibility(View.INVISIBLE);
                }
            }
        });

        mDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setReminderBool(true);
                selectDate();
            }
        });

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
        TaskEntry task = new TaskEntry(description, priority, date); //needs to be final to be executed in a different thread
        task.setId(mTaskId);
        viewModel.updateTask(task);
        if(viewModel.getUpdateDate()!=null){
            if (viewModel.getReminderId() != 0){
                Reminder reminder = new Reminder(viewModel.getReminderId(),mTaskId,viewModel.getUpdateDate());
                viewModel.updateReminder(reminder);
                cancelAlarm(task);
                startAlarm(reminder,task);
            }else{
                Reminder reminder = new Reminder(mTaskId,viewModel.getUpdateDate());
                viewModel.insertReminder(reminder);
                startAlarm(reminder,task);
            }

            updateTimeText(viewModel.getUpdateDate());
        }else if (viewModel.getReminderId() != 0 && viewModel.getUpdateDate()==null && !viewModel.isReminder()){
            cancelAlarm(task);
        }
        resetViewModel();
        replaceView();
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
                rootView.findViewById(R.id.radButton2).setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryLight));
                rootView.findViewById(R.id.radButton1).setBackgroundColor(Color.TRANSPARENT);
                rootView.findViewById(R.id.radButton3).setBackgroundColor(Color.TRANSPARENT);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) rootView.findViewById(R.id.radioGroup)).check(R.id.radButton3);
                Log.d("Color","Priority Low");
                rootView.findViewById(R.id.radButton3).setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryLight));
                rootView.findViewById(R.id.radButton2).setBackgroundColor(Color.TRANSPARENT);
                rootView.findViewById(R.id.radButton1).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /**
     * selectDate to collect user selected date and time
     *
     */
    public void selectDate(){
        final Calendar newCalender = Calendar.getInstance();
        DatePickerDialog dialogWithDate = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                final Calendar newDate = Calendar.getInstance();
                Calendar newTime = Calendar.getInstance();
                TimePickerDialog time = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        newDate.set(year,month,dayOfMonth,hourOfDay,minute,0);
                        Calendar tem = Calendar.getInstance();
                        Log.w("TIME",System.currentTimeMillis()+"");
                        if(newDate.getTimeInMillis()-tem.getTimeInMillis()>0){
                            viewModel.setUpdateDate(newDate.getTime());
                            updateTimeText(newDate.getTime());
                        }
                        else
                            Toast.makeText(getActivity(),"Invalid time",Toast.LENGTH_SHORT).show();

                    }
                },newTime.get(Calendar.HOUR_OF_DAY),newTime.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(getActivity()));
                time.show();
            }
        },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));

        dialogWithDate.getDatePicker().setMinDate(System.currentTimeMillis());
        dialogWithDate.show();
    }

    /**
     * replaceView is called to go back to MainActivity
     *
     */
    public void replaceView(){
        //go back to MainActivity
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * updateTimeText is called to update the textview with selected date
     *
     * @param date date selected by the user
     */
    public void updateTimeText(Date date){
        String date_format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.ENGLISH).format(date);
        mDateView.setText(date_format);
        mDateView.setVisibility(View.VISIBLE);
    }

    public void startAlarm(Reminder reminder, TaskEntry task){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        calendar.setTime(reminder.getRemindDate());
        calendar.set(Calendar.SECOND,0);

        //go to notifier alarm
        Intent intent = new Intent(getActivity(), NotificationAlertReceiver.class);
        intent.putExtra("Message",task.getDescription());
        intent.putExtra("RemindDate",reminder.getRemindDate().toString());
        intent.putExtra("id",task.getId());

        PendingIntent pendingIn = PendingIntent.getBroadcast(getActivity(),task.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIn);
    }

    private void cancelAlarm(TaskEntry task){
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), NotificationAlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),task.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public void resetViewModel(){
        viewModel.setReminderBool(false);
        viewModel.setReminderId(0);
        viewModel.setUpdateDate(null);
    }
}
