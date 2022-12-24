package com.alex.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alex.chatapp.R;
import com.alex.chatapp.models.User;
import com.alex.chatapp.providers.AuthProvider;
import com.alex.chatapp.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteInfoActivity extends AppCompatActivity {

    TextInputEditText etUserName;
    Button btnConfirmInfo;
    CircleImageView mCircleImagePhoto;

    UsersProvider mUserProvider;
    AuthProvider mAuthProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);

        etUserName = findViewById(R.id.etUserName);
        btnConfirmInfo = findViewById(R.id.btnConfirmInfo);
        mCircleImagePhoto = findViewById(R.id.circleImagePhoto);

        mUserProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();

        btnConfirmInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo();
            }
        });
    }

    private void updateUserInfo() {
        String username = etUserName.getText().toString();
        if (!username.equals("")){
            User user = new User();
            user.setUsername(username);
            user.setId(mAuthProvider.getId());
            mUserProvider.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CompleteInfoActivity.this, "La información se actualizo correctamente", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}