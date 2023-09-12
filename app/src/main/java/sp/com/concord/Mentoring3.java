package sp.com.concord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Mentoring3 extends AppCompatActivity {
    RecyclerView rvBooking;
    apointAdapter adapter;
    String UID, DATE;
    ExtendedFloatingActionButton fab;
    int row_index;
    DatabaseReference refrencedelete;
    TextView bookingDATE;

    String fullName,email,date,from,to;

    private static final String TAG = "FARHAN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fab = (ExtendedFloatingActionButton) findViewById(R.id.book_fab);
        setContentView(R.layout.activity_mentoring3);
        UID = getIntent().getExtras().get("userid").toString();
        DATE = getIntent().getExtras().get("DATE").toString();
        bookingDATE = (TextView) findViewById(R.id.bookingdate);
        bookingDATE.setText(DATE);

        rvBooking = (RecyclerView) findViewById(R.id.booking_list);
        rvBooking.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Appointments> options =
                new FirebaseRecyclerOptions.Builder<Appointments>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Appointments").child(UID).child(DATE), Appointments.class)
                        .build();
        //Log.d(TAG, "DATE2 " + DATE);
        adapter = new apointAdapter(options);
        rvBooking.setAdapter(adapter);

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

    public void fabOnclick(View view) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Members/Users");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if(user != null) {
                    fullName = user.name;
                    email = user.email;
                    Log.d(TAG, fullName);
                    Log.d(TAG, email);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(date == null){
            Toast.makeText(this, "Please select a slot", Toast.LENGTH_SHORT).show();
        } else {
            addToFirebaseBooking(UID,fullName,email,from,to,date,userId);
        }

    }

    private void addToFirebaseBooking(String uid, String fullName, String email, String from, String to, String date,String bookerid) {
        Booking booking = new Booking(fullName,email,from,to,date,bookerid);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bookings").child(uid);
        reference.push().setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Mentoring3.this, "Your Booking Request Is Sent!", Toast.LENGTH_LONG).show();
                    deleterecord(UID,date);

                } else {
                    Toast.makeText(Mentoring3.this, "Failed to book! Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void deleterecord(String UID, String date) {
        if(refrencedelete != null) {
            FirebaseDatabase.getInstance().getReference("Appointments").child(UID).child(date).child(refrencedelete.getKey()).removeValue();
        }
        else{
            Log.d(TAG, "refrencedelete is null");
        }
    }


    public class apointAdapter extends FirebaseRecyclerAdapter<Appointments, apointAdapter.aponintViewholder> {


        public apointAdapter(@NonNull FirebaseRecyclerOptions<Appointments> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull aponintViewholder aponintViewholder, @SuppressLint("RecyclerView") int i, @NonNull Appointments appointments) {

            aponintViewholder.fromTime.setText(" From: " + appointments.getFrom());
            aponintViewholder.toTime.setText(" To: " +appointments.getTo());

            aponintViewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String key = getRef(i).getKey();
                    refrencedelete = getRef(i);
                    String xd = refrencedelete.toString();
                    Log.d(TAG, i + xd);
                    row_index = i;
                    notifyDataSetChanged();

                    refrencedelete.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Appointments appointments1 = snapshot.getValue(Appointments.class);
                            if (appointments1 != null) {
                                from = appointments1.from;
                                to = appointments1.to;
                                date =  appointments1.date;
                                Log.d(TAG, from);
                                Log.d(TAG, to);
                                Log.d(TAG, date);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Members/Users");
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    reference.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);

                            if(user != null) {
                                fullName = user.name;
                                email = user.email;
                                Log.d(TAG, fullName);
                                Log.d(TAG, email);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });
            if (row_index == -1){
                aponintViewholder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            else if(row_index==i){
                aponintViewholder.itemView.setBackgroundColor(getResources().getColor(R.color.teal_200));
            }
            else
            {
                aponintViewholder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
            }

        }

        @NonNull
        @Override
        public aponintViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_appointment, parent, false);
            return new apointAdapter.aponintViewholder(view);
        }

        class aponintViewholder extends RecyclerView.ViewHolder {
            TextView fromTime, toTime;
            ImageView check;
            public aponintViewholder(@NonNull View itemView) {
                super(itemView);
                fromTime = (TextView) itemView.findViewById(R.id.fromTxt);
                toTime = (TextView) itemView.findViewById(R.id.toTxt);
                check =(ImageView) itemView.findViewById(R.id.imgCheck);


            }
        }
    }
}
