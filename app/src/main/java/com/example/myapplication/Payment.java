package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.registration.VendorRegistration;
import com.example.myapplication.registration.VendorService;
import com.example.myapplication.users.User;
import com.example.myapplication.users.Vendor;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.Function;

public class Payment extends AppCompatActivity {

    DBhelper db = new DBhelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.payment), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Vendor vendor = getIntent().getParcelableExtra("vendor");
        vendor.setUserID(getIntent().getLongExtra("vendorId", -1));
        User customer = getIntent().getParcelableExtra("customer");
        customer.setUserID(getIntent().getLongExtra("customerId", -1));
        String service= getIntent().getStringExtra("service");

        TextInputLayout card_number_layout = findViewById(R.id.card_number_layout); TextInputEditText card_number_EditText = findViewById(R.id.card_number_editText);
        TextInputLayout name_layout = findViewById(R.id.name_on_card_layout); TextInputEditText name_EditText = findViewById(R.id.name_on_card_EditText);
        TextInputLayout expiration_layout = findViewById(R.id.expiration_layout); TextInputEditText expiration_EditText = findViewById(R.id.expiration_editText);
        TextInputLayout cvv_layout = findViewById(R.id.cvv_layout); TextInputEditText cvv_EditText = findViewById(R.id.cvv_editText);
        TextInputLayout address_layout = findViewById(R.id.payment_address_layout); TextInputEditText address_EditText = findViewById(R.id.payment_address_EditText);
        TextInputLayout city_layout = findViewById(R.id.payment_city_layout); TextInputEditText city_EditText = findViewById(R.id.payment_city_EditText);
        TextInputLayout state_layout = findViewById(R.id.payment_state_layout); TextInputEditText state_EditText = findViewById(R.id.payment_state_EditText);
        TextInputLayout zip_layout = findViewById(R.id.payment_zip_layout); TextInputEditText zip_EditText = findViewById(R.id.payment_zip_EditText);
        TextInputLayout date_layout = findViewById(R.id.date_time_layout); TextInputEditText dateTimeEditText = findViewById(R.id.date_time_editText);


        attachValidation(card_number_EditText,card_number_layout, User::validateName,"Invalid Card Number");
        attachValidation(name_EditText,name_layout, User::validateName,"Invalid Name");
        attachValidation(expiration_EditText,expiration_layout, User::validateName,"Invalid Expiration");
        attachValidation(cvv_EditText,cvv_layout, User::validateName,"Invalid CVV");
        attachValidation(address_EditText,address_layout, User::validateName,"Invalid Address");
        attachValidation(city_EditText,city_layout, User::validateName,"Invalid City");
        attachValidation(state_EditText,state_layout, User::validateName,"Invalid State");
        attachValidation(zip_EditText,zip_layout, User::validateName,"Invalid Zip Code");
        attachValidation(dateTimeEditText,date_layout,User::validateName, "Set date and time");

        dateTimeEditText.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePicker = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {

                        // After date is picked → open time picker
                        TimePickerDialog timePicker = new TimePickerDialog(this,
                                (timeView, hourOfDay, minute) -> {

                                    calendar.set(year, month, dayOfMonth, hourOfDay, minute);

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                    dateTimeEditText.setText(sdf.format(calendar.getTime()));

                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                false // false = 12hr, true = 24hr
                        );

                        timePicker.show();

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePicker.show();
        });

        findViewById(R.id.payment_back).setOnClickListener(v -> finish());
        Button submit = findViewById(R.id.btnPay);

        submit.setOnClickListener(v ->
        {
            boolean validCardNumber = finalValidation(card_number_EditText, card_number_layout, User::validateName, "Invalid Card Number");
            boolean validName = finalValidation(name_EditText, name_layout, User::validateName, "Invalid Name");
            boolean validExpiration = finalValidation(expiration_EditText, expiration_layout, User::validateName, "Invalid Expiration");
            boolean validCVV = finalValidation(cvv_EditText, cvv_layout, User::validateName, "Invalid CVV");
            boolean validAddress = finalValidation(address_EditText, address_layout, User::validateName, "Invalid Address");
            boolean validCity = finalValidation(city_EditText, city_layout, User::validateName, "Business City");
            boolean validState = finalValidation(state_EditText, state_layout, User::validateName, "Invalid State");
            boolean validZip = finalValidation(zip_EditText, zip_layout, User::validateName, "Invalid Zip Code");
            boolean validDate = finalValidation(dateTimeEditText, date_layout, User::validateName, "Set Date & Time");

            if (validCardNumber && validName && validExpiration && validCVV && validAddress && validCity && validState && validZip)
            {
                //date of the request
                String date = dateTimeEditText.getText().toString();
                //price of the request
                Double price = vendor.getServices().get(service);
                //adding all the address stuff to construct the service request
                StringBuilder address = new StringBuilder("");
                address.append(address_EditText.getText().toString() + ", ");
                address.append(city_EditText.getText().toString() + ", ");
                address.append(state_EditText.getText().toString() + ", ");
                address.append(zip_EditText.getText().toString());

                ServiceRequest ServiceRequest = new ServiceRequest(service,date,price,false,vendor,customer,address.toString(),false);
                //adding the request I just made into the database
                db.addServiceRequest(ServiceRequest);
                Intent intent = new Intent(Payment.this, Confirmation.class);
                intent.putExtra("customer", customer);
                intent.putExtra("customerId", customer.getUserID());

                startActivity(intent);
            }
        });
    }
    void attachValidation(TextInputEditText editText, TextInputLayout layout, Function<String, Boolean> validator, String errorMessage) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String value = editText.getText().toString();

                if (!validator.apply(value)) {
                    layout.setError(errorMessage);
                } else {
                    layout.setError(null);
                }
            }
        });
    }

    boolean finalValidation(TextInputEditText editText, TextInputLayout layout, Function<String, Boolean> validator, String errorMessage)
    {
        String value = editText.getText() != null ? editText.getText().toString().trim() : "";

        if (!validator.apply(value)) {
            layout.setError(errorMessage);
            return false;
        } else {
            layout.setError(null);
            return true;
        }
    }
}