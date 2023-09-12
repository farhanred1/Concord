package sp.com.concord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_therapist extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userId;

    private static final String TAG = "FARHAN";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_therapist);
        bottomNavigationView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this,  R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

       /* FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction t = manager.beginTransaction();
        final homeFragment h1 = new homeFragment();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Members/Therapist");
        userId= user.getUid();
        Log.d(TAG, userId);
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Therapist therapist = snapshot.getValue(Therapist.class);
                Log.d(TAG, "snapshot taken");
                if(therapist != null) {
                    Log.d(TAG, "Therapist is not null");
                    String fullName = therapist.name;
                    int ttl_patients = therapist.ttl_patients;
                    String ttl_patients_String = String.valueOf(ttl_patients);
                    Log.d(TAG, fullName);

                    Bundle b1 = new Bundle();
                    b1.putString("name",fullName);
                    b1.putString("totalPatient",ttl_patients_String);

                    h1.setArguments(b1);
                    final FragmentTransaction add = t.add(R.id.frame_home, h1);
                    t.commit();
                } else{
                    Log.d(TAG, "therapist is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "something wrong happened");
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.therapist_home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case(R.id.logout_therapist):
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}