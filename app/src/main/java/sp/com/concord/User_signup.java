package sp.com.concord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class  User_signup extends AppCompatActivity {
    private TextInputEditText uName,uEmail,uPasswd,uPasswdConf,uAge;
    private RadioGroup uGender;
    private TextView errorMsg;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        mAuth = FirebaseAuth.getInstance();

        uName = findViewById(R.id.user_name);
        uEmail = findViewById(R.id.user_email);
        uPasswd = findViewById(R.id.user_passwd);
        uPasswdConf = findViewById(R.id.user_passwdConf);
        uGender = findViewById(R.id.user_gender);
        uAge = findViewById(R.id.user_age);
        errorMsg = findViewById(R.id.errorUserSignup);

    }

    private void signUpUser() {
        String name = uName.getText().toString().trim();
        String email = uEmail.getText().toString().trim();
        String passwd = uPasswd.getText().toString().trim();
        String passwdConf = uPasswdConf.getText().toString().trim();
        String tmpGender = "";
        switch  (uGender.getCheckedRadioButtonId()) {
            case R.id.user_male:
                tmpGender = "Male";
                break;
            case R.id.user_female:
                tmpGender = "Female";
                break;
            case R.id.user_others:
                tmpGender = "Others";
                break;
        }
        final String gender = tmpGender;
        final String type = "User";


        String age = uAge.getText().toString().trim();


        if (name.isEmpty()) {
            uName.setError("Username is required");
            uName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            uEmail.setError("Email is required");
            uEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            uEmail.setError("Please provide a valid Email");
            uEmail.requestFocus();
            return;
        }

        if (passwd.isEmpty()) {
            uPasswd.setError("Password is required");
            uPasswd.requestFocus();
            return;
        }

        if (passwd.length()<6) {
            uPasswd.setError("Password must be 6 character long");
            uPasswd.requestFocus();
            return;
        }

        if (passwdConf.isEmpty()) {
            uPasswdConf.setError("Please re-enter your password");
            uPasswdConf.requestFocus();
            return;
        }

        if (!passwdConf.equals(passwd)) {
            uPasswdConf.setError("Password do not match re-entered password");
            uPasswdConf.requestFocus();
            return;
        }

        if (uGender.getCheckedRadioButtonId() == -1) {
            errorMsg.setText("Please select your gender");
            uGender.requestFocus();
            return;
        } else{
            errorMsg.setText("");
        }

        if (age.isEmpty()) {
            uAge.setError("Age is required");
            uAge.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, passwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User (name, email, passwd, gender, age, type);

                            FirebaseDatabase.getInstance().getReference("Members/Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        Toast.makeText(User_signup.this, "User has been added successfully!", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(User_signup.this, "Failed to sign up! Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(User_signup.this, "Failed to sign up!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    public void signupComplete(View view) {
        signUpUser();
    }
}