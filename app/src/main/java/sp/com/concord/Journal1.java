package sp.com.concord;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Journal1 extends AppCompatActivity {
    RecyclerView recyclerView;
    private ExtendedFloatingActionButton add_button;


    JournalDBHelper myDB;
    static ArrayList<String> journal_id, journal_title, journal_entry, journal_date;
    static CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal1);

        recyclerView = findViewById(R.id.journalList);
        add_button = findViewById(R.id.add);


        myDB = new JournalDBHelper(Journal1.this);
        journal_id = new ArrayList<>();
        journal_title = new ArrayList<>();
        journal_entry = new ArrayList<>();
        journal_date = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(Journal1.this, this, journal_id, journal_title, journal_entry, journal_date);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Journal1.this));

    }

    public void newJournal(View view) {
        Intent intent = new Intent(Journal1.this, Journal2.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        journal_id = new ArrayList<>();
        journal_title = new ArrayList<>();
        journal_entry = new ArrayList<>();
        journal_date = new ArrayList<>();
        storeDataInArrays();
        customAdapter = new CustomAdapter(Journal1.this, this, journal_id, journal_title, journal_entry, journal_date);
        recyclerView.swapAdapter(customAdapter,false);
        customAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            //empty_imageview.setVisibility(View.VISIBLE);
            //no_data.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                journal_id.add(cursor.getString(0));
                journal_title.add(cursor.getString(1));
                journal_entry.add(cursor.getString(2));
                journal_date.add(cursor.getString(3));
            }
            cursor.close();
            // empty_imageview.setVisibility(View.GONE);
            // no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.journal_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                JournalDBHelper myDB = new JournalDBHelper(Journal1.this);
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(Journal1.this, Journal1.class);
                startActivity(intent);
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