package com.alinturbut.restauranter.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.helper.ApiUrls;
import com.alinturbut.restauranter.service.SharedPreferencesService;
import com.alinturbut.restauranter.view.activity.LoginActivity;

public class SettingsFragment extends Fragment {
    protected Context applicationContext;

    public static SettingsFragment newInstance(Context ctx) {
        SettingsFragment fragment = new SettingsFragment();
        fragment.applicationContext = ctx;

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
                SharedPreferencesService.saveLocalhostUse(getActivity().getApplicationContext(), useLocalhost);
                Toast.makeText(getActivity().getApplicationContext(), "Settings saved!", Toast.LENGTH_SHORT).show();
            }
        });
        Button signOutButton = (Button) view.findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesService.removeLoggedWaiterSession(applicationContext);
                Toast.makeText(applicationContext, "You signed out!", Toast.LENGTH_SHORT).show();
                onSignOut();
            }
        });

        return view;
    }

    private void onSignOut() {
        Intent intent = new Intent(applicationContext, LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
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
