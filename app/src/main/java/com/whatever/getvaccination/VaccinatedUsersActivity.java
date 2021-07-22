package com.whatever.getvaccination;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class VaccinatedUsersActivity extends AppCompatActivity {

    private DatabaseReference myref;
    private Toolbar vaccinateduserToolbar;
    private ListView vaccinated_mylist;
    private ArrayList<String> names;
    private ArrayList<String> cities;
    private ArrayList<String> doses;
    private ArrayList<String> phs;
    private ArrayList<String> emails;
    private ArrayList<String> adhars;
    private ArrayList<String>  ages;
    private  simpleAdapter adapter;
    private EditText edt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccinated_users);

        vaccinateduserToolbar=findViewById(R.id.vaccinated_user_toolbar);
        vaccinateduserToolbar.setTitle("Vaccinated Users");
        setSupportActionBar(vaccinateduserToolbar);

        // Initialising ui
        vaccinated_mylist=findViewById(R.id.vaccinated_mylist);
        edt_search=findViewById(R.id.edt_search);

        //
        myref= FirebaseDatabase.getInstance().getReference();

        //Initialising ArrayList
        names=new ArrayList<>();
        cities=new ArrayList<>();
        phs=new ArrayList<>();
        emails= new ArrayList<>();
        doses=new ArrayList<>();
        ages= new ArrayList<>();
        adhars=new ArrayList<>();

        fillarrays();



        adapter=new VaccinatedUsersActivity.simpleAdapter(this,names,cities,phs,doses,emails,ages,adhars);
        vaccinated_mylist.setAdapter(adapter);


    }

    public class simpleAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private Context mcontext;
        private TextView name;
        private TextView city;
        private TextView dose;
        private TextView email;
        private TextView adhar;
        private TextView  ph;
        private TextView age;

        private ArrayList<String>  names;
        private ArrayList<String>  cities;
        private ArrayList<String>  phs;
        private ArrayList<String>  doses;
        private ArrayList<String>  emails;
        private ArrayList<String>  ages;
        private ArrayList<String>  adhars;








        public simpleAdapter(Context c, ArrayList<String> n,ArrayList<String> ci,ArrayList<String> p,ArrayList<String> d,ArrayList<String> e,ArrayList<String> ag,ArrayList<String> ad)
        {
            mcontext=c;
            names=n;
            ages=ag;
            phs=p;
            doses=d;
            cities=ci;
            adhars=ad;
            emails=e;

            layoutInflater=LayoutInflater.from(mcontext);
        }


        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView==null)
                convertView=layoutInflater.inflate(R.layout.singlevaccinateduserlayout,null);

            name=convertView.findViewById(R.id.dynamic_name);
            city=convertView.findViewById(R.id.dynamic_city);
            dose=convertView.findViewById(R.id.dynamic_dose);
            ph=convertView.findViewById(R.id.dynamic_ph);
            age=convertView.findViewById(R.id.dynamic_age);
            email=convertView.findViewById(R.id.dynamic_mail);
            adhar=convertView.findViewById(R.id.dynamic_adhar);




            name.setText(names.get(position));
            city.setText(cities.get(position));
            dose.setText(doses.get(position));
            adhar.setText(adhars.get(position));
            email.setText(emails.get(position));
            age.setText(ages.get(position));
            ph.setText(phs.get(position));



            return convertView;

        }


    }

    public void fillarrays()
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Vaccinatedusers");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String name=snapshot.child("Name").getValue().toString();
                    String city=snapshot.child("City").getValue().toString();
                    String age=snapshot.child("Age").getValue().toString();
                    String ph=snapshot.child("Ph").getValue().toString();
                    String email=snapshot.child("Email").getValue().toString();
                    String adhar=snapshot.child("Adhar").getValue().toString();
                    String dose=snapshot.child("Dose").getValue().toString();

//                    Toast.makeText(VaccinatedUsersActivity.this, name, Toast.LENGTH_SHORT).show();
                    names.add(name);
                    cities.add(city);
                    adhars.add(adhar);
                    ages.add(age);
                    emails.add(email);
                    doses.add(dose);
                    phs.add(ph);

                    adapter.notifyDataSetChanged();

//
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}