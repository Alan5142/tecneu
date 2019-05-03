package com.tecneu.tecneu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tecneu.tecneu.dummy.DummyContent;
import com.tecneu.tecneu.services.UserService;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CreateUserFragment.OnFragmentInteractionListener,
        AmbientFragment.OnFragmentInteractionListener,
        MainOrdersFragment.OnFragmentInteractionListener,
        ViewOrdersFragment.OnListFragmentInteractionListener,
        CreateOrder.OnFragmentInteractionListener {

    DrawerLayout _drawer;
    Toolbar _toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar _toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);

        _drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.nav_header_close_session).setOnClickListener(v -> {
            UserService.closeSession(this);
            Intent intent = new Intent(this, SignInActivity.class);
            finish();
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        FragmentManager fm = getSupportFragmentManager();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch (item.getItemId()) {
            case R.id.nav_start:
                break;
            case R.id.nav_orders:
                fragment = ViewOrdersFragment.newInstance();
                break;
            case R.id.nav_stock:
                break;
            case R.id.nav_create_user:
                fragment = CreateUserFragment.newInstance();
                break;
            case R.id.nav_environment:
                fragment = AmbientFragment.newInstance();
                break;
            case R.id.nav_settings:
                break;
            default:
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();
        }

        _drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
