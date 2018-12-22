package com.teamturing.dreamteam.MainFragments.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.teamturing.dreamteam.R;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private List<RequestPojo> request_list;
    private Context mContext;

    public RequestAdapter(List<RequestPojo> request_list, Context mContext) {
        this.request_list = request_list;
        this.mContext = mContext;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name ,description,requesId;
        public ImageView profilePic;
        public Button viewProfile , respond;

        public MyViewHolder(View view) {
            super(view);
          name =  view.findViewById(R.id.request_title_textView);
          description =  view.findViewById(R.id.request_description_textView);
          requesId = view.findViewById(R.id.request_id);
          profilePic = view.findViewById(R.id.request_profilePic_imageView);
          viewProfile = view.findViewById(R.id.request_viewProfile_button);
          respond = view.findViewById(R.id.request_respond_button3);

        }
    }



    @Override
    public RequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_request_item, parent, false);

        final RequestAdapter.MyViewHolder vHolder = new RequestAdapter.MyViewHolder(itemView);


        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RequestPojo requestPojo = request_list.get(position);

        holder.name.setText(requestPojo.getName());
        holder.description.setText(requestPojo.getDescription());
        holder.requesId.setText(requestPojo.getRequesId());
        holder.profilePic.setImageResource(requestPojo.profilePic);

    }

    @Override
    public int getItemCount() {
        return request_list.size();
    }

}
