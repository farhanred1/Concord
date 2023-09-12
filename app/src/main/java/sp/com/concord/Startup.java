package sp.com.concord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Startup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        getSupportActionBar().hide();
    }

    public void goToLoginPage(View view) {
        Intent intent = new Intent(Startup.this, Login.class);
        startActivity(intent);

    }

    public void goToUserSignupPage(View view) {
        Intent intent = new Intent(Startup.this, User_signup.class);
        startActivity(intent);
    }

    public void goToTherapistSignupPage(View view) {
        Intent intent = new Intent(Startup.this, Therapist_signup.class);
        startActivity(intent);
    }
}