package com.expoagro.expoagrobrasil.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.expoagro.expoagrobrasil.controller.AnunciosActivity;
import com.expoagro.expoagrobrasil.controller.LoginActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

/**
 * Created by Fabricio on 6/21/2017.
 */

public class GoogleSignIn {

    private static final int RC_SIGN_IN = 9001;

    public static void firebaseAuthWithGoogle(FirebaseAuth mAuth, final Activity activity, final GoogleSignInAccount acct) {
        System.out.println("firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            System.out.println("signInWithCredential. " + "Not Successful." );
                        } else {
                            Intent it = new Intent(activity, AnunciosActivity.class);
                            activity.startActivity(it);
                            activity.finish();
                        }
                        // ...
                    }
                });
    }

    public static void handleSignInResult(TextView mStatusTextView, Activity activity, GoogleSignInResult result) {
        System.out.println("handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
       //     mStatusTextView.setText(activity.getString(R.string.signed_in_fmt, acct.getDisplayName()));
            System.out.println("Google Sign In:Success");
        } else {
            // Signed out, show unauthenticated UI.
            System.out.println("false");
        }
    }

    public static void signIn(GoogleApiClient mGoogleApiClient, Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public static void signOut(final Activity activity, GoogleApiClient mGoogleApiClient) {
        if(mGoogleApiClient.isConnected()) { // Conectado pelo Google
            FirebaseAuth.getInstance().getCurrentUser().delete();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Intent it = new Intent(activity, LoginActivity.class);
                            activity.startActivity(it);
                            activity.finish();
                        }
                    });
        } else { // Conectado pelo App
            FirebaseAuth.getInstance().signOut();
            Intent it = new Intent(activity, LoginActivity.class);
            activity.startActivity(it);
            activity.finish();
        }
        System.out.println("saiu");
    }

    public static void getInfo() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                System.out.println(email);
            }
        } else {
            System.out.println("O usuário não está logado.");
        }
    }
}
