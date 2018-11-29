package com.androidchatapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WelcomeScreen extends AppCompatActivity
{
    ViewPager pager;
    public int LAYOUTS[];
    private Button skip, next;
    IntroViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        pager=(ViewPager) findViewById(R.id.view_pager);
        next=(Button)findViewById(R.id.next);
        skip=(Button)findViewById(R.id.skip);
        LAYOUTS=new int[]
                {
                R.layout.slide_1,R.layout.slide_2,R.layout.slide_3,R.layout.slide_4
        };
        adapter=new IntroViewPagerAdapter();
        pager.setAdapter(adapter);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeScreen.this,Register.class));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current=pager.getCurrentItem()+1;
                if(current<LAYOUTS.length)
                {
                    pager.setCurrentItem(current);
                }
                else
                {
                    startActivity(new Intent(WelcomeScreen.this,Register.class));
                }
            }
        });
    }

    private int getItem(int i) {
        return pager.getCurrentItem() + i;
    }

    class IntroViewPagerAdapter extends PagerAdapter
    {
        private LayoutInflater layoutInflater;
        AppCompatActivity activity;

        @Override
        public int getCount()
        {
            return LAYOUTS.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }


        public Object instantiateItem(ViewGroup container, int pos)
        {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(LAYOUTS[pos], container, false);
            container.addView(view);
            return view;
        }

        public void destroyItem(ViewGroup container,int position, Object object)
        {
            View view=(View) object;
            container.removeView(view);
        }
    }
}
