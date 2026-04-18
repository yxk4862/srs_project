package com.example.myapplication;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.users.User;
import com.example.myapplication.users.Vendor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHelper {
    public static void setupCustomerBottomNav(
            AppCompatActivity activity,
            BottomNavigationView bottomNav,
            User customer,
            long userId,
            int selectedItemId
    ) {
        bottomNav.setSelectedItemId(selectedItemId);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // already on this screen
            if (id == selectedItemId) {
                return true;
            }

            Intent intent = null;

            if (id == R.id.home) {
                intent = new Intent(activity, Homepage.class);
                intent.putExtra("customer", customer);
                intent.putExtra("id", userId);

            } else if (id == R.id.requests_icon) {
                intent = new Intent(activity, MyOrders.class);
                intent.putExtra("customer", customer);
                intent.putExtra("id", userId);

            } else if (id == R.id.user_icon) {
                intent = new Intent(activity, CustomerAccount.class);
                intent.putExtra("user", customer);
                intent.putExtra("id", userId);
            }

            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);

                // smoother transition
                activity.overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });
    }

    public static void setupVendorBottomNav(
            AppCompatActivity activity,
            BottomNavigationView bottomNav,
            Vendor vendor,
            long vendorId,
            int selectedItemId
    ) {
        bottomNav.setSelectedItemId(selectedItemId);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == selectedItemId) {
                return true;
            }

            Intent intent = null;

            if (id == R.id.home) {
                intent = new Intent(activity, VendorHomepage.class);
                intent.putExtra("vendor", vendor);
                intent.putExtra("id", vendorId);

            } else if (id == R.id.requests_icon) {
                intent = new Intent(activity, VendorRequests.class);
                intent.putExtra("vendor", vendor);
                intent.putExtra("id", vendorId);

            } else if (id == R.id.user_icon) {
                intent = new Intent(activity, VendorAccountPage.class);
                intent.putExtra("vendor", vendor);
                intent.putExtra("id", vendorId);
            }

            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });
    }
}
