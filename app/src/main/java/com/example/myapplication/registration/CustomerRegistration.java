package com.example.myapplication.registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.DBhelper;
import com.example.myapplication.Homepage;
import com.example.myapplication.R;
//import com.example.myapplication.UserHomePage;
import com.example.myapplication.users.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.function.Function;

public class CustomerRegistration extends AppCompatActivity {

    TextInputLayout firstName_layout;
    TextInputEditText firstName_EditText;
    TextInputLayout lastName_layout;
    TextInputEditText lastName_EditText;
    TextInputLayout email_layout;
    TextInputEditText email_EditText;
    TextInputLayout password_layout;
    TextInputEditText password_EditText;
    TextInputLayout phoneNumber_layout;
    TextInputEditText phoneNumber_EditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.customer_registration), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /* start using the ui stuff to hold the values to pass on to the user object.
           validations are already there, so now, just validate and input if valid.
           once the user presses register, just add them to the database and send them to the
           user home page, and have it show their first name.
         */

        //Setting the fields of this class to the id's of the things they represent
        firstName_layout = findViewById(R.id.customer_firstName_layout);
        firstName_EditText = findViewById(R.id.customer_firstName_EditText);
        lastName_layout = findViewById(R.id.customer_lastName_layout);
        lastName_EditText = findViewById(R.id.customer_lastName_EditText);
        email_layout = findViewById(R.id.customer_email_layout);
        email_EditText = findViewById(R.id.customer_email_EditText);
        password_layout = findViewById(R.id.customer_password_layout);
        password_EditText = findViewById(R.id.customer_password_EditText);
        phoneNumber_layout = findViewById(R.id.customer_number_layout);
        phoneNumber_EditText = findViewById(R.id.customer_number_EditText);

        //these validate the users input as they go from one text box to another
        attachValidation(firstName_EditText, firstName_layout, User::validateName, "Invalid First name");
        attachValidation(lastName_EditText, lastName_layout, User::validateName, "Invalid Last name");
        attachValidation(email_EditText, email_layout, User::validateEmail, "Invalid Email");
        attachValidation(password_EditText, password_layout, User::validateName, "Invalid Password");
        attachValidation(phoneNumber_EditText,phoneNumber_layout,User::validateNumber,"Invalid Phone Number");

        Button register = findViewById(R.id.customer_register_button);
        findViewById(R.id.customer_register_back).setOnClickListener(v -> finish());

        register.setOnClickListener(v ->
        {
            boolean validFirstName = finalValidation(firstName_EditText, firstName_layout, User::validateName, "Invalid First Name");
            boolean validLastName = finalValidation(lastName_EditText, lastName_layout, User::validateName, "Invalid Last name");
            boolean validEmail = finalValidation(email_EditText, email_layout, User::validateEmail, "Invalid Email");
            boolean validPassword = finalValidation(password_EditText, password_layout, User::validateName, "Invalid Password");
            boolean validNumber = finalValidation(phoneNumber_EditText, phoneNumber_layout, User::validateNumber, "Invalid Phone Number");

            if (validFirstName && validLastName && validEmail && validPassword && validNumber)
            {
                DBhelper db = new DBhelper(this);

                User activeUser = new User(firstName_EditText.getText().toString(),lastName_EditText.getText().toString(),
                                        email_EditText.getText().toString(),password_EditText.getText().toString(),
                                        1,phoneNumber_EditText.getText().toString(),null);
                Intent intent = new Intent(CustomerRegistration.this, Homepage.class);
                intent.putExtra("user", activeUser);
                long userid = db.addUser(activeUser);
                if (userid != -1) {
                    activeUser.setUserID(userid);
                    startActivity(intent);
                }
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