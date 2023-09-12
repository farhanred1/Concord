package sp.com.concord;

import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomCalendarView extends LinearLayout {
    private static final String TAG = "FARHAN";
    ImageButton NextButton, PreviousButton;
    TextView CurrentDate;
    android.widget.GridView GridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);

    MyGridAdapter myGridAdapter;

    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Appointments> appointmentsList = new ArrayList<>();

    DBOpenHelper dbOpenHelper;

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        InitializeLayout();
        SetUpCalendar();

        PreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        NextButton.setOnClickListener((new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH,1);
                SetUpCalendar();
            }
        }));

        GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_new_event_layout,null);
                Button fromTime = addView.findViewById(R.id.fromTimePicker);
                Button toTime = addView.findViewById(R.id.toTimePicker);
                Button AddEvent = addView.findViewById(R.id.addEvent);
                int fHour=0, fMinute=0, tHour=0, tMinute=0;
                fromTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int fromHour, int fromMinute) {
                                int fHour = fromHour;
                                int fMinute = fromMinute;
                                fromTime.setText(String.format(Locale.getDefault(), "%02d:%02d",fHour, fMinute));
                            }
                        };
                        int style = android.app.AlertDialog.THEME_HOLO_DARK;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), style, onTimeSetListener, fHour, fMinute, true);
                        timePickerDialog.show();
                    }
                });
                toTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int toHour, int toMinute) {
                                int fHour = toHour;
                                int fMinute = toMinute;
                                toTime.setText(String.format(Locale.getDefault(), "%02d:%02d",fHour, fMinute));
                            }
                        };
                        int style = android.app.AlertDialog.THEME_HOLO_DARK;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), style, onTimeSetListener, tHour, tMinute, true);
                        timePickerDialog.show();
                    }
                });


                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(fromTime.getText().toString(),toTime.getText().toString(),date,month,year);
                        sendToFirebase(fromTime.getText().toString(),toTime.getText().toString(),date,month,year);
                        SetUpCalendar();
                        alertDialog.dismiss();
                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
        GridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String date = eventDateFormat.format(dates.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout, null);

                RecyclerView recyclerView = showView.findViewById(R.id.EventsRV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showView.getContext()
                        ,CollectEventByDate(date));
                recyclerView.setAdapter(eventRecyclerAdapter);
                eventRecyclerAdapter.notifyDataSetChanged();

                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();

                return true;
            }
        });

    }

    private ArrayList<Appointments> CollectEventByDate (String date) {
        ArrayList<Appointments> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while(cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.FROM));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.TO));
            String Date = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.YEAR));

            Appointments appointments = new Appointments(event, time,Date, month, Year);
            arrayList.add(appointments);

        }

        cursor.close();
        dbOpenHelper.close();

        return arrayList;
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void SaveEvent(String fromTime, String toTime, String date, String month, String year) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(fromTime,toTime,date,month,year,database);
        dbOpenHelper.close();
        Toast.makeText(context,"Event Saved", Toast.LENGTH_LONG).show();


    }

    private void sendToFirebase(String fromTime, String toTime, String date, String month, String year) {

            String uid = FirebaseAuth.getInstance().getUid();
            Appointments appointments = new Appointments(fromTime, toTime, date, month, year);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointments").child(uid).child(date);
            reference.push().setValue(appointments).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Appointment added successfully", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getContext(), "Failed to add! Try again!", Toast.LENGTH_LONG).show();
                    }
                }
            });

    }

    private void InitializeLayout() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout,this);
        NextButton = view.findViewById(R.id.nextBtn);
        PreviousButton = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.currentDate);
        GridView = view.findViewById(R.id.gridview);
    }

    private void SetUpCalendar() {
        String currentDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));

        while ((dates.size() < MAX_CALENDAR_DAYS)) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
        }

        myGridAdapter = new MyGridAdapter(context,dates,calendar,appointmentsList);
        GridView.setAdapter(myGridAdapter);
    }

    private void CollectEventsPerMonth(String Month, String year) {
        appointmentsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsperMonth(Month,year,database);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.FROM));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.TO));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.MONTH));
            String Year = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.YEAR));
            Appointments appointments = new Appointments(event, time, date, month, Year);
            appointmentsList.add(appointments);
        }
        cursor.close();
        dbOpenHelper.close();
    }
}
