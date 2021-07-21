package com.hariomgarments.pink.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hariomgarments.pink.Activity.Activity_Home;
import com.hariomgarments.pink.Adapter.Catagory_adaptor;
import com.hariomgarments.pink.Adapter.Product_adaptor;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.AndroidNetworking;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.common.Priority;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.error.ANError;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.interfaces.JSONObjectRequestListener;
import com.hariomgarments.pink.BackEnd.Apis;
import com.hariomgarments.pink.Components.OnLoadMoreListener;
import com.hariomgarments.pink.Model.Product_model;
import com.hariomgarments.pink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Product_fragment extends Fragment implements Product_adaptor.OnItemClick {

    //Creating a List of superheroes
    ArrayList<Product_model> product_list = new ArrayList<>();

    //Creating Views
    private RecyclerView recyclerView,catagory_rv;
    private RecyclerView.LayoutManager layoutManager;
    private Product_adaptor adapter;
    private Catagory_adaptor catagory_adaptor;

    public static int cart_count = 0;
    String product_idd;

    private TextView hair_txt, makeup, body_txt, all_products,badge_txt;
    CircleImageView profile_imgg;
    Button cart_btn;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);

        //Initializing Views
        recyclerView = (RecyclerView) rootView.findViewById(R.id.product_recycler);

        catagory_rv = (RecyclerView) rootView.findViewById(R.id.cat_recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        /*catagory_rv = (RecyclerView) rootView.findViewById(R.id.cat_recycler);
        catagory_rv.setHasFixedSize(true);
        RecyclerView.LayoutManager cat_layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        catagory_rv.setLayoutManager(cat_layoutManager);*/

        get_data();

        badge_txt = (TextView) rootView.findViewById(R.id.badge_count_txt);
        all_products = (TextView) rootView.findViewById(R.id.skin_txt);
        body_txt = (TextView) rootView.findViewById(R.id.body_txt);
        makeup = (TextView) rootView.findViewById(R.id.face_txt);
        hair_txt = (TextView) rootView.findViewById(R.id.hair_txt);
        cart_btn = (Button) rootView.findViewById(R.id.cart_btn);

        profile_imgg = (CircleImageView) rootView.findViewById(R.id.profileimg);


        cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new My_cart_fragment());
            }
        });

        all_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_products.setTextColor(Color.GREEN);
                body_txt.setTextColor(Color.BLACK);
                makeup.setTextColor(Color.BLACK);
                hair_txt.setTextColor(Color.BLACK);
                adapter.clear();
                get_data();
            }
        });


        body_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_products.setTextColor(Color.BLACK);
                body_txt.setTextColor(Color.GREEN);
                makeup.setTextColor(Color.BLACK);
                hair_txt.setTextColor(Color.BLACK);
                cat_data("body");
            }
        });


        makeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_products.setTextColor(Color.BLACK);
                body_txt.setTextColor(Color.BLACK);
                makeup.setTextColor(Color.GREEN);
                hair_txt.setTextColor(Color.BLACK);
                cat_data("face");

            }
        });

        hair_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_products.setTextColor(Color.BLACK);
                body_txt.setTextColor(Color.BLACK);
                makeup.setTextColor(Color.BLACK);
                hair_txt.setTextColor(Color.GREEN);
                cat_data("hair");

            }
        });

        profile_imgg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new Profile_fragment());
            }
        });





        adapter = new Product_adaptor(getActivity(), product_list, Product_fragment.this);
        recyclerView.setAdapter(adapter);

       // catagory_adaptor = new Catagory_adaptor(getActivity(), product_list);
        //catagory_rv.setAdapter(catagory_adaptor);

        searchView = (SearchView) rootView.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

              /*  if(product_list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(getActivity(), "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                filter(newText);
                return false;
            }
        });

        My_cart_fragment.MYproduct_list.size();

        //Toast.makeText(getActivity(), "size>>>>"+ My_cart_fragment.MYproduct_list.size(), Toast.LENGTH_SHORT).show();
       String qunntity = String.valueOf(My_cart_fragment.MYproduct_list.size());
       if (qunntity.equals("0")){
           cart_btn.setText("Your cart is Empty");
           badge_txt.setText("0");
          // Toast.makeText(getActivity(), "dfdsfadsfas>>>>"+ My_cart_fragment.MYproduct_list.size(), Toast.LENGTH_SHORT).show();
       }else {
           cart_btn.setText("Your cart is have " +qunntity);
           badge_txt.setText(String.valueOf(My_cart_fragment.MYproduct_list.size()));
       }


        return rootView;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Product_model> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Product_model item : product_list) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    public void get_data(){
        //Toast.makeText(getActivity(), "sizw"+product_list.size(), Toast.LENGTH_SHORT).show();
        AndroidNetworking.post(Apis.fetchData)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            //Toast.makeText(getActivity(), "session" + response.getBoolean("status"), Toast.LENGTH_SHORT).show();
                            Log.w("wwwwwwww", "-----------" +response.getString("data"));
                            //Toast.makeText(getActivity(), "session" + response.getString("message"), Toast.LENGTH_SHORT).show();
                            adapter.clear();
                            if (response.getBoolean("status")) {

                                JSONArray valarray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < valarray.length(); i++) {
                                    JSONObject jsonobject= (JSONObject) valarray.get(i);
                                    Product_model personUtils = new Product_model();
                                    personUtils.setProductID(jsonobject.getString("id"));
                                    personUtils.setImageUrl(jsonobject.getString("product_image"));
                                    personUtils.setPrice(jsonobject.getString("product_amount"));
                                    personUtils.setName(jsonobject.getString("product_name"));
                                    personUtils.setCatagory(jsonobject.getString("sub_catagory"));
                                   // Toast.makeText(getActivity(), "session" + jsonobject, Toast.LENGTH_SHORT).show();
                                    product_list.add(personUtils);
                                }
                                adapter.notifyDataSetChanged();
                                //catagory_adaptor.notifyDataSetChanged();

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

    public void cat_data(String s){
        adapter.clear();
      //  Toast.makeText(getActivity(), "sizw"+product_list.size(), Toast.LENGTH_SHORT).show();
        AndroidNetworking.post(Apis.fetchData)
                .addBodyParameter("catagory", s)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                          //  Toast.makeText(getActivity(), "session" + response.getBoolean("status"), Toast.LENGTH_SHORT).show();
                          //  Log.w("wwwwwwww", "-----------" +response.getString("data"));
                            //Toast.makeText(getActivity(), "session" + response.getString("message"), Toast.LENGTH_SHORT).show();

                            if (response.getBoolean("status")) {

                                JSONArray valarray = new JSONArray(response.getString("data"));

                                for (int i = 0; i < valarray.length(); i++) {
                                    JSONObject jsonobject= (JSONObject) valarray.get(i);
                                    Product_model personUtils = new Product_model();
                                    personUtils.setProductID(jsonobject.getString("id"));
                                    personUtils.setImageUrl(jsonobject.getString("product_image"));
                                    personUtils.setPrice(jsonobject.getString("product_amount"));
                                    personUtils.setName(jsonobject.getString("product_name"));
                                    // Toast.makeText(getActivity(), "session" + jsonobject, Toast.LENGTH_SHORT).show();
                                    product_list.add(personUtils);
                                }
                                adapter.notifyDataSetChanged();

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


    @Override
    public void onClick(String value) {
        product_idd = value;
    }

    private void changeFragment(Fragment targetFragment){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.HomeFrameLayout, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
