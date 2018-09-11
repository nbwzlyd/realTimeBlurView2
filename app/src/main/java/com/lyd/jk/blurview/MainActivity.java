package com.lyd.jk.blurview;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Button mButton;

    private LinearLayoutManager manager;
    private ArrayList<Integer> mViewIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mRecyclerView = findViewById(R.id.RecyclerView);

       manager = new LinearLayoutManager(this);

        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.getOrientation()==LinearLayoutManager.VERTICAL) {
                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                }else {
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                }
                mRecyclerView.setLayoutManager(manager);
            }
        });


        mViewIds.add(R.drawable.s1);
        mViewIds.add(R.drawable.s2);
        mViewIds.add(R.drawable.s3);
        mViewIds.add(R.drawable.s4);
        mViewIds.add(R.drawable.s5);
        mViewIds.add(R.drawable.s6);
        mViewIds.add(R.drawable.s7);
        mViewIds.add(R.drawable.s8);
        mViewIds.add(R.drawable.s9);

        mRecyclerView.setAdapter(new RecyclerAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private class RecyclerAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return new SimpleViewHolder(imageView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ((SimpleViewHolder)holder).imageView.setImageResource(mViewIds.get(position));
        }

        @Override
        public int getItemCount() {
            return mViewIds==null?0:mViewIds.size();
        }
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView;
        }
    }
}
