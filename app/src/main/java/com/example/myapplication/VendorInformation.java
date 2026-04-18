package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.users.User;
import com.example.myapplication.users.Vendor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VendorInformation extends AppCompatActivity {


    Vendor vendor;
    TextView business_name;
    TextView phone_number;
    TextView email;
    TextView address;
    TextView description;
    TextView average_rating;

    DBhelper db;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vendor_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vendor_information), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //this is the vendor that was selected
        db = new DBhelper(this);
        try {
            vendor = getIntent().getParcelableExtra("vendor");
            vendor.setUserID(getIntent().getLongExtra("id", -1));
            User customer = getIntent().getParcelableExtra("customer");
            customer.setUserID(getIntent().getLongExtra("customerId", -1));

            Toast.makeText(this, "User ID: " + customer.getUserID(), Toast.LENGTH_LONG).show();

            String service = getIntent().getStringExtra("service");
            // setting all the fields to the views in the xml
            business_name = findViewById(R.id.vendor_info_name);
            phone_number = findViewById(R.id.vendor_info_phone);
            email = findViewById(R.id.vendor_info_email);
            address = findViewById(R.id.vendor_info_address);
            description = findViewById(R.id.vendor_info_description);
            average_rating = findViewById(R.id.vendor_info_avgRating);

            //setting all the card info from the vendor;
            business_name.setText(vendor.getBusiness_name());
            phone_number.setText(vendor.getphoneNumber());
            email.setText(vendor.getEmail());
            address.setText(vendor.getAddress());
            description.setText(vendor.getDescription());

            average_rating.setText("⭐" + db.getAverageRatingFormatted((int)vendor.getUserID()));

            LinearLayout servicesContainer = findViewById(R.id.servicesContainer);
            HashMap<String, Double> services = db.getVendorServices((int)vendor.getUserID());

            Toast.makeText(this, "User ID: " + services.size(), Toast.LENGTH_LONG).show();
            for (Map.Entry<String, Double> entry : services.entrySet()) {
                String serviceName = entry.getKey();
                Double price = entry.getValue();

                Button serviceButton = new Button(this);

                serviceButton.setText(serviceName + "\n$" + price);

                serviceButton.setAllCaps(false);           // keep the text as is
                serviceButton.setTextSize(16);
                serviceButton.setPadding(16, 16, 16, 16);
                serviceButton.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // width: fill parent
                        LinearLayout.LayoutParams.WRAP_CONTENT  // height: wrap content
                ));


                serviceButton.setOnClickListener(v -> {
                    //Toast.makeText(this, serviceName + " clicked!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VendorInformation.this, Payment.class);
                    intent.putExtra("customer", customer);
                    intent.putExtra("vendor", vendor);
                    intent.putExtra("service", service);
                    intent.putExtra("customerId", customer.getUserID());
                    intent.putExtra("vendorId", vendor.getUserID());// passing the service name here
                    startActivity(intent);
                });

                // Add the button to the container
                servicesContainer.addView(serviceButton);
            }

            refreshRatings(vendor);
           /* List<Rating> ratings = db.getRatingsForVendor((int)vendor.getUserID());
            LinearLayout container = findViewById(R.id.vendor_reviews_layout);
            LayoutInflater inflater = LayoutInflater.from(this);

            Toast.makeText(this, "Rating: " + ratings.size(), Toast.LENGTH_LONG).show();
            for (Rating r : ratings) {
                View card = inflater.inflate(R.layout.rating_display, container, false);

                TextView name = card.findViewById(R.id.customer_name_on_rating);
                TextView date = card.findViewById(R.id.date_on_left_rating);
                TextView comment = card.findViewById(R.id.customer_comment_on_rating);
                RatingBar stars = card.findViewById(R.id.stars_left_on_review);

                name.setText(r.getCustomer().getFirstName());
                date.setText(r.getDate());
                comment.setText(r.getComment());
                stars.setRating((float) r.getStars());

                container.addView(card);
            }
*/

            findViewById(R.id.vendor_info_back).setOnClickListener(v -> finish());
            FloatingActionButton addRatingBtn = findViewById(R.id.button_for_review);

            addRatingBtn.setOnClickListener(v -> {

                Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_review, null);

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setView(dialogView)
                        .create();

                RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
                EditText comment = dialogView.findViewById(R.id.write_comment);
                TextView ratingValue = dialogView.findViewById(R.id.rating_value);
                Button submit = dialogView.findViewById(R.id.btnSubmitRating);

                ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
                    ratingValue.setText(String.valueOf(rating));
                });

                submit.setOnClickListener(x -> {

                    double stars = ratingBar.getRating();
                    String text = comment.getText().toString();
                    String date = java.time.LocalDate.now().toString();

                    Rating rating = new Rating(
                            stars,
                            text,
                            date,
                            customer,
                            vendor
                    );

                       db.addRating(rating);


                       Toast.makeText(this, "Rating posted!", Toast.LENGTH_SHORT).show();

                       dialog.dismiss();

                       average_rating.setText("⭐" + db.getAverageRatingFormatted((int)vendor.getUserID()));
                       refreshRatings(vendor);
                });

                dialog.show();
            });

        } catch (Exception e) {Toast.makeText(this, "id= " + vendor.getUserID(), Toast.LENGTH_SHORT).show();}
    }
    private void refreshRatings(Vendor vendor) {

        LinearLayout container = findViewById(R.id.vendor_reviews_layout);
        container.removeAllViews();

        List<Rating> ratings =
                db.getRatingsForVendor((int) vendor.getUserID());

        LayoutInflater inflater = LayoutInflater.from(this);

        for (Rating r : ratings) {
            View card = inflater.inflate(R.layout.rating_display, container, false);

            TextView name = card.findViewById(R.id.customer_name_on_rating);
            TextView date = card.findViewById(R.id.date_on_left_rating);
            TextView comment = card.findViewById(R.id.customer_comment_on_rating);
            RatingBar stars = card.findViewById(R.id.stars_left_on_review);

            name.setText(r.getCustomer().getFirstName());
            date.setText(r.getDate());
            comment.setText(r.getComment());
            stars.setRating((float) r.getStars());

            container.addView(card);
        }
    }
}