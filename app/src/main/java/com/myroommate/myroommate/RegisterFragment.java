package com.myroommate.myroommate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static com.myroommate.myroommate.MainActivity.hideKeyboardFrom;

public class RegisterFragment extends Fragment {

    Button register;
    EditText First_Name, Last_Name, Email, Password ;
    String F_Name_Holder, L_Name_Holder, EmailHolder, PasswordHolder;
    String HttpURL = "https://myroommate.000webhostapp.com/UserRegistration.php";
    Boolean CheckEditText ;
    RequestQueue requestQueue;

    public static final String TITLE = "Register";

    public static RegisterFragment newInstance() {

        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_register, container, false);

        requestQueue = Volley.newRequestQueue(getContext());

        //Assign Id'S
        First_Name = (EditText)RootView.findViewById(R.id.register_firstname);
        Last_Name = (EditText)RootView.findViewById(R.id.register_lastname);
        Email = (EditText)RootView.findViewById(R.id.register_email);
        Password = (EditText)RootView.findViewById(R.id.register_password);
        register = (Button)RootView.findViewById(R.id.email_register_button);

        //Adding Click Listener on button.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                hideKeyboardFrom(getContext(), view);

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    StringRequest stringRequest= new StringRequest(Request.Method.POST, HttpURL , new Response.Listener<String>(){
                        @Override
                        public void onResponse(String stringResponse){
                            Snackbar snackbar = Snackbar
                                    .make(view, stringResponse, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.toString());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> parameters = new HashMap<String,String>();
                            parameters.put("f_name",F_Name_Holder);
                            parameters.put("L_name",L_Name_Holder);
                            parameters.put("email",EmailHolder);
                            parameters.put("password",PasswordHolder);
                            return parameters;
                        }
                    };

                    requestQueue.add(stringRequest);

                }
                else {

                    // If EditText is empty then this block will execute .
                    Snackbar snackbar = Snackbar
                            .make(view, "Please fill all the form fields.", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
        });

        return RootView;
    }

    public void CheckEditTextIsEmptyOrNot(){

        F_Name_Holder = First_Name.getText().toString();
        L_Name_Holder = Last_Name.getText().toString();
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();

        if(TextUtils.isEmpty(F_Name_Holder) || TextUtils.isEmpty(L_Name_Holder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)) {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }
    }

}