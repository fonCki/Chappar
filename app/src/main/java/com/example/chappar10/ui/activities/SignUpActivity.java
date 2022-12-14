package com.example.chappar10.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.chappar10.R;
import com.example.chappar10.ui.view_model.AccessViewModel;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private EditText nickName, email, password;
    private String nickNameString, emailString, passwordString;
    private DatePicker datePicker;
    private Switch aSwitch;
    private Date birthDate;
    private CircleImageView profile;
    private Uri uri;
    private AccessViewModel viewModel;
    private Button register;
    boolean profileSelected = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        viewModel = new ViewModelProvider(this).get(AccessViewModel.class);

        register = findViewById(R.id.register);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        nickName = findViewById(R.id.editTextNickName);
        aSwitch = findViewById(R.id.gender_switch);
        profile = findViewById(R.id.profile_img);
        datePicker = findViewById(R.id.datePicker);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 33);
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (profileSelected == false) {
                    profile.setImageResource(isChecked?R.drawable.captain_marvel:R.drawable.spidermanmcu);
                }
            }
        });

        register.setOnClickListener(v -> {
            if (validateFields()) {
                viewModel.createUser(emailString, passwordString, nickNameString, !aSwitch.isChecked(), uri, birthDate).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();
                        viewModel.updateLocation();
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        Toast.makeText(this, "User creation failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            profileSelected = true;
            uri = data.getData();
            profile.setImageURI(uri);
        }
    }

    private boolean validateFields() {
        nickNameString = nickName.getText().toString();
        emailString = email.getText().toString();
        passwordString = password.getText().toString();

        //log the datepicker values
        Log.i("DatePicker", "Date: " + datePicker.getDayOfMonth() + " ===  " + datePicker.getMonth() + " ===== " + datePicker.getYear());

        birthDate = new Date();

        birthDate = new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        Log.i("DatePicker", "Date: " + birthDate);

        if (emailString.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            this.email.setError("Email is required and must be valid");
            this.email.requestFocus();
            return false;
        }
        if (passwordString.isEmpty() || passwordString.length() < 6) {
            this.password.setError("Password is required and must be at least 6 characters");
            this.password.requestFocus();
            return false;
        }
        if (nickNameString.isEmpty()) {
            this.nickName.setError("nickname is required");
            this.nickName.requestFocus();
            return false;
        }
        return true;
    }
}