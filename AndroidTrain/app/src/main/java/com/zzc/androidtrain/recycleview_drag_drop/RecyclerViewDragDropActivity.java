/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zzc.androidtrain.recycleview_drag_drop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseActivity;
import com.zzc.androidtrain.util.Toaster;

/**
 * @author Paul Burke (ipaulpro)
 */
public class RecyclerViewDragDropActivity extends BaseActivity implements MainFragment.OnListItemClickListener {

    Fragment fragment = null;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RecyclerViewDragDropActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycleview_drag_drop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("RecyclerView");
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            MainFragment fragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, fragment)
                    .commit();
        }
    }

    @Override
    public void onListItemClick(int position) {
        switch (position) {
            case 0:
                fragment = new RecyclerListFragment();
                break;

            case 1:
                fragment = new RecyclerGridFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recyclerview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(fragment == null || !(fragment instanceof RecyclerOperateListener)) {
            return false;
        }
        switch (item.getItemId()) {
            case R.id.action_add:
                Toaster.showShortToast(getBaseContext(),  "添加");
                ((RecyclerOperateListener)fragment).onAdd();
                break;
            case R.id.action_del:
                Toaster.showShortToast(getBaseContext(),  "删除");
                ((RecyclerOperateListener)fragment).onDel();
                break;
            case R.id.action_refresh:
                Toaster.showShortToast(getBaseContext(),  "刷新");
                ((RecyclerOperateListener)fragment).onRefresh();
                break;
            case R.id.action_add_height:
                ((RecyclerOperateListener)fragment).onAddFirstItemHeight();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    interface RecyclerOperateListener {
        void onAdd();
        void onDel();
        void onRefresh();
        void onAddFirstItemHeight();
    }
}
