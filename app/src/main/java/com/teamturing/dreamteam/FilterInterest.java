package com.teamturing.dreamteam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class FilterInterest extends AppCompatActivity {

   ChipGroup interests;

    Button register,chipTest;
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
    List<String> choices;
    String name, password,contact,email,image;
    Uri selectedImage;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_interest);
        interests = findViewById(R.id.interests);
       // register = findViewById(R.id.registerBtn);
       register = findViewById(R.id.registerBtn);
        choices  = new ArrayList<>();
       Bundle bundle = getIntent().getExtras();
       Intent intent = getIntent();

       name = intent.getExtras().getString("name");
       password = intent.getExtras().getString("password");
       email = intent.getExtras().getString("email");
       contact = intent.getExtras().getString("contact");
        image =intent.getExtras().getString("selectedImage");

        selectedImage = Uri.parse(image);
       auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = 0;i<interests.getChildCount();i++)

                {
                    Chip chip = (Chip)interests.getChildAt(i);
                    if(chip.isChecked())
                    {
                        Log.e("Chips are",":"+chip.getChipText());
                        choices.add(chip.getChipText().toString());
                    }
                }
                register();
            }
        });

//            Chip chip = new Chip(getApplicationContext());
//            chip.setChipText("Hello");
//            interests.addView(chip);
        for(int i=0;i<skills.length;i++)
        {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.chip_view,interests,false);

            Chip chip = view.findViewById(R.id.dynamicChips);
           chip.setChipText(skills[i]);

           interests.addView(chip);

        }


    }
    public void register(){
        progressDialog.setMessage("Registering User..");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if(currentUser!=null)
                {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Techie").child(currentUser.getUid());
                    databaseReference.child("name").setValue(name);
                    databaseReference.child("email").setValue(email);
                    databaseReference.child("contact").setValue(contact);
                    for(int i=0;i<choices.size();i++)
                    {
                        databaseReference.child("skills").child(String.valueOf(i)).setValue(choices.get(i));

                    }
                    StorageReference ref = FirebaseStorage.getInstance().getReference().child("ProfileImages/"+currentUser.getUid());
                    ref.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                        }
                    });

                }
            }
        });

    }
}
