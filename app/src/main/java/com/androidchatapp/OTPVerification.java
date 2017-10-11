package com.androidchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ThrowOnExtraProperties;

import java.util.concurrent.TimeUnit;

public class OTPVerification extends AppCompatActivity implements View.OnClickListener
{

    private static final String TAG = "PhoneAuthActivity";


    private FirebaseAuth mAuth;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    Firebase reference;

    private TextView resend_otp;
    private EditText opt_text;
    private Button verify;
    String user_name,phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optverification);

        resend_otp=(TextView)findViewById(R.id.resend_otp);
        opt_text=(EditText)findViewById(R.id.opt_field);
        verify=(Button)findViewById(R.id.verify_button);

        Intent intent=getIntent();
        user_name=intent.getStringExtra("USER_NAME");
        phone_number=intent.getStringExtra("PHONE_NUMBER");

        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        resend_otp.setOnClickListener(this);
        verify.setOnClickListener(this);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential)
            {
                mVerificationInProgress = false;
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException)
                {
                    opt_text.setError("Invalid phone number.");
                }
                else if (e instanceof FirebaseTooManyRequestsException)
                {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCodeSent(String verificationId,PhoneAuthProvider.ForceResendingToken token)
            {
                mVerificationId = verificationId;
                mResendToken = token;
                Snackbar.make(findViewById(android.R.id.content), "Verification code was sent. Please check your inbox!",Snackbar.LENGTH_SHORT).show();
            }
        };

    }

    @Override
    public void onStart()
    {
        super.onStart();
        startPhoneNumberVerification(phone_number);
    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.verify_button:
                verifyPhoneNumberWithCode(mVerificationId, opt_text.getText().toString());
                break;
            case R.id.resend_otp:
                resendVerificationCode(phone_number,mResendToken);
                opt_text.setText("");
                break;
        }
    }

    private void startPhoneNumberVerification(String phoneNumber)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("91"+phoneNumber,60,TimeUnit.SECONDS,this,mCallbacks);
        mVerificationInProgress = true;
    }

    private void resendVerificationCode(String phoneNumber,PhoneAuthProvider.ForceResendingToken token)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("91"+ phoneNumber,60,TimeUnit.SECONDS,this,mCallbacks,token);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        final PhoneAuthCredential cred=credential;
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            //if (cred.getSmsCode() != null)
                            {
                                opt_text.setText(cred.getSmsCode());
                                final ProgressDialog pd = new ProgressDialog(OTPVerification.this);
                                pd.setMessage("Please wait...");
                                pd.show();
                                reference = new Firebase("https://chatapp-944ef.firebaseio.com/users");
                                reference.child(phone_number).child("user_name").setValue(user_name);
                                reference.child(phone_number).child("phone_number").setValue(phone_number);

                                SharedPreferences.Editor editor = getSharedPreferences("COOKIE", MODE_PRIVATE).edit();
                                editor.putString("USER_NAME", user_name);
                                editor.putString("PHONE_NUMBER", phone_number);
                                editor.apply();

                                startActivity(new Intent(OTPVerification.this,Users.class));
                                pd.dismiss();

                            }

                        }
                        else
                        {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                opt_text.setError("Invalid code.");
                            }
                        }
                    }
                });
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code)
    {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


}
