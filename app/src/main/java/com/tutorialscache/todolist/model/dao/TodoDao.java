package com.tutorialscache.todolist.model.dao;

import com.tutorialscache.todolist.model.entity.TodoModel;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
//data access object(Dao) to apply all queries on data base
@Dao
public interface TodoDao {
    //annotation for inserting
    @Insert
    void insertTodo(TodoModel todoModel);

    //annotation for deleting
    @Delete
    void deleteTodo(TodoModel todoModel);

    //annotation for deleting
    @Update
    void updateTodo(TodoModel todoModel);

    //delete all list in main activity
    @Query("DELETE FROM tasks WHERE status_check=0")
    void deleteAllTodo();

    //query to get count of pending tasks
    @Query("SELECT COUNT(id) FROM tasks WHERE status_check=0")
    LiveData<Integer> totalPendingTask();

    //query to get count of completed taks
    @Query("SELECT COUNT(id) FROM tasks WHERE status_check=1 ")
    LiveData<Integer> totalCompletedTaks();

    //query get list for Compelete Tasks activity
    @Query("SELECT * FROM tasks WHERE status_check=1 ORDER BY todo_createdAt DESC")
    LiveData<List<TodoModel>> comletedTasks();

    //query to get list for Main Activity
    @Query("SELECT * FROM tasks WHERE status_check=0 ORDER BY todo_createdAt DESC")
    LiveData<List<TodoModel>> getAllNotes();
}