package com.bwie.chitchat.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bwie.chitchat.R;
import com.bwie.chitchat.activitys.LoginActivity;
import com.bwie.chitchat.base.IApplication;
import com.bwie.chitchat.network.cookie.CookiesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class FourthFragment extends Fragment {


    @BindView(R.id.btn_fourth_tui)
    Button btnFourthTui;
    Unbinder unbinder;

    public FourthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;


    }



    @OnClick(R.id.btn_fourth_tui)
    public void onViewClicked() {
        new CookiesManager(IApplication.application).removeAllCookie();

        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
