package imf.lin.android.imf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    private void setupViews(){
        findViewById(R.id.manipulation_execute_button).setOnClickListener(new
        View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = ScheduleListActivity.createIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

}
