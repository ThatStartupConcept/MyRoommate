package com.myroommate.myroommate;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class LoginFragment extends Fragment {

    Button login;
    EditText Email, Password ;
    String EmailHolder, PasswordHolder;
    String finalResult ;
    String HttpURL = "https://myroommate.000webhostapp.com/UserLogin.php";
    Boolean CheckEditText ;
    ProgressDialog progressDialog;
    HashMap<String,String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();

    public static final String TITLE = "Login";

    public static LoginFragment newInstance() {

        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_login, container, false);

        //Assign Id'S
        Email = (EditText)RootView.findViewById(R.id.login_email);
        Password = (EditText)RootView.findViewById(R.id.login_password);
        login = (Button)RootView.findViewById(R.id.email_login_button);

        //Adding Click Listener on button.
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Checking whether EditText is Empty or Not
                CheckEditTextIsEmptyOrNot();

                if(CheckEditText){

                    // If EditText is not empty and CheckEditText = True then this block will execute.

                    UserLoginFunction(EmailHolder, PasswordHolder);

                }
                else {

                    // If EditText is empty then this block will execute .
                    Toast.makeText(getActivity(), "Please fill all the form fields.", Toast.LENGTH_LONG).show();

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

    public void UserLoginFunction(final String email, final String password){

        class UserLoginFunctionClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(getActivity(),"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(getActivity(),httpResponseMsg, Toast.LENGTH_LONG).show();

                if(httpResponseMsg.equals("S"));
            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("email",params[0]);

                hashMap.put("password",params[1]);

                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }

        UserLoginFunctionClass userLoginFunctionClass = new UserLoginFunctionClass();

        userLoginFunctionClass.execute(email,password);
    }


}