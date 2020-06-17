package com.example.todomvvm.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.todomvvm.R;
import com.example.todomvvm.database.Reminder;
import com.example.todomvvm.edittask.EditTaskActivity;
import com.example.todomvvm.database.TaskEntry;
import com.example.todomvvm.edittask.EditTaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayFragment extends Fragment implements TaskAdapter.ItemClickListener {

    // Constant for logging
    private static final String TAG = DisplayFragment.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

    MainViewModel viewModel;

    private MenuItem priorityItem, dateItem;

    public DisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_display, container, false);
        setHasOptionsMenu(true);

        MainViewModel.listDisplayFragment = true;

        // Set the RecyclerView to its corresponding view
        mRecyclerView = rootView.findViewById(R.id.recyclerViewTasks);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new TaskAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        mRecyclerView.addItemDecoration(decoration);

         /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                int position = viewHolder.getAdapterPosition();
                final TaskEntry task = mAdapter.getTasks().get(position);
                viewModel.getReminderById(task.getId()).observe(getActivity(), new Observer<Reminder>() {
                    @Override
                    public void onChanged(Reminder reminder) {
                        viewModel.getReminderById(task.getId()).removeObserver(this);
                        if (reminder != null){
                            Log.d(TAG,"Reminder "+reminder.getReminderId());
                            cancelAlarm(reminder);
                            viewModel.deleteReminder(reminder);
                        }

                    }
                });
                viewModel.deleteTask(task);
            }
        }).attachToRecyclerView(mRecyclerView);

         /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddTaskActivity.
         */
        final FloatingActionButton fabButton = rootView.findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //replace fragment
                Fragment fragment = new AddTaskFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        });

        setupViewModel();
        viewModel.showSnackBarEvent().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean  showSnackBar) {
                if (showSnackBar == true){
                    Snackbar snackbar;
                    snackbar = Snackbar.make(fabButton, "Deleted", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });


        return rootView;
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        if (viewModel.isPriorityListDisplay()){
            viewModel.getTaskByPriority().observe(getActivity(), new Observer<List<TaskEntry>>() {
                @Override
                public void onChanged(List<TaskEntry> taskEntries) {
                    Log.d(TAG,"Updating list of tasks from LiveData in ViewModel");
                    mAdapter.setTasks(taskEntries);
                }
            });
        }else{
            viewModel.getTaskByDate().observe(getActivity(), new Observer<List<TaskEntry>>() {
                @Override
                public void onChanged(List<TaskEntry> taskEntries) {
                    Log.d(TAG,"Updating list of tasks from LiveData in ViewModel");
                    mAdapter.setTasks(taskEntries);
                }
            });
        }

    }

    @Override
    public void onItemClickListener(int itemId) {
         //Launch AddTaskActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);
        intent.putExtra(EditTaskActivity.EXTRA_TASK_ID, itemId);
        EditTaskViewModel.userClick = true;
        startActivity(intent);
    }

    @Override
    public void onItemLongTapListener(String description) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,description);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an email"));
    }

    private void cancelAlarm(Reminder reminder){
        if (reminder != null){
            AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), NotificationAlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),reminder.getTaskId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        priorityItem = menu.findItem(R.id.priorityMenuItem);
        dateItem = menu.findItem(R.id.dateMenuItem);
        if (viewModel.isPriorityListDisplay()){
            displayDateMenu();
        }else{
            displayPriorityMenu();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.priorityMenuItem:
                viewModel.setPriorityListDisplay(true);
                refreshFragment();
                return true;
            case R.id.dateMenuItem:
                viewModel.setPriorityListDisplay(false);
                refreshFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayPriorityMenu()
    {
        dateItem.setVisible(false);
        priorityItem.setVisible(true);
    }

    private void displayDateMenu(){
        priorityItem.setVisible(false);
        dateItem.setVisible(true);
    }

    private void refreshFragment(){
        //replace fragment
        Fragment fragment = new DisplayFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
