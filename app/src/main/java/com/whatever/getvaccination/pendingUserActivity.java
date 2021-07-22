package com.whatever.getvaccination;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class pendingUserActivity extends AppCompatActivity {

    private DatabaseReference myref;
    private Toolbar pendingUserToolbar;
    private ListView pending_mylist;
    private ArrayList<String> names;
    private ArrayList<String> cities;
    private ArrayList<String> doses;
    private ArrayList<String> phs;
    private ArrayList<String> emails;
    private ArrayList<String> adhars;
    private ArrayList<String>  ages;
    private  simpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_user);


        //Setting up the Action bar
        pendingUserToolbar=findViewById(R.id.pending_user_toolbar);
        pendingUserToolbar.setTitle("Pending Applications");
        setSupportActionBar(pendingUserToolbar);

        // Initialising ui
        pending_mylist=findViewById(R.id.pending_mylist);

        //
        myref=FirebaseDatabase.getInstance().getReference();

        //Initialising ArrayList
        names=new ArrayList<>();
        cities=new ArrayList<>();
        phs=new ArrayList<>();
        emails= new ArrayList<>();
        doses=new ArrayList<>();
        ages= new ArrayList<>();
        adhars=new ArrayList<>();





        //ToDo: Take Data From Database and Insert into Arraylists
        fillarrays();



        adapter=new simpleAdapter(this,names,cities,phs,doses,emails,ages,adhars);
        pending_mylist.setAdapter(adapter);

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
        private MaterialButton btn_tick;


        AlertDialog.Builder builder;



        MaterialButton btn_copy;
        MaterialButton switch_type;
        MaterialButton delete_pass;


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

           builder=new AlertDialog.Builder(c);



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
                convertView=layoutInflater.inflate(R.layout.singlependinguserlayout,null);

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

            btn_tick=convertView.findViewById(R.id.dynamic_btn_done);
            btn_tick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showalert(position,names.get(position),cities.get(position),doses.get(position),adhars.get(position),emails.get(position),ages.get(position),phs.get(position));
                }
            });


            return convertView;

        }

        public void showalert(int pos,String n,String c,String dos,String ad,String e,String ag,String p){
            builder.setMessage("")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
//
                            //TODO: Add all the data to vaccinated node then remove all this data from pending user and then delete from arraylists
                            String parentNode= dos+""+ad;

                            // Inserting all data in map as key-value pair
                            HashMap<String ,String> map=new HashMap<>();
                            map.put("Name",n);
                            map.put("Adhar",ad);
                            map.put("Age",ag);
                            map.put("Email",e);
                            map.put("Dose",dos);
                            map.put("City",c);
                            map.put("Ph",p);

                            // Sending mail
                            Intent email = new Intent(Intent.ACTION_SEND);
                            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ e});
                            email.putExtra(Intent.EXTRA_SUBJECT, "Successfully Vaccinated");
                            email.putExtra(Intent.EXTRA_TEXT,"Congrats,you have been vaccinated successfully");   

//need this to prompts email client only
                            email.setType("message/rfc822");

                            startActivity(Intent.createChooser(email, "Choose an Email client :"));

                            myref.child("Vaccinatedusers").child(parentNode).setValue(map);
                            Toast.makeText(getApplicationContext(),"Person Vaccinated",
                                    Toast.LENGTH_SHORT).show();
                            myref.child("Pendingusers").child(parentNode).removeValue();

                            //Removing from ArrayLists alos
                            names.remove(pos);
                            cities.remove(pos);
                            doses.remove(pos);
                            phs.remove(pos);
                            emails.remove(pos);
                            ages.remove(pos);
                            adhars.remove(pos);

                            adapter.notifyDataSetChanged();

                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle(n +" got vaccinated?");
            alert.show();
        }
    }

    public void fillarrays()
    {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pendingusers");

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