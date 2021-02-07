package com.unito.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.unito.SecurePreferences;
import com.unito.Provider;
import com.unito.projium.R;
import com.unito.viewmodel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoggedActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private String username;
    private Provider mia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemReselectedListener(item -> {
        });
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_courses)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        username = getIntent().getStringExtra(LoginActivity.USER_MESSAGE);
        configureViewModel();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.disconnect) {
            SecurePreferences preferences = new SecurePreferences(getApplicationContext(),
                    "user-info", "key", true);
            if (preferences.containsKey("username"))
                preferences.removeValue("username");

            Intent intent = new Intent(LoggedActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Instantiate ViewModel
     */
    private void configureViewModel() {
        mia = Provider.getProvider(getApplication());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.setRepo(mia.ur);
        userViewModel.setLoggedUser(username);

    }

}