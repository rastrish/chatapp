package com.example.rishabh.chatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class messageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

   public static final int  MSG_TYP_LFT = 0;
   public static final int MSG_TYP_RYT = 1;

    private Context mcontext;
    private List<chat> mchat;
    private String imageurl;
    FirebaseUser firebaseUser;



    public messageAdapter(Context mcontext ,List<chat> mchat, String imageurl)
    {
        this.mchat = mchat;
        this.mcontext = mcontext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {
        if (viewtype == MSG_TYP_RYT) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_right, viewGroup, false);
            return new messageAdapter.ViewholderAdapter(view);
        } else
            {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_left, viewGroup, false);
            return new messageAdapter.ViewholderAdapter(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i)
    {
        chat chat = mchat.get(i);

        ViewholderAdapter.messageshow.setText(chat.getMessages());
        Glide.with(mcontext).load(imageurl).into(ViewholderAdapter.profileimage);

    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public static class ViewholderAdapter extends RecyclerView.ViewHolder
    {
        static public TextView messageshow;
        static public ImageView profileimage;



        public ViewholderAdapter(@NonNull View itemView) {
            super(itemView);

            messageshow = itemView.findViewById(R.id.messageshow);
            profileimage = itemView.findViewById(R.id.circleimagechat);
        }
        }

    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        if (mchat.get(position).getSender().equals(firebaseUser.getUid()))
        {
        return MSG_TYP_RYT;
    }
        else
            return MSG_TYP_LFT;
        }
}


