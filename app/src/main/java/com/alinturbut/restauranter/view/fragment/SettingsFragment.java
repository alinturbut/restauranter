package com.alinturbut.restauranter.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.helper.ApiUrls;
import com.alinturbut.restauranter.service.SharedPreferencesService;

public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();

        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        final Switch localhostUseSwitch = (Switch) view.findViewById(R.id.settings_use_localhost);
        localhostUseSwitch.setChecked(SharedPreferencesService.getLocalhostUse(getActivity().getApplicationContext()));
        Button saveButton = (Button) view.findViewById(R.id.settings_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean useLocalhost = localhostUseSwitch.isChecked();
                ApiUrls.setUseLocalhost(useLocalhost);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
