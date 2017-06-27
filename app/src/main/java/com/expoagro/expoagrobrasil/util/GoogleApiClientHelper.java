package com.expoagro.expoagrobrasil.util;

import android.app.Activity;

import com.expoagro.expoagrobrasil.R;
import com.expoagro.expoagrobrasil.controller.LoginActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Fabricio on 6/26/2017.
 */

public class GoogleApiClientHelper {

    private static GoogleApiClientHelper instance;
    private GoogleApiClient mGoogleApiClient;

    private static class GoogleHelperHolder {
        private static final GoogleApiClientHelper HELPER = new GoogleApiClientHelper();
    }
    private GoogleApiClientHelper() {

    }

    public static GoogleApiClientHelper getInstance() {
        return GoogleHelperHolder.HELPER;
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        mGoogleApiClient = googleApiClient;
    }
}
