package com.example.myapplication.registration;
//Finish vendor registration logic
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.users.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.function.Function;

public class VendorRegistration extends AppCompatActivity {
    /*fix varaibles within VendorRegistration and complete registration logic.
    considering creating a database table the stores vendors and services they provide
    really just try to ge to service request logic
     */
    TextInputLayout vendor_name_l;
    TextInputEditText vendor_name_TI;
    TextInputLayout vendor_email_l;
    TextInputEditText vendor_email_TI;
    TextInputLayout vendor_password_l;
    TextInputEditText vendor_password_TI;
    TextInputLayout vendor_phone_l;
    TextInputEditText vendor_phone_TI;
    TextInputLayout vendor_address_l;
    TextInputEditText vendor_address_TI;

    TextInputLayout color_layout; TextInputEditText color_EditText;
    TextInputLayout city_layout; TextInputEditText city_EditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vendor_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.vendor_registration), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Setting the fields of this class to the id's of the things they represent
        vendor_name_l = findViewById(R.id.vendor_name_layout);
        vendor_name_TI = findViewById(R.id.vendor_name_TextInput);
        vendor_email_l = findViewById(R.id.vendor_email_layout);
        vendor_email_TI = findViewById(R.id.vendor_email_TextInput);
        vendor_password_l = findViewById(R.id.vendor_password_layout);
        vendor_password_TI = findViewById(R.id.vendor_password_TextInput);
        vendor_phone_l = findViewById(R.id.vendor_phone_layout);
        vendor_phone_TI = findViewById(R.id.vendor_phone_TextInput);
        vendor_address_l = findViewById(R.id.vendor_address_layout);
        vendor_address_TI = findViewById(R.id.vendor_address_TextInput);
        color_layout = findViewById(R.id.vendor_color_layout); color_EditText = findViewById(R.id.vendor_color_EditText);
        city_layout = findViewById(R.id.vendor_city_layout); city_EditText  = findViewById(R.id.vendor_city_EditText);


        //these validate the users input as they go from one text box to another
        attachValidation(vendor_name_TI, vendor_name_l, User::validateName, "Invalid Business Name");
        attachValidation( vendor_email_TI,vendor_email_l, User::validateEmail, "Invalid Email");
        attachValidation(vendor_password_TI, vendor_password_l, User::validateName, "Invalid Password");
        attachValidation(vendor_phone_TI, vendor_phone_l, User::validateName, "Invalid Phone Number");
        attachValidation(vendor_address_TI,vendor_address_l,User::validateNumber,"Invalid Address");
        attachValidation(color_EditText,color_layout,User::validateNumber,"Favorite Color Required");
        attachValidation(city_EditText,city_layout,User::validateNumber,"Born City Required");

        Button register = findViewById(R.id.register_next_vendor);
        findViewById(R.id.vendor_register_back).setOnClickListener(v -> finish());
        register.setOnClickListener(v ->
        {
            boolean validFirstName = finalValidation(vendor_name_TI, vendor_name_l, User::validateName, "Business Name");
            boolean validLastName = finalValidation(vendor_email_TI, vendor_email_l, User::validateEmail, "Invalid Email");
            boolean validEmail = finalValidation(vendor_password_TI, vendor_password_l, User::validateName, "Invalid Password");
            boolean validPassword = finalValidation(vendor_phone_TI, vendor_phone_l, User::validateName, "Invalid Phone Number");
            boolean validNumber = finalValidation(vendor_address_TI, vendor_address_l, User::validateNumber, "Invalid Address");
            boolean validColor = finalValidation(color_EditText,color_layout,User::validateNumber,"Favorite Color Required");
            boolean validCity = finalValidation(city_EditText,city_layout,User::validateNumber,"Born City Required");

            if (validFirstName && validLastName && validEmail && validPassword && validNumber && validColor && validCity)
            {
                User activeUser = new User(vendor_name_TI.getText().toString(),null,
                        vendor_email_TI.getText().toString(), vendor_password_TI.getText().toString(),
                        2,vendor_phone_TI.getText().toString(),vendor_address_TI.getText().toString());
                Intent intent = new Intent(VendorRegistration.this, VendorService.class);
                intent.putExtra("user", activeUser);
                intent.putExtra("color", color_EditText.getText().toString().trim());
                intent.putExtra("city", city_EditText.getText().toString().trim());
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