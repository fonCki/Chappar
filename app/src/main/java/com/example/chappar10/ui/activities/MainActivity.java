package com.example.chappar10.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.chappar10.R;
import com.example.chappar10.ui.view_model.AccessViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavigationView navigationDrawer;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    AccessViewModel accessViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accessViewModel = new ViewModelProvider(this).get(AccessViewModel.class);
        if (accessViewModel.getMyUserID() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            setContentView(R.layout.activity_main);
            initViews();
            setupNavigation();
        }

    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationDrawer = findViewById(R.id.navigation_drawer);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setSupportActionBar(toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_main,
                R.id.nav_chat_list,
                R.id.nav_location)
                .setOpenableLayout(drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        NavigationUI.setupWithNavController(navigationDrawer, navController);
        setBottomNavigationVisibility();
    }

    private void setBottomNavigationVisibility() {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            final int id = destination.getId();
            if (id == R.id.nav_main || id == R.id.nav_chat_list || id == R.id.nav_location) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (navController.getCurrentDestination().getId() == R.id.nav_main
                || navController.getCurrentDestination().getId() == R.id.nav_chat_list
                || navController.getCurrentDestination().getId() == R.id.nav_location) {
            getMenuInflater().inflate(R.menu.appbar, menu);
            return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

}