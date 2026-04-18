package com.example.myapplication;

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

import com.example.myapplication.users.User;
import com.example.myapplication.users.Vendor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MyOrders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.my_orders), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DBhelper db = new DBhelper(this);
        Long id = getIntent().getLongExtra("id",-1);
        User customer = getIntent().getParcelableExtra("customer");
        customer.setUserID(id);

        //logged_vendor.setUserID(id);
        LinearLayout container = findViewById(R.id.orders_container);
        LayoutInflater inflater = LayoutInflater.from(this);

        BottomNavigationView homepage_bottom = findViewById(R.id.bottomNav);
        BottomNavHelper.setupCustomerBottomNav(this,homepage_bottom,customer,id,R.id.requests_icon);

        List<ServiceRequest> requests = db.getRequestsByUserId((int)customer.getUserID());
        Toast.makeText(this, "User ID: " + id, Toast.LENGTH_LONG).show();
        for (ServiceRequest r : requests) {

            View card = inflater.inflate(R.layout.user_service_request, container, false);

            TextView serviceName = card.findViewById(R.id.service_name_myOrder);
            TextView vendorName = card.findViewById(R.id.vendor_or_customer_name_myOrder);
            TextView vendorEmail = card.findViewById(R.id.vendor_or_customer_email_myOrder);
            TextView serviceDate = card.findViewById(R.id.service_date_myOrder);
            TextView serviceAddress = card.findViewById(R.id.service_address_myOrder);
            TextView status = card.findViewById(R.id.request_status_myOrder);

            serviceName.setText(r.getService());
            vendorName.setText("Vendor: " + r.getVendor().getBusiness_name());
            vendorEmail.setText("Email: " + r.getVendor().getEmail());
            serviceDate.setText("Date: " + r.getDate());
            serviceAddress.setText("Address: " + r.getAddress());

            if(r.getStatus() == false){status.setText("Status: Incomplete");}
            else{status.setText("Status: Complete");}

            // status doesn't really do anything so keep this just in case work on later
            /*switch (request.getStatus().toLowerCase()) {
                case "accepted":
                    status.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    break;
                case "declined":
                    status.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    break;
                default:
                    status.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                    break;
            }*/

            container.addView(card);
        }
    }
}