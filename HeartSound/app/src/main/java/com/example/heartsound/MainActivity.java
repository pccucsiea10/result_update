package com.example.heartsound;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import com.example.heartsound.Communication.Connect;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button)findViewById(R.id.button);
        btn.setEnabled(false);

        CheckBox chx = (CheckBox)findViewById(R.id.checkBox);

        chx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chx.isChecked()==true) {
                    btn.setEnabled(true);
                    btn.getBackground().setColorFilter(null);
                }
                else {
                    btn.setEnabled(false);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chx.setChecked(false);
                btn.setEnabled(false);
                startActivity(new Intent(MainActivity.this, Connect.class));
            }
        });
    }
}