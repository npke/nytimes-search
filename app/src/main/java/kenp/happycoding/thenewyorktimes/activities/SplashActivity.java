package kenp.happycoding.thenewyorktimes.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import kenp.happycoding.thenewyorktimes.R;

public class SplashActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
      }
    }, 1500);
  }
}
