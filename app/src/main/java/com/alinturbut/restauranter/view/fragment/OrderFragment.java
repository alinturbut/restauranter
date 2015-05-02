package com.alinturbut.restauranter.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.model.Category;
import com.alinturbut.restauranter.service.MenuService;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    private RecyclerView recList;
    private OnFragmentInteractionListener mListener;

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    public OrderFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), MenuService.class);
        getActivity().startService(intent);
        initView();
    }

    private void initView() {
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_restaurant_menu, container, false);
        recList = (RecyclerView) inflatedView.findViewById(R.id.categoryList);

        return inflatedView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private BroadcastReceiver categoriesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                ArrayList<Category> allMenuCategories = (ArrayList<Category>) bundle.get(MenuService.ALL_CATEGORIES);
                Log.d("MenuFragment", allMenuCategories.toString());
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(categoriesReceiver, new IntentFilter(MenuService.INTENT_CATEGORY_PUBLISH));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(categoriesReceiver);
    }
}
