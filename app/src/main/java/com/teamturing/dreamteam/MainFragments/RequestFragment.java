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
import com.teamturing.dreamteam.MainFragments.Adapters.RequestAdapter;
import com.teamturing.dreamteam.MainFragments.Adapters.RequestPojo;
import com.teamturing.dreamteam.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    public RecyclerView recyclerView;
    public List<RequestPojo> requestLists = new ArrayList<>();
    int image;
    ProgressDialog mProgressDialouge;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);
        recyclerView = view.findViewById(R.id.request_recyclerView);
        image = R.drawable.avtar_icon;

        mProgressDialouge = new ProgressDialog(view.getContext());
        mProgressDialouge.setMessage("Fetching data..");
        mProgressDialouge.show();
        getData();

       // RequestPojo request = new RequestPojo("Smart India Hackathon","hii there i want to participate",1234,image);
       // requestLists.add(request);
        //requestLists.add(request);



        return view;
    }
    public void getData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Techie");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                   // mProgressDialouge.dismiss();
                    Log.e("datasnapshot1",dataSnapshot1.toString());
                    DatabaseReference databaseReference1 = dataSnapshot1.child("requests").getRef();
                    requestLists.clear();
                    databaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren())
                            {
                               // Log.e("Everything",dataSnapshot2.getKey());

                             //   Log.e("description",dataSnapshot2.child("description").getValue().toString());
                                RequestPojo requestPojo = new RequestPojo(dataSnapshot2.child("name").getValue().toString(),
                                        dataSnapshot2.child("description").getValue().toString(),
                                        dataSnapshot2.getKey(),image);
                                requestLists.add(requestPojo);
                            }
                            if(requestLists.size()!=0)
                            {
                                Log.e("RequestList","Not 0");
                                mProgressDialouge.dismiss();
                                RequestAdapter requestAdapter = new RequestAdapter(requestLists,getContext());
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(requestAdapter);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
