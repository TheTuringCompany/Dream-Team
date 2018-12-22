package com.teamturing.dreamteam.MainFragments.Adapters;
 
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teamturing.dreamteam.R;

import java.util.List;
 
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<HomeEvent> eventList;
    private Context mContext;
    public Dialog myDialog;
    TextView dialog_URL;
    FirebaseAuth mAuth;
    public Button dialog_joinAteam,dialog_findAteam;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog mProgressDialog;

    public EventAdapter(List<HomeEvent> eventList, Context mContext) {
        this.eventList = eventList;
        this.mContext = mContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id,title, description, startDate, lastdate;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.event_id);
            title = (TextView) view.findViewById(R.id.recycler_title);
            description = (TextView) view.findViewById(R.id.recycler_Description);
            startDate = view.findViewById(R.id.recycler_startdate);
            lastdate = view.findViewById(R.id.recycler_lastdate);
            imageView = view.findViewById(R.id.recycler_imageView);
            firebaseDatabase =FirebaseDatabase.getInstance();
            mAuth = FirebaseAuth.getInstance();
            mProgressDialog = new ProgressDialog(mContext);

        }
    }


    public EventAdapter(List<HomeEvent> eventList) {
        this.eventList = eventList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_single_item, parent, false);

        final MyViewHolder vHolder = new MyViewHolder(itemView);

        myDialog = new Dialog(mContext);
        myDialog.setContentView(R.layout.home_dialoge);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView dialog_eventImage = myDialog.findViewById(R.id.dialog_imageview_id);
       dialog_URL = myDialog.findViewById(R.id.dialog_eventLink);
       dialog_joinAteam = myDialog.findViewById(R.id.dialog_joinBtn_id);
       dialog_findAteam = myDialog.findViewById(R.id.dialog_findTeam);

        //HomeEvent homeEvent = eventList.get(pos)
      //  URL.setText("Hello");

        //put data in the dialog from firebase (use event ID as key)



        vHolder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d("TAG","Event ID is : "+String.valueOf(vHolder.id.getText()));
                //Here we got our event ID , use this to fetch data from firebase .
                    HomeEvent homeEvent = eventList.get(Integer.parseInt(vHolder.id.getText().toString()));
                    dialog_URL.setText(homeEvent.getLink());
                    myDialog.show();
                    dialog_findAteam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(view.getContext(),"find a team",Toast.LENGTH_SHORT).show();
                            HomeEvent homeEvent1 = eventList.get(Integer.parseInt(vHolder.id.getText().toString()));
                            mProgressDialog.setMessage("Making a request....");
                            mProgressDialog.show();
                            requestForAteamMember(homeEvent1.getKey(),homeEvent1.getTitle());
                        }
                    });
                    dialog_joinAteam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(view.getContext(),"find a team member ", Toast.LENGTH_SHORT).show();
                            HomeEvent homeEvent1 = eventList.get(Integer.parseInt(vHolder.id.getText().toString()));
                            requestForAteam(homeEvent1.getKey(),homeEvent1.getTitle());
                           // requestForAteam();
                        }
                    });

            }
        });


        return new MyViewHolder(itemView);
    }
    public void requestForAteamMember(final String key,final String title){
        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("Techie").child(mAuth.getUid());
        databaseReference.child("requests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(key)){
                    Toast.makeText(mContext,"You have already made a request for this event",
                            Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("requests").child(key).child("name").setValue(title);
                    databaseReference.child("requests").child(key).child("requestType").setValue("member");
                    databaseReference.child("requests").child(key).child("from").setValue(mAuth.getUid());
                    databaseReference.child("requests").child(key).child("description").setValue("Need a teammate for"+" "+title);
                    Toast.makeText(mContext,"Request added!!",Toast.LENGTH_SHORT).show();


                }
                mProgressDialog.dismiss();
                myDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void requestForAteam(final String key,final String title){
        final DatabaseReference databaseReference = firebaseDatabase.getReference().child("Techie").child(mAuth.getUid());
        databaseReference.child("requests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(key)){
                    Toast.makeText(mContext,"You have already made a request for this event",
                            Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("requests").child(key).child("name").setValue(title);
                    databaseReference.child("requests").child(key).child("requestType").setValue("team");
                    databaseReference.child("requests").child(key).child("from").setValue(mAuth.getUid());
                    databaseReference.child("requests").child(key).child("description").setValue("Need a team for"+" "+title);
                    Toast.makeText(mContext,"Request added!!",Toast.LENGTH_SHORT).show();


                }
                mProgressDialog.dismiss();
                myDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


       // DatabaseReference databaseReference = firebaseDatabase.getReference().child("Techie").child(mAuth.getUid());
       // DatabaseReference databaseReference = firebaseDatabase.getReference().child("Techie").child(mAuth.getUid());

      //  myDialog.dismiss();
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HomeEvent homeEvent = eventList.get(position);

        holder.id.setText(homeEvent.getId());
        holder.title.setText(homeEvent.getTitle());
        holder.description.setText(homeEvent.getDescription());
        holder.startDate.setText(homeEvent.getStartDate());
        holder.lastdate.setText(homeEvent.getLastDate());
        holder.imageView.setImageResource(homeEvent.getImage());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }


}