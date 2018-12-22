package com.teamturing.dreamteam;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

        RoundedImageView roundedImageView ;
        private static final int RESULT_LOAD_IMAGE = 100;
        Button submitButton;
        String email, namestr,githublinkstr,aboutmestr;
        EditText name,contact,org,githublink,aboutme;
        Uri selectImage;
        List<String> choices;
        ChipGroup interests;
        String [] skills = {
                "Java",
                "Python",
                "C++",
                "JavaScript",
                "Angular",
                "R",
                "ML",
                "IoT",
                "NodeJS",
                "SQL",
                "MongoDB",
                "Android",
                "Kotlin",
                "Flutter",
                "Full stack",
                "HTML",
                "CSS"

        };
        String[] proffesion = {"Select your profession","Student","Professional","Freelancer"};
        Spinner proffessionSpin;
    StorageReference storageReference;
        FirebaseAuth mAuth;
        ProgressDialog mProgressDialog;
        AuthCredential authCredential;
        String accIdToken;
        String loginType;
        DatabaseReference databaseReference;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_register);
            roundedImageView = findViewById(R.id.profileImage);
            submitButton = findViewById(R.id.submitBtn);

            //  frameworks = findViewById(R.id.frameworks);
            interests = findViewById(R.id.interests);
            name = findViewById(R.id.enterName);
            proffessionSpin = findViewById(R.id.proffession);
            aboutme=findViewById(R.id.aboutme);
            mProgressDialog =  new ProgressDialog(this);
            contact = findViewById(R.id.enterContact);
            org = findViewById(R.id.enterOrg);
            githublink=findViewById(R.id.enterGithub);

            choices = new ArrayList<>();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>( getApplicationContext(),
                    android.R.layout.simple_list_item_1,proffesion);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            proffessionSpin.setAdapter(adapter);
            mAuth = FirebaseAuth.getInstance();





            roundedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isReadStoragePermissionGranted())
                    {
                        uploadImage();
                    }


                }
            });

            Intent intent = getIntent();


            loginType = intent.getExtras().getString("loginType");
         //   Log.e("accIDToken",accIdToken);
            Log.e("LoginType",loginType);


                databaseReference=FirebaseDatabase.getInstance().getReference().child("Techie").child(mAuth.getCurrentUser().getUid());
                storageReference= FirebaseStorage.getInstance().getReference().child("ProfileImages/").child(mAuth.getCurrentUser().getUid());

                getData();

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(TextUtils.isEmpty(name.getText()))
                    {
                        name.setError("Enter your name");
                        name.requestFocus();
                        return;
                    }
                    if(TextUtils.isEmpty(contact.getText()))
                    {
                        contact.setError("Enter your contact");
                        contact.requestFocus();
                        return;
                    }
                    if(TextUtils.isEmpty(githublink.getText()))
                    {
                        githublink.setError("Enter your github link");
                        githublink.requestFocus();
                        return;
                    }
                    if(TextUtils.isEmpty(aboutme.getText()))
                    {
                        aboutme.setError("Enter about you");
                        aboutme.requestFocus();
                        return;
                    }
                    if(TextUtils.isEmpty(org.getText()))
                    {
                        org.setError("Enter your organisation");
                        org.requestFocus();
                        return;
                    }
                    if(proffessionSpin.getSelectedItem().toString().equals("Select your profession")){
                        Toast.makeText(getApplicationContext(),"Please select a profession",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(selectImage==null)
                    {
                        Toast.makeText(getApplicationContext(), "Please Select a profile image", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for(int i = 0;i<interests.getChildCount();i++)

                    {
                        Chip chip = (Chip)interests.getChildAt(i);
                        if(chip.isChecked())
                        {
                            Log.e("Chips are",":"+chip.getChipText());
                            choices.add(chip.getChipText().toString());
                        }
                    }

                    updateDatabases();



                }
            });


            //For dynamically inflating chips
            for(int i=0;i<skills.length;i++)
            {
                LayoutInflater layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.chip_view,interests,false);

                Chip chip = view.findViewById(R.id.dynamicChips);
                chip.setChipText(skills[i]);


                interests.addView(chip);

            }


        }

//    public void checkUser(){
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Techie");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
//                {
//                   Log.e("USER is",dataSnapshot1.child("name").getValue().toString());
//                   Log.e("Token is",dataSnapshot1.child("idToken").getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
    public void registerUser(){
        mProgressDialog.setMessage("Registering user...");
        mProgressDialog.show();

//            authCredential = GoogleAuthProvider.getCredential(accIdToken,null);

            mAuth.signInWithCredential(authCredential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                updateDatabases();
                            } else {
                                Log.w("Error:", "signInWithCredential:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "signInWith Cretdential failure", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });


                        }












    public void updateDatabases(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        email = currentUser.getEmail();
        namestr = name.getText().toString();
        githublinkstr = githublink.getText().toString();
        String contactStr = contact.getText().toString();
        String orgStr = org.getText().toString();
        aboutmestr=aboutme.getText().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().
                child("Techie").
                child(currentUser.getUid());
        databaseReference.child("name").setValue(namestr);
        databaseReference.child("githublink").setValue(githublinkstr);
        databaseReference.child("aboutme").setValue(aboutmestr);
        databaseReference.child("email").setValue(email);
        databaseReference.child("contact").setValue(contactStr);


        databaseReference.child("organisation").setValue(orgStr);
        //databaseReference.child("idToken").setValue(accIdToken);
        databaseReference.child("profession").setValue(proffessionSpin.getSelectedItem().toString());
        for(int i=0;i<choices.size();i++)
        {
            databaseReference.child("skills").child(String.valueOf(i)).setValue(choices.get(i));

        }
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("ProfileImages/"+currentUser.getUid());
        ref.putFile(selectImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                mProgressDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
        });

    }
    public void uploadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,RESULT_LOAD_IMAGE);
    }
    public boolean isReadStoragePermissionGranted(){
        if(Build.VERSION.SDK_INT>=23)
        {
            if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                return true;
            }else{
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}
                        ,3);
                return  false;
            }

        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==3&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            uploadImage();
        }
    }






    public void   getData(){
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri.toString()).into(roundedImageView);
                selectImage=uri;
                Log.e("imageuriString",uri.toString());
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                String stremail,strorganisation,strprofession,namestr;
                ArrayList<String> skillsList=new ArrayList<String>();
                // DataSnapshot dataSnapshot1
                String aboutmestr=dataSnapshot1.child("aboutme").getValue().toString();
              String strcontact=dataSnapshot1.child("contact").getValue().toString();
                stremail=dataSnapshot1.child("email").getValue().toString();
                strorganisation=dataSnapshot1.child("organisation").getValue().toString();
                strprofession=dataSnapshot1.child("profession").getValue().toString();
                namestr=dataSnapshot1.child("name").getValue().toString();
                String githubstr=dataSnapshot1.child("githublink").getValue().toString();
                //for(DataSnapshot skills:dataSnapshot1.child("skills").getChildren()){
                aboutme.setText(aboutmestr);
                contact.setText(strcontact);
                name.setText(namestr);
                org.setText(strorganisation);
               // proffesion.setText(strprofession);
                githublink.setText(githubstr);

             int i=   Arrays.asList(proffesion).indexOf(strprofession);

                proffessionSpin.setSelection(i);

                //registerUser();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = findViewById(R.id.profile_pic);
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
