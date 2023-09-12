package sp.com.concord;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class homeFragment extends Fragment {

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userId;

    private static final String TAG = "FARHAN";


    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Members/Therapist");
        userId= user.getUid();

        TextView patientNoAlltime = (TextView) view.findViewById(R.id.ttlp_alltime);
        TextView nameTxt = (TextView) view.findViewById(R.id.therapist_fullname);
        MaterialButton addAvailability = (MaterialButton) view.findViewById(R.id.btn_more_addAppointments);
        MaterialButton todaysApoin = (MaterialButton) view.findViewById(R.id.btn_more_viewAppointments);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Therapist therapist = snapshot.getValue(Therapist.class);

                if(therapist != null) {
                    String fullName = therapist.name;
                    int ttl_patients = therapist.ttl_patients;
                    Log.d(TAG, fullName);

                    nameTxt.setText(fullName);
                    patientNoAlltime.setText(String.valueOf(ttl_patients));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "something wrong happened");
            }
        });

        addAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Availability_therapist.class);
                startActivity(intent);
            }
        });

        todaysApoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppointmentsToday.class);
                startActivity(intent);
            }
        });

        return view;

    }


}