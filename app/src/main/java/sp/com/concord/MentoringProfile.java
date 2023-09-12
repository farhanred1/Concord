package sp.com.concord;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MentoringProfile extends AppCompatActivity {

    String name, qualification, email, phone, Iurl, userID;
    DayScrollDatePicker mPicker;

    String DATE;
    RecyclerView rvBooking;
    //apointAdapter adapter;
    private static final String TAG = "FARHAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentoring_profile);


        CircleImageView imageholder = findViewById(R.id.profilepic);
        TextView nameholder = findViewById(R.id.profilename);
        TextView specialholder = findViewById(R.id.profilequalification);
        TextView emailholder = findViewById(R.id.profileemail);
        TextView phoneholder = findViewById(R.id.profilephone);

        name = getIntent().getExtras().get("name").toString();
        email = getIntent().getExtras().get("email").toString();
        phone = getIntent().getExtras().get("phone").toString();
        qualification = getIntent().getExtras().get("specialisation").toString();
        Iurl = getIntent().getExtras().get("Iurl").toString();
        userID = getIntent().getExtras().get("userID").toString();
        Log.d(TAG, "id: " + userID);

        nameholder.setText(name);
        specialholder.setText(qualification);
        emailholder.setText("Email : " + email);
        phoneholder.setText("Phone No : " + phone);
        Glide.with(MentoringProfile.this).load(Iurl).into(imageholder);

        mPicker = (DayScrollDatePicker) findViewById(R.id.day_date_picker);
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH) + 1;
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        Date thisdate = calendar.getTime();
        mPicker.setStartDate(thisDay, thisMonth, thisYear);
        mPicker.setEndDate(thisDay, thisMonth + 1, thisYear);
        SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


        mPicker.getSelectedDate(new OnDateSelectedListener() {

            @Override
            public void onDateSelected(@Nullable Date date) {
                if (date != null) {
                    DATE = eventDateFormat.format(date);
                    Intent intent = new Intent(MentoringProfile.this, Mentoring3.class);

                    intent.putExtra("DATE", DATE);
                    intent.putExtra("userid", userID);

                    startActivity(intent);
                }
            }
        });
    }
}









