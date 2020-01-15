package com.tutorialscache.todolist.model;

import android.app.Application;
import android.os.AsyncTask;

import com.tutorialscache.todolist.model.dao.TodoDao;
import com.tutorialscache.todolist.model.database.TodoDatabase;
import com.tutorialscache.todolist.model.entity.TodoModel;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TodoRepository {
    private TodoDao todoDao;
    private LiveData<List<TodoModel>> todolist,completedTodo;
    private LiveData<Integer> pending,completed;

    public TodoRepository(Application application)
    {
        TodoDatabase database = TodoDatabase.getInstance(application);
        todoDao = database.todoDao();
        todolist = todoDao.getAllNotes();
        completedTodo= todoDao.comletedTasks();
        pending=todoDao.totalPendingTask();
        completed=todoDao.totalCompletedTaks();
    }
    //our viewmodel will access these mathods
    public void insert(TodoModel todo) {
        new InsertTodoAsyncTask(todoDao).execute(todo);
    }

    public void update(TodoModel todo) {
        new UpdateTodoAsyncTask(todoDao).execute(todo);
    }

    public void delete(TodoModel todo) {
        new DeleteTodoAsyncTask(todoDao).execute(todo);
    }

    public void deleteAllNotes() {
        new DeleteAllTodoAsyncTask(todoDao).execute();
    }

    public LiveData<Integer> getCompleted(){
        return completed;
    }

    public LiveData<Integer> getPending(){
        return pending;
    }

    public LiveData<List<TodoModel>> getAllNotes() {
        return todolist;
    }

    public LiveData<List<TodoModel>> getCompletedTodo(){
        return completedTodo;
    }

    //all asyncronous tasks to run in background thread
    private static class InsertTodoAsyncTask extends AsyncTask<TodoModel, Void, Void> {
        private TodoDao todoDao;
        private InsertTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(TodoModel... todoModels) {
            todoDao.insertTodo(todoModels[0]);
            return null;
        }
    }
    private static class DeleteTodoAsyncTask extends AsyncTask<TodoModel, Void, Void> {
        private TodoDao todoDao;
        private DeleteTodoAsyncTask(TodoDao todoDao) {
            this.todoDao=todoDao;
        }

        @Override
        protected Void doInBackground(TodoModel... todoModels) {
            todoDao.deleteTodo(todoModels[0]);
            return null;
        }
    }
    private static class UpdateTodoAsyncTask extends AsyncTask<TodoModel, Void, Void> {
        private TodoDao todoDao;
        private UpdateTodoAsyncTask(TodoDao noteDao) {
            this.todoDao = noteDao;
        }

        @Override
        protected Void doInBackground(TodoModel... todoModels) {
            todoDao.updateTodo(todoModels[0]);
            return null;
        }
    }
    private static class DeleteAllTodoAsyncTask extends AsyncTask<Void, Void, Void> {
        private TodoDao todoDao;
        private DeleteAllTodoAsyncTask(TodoDao todoDao) {
            this.todoDao = todoDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            todoDao.deleteAllTodo();
            return null;
        }
    }

}
