package com.tutorialscache.todolist.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Entity annotation for giving table name and declaring it
@Entity(tableName = "tasks")
public class TodoModel {

    //@primary Key to set id as primary key
    // and making auto increment for each new list
    @PrimaryKey(autoGenerate = true)
    private int id;

    //@ColumnInfo for giving column name todo_title for entity title
    //and this name will be used in all database queries
    @ColumnInfo(name = "todo_title")
    private String title;

    @ColumnInfo(name = "todo_createdAt")
    private String created_at;

    // status is initialized with 0
    @ColumnInfo(name="status_check")
    private int status=0;

    public TodoModel(String title, String created_at) {
        this.title = title;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}