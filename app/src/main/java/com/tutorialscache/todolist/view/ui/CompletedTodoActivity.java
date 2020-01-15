package com.tutorialscache.todolist.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.tutorialscache.todolist.R;
import com.tutorialscache.todolist.model.entity.TodoModel;
import com.tutorialscache.todolist.view.ui.adapter.CompletedListAdapter;
import com.tutorialscache.todolist.viewmodel.TodoViewModel;
import java.util.List;

public class CompletedTodoActivity extends AppCompatActivity {
    TodoViewModel todoViewModel;
    RecyclerView recyclerView;
    CompletedListAdapter completedListAdapter;
    ImageView emptyImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_todo);
        emptyImage=findViewById(R.id.empty);
        //RecyclerVeiw configuration
        recyclerView = findViewById(R.id.tv_item_completed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        //setting recyclerView to listAdapter
        completedListAdapter=new CompletedListAdapter();
        recyclerView.setAdapter(completedListAdapter);
        //to show enable home back button
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        setTitle("Completed Todo");

        //getting access to view model class
        todoViewModel= ViewModelProviders.of(this).get(TodoViewModel.class);
        todoViewModel.getCompletedTasks().observe(this, new Observer<List<TodoModel>>() {
            @Override
            public void onChanged(List<TodoModel> todoModels) {
                //to show list
                completedListAdapter.submitList(todoModels);
            }
        });
        todoViewModel.getCompleted().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer==0)
                {
                    recyclerView.setVisibility(View.GONE);
                    emptyImage.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyImage.setVisibility(View.GONE);
                }
            }
        });
        //interface method body of list adapter
        completedListAdapter.setCompletedListner(new CompletedListAdapter.ItemCompletedListener() {
            @Override
            public void onDeleteItem(final TodoModel todoModel) {
                //Alert Dialog for deleting all todo
                new AlertDialog.Builder(CompletedTodoActivity.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                todoViewModel.delete(todoModel);
                                Log.d("response", "Deleted Item"+todoModel.toString()+"");
                                Toast.makeText(CompletedTodoActivity.this, "Todo deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(true).show();
            }

            @Override
            public void onUndoItem(final TodoModel todoModel) {
                //Alert Dialog for undo a todo
                new AlertDialog.Builder(CompletedTodoActivity.this)
                        .setTitle("Undo")
                        .setMessage("Are you sure you want to Undo?")
                        .setPositiveButton("Undo", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                todoModel.setStatus(0);
                                todoViewModel.update(todoModel);
                                Log.d("response", "Deleted Item"+todoModel.toString()+"");
                                Toast.makeText(CompletedTodoActivity.this, "Todo deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(true).show();

            }
        });

    }



}


