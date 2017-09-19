package com.androidchatapp;

/**
 * Created by soorya on 9/15/2017.
 */


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{

    int index=0;


    public static ArrayList<String> message_list;
    public static ArrayList<Integer> type_list;
    LinearLayout layout;

    public RecyclerAdapter()
    {
        message_list = new ArrayList<>();
        type_list = new ArrayList<>();
    }
    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView message_content;

        public ViewHolder(View itemView)
        {
            super(itemView);
            message_content =(TextView)itemView.findViewById(R.id.message_content);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
        viewHolder.message_content.setText(message_list.get(index));

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;
        if(type_list.get(index) == 1)
        {
            lp2.gravity = Gravity.LEFT;
            viewHolder.message_content.setBackgroundResource(R.drawable.bubble_in);
        }
        else
        {
            lp2.gravity = Gravity.RIGHT;
            viewHolder.message_content.setBackgroundResource(R.drawable.bubble_out);
        }
        viewHolder.message_content.setLayoutParams(lp2);
        index++;
    }

    @Override
    public int getItemCount()
    {

        return type_list.size();

    }

}