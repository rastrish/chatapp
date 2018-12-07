package com.example.rishabh.chatapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecylerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mcontext;
    private List<User> mUsers;

    public RecylerviewAdapter(Context mcontext ,List<User> mUsers)
    {
        this.mUsers = mUsers;
        this.mcontext = mcontext;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.listlayout,viewGroup,false);
        return new ViewholderAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i)
    {
        final User user = mUsers.get(i);
        ViewholderAdapter.username.setText(user.getName());
        Glide.with(mcontext).load(user.getImage()).into(ViewholderAdapter.imageurl);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,Message.class);
                intent.putExtra("userid",user.getUserId());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class ViewholderAdapter extends RecyclerView.ViewHolder
    {
        static public TextView username;
        static public ImageView imageurl;

        public ViewholderAdapter(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            imageurl = itemView.findViewById(R.id.userimage);
        }
    }
}
