package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.users.User;

public class Confirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirmation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.confirmation), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        User customer = getIntent().getParcelableExtra("customer");
        customer.setUserID(getIntent().getLongExtra("customerId", -1));
        Button home = findViewById(R.id.btnHome);
        home.setOnClickListener(v -> {
            //Toast.makeText(this, serviceName + " clicked!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Confirmation.this, Homepage.class);
            intent.putExtra("user", customer);// pass whole object
            intent.putExtra("id", customer.getUserID());
            startActivity(intent);
        });

    }
}