package com.androidchatapp;

/**
 * Created by soorya on 9/15/2017.
 */



import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{




    public static ArrayList<String> message_list;
    public static ArrayList<Integer> type_list;



    public RecyclerAdapter()
    {
        message_list = new ArrayList<>();
        type_list = new ArrayList<>();
    }
    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView message_content_left;
        //public TextView message_content_right;

        public ViewHolder(View itemView)
        {
            super(itemView);
            message_content_left =(TextView)itemView.findViewById(R.id.message_content_left);
            //message_content_right =(TextView)itemView.findViewById(R.id.message_content_right);


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


        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(type_list.get(i) == 1)
        {

            lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            viewHolder.message_content_left.setText(message_list.get(i));
            viewHolder.message_content_left.setBackgroundResource(R.drawable.bubble_in);
        }
        else
        {
            lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            viewHolder.message_content_left.setText(message_list.get(i));

            viewHolder.message_content_left.setBackgroundResource(R.drawable.bubble_out);
        }
       viewHolder.message_content_left.setLayoutParams(lp2);
    }

    @Override
    public int getItemCount()
    {

        return type_list.size();

    }

}