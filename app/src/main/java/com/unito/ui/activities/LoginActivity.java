package com.unito.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.unito.Provider;
import com.unito.SecurePreferences;
import com.unito.projium.R;
import com.unito.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {
    public static final String USER_MESSAGE = "username";

    private Provider mia;
    private UserViewModel userViewModel;

    private EditText editUser;
    private EditText editPwd;
    private CheckBox checkRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SecurePreferences preferences = new SecurePreferences(getBaseContext(),
                "user-info", "key", true);

        /* If already exists a user preference, go to logged activity */

        if (preferences.containsKey("username")) {
            String username = preferences.getString("username");
            activityLogin(username);
        }
        /* else, load the login view */
        else {

            setContentView(R.layout.activity_main);

            editUser = findViewById(R.id.user_text);
            editPwd = findViewById(R.id.pwd_text);
            checkRemember = findViewById(R.id.rememberCheck);

            configureViewModel();
        }

    }


    /**
     * Instantiate ViewModel
     */
    private void configureViewModel() {
        mia = Provider.getProvider(getApplication());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setRepo(mia.ur);

    }

    /**
     * Navigate to LoggedActivity passing the username in a bundle
     *
     * @param username username of successfully logged user
     */
    private void activityLogin(String username) {
        Intent intent = new Intent(LoginActivity.this, LoggedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(USER_MESSAGE, username);
        startActivity(intent);

    }


    /**
     * event handler for "login" click. get a user from database and check
     * if there is a user-password match.
     *
     * @param view
     */
    public void doLogin(View view) {
        String readUname = editUser.getText().toString();
        String readPwd = editPwd.getText().toString();

        SecurePreferences preferences = new SecurePreferences(getApplicationContext(),
                "user-info", "key", true);

        userViewModel.login(readUname).observe(this, user -> {

            if (user != null && user.getPassword().equals(readPwd)) {

                if (checkRemember.isChecked())
                    preferences.put("username", readUname);

                activityLogin(readUname);
            } else {
                Toast.makeText(this, R.string.wrong_login, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void todoClick(View view) {
        Toast.makeText(getApplicationContext(), R.string.not_implemented_function, Toast.LENGTH_SHORT).show();
    }
}