package com.tecneu.tecneu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mercadolibre.android.sdk.Meli;
import com.tecneu.tecneu.Orders.CreateOrder;
import com.tecneu.tecneu.Orders.ModifyOrdersFragment;
import com.tecneu.tecneu.Orders.ViewOrdersFragment;
import com.tecneu.tecneu.Products.ProductFragment;
import com.tecneu.tecneu.Providers.CreateProviderFragment;
import com.tecneu.tecneu.Providers.ProviderFragment;
import com.tecneu.tecneu.Users.CreateUserFragment;
import com.tecneu.tecneu.Users.ViewUserFragment;
import com.tecneu.tecneu.dummy.DummyContent;
import com.tecneu.tecneu.models.Product;
import com.tecneu.tecneu.models.User;
import com.tecneu.tecneu.services.UserService;

public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CreateUserFragment.OnFragmentInteractionListener,
        AmbientFragment.OnFragmentInteractionListener,
        ModifyOrdersFragment.OnFragmentInteractionListener,
        ViewOrdersFragment.OnListFragmentInteractionListener,
        CreateOrder.OnFragmentInteractionListener,
        ViewUserFragment.OnListFragmentInteractionListener,
        ProviderFragment.OnListFragmentInteractionListener,
        CreateProviderFragment.OnFragmentInteractionListener,
        MainScreenFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        ProductFragment.OnListFragmentInteractionListener,
        ConfirmPassword.OnFragmentInteractionListener,
        ChangePassFragment.OnFragmentInteractionListener{

    DrawerLayout _drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Meli.initializeSDK(this);
        setContentView(R.layout.activity_main_screen);
        Toolbar _toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);

        _drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.nav_header_close_session).setOnClickListener(v -> {
            UserService.closeSession(this);
            Intent intent = new Intent(this, SignInActivity.class);
            finish();
            startActivity(intent);
        });


        String type = UserService.getUserType(this);
        if (type.equals("estandar")) {
            showStandardNav();
        } else {
            showAdminNav();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment,MainScreenFragment.newInstance())
                .commit();

    }

    private void showStandardNav() {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_products).setVisible(false);
        nav_Menu.findItem(R.id.nav_providers).setVisible(false);
        nav_Menu.findItem(R.id.nav_users).setVisible(false);
    }

    private void showAdminNav() {
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_stock).setVisible(false);
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
        // getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch (item.getItemId()) {
            case R.id.nav_start:
                fragment = MainScreenFragment.newInstance();
                break;
            case R.id.nav_orders:
                fragment = ViewOrdersFragment.newInstance();
                break;
            case R.id.nav_products:
                fragment = ProductFragment.newInstance();
                break;
            case R.id.nav_providers:
                fragment = ProviderFragment.newInstance();
                break;
            case R.id.nav_users:
                fragment = ViewUserFragment.newInstance(1);
                break;
            case R.id.nav_environment:
                fragment = AmbientFragment.newInstance();
                break;
            case R.id.nav_settings:
                fragment = SettingsFragment.newInstance();
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

    @Override
    public void onListFragmentInteraction(User item) {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 300) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Conectado con exito", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onListFragmentInteraction(Product item) {

    }
}
