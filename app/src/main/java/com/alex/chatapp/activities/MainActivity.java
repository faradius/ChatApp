package com.alex.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alex.chatapp.R;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {

    Button btnSendCode;
    EditText etPhone;
    CountryCodePicker countryCodePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendCode = findViewById(R.id.btnSendCode);
        etPhone = findViewById(R.id.etPhone);
        countryCodePicker = findViewById(R.id.ccp);


        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                goToCodeVerificationActivity();
                getData();
            }
        });


    }

    private void getData(){

        String code = countryCodePicker.getSelectedCountryCodeWithPlus();

        String phone = etPhone.getText().toString();
        
        if (phone.equals("")){
            Toast.makeText(this, "Debe insertar el telefono", Toast.LENGTH_SHORT).show();
        }else {
            goToCodeVerificationActivity(code + phone);
//            Toast.makeText(this, "Telefono: "+ code + " " + phone, Toast.LENGTH_LONG).show();
        }

    }

    private void goToCodeVerificationActivity(String phone){
        Intent i = new Intent(MainActivity.this, CodeVerificationActivity.class);
        i.putExtra("phone", phone);
        startActivity(i);
    }
}