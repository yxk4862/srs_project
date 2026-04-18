package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.users.User;
import com.example.myapplication.users.Vendor;

import java.util.List;

public class ListVendors extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_vendors);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list_vendors), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String service = getIntent().getStringExtra("service");
        User customer = getIntent().getParcelableExtra("customer");
        customer.setUserID(getIntent().getLongExtra("customerId", -1));

        DBhelper db = new DBhelper(this);
        List<Vendor> vendors = db.getVendorsByService(service);

        LinearLayout container = findViewById(R.id.list_vendors_linear);
        findViewById(R.id.list_vendors_back).setOnClickListener(v -> finish());
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Vendor v : vendors) {

            View card = inflater.inflate(R.layout.vendor_card, container, false);

            TextView name = card.findViewById(R.id.vendor_name);
            TextView desc = card.findViewById(R.id.vendor_description);
            TextView price = card.findViewById(R.id.vendor_price);
            RatingBar rating = card.findViewById(R.id.vendor_rating);
            ImageView image = card.findViewById(R.id.vendor_image);

            name.setText(v.getBusiness_name());
            desc.setText(v.getEmail());

            Double priceValue = v.getServices().get(service);
            price.setText(priceValue != null ? "$" + priceValue + " ": "N/A");

            String vendor_rating = db.getAverageRatingFormatted((int)v.getUserID());

            rating.setRating(Float.parseFloat(vendor_rating));


            card.setOnClickListener(view -> {
                Intent intent = new Intent(ListVendors.this, VendorInformation.class);
                intent.putExtra("vendor", v); // passing the vendor
                intent.putExtra("id", v.getUserID());
                intent.putExtra("customer", customer);
                intent.putExtra("service", service);
                intent.putExtra("customerId", customer.getUserID());// passing the customer
                startActivity(intent);
            });

            // IMPORTANT: just add the view, nothing else
            container.addView(card);
        }

        if (vendors == null || vendors.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText("No vendors available for this service.");
            tv.setPadding(20, 20, 20, 20);
            container.addView(tv);
        }
    }
}