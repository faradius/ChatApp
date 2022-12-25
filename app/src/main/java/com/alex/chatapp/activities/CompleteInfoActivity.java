package com.alex.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alex.chatapp.R;
import com.alex.chatapp.models.User;
import com.alex.chatapp.providers.AuthProvider;
import com.alex.chatapp.providers.UsersProvider;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompleteInfoActivity extends AppCompatActivity {

    TextInputEditText etUserName;
    Button btnConfirmInfo;
    CircleImageView mCircleImagePhoto;

    UsersProvider mUserProvider;
    AuthProvider mAuthProvider;

    Options mOptions;

    ArrayList<String> mReturnValues = new ArrayList<>();

    File mImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);

        etUserName = findViewById(R.id.etUserName);
        btnConfirmInfo = findViewById(R.id.btnConfirmInfo);
        mCircleImagePhoto = findViewById(R.id.circleImagePhoto);

        mUserProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();

        mOptions = Options.init()
                .setRequestCode(100)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(mReturnValues)                               //Pre selected Image Urls
                .setExcludeVideos(true)                                       //Option to exclude videos
                .setVideoDurationLimitinSeconds(0)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");

        btnConfirmInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo();
            }
        });

        mCircleImagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPix();
            }
        });
    }

    private void startPix() {
        Pix.start(CompleteInfoActivity.this, mOptions);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            mReturnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            mImageFile = new File(mReturnValues.get(0));
            mCircleImagePhoto.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Pix.start(CompleteInfoActivity.this, mOptions);
            }else {
                Toast.makeText(CompleteInfoActivity.this, "Por favor conceder los permisos de la camara", Toast.LENGTH_SHORT).show();
            }
        }
    }
}