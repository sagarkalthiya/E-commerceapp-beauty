package com.hariomgarments.pink.Fragments;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hariomgarments.pink.Activity.Activity_Home;
import com.hariomgarments.pink.Adapter.prod_adaptor;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.AndroidNetworking;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.common.Priority;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.error.ANError;
import com.hariomgarments.pink.BackEnd.AndroidNetworking.interfaces.JSONObjectRequestListener;
import com.hariomgarments.pink.BackEnd.Apis;
import com.hariomgarments.pink.Components.OnLoadMoreListener;
import com.hariomgarments.pink.Components.SessionManager;
import com.hariomgarments.pink.Model.Prod_model;
import com.hariomgarments.pink.Model.Product_model;
import com.hariomgarments.pink.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home_fragment extends Fragment {

    private TextView hair_txt, face_txt, body_txt, skin_txt;
    private RecyclerView mRecyclerView;
    private prod_adaptor mAdapter;
    private LinearLayoutManager mLayoutManager;

    private List<Prod_model> studentList;

    int count = 1;
    int cat =0;
    String skin ="skin";
    String body ="body";
    String face ="face";
    String hair ="hair";
    protected Handler handler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);

        skin_txt = (TextView) rootView.findViewById(R.id.skin_txt);
        body_txt = (TextView) rootView.findViewById(R.id.body_txt);
        face_txt = (TextView) rootView.findViewById(R.id.face_txt);
        hair_txt = (TextView) rootView.findViewById(R.id.hair_txt);

        skin_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skin_txt.setTextColor(Color.GREEN);
                body_txt.setTextColor(Color.BLACK);
                face_txt.setTextColor(Color.BLACK);
                hair_txt.setTextColor(Color.BLACK);
                cat_data(skin);
            }
        });


        body_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat=1;
                skin_txt.setTextColor(Color.BLACK);
                body_txt.setTextColor(Color.GREEN);
                face_txt.setTextColor(Color.BLACK);
                hair_txt.setTextColor(Color.BLACK);
                cat_data(body);
            }
        });


        face_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat=2;
                skin_txt.setTextColor(Color.BLACK);
                body_txt.setTextColor(Color.BLACK);
                face_txt.setTextColor(Color.GREEN);
                hair_txt.setTextColor(Color.BLACK);
                cat_data(face);

            }
        });

        hair_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cat=3;
                    skin_txt.setTextColor(Color.BLACK);
                    body_txt.setTextColor(Color.BLACK);
                    face_txt.setTextColor(Color.BLACK);
                    hair_txt.setTextColor(Color.GREEN);
                cat_data(hair);

            }
        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.product_recycler);
        studentList = new ArrayList<Prod_model>();
        handler = new Handler();


        get_data();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        // use a linear layout manager
        // mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new prod_adaptor(getActivity(), studentList, mRecyclerView);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);
        //  mAdapter.notifyDataSetChanged();


        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);
