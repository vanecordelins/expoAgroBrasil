package com.expoagro.expoagrobrasil.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.expoagro.expoagrobrasil.controller.InicialArrobaActivity;
import com.expoagro.expoagrobrasil.controller.LoginActivity;
import com.expoagro.expoagrobrasil.controller.CompletarCadastroActivity;
import com.expoagro.expoagrobrasil.controller.MenuActivity;
import com.expoagro.expoagrobrasil.dao.UserDAO;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Fabricio on 6/21/2017.
 */

public class GoogleSignIn {

    private static final int RC_SIGN_IN = 9001;

    public static void firebaseAuthWithGoogle(FirebaseAuth mAuth, final Activity activity, final GoogleSignInAccount acct, final ProgressDialog dialog) {
        System.out.println("firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                System.out.println("signInWithCredential:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Toast.makeText(activity, "Sem conexão à internet.", Toast.LENGTH_SHORT).show();
                    System.out.println("signInWithCredential. " + "Not Successful." );
                    dialog.dismiss();
                } else {

                    final String uid = task.getResult().getUser().getUid();

                    UserDAO.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getMessage());
                            dialog.dismiss();
                        }

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                if (user.getKey().equals(uid)) {
                                    Intent it = new Intent(activity, MenuActivity.class);
                                    activity.startActivity(it);
                                    activity.finish();
                                    return;
                                }
                            }
                            Intent it = new Intent(activity, CompletarCadastroActivity.class);
                            activity.startActivity(it);
                            activity.finish();
                        }
                    });
                }
            }
        });
    }

    public static void signIn(GoogleApiClient mGoogleApiClient, Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public static void signOut(final Activity activity, GoogleApiClient mGoogleApiClient) {
        if(mGoogleApiClient.isConnected()) { // Conectado pelo Google
            System.out.println("desconectando google account");
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            FirebaseAuth.getInstance().signOut();
                            //Intent it = new Intent(activity, InicialArrobaActivity.class);
                            //activity.startActivity(it);
                            activity.finish();
                        }
                    });
        } else { // Conectado pelo App
            System.out.println("desconectando firebase account");
            FirebaseAuth.getInstance().signOut();
            //Intent it = new Intent(activity, InicialArrobaActivity.class);
            //activity.startActivity(it);
            activity.finish();
        }
        System.out.println("saiu");
    }

}
