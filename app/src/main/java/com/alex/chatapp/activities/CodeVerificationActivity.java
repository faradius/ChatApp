package com.alex.chatapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.chatapp.R;
import com.alex.chatapp.models.User;
import com.alex.chatapp.providers.AuthProvider;
import com.alex.chatapp.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

public class CodeVerificationActivity extends AppCompatActivity {

    String TAG = "CodeVerification";

    Button btnCodeVerification;
    EditText etCode;
    TextView tvSMSWait;
    ProgressBar mProgressBar;

    String mExtraPhone;

    AuthProvider mAuthProvider;
    UsersProvider mUsersProvider;

    String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        btnCodeVerification = findViewById(R.id.btnCodeVerification);
        etCode = findViewById(R.id.etCodeVerification);
        tvSMSWait = findViewById(R.id.tvSMSWait);
        mProgressBar = findViewById(R.id.progressBar);

        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();

        mExtraPhone = getIntent().getStringExtra("phone");
        //Toast.makeText(this, mExtraPhone, Toast.LENGTH_SHORT).show();

        mAuthProvider.sendCodeVerification(mExtraPhone, mCallbacks);

        btnCodeVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etCode.getText().toString();
                if (!code.equals("") && code.length() >= 6){
                    signIn(code);
                }else {
                    Toast.makeText(CodeVerificationActivity.this, "Por favor ingrese el codigo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            mProgressBar.setVisibility(View.GONE);
            tvSMSWait.setVisibility(View.GONE);

            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                etCode.setText(code);
                //signIn(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(CodeVerificationActivity.this, "Se produjo un error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "onVerificationFailed: " + e.getMessage());
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(verificationId, forceResendingToken);
            Toast.makeText(CodeVerificationActivity.this, "El codigo se envio", Toast.LENGTH_SHORT).show();
            mVerificationId = verificationId;
        }
    };

    private void signIn(String code) {
        mAuthProvider.signInPhone(mVerificationId, code).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final User user = new User();
                    user.setId(mAuthProvider.getId());
                    user.setPhone(mExtraPhone);

                    mUsersProvider.getUserInfo(mAuthProvider.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (!documentSnapshot.exists()){
                                mUsersProvider.create(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        goToCompleteInfo();
                                    }
                                });
                            }else {
                                goToCompleteInfo();
                            }
                        }
                    });

                    Log.d(TAG, "signInWithCredential:success");
                    Toast.makeText(CodeVerificationActivity.this, "La autenticacion fue exitosa", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(CodeVerificationActivity.this, "No se pudo autenticar al usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToCompleteInfo() {
        Intent i = new Intent(CodeVerificationActivity.this, CompleteInfoActivity.class);
        startActivity(i);
    }
}