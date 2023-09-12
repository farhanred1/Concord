package sp.com.concord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;

public class Splash_screen extends AppCompatActivity {
    MediaPlayer music;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        Thread timer = new Thread()
        {
            @Override
            public void run() {
                try                {
                    music = MediaPlayer.create(Splash_screen.this,R.raw.splash);
                    music.start();
                    sleep(3000);

                }
                catch(InterruptedException e)
                {

                }
                finally {
                    Intent i=new Intent(Splash_screen.this,Startup.class);
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();


    }

    @Override    protected void onPause() {
        super.onPause();
        music.release();
    }
    }