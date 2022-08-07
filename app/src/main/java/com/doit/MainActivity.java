package com.doit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.doit.Adapter.ToDoAdapter;
import com.doit.Model.ToDoModel;
import com.doit.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView taskRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton floatingActionButton;

    private List<ToDoModel> taskList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskRecyclerView = findViewById(R.id.tasksRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, this);
        taskRecyclerView.setAdapter(tasksAdapter);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        taskList = db.getAllTasks();
        tasksAdapter.setTasks(taskList);

        floatingActionButton.setOnClickListener(view -> AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG));
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}