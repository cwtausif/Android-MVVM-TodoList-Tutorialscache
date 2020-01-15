package com.tutorialscache.todolist.viewmodel;

import android.app.Application;
import com.tutorialscache.todolist.model.TodoRepository;
import com.tutorialscache.todolist.model.entity.TodoModel;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TodoViewModel extends AndroidViewModel {
    private TodoRepository repository;
    private LiveData<List<TodoModel>> todolist,completedTasks;
    private LiveData<Integer> completed,pending;

    //constructor of todoviewModel
    public TodoViewModel(@NonNull Application application)
    {
        super(application);
        repository=new TodoRepository(application);
        todolist=repository.getAllNotes();
        completedTasks=repository.getCompletedTodo();
        completed=repository.getCompleted();
        pending=repository.getPending();
    }

    //our activities will use these methods
    public void insert(TodoModel todoModel)
    {
        repository.insert(todoModel);
    }

    public void delete(TodoModel todoModel)
    {
        repository.delete(todoModel);
    }

    public void update(TodoModel todoModel)
    {
        repository.update(todoModel);
    }

    public void deleteAll()
    {
        repository.deleteAllNotes();
    }

    public  LiveData<Integer> getCompleted(){
        return completed;
    }

    public  LiveData<Integer> getPending(){
        return pending;
    }

    public LiveData<List<TodoModel>> getTodolist()
    {
        return todolist;
    }

    public LiveData<List<TodoModel>>  getCompletedTasks()
    {
        return completedTasks;
    }
}
