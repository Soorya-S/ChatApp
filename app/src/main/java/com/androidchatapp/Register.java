package com.androidchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class Register extends AppCompatActivity
{
    EditText username,phone_number;
    Button registerButton;
    String user,phonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        phone_number = (EditText)findViewById(R.id.phonenumber);
        registerButton = (Button)findViewById(R.id.register);






        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                user = username.getText().toString();
                phonenumber=phone_number.getText().toString();

                if (user.equals("")) {
                    username.setError("can't be blank");
                }
                 else if (!user.matches("[A-Za-z0-9]+")) {
                    username.setError("only alphabet or number allowed");

                }
                else
                {
                    final ProgressDialog pd = new ProgressDialog(Register.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    Intent intent=new Intent(Register.this,OTPVerification.class);
                    intent.putExtra("PHONE_NUMBER",phonenumber);
                    intent.putExtra("USER_NAME",user);
                    startActivity(intent);

                    pd.dismiss();
                }
            }
        });


}




}