package com.tutorialscache.todolist.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tutorialscache.todolist.R;
import com.tutorialscache.todolist.model.entity.TodoModel;
import com.tutorialscache.todolist.utils.Constants;
import com.tutorialscache.todolist.view.ui.adapter.TodoListAdapter;
import com.tutorialscache.todolist.viewmodel.TodoViewModel;
import java.util.List;

public class TodoActivity extends AppCompatActivity {
    TodoViewModel todoViewModel;
    RecyclerView recyclerView;
    TodoListAdapter todoListAdapter;
    FloatingActionButton floatingActionButton;
    ImageView emptyImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        emptyImage=findViewById(R.id.empty);
        //configuring recycler view
        recyclerView = findViewById(R.id.rv_todo_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        // setting adapter in recycler view
        todoListAdapter=new TodoListAdapter();
        recyclerView.setAdapter(todoListAdapter);
        //
        floatingActionButton=findViewById(R.id.fab_new_todo);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoActivity.this, CreateTodoActivity.class);
                startActivityForResult(intent, Constants.ADD_TODO);
            }
        });
        //getting access to view model class
        todoViewModel= ViewModelProviders.of(this).get(TodoViewModel.class);
        //add observer to for view model getTodolist()(LiveData)
        todoViewModel.getTodolist().observe(this, new Observer<List<TodoModel>>() {
            @Override
            public void onChanged(List<TodoModel> todoModels) {
                //to show list
                todoListAdapter.submitList(todoModels);
            }
        });

        //interface method body of list adapter
        todoListAdapter.setClickListener(new TodoListAdapter.ItemClickListener() {
            //method to delete list
            @Override
            public void onDeleteItem( final TodoModel todoModel) {
                //Alert Dialog for deleting all todo
                new AlertDialog.Builder(TodoActivity.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // to delete list
                                todoViewModel.delete(todoModel);
                                Log.d("response", "Deleted Item"+todoModel.toString()+"");
                                Toast.makeText(TodoActivity.this, "Todo deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                        .setCancelable(true)
                        .show();
            }
            //method to send all list items for edit
            @Override
            public void onEditItem(TodoModel todoModel) {
                Intent intent = new Intent(TodoActivity.this, CreateTodoActivity.class);
                intent.putExtra(Constants.EXTRA_ID, todoModel.getId());
                intent.putExtra(Constants.EXTRA_TITLE, todoModel.getTitle());
                intent.putExtra(Constants.EXTRA_DATE, todoModel.getCreated_at() );
                startActivityForResult(intent, Constants.EDIT_TODO);
            }
            //method to make item completed
            @Override
            public void onCheckItem(final TodoModel todoModel) {
                new AlertDialog.Builder(TodoActivity.this)
                        .setTitle("Task Completed")
                        .setMessage("Are you sure you have completed this todo?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //setting status 1 and updating it
                                todoModel.setStatus(1);
                                todoViewModel.update(todoModel);
                                Log.d("response", "Item Completed"+todoModel.toString()+"");
                                Toast.makeText(TodoActivity.this, "Todo Completed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setCancelable(true).show();
            }
        });
    }
    //override method to show result after inserting,updating list
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check if request code is for inserting new list then perform insertion
        if (requestCode == Constants.ADD_TODO && resultCode == RESULT_OK) {
            String title = data.getStringExtra(Constants.EXTRA_TITLE);
            String time = data.getStringExtra(Constants.EXTRA_DATE);

            TodoModel todoModel = new TodoModel(title, time);
            todoViewModel.insert(todoModel);
            Toast.makeText(this, "Todo saved", Toast.LENGTH_SHORT).show();
        }
        //check if request code is for updating list and perform updation
        else if (requestCode == Constants.EDIT_TODO && resultCode == RESULT_OK)
        {
            int id = data.getIntExtra(Constants.EXTRA_ID, -1);
            if (id == -1)
            {
                Toast.makeText(this, "Todo can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(Constants.EXTRA_TITLE);
            String time = data.getStringExtra(Constants.EXTRA_DATE);
            TodoModel todoModel = new TodoModel(title, time);
            todoModel.setId(id);
            todoViewModel.update(todoModel);
            Toast.makeText(this, "todo updated", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem item = menu.findItem(R.id.deleteall);
        //delete all option will be disbled and default image will be appeared if list will bhi empty
        todoViewModel.getPending().observe(TodoActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer==0) {
                    item.setEnabled(false);
                    recyclerView.setVisibility(View.GONE);
                    emptyImage.setVisibility(View.VISIBLE);
                }
                else {
                    item.setEnabled(true);
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyImage.setVisibility(View.GONE);
                }
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (item.getItemId()==R.id.deleteall)
        {
            //Alert Dialog for deleting all todo
            new AlertDialog.Builder(TodoActivity.this)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete all todo's?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {
                            todoViewModel.deleteAll();
                            Toast.makeText(TodoActivity.this, "All todo's deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(true).show();
        }
        if (item.getItemId()==R.id.completed)
        {
            Intent intent=new Intent(TodoActivity.this, CompletedTodoActivity.class);
            startActivity(intent);
        }
        return true;
    }
}