//   remove progress item
                studentList.remove(studentList.size() - 1);
                mAdapter.notifyItemRemoved(studentList.size());
                int start = studentList.size();
                if(count == 0){
                    load_more(start);
                }else {
                    if (cat==0)
                        cat_data(skin);
                    else if (cat==1)
                        cat_data(body);
                    else if (cat==2)
                        cat_data(face);
                    else if (cat==3)
                        cat_data(hair);

                }
              /*  handler.postDelayed(new Runnable() {
                   @Override
                    public void run() {


                        //add items one by one

                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);*/

            }
        });

        return rootView;
    }


    public void load_more(Integer integer){
        count=0;
        AndroidNetworking.post(Apis.fetchData)
                .addBodyParameter("offset", String.valueOf(integer))
                //.addBodyParameter("offset",  studentList.size())
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            Log.w("wwwwwwww", "-----------" + response.getString("data"));
                          //  Toast.makeText(getActivity(), "offset" + response.getString("offsetvalue"), Toast.LENGTH_SHORT).show();

                            if (response.getBoolean("data_status")){
                                if (response.getBoolean("status")) {
                                    if (studentList.size() <= 10) {
                                       //Toast.makeText(getActivity(), "Loading" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                        JSONArray valarray = new JSONArray(response.getString("data"));
                                        for (int i = 0; i < valarray.length(); i++) {
                                            JSONObject jsonobject = (JSONObject) valarray.get(i);
                                            //p personUtils.setImageUrl(jsonobject.getString("product_image"));
                                            //personUtils.setName(jsonobject.getString("product_description"));
                                            String Imageurl = jsonobject.getString("product_image");
                                            String price = jsonobject.getString("product_amount");
                                            String name = jsonobject.getString("product_name");
                                            //Toast.makeText(getActivity(), "session" + price, Toast.LENGTH_SHORT).show();
                                            studentList.add(new Prod_model(price, name, Imageurl));
                                            mAdapter.notifyItemInserted(studentList.size());
                                        }
                                        mAdapter.notifyDataSetChanged();
                                        mAdapter.setLoaded();
                                    } else {
                                        mAdapter.notifyDataSetChanged();
                                        mAdapter.setLoaded();
                                        Toast.makeText(getActivity(), "Loading data completed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    JSONArray valarray = new JSONArray(response.getString("data"));
                                    //Log.w("wwwwwwww", "-----------" + response.getString("message"));
                                    for (int i = 0; i < valarray.length(); i++) {
                                        JSONObject jsonobject = (JSONObject) valarray.get(i);
                                        Product_model personUtils = new Product_model();
                                        //p personUtils.setImageUrl(jsonobject.getString("product_image"));
                                        //personUtils.setName(jsonobject.getString("product_description"));
                                        String Imageurl = jsonobject.getString("product_image");
                                        String price = jsonobject.getString("product_amount");
                                        String name = jsonobject.getString("product_name");
                                        //Toast.makeText(getActivity(), "session" + price, Toast.LENGTH_SHORT).show();
                                        studentList.add(new Prod_model(price, name, Imageurl));
                                        mAdapter.notifyItemInserted(studentList.size());
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    mAdapter.setLoaded();
                                }
                            }else {
                                Log.w("wwwwwwww", "-----------ff" + response.getString("data_status"));
                                Toast.makeText(getActivity(), "No more dataaaaaa", Toast.LENGTH_SHORT).show();
                                mAdapter.setLoaded();
                            }


                            mAdapter.setLoaded();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        //  progressDialog.dismiss();
                        Toast.makeText(getActivity(), "No more data", Toast.LENGTH_SHORT).show();
                        Log.w("------", "msg+--" + error);
                    }
                });
        mAdapter.notifyDataSetChanged();
        mAdapter.setLoaded();
    }


    public void get_data() {
        // Toast.makeText(getActivity(), "sizw"+product_list.size(), Toast.LENGTH_SHORT).show();
        count=0;
        AndroidNetworking.post(Apis.fetchData)
                .addBodyParameter("offset", "0")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            Log.w("wwwwwwww", "-----------" + response.getString("data"));
                            Log.w("wwwwwwww", "-----------" + response.getString("offsetvalue"));
                            //Log.w("wwwwwwww", "-----------" + response.getString("data_status"));
                            if (!response.getBoolean("status")) {
                                JSONArray valarray = new JSONArray(response.getString("data"));
                                for (int i = 0; i < valarray.length(); i++) {
                                    JSONObject jsonobject = (JSONObject) valarray.get(i);
                                    //p personUtils.setImageUrl(jsonobject.getString("product_image"));
                                    //personUtils.setName(jsonobject.getString("product_description"));
                                    String Imageurl = jsonobject.getString("product_image");
                                    String price = jsonobject.getString("product_amount");
                                    String name = jsonobject.getString("product_name");
                                    //Toast.makeText(getActivity(), "session" + price, Toast.LENGTH_SHORT).show();
                                    studentList.add(new Prod_model(price, name, Imageurl));
                                }
                                mAdapter.notifyDataSetChanged();
                                mAdapter.setLoaded();
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

    public void cat_data(String catagory){
        // Toast.makeText(getActivity(), "sizw"+product_list.size(), Toast.LENGTH_SHORT).show();
        mAdapter.clear();
        count = 0;
      //studentList.clear();
        Toast.makeText(getActivity(), catagory +studentList.size(), Toast.LENGTH_SHORT).show();
        mAdapter.notifyDataSetChanged();
        AndroidNetworking.post(Apis.fetchData)
                .addBodyParameter("offset", studentList.size())
                .addBodyParameter("catagory" ,catagory)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            Log.w("wwwwwwww", "-----------" + response.getString("data"));
                            if (!response.getBoolean("status")) {
                                JSONArray valarray = new JSONArray(response.getString("data"));
                                for (int i = 0; i < valarray.length(); i++) {
                                    JSONObject jsonobject = (JSONObject) valarray.get(i);
                                    //p personUtils.setImageUrl(jsonobject.getString("product_image"));
                                    //personUtils.setName(jsonobject.getString("product_description"));
                                    String Imageurl = jsonobject.getString("product_image");
                                    String price = jsonobject.getString("product_amount");
                                    String name = jsonobject.getString("product_name");
                                    //Toast.makeText(getActivity(), "session" + price, Toast.LENGTH_SHORT).show();
                                    studentList.add(new Prod_model(price, name, Imageurl));
                                }
                                mAdapter.notifyDataSetChanged();
                                mAdapter.setLoaded();
                            }else {
                                JSONArray valarray = new JSONArray(response.getString("data"));
                                for (int i = 0; i < valarray.length(); i++) {
                                    JSONObject jsonobject = (JSONObject) valarray.get(i);
                                    //p personUtils.setImageUrl(jsonobject.getString("product_image"));
                                    //personUtils.setName(jsonobject.getString("product_description"));
                                    String Imageurl = jsonobject.getString("product_image");
                                    String price = jsonobject.getString("product_amount");
                                    String name = jsonobject.getString("product_name");
                                    //Toast.makeText(getActivity(), "session" + price, Toast.LENGTH_SHORT).show();
                                    studentList.add(new Prod_model(price, name, Imageurl));
                                }

                            }
                            mAdapter.notifyDataSetChanged();
                            mAdapter.setLoaded();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        //  progressDialog.dismiss();
                        Toast.makeText(getActivity(), "catagory---"+"No more data", Toast.LENGTH_SHORT).show();
                        Log.w("------", "msg+--" + error);
                    }
                });

    }
}
