package com.myroommate.myroommate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseUser currentUser;
    MenuItem nav_profile_or_login, nav_dynamic_profile_action, nav_dynamic_listing_action;
    RequestQueue requestQueue;
    String HttpDetailsURL = "http://merakamraa.com/php/AccountDetails.php", userType;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        requestQueue = Volley.newRequestQueue(getBaseContext());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    Toast.makeText(MainActivity.this, "logged in", Toast.LENGTH_LONG).show();
                    currentUser.getIdToken(true)
                            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                public void onComplete(@NonNull Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        final String idToken = task.getResult().getToken();
                                        userTypeRequest(idToken);

                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(MainActivity.this, "logged out", Toast.LENGTH_LONG).show();
                    editor.putString("userType", null).apply();

                }
            }
        };

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                hideKeyboardFrom(MainActivity.this, drawerView);


                nav_profile_or_login = navigationView.getMenu().getItem(1);
                nav_dynamic_profile_action = navigationView.getMenu().getItem(2);
                nav_dynamic_listing_action = navigationView.getMenu().getItem(3);

                if (currentUser != null) {

                    nav_profile_or_login.setTitle("Your Profile");
                    nav_dynamic_profile_action.setVisible(true);

                    String userTypePref = sharedPreferences.getString("userType", null);
                    if (userTypePref.equals("House Owner")) {
                        nav_dynamic_profile_action.setTitle("Your Listings");
                        nav_dynamic_profile_action.setIcon(R.drawable.ic_menu_owner_action);
                        nav_dynamic_listing_action.setTitle("List a Place");
                        nav_dynamic_listing_action.setIcon(R.drawable.ic_menu_list);
                    } else {
                        nav_dynamic_profile_action.setTitle("Your Kamraa");
                        nav_dynamic_profile_action.setIcon(R.drawable.ic_menu_tenant_action);
                        nav_dynamic_listing_action.setTitle("Find a Place");
                        nav_dynamic_listing_action.setIcon(R.drawable.ic_menu_find);

                    }


                } else {
                    nav_profile_or_login.setTitle("Login/Register");
                    nav_dynamic_profile_action.setVisible(false);
                }
            }
        };
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        displaySelectedScreen(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!this.getTitle().equals("MeraKamraa")) {
            displaySelectedScreen(R.id.nav_home);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void userTypeRequest(final String idToken) {
        StringRequest detailsRequest = new StringRequest(Request.Method.POST, HttpDetailsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String stringResponse) {
                if (stringResponse.equals("User not found.")) {
                    Snackbar snackbar = Snackbar
                            .make(getCurrentFocus(), stringResponse, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    try {
                        JSONObject jsonResponse = new JSONObject(stringResponse);
                        userType = jsonResponse.getString("user_type");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    editor.putString("userType", userType).apply();

                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse errorRes = error.networkResponse;
                String stringData = "";
                try {
                    if (errorRes != null && errorRes.data != null) {
                        stringData = new String(errorRes.data, "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {

                }
                Log.e("Error", stringData);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("user_token", idToken);
                return parameters;
            }
        };

        requestQueue.add(detailsRequest);
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;


        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeScreenFragment();
                break;
            case R.id.nav_profile_or_login:
                if (currentUser == null) {
                    fragment = new LoginRegisterFragment();
                } else {
                    fragment = new AccountFragment();
                }
                break;
            case R.id.nav_dynamic_profile_action:
                //fragment = new DynamicProfileActionFragment();
                break;
            case R.id.nav_dynamic_listing_action:
                if (userType.equals("House Owner")) {
                    fragment = new ListYourPlaceInfoFragment();
                } else {
                    fragment = new FindAPlaceFragment();
                }
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
    }
}
