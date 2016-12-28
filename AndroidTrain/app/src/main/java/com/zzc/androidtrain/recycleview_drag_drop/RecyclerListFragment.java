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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzc.androidtrain.recycleview_drag_drop.helper.OnStartDragListener;
import com.zzc.androidtrain.recycleview_drag_drop.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;


/**
 *
 */
public class RecyclerListFragment extends Fragment implements OnStartDragListener, RecyclerViewDragDropActivity.RecyclerOperateListener,
        RecyclerListAdapter.OnItemClickListener {

    private ItemTouchHelper mItemTouchHelper;
    RecyclerListAdapter adapter;
    RecyclerView recyclerView;

    public RecyclerListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RecyclerView(container.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new RecyclerListAdapter(getActivity(), this);
        adapter.setOnItemClickListener(this);
        recyclerView = (RecyclerView) view;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
//        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
//        defaultItemAnimator.setChangeDuration(1000 * 2);
//        defaultItemAnimator.setAddDuration(1000 * 2);
//        defaultItemAnimator.setRemoveDuration(1000 * 2);
//        defaultItemAnimator.setSupportsChangeAnimations(true);
//        SlideInRightAnimator defaultItemAnimator2 = new SlideInRightAnimator();
//        recyclerView.setItemAnimator(defaultItemAnimator2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onAdd() {
        adapter.onItemAdd("Zero", 0);
//        List<String> items = new ArrayList<>();
//        items.add("Zero");
//        items.add("Zero");
//        items.add("Zero");
//        items.add("Zero");
//        adapter.onItemAdd(items, 0);
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onDel() {
        adapter.onItemDel(0);
    }

    @Override
    public void onRefresh() {
        adapter.onDataRefresh();
    }

    @Override
    public void onAddFirstItemHeight() {
        View view = recyclerView.getChildAt(0);
        if(view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height += 40;
            view.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onItemClick(int position) {
//        adapter.onItemChange(position + "", position);
    }
}
