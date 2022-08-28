package com.example.todolistv3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.todolistv3.db.InvokeDatabase;
import com.example.todolistv3.db.ToDoEntity;
import com.example.todolistv3.db.dao.TodoListDao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    // TODO Add groups
    // TODO Prioritize (Importance & Priority)
    // TODO Color Code
    // TODO Move an item from one group to another

    private ListView todosList;
    private List<String> todos;
    private ArrayAdapter<String> todosAdapter;
    private InvokeDatabase invokeDatabase;
    private int idNum = -1;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //TODO: shut the database down.
    }

    // Menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.controls:
                Intent controlsActivityIntent = new Intent(this, ControlsActivity.class);
                startActivity(controlsActivityIntent);
                return true;
            case R.id.about:
                // TODO: Launch About Activity
                Intent aboutActivityIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutActivityIntent);
                return true;
            default:
                Toast.makeText(this, "Unable to act upon the selected item :( Please try again", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
    }

    // onCreate method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        invokeDatabase = new InvokeDatabase(getApplicationContext());

        todosList = findViewById(R.id.todosList);
        Button addTodoButton = findViewById(R.id.addItemButton);

        // this is what is being captured from the UI as input from the user
        todos = new ArrayList<>();
        todosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, todos);
        todosList.setAdapter(todosAdapter);

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                todos = invokeDatabase.getAppDatabase().todoListDao().getAll().stream().map(s-> s.todo).collect(Collectors.toList());
                todosAdapter.clear();
                todos.stream().forEach(t -> todosAdapter.add(t));
                idNum = todos.size();
            }
        });
        thread2.start();

        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = addTodo();

                    ToDoEntity toDoEntity = new ToDoEntity();
                    idNum++;
                    toDoEntity.id = idNum;
                    toDoEntity.todo = text;
                    // create the TodoEntity object and use the below code to save it in the db.
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            invokeDatabase.getAppDatabase().todoListDao().insertAll(toDoEntity);

                        }
                    });
                    thread.start();

            }
        });

        setUpListViewListener();
    }

    // Task Adder
    private String addTodo() {
        EditText addTodoEditText = findViewById(R.id.addItemEditText);
        String editTextText = addTodoEditText.getText().toString();

        if (editTextText.equals("") || editTextText == null) {
            Toast.makeText(this, "Can not add an empty todo :(", Toast.LENGTH_SHORT).show();
        } else if (todos.contains(editTextText)) {
            Toast.makeText(this, "Can not have duplicate todos :(", Toast.LENGTH_SHORT).show();
        } else {
            todos.add(editTextText);
            todosAdapter.add(editTextText);
            todosAdapter.notifyDataSetChanged();

            Toast.makeText(this, "Added Todo to " + editTextText + " :)", Toast.LENGTH_SHORT).show();
            addTodoEditText.setText("");
        }
        return editTextText;
    }

    // Task Editor
    private void editTodo() {
        todosList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), EditTodoActivity.class);
                String previousTodoText = todos.get(position);
                intent.putExtra("com.example.todolistv3.PREVIOUS_TODO_TEXT", previousTodoText);
                startActivity(intent);

                Intent getIntent = getIntent();
                String newTodoText = getIntent.getStringExtra("com.example.todolistv3.PREVIOUS_TODO_TEXT");
                todos.set(position, newTodoText);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        invokeDatabase.getAppDatabase().todoListDao().findToDoWithToDoString(previousTodoText);

                    }
                });

                return true;
            }
        });
    }

    // Task Remover
    private void setUpListViewListener() {
        todosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String removedTodo = todos.get(position);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //List<ToDoEntity> listOfTodosWithStringSearch = invokeDatabase.getAppDatabase().todoListDao().findToDoWithToDoString(removedTodo);
                        //invokeDatabase.getAppDatabase().todoListDao().delete(invokeDatabase.getAppDatabase().todoListDao().findToDoWithToDoString(removedTodo));
                        invokeDatabase.getAppDatabase().todoListDao().delete(invokeDatabase.getAppDatabase().todoListDao().findToDoWithToDoString(removedTodo));
                    }
                });
                thread.start();

                todos.remove(position);
                todosAdapter.remove(todosAdapter.getItem(position));
                todosAdapter.notifyDataSetChanged();

                Toast.makeText(MainActivity.this, "Todo to " + removedTodo +  " removed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}