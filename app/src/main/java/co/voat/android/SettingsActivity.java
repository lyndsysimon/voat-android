package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.api.UserPreferencesResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.User;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Jawn on 6/11/2015.
 */
public class SettingsActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    @InjectView(R.id.setting_username) TextView userName;
    @InjectView(R.id.setting_enableAdultContent)CheckBox adultContent;
    @InjectView(R.id.setting_enableNightMode)CheckBox nightMode;
    @InjectView(R.id.setting_openLinkNewWindow)CheckBox openLinkNewView;
    @InjectView(R.id.setting_publicyDisplaySubscriptions)CheckBox displaySubscription;
    @InjectView(R.id.setting_publicyDisplyVotes)CheckBox displayVote;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    User.Preferences userPreference;

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private final Callback<UserPreferencesResponse> preferencesResponseCallback = new Callback<UserPreferencesResponse>() {
        @Override
        public void success(UserPreferencesResponse userPreferencesResponse, Response response) {
            if(userPreferencesResponse.success)
            {
                userPreference = userPreferencesResponse.data;
            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        toolbar.setTitle(getString(R.string.settings));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);

        //VoatClient.instance().getUserPreferences(preferencesResponseCallback);
        createDummyUserPreference();
        setUI();
    }

    void setUI()
    {
        userName.setText(userPreference.getUserName());

        if(userPreference.isEnableAdultContent())
            adultContent.setChecked(true);
        else
            adultContent.setChecked(false);

        if(userPreference.isEnableNightMode())
            nightMode.setChecked(true);
        else
            nightMode.setChecked(false);

        if(userPreference.isOpenLinkNewWindow())
            openLinkNewView.setChecked(true);
        else
            openLinkNewView.setChecked(false);

        if(userPreference.isPubliclyDisplaySubscriptions())
            displaySubscription.setChecked(true);
        else
            displaySubscription.setChecked(false);

        if(userPreference.isPubliclyDisplayVotes())
            displaySubscription.setChecked(true);
        else
            displaySubscription.setChecked(false);
    }

    void createDummyUserPreference()
    {
        userPreference = new User.Preferences();
        userPreference.setUserName("Htoo Aung Win");
        userPreference.setEnableNightMode(false);
        userPreference.setOpenLinkNewWindow(false);
        userPreference.setEnableAdultContent(true);
        userPreference.setPubliclyDisplayVotes(true);
        userPreference.setPubliclyDisplaySubscriptions(false);
    }
}