package com.pac.weatherrebuild.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

//import com.pac.weatherapp.BaseApp;
//import com.pac.weatherapp.R;
//import com.pac.weatherapp.db.entity.LocationEntity;
//import com.pac.weatherapp.viewmodel.LocationListViewModel;

import com.google.android.material.navigation.NavigationView;
import com.pac.weatherrebuild.BaseApp;
import com.pac.weatherrebuild.R;
import com.pac.weatherrebuild.Repository;
import com.pac.weatherrebuild.viewmodel.WeatherViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);

        WeatherViewModel mViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

        Repository repository = ((BaseApp)getApplication()).getRepository();
        repository.setViewModel(mViewModel);

        FragmentManager fragmentManager = getSupportFragmentManager();
        WeatherFragment fragment = WeatherFragment.newInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);

        NavigationView nav = findViewById(R.id.nav_view);

        /*final LocationListViewModel viewModel = ViewModelProviders.of(this).get(LocationListViewModel.class);
        viewModel.getLocations().observe(this, new Observer<List<LocationEntity>>() {
            @Override
            public void onChanged(@Nullable List<LocationEntity> locationEntities) {
                if (locationEntities != null) {
                    nav.getMenu().clear();
                    for(LocationEntity l : locationEntities){
                        nav.getMenu().add(Menu.NONE, l.getId(), Menu.NONE, l.getName());
                    }
                    nav.getMenu().add(Menu.NONE, 0, Menu.NONE, "Save Location");
                }
            }
        });*/

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawerLayout.closeDrawers();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}