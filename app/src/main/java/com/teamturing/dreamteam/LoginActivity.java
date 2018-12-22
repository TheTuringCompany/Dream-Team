package com.teamturing.dreamteam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 234;
    private static final String TAG = "DreamTeam";

    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;
    AuthCredential credential;
    boolean validity=false;
    String emailInDatabase;
    ProgressDialog mProgressDialog;
  //  LoginButton fbSignIn;
  //  CallbackManager mCallbackManager;

    boolean checkEmail=false;
    GoogleSignInAccount account;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressDialog = new ProgressDialog(this);
        //fbSignIn =findViewById(R.id.sign_in_fb);
       // mCallbackManager = CallbackManager.Factory.create();
        //  LoginButton loginButton = findViewById(R.id.buttonFacebookLogin);

        firebaseAuth=FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });

        context = this;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                 account = task.getResult(ApiException.class);


                mProgressDialog.setMessage("Checking User....");
                mProgressDialog.show();
                credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        new CheckingUser().execute(account.getEmail());
                    }
                });




            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class CheckingUser extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(final String...emailChoosen ) {


            Log.e("Email Choosen",emailChoosen[0]);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference().child("Techie");

            if(databaseReference!=null)
            {
                Log.e("Database Reference","Reference IS NOT NULL");
            }else{
                Log.e("Database Reference","Reference IS  NULL");
            }
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e("Datasnap",dataSnapshot.getChildren().toString());
                   if( dataSnapshot.hasChild(firebaseAuth.getCurrentUser().getUid()))
                   {
                       signInDirectly();
                   }else {
                       goToRegistration();
                   }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
/////
                }

            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public void goToRegistration(){
        mProgressDialog.dismiss();
        Intent intent=new Intent(getApplicationContext(),RegistrationActivity.class);
        intent.putExtra("accId", account.getIdToken());
        intent.putExtra("loginType","google");
        startActivity(intent);

    }

    public void signInDirectly(){
        Log.e("Sign IN directly","CALLED");

        credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            Log.d("SIGNIN", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            mProgressDialog.dismiss();
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "signInWith Cretdential failure", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }









}
