package com.androidchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Chat extends AppCompatActivity
{

    Button sendButton;
    EditText messageArea;

    Firebase reference1, reference2;

    static RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    //private ArrayAdapter<String> arrayAdapter;
    public static ArrayList<String> message_list;
    public static ArrayList<Integer> type_list;

    //ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://chatapp-944ef.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://chatapp-944ef.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        Intent intent=getIntent();
        setTitle(intent.getStringExtra("USER_NAME"));

        sendButton = (Button) findViewById(R.id.send_button);
        messageArea = (EditText)findViewById(R.id.message);

        messageArea.setSelected(false);


        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        message_list = new ArrayList<>();
        type_list = new ArrayList<>();

//        scrollView =(ScrollView)findViewById(R.id.scroll_view);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals(""))
                {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });




        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username))
                {
                    RecyclerAdapter.message_list.add("You:\n" + message);
                    RecyclerAdapter.type_list.add(1);
                    adapter.notifyDataSetChanged();

                }
                else
                {
                    RecyclerAdapter.message_list.add(UserDetails.chatWith + ":\n" + message);
                    RecyclerAdapter.type_list.add(2);
                    adapter.notifyDataSetChanged();
                }
                recyclerView.post(new Runnable() {
                    @Override
                    public void run()
                    {
                        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
                    }
                });


//                scrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                    scrollView.fullScroll(scrollView.FOCUS_DOWN);
//                    }
//                });

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
        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }


}
