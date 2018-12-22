package com.teamturing.dreamteam.MainFragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamturing.dreamteam.MainFragments.Adapters.EventAdapter;
import com.teamturing.dreamteam.MainFragments.Adapters.HomeEvent;
import com.teamturing.dreamteam.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public RecyclerView recyclerView;
    public List<HomeEvent> eventLists = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    int image;
    ProgressDialog mProgressDialouge;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_home, null, false);
        recyclerView = view.findViewById(R.id.recycler_home);
        firebaseDatabase = FirebaseDatabase.getInstance();
        image = R.drawable.sih_logo;
        mProgressDialouge = new ProgressDialog(view.getContext());
        mProgressDialouge.setMessage("Fetching data..");
        mProgressDialouge.show();
        getData();
        String title = "Smart India Hackathon";
        String description = "Smart India Hackathon 2019 is a nationwide initiative to provide students a platform to solve some of pressing"
        +"problems we face in our daily lives, and thus inculcate a culture of product innovation and a mindset"
        +"of problem solving.";
        String startDate = "25th Dec 2018";
        String lastDate = "13th Jan 2019";




      //  HomeEvent homeEvent =  new HomeEvent("1111",title,description,startDate,lastDate,image);
       // HomeEvent homeEvent2 =  new HomeEvent("2222","IEEMadc",description,startDate,lastDate,image);

        if(eventLists.size()!=0)
        {

        }



        return  view;

    }
    public void getData(){
        Log.e("CALLING","GET DATA()");
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Hackathon");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long n = dataSnapshot.getChildrenCount();
                Log.e("Children Count",String.valueOf(n));


                    String id="",key="",
                            startDate="",
                            name="",
                            deadline=""
                            ,description="",
                            link = "";
                    Log.e("FOR LOOP","1st for loop");

                    eventLists.clear();
                    int i =0;
                    Log.e("Data-Snapshot Children",dataSnapshot.getChildren().toString());
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        Log.e("Getting here",dataSnapshot1.toString());
                         id = String.valueOf(i);
                         key = dataSnapshot1.getKey();
                        name= dataSnapshot1.child("name").getValue().toString();
                        startDate = dataSnapshot1.child("start_date").getValue().toString();
                        deadline = dataSnapshot1.child("deadline").getValue().toString();
                        description = dataSnapshot1.child("description").getValue().toString();
                        link =dataSnapshot1.child("link").getValue().toString();

                        HomeEvent homeEvent =  new HomeEvent(id,name,description,startDate,deadline,image,link,key);

                        eventLists.add(homeEvent);
                        i++;
                    }


                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                if(eventLists.size()!=0)
                {
                    EventAdapter eventAdapter = new EventAdapter(eventLists,getContext());
                    recyclerView.setAdapter(eventAdapter);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    mProgressDialouge.dismiss();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
