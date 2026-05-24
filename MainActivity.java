package com.beykent.akillitestanaliz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnBasla; // MaterialButton yerine sadece Button yaz.
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BUTTON BIND
        btnBasla = findViewById(R.id.btnStart);

        btnBasla.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(i);
            finish(); // geri tuşu ile bu ekrana dönmesin
        });
    }
}
