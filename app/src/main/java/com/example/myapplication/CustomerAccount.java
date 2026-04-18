package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.users.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomerAccount extends AppCompatActivity {

    private TextView userName, userEmail, userPhone, userAddress, userPassword, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.customer_account), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bind views
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.user_phone);
        userAddress = findViewById(R.id.user_address);
        userPassword = findViewById(R.id.user_password);
        btnLogout = findViewById(R.id.btnLogout);

        // Get User object from Intent
        User user = getIntent().getParcelableExtra("user");
        Long id = getIntent().getLongExtra("id", -1);

        BottomNavigationView homepage_bottom = findViewById(R.id.bottomNav);
        BottomNavHelper.setupCustomerBottomNav(this,homepage_bottom,user,id,R.id.user_icon);

        if (user != null) {
            userName.setText(user.getFirstName() + " " + user.getLastName());
            userEmail.setText(user.getEmail());
            userPhone.setText(user.getphoneNumber());
            userAddress.setText(user.getAddress());

            // Mask password for display
            userPassword.setText("••••••••");
        }

        // Logout action
        btnLogout.setOnClickListener(v -> {
            Intent logoutIntent = new Intent(this, MainActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
        });
    }
}