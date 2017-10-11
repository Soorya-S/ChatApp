package com.androidchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Users extends AppCompatActivity {
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    int totalUsers = 0;
    ProgressDialog pd;
    String cur_user;

    Firebase reference1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        SharedPreferences pref = getSharedPreferences("COOKIE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String p_tmp = pref.getString("PHONE_NUMBER", "NEW_USER");
        String u_tmp = pref.getString("USER_NAME", "NEW_USER");

        if(!u_tmp.equals("NEW_USER"))   UserDetails.username=u_tmp;
        if(!p_tmp.equals("NEW_USER"))   UserDetails.phone=p_tmp;

        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,al);
        usersList.setAdapter(arrayAdapter);
        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://chatapp-944ef.firebaseio.com/users");

        setTitle("Contacts");
        pd = new ProgressDialog(Users.this);
        pd.setMessage("Loading...");
        pd.show();


        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                Intent  intent=new Intent(Users.this, Chat.class);
                intent.putExtra("USER_NAME",al.get(position));
                startActivity(intent);
            }
        });


        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        String name=dataSnapshot1.child("user_name").getValue().toString();
                        if(!name.equals( UserDetails.username))
                        {
                            al.add(name);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        reference1.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        pd.dismiss();
    }

}