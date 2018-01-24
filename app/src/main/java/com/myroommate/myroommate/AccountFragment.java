package com.myroommate.myroommate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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

import static android.content.Context.MODE_PRIVATE;
import static com.myroommate.myroommate.MainActivity.hideKeyboardFrom;

/**
 * Created by G551JK-DM053H on 24-09-2017.
 */

public class AccountFragment extends Fragment {

    Button Logout,Update,Delete;
    EditText First_Name, Last_Name, Email, Password, PasswordMatch ;
    String F_Name_Holder, L_Name_Holder, EmailHolder, PasswordHolder,PasswordMatchHolder;
    String HttpUpdateURL = "http://merakamraa.com/php/AccountUpdate.php";
    String HttpDetailsURL = "http://merakamraa.com/php/AccountDetails.php";
    String HttpDeleteURL = "http://merakamraa.com/php/AccountDelete.php";
    Boolean CheckEditText ;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;

    GoogleSignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        requestQueue = Volley.newRequestQueue(getContext());

        //Assign Id'S
        First_Name = (EditText)RootView.findViewById(R.id.account_firstname);
        Last_Name = (EditText)RootView.findViewById(R.id.account_lastname);
        Email = (EditText)RootView.findViewById(R.id.account_email);
        Password = (EditText)RootView.findViewById(R.id.account_password);
        PasswordMatch = (EditText)RootView.findViewById(R.id.account_password_match);

        sharedPreferences = getActivity().getSharedPreferences("logindetails",MODE_PRIVATE);
        EmailHolder= sharedPreferences.getString("email","");

        final FirebaseUser user = mAuth.getCurrentUser();

        user.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                           public void onComplete(@NonNull Task<GetTokenResult> task) {
                                               if (task.isSuccessful()) {
                                                   final String idToken = task.getResult().getToken();

                                                   StringRequest detailsRequest = new StringRequest(Request.Method.POST, HttpDetailsURL, new Response.Listener<String>() {
                                                       @Override
                                                       public void onResponse(String stringResponse) {
                                                           if (stringResponse.equals("User not found.")) {
                                                               Snackbar snackbar = Snackbar
                                                                       .make(getView(), stringResponse, Snackbar.LENGTH_LONG);
                                                               snackbar.show();
                                                           } else {
                                                               try {
                                                                   JSONObject jsonResponse = new JSONObject(stringResponse);
                                                                   F_Name_Holder = jsonResponse.getString("first_name");
                                                                   L_Name_Holder = jsonResponse.getString("last_name");
                                                                   EmailHolder = jsonResponse.getString("user_email");

                                                                   First_Name.setText(F_Name_Holder);
                                                                   Last_Name.setText(L_Name_Holder);
                                                                   Email.setText(EmailHolder);


                                                               } catch (JSONException e) {
                                                                   e.printStackTrace();
                                                               }
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
                                           }
                                       });

        Update = (Button)RootView.findViewById(R.id.account_update);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                hideKeyboardFrom(getContext(), view);

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

             /*   if(CheckEditText){
                    if(PasswordHolder.equals(PasswordMatchHolder)) { */

                        // If EditText is not empty and CheckEditText = True then this block will execute.
                        user.getIdToken(true)
                                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                                        if (task.isSuccessful()) {
                                            final String idToken = task.getResult().getToken();

                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUpdateURL, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String stringResponse) {
                                                    Snackbar snackbar = Snackbar
                                                            .make(view, stringResponse, Snackbar.LENGTH_LONG);
                                                    snackbar.show();
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
                                                    parameters.put("f_name", F_Name_Holder);
                                                    parameters.put("L_name", L_Name_Holder);
                                                    parameters.put("email", EmailHolder);
                                                    parameters.put("user_token", idToken);
                                                    return parameters;
                                                }
                                            };

                                            requestQueue.add(stringRequest);
                                        }
                                    }
                                });
                 /*   }
                    else{
                        Snackbar snackbar = Snackbar
                                .make(view, "Passwords do not match. Try again.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                }
                else {

                    // If EditText is empty then this block will execute .
                    Snackbar snackbar = Snackbar
                            .make(view, "Please fill all the form fields.", Snackbar.LENGTH_LONG);
                    snackbar.show();

                } */
            }
            });

        Logout=(Button)RootView.findViewById(R.id.account_logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                getActivity().getSharedPreferences("logindetails", 0).edit().clear().apply();
                FirebaseAuth.getInstance().signOut();
                signOut();
                Snackbar snackbar = Snackbar
                        .make(view, "Successfully Logged Out.", Snackbar.LENGTH_LONG);
                snackbar.show();

                Fragment fragment = new HomeScreenFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
            });

        Delete=(Button)RootView.findViewById(R.id.account_delete);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                StringRequest deleteRequest= new StringRequest(Request.Method.POST, HttpDeleteURL , new Response.Listener<String>(){
                    @Override
                    public void onResponse(String stringResponse){

                        Snackbar snackbar = Snackbar
                                .make(view, stringResponse, Snackbar.LENGTH_LONG);
                        snackbar.show();

                        if(stringResponse.equals("Account deleted successfully!")) {
                            getActivity().getSharedPreferences("logindetails", 0).edit().clear().apply();


                            Fragment fragment = new HomeScreenFragment();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, fragment);
                            ft.commit();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse errorRes = error.networkResponse;
                        String stringData = "";
                        try{
                            if(errorRes != null && errorRes.data != null){
                                stringData = new String(errorRes.data,"UTF-8");
                            }}
                        catch (UnsupportedEncodingException e){

                        }
                        Log.e("Error",stringData);
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> parameters = new HashMap<String,String>();
                        parameters.put("email",EmailHolder);
                        return parameters;
                    }
                };
                requestQueue.add(deleteRequest);
            }
        });
        return RootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Account Details");
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    public void CheckEditTextIsEmptyOrNot(){

        F_Name_Holder = First_Name.getText().toString();
        L_Name_Holder = Last_Name.getText().toString();
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();
        PasswordMatchHolder = PasswordMatch.getText().toString();

        if(TextUtils.isEmpty(F_Name_Holder) || TextUtils.isEmpty(L_Name_Holder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder) || TextUtils.isEmpty(PasswordMatchHolder)) {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }
    }
}


