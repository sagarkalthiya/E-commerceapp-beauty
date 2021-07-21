package com.hariomgarments.pink.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hariomgarments.pink.Components.OnLoadMoreListener;
import com.hariomgarments.pink.Fragments.Home_fragment;
import com.hariomgarments.pink.Fragments.Product_details_fragment;
import com.hariomgarments.pink.Fragments.Product_fragment;
import com.hariomgarments.pink.Model.Product_model;
import com.hariomgarments.pink.R;

import org.jetbrains.annotations.NotNull;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Product_adaptor extends RecyclerView.Adapter<Product_adaptor.ExampleViewHolder> {
    private ArrayList<Product_model> mExampleList;
    Context context;
    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;
    Fragment fg;

    public interface product_id_interface{
        void sendvalue(String product_id);
    }
    private product_id_interface callback;

    private OnItemClick mCallback;

    public interface OnItemClick {
        void onClick(String value);
    }



    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView pricetxt;
        public ImageView Image;
        public RelativeLayout relativeLayout;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.prod_name);
            pricetxt = (TextView) itemView.findViewById(R.id.prod_price);
            Image = (ImageView) itemView.findViewById(R.id.prod_image);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.content_main);

        }
    }

    public Product_adaptor( Context context,ArrayList<Product_model> exampleList,OnItemClick listener) {
        mExampleList = exampleList;
        this.context = context;
        this.mCallback = listener;

    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_product_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        final Product_model currentItem = mExampleList.get(position);

        //  holder.img_android.setImageResource(currentItem.getProduct_Photo_Path());
        holder.tvName.setText(currentItem.getName());
        holder.pricetxt.setText(currentItem.getPrice());
        //holder.pro_id_tv.setText(currentItem.getProduct_Id());

        final String proId =currentItem.getProductID();

        Glide.with(context)
                .load(currentItem.getImageUrl())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.Image);


        holder.relativeLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // get position
                // check if item still exists
                Product_details_fragment fragmentB=new Product_details_fragment();
                Bundle bundle=new Bundle();
                bundle.putString("PRODUCT_ID",currentItem.getProductID());
                fragmentB.setArguments(bundle);
                mFragmentManager = ((AppCompatActivity) context).getSupportFragmentManager() ;
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.HomeFrameLayout, fragmentB);
                mFragmentTransaction.addToBackStack(null).commit();
                String clickedDataItem =currentItem.getProductID();
                //  Toast.makeText(v.getContext(), "You clicked " + clickedDataItem, Toast.LENGTH_SHORT).show();
                mCallback.onClick(clickedDataItem);

            }
        });

      /*  holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              callback.sendvalue(currentItem.getProduct_Id());

            }
        });*/
    }


    public void clear() {
        mExampleList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public void filterList(ArrayList<Product_model> filteredList) {
        mExampleList = filteredList;
        notifyDataSetChanged();
    }




}