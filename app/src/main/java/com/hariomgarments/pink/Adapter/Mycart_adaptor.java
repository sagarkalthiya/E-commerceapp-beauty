package com.hariomgarments.pink.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.AndroidNetworking;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.common.Priority;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.error.ANError;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.interfaces.JSONObjectRequestListener;
import com.hariomgarments.pink.BackEnd.Apis;
import com.hariomgarments.pink.Fragments.My_cart_fragment;
import com.hariomgarments.pink.Fragments.Product_details_fragment;
import com.hariomgarments.pink.Model.My_cart_model;
import com.hariomgarments.pink.Model.Product_model;
import com.hariomgarments.pink.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Mycart_adaptor extends RecyclerView.Adapter<Mycart_adaptor.ExampleViewHolder> {
    private ArrayList<My_cart_model> mExampleList;
    Context context;
    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    public String basket_id;
    ProgressDialog progressDialog;
    My_cart_fragment fragment;


    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView pricetxt;
        public TextView discriptiontxt;
        public ImageView Image,remove_img;
        public RelativeLayout relativeLayout;


        public ExampleViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.basket_product_name);
            pricetxt = (TextView) itemView.findViewById(R.id.basket_product_price_txt);
            discriptiontxt = (TextView) itemView.findViewById(R.id.basket_product_discription_txt);
            Image = (ImageView) itemView.findViewById(R.id.basket_product_img);
            remove_img = (ImageView) itemView.findViewById(R.id.removebtn);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.content_main);

        }
    }

    public Mycart_adaptor(Context context, ArrayList<My_cart_model> exampleList,My_cart_fragment fragment) {
        mExampleList = exampleList;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public Mycart_adaptor.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_mycart_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(Mycart_adaptor.ExampleViewHolder holder, int position) {
        final My_cart_model currentItem = mExampleList.get(position);

        //  holder.img_android.setImageResource(currentItem.getProduct_Photo_Path());
        holder.tvName.setText(currentItem.getName());
        holder.pricetxt.setText(currentItem.getPrice());
        holder.discriptiontxt.setText(currentItem.getDescription());
        holder.remove_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ; //get the position of the view to delete stored in the tag
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Loading..."); // Setting Message
                progressDialog.setTitle("please wait..."); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressDialog.setCancelable(false);
                send_product(position,currentItem.getUser_ID(),currentItem.getBasketID());
            }
        });
        //holder.pro_id_tv.setText(currentItem.getProduct_Id());

        //basket_id =currentItem.getBasketID();

        Glide.with(context)
                .load(currentItem.getImageUrl())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.Image);



    }

    public void removeAt(Integer position) {
        mExampleList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        mExampleList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public void filterList(ArrayList<My_cart_model> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }


    public void send_product(int id_position, String user_id, String basket_id) {
       // Toast.makeText(context, "user_id"+ user_id +"basket iDD"+basket_id, Toast.LENGTH_SHORT).show();
        mExampleList.remove(id_position);
        notifyDataSetChanged();
        progressDialog.show();
        AndroidNetworking.post(Apis.Remove_from_basket)
                .addBodyParameter("basket_id", basket_id)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            if (response.getBoolean("status")) {
                                Log.w("------", "msg+--" + response.getString("message"));
                                removeAt(id_position);
                                Toast.makeText(context, "Item remove Succesfully", Toast.LENGTH_SHORT).show();
                                fragment.get_data(user_id);
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                //Snackbar.make(context, response.getString("message"), Snackbar.LENGTH_LONG).show();
                                 Toast.makeText(context, "Item not remove" +  response.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                        Toast.makeText(context, "session" + error, Toast.LENGTH_SHORT).show();
                        Log.w("------", "msg+--" + error);
                    }
                });
    }



}