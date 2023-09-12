package sp.com.concord;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class profileFragment extends Fragment {




    private FirebaseUser user;
    private DatabaseReference reference;
    String userId;

    private static final String TAG = "FARHAN";

    public profileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        CircleImageView imageholder = view.findViewById(R.id.fragprofilepic);
        TextView nameholder = view.findViewById(R.id.fragprofilename);
        TextView specialholder = view.findViewById(R.id.fragprofilequalification);
        TextView emailholder = view.findViewById(R.id.fragprofileemail);
        TextView phoneholder = view.findViewById(R.id.fragprofilephone);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Members/Therapist");
        userId= user.getUid();

        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Therapist therapist = snapshot.getValue(Therapist.class);

                if(therapist != null) {
                    String fullName = therapist.name;
                    String imgUrl = therapist.imgUrl;
                    String quali = therapist.specialisation;
                    String email = therapist.email;
                    String phone = therapist.phone;
                    int ttl_patients = therapist.ttl_patients;
                    Log.d(TAG, fullName);
                    nameholder.setText(fullName);
                    specialholder.setText(quali);
                    emailholder.setText("Email  :"+email);
                    phoneholder.setText("Phone No   :"+phone);
                    Glide.with(getContext()).load(imgUrl).into(imageholder);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "something wrong happened at profile fragment");
            }
        });



        return view;
    }



}