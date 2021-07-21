package com.hariomgarments.pink.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration_tab extends Fragment {

    EditText et_name, et_email, et_mobile, et_password, et_confirm_pass;
    Button login_btn;
    TextView forget_pass;
    ProgressDialog progressDialog;
    SessionManager session;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_signup, null);

        et_name = (EditText) rootView.findViewById(R.id.name);
        et_email = (EditText) rootView.findViewById(R.id.et_email);
        et_mobile = (EditText) rootView.findViewById(R.id.mob);
        et_password = (EditText) rootView.findViewById(R.id.s_pass);
        et_confirm_pass = (EditText) rootView.findViewById(R.id.c_pass);

        login_btn = (Button) rootView.findViewById(R.id.signin_btn);

        session = new SessionManager(getActivity());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("please wait..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = et_name.getText().toString();
                final String email = et_email.getText().toString();
                final String mobile = et_mobile.getText().toString();
                final String pass = et_password.getText().toString();
                final String conform_pass = et_confirm_pass.getText().toString();

                if(name.isEmpty()){
                    et_name.setError("Enter a name");
                } else if (!isValidEmail(email)) {
                    et_email.setError("Invalid Email");
                } else if (!isValidMobile(mobile)) {
                    et_mobile.setError("Invalid mobile");
                } else if (!isValidPassword(pass)) {
                    et_password.setError("Invalid Password or Password must be 8 character");
                } else if(!pass.equals(conform_pass)){
                    et_confirm_pass.setError("Invalid Password");
                } else {
                    progressDialog.show();
                    AndroidNetworking.post(Apis.signup)
                            .addBodyParameter("name", name)
                            .addBodyParameter("email", email)
                            .addBodyParameter("password", pass)
                            .addBodyParameter("mobile", mobile)
                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // do anything with response
                                    progressDialog.dismiss();
                                    Log.w("------", "msg response--" + response);
                                    //Toast.makeText(getActivity(), "session" + response, Toast.LENGTH_SHORT).show();

                                    try {

                                        if (response.getBoolean("status")) {
                                            Log.w("wwwwwwww", "resss+--" + response.getBoolean("status"));
                                            if (response.getBoolean("account")){
                                                JSONObject data = new JSONObject(response.getString("data"));
                                                String user_id = data.getString("id");
                                                String user_name = data.getString("name");
                                                String user_email = data.getString("email");
                                                String user_password = data.getString("password");
                                                String mobile = data.getString("mobile");

                                                session.createLoginSession(user_id,user_name, user_email,user_password, mobile);
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(getContext() , Activity_Home.class);
                                                startActivity(intent);

                                            }else {
                                                progressDialog.dismiss();
                                                Snackbar.make(getView(),response.getString("message") , Snackbar.LENGTH_LONG).show();
                                               // Toast.makeText(getActivity(), "session" +  response.getString("message"), Toast.LENGTH_SHORT).show();
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

    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

}