package com.alinturbut.restauranter.view.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.model.Table;
import com.alinturbut.restauranter.service.TableService;
import com.alinturbut.restauranter.view.adapter.TableListAdapter;

import java.util.ArrayList;

public class TableOverviewFragment extends Fragment {
    private RecyclerView mRecyclerView;
    public static final String CONTEXT_FOR_ORDER = "order.context";
    public static final String CONTEXT_FOR_SHOW = "show.context";
    public static final String CONTEXT_PARAM = "context.param";
    public static final String START_TABLE_FRAGMENT = "restauranter.start.table.fragment";
    private String mContext;

    public static TableOverviewFragment newInstance(String context) {
        TableOverviewFragment fragment = new TableOverviewFragment();
        Bundle args = new Bundle();
        args.putString(CONTEXT_PARAM, context);
        fragment.setArguments(args);

        return fragment;
    }

    public TableOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), TableService.class);
        intent.putExtra("Action", TableService.INTENT_TABLE_GET_ALL);
        intent.putExtra("Context", mContext);
        getActivity().startService(intent);
        initView();
    }

    private void initView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.mContext = getArguments().getString(CONTEXT_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table_overview, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tableList);

        return view;
    }

    private BroadcastReceiver tablesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                ArrayList<Table> allTables =
                        (ArrayList<Table>) bundle.get(TableService.ALL_TABLES);
                final String overviewContext = bundle.getString("Context");
                Log.d("TableOverviewFragment", allTables.toString());

                TableListAdapter listAdapter = null;
                if(CONTEXT_FOR_ORDER.equals(overviewContext)) {
                    listAdapter = new TableListAdapter(allTables, getActivity().getResources(), true, getActivity()
                            .getApplicationContext(), getInstance());
                } else {
                    listAdapter = new TableListAdapter(allTables, getActivity().getResources(), false, getActivity()
                            .getApplicationContext(), getInstance());
                }
                mRecyclerView.setAdapter(listAdapter);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(tablesReceiver, new IntentFilter(TableService.INTENT_TABLES_PUBLISH));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(tablesReceiver);
    }

    public void goBack() {
        getActivity().onBackPressed();
    }

    private TableOverviewFragment getInstance() {
        return this;
    }

}
