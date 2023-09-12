package sp.com.concord;

import androidx.annotation.NonNull;
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
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Mentoring1 extends AppCompatActivity {
    RecyclerView recview;
    myadapter adapter;
    private static final String TAG = "FARHAN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentoring1);

        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Therapist> options =
                new FirebaseRecyclerOptions.Builder<Therapist>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Members").child("Therapist"), Therapist.class)
                        .build();
        Log.d(TAG, "mentoring1 " + options);

        adapter=new myadapter(options);
        recview.setAdapter(adapter);
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

    public class myadapter extends FirebaseRecyclerAdapter<Therapist,myadapter.myviewholder> {

        private static final String TAG = "FARHAN";
        public myadapter(@NonNull FirebaseRecyclerOptions<Therapist> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, @SuppressLint("RecyclerView") int i, @NonNull Therapist therapist) {
            holder.name.setText(therapist.getName());
            holder.specialization.setText(therapist.getSpecialisation());
            holder.email.setText(therapist.getEmail());
            holder.phone.setText(therapist.getPhone());
            Glide.with(holder.img.getContext()).load(therapist.getImgUrl()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(holder.img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String user_id = getRef(i).getKey();
                    DatabaseReference refrence = getRef(i);
                    refrence. addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("type").getValue() != null) {
                                //String user_id = getRef(i).getKey();

                                String name = dataSnapshot.child("name").getValue().toString();
                                String email =dataSnapshot.child("email").getValue().toString();
                                String phone = dataSnapshot.child("phone").getValue().toString();
                                String specialisation = dataSnapshot.child("specialisation").getValue().toString();
                                String Iurl = dataSnapshot.child("imgUrl").getValue().toString();
                                Log.d(TAG, name);
                                Intent intent = new Intent(v.getContext(),MentoringProfile.class);

                                intent.putExtra("name",name);
                                intent.putExtra("email",email);
                                intent.putExtra("specialisation",specialisation);
                                intent.putExtra("phone",phone);
                                intent.putExtra("Iurl",Iurl);
                                intent.putExtra("userID",user_id);
                                startActivity(intent);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG,error.getMessage());
                        }
                    });





                }
            });
        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_therapist,parent,false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {
            CircleImageView img;
            TextView name,phone,email,specialization;
            public myviewholder(@NonNull View itemView) {
                super(itemView);
                img=(CircleImageView)itemView.findViewById(R.id.pImg);
                name=(TextView)itemView.findViewById(R.id.nametext);
                phone=(TextView)itemView.findViewById(R.id.phonenotxt);
                email=(TextView)itemView.findViewById(R.id.emailtxt);
                specialization=(TextView)itemView.findViewById(R.id.qualificationtxt);
            }
        }




    }

}