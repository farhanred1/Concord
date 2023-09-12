package sp.com.concord;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText lEmail, lPasswd;
    private ProgressBar lProgressBar;

    private FirebaseAuth mAuth;

    private static final String TAG = "FARHAN";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        lEmail = findViewById(R.id.logEmail);
        lPasswd = findViewById(R.id.logPasswd);
        lProgressBar = findViewById(R.id.progressBarLogin);
        lProgressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

        }else{

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Members/Therapist").child(currentUser.getUid());

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("type").getValue() != null) {
                        String ma = dataSnapshot.child("type").getValue().toString();

                        Log.d(TAG, ma);

                        if (ma.equals("Therapist")) {
                            Intent intent1 = new Intent(Login.this, Home_therapist.class);
                            startActivity(intent1);
                            finish();
                        }


                    }else {
                        Log.d(TAG, "it is a user");
                        Intent intent = new Intent(Login.this, Home_user.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG,error.getMessage());
                }
            });
        }
    }

    public void onLogIn(View view) {
        userLogin();
        lProgressBar.setVisibility(View.VISIBLE);
    }

    private void userLogin() {
        String email = lEmail.getText().toString().trim();
        String passwd = lPasswd.getText().toString().trim();

        if (email.isEmpty()) {
            lEmail.setError("Email is required");
            lEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            lEmail.setError("Please provide a valid Email");
            lEmail.requestFocus();
            return;
        }
        if (passwd.isEmpty()) {
            lPasswd.setError("Password is required");
            lPasswd.requestFocus();
            return;
        }

        if (passwd.length()<6) {
            lPasswd.setError("Password must be 6 character long");
            lPasswd.requestFocus();
            return;
        }



        mAuth.signInWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //go to profile page
                    lProgressBar.setVisibility(View.GONE);

                   String uid = FirebaseAuth.getInstance().getUid();

                   DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Members/Therapist").child(uid);

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("type").getValue() != null) {
                                String ma = dataSnapshot.child("type").getValue().toString();
                                Toast.makeText(Login.this, ma, Toast.LENGTH_LONG).show();
                                Log.d(TAG, ma);

                                if (ma.equals("Therapist")) {
                                    Intent intent1 = new Intent(Login.this, Home_therapist.class);
                                    startActivity(intent1);
                                    finish();
                                }


                            }else {
                                Log.d(TAG, "else is reached");
                                Intent intent = new Intent(Login.this, Home_user.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d(TAG,error.getMessage());
                        }
                    });



                } else {
                    Toast.makeText(Login.this, "Failed to Login. Please check your credentials.", Toast.LENGTH_LONG).show();
                    lProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}