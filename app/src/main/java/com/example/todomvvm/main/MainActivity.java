package com.example.todomvvm.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.todomvvm.R;


public class MainActivity extends AppCompatActivity /*implements TaskAdapter.ItemClickListener*/ {
    // Constant for logging
    private static final String TAG = MainActivity.class.getSimpleName();
    // Member variables for the adapter and RecyclerView
//    private RecyclerView mRecyclerView;
//    private TaskAdapter mAdapter;

//    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragmentDisplay = new DisplayFragment();
        Fragment fragmentAdd = new AddTaskFragment();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (MainViewModel.listDisplayFragment){
            fragmentTransaction.replace(R.id.fragment_container, fragmentDisplay);
        }else{
            fragmentTransaction.replace(R.id.fragment_container, fragmentAdd);
        }
        fragmentTransaction.commit();
//        // Set the RecyclerView to its corresponding view
//        mRecyclerView = findViewById(R.id.recyclerViewTasks);
//
//        // Set the layout for the RecyclerView to be a linear layout, which measures and
//        // positions items within a RecyclerView into a linear list
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Initialize the adapter and attach it to the RecyclerView
//        mAdapter = new TaskAdapter(this, this);
//        mRecyclerView.setAdapter(mAdapter);
//
//        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
//        mRecyclerView.addItemDecoration(decoration);
//
//        /*
//         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
//         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
//         and uses callbacks to signal when a user is performing these actions.
//         */
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            // Called when a user swipes left or right on a ViewHolder
//            @Override
//            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                // Here is where you'll implement swipe to delete
//                int position = viewHolder.getAdapterPosition();
//                TaskEntry task = mAdapter.getTasks().get(position);
//                viewModel.deleteTask(task);
//            }
//        }).attachToRecyclerView(mRecyclerView);
//
//        /*
//         Set the Floating Action Button (FAB) to its corresponding View.
//         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
//         to launch the AddTaskActivity.
//         */
//        final FloatingActionButton fabButton = findViewById(R.id.fab);
//
//        fabButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Create a new intent to start an AddTaskActivity
//                Intent addTaskIntent = new Intent(MainActivity.this, AddEditTaskActivity.class);
//                startActivity(addTaskIntent);
//            }
//        });
//
//        setupViewModel();
//        viewModel.showSnackBarEvent().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean  showSnackBar) {
//                if (showSnackBar == true){
//                    Snackbar.make(fabButton, "Deleted",Snackbar.LENGTH_LONG).show();
//                }
//            }
//        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

//    private void setupViewModel() {
//        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        viewModel.getTask().observe(this, new Observer<List<TaskEntry>>() {
//            @Override
//            public void onChanged(List<TaskEntry> taskEntries) {
//                Log.d(TAG,"Updating list of tasks from LiveData in ViewModel");
//                mAdapter.setTasks(taskEntries);
//            }
//        });
//    }

//    @Override
//    public void onItemClickListener(int itemId) {
//        // Launch AddTaskActivity adding the itemId as an extra in the intent
//        Intent intent = new Intent(this, AddEditTaskActivity.class);
//        intent.putExtra(AddEditTaskActivity.EXTRA_TASK_ID, itemId);
//        startActivity(intent);
//    }
}
