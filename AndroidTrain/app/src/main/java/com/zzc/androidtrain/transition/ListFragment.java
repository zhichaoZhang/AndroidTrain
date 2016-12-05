package com.zzc.androidtrain.transition;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseFragment;

/**
 * Created by zczhang on 16/11/17.
 */

public class ListFragment extends BaseFragment implements ListAdapter.ItemClickListener {
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transition_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        ListAdapter adapter = new ListAdapter(getContext());
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickItem(View view, int position) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailActivity.class);
            ActivityOptions options =
                    null;

            options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                    Pair.create(view, getString(R.string.transition_image)),
                    Pair.create(view, getString(R.string
                            .transition_root)));
            startActivity(intent, options.toBundle());
        }
    }
}
