package com.hariomgarments.pink.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hariomgarments.pink.Adapter.Mycart_adaptor;
import com.hariomgarments.pink.Adapter.Product_adaptor;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.AndroidNetworking;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.common.Priority;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.error.ANError;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.interfaces.JSONObjectRequestListener;
import com.hariomgarments.pink.BackEnd.Apis;
import com.hariomgarments.pink.Components.SessionManager;
import com.hariomgarments.pink.Model.My_cart_model;
import com.hariomgarments.pink.Model.Product_model;
import com.hariomgarments.pink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class My_cart_fragment extends Fragment {

    //Creating a List of superheroes
    public static ArrayList<My_cart_model> MYproduct_list = new ArrayList<>();
    public static TextView badge_txt;
    public static int grandTotalplus;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Mycart_adaptor adapter;

    String product_idd;

    String porduct_quntity,name,price,discription,image,product_id,user_ID;
    Integer quntity;
    TextView totaoprice;
    SessionManager session;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_cart, null);

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

        session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        // name
        user_ID = user.get(SessionManager.KEY_USER_ID);

        totaoprice = (TextView) rootView.findViewById(R.id.totaoprice);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_mycart);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        get_data(user_ID);
        adapter = new Mycart_adaptor(getActivity(), MYproduct_list ,My_cart_fragment.this);
        recyclerView.setAdapter(adapter);
        return rootView;
    }


    public void get_data(String id){
        //Toast.makeText(getActivity(), "sizw"+product_list.size(), Toast.LENGTH_SHORT).show();
        AndroidNetworking.post(Apis.My_basket)
                .addBodyParameter("user_id",id)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            adapter.clear();
                            //Toast.makeText(getActivity(), "session" + response.getBoolean("status"), Toast.LENGTH_SHORT).show();

                            //Toast.makeText(getActivity(), "session" + response.getString("message"), Toast.LENGTH_SHORT).show();
                            int total_sum = 0;
                            if (response.getBoolean("status")) {

                                JSONArray valarray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < valarray.length(); i++) {
                                    JSONObject jsonobject= (JSONObject) valarray.get(i);
                                    Log.w("wwwwwwww", "-----------" +jsonobject.getString("basket_id"));
                                    My_cart_model personUtils = new My_cart_model();
                                    porduct_quntity =jsonobject.getString("product_quntity");
                                    personUtils.setUser_ID(jsonobject.getString("user_id"));
                                    personUtils.setBasketID(jsonobject.getString("basket_id"));
                                    personUtils.setProductID(jsonobject.getString("product_id"));
                                    personUtils.setImageUrl(jsonobject.getString("product_image"));
                                    personUtils.setPrice(jsonobject.getString("product_amount"));
                                    personUtils.setName(jsonobject.getString("product_name"));
                                    personUtils.setDescription(jsonobject.getString("product_description"));
                                    MYproduct_list.add(personUtils);

                                    total_sum += Integer.parseInt(porduct_quntity)  * Integer.parseInt(jsonobject.getString("product_amount"));
                                }
                                adapter.notifyDataSetChanged();
                                totaoprice.setText("TOTAL PRICE : $ " + total_sum + " USD");
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


    private void changeFragment(Fragment targetFragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.HomeFrameLayout, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                //.addToBackStack("my_fragment")
                .commit();
    }



}
