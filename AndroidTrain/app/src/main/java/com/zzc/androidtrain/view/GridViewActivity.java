package com.zzc.androidtrain.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zzc.androidtrain.R;

import java.util.ArrayList;
import java.util.List;

public class GridViewActivity extends AppCompatActivity {
    NoScrollGridView noScrollGridView;
    MyListView myListView;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, GridViewActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_grid_view);
        noScrollGridView = (NoScrollGridView) findViewById(R.id.gv_test);
        myListView = (MyListView) findViewById(R.id.lv_test);

        List<String> listData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            listData.add("Item " + i);
        }
        GridViewAdapter adapter = new GridViewAdapter(this);
        noScrollGridView.setAdapter(adapter);
//        myListView.setAdapter(adapter);
        adapter.setContent(listData);
    }
}
