package sp.com.concord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppointmentsToday extends AppCompatActivity {
    RecyclerView todaysView;
    TextView todaysDate;
    todaysAdapter adapter;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_today);
        todaysView = (RecyclerView) findViewById(R.id.todaysView);
        todaysDate = (TextView) findViewById(R.id.todaysdate);
        SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        String currentDate = eventDateFormat.format(calendar.getTime());
        todaysDate.setText(currentDate);
        uid =FirebaseAuth.getInstance().getCurrentUser().getUid();

        todaysView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Booking> options =
                new FirebaseRecyclerOptions.Builder<Booking>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("tApproved").child(uid).child(currentDate), Booking.class)
                        .build();

        adapter=new todaysAdapter(options);
        todaysView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class todaysAdapter extends FirebaseRecyclerAdapter<Booking, todaysAdapter.todaysViewHolder>{

        public todaysAdapter(@NonNull FirebaseRecyclerOptions<Booking> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull todaysViewHolder todaysViewHolder, int i, @NonNull Booking booking) {
            todaysViewHolder.name.setText(booking.getbName());
            todaysViewHolder.email.setText(booking.getbEmail());
            todaysViewHolder.time.setText("From : "+booking.getbFrom()+" To : "+ booking.getbTo());
        }

        @NonNull
        @Override
        public todaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_todays_appointment,parent,false);
            return new todaysViewHolder(view);
        }

        class todaysViewHolder extends RecyclerView.ViewHolder{
            TextView name, email, time;
            public todaysViewHolder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.todaysNAME);
                email = (TextView) itemView.findViewById(R.id.todaysEMAIL);
                time = (TextView) itemView.findViewById(R.id.todaysTIME);
            }
        }
    }
}