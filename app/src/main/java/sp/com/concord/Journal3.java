package sp.com.concord;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Journal3 extends AppCompatActivity {
    private EditText title, entry;
    private FloatingActionButton update, delete;
    private TextView date;

    String id, titleS, entryS, dateS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal3);

        title = findViewById(R.id.tItleU);
        entry  = findViewById(R.id.entryU);
        update = findViewById(R.id.updateJ);
        delete = findViewById(R.id.deleteJ);
        date = (TextView) findViewById(R.id.dateU);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy   HH:mm");
        String currentDateNdayString = simpleDateFormat.format(new Date());
        date.setText(currentDateNdayString);



        //First we call this
        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(titleS);
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                JournalDBHelper myDB = new JournalDBHelper(Journal3.this);
                titleS = title.getText().toString().trim();
                entryS = entry.getText().toString().trim();
                dateS = date.getText().toString();

                myDB.updateData(id, titleS, entryS, dateS);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("entry") && getIntent().hasExtra("date")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            titleS = getIntent().getStringExtra("title");
            entryS = getIntent().getStringExtra("entry");
            dateS = getIntent().getStringExtra("date");


            //Setting Intent Data
            title.setText(titleS);
            entry.setText(entryS);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy   HH:mm");
            String currentDateNdayString = simpleDateFormat.format(new Date());
            date.setText(currentDateNdayString);

        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + titleS + " ?");
        builder.setMessage("Are you sure you want to delete " + titleS + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                JournalDBHelper myDB = new JournalDBHelper(Journal3.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}