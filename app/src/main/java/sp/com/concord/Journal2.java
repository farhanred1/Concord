package sp.com.concord;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Journal2 extends AppCompatActivity {


    private EditText title, entry;
    private TextView date;
    private FloatingActionButton save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal2);

        title = (EditText) findViewById(R.id.tItleU);
        entry = (EditText) findViewById(R.id.entryU);
        date = (TextView) findViewById(R.id.dayNdate);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy   HH:mm");
        String currentDateNdayString = simpleDateFormat.format(new Date());
        date.setText(currentDateNdayString);


        save = findViewById(R.id.saveJ);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titleS= title.getText().toString();
                String entryS = entry.getText().toString();
                String dateS = date.getText().toString();


                JournalDBHelper myDB = new JournalDBHelper(Journal2.this);
                myDB.addJournal(titleS,entryS, dateS);
                Journal1.customAdapter.notifyItemInserted(Journal1.journal_id.size());
                finish();
            }
        });
    }
}