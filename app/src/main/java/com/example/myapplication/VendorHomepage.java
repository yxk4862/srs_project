package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.registration.VendorOrCustomer;
import com.example.myapplication.users.Vendor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class VendorHomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vendor_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vendor_homepage), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DBhelper db = new DBhelper(this);
        Long id = getIntent().getLongExtra("id",-1);
        Vendor logged_vendor = getIntent().getParcelableExtra("vendor");
        logged_vendor.setUserID(id);
        LinearLayout container = findViewById(R.id.vendor_homepage_linear);
        LayoutInflater inflater = LayoutInflater.from(this);

        List<ServiceRequest> requests = db.getRequestsByUserId((int)logged_vendor.getUserID());
        Toast.makeText(this, "User ID: " + logged_vendor.getUserID(), Toast.LENGTH_LONG).show();
        for (ServiceRequest r : requests) {

            View card = inflater.inflate(R.layout.customer_request, container, false);

            TextView service = card.findViewById(R.id.service_name);
            TextView name = card.findViewById(R.id.customer_name_service);
            TextView email = card.findViewById(R.id.customer_email_service);
            TextView date = card.findViewById(R.id.service_date);
            TextView address = card.findViewById(R.id.service_address);

            Button accept = card.findViewById(R.id.btnAccept);
            Button decline = card.findViewById(R.id.btnDecline);

            service.setText(r.getService() + " Request");
            name.setText("Customer: " + r.getCustomer().getFirstName());
            email.setText(r.getCustomer().getEmail());
            date.setText(r.getDate());
            address.setText(r.getAddress());

            accept.setOnClickListener(v -> {
                db.acceptRequest(r.getServiceRequestID());
                // remove card from UI
                container.removeView(card);

                Toast.makeText(this, "Request accepted", Toast.LENGTH_SHORT).show();
            });

            decline.setOnClickListener(v -> {
                db.declineRequest(r.getServiceRequestID());

                // remove card from UI
                container.removeView(card);

                Toast.makeText(this, "Request declined", Toast.LENGTH_SHORT).show();
            });
            if(!r.isAccepted()){container.addView(card);}
        }

        BottomNavigationView homepage_bottom = findViewById(R.id.vendor_bottom_nav);
        BottomNavHelper.setupVendorBottomNav(this,homepage_bottom,logged_vendor,id,R.id.home);
        /*homepage_bottom.setOnItemSelectedListener(item -> {
            int item_id = item.getItemId();

            if (item_id == R.id.home) {
                Intent intent = new Intent(this, VendorHomepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            } else if (item_id == R.id.requests_icon) {
                Intent intent = new Intent(this, VendorRequests.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("vendor", logged_vendor);
                intent.putExtra("id", id);
                startActivity(intent);
                return true;
            } else if (item_id == R.id.user_icon) {
                Intent intent = new Intent(this, VendorAccountPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("vendor", logged_vendor);
                intent.putExtra("id", id);
                startActivity(intent);
                return true;
            }
            return false;
        });*/
    }
}