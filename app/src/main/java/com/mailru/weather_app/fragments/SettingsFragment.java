package com.mailru.weather_app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.mailru.weather_app.MainActivity;
import com.mailru.weather_app.R;
import com.mailru.weather_app.RecyclerAdapter;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    private Context context;
    private RecyclerView settings_list;
    private ArrayList<String> data = new ArrayList<>();


    private GoogleSignInClient googleSignInClient;
    private int RC_SIGN_IN = 100;
    private String serverClientId = "278873659235-4gkuavs8ctlu8ovfae996k88kv0d3ksi.apps.googleusercontent.com";
    private SignInButton signInButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();
        initViews(view);
        initData();
        initList();
        initSignInParameters();
        setOnSignInBtnClickListener();
    }

    private void setOnSignInBtnClickListener() {
        signInButton.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(MainActivity.class.getSimpleName(), "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null || account.isExpired() || account.getIdToken() == null) {
            //logout. Показываем страницу входа. Без возможности вернуться назад.
        } else {
            Toast.makeText(context, "You have signed in GoogleAccount", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        updateUI(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void initSignInParameters() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(serverClientId)
                .requestServerAuthCode(serverClientId, false)
                .build();

        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    private void initViews(View view) {
        settings_list = view.findViewById(R.id.settings_list_view);
        signInButton = view.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
    }

    private void initData() {
        data.add(getResources().getString(R.string.unit));
        data.add(getResources().getString(R.string.auto_refresh));
        data.add(getResources().getString(R.string.use_current_location));
        data.add(getResources().getString(R.string.notification));
        data.add(getResources().getString(R.string.show_on_widget));
        data.add(getResources().getString(R.string.refresh_when_app_opens));
        data.add(getResources().getString(R.string.weather_alerts));
        data.add(getResources().getString(R.string.add_weather_icon));
        data.add(getResources().getString(R.string.customization_service));
        data.add(getResources().getString(R.string.about_weather));
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        settings_list.setLayoutManager(linearLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(data, (int position) -> {
        });
        settings_list.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
