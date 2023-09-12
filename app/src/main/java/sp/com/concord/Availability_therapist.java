package sp.com.concord;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Availability_therapist extends AppCompatActivity {
    CustomCalendarView customCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_therapist);
        customCalendarView = (CustomCalendarView) findViewById(R.id.custom_calendar_view);
    }
}