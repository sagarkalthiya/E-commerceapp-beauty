package com.hariomgarments.pink.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.AndroidNetworking;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.common.Priority;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.error.ANError;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.interfaces.JSONObjectRequestListener;
import com.hariomgarments.pink.BackEnd.Apis;
import com.hariomgarments.pink.Fragments.My_cart_fragment;
import com.hariomgarments.pink.Model.Catagory_model;
import com.hariomgarments.pink.Model.My_cart_model;
import com.hariomgarments.pink.Model.Prod_model;
import com.hariomgarments.pink.Model.Product_model;
import com.hariomgarments.pink.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Catagory_adaptor extends RecyclerView.Adapter<Catagory_adaptor.ExampleViewHolder> {
    private ArrayList<Product_model> mExampleList;
    Context context;
    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    public String basket_id;
    ProgressDialog progressDialog;
  // My_cart_fragment fragment;


    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.cat_name);

        }
    }

    public Catagory_adaptor(Context context, ArrayList<Product_model> exampleList) {
        mExampleList = exampleList;
        this.context = context;
        //this.fragment = fragment;
    }

    @Override
    public Catagory_adaptor.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catagory_row_layout, parent, false);
        Catagory_adaptor.ExampleViewHolder evh = new Catagory_adaptor.ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(Catagory_adaptor.ExampleViewHolder holder, int position) {
        final Product_model currentItem = mExampleList.get(position);

        //  holder.img_android.setImageResource(currentItem.getProduct_Photo_Path());
        holder.tvName.setText(currentItem.getCatagory());


    }


    public void clear() {
        mExampleList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


}