package com.zzc.androidtrain.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zzc.androidtrain.R;
import com.zzc.androidtrain.app.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomFragment extends BaseFragment {
    BaseFragment currentFragment;

    public BottomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnChange = (Button) view.findViewById(R.id.btn_change_center_fragment);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebFragment webFragment = WebFragment.newInstance("http://www.baidu.com");
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

                if (currentFragment != null) {
                    fragmentTransaction.hide(currentFragment);
                }
                fragmentTransaction.add(R.id.fl_container, webFragment);
                currentFragment = webFragment;

                fragmentTransaction.commitAllowingStateLoss();
            }
        });
    }

}
