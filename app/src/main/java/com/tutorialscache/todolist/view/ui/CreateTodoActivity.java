package com.tutorialscache.todolist.view.ui;

        import androidx.appcompat.app.AppCompatActivity;
        import android.app.DatePickerDialog;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.Toast;
        import com.tutorialscache.todolist.R;
        import com.tutorialscache.todolist.utils.Constants;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.Locale;

public class CreateTodoActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextTime;
    private static Date eventDate;
    private String startDateStr;
    private Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        editTextTitle = findViewById(R.id.et_todo_title);
        editTextTime = findViewById(R.id.et_todo_content);
        //set it as current date.
        String date_n = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(new Date());
        editTextTime.setText(date_n);
        //to disable editing in date section
        editTextTime.setKeyListener(null);
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        Intent intent = getIntent();
        //condition to check id user sends id form main activity then the title will change accordingly
        if (intent.hasExtra(Constants.EXTRA_ID)) {
            setTitle("Edit Todo");
            editTextTitle.setText(intent.getStringExtra(Constants.EXTRA_TITLE));
            editTextTime.setText(intent.getStringExtra(Constants.EXTRA_DATE));
        } else {
            setTitle("Add Todo");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_todo:
                saveTodo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //method to save date and title
    private void saveTodo() {
        String title = editTextTitle.getText().toString();
        String time = editTextTime.getText().toString();
        //check if time or date id empty
        if (title.trim().isEmpty() || time.trim().isEmpty())
        {
            Toast.makeText(this, "Please insert a title and date", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(Constants.EXTRA_TITLE, title);
        data.putExtra(Constants.EXTRA_DATE, time);
        int id = getIntent().getIntExtra(Constants.EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(Constants.EXTRA_ID, id);
        }
        setResult(RESULT_OK,data);
        finish();
    }
    //method to get date
    private void getDate() {
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //create a date string
                String myFormat = "dd MMM, yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                try {
                    eventDate = sdf.parse(sdf.format(myCalendar.getTime()));
                    startDateStr = sdf.format(myCalendar.getTime());
                    editTextTime.setText(startDateStr);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        DatePickerDialog datePickerDialog = new
                DatePickerDialog(CreateTodoActivity.this, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        // Limiting access to past dates in the step below:
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}
