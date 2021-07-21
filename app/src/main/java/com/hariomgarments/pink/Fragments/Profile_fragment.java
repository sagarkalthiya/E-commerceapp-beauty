package com.hariomgarments.pink.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.hariomgarments.pink.Components.RoundedImg;
import com.hariomgarments.pink.Components.SessionManager;
import com.hariomgarments.pink.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Profile_fragment extends Fragment {

    SessionManager session;
    String user_ID,user_name,user_email,user_password,user_mobile,img_base64_string;
    TextView profile_name,profile_email;
    CircleImageView user_profile_img;
    RoundedImg roundedImage;

    Bitmap bitmap;

    private static final int REQUEST_CAMERA = 2;
      private static final int PICK_GALLERY_IMAGE = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, null);
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
        user_name = user.get(SessionManager.KEY_NAME);
        user_email = user.get(SessionManager.KEY_EMAIL);
        user_password = user.get(SessionManager.KEY_PASSWORD);
        user_mobile = user.get(SessionManager.KEY_Mobile);

        profile_name = (TextView) rootView.findViewById(R.id.profile_name_txt);
        profile_email = (TextView) rootView.findViewById(R.id.profile_email);
        user_profile_img = (CircleImageView) rootView.findViewById(R.id.user_profile_img);
        profile_name.setText(user_name);
        profile_email.setText(user_email);

        user_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Add Photo!");

                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override

                    public void onClick(DialogInterface dialog, int item) {

                        if (options[item].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        } else if (options[item].equals("Choose from Gallery")) {
                            Intent intentGalley = new Intent(Intent.ACTION_PICK);
                            intentGalley.setType("image/*");
                            startActivityForResult(intentGalley, PICK_GALLERY_IMAGE);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }

                    }

                });

                builder.show();
            }
        });


        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_GALLERY_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    img_base64_string = getEncoded64ImageStringFromBitmap(bitmap);
                    Log.v("aaaaa", "data:image/png;base64," + img_base64_string);

                        roundedImage = new RoundedImg(BitmapFactory.decodeFile(picturePath));
                        user_profile_img.setImageDrawable(roundedImage);



                }
                break;

            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");


                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    img_base64_string = getEncoded64ImageStringFromBitmap(bitmap);
                    Log.v("camera", "data:image/png;base64," + img_base64_string);

                        roundedImage = new RoundedImg(bitmap);
                    user_profile_img.setImageDrawable(roundedImage);


                }
            default:
                break;
        }
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
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
