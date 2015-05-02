package com.alinturbut.restauranter.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alinturbut.restauranter.helper.dummy.DummyContent;
import com.alinturbut.restauranter.helper.contracts.RestaurantFactsContract;
import com.alinturbut.restauranter.model.RestaurantFacts;
import com.alinturbut.restauranter.service.RestaurantFactsService;

public class RestaurantFactListFragment extends ListFragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;

    public interface Callbacks {
        public void onItemSelected(String id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    public RestaurantFactListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callServiceForFacts();
    }

    public void setListAdapter() {
        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                DummyContent.ITEMS));
    }

    private void populateDummyContent(RestaurantFacts restaurantFacts) {
        DummyContent.addItem(new DummyContent.DummyItem(RestaurantFactsContract.RestaurantFactsEntry.COLUMN_NAME,
                restaurantFacts.getName()));
        DummyContent.addItem(new DummyContent.DummyItem(RestaurantFactsContract.RestaurantFactsEntry.COLUMN_STREET,
                restaurantFacts.getStreet()));
        DummyContent.addItem(new DummyContent.DummyItem(RestaurantFactsContract.RestaurantFactsEntry.COLUMN_CITY,
                restaurantFacts.getCity()));
        setListAdapter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private void callServiceForFacts() {
        Intent intent = new Intent(getActivity(), RestaurantFactsService.class);
        intent.putExtra("Action", RestaurantFactsService.INTENT_READ_FACTS);
        getActivity().startService(intent);
    }

    private BroadcastReceiver factsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RestaurantFacts restaurantFacts = (RestaurantFacts) intent.getExtras().get("facts");
            if(restaurantFacts != null) {
                populateDummyContent(restaurantFacts);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(factsReceiver, new IntentFilter(RestaurantFactsService.INTENT_FACTS_SERVICE));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(factsReceiver);
    }
}
