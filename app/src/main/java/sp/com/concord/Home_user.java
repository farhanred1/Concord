package sp.com.concord;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class Home_user extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case(R.id.logout) :
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            case(R.id.approved):
                Intent intent = new Intent(Home_user.this, UsersApproved.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToJournal(View view) {
        Intent intent = new Intent(Home_user.this, Journal1.class);
        startActivity(intent);
    }

    public void goToMentoring(View view) {
        Intent intent1 = new Intent(Home_user.this, Mentoring1.class);
        startActivity(intent1);
    }

    public void goToResources(View view) {
        Intent intent2 = new Intent(Home_user.this, Resources1.class);
        startActivity(intent2);
    }
}