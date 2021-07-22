package com.whatever.getvaccination;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    Toolbar mytoolbar;
    MaterialButton btn_admin;
    MaterialButton btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mytoolbar=findViewById(R.id.main_toolbar);
        mytoolbar.setTitle("Home");
        setSupportActionBar(mytoolbar);


        btn_admin=findViewById(R.id.main_btn_admin);
        btn_register=findViewById(R.id.main_btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FormActivity.class));
            }
        });

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AdminActivity.class));
            }
        });

    }


}