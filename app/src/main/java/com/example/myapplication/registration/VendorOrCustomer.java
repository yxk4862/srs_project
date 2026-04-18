package com.example.myapplication.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class VendorOrCustomer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vendor_or_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vendor_or_customer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button customerButton = findViewById(R.id.customer_button);
        Button vendorButton = findViewById(R.id.vendor_button);

        vendorButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(VendorOrCustomer.this, VendorRegistration.class);
            startActivity(intent);
        });

        customerButton.setOnClickListener(v ->
        {
                Intent intent = new Intent(VendorOrCustomer.this, CustomerRegistration.class);
                startActivity(intent);
        });

        findViewById(R.id.vendor_or_customer_back).setOnClickListener(v -> finish());

    }
}