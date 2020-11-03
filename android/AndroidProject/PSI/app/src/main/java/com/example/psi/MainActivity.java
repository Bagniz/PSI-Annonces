package com.example.psi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import client.lib.ServerDaemon;

public class MainActivity extends AppCompatActivity {
    private static class DownloadFilesTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... voids) {
            ServerDaemon serverDaemon = new ServerDaemon();
            if(serverDaemon.logIn(voids[0],voids[1])==1) {

                return Boolean.TRUE;
            }
            else{
                return Boolean.FALSE;
            }

        }
    }

    private Button logInButton = null;
    private TextView signIn = null;
    private EditText email = null;
    private EditText password = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ServerDaemon serverDaemon = new ServerDaemon("");
        signIn = findViewById(R.id.signin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        logInButton = findViewById(R.id.login);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new DownloadFilesTask().doInBackground(email.getText().toString(), password.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "connecté", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignInActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}
