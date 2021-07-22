package com.whatever.getvaccination;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class AdminActivity extends AppCompatActivity {

    private Toolbar admin_toolbar;
    private MaterialButton btn_view_vaccinated;
    private MaterialButton btn_view_pending;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Initialisation of Ui

        admin_toolbar=findViewById(R.id.admin_toolbar);
        btn_view_pending=findViewById(R.id.btn_view_pending);
        btn_view_vaccinated=findViewById(R.id.btn_view_vaccinated);

        //Setting Toolbar as Action bar
        admin_toolbar.setTitle("Status");
        setSupportActionBar(admin_toolbar);

        //Event Listener on Buttons
        btn_view_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,pendingUserActivity.class));
            }
        });

        btn_view_vaccinated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,VaccinatedUsersActivity.class));
            }
        });



    }
}