package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.users.Vendor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorAccountPage extends AppCompatActivity {

    DBhelper db = new DBhelper(this);
    private TextView name, email, phone, address, ratingText, btnLogout;
    private LinearLayout servicesContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vendor_account_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vendor_account), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        name = findViewById(R.id.vendor_account_name);
        email = findViewById(R.id.vendor_account_email);
        phone = findViewById(R.id.vendor_account_phone);
        address = findViewById(R.id.vendor_account_address);
        ratingText = findViewById(R.id.vendor_account_rating);
        servicesContainer = findViewById(R.id.vendor_account_services);
        btnLogout = findViewById(R.id.btnLogout);

        // Get vendor from intent
        Vendor vendor = getIntent().getParcelableExtra("vendor");
        Long id = getIntent().getLongExtra("id",-1);
        vendor.setUserID(id);

        BottomNavigationView homepage_bottom = findViewById(R.id.vendor_bottom_nav);
        BottomNavHelper.setupVendorBottomNav(this,homepage_bottom,vendor,id,R.id.user_icon);

        if (vendor != null) {
            name.setText(vendor.getBusiness_name());
            email.setText(vendor.getEmail());
            phone.setText(vendor.getphoneNumber());
            address.setText(vendor.getAddress());
            List<Rating> ratings = db.getRatingsForVendor((int) vendor.getUserID());


            String ratingDisplay = "(" + ratings.size() + ")" + " Reviews";
            ratingText.setText(ratingDisplay);


            HashMap<String, Double> services = db.getVendorServices((int)vendor.getUserID());
            if (services != null) {
                for (Map.Entry<String, Double> entry : services.entrySet()){
                    String service = entry.getKey();

                    TextView serviceView = new TextView(this);
                    serviceView.setText("• " + entry.getKey() + " $" +entry.getValue());
                    serviceView.setTextSize(14f);
                    serviceView.setPadding(0, 4, 0, 4);
                    servicesContainer.addView(serviceView);
                }
            }
        }

        // going to ratings screen, idk yet lol
       /* ratingText.setOnClickListener(v -> {
            Intent intent = new Intent(VendorAccountActivity.this, VendorRatingsActivity.class);
            intent.putExtra("vendor", vendor); // pass vendor again
            startActivity(intent);
        });*/

        // Logout
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}