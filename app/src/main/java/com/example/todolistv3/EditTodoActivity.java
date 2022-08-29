package com.example.todolistv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditTodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        // Get the Intent that started this activity and extract the string
        Intent getIntent = getIntent();
        String previousTodoText = getIntent.getStringExtra("com.example.todolistv3.PREVIOUS_TODO_TEXT");
        int position = getIntent.getIntExtra("com.example.todolistv3.POSITION", 0);

        // Capture the layout's TextView and set previousTodoText as its text
        TextView textView = findViewById(R.id.previousTodoTextView);
        textView.setText(previousTodoText);


        Button saveBtn = findViewById(R.id.save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newTodoEditText = findViewById(R.id.newTodoEditText);
                String newTodoText = newTodoEditText.getText().toString();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("com.example.todolistv3.NEW_TODO_TEXT", newTodoText);
                intent.putExtra("com.example.todolistv3.PREVIOUS_TODO_TEXT", previousTodoText);
                intent.putExtra("com.example.todolistv3.POSITION", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}