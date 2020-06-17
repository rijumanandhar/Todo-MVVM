package com.example.todomvvm.main;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static androidx.core.content.ContextCompat.getSystemService;

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
    TextView mDateView;
    Button mBackButton;
    Context mContext;

    MainViewModel viewModelAdd;

    public AddTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);
        MainViewModel.listDisplayFragment = false;
        viewModelAdd = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        viewModelAdd.getMaximumId().observe(getActivity(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                viewModelAdd.getMaximumId().removeObserver(this);
                if (integer != null){
                    viewModelAdd.setMaxTaskId(integer);
                }
            }
        });
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
        mDateView = rootView.findViewById(R.id.remiderDateView);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    viewModelAdd.setReminder(true);
                    mTextView.setVisibility(View.VISIBLE);
                    mDateView.setVisibility(View.VISIBLE);
                    selectDate();
                    if (viewModelAdd.getRemindDate() != null){
                       updateTimeText(viewModelAdd.getRemindDate());
                    }
                }else{
                    viewModelAdd.setReminder(false);
                    viewModelAdd.setRemindDate(null);
                    mTextView.setVisibility(View.INVISIBLE);
                    mDateView.setVisibility(View.INVISIBLE);
                }
            }
        });

        mDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModelAdd.setReminder(true);
                selectDate();
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
                        break;
                    case R.id.radButton3:
                        viewModelAdd.setPriority(PRIORITY_LOW);
                        setPriorityInViews(PRIORITY_LOW,rootView);
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
        int taskID= viewModelAdd.getMaxTaskId()+1;
        Date date = new Date();
        TaskEntry task = new TaskEntry(taskID,description, priority, date); //needs to be final to be executed in a different thread
        viewModelAdd.addTask(task);
        if (viewModelAdd.isReminder()&&viewModelAdd.getRemindDate()!=null){
            //Log.d(TAG,"Reminder true and added");
            Reminder reminder = new Reminder(0,viewModelAdd.getRemindDate()); //instantiate
            viewModelAdd.addReminder(task,reminder);
            startAlarm(reminder,task);
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
                            viewModelAdd.setRemindDate(newDate.getTime());
                            updateTimeText(viewModelAdd.getRemindDate());
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
     * updateTimeText is called to update the textview with selected date
     *
     * @param date date selected by the user
     */
    public void updateTimeText(Date date){
        String date_format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.ENGLISH).format(date);
        mDateView.setText(date_format);
    }

    public void startAlarm(Reminder reminder, TaskEntry task){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        calendar.setTime(reminder.getRemindDate());
        calendar.set(Calendar.SECOND,0);

        //go to notifier alarm
        Intent intent = new Intent(getActivity(),NotificationAlertReceiver.class);
        intent.putExtra("Message",task.getDescription());
        intent.putExtra("RemindDate",reminder.getRemindDate().toString());
        intent.putExtra("id",task.getId());

        PendingIntent pendingIn = PendingIntent.getBroadcast(getActivity(),task.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIn);
    }

}