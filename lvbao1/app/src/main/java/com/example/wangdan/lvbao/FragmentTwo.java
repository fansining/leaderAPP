package com.example.wangdan.lvbao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.page2.tour1;
import com.example.page2.tour2;
import com.example.page2.tour3;
import com.example.page2.tour4;
import com.example.page2.tour5;
import com.example.page2.tour6;


/**
 * Created by wangdan on 2016/7/28.
 */
public class FragmentTwo extends Fragment {
    View v;
    private ImageView lvyou1;
    private ImageView lvyou2;
    private ImageView lvyou3;
    private ImageView lvyou4;
    private ImageView lvyou5;
    private ImageView lvyou6;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_two, container, false);
        context = getActivity();
        lvyou1 = (ImageView)v.findViewById(R.id.lvyou1);
        lvyou2 = (ImageView)v.findViewById(R.id.lvyou2);
        lvyou3 = (ImageView)v.findViewById(R.id.lvyou3);
        lvyou4 = (ImageView)v.findViewById(R.id.lvyou4);
        lvyou5 = (ImageView)v.findViewById(R.id.lvyou5);
        lvyou6 = (ImageView)v.findViewById(R.id.lvyou6);
        lvyou1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), tour1.class);
                startActivity(intent);
            }
        });
        lvyou2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), tour2.class);
                startActivity(intent);
            }
        });

        lvyou3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), tour3.class);
                startActivity(intent);
            }
        });
        lvyou4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), tour4.class);
                startActivity(intent);
            }
        });

        lvyou5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), tour5.class);
                startActivity(intent);
            }
        });
        lvyou6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), tour6.class);
                startActivity(intent);
            }
        });

        return v;
    }


    }

