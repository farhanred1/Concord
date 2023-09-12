package sp.com.concord;

import static android.service.controls.ControlsProviderService.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class notificationFragment extends Fragment {
    RecyclerView notficationView;
    notifadapter adapter;
    String userID, docName, docEmail;


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public notificationFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notficationView = (RecyclerView) view.findViewById(R.id.recnotif);
        notficationView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("Members/Therapist");
        reference3.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Therapist therapist = snapshot.getValue(Therapist.class);

                if(therapist != null) {
                    docName = therapist.name;
                    docEmail = therapist.email;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "something wrong happened");
            }
        });

        FirebaseRecyclerOptions<Booking>options =
                new FirebaseRecyclerOptions.Builder<Booking>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Bookings").child(userID), Booking.class)
                        .build();
        //Log.d(TAG, "options" + options);

        adapter = new notifadapter(options);
        notficationView.setAdapter(adapter);
        return view;
    }

    public class notifadapter extends FirebaseRecyclerAdapter<Booking, notifadapter.notifviewholder>{

        public notifadapter(@NonNull FirebaseRecyclerOptions<Booking> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull notifviewholder notifviewholder, @SuppressLint("RecyclerView") int i, @NonNull Booking booking) {
            notifviewholder.pname.setText(booking.getbName());
            notifviewholder.pemail.setText(booking.getbEmail());
            notifviewholder.pdate.setText(booking.getbDate());
            notifviewholder.ptime.setText("From "+booking.getbFrom()+" To "+booking.getbTo());
            notifviewholder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference0 = getRef(i);
                    reference0.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Booking booking1 = snapshot.getValue(Booking.class);

                            if (booking1 != null) {
                                String name = booking1.bName;
                                String email = booking1.bEmail;
                                String from = booking1.bFrom;
                                String to = booking1.bTo;
                                String date = booking1.bDate;
                                String bookerid = booking1.bookerid;
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("tApproved").child(userID).child(date);
                                reference1.push().setValue(booking1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Approved approved = new Approved(docName,docEmail,from,to,date,userID);
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("uApproved").child(bookerid);
                                        reference.push().setValue(approved).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                FirebaseDatabase.getInstance().getReference("Bookings").child(userID).child(getRef(i).getKey()).removeValue();

                                            }
                                        });




                                    }
                                });



                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            notifviewholder.decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference("Bookings").child(userID).child(getRef(i).getKey()).removeValue();

                }
            });


        }

        @NonNull
        @Override
        public notifviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false);
            return new notifviewholder(view);
        }

        class notifviewholder extends RecyclerView.ViewHolder{
            TextView pname, pemail, ptime,pdate;
            ImageButton accept, decline;

            public notifviewholder(@NonNull View itemView) {
                super(itemView);
                pname = (TextView) itemView.findViewById(R.id.patientname);
                pemail = (TextView) itemView.findViewById(R.id.patientemail);
                ptime = (TextView) itemView.findViewById(R.id.patienttime);
                pdate = (TextView) itemView.findViewById(R.id.patientdate);
                accept = (ImageButton) itemView.findViewById(R.id.approveBTN);
                decline = (ImageButton) itemView.findViewById(R.id.declineBTN);
            }
        }
    }
}