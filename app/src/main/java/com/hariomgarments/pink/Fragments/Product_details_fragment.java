package com.hariomgarments.pink.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.hariomgarments.pink.Activity.Activity_Home;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.AndroidNetworking;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.common.Priority;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.error.ANError;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.interfaces.JSONObjectRequestListener;
import com.hariomgarments.pink.BackEnd.Apis;
import com.hariomgarments.pink.Components.SessionManager;
import com.hariomgarments.pink.Model.Product_model;
import com.hariomgarments.pink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Product_details_fragment extends Fragment {

    TextView prod_name, prod_price, prod_discription, displayInteger;
    ImageView prod_imag, increment, decrement;
    String name, price, discription, image, product_id, user_ID;
    int minteger = 0;
    Integer quntity;
    Button addtocart_btn;
    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;

    SessionManager session;
    ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_product_details, null);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        changeFragment(new Product_fragment());
                        return true;
                    }
                }
                return false;
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("please wait..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);

        session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        // name
        user_ID = user.get(SessionManager.KEY_USER_ID);

        product_id = getArguments().getString("PRODUCT_ID");
       // Toast.makeText(getContext(), "product id---" + product_id + "/nuser id" + user_ID, Toast.LENGTH_SHORT).show();
        get_data(product_id);

        prod_imag = (ImageView) rootView.findViewById(R.id.prod_img);
        prod_name = (TextView) rootView.findViewById(R.id.prod_name);
        prod_price = (TextView) rootView.findViewById(R.id.prod_price);
        prod_discription = (TextView) rootView.findViewById(R.id.prod_discription);
        displayInteger = (TextView) rootView.findViewById(R.id.quntity_txt);
        increment = (ImageView) rootView.findViewById(R.id.plus_btn);
        decrement = (ImageView) rootView.findViewById(R.id.minus_btn);
        addtocart_btn = (Button) rootView.findViewById(R.id.addcart_btn);


        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseInteger(v);
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseInteger(v);
            }
        });

        addtocart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quntity == null) {
                    Toast.makeText(getContext(), "please select a quantity ", Toast.LENGTH_SHORT).show();
                } else {
                    send_product();

                }
            }
        });


        return rootView;
    }


    public void get_data(String id) {
        //Toast.makeText(getActivity(), "sizw"+product_list.size(), Toast.LENGTH_SHORT).show();
        AndroidNetworking.post(Apis.productdetails)
                .addBodyParameter("id", id)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            //Toast.makeText(getActivity(), "session" + response.getBoolean("status"), Toast.LENGTH_SHORT).show();
                            Log.w("wwwwwwww", "-----------" + response.getString("data"));
                            //Toast.makeText(getActivity(), "session" + response.getString("message"), Toast.LENGTH_SHORT).show();

                            if (response.getBoolean("status")) {

                                JSONArray valarray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < valarray.length(); i++) {
                                    JSONObject jsonobject = (JSONObject) valarray.get(i);
                                   // product_id = jsonobject.getString("id");
                                    image = jsonobject.getString("product_image");
                                    name = jsonobject.getString("product_name");
                                    price = jsonobject.getString("product_amount");
                                    discription = jsonobject.getString("product_description");

                                }
                                Glide.with(getActivity())
                                        .load(image)
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(prod_imag);

                                prod_name.setText(name);
                                prod_price.setText("Rs " + price);
                                prod_discription.setText(discription);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        //  progressDialog.dismiss();
                        Toast.makeText(getActivity(), "session" + error, Toast.LENGTH_SHORT).show();
                        Log.w("------", "msg+--" + error);
                    }
                });

    }

    public void increaseInteger(View view) {
        minteger = minteger + 1;
        minteger = Math.min(minteger++, 10);
        display(minteger);

    }

    public void decreaseInteger(View view) {
        minteger = minteger - 1;
        minteger = Math.max(minteger--, 1);
        display(minteger);
    }

    private void display(int number) {

        displayInteger.setText("" + number);
        quntity = number;
    }

    public void send_product() {

       // Toast.makeText(getActivity(), "session" + user_ID+"p id"+product_id+ "quntity"+quntity , Toast.LENGTH_SHORT).show();
        progressDialog.show();
        AndroidNetworking.post(Apis.Add_to_cart)
                .addBodyParameter("user_id", user_ID)
                .addBodyParameter("product_id", product_id)
                .addBodyParameter("product_quntity", String.valueOf(quntity))
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if (response.getBoolean("status")) {
                                progressDialog.dismiss();
                                changeFragment(new My_cart_fragment());

                            } else {
                                progressDialog.dismiss();
                                //Snackbar.make(getView(), response.getString("message"), Snackbar.LENGTH_LONG).show();
                                 Toast.makeText(getActivity(), "errot" +  response.getString("message"), Toast.LENGTH_SHORT).show();
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


    private void changeFragment(Fragment targetFragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.HomeFrameLayout, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                //.addToBackStack("my_fragment")
                .commit();
    }

}
