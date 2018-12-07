package com.example.rishabh.chatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message extends AppCompatActivity {

    ImageView profile;
    TextView name;
    DatabaseReference reference;


    ImageView sendbtn;
    TextView messagebox;
    messageAdapter messageAdapter;
    RecyclerView recyclerView;
    public  List<chat> mchat;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);



        profile = findViewById(R.id.circleimage);
        name = findViewById(R.id.text);

        sendbtn = findViewById(R.id.sendbtn);
        messagebox = findViewById(R.id.messagebox);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerviewmessage);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        final String userid = intent.getStringExtra("userid");
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                name.setText(user.getName());
                Glide.with(Message.this).load(user.getImage()).into(profile);

                readmessage(firebaseUser.getUid(),userid,user.getImage());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messages = messagebox.getText().toString();
                if (!messages.equals("")) {
                    sendmessage(firebaseUser.getUid(), userid, messages);
                } else {
                    Toast.makeText(Message.this, "Message box cant be left empty", Toast.LENGTH_SHORT).show();
                }
                messagebox.setText(" ");
            }
        });
    }


        public void sendmessage (String sender, String receiver, String messages)
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", sender);
            hashMap.put("receiver", receiver);
            hashMap.put("messages", messages);
            reference.child("Chats").push().setValue(hashMap);

        }
    public void readmessage(final String myid, final String useruid, final String imageurl)
    {
        mchat = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    chat chats = snapshot.getValue(chat.class);
                    if ((chats.getReceiver().equals(myid) && chats.getSender().equals(useruid)) ||
                            chats.getReceiver().equals(useruid) && chats.getSender().equals(myid))
                    {
                        mchat.add(chats);
                    }
                    messageAdapter = new messageAdapter(Message.this,mchat,imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    }
