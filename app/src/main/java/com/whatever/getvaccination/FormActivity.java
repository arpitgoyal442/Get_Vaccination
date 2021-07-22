package com.whatever.getvaccination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.HashMap;

public class FormActivity extends AppCompatActivity {

    private EditText edt_name;
    private EditText edt_age;
    private EditText edt_ph;
    private EditText edt_adhar;
    private EditText edt_dose;
    private EditText edt_location;
    private EditText edt_email;
    private MaterialButton btn_submit;

    int g=0;

    //Database Referense
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Toolbar form_toolbar=findViewById(R.id.pending_user_toolbar);
        form_toolbar.setTitle("Register");
        setSupportActionBar(form_toolbar);

        //Initialising database referense object
        ref=FirebaseDatabase.getInstance().getReference();

        //Back Button
//         actionBar=getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        //Initialising Ui Elements
        edt_age=findViewById(R.id.edt_age);
        edt_name=findViewById(R.id.edt_name);
        edt_email=findViewById(R.id.edt_email);
        edt_adhar=findViewById(R.id.edt_adhar);
        edt_dose=findViewById(R.id.edt_dose);
        edt_location=findViewById(R.id.edt_city);
        edt_ph=findViewById(R.id.edt_phone);
        btn_submit=findViewById(R.id.btn_submit);

        //Event Listener on Button
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ToDo : Check if this user is already registered?

                putInDatabase();

            }
        });


    }

    private void putInDatabase(){

        String name=edt_name.getText().toString();
        String adhar=edt_adhar.getText().toString();
        String ph=edt_ph.getText().toString();
        String city=edt_location.getText().toString();
        String dose=edt_dose.getText().toString();
        String email=edt_email.getText().toString();
        String age=edt_age.getText().toString();

        g=0;

        if(name.length()==0)
        {
            Toast.makeText(this, "Empty Field", Toast.LENGTH_SHORT).show();
            return;
        }

        if(adhar.length()<12)
        {
            Toast.makeText(this, "Adhar no. Incorrect", Toast.LENGTH_SHORT).show();
            return;
        }
        if(ph.length()<10)
        {
            Toast.makeText(this, "ph no. Incorrect", Toast.LENGTH_SHORT).show();
            return;
        }
        if(city.length()==0)
        {
            Toast.makeText(this, "Empty Field", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dose.equals("1")==false && dose.equals("2")==false)
        {
            Toast.makeText(this, "Write integer value in Dose", Toast.LENGTH_SHORT).show();
            return;
        }

        if(city.length()==0||age.length()==0)
        {
            Toast.makeText(this, "Empty Field", Toast.LENGTH_SHORT).show();
            return;
        }

        //Parent Node for all this data
        String parentNode= dose+""+adhar;



        //TODO: Check if user already exits
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.child("Pendingusers").hasChild(parentNode))
                {
                    Toast.makeText(FormActivity.this, "You have Already Registered For this dose", Toast.LENGTH_LONG).show();

                    return;
                }

                else{
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Name", name);
                    map.put("Adhar", adhar);
                    map.put("Age", age);
                    map.put("Email", email);
                    map.put("Dose", dose);
                    map.put("City", city);
                    map.put("Ph", ph);


                    ref.child("Pendingusers").child(parentNode).setValue(map);

                    // Setting all edit text to empty again
                    edt_adhar.setText("");
                    edt_age.setText("");
                    edt_dose.setText("");
                    edt_email.setText("");
                    edt_location.setText("");
                    edt_name.setText("");
                    edt_ph.setText("");


                    Toast.makeText(FormActivity.this, "Registeration Successful", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        }
//







    }
