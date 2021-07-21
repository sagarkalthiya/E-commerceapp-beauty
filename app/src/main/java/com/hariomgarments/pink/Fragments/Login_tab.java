package com.hariomgarments.pink.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.hariomgarments.pink.Activity.Activity_Home;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.AndroidNetworking;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.common.Priority;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.error.ANError;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.interfaces.JSONObjectRequestListener;
import com.hariomgarments.pink.BackEnd.Apis;
import com.hariomgarments.pink.Components.SessionManager;
import com.hariomgarments.pink.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_tab extends Fragment {

    EditText  et_email, et_password;
    Button login_btn;
    TextView forget_pass;
    ProgressDialog progressDialog;
    SessionManager session;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_login, null);

        et_email = (EditText) rootView.findViewById(R.id.email);
        et_password = (EditText) rootView.findViewById(R.id.pass);
        login_btn = (Button) rootView.findViewById(R.id.login_btn);
        forget_pass = (TextView) rootView.findViewById(R.id.forget_pass);

        session = new SessionManager(getActivity());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("please wait..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);


        ((View) et_email).setTranslationX(800);
        ((View) et_password).setTranslationX(800);
        ((View) forget_pass).setTranslationX(800);
        ((View) login_btn).setTranslationX(800);

        float v =1;
        et_email.setAlpha(v);
        et_password.setAlpha(v);
        forget_pass.setAlpha(v);
        login_btn.setAlpha(v);

        et_email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        et_password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forget_pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = et_email.getText().toString();
                final String pass = et_password.getText().toString();
                Toast.makeText(getActivity(), "session" + pass, Toast.LENGTH_SHORT).show();
                Log.w("------", "msg+--" + pass);
                if (!isValidEmail(email)) {
                    et_email.setError("Invalid Email");
                } else if (!isValidPassword(pass)) {
                    et_password.setError("Invalid Password or Password must be 8 character");
                } else {
                    //  network compiler post the method on server
                    progressDialog.show();
                    AndroidNetworking.post(Apis.Login)
                            .addBodyParameter("email", email)
                            .addBodyParameter("password", pass)
                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // do anything with response
                                    try {
                                        progressDialog.dismiss();
                                        Log.w("wwwwwwww", "resss+--" + response.getString("data"));
                                        if (response.getBoolean("status")) {
                                            Log.w("wwwwwwww", "resss+--" + response.getBoolean("status"));
                                            if (response.getBoolean("status")){
                                                JSONObject data = new JSONObject(response.getString("data"));

                                               String user_id = data.getString("user_id");
                                                String user_name = data.getString("name");
                                                String user_email = data.getString("email");
                                                String password = data.getString("password");
                                                String mobile = data.getString("mobile");

                                                session.createLoginSession(user_id,user_name, user_email,password,mobile);
                                                Toast.makeText(getActivity(), "session" + data, Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();

                                                Intent intent = new Intent(getContext() , Activity_Home.class);
                                                startActivity(intent);

                                            }else {
                                                progressDialog.dismiss();
                                                //Snackbar.make(getView(),response.getString("message") , Snackbar.LENGTH_LONG).show();
                                                Toast.makeText(getActivity(), "session" +  response.getString("message"), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError error) {
                                    // handle error
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "session" + error, Toast.LENGTH_SHORT).show();
                                    Log.w("------", "msg+--" + error);
                                }
                            });

                }
            }
        });
        return rootView;
    }


    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{3,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 7) {
            return true;
        }
        return false;
    }


}