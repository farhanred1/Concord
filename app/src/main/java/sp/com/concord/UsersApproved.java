package sp.com.concord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class UsersApproved extends AppCompatActivity {
    RecyclerView recyclerApproved;
    String userID;
    approvedadapter adapter;
    DatabaseReference clickref;
    private static final String TAG = "FARHAN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_approved);
        recyclerApproved = (RecyclerView) findViewById(R.id.approvedList);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerApproved.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Approved> options =
                new FirebaseRecyclerOptions.Builder<Approved>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("uApproved").child(userID), Approved.class)
                        .build();

        adapter = new approvedadapter(options);
        recyclerApproved.setAdapter(adapter);
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

    public class approvedadapter extends FirebaseRecyclerAdapter<Approved, approvedadapter.approveViewholder>{

        public approvedadapter(@NonNull FirebaseRecyclerOptions<Approved> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull approveViewholder approveViewholder, @SuppressLint("RecyclerView") int i, @NonNull Approved approved) {
            approveViewholder.date.setText(approved.docDate);
            approveViewholder.name.setText(approved.docName);
            approveViewholder.email.setText(approved.docEmail);
            approveViewholder.time.setText("From "+approved.docFrom + " To " + approved.docTo);
            approveViewholder.done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickref = getRef(i);

                    clickref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Approved approved1 = snapshot.getValue(Approved.class);
                            if(approved1 != null){
                                String docid = approved1.docId;
                                FirebaseDatabase.getInstance().getReference("uApproved").child(userID).child(clickref.getKey()).removeValue();
                                finish();
                                Log.d(TAG, "getkey " + clickref.getKey());

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });
        }

        @NonNull
        @Override
        public approveViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_approved, parent, false);
            return new approveViewholder(view);
        }

        class approveViewholder extends RecyclerView.ViewHolder{
            TextView date, name, email, time, done;

            public approveViewholder(@NonNull View itemView) {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.docdate);
                name = (TextView) itemView.findViewById(R.id.docname);
                email = (TextView) itemView.findViewById(R.id.docmail);
                time = (TextView) itemView.findViewById(R.id.doctime);
                done = (TextView) itemView.findViewById(R.id.Udonetxt);
            }
        }

    }
}