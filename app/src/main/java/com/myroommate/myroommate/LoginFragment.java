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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.myroommate.myroommate.ListYourPlaceInfoFragment.isRedirectedFromLYPInfo;
import static com.myroommate.myroommate.MainActivity.hideKeyboardFrom;

public class LoginFragment extends Fragment {

    Button login;
    EditText Email, Password ;
    String EmailHolder, PasswordHolder;
    String HttpURL = "http://merakamraa.com/php/UserLogin.php";
    Boolean CheckEditText ;
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editPreferences;
    private FirebaseAuth mAuth;


    public static final String TITLE = "Login";

    public static LoginFragment newInstance() {

        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        requestQueue = Volley.newRequestQueue(getContext());
        sharedPreferences = getActivity().getSharedPreferences("logindetails",MODE_PRIVATE);
        editPreferences = sharedPreferences.edit();

        //Assign Id'S
        Email = (EditText)RootView.findViewById(R.id.login_email);
        Password = (EditText)RootView.findViewById(R.id.login_password);
        login = (Button)RootView.findViewById(R.id.email_login_button);

        //Adding Click Listener on button.
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                    hideKeyboardFrom(getContext(), view);

                    // Checking whether EditText is Empty or Not
                    CheckEditTextIsEmptyOrNot();

                    if (CheckEditText) {

                        // If EditText is not empty and CheckEditText = True then this block will execute.

                        /*  */

                        editPreferences.putString("email",EmailHolder);
                        editPreferences.putString("password",PasswordHolder);

                        mAuth.signInWithEmailAndPassword(EmailHolder,PasswordHolder)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            user.getIdToken(true)
                                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                            if (task.isSuccessful()) {
                                                                final String idToken = task.getResult().getToken();
                                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpURL, new Response.Listener<String>() {
                                                                    @Override
                                                                    public void onResponse(String stringResponse) {
                                                                        if(stringResponse.equals("Invalid Username or Password.")){
                                                                            Snackbar snackbar = Snackbar
                                                                                    .make(view, stringResponse, Snackbar.LENGTH_LONG);
                                                                            snackbar.show();
                                                                        }
                                                                        else {
                                                                            try {
                                                                                JSONObject jsonResponse = new JSONObject(stringResponse);
                                                                                String F_Name_Holder = jsonResponse.getString("first_name");
                                                                                String L_Name_Holder = jsonResponse.getString("last_name");
                                                                                editPreferences.putString("first_name",F_Name_Holder);
                                                                                editPreferences.putString("last_name",L_Name_Holder);
                                                                                editPreferences.putString("email",EmailHolder);
                                                                                editPreferences.putString("password",PasswordHolder);
                                                                                editPreferences.apply();
                                                                                Snackbar snackbar = Snackbar
                                                                                        .make(view, "Logged In successfully as " + F_Name_Holder + " " + L_Name_Holder, Snackbar.LENGTH_LONG);
                                                                                snackbar.show();
                                                                            }
                                                                            catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            if(isRedirectedFromLYPInfo) {
                                                                                Fragment fragment = new ListYourPlaceFragment();
                                                                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                                                ft.replace(R.id.content_frame, fragment);
                                                                                ft.commit();
                                                                                isRedirectedFromLYPInfo=false;
                                                                            }

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
                                                                }) {
                                                                    @Override
                                                                    protected Map<String, String> getParams() throws AuthFailureError {

                                                                        Map<String, String> parameters = new HashMap<String, String>();
                                                                        parameters.put("user_token", idToken);
                                                                        return parameters;
                                                                    }
                                                                };

                                                                requestQueue.add(stringRequest);

                                                            } else {
                                                                // Handle error -> task.getException();
                                                            }
                                                        }
                                                    });






                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(getActivity(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();

                                        }

                                        // ...
                                    }
                                });

                    } else {

                        // If EditText is empty then this block will execute .
                        Snackbar snackbar = Snackbar
                                .make(view, "Please fill all the form fields.", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }


            }
        });

        return RootView;
    }



    protected void CheckEditTextIsEmptyOrNot(){

        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();


        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            CheckEditText = false;
        }
        else {

            CheckEditText = true ;
        }

    }
}