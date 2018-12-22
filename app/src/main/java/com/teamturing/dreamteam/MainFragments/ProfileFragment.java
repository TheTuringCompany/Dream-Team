package com.teamturing.dreamteam.MainFragments;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teamturing.dreamteam.EditProfileActivity;
import com.teamturing.dreamteam.LoginActivity;
import com.teamturing.dreamteam.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    ChipGroup skills;

    public ProfileFragment() {
        // Required empty public constructor
    }
    private static final int RESULT_LOAD_IMAGE = 100;
    private static final int RESULT_LOAD_COVER = 101;
    ImageView propic;
    ImageView coverpic;
    View rootview;
    TextView logout,email,organisation,designation,name,editProfile,githublink,contact,aboutme;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    //Pushing
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_profile,container,false);
        propic = (ImageView) rootview.findViewById(R.id.profile_pic);
        logout = rootview.findViewById(R.id.logOutBtn);
        auth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Techie").child(auth.getCurrentUser().getUid());
        storageReference= FirebaseStorage.getInstance().getReference().child("ProfileImages/").child(auth.getCurrentUser().getUid());
        aboutme=rootview.findViewById(R.id.aboutme);
        email=rootview.findViewById(R.id.email);
        organisation=rootview.findViewById(R.id.organization);
        designation=rootview.findViewById(R.id.designation);
        name=rootview.findViewById(R.id.user_profile_name);
        skills = rootview.findViewById(R.id.skillGroup);
        editProfile=rootview.findViewById(R.id.edit_profile);
        githublink=rootview.findViewById(R.id.githublink);
        contact=rootview.findViewById(R.id.contact);
        getData();
        propic.setImageResource(R.drawable.loadingimgjpg);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra("loginType","edit");
                startActivity(intent);
            }
        });
        propic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        coverpic = rootview.findViewById(R.id.header_cover_image);
        coverpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_COVER);
            }
        });




        /*listview remove on firebase integration
        ListView skillview = (ListView) rootview.findViewById(R.id.skillsview);
        String skillset[] = {"Internet of Things","Android Development","Embedded C/C++"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, skillset);
        skillview.setAdapter(adapter);
        setListViewHeightBasedOnChildren(skillview);

        */
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return rootview;
    }
     public void   getData(){
         storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
             @Override
             public void onSuccess(Uri uri) {


                 Glide.with(getContext()).load(uri.toString()).into(propic);
                 Log.e("imageuriString",uri.toString());
             }
         });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                String strcontact,stremail,strname,strorganisation,strprofession,namestr;String aboutmestr="hi";
                ArrayList<String> skillsList=new ArrayList<String>();
               // DataSnapshot dataSnapshot1
                    strcontact=dataSnapshot1.child("contact").getValue().toString();
                    stremail=dataSnapshot1.child("email").getValue().toString();
                    strorganisation=dataSnapshot1.child("organisation").getValue().toString();
                    strprofession=dataSnapshot1.child("profession").getValue().toString();



                     aboutmestr=dataSnapshot1.child("aboutme").getValue().toString();

             String   githublinkstr=dataSnapshot1.child("githublink").getValue().toString(); namestr=dataSnapshot1.child("name").getValue().toString();
                    //for(DataSnapshot skills:dataSnapshot1.child("skills").getChildren()){
                        long n= dataSnapshot1.child("skills").getChildrenCount();
                        for(long i=0;i<n;i++){

                            skillsList.add(dataSnapshot1.child("skills").child(String.valueOf(i)).getValue().toString());
                        }
                   // }
                aboutme.setText(aboutmestr);
                    contact.setText(strcontact);
                    email.setText(stremail);
                    name.setText(namestr);
                    organisation.setText(strorganisation);
                    designation.setText(strprofession+" at "+strorganisation);
                githublink.setText(githublinkstr);
                    for(int i=0;i<skillsList.size();i++)
                    {
                            LayoutInflater layoutInflater =getLayoutInflater();
                            View view = layoutInflater.inflate(R.layout.chip_view,skills,false);
                            Chip chip = view.findViewById(R.id.dynamicChips);
                            chip.setCheckable(false);
                            chip.setChipText(skillsList.get(i));
                            skills.addView(chip);

                    }

                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

     }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = rootview.findViewById(R.id.profile_pic);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

        if (requestCode == RESULT_LOAD_COVER && resultCode == RESULT_OK && null != data) {
            Uri selectedcover = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(selectedcover,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = rootview.findViewById(R.id.header_cover_image);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }

    //List view expander
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
