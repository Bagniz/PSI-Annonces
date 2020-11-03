package com.example.psi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import client.lib.ServerDaemon;

public class SignInActivity extends AppCompatActivity {

    private Button submit;
    private EditText firstName;
    private EditText lastName;
    private EditText birthdate;
    private EditText email;
    private EditText password;
    private EditText address;
    private EditText codePostal;
    private EditText city;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        birthdate = findViewById(R.id.birthdate);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        address = findViewById(R.id.address);
        codePostal = findViewById(R.id.postal_code);
        city = findViewById(R.id.city);
        phoneNumber = findViewById(R.id.phone);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(MainActivity.serverDaemon.signUp(firstName.getText().toString(),lastName.getText().toString(),birthdate.getText().toString(),email.getText().toString(),password.getText().toString(),address.getText().toString(),codePostal.getText().toString(),city.getText().toString(),phoneNumber.getText().toString())==1)
                {
                    Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "User Not created", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

    }
}
