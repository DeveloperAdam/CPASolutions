package com.techease.cpasolutions.Activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.techease.cpasolutions.Adapters.FilesAdapter;
import com.techease.cpasolutions.Adapters.FolderAdapter;
import com.techease.cpasolutions.Fragments.AboutFragment;
import com.techease.cpasolutions.Fragments.BlogFragment;
import com.techease.cpasolutions.Fragments.ClientFoldersFragment;
import com.techease.cpasolutions.Fragments.SettingsFragment;
import com.techease.cpasolutions.R;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences =this.getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setTitle("Client Portal");
        fragment=new ClientFoldersFragment();
        getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.client) {
            setTitle("CLIENT PORTAL");
            fragment=new ClientFoldersFragment();
            getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).commit();
        } else if (id == R.id.about) {

            setTitle("ABOUT");
            fragment=new AboutFragment();
            getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).commit();
        } else if (id == R.id.blog) {

            setTitle("BLOGS");
            fragment=new BlogFragment();
            getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).commit();
        } else if (id == R.id.payment) {

        } else if (id == R.id.settings) {
            setTitle("SETTINGS");
            fragment=new SettingsFragment();
            getFragmentManager().beginTransaction().replace(R.id.navContainer,fragment).commit();

        } else if (id == R.id.logout) {
            editor.putString("token","").commit();
            startActivity(new Intent(NavigationActivity.this,MainActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}