package com.teamturing.dreamteam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.teamturing.dreamteam.MainFragments.HomeFragment;
import com.teamturing.dreamteam.MainFragments.NotificationsFragment;
import com.teamturing.dreamteam.MainFragments.ProfileFragment;
import com.teamturing.dreamteam.MainFragments.RequestFragment;

public class MainActivity extends AppCompatActivity {

    //Firebase integrated
    FirebaseAuth firebaseAuth;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            HomeFragment homeFragment = new HomeFragment();
            NotificationsFragment notificationsFragment = new NotificationsFragment();
            ProfileFragment profileFragment = new ProfileFragment();
            RequestFragment requestFragment = new RequestFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FrameLayout fl = (FrameLayout) findViewById(R.id.fragment);

            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fl.removeAllViews();
                    transaction.replace(R.id.fragment, homeFragment);
                    transaction.commit();
                    return true;

                case R.id.navigation_notifications:

                    fl.removeAllViews();
                    transaction.hide(homeFragment);
                    transaction.replace(R.id.fragment, notificationsFragment);
                    transaction.commit();
                    return true;

                case R.id.navigation_profile:
                    fl.removeAllViews();
                    transaction.replace(R.id.fragment, profileFragment);
                    transaction.commit();
                    return true;

                case R.id.navigation_request:
                    fl.removeAllViews();
                    transaction.replace(R.id.fragment, requestFragment);
                    transaction.commit();
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth= FirebaseAuth.getInstance();

      if(firebaseAuth.getCurrentUser()==null)
      {
          Intent intent = new Intent(MainActivity.this,LoginActivity.class);
          startActivity(intent);
          finish();
      }


        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, homeFragment);
        transaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }

}